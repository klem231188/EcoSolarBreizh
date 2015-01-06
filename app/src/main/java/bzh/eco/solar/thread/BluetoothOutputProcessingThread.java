package bzh.eco.solar.thread;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class BluetoothOutputProcessingThread extends Thread {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "BluetoothOutputProcessingThread";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private Handler mHandler;

    private BluetoothSocket mBluetoothSocket;

    private Looper mLooper;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public BluetoothOutputProcessingThread(BluetoothSocket bluetoothSocket) {
        super();
        mBluetoothSocket = bluetoothSocket;
    }

    // -------------------------------------------------------------------------------------
    // Section : Method(s)
    // -------------------------------------------------------------------------------------
    public void run() {
        Looper.prepare();

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                Log.i(TAG, "handleMessage(" + msg.toString() + ")");
            }
        };

        mLooper = Looper.myLooper();

        Looper.loop();
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void quit() {
        if (mLooper != null) {
            mLooper.quitSafely();
        }
    }
}