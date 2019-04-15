package u.luxing.smartmixer.app.recipes;
/**
 * Enumeration defining the supported cup sizes.
 *
 * @author hockeyhurd
 * @version 10/8/18
 */
public enum EnumCupSize {

    SHOT(40, 44), SOLO(500, 532), MUG(700, 750);

    private final int amount;
    private final int max;

    EnumCupSize(final int amount, final int max) {
        this.amount = amount;
        this.max = max;
    }

    public static EnumCupSize getCupSize(final int amount) {
        switch (amount) {
            case 40:
                return SHOT;
            case 500:
                return SOLO;
            case 700:
                return MUG;
            default:
                return null;
        }
    }

    /**
     * Amount to pour in milliliters (mL).
     *
     * @return int.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Max size in milliliters (mL).
     *
     * @return int.
     */
    public int getMax() {
        return max;
    }
}
