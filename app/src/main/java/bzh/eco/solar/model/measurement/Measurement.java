package bzh.eco.solar.model.measurement;

import android.util.Log;

import java.io.Serializable;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import de.greenrobot.event.EventBus;

/**
 * @author : Clément.Tréguer
 */
public class Measurement implements Serializable {

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private final int id;

    private final String meaning;

    private final Type type;

    private final Unity unity;

    private final ConvertType convertType;

    private final double maxValue;

    private double value;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    private Measurement(Builder builder) {
        this.id = builder.mId;
        this.meaning = builder.mMeaning;
        this.type = builder.mType;
        this.unity = builder.mUnity;
        this.maxValue = builder.mMaxValue;
        this.convertType = builder.mConvertType;
        this.value = 0.0;
    }

    // -------------------------------------------------------------------------------------
    // Section : Methods(s)
    // -------------------------------------------------------------------------------------
    public void update(BluetoothFrame frame) {
        value = 0.0;

        if (convertType == ConvertType.INTEGER) {
            updateToInteger(frame);
        } else {
            updateToFloat(frame);
        }

        Log.i("Measurement", frame.toString());

        // Send to event bus changing state
        EventBus.getDefault().post(this);
    }

    private void updateToInteger(BluetoothFrame frame) {
        char[] dataArray = frame.getDataArray();
        int multiple = 1;
        for (int i = dataArray.length - 1; i >= 0; i--) {
            char data = dataArray[i];
            if (Character.isDigit(data)) {
                value += Character.getNumericValue(data) * multiple;
                multiple *= 10;
            }
        }
    }

    private void updateToFloat(BluetoothFrame frame) {
        char[] originalData = frame.getDataArray();
        char decade = originalData[0];
        char unit = originalData[1];
        char tenth = originalData[2];
        char hundredth = originalData[3];

        // Rule 1)
        if (unit == 0x00) {
            unit = decade;
            decade = '0';
        }
        // Rule 2)
        if (hundredth == 0x00) {
            hundredth = tenth;
            tenth = '0';
        }
        // Rule 3)
        if (Character.isDigit(decade)
                && Character.isDigit(unit)
                && Character.isDigit(tenth)
                && Character.isDigit(hundredth)) {

            value = Character.getNumericValue(decade) * 10;
            value += Character.getNumericValue(unit);
            value += Character.getNumericValue(tenth) * 0.1;
            value += Character.getNumericValue(hundredth) * 0.01;
        }
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", meaning='" + meaning + '\'' +
                ", type=" + type +
                ", unity=" + unity +
                ", convertType=" + convertType +
                ", maxValue=" + maxValue +
                ", value=" + value +
                '}';
    }

    // -------------------------------------------------------------------------------------
    // Section : Getter(s)/Setter(s)
    // -------------------------------------------------------------------------------------
    public int getID() {
        return id;
    }

    public String getMeaning() {
        return meaning;
    }

    public Type getType() {
        return type;
    }

    public Unity getUnity() {
        return unity;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getValue() {
        return value;
    }

    // -------------------------------------------------------------------------------------
    // Section : Inner class(es) & enums
    // -------------------------------------------------------------------------------------
    public enum Type {
        TEMPERATURE,
        ELECTRICAL_POWER,
        SPEED
    }

    public enum Unity {
        CELSIUS("°C"),
        AMPERE("A"),
        MILLI_AMPERE("mA"),
        KILOMETER_PER_HOUR("Km/h"),
        PERCENTAGE("%"),
        RPM("Tr/Min");

        private String value;

        Unity(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ConvertType {
        INTEGER,
        FLOAT
    }

    public static class Builder {

        private int mId;

        private String mMeaning;

        private Measurement.Type mType;

        private Measurement.Unity mUnity;

        private double mMaxValue;

        private Measurement.ConvertType mConvertType;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setMeaning(String meaning) {
            mMeaning = meaning;
            return this;
        }

        public Builder setType(Measurement.Type type) {
            mType = type;
            return this;
        }

        public Builder setUnity(Measurement.Unity unity) {
            mUnity = unity;
            return this;
        }

        public Builder setMaxValue(double maxValue) {
            mMaxValue = maxValue;
            return this;
        }

        public Builder setConvertType(Measurement.ConvertType convertType) {
            mConvertType = convertType;
            return this;
        }

        public Measurement createMeasurement() {
            return new Measurement(this);
        }
    }
}
