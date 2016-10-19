package bzh.eco.solar.model.voiture.element.impl.clignotant;

import java.util.Arrays;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.Etat;
import bzh.eco.solar.model.voiture.Ids;
import bzh.eco.solar.model.voiture.element.ElementVoiture;

/**
 * @author : Clément.Tréguer
 */
public class ClignotantGauche implements ElementVoiture {

    public static final int CLIGNONANT_GAUCHE_ACTIF = 20;

    private static ClignotantGauche mInstance = null;

    private Etat mEtat = null;

    public static ClignotantGauche getInstance() {
        if (mInstance == null) {
            mInstance = new ClignotantGauche();
        }

        return mInstance;
    }

    public ClignotantGauche() {
        mEtat = Etat.INACTIF;
    }

    @Override
    public List<Integer> getIds() {
        return Arrays.asList(Ids.CLIGNOTANTS);
    }

    @Override
    public void update(BluetoothFrame frame) {
        switch (frame.getId()) {
            case Ids.CLIGNOTANTS:
                if (frame.asInteger() == CLIGNONANT_GAUCHE_ACTIF) {
                    mEtat = Etat.ACTIF;
                } else {
                    mEtat = Etat.INACTIF;
                }
                break;
            default:
                break;
        }
    }

    public Etat getEtat() {
        return mEtat;
    }

    public void setEtat(Etat etat) {
        mEtat = etat;
    }
}
