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
import bzh.eco.solar.adapter.ElectricalPowerArrayAdapter;
import bzh.eco.solar.adapter.TemperatureArrayAdapter;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.car.elements.SolarPanels;
import bzh.eco.solar.model.measurement.AbstractMeasurementElement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SolarPanelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SolarPanelFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Normal Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "SolarPanelElectricalPowerFragment";

    private BroadcastReceiver mDataUpdateReceiver = null;

    // -------------------------------------------------------------------------------------
    // Section : UI Fields(s)
    // -------------------------------------------------------------------------------------
    private ListView mListViewSolarPanelElectricalPower;

    private ArrayAdapter mElectricalPowerArrayAdapter;

    private ListView mListViewSolarPanelTemperature;

    private ArrayAdapter mTemperatureArrayAdapter;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public static SolarPanelFragment newInstance() {
        return new SolarPanelFragment();
    }

    public SolarPanelFragment() {
        mDataUpdateReceiver = new DataUpdateReceiver();
    }

    // -------------------------------------------------------------------------------------
    // Section : @Override Method(s)
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
        View root = inflater.inflate(R.layout.fragment_solar_panel, container, false);

        mListViewSolarPanelElectricalPower = (ListView) root.findViewById(R.id.list_view_electrical_power);
        mElectricalPowerArrayAdapter = new ElectricalPowerArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mElectricalPowerArrayAdapter.addAll(SolarPanels.getInstance().getElectricalPowerMeasurementElements());
        mListViewSolarPanelElectricalPower.setAdapter(mElectricalPowerArrayAdapter);

        mListViewSolarPanelTemperature = (ListView) root.findViewById(R.id.list_view_temperature);
        mTemperatureArrayAdapter = new TemperatureArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mTemperatureArrayAdapter.addAll(SolarPanels.getInstance().getTemperatureMeasurementElements());
        mListViewSolarPanelTemperature.setAdapter(mTemperatureArrayAdapter);

        return root;
    }

    @Override
    public void onResume() {
        if (mDataUpdateReceiver == null) {
            mDataUpdateReceiver = new DataUpdateReceiver();
        }

        IntentFilter intentFilter = new IntentFilter("VALUE_CHANGED");
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
    }

    // -------------------------------------------------------------------------------------
    // Section : Inner Class(es)
    // -------------------------------------------------------------------------------------
    private class DataUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("VALUE_CHANGED")) {
                if(intent.getSerializableExtra("CAR_ELEMENT_TYPE") == Car.ElementType.SOLAR_PANEL)
                {
                    if(intent.getSerializableExtra("MEASUREMENT_TYPE") == AbstractMeasurementElement.Measurement.ELECTRICAL_POWER) {
                        mElectricalPowerArrayAdapter.notifyDataSetChanged();
                    }
                    if(intent.getSerializableExtra("MEASUREMENT_TYPE") == AbstractMeasurementElement.Measurement.TEMPERATURE) {
                        mTemperatureArrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
