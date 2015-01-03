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
import bzh.eco.solar.model.measurement.TemperatureMeasurement;

/**
 * @author : Clément.Tréguer
 */
public class TemperatureArrayAdapter extends ArrayAdapter<TemperatureMeasurement> {

    private static final double MAX_VALUE_TEMPERATURE = 100; //°C

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

        TemperatureMeasurement measurement = getItem(position);

        TextView textViewMeaning = (TextView) v.findViewById(R.id.text_view_meaning);
        TextView textViewElectricalPower = (TextView) v.findViewById(R.id.text_view_value);
        ProgressBar progressBarElectricalPower = (ProgressBar) v.findViewById(R.id.progress_bar_value);

        if (measurement != null) {
            textViewMeaning.setText(measurement.getMeaning());
            textViewElectricalPower.setText(formatter.format(measurement.getTemperature()) + " °C");
            progressBarElectricalPower.setProgress((int) ((measurement.getTemperature() * 100) / MAX_VALUE_TEMPERATURE));
        }

        return v;
    }
}