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
public class KellyDroit implements ElementVoiture {

    private Etat etat = Etat.ETEINT;

    private MSB msb;

    private LSB lsb;

    private static KellyDroit mInstance = null;

    public static KellyDroit getInstance() {
        if (mInstance == null) {
            mInstance = new KellyDroit();
        }

        return mInstance;
    }

    public KellyDroit() {
        msb = new MSB(0);
        lsb = new LSB(0);
    }

    @Override
    public List<Integer> getIds() {
        return Arrays.asList(Ids.KELLY_DROIT_MSB, Ids.KELLY_DROIT_LSB);
    }

    @Override
    public void update(BluetoothFrame frame) {
        switch (frame.getId()) {
            case Ids.KELLY_DROIT_MSB:
                msb = new MSB(frame.asInteger());
                break;
            case Ids.KELLY_DROIT_LSB:
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

        Log.i("KellyDroit", toString());
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
        return "KellyDroit{" +
                "etat=" + etat +
                ", msb=" + msb +
                ", lsb=" + lsb +
                '}';
    }
}
