package bzh.eco.solar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import bzh.eco.solar.R;
import bzh.eco.solar.model.measurement.ElectricalPowerMeasurement;

/**
 * @author : Clément.Tréguer
 */
public class ElectricalPowerArrayAdapter extends ArrayAdapter<ElectricalPowerMeasurement> {

    private static final double MAX_VALUE_ELECTRICAL_POWER = 1; // Ampère

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

        ElectricalPowerMeasurement measurement = getItem(position);

        TextView textViewMeaning = (TextView) v.findViewById(R.id.text_view_meaning);
        TextView textViewElectricalPower = (TextView) v.findViewById(R.id.text_view_value);
        ProgressBar progressBarElectricalPower = (ProgressBar) v.findViewById(R.id.progress_bar_value);

        if (measurement != null) {
            textViewMeaning.setText(measurement.getMeaning());
            textViewElectricalPower.setText(formatter.format(measurement.getElectricalPower()) + " A");
            progressBarElectricalPower.setProgress((int) ((measurement.getElectricalPower() * 100) / MAX_VALUE_ELECTRICAL_POWER));
        }

        return v;
    }
}