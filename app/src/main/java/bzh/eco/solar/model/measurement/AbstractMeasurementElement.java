package bzh.eco.solar.model.measurement;

import java.io.Serializable;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public abstract class AbstractMeasurementElement implements Serializable {

    private int id;
    private Measurement type;
    private String meaning;

    public AbstractMeasurementElement(int id, String meaning, Measurement type) {
        this.id = id;
        this.meaning = meaning;
        this.type = type;
    }

    public int getId() {
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
        return "AbstractMeasurementElement{" +
                "id=" + id +
                ", type=" + type +
                ", meaning='" + meaning + '\'' +
                '}';
    }

    public abstract void update(BluetoothFrame frame);

    public enum Measurement {
        TEMPERATURE,
        ELECTRICAL_POWER,
        SPEED
    }
}
