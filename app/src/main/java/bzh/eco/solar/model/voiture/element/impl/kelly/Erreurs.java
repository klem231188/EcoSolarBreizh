package bzh.eco.solar.model.voiture.element.impl.kelly;

/**
 * Created by Eco Solar Breizh on 05/03/2016.
 */
public class Erreurs {
    // Sans Erreur
    public static final Erreur SANS_ERREUR = new Erreur("", "Pas d'erreur", "");

    //--> LSB
    public static final Erreur ERREUR_LSB_0x11 = new Erreur("0x11", "", "");
    public static final Erreur ERREUR_LSB_0x12 = new Erreur("0x12", "Over voltage error", "1. Battery voltage is too high for the controller. Check battery volts and configuration.\n" +
            "2. Regeneration over-voltage. Controller will have cut back or stopped regen.\n" +
            "3. This only accurate to ± 2% upon Overvoltage setting.");
    public static final Erreur ERREUR_LSB_0x13 = new Erreur("0x13", "Low voltage error", "1. The controller will clear after 5 seconds if battery volts returns to normal.\n" +
            "2. Check battery volts & recharge if required.");
    public static final Erreur ERREUR_LSB_0x14 = new Erreur("0x14", "Over temperature warning", "1. Controller case temperature is above 90℃. Current will be limited. Reduce controller loading or switch Off until controller cools down.\n" +
            "2. Clean or improve heatsink or fan.");
    public static final Erreur ERREUR_LSB_0x21 = new Erreur("0x21", "Motor did not start", "Motor did not reach 25 electrical RPM within 2 seconds of start-up. Hall sensor or phase wiring problem.");
    public static final Erreur ERREUR_LSB_0x22 = new Erreur("0x22", "Internal volts fault", "1. Measure that B+ & PWR are correct when measured to B- or RTN.\n" +
            "2. There may be excessive load on the +5V supply caused by too low a value of Regen or throttle potentiometers or incorrect wiring.\n" +
            "3. Controller is damaged. Contact Kelly about a warranty repair.");
    public static final Erreur ERREUR_LSB_0x23 = new Erreur("0x23", "Over temperature", "The controller temperature has exceeded 100℃. The controller will be stopped but will restart when temperature falls below 80℃.");
    public static final Erreur ERREUR_LSB_0x24 = new Erreur("0x24", "Throttle error at power-up", "Throttle signal is higher than the preset „dead zone‟ at Power On. Fault clears when throttle is released.");

    //--> MSB
    public static final Erreur ERREUR_MSB_0x31 = new Erreur("0x31", "Frequent reset", "May be caused by over-voltage, bad motor intermittent earthing problem, bad wiring, etc.");
    public static final Erreur ERREUR_MSB_0x32 = new Erreur("0x32", "Internal reset", "May be caused by some transient fault condition like a temporary over-current, momentarily high or low battery voltage. This can happen during normal operation.");
    public static final Erreur ERREUR_MSB_0x33 = new Erreur("0x33", "Hall throttle is open or short-circuit", "When the throttle is repaired, a restart will clear the fault.");
    public static final Erreur ERREUR_MSB_0x34 = new Erreur("0x34", "Non-zero throttle on direction change", "Controller won‟t allow a direction change unless the throttle or speed is at zero. Fault clears when throttle is released.");
    public static final Erreur ERREUR_MSB_0x41 = new Erreur("0x41", "Regen or Start-up over-voltage", "Motor drive is disabled if an over-voltage is detected at start-up or during regen. The voltage threshold detection level is set during configuration.");
    public static final Erreur ERREUR_MSB_0x42 = new Erreur("0x42", "Hall sensor error", "1. Incorrect or loose wiring or a damaged hall sensor.\n" +
            "2. Also be caused by incorrect hall angle configuration (60 degree or 120 degree).");
    public static final Erreur ERREUR_MSB_0x43 = new Erreur("0x43", "Motor over-temperature", "Motor temperature has exceeded the configured maximum. The controller will shut down until the motor temperature cools down.");
    public static final Erreur ERREUR_MSB_0x44 = new Erreur("0x44", "Motor locked rotor", "When in locked rotor condition, the max output phase current of the motor will be limited to 90% of previous current. Once this problem disappears, the fault will clear and the max output phase current will return to normal.");

}
