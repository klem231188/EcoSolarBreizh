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
import android.widget.ListView;

import bzh.eco.solar.R;
import bzh.eco.solar.adapter.MeasurementArrayAdapter;
import bzh.eco.solar.model.car.elements.Motors;
import bzh.eco.solar.model.measurement.Measurement;

public class MotorFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "MotorFragment";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private BroadcastReceiver mDataUpdateReceiver = null;

    private MeasurementArrayAdapter mElectricalPowerArrayAdapter;

    private MeasurementArrayAdapter mTemperatureArrayAdapter;

    private MeasurementArrayAdapter mSpeedArrayAdapter;

    private ListView mListViewElectricalPower;

    private ListView mListViewTemperature;

    private ListView mListViewSpeed;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static MotorFragment newInstance() {
        return new MotorFragment();
    }

    public MotorFragment() {
        mDataUpdateReceiver = new DataUpdateReceiver();
    }

    // -------------------------------------------------------------------------------------
    // Section : Android Lifecycle Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_motor, container, false);

        mListViewElectricalPower = (ListView) root.findViewById(R.id.list_view_electrical_power);
        mElectricalPowerArrayAdapter = new MeasurementArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mElectricalPowerArrayAdapter.addAll(Motors.getInstance().getElectricalPowerMeasurements());
        mListViewElectricalPower.setAdapter(mElectricalPowerArrayAdapter);

        mListViewTemperature = (ListView) root.findViewById(R.id.list_view_temperature);
        mTemperatureArrayAdapter = new MeasurementArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mTemperatureArrayAdapter.addAll(Motors.getInstance().getTemperatureMeasurements());
        mListViewTemperature.setAdapter(mTemperatureArrayAdapter);

        mListViewSpeed = (ListView) root.findViewById(R.id.list_view_speed);
        mSpeedArrayAdapter = new MeasurementArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mSpeedArrayAdapter.addAll(Motors.getInstance().getSpeedMeasurements());
        mListViewSpeed.setAdapter(mSpeedArrayAdapter);

        return root;
    }

    @Override
    public void onResume() {
        if (mDataUpdateReceiver == null) {
            mDataUpdateReceiver = new DataUpdateReceiver();
        }

        IntentFilter intentFilter = new IntentFilter(Motors.getInstance().getType().name());
        getActivity().registerReceiver(mDataUpdateReceiver, intentFilter);

        super.onResume();
    }

    @Override
    public void onPause() {
        if (mDataUpdateReceiver != null) {
            getActivity().unregisterReceiver(mDataUpdateReceiver);
        }

        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // -------------------------------------------------------------------------------------
    // Section : Inner Class(es)
    // -------------------------------------------------------------------------------------
    private class DataUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Motors.getInstance().getType().name())) {
                if (intent.getSerializableExtra("MEASUREMENT_TYPE") == Measurement.Type.ELECTRICAL_POWER) {
                    mElectricalPowerArrayAdapter.notifyDataSetChanged();
                }
                if (intent.getSerializableExtra("MEASUREMENT_TYPE") == Measurement.Type.TEMPERATURE) {
                    mTemperatureArrayAdapter.notifyDataSetChanged();
                }
                if (intent.getSerializableExtra("MEASUREMENT_TYPE") == Measurement.Type.SPEED) {
                    mSpeedArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
