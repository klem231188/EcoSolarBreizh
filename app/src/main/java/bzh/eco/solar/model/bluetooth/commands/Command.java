package bzh.eco.solar.model.bluetooth.commands;

/**
 * @author : Clément.Tréguer
 */
public enum Command {
    TURN_LEFT(new char[]{'+', '+', '1', '0', '0', '0', '-', '-'}),
    TURN_RIGHT(new char[]{'+', '+', '0', '1', '0', '0', '-', '-'}),
    WARNING_LIGHTS(new char[]{'+', '+', '0', '0', '1', '0', '-', '-'}),
    DEEPED_HEADLIGHTS(new char[]{'+', '+', '0', '0', '0', '1', '-', '-'});

    // TODO
    // FULL_HEADLIGHTS(new char[]{'+', '+', '?', '?', '?', '?', '-', '-'});

    private final char[] value;

    private Command(final char[] command) {
        this.value = command;
    }

    public char[] getValue() {
        return value;
    }
}
