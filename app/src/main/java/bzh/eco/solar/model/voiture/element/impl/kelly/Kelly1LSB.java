package bzh.eco.solar.model.voiture.element.impl.kelly;

import java.util.Arrays;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.Ids;
import bzh.eco.solar.model.voiture.element.ElementVoiture;

/**
 * @author : Clément.Tréguer
 */
public class Kelly1LSB implements ElementVoiture {


    @Override
    public List<Integer> getIds() {
        return Arrays.asList(Ids.VARIATEUR_KELLY_1_LSB);
    }

    @Override
    public void update(BluetoothFrame frame) {

    }
}
