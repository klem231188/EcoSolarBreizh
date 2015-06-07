package bzh.eco.solar.thread;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.service.BluetoothService;

public class BluetoothInputProcessingThread extends Thread {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "InputProcessing";

    public static final int FRAME_SIZE = 8;

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private Handler mHandler;

    private BluetoothSocket mBluetoothSocket;

    private boolean mStopped;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public BluetoothInputProcessingThread(Handler handler, BluetoothSocket bluetoothSocket) {
        super();
        mHandler = handler;
        mBluetoothSocket = bluetoothSocket;
        mStopped = false;
    }

    // -------------------------------------------------------------------------------------
    // Section : Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void run() {
        try {
            // Lecture du flux d'entrée
            String charsetName = "US-ASCII";
            InputStreamReader inputStreamReader = new InputStreamReader(mBluetoothSocket.getInputStream(), charsetName);
            BufferedReader reader = new BufferedReader(inputStreamReader, 8);
            //TODO : Séparer le thread de lecture de flux du/des thread(s) de traitement(s) ??
            char[] buffer = new char[FRAME_SIZE];
            int nbCharactersRead;
            int offset = 0;
            try {
                while (!mStopped && (nbCharactersRead = reader.read(buffer, offset, FRAME_SIZE - offset)) != -1) {
                    offset += nbCharactersRead;
                    if (offset < FRAME_SIZE) {
                        Log.e(TAG, "ATTENTION : offset = " + offset + " < FRAME_SIZE");
                        continue;
                    } else if (offset == FRAME_SIZE) {
                        Log.i(TAG, "offset == FRAME_SIZE");
                        offset = 0;
                        BluetoothFrame bluetoothFrame = BluetoothFrame.makeInstance(buffer);

                        Message msg = Message.obtain(mHandler, BluetoothService.BLUETOOTH_FRAME_PROCESSED, bluetoothFrame);
                        msg.sendToTarget();
                    } else {
                        Log.e(TAG, "ATTENTION : offset = " + offset + " > FRAME_SIZE");
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            Message msg = Message.obtain(mHandler, BluetoothService.BLUETOOTH_SOCKET_DISCONNECTED);
            msg.sendToTarget();
        }
    }

    public void quit() {
        mStopped = true;
    }
}