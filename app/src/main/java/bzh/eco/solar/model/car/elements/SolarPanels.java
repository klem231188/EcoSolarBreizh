package bzh.eco.solar.model.car.elements;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.Measurement;

/**
 * @author : Clément.Tréguer
 */
public class SolarPanels implements Car.CarElement {

    private static final String TAG = "SolarPanels";

    private static SolarPanels mInstance = null;

    private List<Measurement> mMeasurements = null;

    private List<Measurement> mElectricalPowerMeasurements = null;

    private List<Measurement> mTemperatureMeasurements = null;

    public static SolarPanels getInstance() {
        if (mInstance == null) {
            mInstance = new SolarPanels();
            mInstance.init();
        }

        return mInstance;
    }

    private void init() {
        initElectricalPowerMeasurements();
        initTemperatureMeasurements();

        mMeasurements = new ArrayList<Measurement>();
        mMeasurements.addAll(mElectricalPowerMeasurements);
        mMeasurements.addAll(mTemperatureMeasurements);
    }

    private void initTemperatureMeasurements() {
        mTemperatureMeasurements = new ArrayList<Measurement>();

        mTemperatureMeasurements.add(Measurement.Builder.SolarPanel.buildTemperatureMeasurement(11, "Température avant des panneaux"));
        mTemperatureMeasurements.add(Measurement.Builder.SolarPanel.buildTemperatureMeasurement(12, "Température milieu des panneaux"));
        mTemperatureMeasurements.add(Measurement.Builder.SolarPanel.buildTemperatureMeasurement(13, "Température arrière des panneaux"));
    }

    private void initElectricalPowerMeasurements() {
        mElectricalPowerMeasurements = new ArrayList<Measurement>();

        mElectricalPowerMeasurements.add(Measurement.Builder.SolarPanel.buildElectricalMeasurement(52, "Panneau 1 secteur 1-2"));
        mElectricalPowerMeasurements.add(Measurement.Builder.SolarPanel.buildElectricalMeasurement(51, "Panneau 2 secteur 3-4"));
        mElectricalPowerMeasurements.add(Measurement.Builder.SolarPanel.buildElectricalMeasurement(53, "Panneau 3 secteur 5-6"));
        mElectricalPowerMeasurements.add(Measurement.Builder.SolarPanel.buildElectricalMeasurement(56, "Panneau 4 secteur 7-8"));
        mElectricalPowerMeasurements.add(Measurement.Builder.SolarPanel.buildElectricalMeasurement(57, "Panneau 5 secteur 9-10"));
        mElectricalPowerMeasurements.add(Measurement.Builder.SolarPanel.buildElectricalMeasurement(58, "Courant global des panneaux"));
    }

    @Override
    public boolean isFrameAccepted(BluetoothFrame frame) {
        for (Measurement measurement : mMeasurements) {
            if (frame.getID() == measurement.getID()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Car.ElementType getType() {
        return Car.ElementType.SOLAR_PANEL;
    }

    @Override
    public void update(BluetoothFrame frame, Context context) {
        Log.i(TAG, "update(" + frame.toString() + ")");

        for (Measurement measurement : mMeasurements) {
            if (frame.getID() == measurement.getID()) {
                measurement.update(frame);

                Intent intent = new Intent(getType().name());
                intent.putExtra("MEASUREMENT_TYPE", measurement.getType());
                context.sendBroadcast(intent);

                break;
            }
        }
    }

    public List<Measurement> getElectricalPowerMeasurements() {
        return mElectricalPowerMeasurements;
    }

    public List<Measurement> getTemperatureMeasurements() {
        return mTemperatureMeasurements;
    }
}
