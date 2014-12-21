package bzh.eco.solar.manager;

import java.util.Arrays;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public class SolarPanelElecticalPowerManager extends BluetoothFrameManager {

    private static SolarPanelElecticalPowerManager instance = null;

    private SolarPanelElecticalPowerManager() {
        super();

        mType = TYPE.SOLAR_PANEL_ELECTRICAL_POWER;
        mIDs = Arrays.asList(51, 52, 53, 54, 55);
    }

    public static SolarPanelElecticalPowerManager getInstance() {
        if (instance == null) {
            instance = new SolarPanelElecticalPowerManager();
        }

        return instance;
    }

    @Override
    protected void processFrame(BluetoothFrame frame) {
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

            double data = Character.getNumericValue(decade) * 10;
            data += Character.getNumericValue(unit);
            data += Character.getNumericValue(tenth) * 0.1;
            data += Character.getNumericValue(hundredth) * 0.01;

            frame.setData(data);
        }
    }
}
