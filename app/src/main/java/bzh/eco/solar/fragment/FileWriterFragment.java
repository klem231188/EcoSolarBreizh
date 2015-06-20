package bzh.eco.solar.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bzh.eco.solar.model.measurement.Measurement;
import de.greenrobot.event.EventBus;

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

    public FileWriterFragment() {
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
        EventBus.getDefault().register(this);
        if (isExternalStorageWritable()) {
            try {
                mRecordFileStream = getRecordFileOutputStream();
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
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // -------------------------------------------------------------------------------------
    // Section : EventBus onEvent Method(s)
    // -------------------------------------------------------------------------------------
    public void onEvent(Measurement measurement) {
        try {
            String line = measurement.flush() + "\n";
            mRecordFileStream.write(line.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "Impossible d'ecrire dans le fichier contenant les trames bluetooth", e);
        }
    }

    // -------------------------------------------------------------------------------------
    // Section : Private Method(s)
    // -------------------------------------------------------------------------------------
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
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
        return "record" + "-" + date + ".txt";
    }
}
