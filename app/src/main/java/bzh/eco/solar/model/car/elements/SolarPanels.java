package bzh.eco.solar.model.car.elements;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.AbstractMeasurement;
import bzh.eco.solar.model.measurement.AbstractMeasurement.ConvertType;
import bzh.eco.solar.model.measurement.ElectricalPowerMeasurement;
import bzh.eco.solar.model.measurement.TemperatureMeasurement;

/**
 * @author : Clément.Tréguer
 */
public class SolarPanels implements Car.CarElement {

    private static final String TAG = "SolarPanels";

    private static SolarPanels mInstance = null;

    private List<AbstractMeasurement> mMeasurements = null;

    private List<ElectricalPowerMeasurement> mElectricalPowerMeasurements = null;

    private List<TemperatureMeasurement> mTemperatureMeasurements = null;

    public static SolarPanels getInstance() {
        if (mInstance == null) {
            mInstance = new SolarPanels();
            mInstance.init();
        }

        return mInstance;
    }

    private void init() {
        initElectricalPowerMeasurements();
        initTemperatureMeasurements();

        mMeasurements = new ArrayList<AbstractMeasurement>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
        mMeasurements.addAll(mTemperatureMeasurements);
    }

    private void initTemperatureMeasurements() {
        mTemperatureMeasurements = new ArrayList<TemperatureMeasurement>();

        mTemperatureMeasurements.add(new TemperatureMeasurement(11, "Température avant des panneaux", ConvertType.INTEGER));
        mTemperatureMeasurements.add(new TemperatureMeasurement(12, "Température milieu des panneaux", ConvertType.INTEGER));
        mTemperatureMeasurements.add(new TemperatureMeasurement(13, "Température arrière des panneaux", ConvertType.INTEGER));
    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<ElectricalPowerMeasurement>();

        mElectricalPowerMeasurements.add(new ElectricalPowerMeasurement(52, "Panneau 1 secteur 1-2", ConvertType.FLOAT));
        mElectricalPowerMeasurements.add(new ElectricalPowerMeasurement(51, "Panneau 2 secteur 3-4", ConvertType.FLOAT));
        mElectricalPowerMeasurements.add(new ElectricalPowerMeasurement(53, "Panneau 3 secteur 5-6", ConvertType.FLOAT));
        mElectricalPowerMeasurements.add(new ElectricalPowerMeasurement(56, "Panneau 4 secteur 7-8", ConvertType.FLOAT));
        mElectricalPowerMeasurements.add(new ElectricalPowerMeasurement(57, "Panneau 5 secteur 9-10", ConvertType.FLOAT));
        mElectricalPowerMeasurements.add(new ElectricalPowerMeasurement(58, "Courant global des panneaux", ConvertType.FLOAT));
    }

    @Override
    public boolean isFrameAccepted(BluetoothFrame frame) {
        for (AbstractMeasurement measurement : mMeasurements) {
            if (frame.getID() == measurement.getID()) {
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

        for (AbstractMeasurement measurement : mMeasurements) {
            if (frame.getID() == measurement.getID()) {
                measurement.update(frame);

                Intent intent = new Intent(getType().name());
                intent.putExtra("MEASUREMENT_TYPE", measurement.getType());
                context.sendBroadcast(intent);

                break;
            }
        }
    }

    public List<ElectricalPowerMeasurement> getElectricalPowerMeasurements() {
        return mElectricalPowerMeasurements;
    }

    public List<TemperatureMeasurement> getTemperatureMeasurements() {
        return mTemperatureMeasurements;
    }
}
