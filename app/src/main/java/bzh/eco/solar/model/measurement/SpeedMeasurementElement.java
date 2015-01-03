package bzh.eco.solar.model.measurement;

/**
 * @author : Clément.Tréguer
 */
public class SpeedMeasurementElement extends AbstractMeasurementElement {

    public SpeedMeasurementElement(int id, String meaning, ConvertType convertType) {
        super(id, meaning, Measurement.SPEED, convertType);
    }

    public double getSpeed() {
        return value;
    }
}
