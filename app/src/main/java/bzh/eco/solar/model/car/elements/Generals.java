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
public class Generals implements Car.CarElement {

    private static final String TAG = "Generals";

    private static Generals mInstance = null;

    private List<AbstractMeasurement> mMeasurements = null;

    private List<ElectricalPowerMeasurement> mElectricalPowerMeasurements = null;

    private List<TemperatureMeasurement> mTemperatureMeasurements = null;

    private List<SpeedMeasurement> mSpeedMeasurements = null;

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

        mMeasurements = new ArrayList<AbstractMeasurement>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
        mMeasurements.addAll(mTemperatureMeasurements);
        mMeasurements.addAll(mSpeedMeasurements);
    }

    private void initTemperatureMeasurements() {
        mTemperatureMeasurements = new ArrayList<TemperatureMeasurement>();

        mTemperatureMeasurements.add(new TemperatureMeasurement(80, "Température Cockpit", ConvertType.INTEGER));
        mTemperatureMeasurements.add(new TemperatureMeasurement(81, "Température Processeur RX63N", ConvertType.INTEGER));
    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<ElectricalPowerMeasurement>();

        mElectricalPowerMeasurements.add(new ElectricalPowerMeasurement(70, "Mesure du courant général consommé par l’électronique (12v et 5v) en mA", ConvertType.INTEGER));
    }

    private void initSpeedMeasurements() {
        mSpeedMeasurements = new ArrayList<SpeedMeasurement>();

        mSpeedMeasurements.add(new SpeedMeasurement(23, "Vitesse de la voiture", ConvertType.INTEGER));
        mSpeedMeasurements.add(new SpeedMeasurement(00, "Etat Régulateur", ConvertType.INTEGER));
        mSpeedMeasurements.add(new SpeedMeasurement(43, "Consigne de vitesse", ConvertType.INTEGER));
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
        return Car.ElementType.GENERAL;
    }

    @Override
    public void update(BluetoothFrame frame, Context context) {
        Log.i(TAG, "update(" + frame.toString() + ")");

        for (AbstractMeasurement measurement : mMeasurements) {
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
