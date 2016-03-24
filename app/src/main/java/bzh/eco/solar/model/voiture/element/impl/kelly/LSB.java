package bzh.eco.solar.model.voiture.element.impl.kelly;

import android.util.Log;

/**
 * Created by Eco Solar Breizh on 05/03/2016.
 */
public class LSB {

    private static final Erreur[] ERREURS = {
            Erreurs.ERREUR_LSB_0x11,
            Erreurs.ERREUR_LSB_0x12,
            Erreurs.ERREUR_LSB_0x13,
            Erreurs.ERREUR_LSB_0x14,
            Erreurs.ERREUR_LSB_0x21,
            Erreurs.ERREUR_LSB_0x22,
            Erreurs.ERREUR_LSB_0x23,
            Erreurs.ERREUR_LSB_0x24,
    };

    private final String binary;

    private final Erreur erreur;

    public LSB(Integer decimal) {
        binary = Integer.toBinaryString(decimal);
        erreur = Erreur.from(binary, ERREURS);
    }

    public Erreur getErreur() {
        return erreur;
    }

    @Override
    public String toString() {
        return "LSB{" +
                "binary='" + binary + '\'' +
                ", erreur=" + erreur +
                '}';
    }
}
