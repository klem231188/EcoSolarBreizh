package bzh.eco.solar.model.car.elements;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.Measurement;

/**
 * @author : Clément.Tréguer
 */
public class SolarPanels implements Car.CarElement {

    private static final String TAG = "SolarPanels";

    private static SolarPanels mInstance = null;

    private List<Measurement> mMeasurements = null;

    private List<Measurement> mElectricalPowerMeasurements = null;

    private List<Measurement> mTemperatureMeasurements = null;

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

        mMeasurements = new ArrayList<>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
        mMeasurements.addAll(mTemperatureMeasurements);
    }

    private void initTemperatureMeasurements() {
        mTemperatureMeasurements = new ArrayList<>();

        // ID = 11 / TEMPERATURE_PANNEAU_AVANT
        Measurement TEMPERATURE_PANNEAU_AVANT = new Measurement.Builder()
                .setId(11)
                .setMeaning("TEMPERATURE PANNEAU AVANT")
                .setType(Measurement.Type.TEMPERATURE)
                .setUnity(Measurement.Unity.CELSIUS)
                .setMaxValue(100)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();
        mTemperatureMeasurements.add(TEMPERATURE_PANNEAU_AVANT);

        // ID = 12 / TEMPERATURE_PANNEAU_MILIEU
        Measurement TEMPERATURE_PANNEAU_MILIEU = new Measurement.Builder()
                .setId(12)
                .setMeaning("TEMPERATURE PANNEAU MILIEU")
                .setType(Measurement.Type.TEMPERATURE)
                .setUnity(Measurement.Unity.CELSIUS)
                .setMaxValue(100)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();
        mTemperatureMeasurements.add(TEMPERATURE_PANNEAU_MILIEU);

        // ID = 13 / TEMPERATURE_PANNEAU_ARRIERE
        Measurement TEMPERATURE_PANNEAU_ARRIERE = new Measurement.Builder()
                .setId(13)
                .setMeaning("TEMPERATURE PANNEAU ARRIERE")
                .setType(Measurement.Type.TEMPERATURE)
                .setUnity(Measurement.Unity.CELSIUS)
                .setMaxValue(100)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();
        mTemperatureMeasurements.add(TEMPERATURE_PANNEAU_ARRIERE);
    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<>();

        // ID = 51 / COURANT_PANNEAU_AVANT
        Measurement COURANT_PANNEAU_AVANT = new Measurement.Builder()
                .setId(51)
                .setMeaning("COURANT PANNEAU AVANT")
                .setType(Measurement.Type.ELECTRICAL_POWER)
                .setUnity(Measurement.Unity.AMPERE)
                .setMaxValue(10)
                .setConvertType(Measurement.ConvertType.FLOAT)
                .createMeasurement();
        mElectricalPowerMeasurements.add(COURANT_PANNEAU_AVANT);

        // ID = 52 / COURANT_PANNEAU_MILIEU
        Measurement COURANT_PANNEAU_MILIEU = new Measurement.Builder()
                .setId(52)
                .setMeaning("COURANT PANNEAU MILIEU")
                .setType(Measurement.Type.ELECTRICAL_POWER)
                .setUnity(Measurement.Unity.AMPERE)
                .setMaxValue(10)
                .setConvertType(Measurement.ConvertType.FLOAT)
                .createMeasurement();
        mElectricalPowerMeasurements.add(COURANT_PANNEAU_MILIEU);

        // ID = 53 / COURANT_PANNEAU_ARRIERE
        Measurement COURANT_PANNEAU_ARRIERE = new Measurement.Builder()
                .setId(53)
                .setMeaning("COURANT PANNEAU ARRIERE")
                .setType(Measurement.Type.ELECTRICAL_POWER)
                .setUnity(Measurement.Unity.AMPERE)
                .setMaxValue(10)
                .setConvertType(Measurement.ConvertType.FLOAT)
                .createMeasurement();
        mElectricalPowerMeasurements.add(COURANT_PANNEAU_ARRIERE);

        // ID = 56 / COURANT_4E_PANNEAU_ARRIERE
        Measurement COURANT_4E_PANNEAU_ARRIERE = new Measurement.Builder()
                .setId(56)
                .setMeaning("COURANT 4E PANNEAU ARRIERE")
                .setType(Measurement.Type.ELECTRICAL_POWER)
                .setUnity(Measurement.Unity.AMPERE)
                .setMaxValue(10)
                .setConvertType(Measurement.ConvertType.FLOAT)
                .createMeasurement();
        mElectricalPowerMeasurements.add(COURANT_4E_PANNEAU_ARRIERE);

        // ID = 57 / COURANT_5E_PANNEAU_ARRIERE
        Measurement COURANT_5E_PANNEAU_ARRIERE = new Measurement.Builder()
                .setId(57)
                .setMeaning("COURANT 5E PANNEAU ARRIERE")
                .setType(Measurement.Type.ELECTRICAL_POWER)
                .setUnity(Measurement.Unity.AMPERE)
                .setMaxValue(10)
                .setConvertType(Measurement.ConvertType.FLOAT)
                .createMeasurement();
        mElectricalPowerMeasurements.add(COURANT_5E_PANNEAU_ARRIERE);

        // ID = 58 / COURANT_GLOBAL
        Measurement COURANT_GLOBAL = new Measurement.Builder()
                .setId(58)
                .setMeaning("COURANT GLOBAL")
                .setType(Measurement.Type.ELECTRICAL_POWER)
                .setUnity(Measurement.Unity.AMPERE)
                .setMaxValue(10)
                .setConvertType(Measurement.ConvertType.FLOAT)
                .createMeasurement();
        mElectricalPowerMeasurements.add(COURANT_GLOBAL);
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
        return Car.ElementType.SOLAR_PANEL;
    }

    @Override
    public void update(BluetoothFrame frame) {
        Log.i(TAG, "update(" + frame.toString() + ")");

        for (Measurement measurement : mMeasurements) {
            if (frame.getID() == measurement.getID()) {
                measurement.update(frame);
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

    @Override
    public List<Measurement> getAllMeasurements() {
        return mMeasurements;
    }
}
