package bzh.eco.solar.model.car.elements;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.Measurement;

/**
 * @author : Clément.Tréguer
 */
public class Motors implements Car.CarElement {

    private static final String TAG = "Motors";

    private static Motors mInstance = null;

    private List<Measurement> mMeasurements = null;

    private List<Measurement> mElectricalPowerMeasurements = null;

    private List<Measurement> mTemperatureMeasurements = null;

    private List<Measurement> mSpeedMeasurements = null;

    public static Motors getInstance() {
        if (mInstance == null) {
            mInstance = new Motors();
            mInstance.init();
        }

        return mInstance;
    }

    private void init() {
        initElectricalPowerMeasurements();
        initTemperatureMeasurements();
        initSpeedMeasurements();

        mMeasurements = new ArrayList<>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
        mMeasurements.addAll(mTemperatureMeasurements);
        mMeasurements.addAll(mSpeedMeasurements);
    }

    private void initTemperatureMeasurements() {
        mTemperatureMeasurements = new ArrayList<>();

        mTemperatureMeasurements.add(Measurement.Builder.Motors.buildTemperatureMeasurement(65, "Température moteur droit"));
        mTemperatureMeasurements.add(Measurement.Builder.Motors.buildTemperatureMeasurement(66, "Température moteur gauche"));
    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<>();

        mElectricalPowerMeasurements.add(Measurement.Builder.Motors.buildElectricalMeasurement(54, "Courant moteur droit"));
        mElectricalPowerMeasurements.add(Measurement.Builder.Motors.buildElectricalMeasurement(55, "Courant moteur gauche"));
    }

    private void initSpeedMeasurements() {
        mSpeedMeasurements = new ArrayList<>();

        mSpeedMeasurements.add(Measurement.Builder.Motors.buildSpeedMeasurement(61, "Nombre de tours moteur droit"));
        mSpeedMeasurements.add(Measurement.Builder.Motors.buildSpeedMeasurement(62, "Nombre de tours moteur gauche"));
    }

    @Override
    public boolean isFrameAccepted(BluetoothFrame frame) {
        for (Measurement measurement : mMeasurements) {
            if (frame.getID() == measurement.getID()) {
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
    public void update(BluetoothFrame frame) {
        Log.i(TAG, "update(" + frame.toString() + ")");

        for (Measurement measurement : mMeasurements) {
            if (frame.getID() == measurement.getID()) {
                measurement.update(frame);

                Intent intent = new Intent(getType().name());
                intent.putExtra("MEASUREMENT_TYPE", measurement.getType());
                //context.sendBroadcast(intent);

                break;
            }
        }
    }

    public List<Measurement> getElectricalPowerMeasurements() {
        return mElectricalPowerMeasurements;
    }

    public List<Measurement> getTemperatureMeasurements() {
        return mTemperatureMeasurements;
    }

    public List<Measurement> getSpeedMeasurements() {
        return mSpeedMeasurements;
    }
}
