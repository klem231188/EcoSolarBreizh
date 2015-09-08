package bzh.eco.solar.model.car.elements;

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

        mMeasurements = new ArrayList<>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
        mMeasurements.addAll(mTemperatureMeasurements);
        mMeasurements.addAll(mSpeedMeasurements);
    }

    private void initTemperatureMeasurements() {
        mTemperatureMeasurements = new ArrayList<>();

    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<>();
        // ID = 70 / COURANT_RESEAU
        Measurement COURANT_RESEAU = new Measurement.Builder()
                .setId(70)
                .setMeaning("COURANT RESEAU (Courant consommé par l'électronique)")
                .setType(Type.ELECTRICAL_POWER)
                .setUnity(Unity.MILLI_AMPERE)
                .setMaxValue(2000)
                .setConvertType(ConvertType.INTEGER)
                .createMeasurement();
        mElectricalPowerMeasurements.add(COURANT_RESEAU);
    }

    private void initSpeedMeasurements() {
        mSpeedMeasurements = new ArrayList<>();

        // ID = 23 / VITESSE REELLE
        Measurement VITESSE_REELLE = new Measurement.Builder()
                .setId(23)
                .setMeaning("VITESSE REELLE")
                .setType(Type.SPEED)
                .setUnity(Unity.KILOMETER_PER_HOUR)
                .setMaxValue(150)
                .setConvertType(ConvertType.INTEGER)
                .createMeasurement();
        mSpeedMeasurements.add(VITESSE_REELLE);
    }

    @Override
    public boolean accepts(BluetoothFrame frame) {
        for (Measurement measurement : mMeasurements) {
            if (frame.getId() == measurement.getID()) {
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
    public void update(BluetoothFrame frame) {
        for (Measurement measurement : mMeasurements) {
            if (frame.getId() == measurement.getID()) {
                measurement.update(frame);
                break;
            }
        }
    }

    @Override
    public List<Measurement> getAllMeasurements() {
        return mMeasurements;
    }
}
