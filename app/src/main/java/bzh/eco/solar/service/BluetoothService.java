package bzh.eco.solar.service;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;

import bzh.eco.solar.model.bluetooth.BluetoothDeviceWrapper;
import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.thread.BluetoothConnectionThread;
import bzh.eco.solar.thread.BluetoothInputProcessingThread;
import bzh.eco.solar.thread.BluetoothOutputProcessingThread;

public class BluetoothService extends Service {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "BluetoothService";

    public static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";

    public static final String ACTION_BLUETOOTH_SOCKET_DISCONNECTED = "ACTION_BLUETOOTH_SOCKET_DISCONNECTED";

    public static final String ACTION_BLUETOOTH_SOCKET_CONNECTED = "ACTION_BLUETOOTH_SOCKET_CONNECTED";

    public static final int BLUETOOTH_SOCKET_CONNECTED = 0;

    public static final int BLUETOOTH_SOCKET_DISCONNECTED = 1;

    public static final int BLUETOOTH_FRAME_PROCESSED = 0;

    public static final String ACTION_SEND_COMMAND = "ACTION_SEND_COMMAND";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private BluetoothSocket mBluetoothSocket;

    private ServiceBroadcastReceiver mReceiver;

    private BluetoothConnectionThread mConnectionThread;

    private ConnectionHandler mConnectionHandler;

    private BluetoothInputProcessingThread mInputProcessingThread;

    private InputProcessingHandler mInputProcessingHandler;

    private BluetoothOutputProcessingThread mOutputProcessingThread;

    private BluetoothDeviceWrapper mDevice;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public BluetoothService() {
        super();

        mConnectionHandler = new ConnectionHandler(new WeakReference<BluetoothService>(this));
        mInputProcessingHandler = new InputProcessingHandler(new WeakReference<BluetoothService>(this));
        mReceiver = new ServiceBroadcastReceiver();
    }

    // -------------------------------------------------------------------------------------
    // Section : @Override Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "BluetoothService onCreate ", Toast.LENGTH_SHORT).show();

        IntentFilter filter = new IntentFilter(BluetoothService.ACTION_SEND_COMMAND);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "BluetoothService onStartCommand ", Toast.LENGTH_SHORT).show();

        mDevice = intent.getParcelableExtra(EXTRA_DEVICE);

        mConnectionThread = new BluetoothConnectionThread(mConnectionHandler, mDevice);
        mConnectionThread.start();

        // TODO à vérifier...
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent disconnectedIntent = new Intent(BluetoothService.ACTION_BLUETOOTH_SOCKET_DISCONNECTED);
        sendBroadcast(disconnectedIntent);

        if (mBluetoothSocket != null) {
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

        if (mInputProcessingThread != null) {
            mInputProcessingThread.quit();
        }

        if (mOutputProcessingThread != null) {
            mOutputProcessingThread.quit();
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ConnectionHandler extends Handler {

        WeakReference<BluetoothService> mParent;

        public ConnectionHandler(WeakReference<BluetoothService> parent) {
            mParent = parent;
        }

        @Override
        public void handleMessage(Message msg) {
            BluetoothService parent = mParent.get();
            if (null != parent) {
                switch (msg.what) {
                    case BLUETOOTH_SOCKET_CONNECTED: {
                        Intent connectedIntent = new Intent(BluetoothService.ACTION_BLUETOOTH_SOCKET_CONNECTED);
                        parent.sendBroadcast(connectedIntent);

                        mBluetoothSocket = (BluetoothSocket) msg.obj;

                        mInputProcessingThread = new BluetoothInputProcessingThread(mInputProcessingHandler, mBluetoothSocket);
                        mInputProcessingThread.start();

                        mOutputProcessingThread = new BluetoothOutputProcessingThread(mBluetoothSocket);
                        mOutputProcessingThread.start();

                        break;
                    }
                    case BLUETOOTH_SOCKET_DISCONNECTED: {
                        Intent disconnectedIntent = new Intent(BluetoothService.ACTION_BLUETOOTH_SOCKET_DISCONNECTED);
                        parent.sendBroadcast(disconnectedIntent);

                        if (mBluetoothSocket != null) {
                            try {
                                mBluetoothSocket.close();
                                mBluetoothSocket = null;
                            } catch (IOException e) {
                                Log.e(TAG, e.toString());
                            }
                        }

                        break;
                    }
                }
            }
        }
    }

    private class InputProcessingHandler extends Handler {

        WeakReference<BluetoothService> mParent;

        public InputProcessingHandler(WeakReference<BluetoothService> parent) {
            mParent = parent;
        }

        @Override
        public void handleMessage(Message msg) {
            BluetoothService parent = mParent.get();
            if (null != parent) {
                switch (msg.what) {
                    case BLUETOOTH_FRAME_PROCESSED: {
                        BluetoothFrame bluetoothFrame = (BluetoothFrame) msg.obj;
                        Car.getInstance().update(bluetoothFrame, BluetoothService.this);

                        break;
                    }
                }
            }
        }
    }

    private class OutputProcessingHandler extends Handler {

        WeakReference<BluetoothService> mParent;

        public OutputProcessingHandler(WeakReference<BluetoothService> parent) {
            mParent = parent;
        }

        @Override
        public void handleMessage(Message msg) {
            BluetoothService parent = mParent.get();
            if (null != parent) {
                switch (msg.what) {
                    case BLUETOOTH_FRAME_PROCESSED: {
                        BluetoothFrame bluetoothFrame = (BluetoothFrame) msg.obj;
                        Car.getInstance().update(bluetoothFrame, BluetoothService.this);

                        break;
                    }
                }
            }
        }
    }

    private class ServiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothService.ACTION_SEND_COMMAND.equals(action)) {
                if (mOutputProcessingThread != null) {
                    if (mOutputProcessingThread.getHandler() != null) {
                        Message msg = Message.obtain(mOutputProcessingThread.getHandler());
                        msg.obj = intent.getSerializableExtra("COMMAND");
                        msg.sendToTarget();
                    }
                }
            }
        }
    }
}
