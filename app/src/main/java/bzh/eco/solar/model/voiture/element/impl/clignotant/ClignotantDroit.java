package bzh.eco.solar.model.voiture.element.impl.clignotant;

import java.util.Arrays;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.Etat;
import bzh.eco.solar.model.voiture.element.ElementVoiture;
import de.greenrobot.event.EventBus;

/**
 * @author : Clément.Tréguer
 */
public class ClignotantDroit implements ElementVoiture {

    private Etat mEtat = null;

    public ClignotantDroit() {
        mEtat = Etat.INACTIF;
    }

    @Override
    public List<Integer> getIds() {
        // TODO : Trouver l'id du clignotant droit
        return Arrays.asList(666);
    }

    @Override
    public void update(BluetoothFrame frame) {
        // TODO : Conversion ici
        EventBus.getDefault().post(this);
    }

    public Etat getEtat() {
        return mEtat;
    }

    public void setEtat(Etat etat) {
        mEtat = etat;
    }
}
