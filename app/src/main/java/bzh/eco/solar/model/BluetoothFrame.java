package bzh.eco.solar.model;

import java.io.Serializable;

/**
 * DTO representing a bluetooth frame
 *
 * @author : Clément.Tréguer
 */
public class BluetoothFrame implements Serializable {

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    public char[] id;

    public char[] data;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public BluetoothFrame() {
        id = new char[2];
        data = new char[4];
    }

    // -------------------------------------------------------------------------------------
    // Section : Method(s)
    // -------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("ID = [")
                .append(Character.isDigit(id[0]) ? id[0] : '.')
                .append(Character.isDigit(id[1]) ? id[1] : '.')
                .append("] - FRAME = [")
                .append(Character.isDigit(data[0]) ? data[0] : '.')
                .append(Character.isDigit(data[1]) ? data[1] : '.')
                .append(Character.isDigit(data[2]) ? data[2] : '.')
                .append(Character.isDigit(data[3]) ? data[3] : '.')
                .append("]")
        ;

        return ret.toString();
    }
}
