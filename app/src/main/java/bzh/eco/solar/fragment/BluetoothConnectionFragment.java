package bzh.eco.solar.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import bzh.eco.solar.R;
import bzh.eco.solar.model.bluetooth.BluetoothDeviceWrapper;
import bzh.eco.solar.service.BluetoothService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link bzh.eco.solar.fragment.BluetoothConnectionFragment.BluetoothCallback} interface
 * to handle interaction events.
 * Use the {@link BluetoothConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothConnectionFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    private static final String TAG = "BluetoothConnectionFragment";

    private static final int BLUETOOTH_ENABLED = 1;

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private BluetoothAdapter mBluetoothAdapter;

    boolean firstTimeStarting;

    private BluetoothDeviceArrayAdapter mBluetoothDeviceArrayAdapter;

    private BroadcastReceiver mReceiver;

    private BluetoothCallback mListener;

    private LinearLayout mLoadingPanel;

    private ListView mPairedDevicesListview;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public static BluetoothConnectionFragment newInstance() {
        return new BluetoothConnectionFragment();
    }

    public BluetoothConnectionFragment() {
    }

    // -------------------------------------------------------------------------------------
    // Section : @Override Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (BluetoothCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        firstTimeStarting = true;
        mBluetoothDeviceArrayAdapter = new BluetoothDeviceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mReceiver = new BluetoothDeviceBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bluetooth_connection, container, false);

        mLoadingPanel = (LinearLayout) root.findViewById(R.id.loading_panel);
        mPairedDevicesListview = (ListView) root.findViewById(R.id.list_view_paired_devices);
        mPairedDevicesListview.setAdapter(mBluetoothDeviceArrayAdapter);
        mPairedDevicesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    BluetoothDeviceWrapper selectedDevice = mBluetoothDeviceArrayAdapter.getItem(position);
                    if (mBluetoothDeviceArrayAdapter.hasNoBluetoothDeviceSelected()) {
                        selectedDevice.setState(BluetoothDeviceWrapper.State.CONNECTING);
                        mBluetoothDeviceArrayAdapter.setActiveBluetoothDeviceWrapper(selectedDevice);
                        mBluetoothDeviceArrayAdapter.notifyDataSetChanged();
                        mListener.onBluetoothConnecting(selectedDevice);
                    } else {
                        BluetoothDeviceWrapper activeDevice = mBluetoothDeviceArrayAdapter.getActiveBluetoothDeviceWrapper();
                        if (selectedDevice == activeDevice) {
                            selectedDevice.setState(BluetoothDeviceWrapper.State.DISCONNECTING);
                            mBluetoothDeviceArrayAdapter.notifyDataSetChanged();
                            mListener.onBluetoothDisconnecting();
                        } else {
                            String activeDeviceName = activeDevice.getBluetoothDevice().getName();
                            Toast.makeText(getActivity(), "A bluetooth device is already active : " + activeDeviceName, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        mLoadingPanel.setVisibility(View.GONE);
        mPairedDevicesListview.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (firstTimeStarting) {
            firstTimeStarting = false;

            IntentFilter filter1 = new IntentFilter(BluetoothService.ACTION_BLUETOOTH_SOCKET_CONNECTED);
            IntentFilter filter2 = new IntentFilter(BluetoothService.ACTION_BLUETOOTH_SOCKET_DISCONNECTED);
            IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            IntentFilter filter4 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            IntentFilter filter5 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

            getActivity().registerReceiver(mReceiver, filter1);
            getActivity().registerReceiver(mReceiver, filter2);
            getActivity().registerReceiver(mReceiver, filter3);
            getActivity().registerReceiver(mReceiver, filter4);
            getActivity().registerReceiver(mReceiver, filter5);

            if (mBluetoothAdapter != null) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Log.i(TAG, "Bluetooth is not enabled. Going to ask user for enable it.");
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, BLUETOOTH_ENABLED);
                } else {
                    mBluetoothAdapter.startDiscovery();
                }
            } else {
                Log.e(TAG, "Bluetooth not supported");
            }
        } else {
            Log.i(TAG, "Fragment has already been started once");
            mPairedDevicesListview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BLUETOOTH_ENABLED) {
            mBluetoothAdapter.startDiscovery();
        }
    }

    // -------------------------------------------------------------------------------------
    // Section : Private Method(s)
    // -------------------------------------------------------------------------------------
    private void addPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice pairedDevice : pairedDevices) {
                mBluetoothDeviceArrayAdapter.add(BluetoothDeviceWrapper.newInstance(pairedDevice));
            }
        }
    }

    // -------------------------------------------------------------------------------------
    // Section : Inner Class(es)
    // -------------------------------------------------------------------------------------
    public interface BluetoothCallback {

        public void onBluetoothConnecting(BluetoothDeviceWrapper device);

        public void onBluetoothDisconnecting();
    }

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    private class BluetoothDeviceArrayAdapter extends ArrayAdapter<BluetoothDeviceWrapper> {

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
                        connectionState.setImageDrawable(getResources().getDrawable(R.drawable.green_button));
                        break;
                    case CONNECTING:
                        connectionState.setImageDrawable(getResources().getDrawable(R.drawable.orange_button));
                        break;
                    case DISCONNECTING:
                        connectionState.setImageDrawable(getResources().getDrawable(R.drawable.orange_button));
                        break;
                    case DISCONNECTED:
                        connectionState.setImageDrawable(getResources().getDrawable(R.drawable.red_button));
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

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    private class BluetoothDeviceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Toast.makeText(getActivity(), "ACTION_FOUND", Toast.LENGTH_SHORT).show();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!mBluetoothAdapter.getBondedDevices().contains(device)) {
                    mBluetoothDeviceArrayAdapter.add(BluetoothDeviceWrapper.newInstance(device));
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(getActivity(), "ACTION_DISCOVERY_STARTED", Toast.LENGTH_SHORT).show();
                mLoadingPanel.setVisibility(View.VISIBLE);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(getActivity(), "ACTION_DISCOVERY_FINISHED", Toast.LENGTH_SHORT).show();
                addPairedDevices();
                mLoadingPanel.setVisibility(View.GONE);
                mPairedDevicesListview.setVisibility(View.VISIBLE);
            } else if (BluetoothService.ACTION_BLUETOOTH_SOCKET_CONNECTED.equals(action)) {
                Toast.makeText(getActivity(), "ACTION_BLUETOOTH_SOCKET_CONNECTED", Toast.LENGTH_SHORT).show();
                mBluetoothDeviceArrayAdapter.getActiveBluetoothDeviceWrapper().setState(BluetoothDeviceWrapper.State.CONNECTED);
                mBluetoothDeviceArrayAdapter.notifyDataSetChanged();
            } else if (BluetoothService.ACTION_BLUETOOTH_SOCKET_DISCONNECTED.equals(action)) {
                Toast.makeText(getActivity(), "ACTION_BLUETOOTH_SOCKET_DISCONNECTED", Toast.LENGTH_SHORT).show();
                mBluetoothDeviceArrayAdapter.getActiveBluetoothDeviceWrapper().setState(BluetoothDeviceWrapper.State.DISCONNECTED);
                mBluetoothDeviceArrayAdapter.setActiveBluetoothDeviceWrapper(null);
                mBluetoothDeviceArrayAdapter.notifyDataSetChanged();
            }
        }
    }
}
