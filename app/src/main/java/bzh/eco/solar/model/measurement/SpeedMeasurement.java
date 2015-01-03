package bzh.eco.solar.model.measurement;

/**
 * @author : Clément.Tréguer
 */
public class SpeedMeasurement extends AbstractMeasurement {

    public SpeedMeasurement(int id, String meaning, ConvertType convertType) {
        super(id, meaning, Measurement.SPEED, convertType);
    }

    public double getSpeed() {
        return value;
    }
}
