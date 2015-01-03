package bzh.eco.solar.model.measurement;

/**
 * @author : Clément.Tréguer
 */
public class TemperatureMeasurementElement extends AbstractMeasurementElement {

    public TemperatureMeasurementElement(int id, String meaning, ConvertType convertType) {
        super(id, meaning, Measurement.TEMPERATURE, convertType);
    }

    public double getTemperature() {
        return value;
    }
}
