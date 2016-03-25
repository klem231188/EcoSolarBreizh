package bzh.eco.solar.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bzh.eco.solar.R;
import bzh.eco.solar.model.bluetooth.BluetoothFrame;
import bzh.eco.solar.model.voiture.Ids;
import bzh.eco.solar.model.voiture.Voiture;
import bzh.eco.solar.model.voiture.element.impl.kelly.KellyDroit;
import bzh.eco.solar.model.voiture.element.impl.kelly.KellyGauche;
import de.greenrobot.event.EventBus;

public class KellyFragment extends Fragment {

    // -------------------------------------------------------------------------------------
    // Section : Static Fields(s)
    // -------------------------------------------------------------------------------------
    public static final String TAG = "KellyFragment";

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private TextView mTextViewEtatKellyGauche;
    private TextView mTextViewErreurValeurHexaLSBKellyGauche;
    private TextView mTextViewErreurSignificationLSBKellyGauche;
    private TextView mTextViewErreurDetailsLSBKellyGauche;
    private TextView mTextViewErreurValeurHexaMSBKellyGauche;
    private TextView mTextViewErreurSignificationMSBKellyGauche;
    private TextView mTextViewErreurDetailsMSBKellyGauche;

    private TextView mTextViewEtatKellyDroit;
    private TextView mTextViewErreurValeurHexaLSBKellyDroit;
    private TextView mTextViewErreurSignificationLSBKellyDroit;
    private TextView mTextViewErreurDetailsLSBKellyDroit;
    private TextView mTextViewErreurValeurHexaMSBKellyDroit;
    private TextView mTextViewErreurSignificationMSBKellyDroit;
    private TextView mTextViewErreurDetailsMSBKellyDroit;

