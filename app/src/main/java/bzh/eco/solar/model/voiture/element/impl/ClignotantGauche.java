package bzh.eco.solar.model.voiture.element.impl;

import java.util.Random;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.Etat;
import bzh.eco.solar.model.voiture.element.ElementVoiture;
import de.greenrobot.event.EventBus;

/**
 * @author : Clément.Tréguer
 */
public class ClignotantGauche implements ElementVoiture {

    private Etat mEtat = null;

    public ClignotantGauche() {
        mEtat = Etat.INACTIF;
    }

    @Override
    public int getId() {
        // TODO : Trouver l'id du clignotant gauche
        return 23;
    }

    @Override
    public void update(BluetoothFrame frame) {
        // TODO : Conversion ici
        if (new Random().nextBoolean()) {
            mEtat = Etat.ACTIF;
        } else {
            mEtat = Etat.INACTIF;
        }

        EventBus.getDefault().post(this);
    }

    public Etat getEtat() {
        return mEtat;
    }

    public void setEtat(Etat etat) {
        mEtat = etat;
    }
}
