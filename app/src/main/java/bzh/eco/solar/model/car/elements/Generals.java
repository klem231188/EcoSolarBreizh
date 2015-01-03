package bzh.eco.solar.model.car.elements;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.Car;
import bzh.eco.solar.model.measurement.AbstractMeasurementElement;
import bzh.eco.solar.model.measurement.AbstractMeasurementElement.ConvertType;
import bzh.eco.solar.model.measurement.ElectricalPowerMeasurementElement;
import bzh.eco.solar.model.measurement.SpeedMeasurementElement;
import bzh.eco.solar.model.measurement.TemperatureMeasurementElement;

/**
 * @author : Clément.Tréguer
 */
public class Generals implements Car.CarElement {

    private static final String TAG = "Generals";

    private static Generals mInstance = null;

    private List<AbstractMeasurementElement> mMeasurementElements = null;

    private List<ElectricalPowerMeasurementElement> mElectricalPowerMeasurementElements = null;

    private List<TemperatureMeasurementElement> mTemperatureMeasurementElements = null;

    private List<SpeedMeasurementElement> mSpeedMeasurementElements = null;

    public static Generals getInstance() {
        if (mInstance == null) {
            mInstance = new Generals();
            mInstance.init();
        }

        return mInstance;
    }

    private void init() {
        initElectricalPowerMeasurementElements();
        initTemperatureMeasurementElements();
        initSpeedMeasurementElements();

        mMeasurementElements = new ArrayList<AbstractMeasurementElement>();
        mMeasurementElements.addAll(mElectricalPowerMeasurementElements);
        mMeasurementElements.addAll(mTemperatureMeasurementElements);
        mMeasurementElements.addAll(mSpeedMeasurementElements);
    }

    private void initTemperatureMeasurementElements() {
        mTemperatureMeasurementElements = new ArrayList<TemperatureMeasurementElement>();

        mTemperatureMeasurementElements.add(new TemperatureMeasurementElement(80, "Température Cockpit", ConvertType.INTEGER));
        mTemperatureMeasurementElements.add(new TemperatureMeasurementElement(81, "Température Processeur RX63N", ConvertType.INTEGER));
    }

    private void initElectricalPowerMeasurementElements() {
        mElectricalPowerMeasurementElements = new ArrayList<ElectricalPowerMeasurementElement>();

        mElectricalPowerMeasurementElements.add(new ElectricalPowerMeasurementElement(70, "Mesure du courant général consommé par l’électronique (12v et 5v) en mA", ConvertType.INTEGER));
    }

    private void initSpeedMeasurementElements() {
        mSpeedMeasurementElements = new ArrayList<SpeedMeasurementElement>();

        mSpeedMeasurementElements.add(new SpeedMeasurementElement(23, "Vitesse de la voiture", ConvertType.INTEGER));
        mSpeedMeasurementElements.add(new SpeedMeasurementElement(00, "Etat Régulateur", ConvertType.INTEGER));
        mSpeedMeasurementElements.add(new SpeedMeasurementElement(43, "Consigne de vitesse", ConvertType.INTEGER));
    }

    @Override
    public boolean isFrameAccepted(BluetoothFrame frame) {
        for (AbstractMeasurementElement measurementElement : mMeasurementElements) {
            if (frame.getID() == measurementElement.getID()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Car.ElementType getType() {
        return Car.ElementType.GENERAL;
    }

    @Override
    public void update(BluetoothFrame frame, Context context) {
        Log.i(TAG, "update(" + frame.toString() + ")");

        for (AbstractMeasurementElement measurementElement : mMeasurementElements) {
            if (frame.getID() == measurementElement.getID()) {
                measurementElement.update(frame);

                Intent intent = new Intent(getType().name());
                intent.putExtra("MEASUREMENT_TYPE", measurementElement.getType());
                intent.putExtra("MEASUREMENT_ELEMENT", measurementElement);
                context.sendBroadcast(intent);

                break;
            }
        }
    }
}
