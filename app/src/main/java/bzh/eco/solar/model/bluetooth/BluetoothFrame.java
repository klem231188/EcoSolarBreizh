package bzh.eco.solar.model.bluetooth;

import java.io.Serializable;

/**
 * DTO representing a bluetooth frame
 *
 * @author : Clément.Tréguer
 */
public class BluetoothFrame implements Serializable {

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private char[] mIdArray;

    private char[] mdataArray;

    private int mId;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    protected BluetoothFrame() {
        mIdArray = new char[2];
        mdataArray = new char[4];
    }

    public static BluetoothFrame makeInstance(char[] buffer) {
        BluetoothFrame bluetoothFrame = new BluetoothFrame();

        bluetoothFrame.mIdArray[0] = buffer[0];
        bluetoothFrame.mIdArray[1] = buffer[1];

        bluetoothFrame.mId = 0;
        if (Character.isDigit(bluetoothFrame.mIdArray[0]) && Character.isDigit(bluetoothFrame.mIdArray[0])) {
            bluetoothFrame.mId = Character.getNumericValue(bluetoothFrame.mIdArray[0]) * 10 + Character.getNumericValue(bluetoothFrame.mIdArray[1]);
        }

        bluetoothFrame.mdataArray[0] = buffer[2];
        bluetoothFrame.mdataArray[1] = buffer[3];
        bluetoothFrame.mdataArray[2] = buffer[4];
        bluetoothFrame.mdataArray[3] = buffer[5];

        return bluetoothFrame;
    }

    public int asInteger() {
        int value = 0;

        char[] dataArray = getDataArray();
        int multiple = 1;
        for (int i = dataArray.length - 1; i >= 0; i--) {
            char data = dataArray[i];
            if (Character.isDigit(data)) {
                value += Character.getNumericValue(data) * multiple;
                multiple *= 10;
            }
        }

        return value;
    }

    public double asDouble(){
        double value = 0.0;

        char[] originalData = getDataArray();
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

            value = Character.getNumericValue(decade) * 10;
            value += Character.getNumericValue(unit);
            value += Character.getNumericValue(tenth) * 0.1;
            value += Character.getNumericValue(hundredth) * 0.01;
        }

        return value;
    }


    // -------------------------------------------------------------------------------------
    // Section : Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public String toString() {

        return "ID = ["
                + (Character.isDigit(mIdArray[0]) ? mIdArray[0] : '.')
                + (Character.isDigit(mIdArray[1]) ? mIdArray[1] : '.')
                + "] - FRAME = ["
                + (Character.isDigit(mdataArray[0]) ? mdataArray[0] : '.')
                + (Character.isDigit(mdataArray[1]) ? mdataArray[1] : '.')
                + (Character.isDigit(mdataArray[2]) ? mdataArray[2] : '.')
                + (Character.isDigit(mdataArray[3]) ? mdataArray[3] : '.')
                + "]";
    }

    // -------------------------------------------------------------------------------------
    // Section : Setter(s)/Getter(s)
    // -------------------------------------------------------------------------------------
    public char[] getDataArray() {
        return mdataArray;
    }

    public int getId() {
        return mId;
    }
}
