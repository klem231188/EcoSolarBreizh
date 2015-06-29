package bzh.eco.solar.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.Measurement;

public class FileWriterFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "FileWriterFragment";

    public static final String BLUETOOTH_STORAGE_DIR = "ECOSOLAR_RECORDS";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private String mRecordFilename = null;

    private FileOutputStream mRecordFileStream = null;

    private Timer mTimer = null;

    private SimpleDateFormat mDateFormat = null;

    private NumberFormat mNumberFormat = null;

    public FileWriterFragment() {
        mDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

        mNumberFormat = DecimalFormat.getInstance(Locale.ENGLISH);
        mNumberFormat.setMaximumFractionDigits(2);
        mNumberFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static FileWriterFragment newInstance() {
        return new FileWriterFragment();
    }

    // -------------------------------------------------------------------------------------
    // Section : Android Lifecycle Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        if (isExternalStorageWritable()) {
            try {
                mRecordFileStream = getRecordFileOutputStream();
                initCsvFile();
                mTimer = new Timer(true);
                mTimer.scheduleAtFixedRate(new WriteTimerTask(), 0, TimeUnit.SECONDS.toMillis(1));
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Impossible d'écrire dans le fichier contenant les trames bluetooth", e);
            }
        }
    }

    @Override
    public void onStop() {
        if (mRecordFileStream != null) {
            try {
                mRecordFileStream.flush();
                mRecordFileStream.close();
            } catch (IOException e) {
                Log.e(TAG, "Impossible de flusher le fichier contenant les trames bluetooth", e);
            }
        }

        if (mTimer != null) {
            mTimer.cancel();
        }

        super.onStop();
    }

    // -------------------------------------------------------------------------------------
    // Section : Private Method(s)
    // -------------------------------------------------------------------------------------
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private FileOutputStream getRecordFileOutputStream() throws FileNotFoundException {
        File storageDir = getBluetoothStorageDir(getActivity(), BLUETOOTH_STORAGE_DIR);
        if (mRecordFilename == null) {
            mRecordFilename = getRecordFilename();
        }
        File recordFile = new File(storageDir.getAbsolutePath() + "/" + mRecordFilename);
        return new FileOutputStream(recordFile, true);
    }

    public File getBluetoothStorageDir(Context context, String bluetoothDir) {
        // Get the directory for the app's private documents directory.
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), bluetoothDir);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    private String getRecordFilename() {
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = s.format(new Date());
        return "record" + "-" + date + ".csv";
    }

    private void initCsvFile() {
        try {
            mRecordFileStream.write("timestamp".getBytes());
            for (Measurement measurement : Car.getInstance().getAllMeasurements()) {
                String id = ";ID=" + measurement.getID();
                mRecordFileStream.write(id.getBytes());
            }
            mRecordFileStream.write("\n".getBytes());
        } catch (IOException e) {
            Log.e(TAG, "Impossible d'ecrire dans le fichier contenant les trames bluetooth", e);
        }
    }

    // -------------------------------------------------------------------------------------
    // Section : Inner class
    // -------------------------------------------------------------------------------------
    public class WriteTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                String timestamp = mDateFormat.format(new Date());
                mRecordFileStream.write(timestamp.getBytes());
                for (Measurement measurement : Car.getInstance().getAllMeasurements()) {
                    String value = ";" + mNumberFormat.format(measurement.getValue());
                    mRecordFileStream.write(value.getBytes());
                }
                mRecordFileStream.write("\n".getBytes());
            } catch (IOException e) {
                Log.e(TAG, "Impossible d'ecrire dans le fichier contenant les trames bluetooth", e);
            }
        }
    }
}
