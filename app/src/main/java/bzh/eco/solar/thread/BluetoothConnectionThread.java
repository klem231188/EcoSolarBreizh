package bzh.eco.solar.thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import bzh.eco.solar.model.bluetooth.BluetoothDeviceWrapper;
import bzh.eco.solar.service.BluetoothService;

public class BluetoothConnectionThread extends Thread {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "Connection";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private Handler mHandler;

    private BluetoothDeviceWrapper mDeviceWrapper;

    private BluetoothSocket mBluetoothSocket;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public BluetoothConnectionThread(Handler handler, BluetoothDeviceWrapper deviceWrapper) {
        super();
        mHandler = handler;
        mDeviceWrapper = deviceWrapper;
    }

    // -------------------------------------------------------------------------------------
    // Section : Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void run() {
        try {
            BluetoothDevice device = mDeviceWrapper.getBluetoothDevice();
            mBluetoothSocket = device.createRfcommSocketToServiceRecord(BluetoothService.BLUETOOTH_UUID);

            // This method will block until a connection is made or the connection fails
            mBluetoothSocket.connect();

            // Bluetooth device is now connected
            Message msg = Message.obtain(mHandler, BluetoothService.BLUETOOTH_SOCKET_CONNECTED, mBluetoothSocket);
            msg.sendToTarget();
        } catch (IOException e) {
            // From method createRfcommSocketToServiceRecord() :
            // IOException on error, for example Bluetooth not available, or insufficient permissions
            // From method connect() :
            // Error occurs during connection

            Log.e(TAG, e.toString());
            Message msg = Message.obtain(mHandler, BluetoothService.BLUETOOTH_SOCKET_DISCONNECTED, mBluetoothSocket);
            msg.sendToTarget();
        }
    }
}