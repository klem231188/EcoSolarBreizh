package bzh.eco.solar.manager;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public abstract class BluetoothFrameManager {

    protected List<Integer> mIDs = null;

    protected TYPE mType = null;

    public static final String ID = "ID";

    public static final String DATA = "DATA";

    public BluetoothFrameManager() {
        mIDs = new ArrayList<Integer>();
        mType = TYPE.UNKNOWN;
    }

    protected abstract void processFrame(BluetoothFrame frame);

    public boolean isFrameInMyScope(BluetoothFrame frame) {
        return mIDs.contains(frame.getID());
    }

    public void sendBroadcast(Context context, BluetoothFrame frame) {
        Intent processedIntent = new Intent(mType.name());
        processedIntent.putExtra(ID, frame.getID());
        processedIntent.putExtra(DATA, frame.getData());
        context.sendBroadcast(processedIntent);
    }

    public enum TYPE {
        UNKNOWN,
        SOLAR_PANEL_ELECTRICAL_POWER,
        SOLAR_PANEL_TEMPERATURE
    }

    public TYPE getType() {
        return mType;
    }
}
