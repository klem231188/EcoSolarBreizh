package bzh.eco.solar.model.measurement;

import android.util.Log;

import java.util.Random;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public class TemperatureMeasurementElement extends AbstractMeasurementElement {

    private static final String TAG = "TemperatureMeasurementElement";

    private Random mRandom;

    private double temperature;

    public TemperatureMeasurementElement(int id, String meaning) {
        super(id, meaning, Measurement.TEMPERATURE);
        temperature = 0;
        mRandom = new Random();
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "TemperatureMeasurementElement{" +
                "mRandom=" + mRandom +
                ", temperature=" + temperature +
                "} " + super.toString();
    }

    @Override
    public void update(BluetoothFrame frame) {
        Log.i(TAG, getType() + " update(" + frame + ")");
        temperature = mRandom.nextDouble() * 100;
    }
}
