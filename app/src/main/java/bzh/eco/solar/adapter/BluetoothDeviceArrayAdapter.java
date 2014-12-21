package bzh.eco.solar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import bzh.eco.solar.R;
import bzh.eco.solar.model.bluetooth.BluetoothDeviceWrapper;

/**
 * @author : Clément.Tréguer
 */
public class BluetoothDeviceArrayAdapter extends ArrayAdapter<BluetoothDeviceWrapper> {

    private BluetoothDeviceWrapper mActiveBluetoothDeviceWrapper;

    public BluetoothDeviceArrayAdapter(Context context, int resource) {
        super(context, resource);
        mActiveBluetoothDeviceWrapper = null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_bluetooth_row, null);
        }

        BluetoothDeviceWrapper device = getItem(position);

        ImageView connectionState = (ImageView) v.findViewById(R.id.connection_state);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView address = (TextView) v.findViewById(R.id.address);

        if (device != null) {
            switch (device.getState()) {
                case CONNECTED:
                    connectionState.setImageDrawable(getContext().getResources().getDrawable(R.drawable.green_button));
                    break;
                case CONNECTING:
                    connectionState.setImageDrawable(getContext().getResources().getDrawable(R.drawable.orange_button));
                    break;
                case DISCONNECTING:
                    connectionState.setImageDrawable(getContext().getResources().getDrawable(R.drawable.orange_button));
                    break;
                case DISCONNECTED:
                    connectionState.setImageDrawable(getContext().getResources().getDrawable(R.drawable.red_button));
                    break;
                default:
                    break;
            }
            name.setText(device.getBluetoothDevice().getName());
            address.setText(String.valueOf(device.getBluetoothDevice().getAddress()));
        }

        return v;
    }

    public boolean hasNoBluetoothDeviceSelected() {
        return mActiveBluetoothDeviceWrapper == null;
    }

    public void setActiveBluetoothDeviceWrapper(BluetoothDeviceWrapper activeBluetoothDeviceWrapper) {
        mActiveBluetoothDeviceWrapper = activeBluetoothDeviceWrapper;
    }

    public BluetoothDeviceWrapper getActiveBluetoothDeviceWrapper() {
        return mActiveBluetoothDeviceWrapper;
    }
}