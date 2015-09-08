package bzh.eco.solar.model.car;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public interface CarCommand {

    boolean accepts(BluetoothFrame frame);

    void update(BluetoothFrame frame);
}
