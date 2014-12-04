package bzh.eco.solar.thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import bzh.eco.solar.model.BluetoothFrame;
import bzh.eco.solar.service.BluetoothService;

public class BluetoothProcessingThread extends Thread {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "BluetoothProcessingThread";

    public static final int FRAME_SIZE = 8;

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private Handler serviceHandler;

    private BluetoothSocket bluetoothSocket;

    private InputStream inputStream;

    private OutputStream outputStream;

    private boolean mStopped;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public BluetoothProcessingThread(Handler handler, BluetoothDevice device) {
        super();
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(BluetoothService.BLUETOOTH_UUID);
            serviceHandler = handler;
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
            mStopped = false;
        } catch (IOException e) {
            // TODO
        }
    }

    // -------------------------------------------------------------------------------------
    // Section : Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void run() {
        try {
            bluetoothSocket.connect();
            serviceHandler.obtainMessage(BluetoothService.BluetoothServiceHandler.ACTION_BLUETOOTH_SOCKET_CONNECTED).sendToTarget();

            String charsetName = "US-ASCII";
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
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
                        BluetoothFrame bluetoothFrame = processBluetoothFrame(buffer);
                        serviceHandler.obtainMessage(BluetoothService.BluetoothServiceHandler.ACTION_BLUETOOTH_FRAME_PROCESSED, bluetoothFrame).sendToTarget();
                    } else {
                        Log.e(TAG, "ATTENTION : offset = " + offset + " > FRAME_SIZE");
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Exception dans le thread : BluetoothTraitementThread (Name/Id) = (" + this.getName() + " / " + this.getId() + ")");
                Log.e(TAG, e.toString());
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                serviceHandler.obtainMessage(BluetoothService.BluetoothServiceHandler.ACTION_BLUETOOTH_SOCKET_DISCONNECTED).sendToTarget();
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private BluetoothFrame processBluetoothFrame(char[] buffer) {
        BluetoothFrame bluetoothFrame = new BluetoothFrame();

        bluetoothFrame.id[0] = buffer[0];
        bluetoothFrame.id[1] = buffer[1];

        bluetoothFrame.data[0] = buffer[2];
        bluetoothFrame.data[1] = buffer[3];
        bluetoothFrame.data[2] = buffer[4];
        bluetoothFrame.data[3] = buffer[5];

        return bluetoothFrame;
    }

    @Deprecated
    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
        }
    }

    public void stopMySelf(){
        mStopped = true;
    }
}
