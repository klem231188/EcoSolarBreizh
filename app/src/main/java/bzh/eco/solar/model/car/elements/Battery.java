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
        mElectricalPowerMeasurements.add(new Measurement(22, "Charge batterie", Measurement.Type.ELECTRICAL_POWER, Measurement.Unity.PERCENTAGE, 1, Measurement.ConvertType.INTEGER));
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
