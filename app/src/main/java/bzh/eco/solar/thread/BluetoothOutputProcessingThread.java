package bzh.eco.solar.thread;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import bzh.eco.solar.model.bluetooth.commands.Command;

public class BluetoothOutputProcessingThread extends Thread {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "OutputProcessing";

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

        mHandler = new OutputProcessingHandler();
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

    private class OutputProcessingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case BLUETOOTH_FRAME_PROCESSED: {
//                    BluetoothFrame bluetoothFrame = (BluetoothFrame) msg.obj;
//                    Car.getInstance().update(bluetoothFrame, BluetoothService.this);
//
//                    break;
//                }
//            }
            try {
                Command command = (Command) msg.obj;

                //char[] command = {'+', '+', '1', '0', '0', '0', '-', '-'};
                OutputStream outputStream = mBluetoothSocket.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                outputStreamWriter.write(command.getValue());
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}