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
import bzh.eco.solar.model.measurement.SpeedMeasurement;
import bzh.eco.solar.model.measurement.TemperatureMeasurement;

/**
 * @author : Clément.Tréguer
 */
public class Motors implements Car.CarElement {

    private static final String TAG = "Motors";

    private static Motors mInstance = null;

    private List<AbstractMeasurement> mMeasurements = null;

    private List<ElectricalPowerMeasurement> mElectricalPowerMeasurements = null;

    private List<TemperatureMeasurement> mTemperatureMeasurements = null;

    private List<SpeedMeasurement> mSpeedMeasurements = null;

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

        mMeasurements = new ArrayList<AbstractMeasurement>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
        mMeasurements.addAll(mTemperatureMeasurements);
        mMeasurements.addAll(mSpeedMeasurements);
    }

    private void initTemperatureMeasurements() {
        mTemperatureMeasurements = new ArrayList<TemperatureMeasurement>();

        mTemperatureMeasurements.add(new TemperatureMeasurement(65, "Température moteur droit", ConvertType.INTEGER));
        mTemperatureMeasurements.add(new TemperatureMeasurement(66, "Température moteur gauche", ConvertType.INTEGER));
    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<ElectricalPowerMeasurement>();

        mElectricalPowerMeasurements.add(new ElectricalPowerMeasurement(54, "Courant moteur droit", ConvertType.INTEGER));
        mElectricalPowerMeasurements.add(new ElectricalPowerMeasurement(55, "Courant moteur gauche", ConvertType.INTEGER));
    }

    private void initSpeedMeasurements() {
        mSpeedMeasurements = new ArrayList<SpeedMeasurement>();

        mSpeedMeasurements.add(new SpeedMeasurement(61, "Nombre de tours moteur droit", ConvertType.INTEGER));
        mSpeedMeasurements.add(new SpeedMeasurement(62, "Nombre de tours moteur gauche", ConvertType.INTEGER));
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
        return Car.ElementType.MOTOR;
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

    public List<SpeedMeasurement> getSpeedMeasurements() {
        return mSpeedMeasurements;
    }
}
