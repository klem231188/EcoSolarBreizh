package bzh.eco.solar.manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public class BluetoothFrameManagers {

    private static BluetoothFrameManagers instance = null;

    private List<BluetoothFrameManager> mManagers = null;

    private BluetoothFrameManagers() {
        mManagers = new ArrayList<BluetoothFrameManager>();
        mManagers.add(SolarPanelElecticalPowerManager.getInstance());
    }

    public static BluetoothFrameManagers getInstance() {
        if (instance == null) {
            instance = new BluetoothFrameManagers();
        }

        return instance;
    }

    public void processFrame(Context context, BluetoothFrame frame) {
        for (BluetoothFrameManager manager : mManagers) {
            if (manager.isFrameInMyScope(frame)) {
                manager.processFrame(frame);
                // TODO use LocalBroadcast instead
                manager.sendBroadcast(context, frame);
                break;
            }
        }
    }
}
