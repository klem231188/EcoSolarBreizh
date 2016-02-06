package bzh.eco.solar.model.voiture.element;

import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * @author : Clément.Tréguer
 */
public interface ElementVoiture {

    List<Integer> getIds();

    void update(BluetoothFrame frame);

}
