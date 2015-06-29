package bzh.eco.solar.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import bzh.eco.solar.R;
import bzh.eco.solar.adapter.MeasurementArrayAdapter;
import bzh.eco.solar.model.car.elements.Motors;
import bzh.eco.solar.model.measurement.Measurement;
import de.greenrobot.event.EventBus;

public class MotorFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "MotorFragment";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private MeasurementArrayAdapter mElectricalPowerArrayAdapter;

    private MeasurementArrayAdapter mSpeedArrayAdapter;

    private ListView mListViewElectricalPower;

    private ListView mListViewSpeed;

    public MotorFragment() {
    }

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static MotorFragment newInstance() {
        return new MotorFragment();
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

        mListViewSpeed = (ListView) root.findViewById(R.id.list_view_speed);
        mSpeedArrayAdapter = new MeasurementArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        mSpeedArrayAdapter.addAll(Motors.getInstance().getSpeedMeasurements());
        mListViewSpeed.setAdapter(mSpeedArrayAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
        switch (measurement.getType()) {
            case ELECTRICAL_POWER:
                mElectricalPowerArrayAdapter.notifyDataSetChanged();
                break;
            case SPEED:
                mSpeedArrayAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
