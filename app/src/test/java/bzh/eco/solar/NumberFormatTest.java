package bzh.eco.solar;

import org.junit.Test;

import android.test.suitebuilder.annotation.SmallTest;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * @author : Clément.Tréguer
 */
@SmallTest
public class NumberFormatTest {

    @Test
    public void test_numberformat() {
        NumberFormat numberFormat = DecimalFormat.getInstance(Locale.FRANCE);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);

        //assertEquals("1 234", numberFormat.format(1234));
        assertEquals("123", numberFormat.format(123));
        assertEquals("123", numberFormat.format(123.00));
        assertEquals("123,25", numberFormat.format(123.25));
        assertEquals("123,25", numberFormat.format(123.251));
        assertEquals("123,26", numberFormat.format(123.255));
        assertEquals("123,26", numberFormat.format(123.256));
    }

}
