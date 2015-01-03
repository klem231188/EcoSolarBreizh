package bzh.eco.solar.model.measurement;

/**
 * @author : Clément.Tréguer
 */
public class ElectricalPowerMeasurementElement extends AbstractMeasurementElement {

    public ElectricalPowerMeasurementElement(int id, String meaning, ConvertType convertType) {
        super(id, meaning, Measurement.ELECTRICAL_POWER, convertType);
    }

    public double getElectricalPower() {
        return value;
    }
}
