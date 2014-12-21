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
import bzh.eco.solar.model.measurement.SpeedMeasurementElement;
import bzh.eco.solar.model.measurement.TemperatureMeasurementElement;

/**
 * @author : Clément.Tréguer
 */
public class SpeedArrayAdapter extends ArrayAdapter<SpeedMeasurementElement> {

    private static final double MAX_VALUE_SPEED = 100; //°C

    NumberFormat formatter = null;

    public SpeedArrayAdapter(Context context, int resource) {
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

        SpeedMeasurementElement measurementElement = getItem(position);

        TextView textViewMeaning = (TextView) v.findViewById(R.id.text_view_meaning);
        TextView textViewValue = (TextView) v.findViewById(R.id.text_view_value);
        ProgressBar progressBarValue = (ProgressBar) v.findViewById(R.id.progress_bar_value);

        if (measurementElement != null) {
            textViewMeaning.setText(measurementElement.getMeaning());
            textViewValue.setText(formatter.format(measurementElement.getSpeed()) + " Tr/Min");
            progressBarValue.setProgress((int) ((measurementElement.getSpeed() * 100) / MAX_VALUE_SPEED));
        }

        return v;
    }
}