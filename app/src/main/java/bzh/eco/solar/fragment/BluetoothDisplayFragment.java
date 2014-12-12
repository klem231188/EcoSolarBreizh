package bzh.eco.solar.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import bzh.eco.solar.R;
import bzh.eco.solar.model.BluetoothFrame;
import bzh.eco.solar.service.BluetoothService;

public class BluetoothDisplayFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private BluetoothCallback mListener;

    private ListView mDisplayFrameListView;

    private ArrayAdapter<String> mDisplayFrameAdapter;

    private BroadcastReceiver mDataUpdateReceiver = new DataUpdateReceiver();
    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public static BluetoothDisplayFragment newInstance() {
        BluetoothDisplayFragment fragment = new BluetoothDisplayFragment();
        return fragment;
    }

    public BluetoothDisplayFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bluetooth_display, container, false);
        mDisplayFrameListView = (ListView) root.findViewById(R.id.list_view_bluetooth_frame);
        mDisplayFrameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        mDisplayFrameListView.setAdapter(mDisplayFrameAdapter);

        return root;
    }

    @Override
    public void onResume() {
        if (mDataUpdateReceiver == null) {
            mDataUpdateReceiver = new DataUpdateReceiver();
        }
        IntentFilter intentFilter = new IntentFilter(BluetoothService.ACTION_BLUETOOTH_FRAME_PROCESSED);
        getActivity().registerReceiver(mDataUpdateReceiver, intentFilter);

        super.onResume();
    }

    @Override
    public void onPause() {
        if (mDataUpdateReceiver == null) {
            getActivity().unregisterReceiver(mDataUpdateReceiver);
        }

        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addFrameToDisplay(BluetoothFrame frame) {
        mDisplayFrameAdapter.add(frame.toString());
    }

    // -------------------------------------------------------------------------------------
    // Section : Inner Class(es)
    // -------------------------------------------------------------------------------------
    public interface BluetoothCallback {

    }

    private class DataUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothService.ACTION_BLUETOOTH_FRAME_PROCESSED.equals(action)) {
                BluetoothFrame frame = (BluetoothFrame) intent.getSerializableExtra(BluetoothService.EXTRA_BLUETOOTH_FRAME);
                BluetoothDisplayFragment.this.addFrameToDisplay(frame);
            }
        }
    }
}