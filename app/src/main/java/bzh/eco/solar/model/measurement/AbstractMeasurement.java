package bzh.eco.solar.model.measurement;

import java.io.Serializable;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public abstract class AbstractMeasurement implements Serializable {

    private int id;

    private Measurement type;

    private String meaning;

    private ConvertType convertType;

    protected double value;

    public AbstractMeasurement(int id, String meaning, Measurement type, ConvertType convertType) {
        this.id = id;
        this.meaning = meaning;
        this.type = type;
        this.convertType = convertType;
        this.value = 0.0;
    }

    public int getID() {
        return id;
    }

    public Measurement getType() {
        return type;
    }

    public String getMeaning() {
        return meaning;
    }

    @Override
    public String toString() {
        return "AbstractMeasurement{" +
                "id=" + id +
                ", type=" + type +
                ", meaning='" + meaning + '\'' +
                '}';
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

    public static enum Measurement {
        TEMPERATURE,
        ELECTRICAL_POWER,
        SPEED
    }

    public static enum ConvertType {
        INTEGER,
        FLOAT
    }
}
