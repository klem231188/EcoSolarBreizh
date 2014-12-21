package bzh.eco.solar.model.car.elements;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.AbstractMeasurementElement;
import bzh.eco.solar.model.measurement.ElectricalPowerMeasurementElement;
import bzh.eco.solar.model.measurement.SpeedMeasurementElement;
import bzh.eco.solar.model.measurement.TemperatureMeasurementElement;

/**
 * @author : Clément.Tréguer
 */
public class Motors implements Car.CarElement {

    private static final String TAG = "Motors";

    private static Motors mInstance = null;

    private List<AbstractMeasurementElement> mMeasurementElements = null;

    private List<ElectricalPowerMeasurementElement> mElectricalPowerMeasurementElements = null;

    private List<TemperatureMeasurementElement> mTemperatureMeasurementElements = null;

    private List<SpeedMeasurementElement> mSpeedMeasurementElements = null;

    public static Motors getInstance() {
        if (mInstance == null) {
            mInstance = new Motors();
            mInstance.init();
        }

        return mInstance;
    }

    private void init() {
        initElectricalPowerMeasurementElements();
        initTemperatureMeasurementElements();
        initSpeedMeasurementElements();

        mMeasurementElements = new ArrayList<AbstractMeasurementElement>();
        mMeasurementElements.addAll(mElectricalPowerMeasurementElements);
        mMeasurementElements.addAll(mTemperatureMeasurementElements);
        mMeasurementElements.addAll(mSpeedMeasurementElements);
    }

    private void initTemperatureMeasurementElements() {
        mTemperatureMeasurementElements = new ArrayList<TemperatureMeasurementElement>();

        mTemperatureMeasurementElements.add(new TemperatureMeasurementElement(65, "Température moteur droit"));
        mTemperatureMeasurementElements.add(new TemperatureMeasurementElement(66, "Température moteur gauche"));
    }

    private void initElectricalPowerMeasurementElements() {
        mElectricalPowerMeasurementElements = new ArrayList<ElectricalPowerMeasurementElement>();

        mElectricalPowerMeasurementElements.add(new ElectricalPowerMeasurementElement(54, "Courant moteur droit"));
        mElectricalPowerMeasurementElements.add(new ElectricalPowerMeasurementElement(55, "Courant moteur gauche"));
    }

    private void initSpeedMeasurementElements() {
        mSpeedMeasurementElements = new ArrayList<SpeedMeasurementElement>();

        mSpeedMeasurementElements.add(new SpeedMeasurementElement(61, "Nombre de tours moteur droit"));
        mSpeedMeasurementElements.add(new SpeedMeasurementElement(62, "Nombre de tours moteur gauche"));
    }

    @Override
    public boolean isFrameAccepted(BluetoothFrame frame) {
        for (AbstractMeasurementElement measurementElement : mMeasurementElements) {
            if (frame.getID() == measurementElement.getId()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Car.ElementType getType() {
        return Car.ElementType.MOTOR;
    }

    @Override
    public void update(BluetoothFrame frame, Context context) {
        Log.i(TAG, "update(" + frame.toString() + ")");

        for (AbstractMeasurementElement measurementElement : mMeasurementElements) {
            if (frame.getID() == measurementElement.getId()) {
                measurementElement.update(frame);

                Intent intent = new Intent("VALUE_CHANGED");
                intent.putExtra("CAR_ELEMENT_TYPE", getType());
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

    public List<SpeedMeasurementElement> getSpeedMeasurementElements() {
        return mSpeedMeasurementElements;
    }
}