    public KellyFragment() {
    }

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s) / Factory
    // -------------------------------------------------------------------------------------
    public static KellyFragment newInstance() {
        return new KellyFragment();
    }

    // -------------------------------------------------------------------------------------
    // Section : Android Lifecycle Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kelly, container, false);

        mTextViewEtatKellyGauche = (TextView) root.findViewById(R.id.text_view_etat_kelly_gauche);
        mTextViewErreurValeurHexaLSBKellyGauche = (TextView) root.findViewById(R.id.text_view_erreur_valeur_hexa_lsb_kelly_gauche);
        mTextViewErreurSignificationLSBKellyGauche = (TextView) root.findViewById(R.id.text_view_erreur_signification_lsb_kelly_gauche);
        mTextViewErreurDetailsLSBKellyGauche = (TextView) root.findViewById(R.id.text_view_erreur_detail_lsb_kelly_gauche);
        mTextViewErreurValeurHexaMSBKellyGauche = (TextView) root.findViewById(R.id.text_view_erreur_valeur_hexa_msb_kelly_gauche);
        mTextViewErreurSignificationMSBKellyGauche = (TextView) root.findViewById(R.id.text_view_erreur_signification_msb_kelly_gauche);
        mTextViewErreurDetailsMSBKellyGauche = (TextView) root.findViewById(R.id.text_view_erreur_detail_msb_kelly_gauche);

        mTextViewEtatKellyDroit = (TextView) root.findViewById(R.id.text_view_etat_kelly_droit);
        mTextViewErreurValeurHexaLSBKellyDroit = (TextView) root.findViewById(R.id.text_view_erreur_valeur_hexa_lsb_kelly_droit);
        mTextViewErreurSignificationLSBKellyDroit = (TextView) root.findViewById(R.id.text_view_erreur_signification_lsb_kelly_droit);
        mTextViewErreurDetailsLSBKellyDroit = (TextView) root.findViewById(R.id.text_view_erreur_detail_lsb_kelly_droit);
        mTextViewErreurValeurHexaMSBKellyDroit = (TextView) root.findViewById(R.id.text_view_erreur_valeur_hexa_msb_kelly_droit);
        mTextViewErreurSignificationMSBKellyDroit = (TextView) root.findViewById(R.id.text_view_erreur_signification_msb_kelly_droit);
        mTextViewErreurDetailsMSBKellyDroit = (TextView) root.findViewById(R.id.text_view_erreur_detail_msb_kelly_droit);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        char[] bufferMSB = {'3', '1', '0', '0', '0', '0', '+', '+'};
        BluetoothFrame frameMSB = BluetoothFrame.makeInstance(bufferMSB);

        char[] bufferLSB = {'3', '2', '0', '0', '0', '8', '+', '+'};
        BluetoothFrame frameLSB = BluetoothFrame.makeInstance(bufferLSB);

        Voiture.getInstance().update(frameMSB);
        Voiture.getInstance().update(frameLSB);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // -------------------------------------------------------------------------------------
    // Section : EventBus onEvent Method(s)
    // -------------------------------------------------------------------------------------
    public void onEvent(Integer id) {
        switch (id) {
            case Ids.KELLY_GAUCHE_LSB:
                mTextViewErreurValeurHexaLSBKellyGauche.setText(KellyGauche.getInstance().getLsb().getErreur().getValeurHexa());
                mTextViewErreurSignificationLSBKellyGauche.setText(KellyGauche.getInstance().getLsb().getErreur().getSignification());
                mTextViewErreurDetailsLSBKellyGauche.setText(KellyGauche.getInstance().getLsb().getErreur().getDetail());
                break;
            case Ids.KELLY_GAUCHE_MSB:
                mTextViewErreurValeurHexaMSBKellyGauche.setText(KellyGauche.getInstance().getMsb().getErreur().getValeurHexa());
                mTextViewErreurSignificationMSBKellyGauche.setText(KellyGauche.getInstance().getMsb().getErreur().getSignification());
                mTextViewErreurDetailsMSBKellyGauche.setText(KellyGauche.getInstance().getMsb().getErreur().getDetail());
                break;
            case Ids.KELLY_DROIT_LSB:
                mTextViewErreurValeurHexaLSBKellyDroit.setText(KellyDroit.getInstance().getLsb().getErreur().getValeurHexa());
                mTextViewErreurSignificationLSBKellyDroit.setText(KellyDroit.getInstance().getLsb().getErreur().getSignification());
                mTextViewErreurDetailsLSBKellyDroit.setText(KellyDroit.getInstance().getLsb().getErreur().getDetail());
                break;
            case Ids.KELLY_DROIT_MSB:
                mTextViewErreurValeurHexaMSBKellyDroit.setText(KellyDroit.getInstance().getMsb().getErreur().getValeurHexa());
                mTextViewErreurSignificationMSBKellyDroit.setText(KellyDroit.getInstance().getMsb().getErreur().getSignification());
                mTextViewErreurDetailsMSBKellyDroit.setText(KellyDroit.getInstance().getMsb().getErreur().getDetail());
                break;
        }

        switch (KellyGauche.getInstance().getEtat()) {
            case ETEINT:
                mTextViewEtatKellyGauche.setCompoundDrawablesWithIntrinsicBounds(R.drawable.orange_button, 0, 0, 0);
                break;
            case OK:
                mTextViewEtatKellyGauche.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_button, 0, 0, 0);
                break;
            case ERREUR:
                mTextViewEtatKellyGauche.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_button, 0, 0, 0);
                break;
        }
        mTextViewEtatKellyGauche.setText(KellyGauche.getInstance().getEtat().toString());

        switch (KellyDroit.getInstance().getEtat()) {
            case ETEINT:
                mTextViewEtatKellyDroit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.orange_button, 0, 0, 0);
                break;
            case OK:
                mTextViewEtatKellyDroit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_button, 0, 0, 0);
                break;
            case ERREUR:
                mTextViewEtatKellyDroit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_button, 0, 0, 0);
                break;
        }
        mTextViewEtatKellyDroit.setText(KellyDroit.getInstance().getEtat().toString());
    }
}
