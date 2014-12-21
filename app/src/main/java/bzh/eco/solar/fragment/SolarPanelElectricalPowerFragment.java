package bzh.eco.solar.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import bzh.eco.solar.R;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.car.elements.SolarPanels;
import bzh.eco.solar.model.measurement.AbstractMeasurementElement;
import bzh.eco.solar.model.measurement.ElectricalPowerMeasurementElement;
import bzh.eco.solar.model.measurement.TemperatureMeasurementElement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SolarPanelElectricalPowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SolarPanelElectricalPowerFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Normal Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "SolarPanelElectricalPowerFragment";

    public static double MAX_VALUE_ELECTRICAL_POWER = 1; //Ampère

    private static final double MAX_VALUE_TEMPERATURE = 50; // °C

    private BroadcastReceiver mDataUpdateReceiver = null;

    private LocalBroadcastManager mLocalBroadcastManager = null;

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
    public static SolarPanelElectricalPowerFragment newInstance() {
        return new SolarPanelElectricalPowerFragment();
    }

    public SolarPanelElectricalPowerFragment() {
        mDataUpdateReceiver = new DataUpdateReceiver();
    }

    // -------------------------------------------------------------------------------------
    // Section : @Override Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_solar_panel_electrical_power, container, false);

        mListViewSolarPanelElectricalPower = (ListView) root.findViewById(R.id.list_view_solar_panel_electrical_power);
        mElectricalPowerArrayAdapter = new ElectricalPowerArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mElectricalPowerArrayAdapter.addAll(SolarPanels.getInstance().getElectricalPowerMeasurementElements());
        mListViewSolarPanelElectricalPower.setAdapter(mElectricalPowerArrayAdapter);

        mListViewSolarPanelTemperature = (ListView) root.findViewById(R.id.list_view_solar_panel_temperature);
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

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    private class ElectricalPowerArrayAdapter extends ArrayAdapter<ElectricalPowerMeasurementElement> {

        NumberFormat formatter = null;

        public ElectricalPowerArrayAdapter(Context context, int resource) {
            super(context, resource);
            formatter = new DecimalFormat("#0.00");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.layout_measurement_row, null);
            }

            ElectricalPowerMeasurementElement measurementElement = getItem(position);

            TextView textViewMeaning = (TextView) v.findViewById(R.id.text_view_meaning);
            TextView textViewElectricalPower = (TextView) v.findViewById(R.id.text_view_value);
            ProgressBar progressBarElectricalPower = (ProgressBar) v.findViewById(R.id.progress_bar_value);

            if (measurementElement != null) {
                textViewMeaning.setText(measurementElement.getMeaning());
                textViewElectricalPower.setText(formatter.format(measurementElement.getElectricalPower()) + " A");
                progressBarElectricalPower.setProgress( (int)((measurementElement.getElectricalPower() * 100) / MAX_VALUE_ELECTRICAL_POWER) );
            }

            return v;
        }
    }

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    private class TemperatureArrayAdapter extends ArrayAdapter<TemperatureMeasurementElement> {

        NumberFormat formatter = null;

        public TemperatureArrayAdapter(Context context, int resource) {
            super(context, resource);
            formatter = new DecimalFormat("#0.00");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.layout_measurement_row, null);
            }

            TemperatureMeasurementElement measurementElement = getItem(position);

            TextView textViewMeaning = (TextView) v.findViewById(R.id.text_view_meaning);
            TextView textViewElectricalPower = (TextView) v.findViewById(R.id.text_view_value);
            ProgressBar progressBarElectricalPower = (ProgressBar) v.findViewById(R.id.progress_bar_value);

            if (measurementElement != null) {
                textViewMeaning.setText(measurementElement.getMeaning());
                textViewElectricalPower.setText(formatter.format(measurementElement.getTemperature()) + " °C");
                progressBarElectricalPower.setProgress( (int)((measurementElement.getTemperature() * 100) / MAX_VALUE_TEMPERATURE) );
            }

            return v;
        }
    }
}
