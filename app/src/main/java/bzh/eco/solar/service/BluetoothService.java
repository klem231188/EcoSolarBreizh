package bzh.eco.solar.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.util.UUID;

import bzh.eco.solar.model.BluetoothDeviceWrapper;
import bzh.eco.solar.model.BluetoothFrame;
import bzh.eco.solar.thread.BluetoothProcessingThread;

public class BluetoothService extends Service {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";

    public static final String EXTRA_BLUETOOTH_FRAME = "EXTRA_BLUETOOTH_FRAME";

    public static final String ACTION_BLUETOOTH_FRAME_PROCESSED = "ACTION_BLUETOOTH_FRAME_PROCESSED";

    public static final String ACTION_BLUETOOTH_SOCKET_DISCONNECTED = "ACTION_BLUETOOTH_SOCKET_DISCONNECTED";

    public static final String ACTION_BLUETOOTH_SOCKET_CONNECTED = "ACTION_BLUETOOTH_SOCKET_CONNECTED";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private Handler mHandler;

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

        mHandler = new BluetoothServiceHandler();
        mBluetoothProcessingThread = null;
        mDevice = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "BluetoothService onStartCommand ", Toast.LENGTH_SHORT).show();

        mDevice = intent.getParcelableExtra(EXTRA_DEVICE);
        mBluetoothProcessingThread = new BluetoothProcessingThread(mHandler, mDevice);
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

    // -------------------------------------------------------------------------------------
    // Section : Inner Class
    // -------------------------------------------------------------------------------------
    public class BluetoothServiceHandler extends Handler {

        public static final int ACTION_BLUETOOTH_FRAME_PROCESSED = 1;

        public static final int ACTION_BLUETOOTH_SOCKET_DISCONNECTED = 2;

        public static final int ACTION_BLUETOOTH_SOCKET_CONNECTED = 3;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_BLUETOOTH_FRAME_PROCESSED:
                    BluetoothFrame frame = (BluetoothFrame) msg.obj;
                    Intent processedIntent = new Intent(BluetoothService.ACTION_BLUETOOTH_FRAME_PROCESSED);
                    processedIntent.putExtra(EXTRA_BLUETOOTH_FRAME, frame);
                    sendBroadcast(processedIntent);
                    break;
                case ACTION_BLUETOOTH_SOCKET_DISCONNECTED:
                    Intent disconnectedIntent = new Intent(BluetoothService.ACTION_BLUETOOTH_SOCKET_DISCONNECTED);
                    sendBroadcast(disconnectedIntent);
                    break;
                case ACTION_BLUETOOTH_SOCKET_CONNECTED:
                    Intent connectedIntent = new Intent(BluetoothService.ACTION_BLUETOOTH_SOCKET_CONNECTED);
                    sendBroadcast(connectedIntent);
                    break;
                default:
                    break;
            }
        }
    }
}
