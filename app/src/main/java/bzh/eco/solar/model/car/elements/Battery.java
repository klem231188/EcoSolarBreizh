package bzh.eco.solar.model.car.elements;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.Measurement;

/**
 * @author : Clément.Tréguer
 */
public class Battery implements Car.CarElement {

    private static final String TAG = "Battery";

    private static Battery mInstance = null;

    private List<Measurement> mMeasurements = null;

    private List<Measurement> mElectricalPowerMeasurements = null;


    public static Battery getInstance() {
        if (mInstance == null) {
            mInstance = new Battery();
            mInstance.init();
        }

        return mInstance;
    }

    private void init() {
        initElectricalPowerMeasurements();

        mMeasurements = new ArrayList<>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<>();

        // ID = 22 / CHARGE BATTERIE
        Measurement CHARGE_BATTERIE = new Measurement.Builder()
                .setId(22)
                .setMeaning("CHARGE BATTERIE")
                .setType(Measurement.Type.ELECTRICAL_POWER)
                .setUnity(Measurement.Unity.PERCENTAGE)
                .setMaxValue(100)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();
        mElectricalPowerMeasurements.add(CHARGE_BATTERIE);
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
        return Car.ElementType.BATTERY;
    }

    @Override
    public void update(BluetoothFrame frame) {
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
}
