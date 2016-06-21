package bzh.eco.solar;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

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

        assertEquals("123", numberFormat.format(123));
        assertEquals("123", numberFormat.format(123.00));
        assertEquals("123,26", numberFormat.format(123.256));
    }

}
