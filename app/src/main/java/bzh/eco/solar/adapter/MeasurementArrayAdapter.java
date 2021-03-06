package bzh.eco.solar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import bzh.eco.solar.R;
import bzh.eco.solar.model.measurement.Measurement;

/**
 * @author : Clément.Tréguer
 */
public class MeasurementArrayAdapter extends ArrayAdapter<Measurement> {

    private NumberFormat mNumberFormat = null;

    public MeasurementArrayAdapter(Context context, int resource) {
        super(context, resource);
        mNumberFormat = DecimalFormat.getInstance(Locale.FRANCE);
        mNumberFormat.setMaximumFractionDigits(2);
        mNumberFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_measurement_row, null);
        }

        Measurement measurement = getItem(position);

        TextView textViewMeaning = (TextView) v.findViewById(R.id.text_view_meaning);
        TextView textViewValue = (TextView) v.findViewById(R.id.text_view_value);
        ProgressBar progressBarValue = (ProgressBar) v.findViewById(R.id.progress_bar_value);

        if (measurement != null) {
            textViewMeaning.setText(measurement.getMeaning());
            textViewValue.setText(mNumberFormat.format(measurement.getValue()) + " " + measurement.getUnity().getValue());
            progressBarValue.setProgress((int) ((measurement.getValue() * 100) / measurement.getMaxValue()));
        }

        return v;
    }
}