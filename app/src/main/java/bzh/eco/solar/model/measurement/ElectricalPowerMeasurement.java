package bzh.eco.solar.model.measurement;

/**
 * @author : Clément.Tréguer
 */
public class ElectricalPowerMeasurement extends AbstractMeasurement {

    public ElectricalPowerMeasurement(int id, String meaning, ConvertType convertType) {
        super(id, meaning, Measurement.ELECTRICAL_POWER, convertType);
    }

    public double getElectricalPower() {
        return value;
    }
}
