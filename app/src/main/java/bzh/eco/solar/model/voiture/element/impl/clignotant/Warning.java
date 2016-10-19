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
public class Warning implements ElementVoiture {
    public static final int WARNING_ACTIF = 3;

    private static Warning mInstance = null;

    private Etat mEtat = null;

    public static Warning getInstance() {
        if (mInstance == null) {
            mInstance = new Warning();
        }

        return mInstance;
    }

    public Warning() {
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
                if (frame.asInteger() == WARNING_ACTIF) {
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
