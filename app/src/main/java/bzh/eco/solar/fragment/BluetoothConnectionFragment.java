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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

import bzh.eco.solar.R;
import bzh.eco.solar.adapter.BluetoothDeviceArrayAdapter;
import bzh.eco.solar.model.bluetooth.BluetoothDeviceWrapper;
import bzh.eco.solar.service.BluetoothService;

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

    private BluetoothDeviceArrayAdapter mBluetoothDeviceArrayAdapter;

    private BroadcastReceiver mReceiver;

    private BluetoothCallback mListener;

    private LinearLayout mContentPanel;

    private LinearLayout mLoadingPanel;

    private ListView mPairedDevicesListview;

    private Button mRefreshButton;

    private Boolean mIsFirstTimeStarting;

    private Boolean mIsDiscoveringDevices;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static BluetoothConnectionFragment newInstance() {
        return new BluetoothConnectionFragment();
    }

    public BluetoothConnectionFragment() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mReceiver = new BluetoothDeviceBroadcastReceiver();
        mIsFirstTimeStarting = true;
        mIsDiscoveringDevices = false;
    }

    // -------------------------------------------------------------------------------------
    // Section : Android Lifecycle Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);
        try {
            mListener = (BluetoothCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mBluetoothDeviceArrayAdapter = new BluetoothDeviceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View root = inflater.inflate(R.layout.fragment_bluetooth_connection, container, false);

        // Loading/Content Panels
        mContentPanel = (LinearLayout) root.findViewById(R.id.content_panel);
        mLoadingPanel = (LinearLayout) root.findViewById(R.id.loading_panel);
        if (mIsDiscoveringDevices) {
            mLoadingPanel.setVisibility(View.VISIBLE);
            mContentPanel.setVisibility(View.GONE);
        } else {
            mLoadingPanel.setVisibility(View.GONE);
            mContentPanel.setVisibility(View.VISIBLE);
        }

        // Refresh button
        mRefreshButton = (Button) root.findViewById(R.id.button_refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentPanel.setVisibility(View.GONE);
                mBluetoothDeviceArrayAdapter.clear();
                startDiscovery();
            }
        });

        // Paired devices ListView
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

        return root;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.i(TAG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();

        if(mIsFirstTimeStarting) {
            mIsFirstTimeStarting = false;

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
                    addPairedDevices();
                }
            } else {
                Log.e(TAG, "Bluetooth not supported");
            }
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        getActivity().unregisterReceiver(mReceiver);
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        if (requestCode == BLUETOOTH_ENABLED) {
            startDiscovery();
        }
    }

    // -------------------------------------------------------------------------------------
    // Other Method(s)
    // -------------------------------------------------------------------------------------
    private void addPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice pairedDevice : pairedDevices) {
                mBluetoothDeviceArrayAdapter.add(BluetoothDeviceWrapper.newInstance(pairedDevice));
            }
        }
    }

    private void startDiscovery() {
        mBluetoothAdapter.startDiscovery();
        mIsDiscoveringDevices = true;
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
                mIsDiscoveringDevices = false;
                addPairedDevices();
                mLoadingPanel.setVisibility(View.GONE);
                mContentPanel.setVisibility(View.VISIBLE);
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
