package bzh.eco.solar.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import bzh.eco.solar.R;
import bzh.eco.solar.adapter.MeasurementArrayAdapter;
import bzh.eco.solar.model.car.elements.SolarPanels;
import bzh.eco.solar.model.measurement.Measurement;
import de.greenrobot.event.EventBus;

public class SolarPanelFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "SolarPanelFragment";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private BroadcastReceiver mDataUpdateReceiver = null;

    private ListView mListViewSolarPanelElectricalPower;

    private ListView mListViewSolarPanelTemperature;

    private MeasurementArrayAdapter mElectricalPowerArrayAdapter;

    private MeasurementArrayAdapter mTemperatureArrayAdapter;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static SolarPanelFragment newInstance() {
        return new SolarPanelFragment();
    }

    public SolarPanelFragment() {
    }

    // -------------------------------------------------------------------------------------
    // Section : Android Lifecycle Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View root = inflater.inflate(R.layout.fragment_solar_panel, container, false);

        mListViewSolarPanelElectricalPower = (ListView) root.findViewById(R.id.list_view_electrical_power);
        mElectricalPowerArrayAdapter = new MeasurementArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mElectricalPowerArrayAdapter.addAll(SolarPanels.getInstance().getElectricalPowerMeasurements());
        mListViewSolarPanelElectricalPower.setAdapter(mElectricalPowerArrayAdapter);

        mListViewSolarPanelTemperature = (ListView) root.findViewById(R.id.list_view_temperature);
        mTemperatureArrayAdapter = new MeasurementArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mTemperatureArrayAdapter.addAll(SolarPanels.getInstance().getTemperatureMeasurements());
        mListViewSolarPanelTemperature.setAdapter(mTemperatureArrayAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        if (mDataUpdateReceiver != null) {
            getActivity().unregisterReceiver(mDataUpdateReceiver);
        }

        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.i(TAG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        super.onDetach();
    }

    // -------------------------------------------------------------------------------------
    // Section : EventBus onEvent Method(s)
    // -------------------------------------------------------------------------------------
    public void onEvent(Measurement measurement) {
        switch (measurement.getType()) {
            case ELECTRICAL_POWER:
                mElectricalPowerArrayAdapter.notifyDataSetChanged();
                break;
            case TEMPERATURE:
                mTemperatureArrayAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
