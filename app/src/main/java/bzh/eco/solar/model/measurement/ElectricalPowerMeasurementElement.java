package bzh.eco.solar.model.measurement;

import android.util.Log;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public class ElectricalPowerMeasurementElement extends AbstractMeasurementElement {

    private static final String TAG = "ElectricalPowerMeasurementElement";

    private double electricalPower;

    public ElectricalPowerMeasurementElement(int id, String meaning) {
        super(id, meaning, Measurement.ELECTRICAL_POWER);
        electricalPower = 0;
    }

    public double getElectricalPower() {
        return electricalPower;
    }

    @Override
    public void update(BluetoothFrame frame) {
        Log.i(TAG, getType() + " update(" + frame + ")");

        char[] originalData = frame.getOriginalData();
        char decade = originalData[0];
        char unit = originalData[1];
        char tenth = originalData[2];
        char hundredth = originalData[3];

        // Rule 1)
        if (unit == 0x00) {
            unit = decade;
            decade = '0';
        }
        // Rule 2)
        if (hundredth == 0x00) {
            hundredth = tenth;
            tenth = '0';
        }
        // Rule 3)
        if (Character.isDigit(decade)
                && Character.isDigit(unit)
                && Character.isDigit(tenth)
                && Character.isDigit(hundredth)) {

            electricalPower = Character.getNumericValue(decade) * 10;
            electricalPower += Character.getNumericValue(unit);
            electricalPower += Character.getNumericValue(tenth) * 0.1;
            electricalPower += Character.getNumericValue(hundredth) * 0.01;
        }
    }

    @Override
    public String toString() {
        return "ElectricalPowerMeasurementElement{" +
                "electricalPower=" + electricalPower +
                "} " + super.toString();
    }
}
