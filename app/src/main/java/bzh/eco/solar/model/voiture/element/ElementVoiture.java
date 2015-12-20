package bzh.eco.solar.model.voiture.element;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public interface ElementVoiture {

    int getId();

    void update(BluetoothFrame frame);

}
