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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import bzh.eco.solar.R;
import bzh.eco.solar.model.bluetooth.commands.Command;
import bzh.eco.solar.model.car.elements.Generals;
import bzh.eco.solar.model.measurement.Measurement;
import bzh.eco.solar.service.BluetoothService;

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

    private ImageButton mButtonTurnLeft = null;

    private ImageButton mButtonTurnRight = null;

    private ImageButton mButtonWarnings = null;

    private ImageButton mButtonDeepedHeadLights = null;

    private ImageButton mButtonFullHeadLights = null;

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

        mButtonTurnLeft = (ImageButton) root.findViewById(R.id.button_turn_left);
        mButtonTurnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothService.ACTION_SEND_COMMAND);
                intent.putExtra("COMMAND", Command.TURN_LEFT);

                getActivity().sendBroadcast(intent);
            }
        });

        mButtonTurnRight = (ImageButton) root.findViewById(R.id.button_turn_right);
        mButtonTurnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothService.ACTION_SEND_COMMAND);
                intent.putExtra("COMMAND", Command.TURN_RIGHT);

                getActivity().sendBroadcast(intent);
            }
        });

        mButtonWarnings = (ImageButton) root.findViewById(R.id.button_warnings);
        mButtonWarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothService.ACTION_SEND_COMMAND);
                intent.putExtra("COMMAND", Command.WARNING_LIGHTS);

                getActivity().sendBroadcast(intent);
            }
        });

        mButtonDeepedHeadLights = (ImageButton) root.findViewById(R.id.button_deeped_headlights_off);
        mButtonDeepedHeadLights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothService.ACTION_SEND_COMMAND);
                intent.putExtra("COMMAND", Command.DEEPED_HEADLIGHTS);

                getActivity().sendBroadcast(intent);
            }
        });

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
                        mTextViewCarSpeed.setText(formatter.format(measurement.getValue()));
                    }
                }
            }
        }
    }
}
