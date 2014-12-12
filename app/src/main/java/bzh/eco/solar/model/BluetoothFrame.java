package bzh.eco.solar.model;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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

    public static final String ID = "ID";

    public static final String DATA = "DATA";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private Context mContext;

    public char[] id;

    public char[] data;

    List<Integer> solarPanelIDs = Arrays.asList(51, 52, 53, 54, 55);

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    private BluetoothFrame() {
        id = new char[2];
        data = new char[4];
    }

    public static BluetoothFrame makeInstance(Context context, char[] buffer) {
        BluetoothFrame bluetoothFrame = new BluetoothFrame();

        bluetoothFrame.mContext = context;

        bluetoothFrame.id[0] = buffer[0];
        bluetoothFrame.id[1] = buffer[1];

        bluetoothFrame.data[0] = buffer[2];
        bluetoothFrame.data[1] = buffer[3];
        bluetoothFrame.data[2] = buffer[4];
        bluetoothFrame.data[3] = buffer[5];

        return bluetoothFrame;
    }

    // -------------------------------------------------------------------------------------
    // Section : Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("ID = [")
                .append(Character.isDigit(id[0]) ? id[0] : '.')
                .append(Character.isDigit(id[1]) ? id[1] : '.')
                .append("] - FRAME = [")
                .append(Character.isDigit(data[0]) ? data[0] : '.')
                .append(Character.isDigit(data[1]) ? data[1] : '.')
                .append(Character.isDigit(data[2]) ? data[2] : '.')
                .append(Character.isDigit(data[3]) ? data[3] : '.')
                .append("]")
        ;

        return ret.toString();
    }

    private boolean isSolarPanelFrame() {
        boolean solarPanel = false;

        if (Character.isDigit(id[0]) && Character.isDigit(id[0])) {
            int decodedID = Character.getNumericValue(id[0]) * 10 + Character.getNumericValue(id[1]);
            if (solarPanelIDs.contains(decodedID)) {
                solarPanel = true;
            }
        }

        return solarPanel;
    }

    public void process() {
        // TODO better
        if (isSolarPanelFrame()) {
            Intent processedIntent = new Intent(BLUETOOTH_FRAME_SOLAR_PANEL);
            int decodedID = Character.getNumericValue(id[0]) * 10 + Character.getNumericValue(id[1]);
            processedIntent.putExtra(ID, decodedID);
            processedIntent.putExtra(DATA, data);
            mContext.sendBroadcast(processedIntent);
        }
    }
}
