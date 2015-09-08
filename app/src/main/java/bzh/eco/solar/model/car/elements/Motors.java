package bzh.eco.solar.model.car.elements;

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
        initSpeedMeasurements();

        mMeasurements = new ArrayList<>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
        mMeasurements.addAll(mSpeedMeasurements);
    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<>();

        // ID = 54 / COURANT MOTEUR DROIT
        Measurement COURANT_MOTEUR_DROIT = new Measurement.Builder()
                .setId(54)
                .setMeaning("COURANT MOTEUR DROIT")
                .setType(Measurement.Type.ELECTRICAL_POWER)
                .setUnity(Measurement.Unity.AMPERE)
                .setMaxValue(200)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();
        mElectricalPowerMeasurements.add(COURANT_MOTEUR_DROIT);

        // ID = 55 / COURANT MOTEUR GAUCHE
        Measurement COURANT_MOTEUR_GAUCHE = new Measurement.Builder()
                .setId(55)
                .setMeaning("COURANT MOTEUR GAUCHE")
                .setType(Measurement.Type.ELECTRICAL_POWER)
                .setUnity(Measurement.Unity.AMPERE)
                .setMaxValue(200)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();
        mElectricalPowerMeasurements.add(COURANT_MOTEUR_GAUCHE);
    }

    private void initSpeedMeasurements() {
        mSpeedMeasurements = new ArrayList<>();

        // ID = 61 / VITESSE_MOTEUR_DROIT
        Measurement VITESSE_MOTEUR_DROIT = new Measurement.Builder()
                .setId(61)
                .setMeaning("VITESSE MOTEUR DROIT")
                .setType(Measurement.Type.SPEED)
                .setUnity(Measurement.Unity.RPM)
                .setMaxValue(1000)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();
        mSpeedMeasurements.add(VITESSE_MOTEUR_DROIT);

        // ID = 62 / VITESSE_MOTEUR_GAUCHE
        Measurement VITESSE_MOTEUR_GAUCHE = new Measurement.Builder()
                .setId(62)
                .setMeaning("VITESSE MOTEUR GAUCHE")
                .setType(Measurement.Type.SPEED)
                .setUnity(Measurement.Unity.RPM)
                .setMaxValue(1000)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();
        mSpeedMeasurements.add(VITESSE_MOTEUR_GAUCHE);
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
        return Car.ElementType.MOTOR;
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

    public double getGlobalElectricalPower() {
        double gobalElectricalPower = 0.0;
        for (Measurement electricalPowerMeasurement : mElectricalPowerMeasurements) {
            gobalElectricalPower += electricalPowerMeasurement.getValue();
        }

        return gobalElectricalPower;
    }

    public List<Measurement> getElectricalPowerMeasurements() {
        return mElectricalPowerMeasurements;
    }

    public List<Measurement> getSpeedMeasurements() {
        return mSpeedMeasurements;
    }

    @Override
    public List<Measurement> getAllMeasurements() {
        return mMeasurements;
    }
}
