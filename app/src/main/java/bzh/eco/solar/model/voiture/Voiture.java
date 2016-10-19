package bzh.eco.solar.model.voiture;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.element.ElementVoiture;
import bzh.eco.solar.model.voiture.element.impl.batterie.Batterie;
import bzh.eco.solar.model.voiture.element.impl.clignotant.ClignotantDroit;
import bzh.eco.solar.model.voiture.element.impl.clignotant.ClignotantGauche;
import bzh.eco.solar.model.voiture.element.impl.clignotant.Warning;
import bzh.eco.solar.model.voiture.element.impl.kelly.KellyDroit;
import bzh.eco.solar.model.voiture.element.impl.kelly.KellyGauche;
import de.greenrobot.event.EventBus;

public class Voiture {

    private List<ElementVoiture> mElementsVoitures = null;

    private static Voiture mInstance = null;

    private Location mCoordonneesGPS = null;

    public static synchronized Voiture getInstance() {
        if (mInstance == null) {
            mInstance = new Voiture();
        }

        return mInstance;
    }

    private Voiture() {
        mElementsVoitures = new ArrayList<>();
        mElementsVoitures.add(ClignotantGauche.getInstance());
        mElementsVoitures.add(ClignotantDroit.getInstance());
        mElementsVoitures.add(Warning.getInstance());
        mElementsVoitures.add(Batterie.getInstance());
        mElementsVoitures.add(KellyGauche.getInstance());
        mElementsVoitures.add(KellyDroit.getInstance());
    }

    public void update(BluetoothFrame frame) {
        for (ElementVoiture elementVoiture : mElementsVoitures) {
            if (elementVoiture.getIds().contains(frame.getId())) {
                elementVoiture.update(frame);
            }
        }

        EventBus.getDefault().post(frame.getId());
    }

    public Location getCoordonneesGPS() {
        return mCoordonneesGPS;
    }

    public void setCoordonneesGPS(Location coordonneesGPS) {
        this.mCoordonneesGPS = coordonneesGPS;
    }
}
