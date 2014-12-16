package bzh.eco.solar.model;

import java.io.Serializable;

/**
 * DTO representing a bluetooth frame
 *
 * @author : Clément.Tréguer
 */
public class BluetoothFrame implements Serializable {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String BLUETOOTH_FRAME_SOLAR_PANEL = "BLUETOOTH_FRAME_SOLAR_PANEL";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private char[] mOriginalID;

    private char[] mOriginalData;

    private int mID;

    private double mData;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    protected BluetoothFrame() {
        mOriginalID = new char[2];
        mOriginalData = new char[4];
    }

    public static BluetoothFrame makeInstance(char[] buffer) {
        BluetoothFrame bluetoothFrame = new BluetoothFrame();

        bluetoothFrame.mOriginalID[0] = buffer[0];
        bluetoothFrame.mOriginalID[1] = buffer[1];

        bluetoothFrame.mID = 0;
        if (Character.isDigit(bluetoothFrame.mOriginalID[0]) && Character.isDigit(bluetoothFrame.mOriginalID[0])) {
            bluetoothFrame.mID = Character.getNumericValue(bluetoothFrame.mOriginalID[0]) * 10 + Character.getNumericValue(bluetoothFrame.mOriginalID[1]);
        }

        bluetoothFrame.mOriginalData[0] = buffer[2];
        bluetoothFrame.mOriginalData[1] = buffer[3];
        bluetoothFrame.mOriginalData[2] = buffer[4];
        bluetoothFrame.mOriginalData[3] = buffer[5];

        bluetoothFrame.mData = 0;

        return bluetoothFrame;
    }

    // -------------------------------------------------------------------------------------
    // Section : Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("ID = [")
                .append(Character.isDigit(mOriginalID[0]) ? mOriginalID[0] : '.')
                .append(Character.isDigit(mOriginalID[1]) ? mOriginalID[1] : '.')
                .append("] - FRAME = [")
                .append(Character.isDigit(mOriginalData[0]) ? mOriginalData[0] : '.')
                .append(Character.isDigit(mOriginalData[1]) ? mOriginalData[1] : '.')
                .append(Character.isDigit(mOriginalData[2]) ? mOriginalData[2] : '.')
                .append(Character.isDigit(mOriginalData[3]) ? mOriginalData[3] : '.')
                .append("]")
        ;

        return ret.toString();
    }

    // -------------------------------------------------------------------------------------
    // Section : Setter(s)/Getter(s)
    // -------------------------------------------------------------------------------------
    public char[] getOriginalData() {
        return mOriginalData;
    }

    public int getID() {
        return mID;
    }

    public double getData() {
        return mData;
    }

    public void setData(double data) {
        mData = data;
    }
}
