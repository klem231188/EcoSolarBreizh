package bzh.eco.solar.model.voiture.element.impl.kelly;

import junit.framework.Assert;

import org.junit.Test;

import bzh.eco.solar.model.bluetooth.BluetoothFrame;

/**
 * Created by Eco Solar Breizh on 05/03/2016.
 */
public class KellyTest {

    private KellyGauche kellyGauche = new KellyGauche();

    @Test
    public void when_0x4008_should_return_Error_Ox43_and_Error_0x14() {
        // 0x4008
        // --> MSB = 64 = 0x40 --> Erreur Ox43
        // Given
        char[] bufferMSB = {'3', '1', '0', '0', '6', '4', '+', '+'};
        BluetoothFrame frameMSB = BluetoothFrame.makeInstance(bufferMSB);

        // When
        kellyGauche.update(frameMSB);

        // Then
        Assert.assertEquals(Erreurs.ERREUR_MSB_0x43, kellyGauche.getMsb().getErreur());

        // --> LSB = 8 = 0x08 --> Erreur Ox14
        char[] bufferLSB = {'3', '2', '0', '0', '0', '8', '+', '+'};
        BluetoothFrame frameLSB = BluetoothFrame.makeInstance(bufferLSB);

        // When
        kellyGauche.update(frameLSB);

        // Then
        Assert.assertEquals(Erreurs.ERREUR_LSB_0x14, kellyGauche.getLsb().getErreur());
    }

}
