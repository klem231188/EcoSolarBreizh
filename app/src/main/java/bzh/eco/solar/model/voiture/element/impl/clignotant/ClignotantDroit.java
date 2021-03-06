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
public class ClignotantDroit implements ElementVoiture {

    public static final int CLIGNOTANT_DROIT_ACTIF = 100;

    private static ClignotantDroit mInstance = null;

    private Etat mEtat = null;

    public static ClignotantDroit getInstance() {
        if (mInstance == null) {
            mInstance = new ClignotantDroit();
        }

        return mInstance;
    }

    public ClignotantDroit() {
        mEtat = Etat.INACTIF;
    }

    @Override
    public List<Integer> getIds() {
        return Arrays.asList(Ids.CLIGNOTANTS);
    }

    //0... --> rien
    //100. --> clignotant droit
    //20.  --> clignotant gauche
    //3... --> warning

    @Override
    public void update(BluetoothFrame frame) {
        switch (frame.getId()) {
            case Ids.CLIGNOTANTS:
                if (frame.asInteger() == CLIGNOTANT_DROIT_ACTIF) {
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
