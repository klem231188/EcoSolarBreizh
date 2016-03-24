package bzh.eco.solar.model.voiture.element.impl.batterie;

import java.util.Arrays;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.Ids;
import bzh.eco.solar.model.voiture.element.ElementVoiture;

/**
 * Created by Eco Solar Breizh on 06/02/2016.
 */
public class Batterie implements ElementVoiture {

    private static Batterie mInstance = null;

    private int tension = 0;

    public static Batterie getInstance() {
        if (mInstance == null) {
            mInstance = new Batterie();
        }

        return mInstance;
    }

    @Override
    public List<Integer> getIds() {
        return Arrays.asList(Ids.TENSION_BATTERIE);
    }

    @Override
    public void update(BluetoothFrame frame) {
        switch (frame.getId()) {
            case Ids.TENSION_BATTERIE:
                tension = frame.asInteger();
                break;
            default:
                break;
        }
    }

    public double getTension() {
        return tension;
    }
}
