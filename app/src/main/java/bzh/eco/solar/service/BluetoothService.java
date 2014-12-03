package bzh.eco.solar.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import bzh.eco.solar.model.BluetoothFrame;
import bzh.eco.solar.thread.BluetoothProcessingThread;

public class BluetoothService extends Service {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "BluetoothService";

    public static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";

    public static final String REFRESH_BLUETOOTH_FRAME = "REFRESH_BLUETOOTH_FRAME";

    public static final String EXTRA_BLUETOOTH_FRAME = "EXTRA_BLUETOOTH_FRAME";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private Handler mHandler;

    private BluetoothProcessingThread mBluetoothProcessingThread;

    private BluetoothDevice mDevice;

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
        super.onDestroy();
        mBluetoothProcessingThread.stopMySelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // -------------------------------------------------------------------------------------
    // Section : Inner Class
    // -------------------------------------------------------------------------------------
    public class BluetoothServiceHandler extends Handler {

        public static final int BLUETOOTH_FRAME_PROCESSED = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BLUETOOTH_FRAME_PROCESSED:
                    BluetoothFrame frame = (BluetoothFrame) msg.obj;
                    Log.i(TAG, "BLUETOOTH_FRAME_PROCESSED " + frame.toString());
                    Intent processedIntent = new Intent(REFRESH_BLUETOOTH_FRAME);
                    processedIntent.putExtra(EXTRA_BLUETOOTH_FRAME, frame);
                    sendBroadcast(processedIntent);
                    break;
                default:
                    break;
            }
        }
    }
}
