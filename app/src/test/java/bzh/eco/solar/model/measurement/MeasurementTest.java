package bzh.eco.solar.model.measurement;

import org.junit.Assert;
import org.junit.Test;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

public class MeasurementTest {

    @Test
    public void testUpdate_FloatingFrame() throws Exception {
        // Given
        char[] buffer = {50, 51, 49, 50, 51, 52, 43, 43};
        BluetoothFrame frame = BluetoothFrame.makeInstance(buffer);
        Measurement speed = new Measurement.Builder()
                .setId(23)
                .setConvertType(Measurement.ConvertType.FLOAT)
                .createMeasurement();

        // When
        speed.update(frame);

        // Then
        Assert.assertEquals(23, speed.getID());
        Assert.assertEquals(12.34, speed.getValue(), 0.001);
    }


    @Test
    public void testUpdate_IntegerFrame_Equals_12() throws Exception {
        // Given
        char[] buffer = {50, 51, 49, 50, 0, 0, 43, 43};
        BluetoothFrame frame = BluetoothFrame.makeInstance(buffer);
        Measurement speed = new Measurement.Builder()
                .setId(23)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();

        // When
        speed.update(frame);

        // Then
        Assert.assertEquals(23, speed.getID());
        Assert.assertEquals(12.0, speed.getValue(), 0.001);
    }

    @Test
    public void testUpdate_IntegerFrame_Equals_174() throws Exception {
        // Given
        char[] buffer = {50, 51, 49, 55, 52, 0, 43, 43};
        BluetoothFrame frame = BluetoothFrame.makeInstance(buffer);
        Measurement speed = new Measurement.Builder()
                .setId(23)
                .setConvertType(Measurement.ConvertType.INTEGER)
                .createMeasurement();

        // When
        speed.update(frame);

        // Then
        Assert.assertEquals(23, speed.getID());
        Assert.assertEquals(174.0, speed.getValue(), 0.001);
    }
}