package bzh.eco.solar.model.car.commands;

import java.io.Serializable;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.car.CarCommand;
import de.greenrobot.event.EventBus;

/**
 * @author : Clément.Tréguer
 */
public class ClignotantWarningCommand implements CarCommand, Serializable {

    private static ClignotantWarningCommand mInstance = null;

    private Command command = Command.RIEN;

    public static ClignotantWarningCommand getInstance() {
        if (mInstance == null) {
            mInstance = new ClignotantWarningCommand();
        }

        return mInstance;
    }

    @Override
    public boolean accepts(BluetoothFrame frame) {
        return frame.getId() == 74;
    }

    @Override
    public void update(BluetoothFrame frame) {
        int commandValue = 0;

        char[] dataArray = frame.getDataArray();
        int multiple = 1;
        for (int i = dataArray.length - 1; i >= 0; i--) {
            char data = dataArray[i];
            if (Character.isDigit(data)) {
                commandValue += Character.getNumericValue(data) * multiple;
                multiple *= 10;
            }
        }

        switch (commandValue) {
            case 100:
                command = Command.CLIGNOTANT_GAUCHE_ON;
                break;
            case 20:
                command = Command.CLIGNOTANT_DROIT_ON;
                break;
            case 3:
                command = Command.WARNING_ON;
                break;
            default:
                command = Command.RIEN;
                break;
        }

        EventBus.getDefault().post(command);
    }

    public enum Command {
        CLIGNOTANT_DROIT_ON,
        CLIGNOTANT_GAUCHE_ON,
        WARNING_ON,
        RIEN
    }
}
