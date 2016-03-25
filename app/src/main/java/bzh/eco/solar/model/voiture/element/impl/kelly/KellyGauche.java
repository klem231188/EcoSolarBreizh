package bzh.eco.solar.model.voiture.element.impl.kelly;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.Ids;
import bzh.eco.solar.model.voiture.element.ElementVoiture;

/**
 * Created by Eco Solar Breizh on 06/02/2016.
 */
public class KellyGauche implements ElementVoiture {

    private Etat etat = Etat.ETEINT;

    private MSB msb;

    private LSB lsb;

    private static KellyGauche mInstance = null;

    public static KellyGauche getInstance() {
        if (mInstance == null) {
            mInstance = new KellyGauche();
        }

        return mInstance;
    }

    public KellyGauche() {
        msb = new MSB(0);
        lsb = new LSB(0);
    }

    @Override
    public List<Integer> getIds() {
        return Arrays.asList(Ids.KELLY_GAUCHE_MSB, Ids.KELLY_GAUCHE_LSB);
    }

    @Override
    public void update(BluetoothFrame frame) {
        switch (frame.getId()) {
            case Ids.KELLY_GAUCHE_MSB:
                msb = new MSB(frame.asInteger());
                break;
            case Ids.KELLY_GAUCHE_LSB:
                lsb = new LSB(frame.asInteger());
                break;
            default:
                break;
        }

        if(msb.getErreur() == Erreurs.SANS_ERREUR && lsb.getErreur() == Erreurs.SANS_ERREUR) {
            etat = Etat.OK;
        } else{
            etat = Etat.ERREUR;
        }

        Log.i("KellyGauche", toString());
    }

    public Etat getEtat() {
        return etat;
    }

    public LSB getLsb() {
        return lsb;
    }

    public MSB getMsb() {
        return msb;
    }

    @Override
    public String toString() {
        return "KellyGauche{" +
                "etat=" + etat +
                ", msb=" + msb +
                ", lsb=" + lsb +
                '}';
    }
}
