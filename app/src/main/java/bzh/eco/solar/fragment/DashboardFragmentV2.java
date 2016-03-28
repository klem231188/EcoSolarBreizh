package bzh.eco.solar.fragment;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import bzh.eco.solar.R;
import bzh.eco.solar.activity.MainActivity;
import bzh.eco.solar.model.car.commands.ClignotantWarningCommand;
import bzh.eco.solar.model.car.elements.Motors;
import bzh.eco.solar.model.measurement.Measurement;
import bzh.eco.solar.model.voiture.Ids;
import bzh.eco.solar.model.voiture.element.impl.batterie.Batterie;
import bzh.eco.solar.model.voiture.element.impl.clignotant.ClignotantDroit;
import bzh.eco.solar.model.voiture.element.impl.clignotant.ClignotantGauche;
import bzh.eco.solar.model.voiture.element.impl.kelly.KellyDroit;
import bzh.eco.solar.model.voiture.element.impl.kelly.KellyGauche;
import bzh.eco.solar.view.BatteryIndicatorGauge;
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

    private BatteryIndicatorGauge mBatteryIndicatorGauge = null;

    private TextView mTextViewMotorsValue = null;

    private TextView mTextViewCellsValue = null;

    private TextView mTextViewBatteryValue = null;

    private TextView mTextViewValeurTensionBatterie = null;

    private NumberFormat mNumberFormat;

    private ImageButton mButtonTurnLeft;

    private ImageButton mButtonTurnRight;

    private ImageButton mButtonWarning;

    private Button mButtonKellyGauche;

    private Button mButtonKellyDroit;

    public DashboardFragmentV2() {
        mNumberFormat = DecimalFormat.getInstance(Locale.FRANCE);
        mNumberFormat.setMaximumFractionDigits(2);
        mNumberFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static DashboardFragmentV2 newInstance() {
        return new DashboardFragmentV2();
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
        mTextViewBatteryValue = (TextView) root.findViewById(R.id.text_view_battery_value);
        mTextViewValeurTensionBatterie = (TextView) root.findViewById(R.id.textview_valeur_tension_batterie);
        mButtonTurnLeft = (ImageButton) root.findViewById(R.id.button_turning_left);
        mButtonTurnRight = (ImageButton) root.findViewById(R.id.button_turning_right);
        mButtonWarning = (ImageButton) root.findViewById(R.id.button_warnings);

        initSpeedometerGauge(root);
        initBatteryIndicatorGauge(root);
        initButtonKellyGauche(root);
        initButtonKellyDroit(root);

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
    public void onEvent(Integer id) {
        switch (id) {
            case Ids.TENSION_BATTERIE:
                mTextViewValeurTensionBatterie.setText(mNumberFormat.format(Batterie.getInstance().getTension()));
                break;
            case Ids.KELLY_GAUCHE_LSB:
            case Ids.KELLY_GAUCHE_MSB:
                switch (KellyGauche.getInstance().getEtat()) {
                    case ETEINT:
                        mButtonKellyGauche.setCompoundDrawablesWithIntrinsicBounds(R.drawable.orange_button, 0, 0, 0);
                        break;
                    case OK:
                        mButtonKellyGauche.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_button, 0, 0, 0);
                        break;
                    case ERREUR:
                        mButtonKellyGauche.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_button, 0, 0, 0);
                        break;
                }
                break;
            case Ids.KELLY_DROIT_LSB:
            case Ids.KELLY_DROIT_MSB:
                switch (KellyDroit.getInstance().getEtat()) {
                    case ETEINT:
                        mButtonKellyDroit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.orange_button, 0, 0, 0);
                        break;
                    case OK:
                        mButtonKellyDroit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_button, 0, 0, 0);
                        break;
                    case ERREUR:
                        mButtonKellyDroit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_button, 0, 0, 0);
                        break;
                }
                break;
            default:
                break;
        }
    }

    public void onEvent(Measurement measurement) {
        switch (measurement.getID()) {
            case 23:
                updateSpeedValue(measurement);
                break;
            case 58:
                updateCellsValue(measurement);
                break;
            case 22:
                updateBatteryValue(measurement);
                break;
            case 54:
            case 55:
                updateMotorsValue();
                break;
            default:
                break;
        }
    }

    public void onEvent(ClignotantWarningCommand.Command command) {
        mButtonTurnLeft.setImageDrawable(getResources().getDrawable(R.drawable.turn_left_off));
        mButtonTurnRight.setImageDrawable(getResources().getDrawable(R.drawable.turn_right_off));
        mButtonWarning.setImageDrawable(getResources().getDrawable(R.drawable.warnings_off));

        switch (command) {
            case CLIGNOTANT_GAUCHE_ON:
                mButtonTurnLeft.setImageDrawable(getResources().getDrawable(R.drawable.turn_left_on));
                break;

            case CLIGNOTANT_DROIT_ON:
                mButtonTurnRight.setImageDrawable(getResources().getDrawable(R.drawable.turn_right_on));
                break;

            case WARNING_ON:
                mButtonWarning.setImageDrawable(getResources().getDrawable(R.drawable.warnings_on));
                break;

            case RIEN:
            default:
                break;
        }
    }

    public void onEvent(ClignotantGauche clignotantGauche) {
        switch (clignotantGauche.getEtat()) {
            case ACTIF:
                mButtonTurnLeft.setImageDrawable(getResources().getDrawable(R.drawable.turn_left_on));
                break;
            case INACTIF:
                mButtonTurnLeft.setImageDrawable(getResources().getDrawable(R.drawable.turn_left_off));
                break;
        }
    }

    public void onEvent(ClignotantDroit rightIndicatorLight) {
        switch (rightIndicatorLight.getEtat()) {
            case ACTIF:
                mButtonTurnRight.setImageDrawable(getResources().getDrawable(R.drawable.turn_right_on));
                break;
            case INACTIF:
                mButtonTurnRight.setImageDrawable(getResources().getDrawable(R.drawable.turn_right_off));
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

    private void initBatteryIndicatorGauge(View root) {
        mBatteryIndicatorGauge = (BatteryIndicatorGauge) root.findViewById(R.id.battery);
        mBatteryIndicatorGauge.setMax(100);
        mBatteryIndicatorGauge.setMin(0);
        mBatteryIndicatorGauge.setValue(0);
    }

    private void initButtonKellyGauche(View root) {
        mButtonKellyGauche = (Button) root.findViewById(R.id.button_kelly_gauche);
        mButtonKellyGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionBar.Tab tab = getActivity().getActionBar().getTabAt(MainActivity.SectionsPagerAdapter.KELLY_SECTION);
                getActivity().getActionBar().selectTab(tab);
            }
        });
    }

    private void initButtonKellyDroit(View root) {
        mButtonKellyDroit = (Button) root.findViewById(R.id.button_kelly_droit);
        mButtonKellyDroit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionBar.Tab tab = getActivity().getActionBar().getTabAt(MainActivity.SectionsPagerAdapter.KELLY_SECTION);
                getActivity().getActionBar().selectTab(tab);
            }
        });
    }

    private void updateSpeedValue(Measurement measurement) {
        double speed = measurement.getValue();
        mTextViewCarSpeed.setText(mNumberFormat.format(speed));
        mSpeedometer.setSpeed(speed, 50, 0);
    }

    private void updateCellsValue(Measurement measurement) {
        mTextViewCellsValue.setText(mNumberFormat.format(measurement.getValue()) + " " + measurement.getUnity().getValue());
    }

    private void updateMotorsValue() {
        mTextViewMotorsValue.setText(mNumberFormat.format(Motors.getInstance().getGlobalElectricalPower()) + " " + Measurement.Unity.AMPERE.getValue());
    }

    private void updateBatteryValue(Measurement measurement) {
        double value = measurement.getValue();
        mBatteryIndicatorGauge.setValue((float) value);
        mTextViewBatteryValue.setText(mNumberFormat.format(value) + " %");
    }
}
