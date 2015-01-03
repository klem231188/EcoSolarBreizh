package bzh.eco.solar.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import bzh.eco.solar.R;
import bzh.eco.solar.model.car.elements.Generals;
import bzh.eco.solar.model.measurement.AbstractMeasurementElement.Measurement;
import bzh.eco.solar.model.measurement.ElectricalPowerMeasurementElement;
import bzh.eco.solar.model.measurement.SpeedMeasurementElement;
import bzh.eco.solar.model.measurement.TemperatureMeasurementElement;

public class DashboardFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "DashboardFragment";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private BroadcastReceiver mDataUpdateReceiver = null;

    private TextView mTextViewCarSpeed = null;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    public DashboardFragment() {
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
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mTextViewCarSpeed = (TextView) root.findViewById(R.id.text_view_car_speed);

        return root;
    }

    @Override
    public void onResume() {
        if (mDataUpdateReceiver == null) {
            mDataUpdateReceiver = new DataUpdateReceiver();
        }

        IntentFilter intentFilter = new IntentFilter(Generals.getInstance().getType().name());
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
            NumberFormat formatter = new DecimalFormat("#0.0");

            if (intent.getAction().equals(Generals.getInstance().getType().name())) {
                if (intent.getSerializableExtra("MEASUREMENT_TYPE") == Measurement.ELECTRICAL_POWER) {
                    ElectricalPowerMeasurementElement measurementElement = (ElectricalPowerMeasurementElement) intent.getSerializableExtra("MEASUREMENT_ELEMENT");
                    Log.i(TAG, measurementElement.toString());
                }
                if (intent.getSerializableExtra("MEASUREMENT_TYPE") == Measurement.TEMPERATURE) {
                    TemperatureMeasurementElement measurementElement = (TemperatureMeasurementElement) intent.getSerializableExtra("MEASUREMENT_ELEMENT");
                    Log.i(TAG, measurementElement.toString());
                }
                if (intent.getSerializableExtra("MEASUREMENT_TYPE") == Measurement.SPEED) {
                    SpeedMeasurementElement measurementElement = (SpeedMeasurementElement) intent.getSerializableExtra("MEASUREMENT_ELEMENT");
                    Log.i(TAG, measurementElement.toString());
                    if (measurementElement.getID() == 23) {
                        mTextViewCarSpeed.setText(formatter.format(measurementElement.getSpeed()));
                    }
                }
            }
        }
    }
}
