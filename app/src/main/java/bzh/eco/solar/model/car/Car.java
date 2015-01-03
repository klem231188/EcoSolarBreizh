package bzh.eco.solar.model.car;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.elements.Generals;
import bzh.eco.solar.model.car.elements.Motors;
import bzh.eco.solar.model.car.elements.SolarPanels;

/**
 * @author : Clément.Tréguer
 */
public class Car {

    private static final String TAG = "Car";

    private static Car mInstance = null;

    private List<CarElement> mCarElements = null;

    public static Car getInstance() {
        if (mInstance == null) {
            mInstance = new Car();
            mInstance.init();
        }

        return mInstance;
    }

    private void init() {
        mCarElements = new ArrayList<CarElement>();
        mCarElements.add(Generals.getInstance());
        mCarElements.add(SolarPanels.getInstance());
        mCarElements.add(Motors.getInstance());
    }

    public void update(BluetoothFrame frame, Context context) {
        Log.i(TAG, "update(" + frame + ")");

        for (CarElement carElement : mCarElements) {
            if (carElement.isFrameAccepted(frame)) {
                carElement.update(frame, context);
            }
        }
    }

    public enum ElementType {
        SOLAR_PANEL,
        MOTOR,
        GENERAL
    }

    public interface CarElement {

        boolean isFrameAccepted(BluetoothFrame frame);

        ElementType getType();

        void update(BluetoothFrame frame, Context context);
    }
}

