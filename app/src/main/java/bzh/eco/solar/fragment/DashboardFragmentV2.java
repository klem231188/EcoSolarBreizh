package bzh.eco.solar.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import bzh.eco.solar.model.measurement.Measurement;
import bzh.eco.solar.view.SpeedometerGauge;

public class DashboardFragmentV2 extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "DashboardFragmentV2";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private BroadcastReceiver mDataUpdateReceiver = null;

    private TextView mTextViewCarSpeed = null;

    private SpeedometerGauge mSpeedometer = null;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static DashboardFragmentV2 newInstance() {
        return new DashboardFragmentV2();
    }

    public DashboardFragmentV2() {
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
        View root = inflater.inflate(R.layout.fragment_dashboard_v2, container, false);

        mTextViewCarSpeed = (TextView) root.findViewById(R.id.text_view_car_speed);
        initSpeedometerGauge(root);


        return root;
    }

    private void initSpeedometerGauge(View root) {
        mSpeedometer = (SpeedometerGauge) root.findViewById(R.id.speedometer);
        mSpeedometer.setMaxSpeed(150);
        mSpeedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });
        mSpeedometer.setMajorTickStep(30);
        mSpeedometer.setMinorTicks(2);
        mSpeedometer.addColoredRange(0, 80, Color.GREEN);
        mSpeedometer.addColoredRange(80, 120, Color.YELLOW);
        mSpeedometer.addColoredRange(120, 150, Color.RED);
        mSpeedometer.setSpeed(0, 0, 0);
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
                Measurement measurement = (Measurement) intent.getSerializableExtra("MEASUREMENT_ELEMENT");

                if (intent.getSerializableExtra("MEASUREMENT_TYPE") == Measurement.Type.ELECTRICAL_POWER) {
                    Log.i(TAG, measurement.toString());
                }
                if (intent.getSerializableExtra("MEASUREMENT_TYPE") == Measurement.Type.TEMPERATURE) {
                    Log.i(TAG, measurement.toString());
                }
                if (intent.getSerializableExtra("MEASUREMENT_TYPE") == Measurement.Type.SPEED) {
                    Log.i(TAG, measurement.toString());
                    if (measurement.getID() == 23) {
                        double speed = measurement.getValue();
                        mTextViewCarSpeed.setText(formatter.format(speed));
                        mSpeedometer.setSpeed(speed, 50, 0);
                    }
                }
            }
        }
    }
}
