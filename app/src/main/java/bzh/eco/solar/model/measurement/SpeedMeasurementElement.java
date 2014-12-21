package bzh.eco.solar.model.measurement;

import android.util.Log;

import java.util.Random;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public class SpeedMeasurementElement extends AbstractMeasurementElement {

    private static final String TAG = "SpeedMeasurementElement";

    private double speed;

    private Random mRandom;

    public SpeedMeasurementElement(int id, String meaning) {
        super(id, meaning, Measurement.SPEED);
        mRandom = new Random();
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "SpeedMeasurementElement{" +
                "speed=" + speed +
                ", mRandom=" + mRandom +
                "} " + super.toString();
    }

    @Override
    public void update(BluetoothFrame frame) {
        Log.i(TAG, getType() + " update(" + frame + ")");
        speed = mRandom.nextDouble() * 100;
    }
}
