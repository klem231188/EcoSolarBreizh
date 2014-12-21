package bzh.eco.solar.thread;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import bzh.eco.solar.model.bluetooth.BluetoothDeviceWrapper;
import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
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
    private Context mContext;

    private BluetoothDeviceWrapper mDeviceBluetoothWrapper;

    private BluetoothSocket mBluetoothSocket;

    private InputStream mInputStream;

    private OutputStream mOutputStream;

    private boolean mStopped;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public BluetoothProcessingThread(Context context, BluetoothDeviceWrapper device) {
        super();
        try {
            mContext = context;
            mDeviceBluetoothWrapper = device;
            mBluetoothSocket = mDeviceBluetoothWrapper.getBluetoothDevice().createRfcommSocketToServiceRecord(BluetoothService.BLUETOOTH_UUID);
            mInputStream = mBluetoothSocket.getInputStream();
            mOutputStream = mBluetoothSocket.getOutputStream();
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
            mBluetoothSocket.connect();

            // Le device est désormais connecté
            Intent disconnectedIntent = new Intent(BluetoothService.ACTION_BLUETOOTH_SOCKET_CONNECTED);
            mContext.sendBroadcast(disconnectedIntent);

            // Lecture du flux d'entrée
            String charsetName = "US-ASCII";
            InputStreamReader inputStreamReader = new InputStreamReader(mInputStream, charsetName);
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
                        Car.getInstance().update(bluetoothFrame, mContext);
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
                mBluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
            Intent disconnectedIntent = new Intent(BluetoothService.ACTION_BLUETOOTH_SOCKET_DISCONNECTED);
            mContext.sendBroadcast(disconnectedIntent);
        }
    }

    @Deprecated
    public void write(byte[] bytes) {
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
        }
    }

    public void stopMySelf() {
        mStopped = true;
    }
}
