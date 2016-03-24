package bzh.eco.solar.model.voiture;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.element.ElementVoiture;
import bzh.eco.solar.model.voiture.element.impl.batterie.Batterie;
import bzh.eco.solar.model.voiture.element.impl.clignotant.ClignotantDroit;
import bzh.eco.solar.model.voiture.element.impl.clignotant.ClignotantGauche;
import bzh.eco.solar.model.voiture.element.impl.kelly.KellyGauche;
import de.greenrobot.event.EventBus;

public class Voiture {

    private List<ElementVoiture> mElementsVoitures = null;

    private static Voiture mInstance = null;

    public static Voiture getInstance() {
        if (mInstance == null) {
            mInstance = new Voiture();
        }

        return mInstance;
    }

    private Voiture() {
        mElementsVoitures = new ArrayList<>();
        mElementsVoitures.add(new ClignotantGauche());
        mElementsVoitures.add(new ClignotantDroit());
        mElementsVoitures.add(Batterie.getInstance());
        mElementsVoitures.add(KellyGauche.getInstance());
    }

    public void update(BluetoothFrame frame) {
        for (ElementVoiture elementVoiture : mElementsVoitures) {
            if (elementVoiture.getIds().contains(frame.getId())) {
                elementVoiture.update(frame);
                break;
            }
        }

        EventBus.getDefault().post(frame.getId());
    }

}
