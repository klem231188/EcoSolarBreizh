package bzh.eco.solar.model.voiture.element.impl.kelly;

import android.util.Log;

/**
 * Created by Eco Solar Breizh on 05/03/2016.
 */
public class MSB {

    private static final Erreur[] ERREURS = {
            Erreurs.ERREUR_MSB_0x31,
            Erreurs.ERREUR_MSB_0x32,
            Erreurs.ERREUR_MSB_0x33,
            Erreurs.ERREUR_MSB_0x34,
            Erreurs.ERREUR_MSB_0x41,
            Erreurs.ERREUR_MSB_0x42,
            Erreurs.ERREUR_MSB_0x43,
            Erreurs.ERREUR_MSB_0x44,
    };

    private final String binary;

    private final Erreur erreur;

    public MSB(Integer decimal) {
        binary = Integer.toBinaryString(decimal);
        erreur = Erreur.from(binary, ERREURS);
    }

    public Erreur getErreur() {
        return erreur;
    }

    @Override
    public String toString() {
        return "MSB{" +
                "binary='" + binary + '\'' +
                ", erreur=" + erreur +
                '}';
    }
}
