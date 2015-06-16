package bzh.eco.solar.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

import bzh.eco.solar.R;
import bzh.eco.solar.model.measurement.Measurement;
import bzh.eco.solar.view.SpeedometerGauge;
import de.greenrobot.event.EventBus;

public class DashboardFragmentV2 extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "DashboardFragmentV2";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private TextView mTextViewCarSpeed = null;

    private SpeedometerGauge mSpeedometer = null;

    private TextView mTextViewMotorsValue = null;

    private TextView mTextViewCellsValue = null;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static DashboardFragmentV2 newInstance() {
        return new DashboardFragmentV2();
    }

    public DashboardFragmentV2() {
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
        mTextViewMotorsValue = (TextView) root.findViewById(R.id.text_view_motors_value);
        mTextViewCellsValue = (TextView) root.findViewById(R.id.text_view_cells_value);
        initSpeedometerGauge(root);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // -------------------------------------------------------------------------------------
    // Section : EventBus onEvent Method(s)
    // -------------------------------------------------------------------------------------
    public void onEvent(Measurement measurement) {
        switch (measurement.getID()) {
            case 23:
                updateSpeedValue(measurement);
                break;
            case 58:
                updateCellsValue(measurement);
                break;
            default:
                break;
        }
    }

    // -------------------------------------------------------------------------------------
    // Section : Private Methods
    // -------------------------------------------------------------------------------------
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

    private void updateSpeedValue(Measurement measurement) {
        double speed = measurement.getValue();
        mTextViewCarSpeed.setText(new DecimalFormat("#0.0").format(speed));
        mSpeedometer.setSpeed(speed, 50, 0);
    }

    private void updateCellsValue(Measurement measurement) {
        mTextViewCellsValue.setText(new DecimalFormat("#0.00").format(measurement.getValue()) + " " + measurement.getUnity().getValue());
    }
}
