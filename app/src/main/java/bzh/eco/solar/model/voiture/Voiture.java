package bzh.eco.solar.model.voiture;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.element.ElementVoiture;
import bzh.eco.solar.model.voiture.element.impl.ClignotantGauche;

/**
 * @author : Clément.Tréguer
 */
public class Voiture {

    private List<ElementVoiture> mElementVoitures = null;

    private static Voiture mInstance = null;

    public static Voiture getInstance() {
        if (mInstance == null) {
            mInstance = new Voiture();
        }

        return mInstance;
    }

    private Voiture() {
        mElementVoitures = new ArrayList<>();
        mElementVoitures.add(new ClignotantGauche());
    }

    public void update(BluetoothFrame frame) {
        for (ElementVoiture elementVoiture : mElementVoitures) {
            if(elementVoiture.getId() == frame.getId()){
                elementVoiture.update(frame);
                break;
            }
        }
    }

}
