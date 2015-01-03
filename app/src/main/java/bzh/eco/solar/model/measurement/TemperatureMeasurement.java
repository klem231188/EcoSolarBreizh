package bzh.eco.solar.model.measurement;

/**
 * @author : Clément.Tréguer
 */
public class TemperatureMeasurement extends AbstractMeasurement {

    public TemperatureMeasurement(int id, String meaning, ConvertType convertType) {
        super(id, meaning, Measurement.TEMPERATURE, convertType);
    }

    public double getTemperature() {
        return value;
    }
}
