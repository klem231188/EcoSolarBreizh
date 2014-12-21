package bzh.eco.solar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.UUID;

import bzh.eco.solar.model.bluetooth.BluetoothDeviceWrapper;
import bzh.eco.solar.thread.BluetoothProcessingThread;

public class BluetoothService extends Service {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";

    public static final String ACTION_BLUETOOTH_SOCKET_DISCONNECTED = "ACTION_BLUETOOTH_SOCKET_DISCONNECTED";

    public static final String ACTION_BLUETOOTH_SOCKET_CONNECTED = "ACTION_BLUETOOTH_SOCKET_CONNECTED";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private BluetoothProcessingThread mBluetoothProcessingThread;

    private BluetoothDeviceWrapper mDevice;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public BluetoothService() {
        super();
    }

    // -------------------------------------------------------------------------------------
    // Section : @Override Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "BluetoothService onCreate ", Toast.LENGTH_SHORT).show();

        mBluetoothProcessingThread = null;
        mDevice = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "BluetoothService onStartCommand ", Toast.LENGTH_SHORT).show();

        mDevice = intent.getParcelableExtra(EXTRA_DEVICE);
        mBluetoothProcessingThread = new BluetoothProcessingThread(this, mDevice);
        mBluetoothProcessingThread.start();

        // TODO à vérifier...
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mBluetoothProcessingThread.stopMySelf();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
