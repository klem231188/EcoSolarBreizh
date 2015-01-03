package bzh.eco.solar.model.measurement;

import java.io.Serializable;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public class Measurement implements Serializable {

    private final int id;

    private final String meaning;

    private final Type type;

    private final Unity unity;

    private final ConvertType convertType;

    private final double maxValue;

    protected double value;

    public Measurement(int id, String meaning, Type type, Unity unity, double maxValue, ConvertType convertType) {
        this.id = id;
        this.meaning = meaning;
        this.type = type;
        this.unity = unity;
        this.maxValue = maxValue;
        this.convertType = convertType;
        this.value = 0.0;
    }

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

    public void update(BluetoothFrame frame) {
        if (convertType == ConvertType.INTEGER) {
            updateToInteger(frame);
        } else {
            updateToFloat(frame);
        }
    }

    // TODO : conversion correcte ? Voir avec sébastien.
    private void updateToInteger(BluetoothFrame frame) {
        char[] originalData = frame.getOriginalData();
        char decade = originalData[0];
        char unit = originalData[1];

        if (unit == 0x00) {
            unit = decade;
            decade = '0';
        }

        if (Character.isDigit(decade)
                && Character.isDigit(unit)
                ) {

            value = Character.getNumericValue(decade) * 10;
            value += Character.getNumericValue(unit);
        }
    }

    private void updateToFloat(BluetoothFrame frame) {
        char[] originalData = frame.getOriginalData();
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

    public static enum Type {
        TEMPERATURE,
        ELECTRICAL_POWER,
        SPEED
    }

    public static enum Unity {
        CELSIUS("°C"),
        AMPERE("A"),
        MILLI_AMPERE("mA"),
        KILOMETER_PER_HOUR("Km/h"),
        RPM("Tr/Min");

        private String value;

        public String getValue() {
            return value;
        }

        Unity(String value) {
            this.value = value;
        }
    }

    public static enum ConvertType {
        INTEGER,
        FLOAT
    }

    public static class Builder {

        public static class SolarPanel {
            public static Measurement buildElectricalMeasurement(int id, String meaning){
                return new Measurement(id, meaning, Type.ELECTRICAL_POWER, Unity.AMPERE, 1, ConvertType.FLOAT);
            }

            public static Measurement buildTemperatureMeasurement(int id, String meaning){
                return new Measurement(id, meaning, Type.TEMPERATURE, Unity.CELSIUS, 100, ConvertType.INTEGER);
            }
        }

        public static class Motors {
            public static Measurement buildElectricalMeasurement(int id, String meaning){
                return new Measurement(id, meaning, Type.ELECTRICAL_POWER, Unity.AMPERE, 1, ConvertType.INTEGER);
            }

            public static Measurement buildSpeedMeasurement(int id, String meaning){
                return new Measurement(id, meaning, Type.SPEED, Unity.RPM, 100, ConvertType.INTEGER);
            }

            public static Measurement buildTemperatureMeasurement(int id, String meaning){
                return new Measurement(id, meaning, Type.TEMPERATURE, Unity.CELSIUS, 100, ConvertType.INTEGER);
            }
        }
    }
}
