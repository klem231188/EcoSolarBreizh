package bzh.eco.solar.model.car.elements;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.Measurement;
import bzh.eco.solar.model.measurement.Measurement.ConvertType;
import bzh.eco.solar.model.measurement.Measurement.Type;
import bzh.eco.solar.model.measurement.Measurement.Unity;

/**
 * @author : Clément.Tréguer
 */
public class Generals implements Car.CarElement {

    private static final String TAG = "Generals";

    private static Generals mInstance = null;

    private List<Measurement> mMeasurements = null;

    private List<Measurement> mElectricalPowerMeasurements = null;

    private List<Measurement> mTemperatureMeasurements = null;

    private List<Measurement> mSpeedMeasurements = null;

    public static Generals getInstance() {
        if (mInstance == null) {
            mInstance = new Generals();
            mInstance.init();
        }

        return mInstance;
    }

    private void init() {
        initElectricalPowerMeasurements();
        initTemperatureMeasurements();
        initSpeedMeasurements();

        mMeasurements = new ArrayList<Measurement>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
        mMeasurements.addAll(mTemperatureMeasurements);
        mMeasurements.addAll(mSpeedMeasurements);
    }

    private void initTemperatureMeasurements() {
        mTemperatureMeasurements = new ArrayList<Measurement>();

    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<Measurement>();

    }

    private void initSpeedMeasurements() {
        mSpeedMeasurements = new ArrayList<Measurement>();

        mSpeedMeasurements.add(new Measurement(23, "Vitesse de la voiture", Type.SPEED, Unity.KILOMETER_PER_HOUR, 150, ConvertType.INTEGER));
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
        return Car.ElementType.GENERAL;
    }

    @Override
    public void update(BluetoothFrame frame, Context context) {
        Log.i(TAG, "update(" + frame.toString() + ")");

        for (Measurement measurement : mMeasurements) {
            if (frame.getID() == measurement.getID()) {
                measurement.update(frame);

                Intent intent = new Intent(getType().name());
                intent.putExtra("MEASUREMENT_TYPE", measurement.getType());
                intent.putExtra("MEASUREMENT_ELEMENT", measurement);
                context.sendBroadcast(intent);

                break;
            }
        }
    }
}
