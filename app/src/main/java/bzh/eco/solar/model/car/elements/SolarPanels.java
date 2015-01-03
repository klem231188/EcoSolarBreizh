package bzh.eco.solar.model.car.elements;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.AbstractMeasurementElement;
import bzh.eco.solar.model.measurement.AbstractMeasurementElement.ConvertType;
import bzh.eco.solar.model.measurement.ElectricalPowerMeasurementElement;
import bzh.eco.solar.model.measurement.TemperatureMeasurementElement;

/**
 * @author : Clément.Tréguer
 */
public class SolarPanels implements Car.CarElement {

    private static final String TAG = "SolarPanels";

    private static SolarPanels mInstance = null;

    private List<AbstractMeasurementElement> mMeasurementElements = null;

    private List<ElectricalPowerMeasurementElement> mElectricalPowerMeasurementElements = null;

    private List<TemperatureMeasurementElement> mTemperatureMeasurementElements = null;

    public static SolarPanels getInstance() {
        if (mInstance == null) {
            mInstance = new SolarPanels();
            mInstance.init();
        }

        return mInstance;
    }

    private void init() {
        initElectricalPowerMeasurementElements();
        initTemperatureMeasurementElements();

        mMeasurementElements = new ArrayList<AbstractMeasurementElement>();
        mMeasurementElements.addAll(mElectricalPowerMeasurementElements);
        mMeasurementElements.addAll(mTemperatureMeasurementElements);
    }

    private void initTemperatureMeasurementElements() {
        mTemperatureMeasurementElements = new ArrayList<TemperatureMeasurementElement>();

        mTemperatureMeasurementElements.add(new TemperatureMeasurementElement(11, "Température avant des panneaux", ConvertType.INTEGER));
        mTemperatureMeasurementElements.add(new TemperatureMeasurementElement(12, "Température milieu des panneaux", ConvertType.INTEGER));
        mTemperatureMeasurementElements.add(new TemperatureMeasurementElement(13, "Température arrière des panneaux", ConvertType.INTEGER));
    }

    private void initElectricalPowerMeasurementElements() {
        mElectricalPowerMeasurementElements = new ArrayList<ElectricalPowerMeasurementElement>();

        mElectricalPowerMeasurementElements.add(new ElectricalPowerMeasurementElement(52, "Panneau 1 secteur 1-2", ConvertType.FLOAT));
        mElectricalPowerMeasurementElements.add(new ElectricalPowerMeasurementElement(51, "Panneau 2 secteur 3-4", ConvertType.FLOAT));
        mElectricalPowerMeasurementElements.add(new ElectricalPowerMeasurementElement(53, "Panneau 3 secteur 5-6", ConvertType.FLOAT));
        mElectricalPowerMeasurementElements.add(new ElectricalPowerMeasurementElement(56, "Panneau 4 secteur 7-8", ConvertType.FLOAT));
        mElectricalPowerMeasurementElements.add(new ElectricalPowerMeasurementElement(57, "Panneau 5 secteur 9-10", ConvertType.FLOAT));
        mElectricalPowerMeasurementElements.add(new ElectricalPowerMeasurementElement(58, "Courant global des panneaux", ConvertType.FLOAT));
    }

    @Override
    public boolean isFrameAccepted(BluetoothFrame frame) {
        for (AbstractMeasurementElement measurementElement : mMeasurementElements) {
            if (frame.getID() == measurementElement.getID()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Car.ElementType getType() {
        return Car.ElementType.SOLAR_PANEL;
    }

    @Override
    public void update(BluetoothFrame frame, Context context) {
        Log.i(TAG, "update(" + frame.toString() + ")");

        for (AbstractMeasurementElement measurementElement : mMeasurementElements) {
            if (frame.getID() == measurementElement.getID()) {
                measurementElement.update(frame);

                Intent intent = new Intent(getType().name());
                intent.putExtra("MEASUREMENT_TYPE", measurementElement.getType());
                context.sendBroadcast(intent);

                break;
            }
        }
    }

    public List<ElectricalPowerMeasurementElement> getElectricalPowerMeasurementElements() {
        return mElectricalPowerMeasurementElements;
    }

    public List<TemperatureMeasurementElement> getTemperatureMeasurementElements() {
        return mTemperatureMeasurementElements;
    }
}
