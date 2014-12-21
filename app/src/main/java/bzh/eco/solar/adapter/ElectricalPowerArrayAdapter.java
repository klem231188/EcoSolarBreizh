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
import bzh.eco.solar.model.measurement.ElectricalPowerMeasurementElement;

/**
 * @author : Clément.Tréguer
 */
public class ElectricalPowerArrayAdapter extends ArrayAdapter<ElectricalPowerMeasurementElement> {

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