package bzh.eco.solar.model.voiture.element.impl.kelly;

import android.util.Log;

/**
 * Created by Eco Solar Breizh on 05/03/2016.
 */
public class Erreur {

    public final String valeurHexa;

    public final String signification;

    public final String detail;

    public Erreur(String valeurHexa, String signification, String detail) {
        this.valeurHexa = valeurHexa;
        this.signification = signification;
        this.detail = detail;
    }

    public static Erreur from(String binary, Erreur[] erreurs) {
        Erreur erreur = Erreurs.SANS_ERREUR;

        if (binary.length() > 8) {
            Log.e("Erreur", "La taille en binaire est supérieure à 8");
        } else {
            String binaryReverse = new StringBuilder(binary).reverse().toString();
            for (int index = 0; index < binaryReverse.length(); index++) {
                if ('1' == binaryReverse.charAt(index)) {
                    erreur = erreurs[index];
                    break;
                }
            }
        }

        Log.i("Erreur", "Erreur = " + erreur);

        return erreur;
    }


    public String getValeurHexa() {
        return valeurHexa;
    }

    public String getSignification() {
        return signification;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        return "Erreur{" +
                "valeurHexa='" + valeurHexa + '\'' +
                ", signification='" + signification + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
