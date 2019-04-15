package u.luxing.smartmixer.app;



import java.util.Map;

import u.luxing.smartmixer.app.recipes.Item;
import u.luxing.smartmixer.app.gson.IEncodeable;
/**
 * Base class for all tanks.
 *
 * @author hockeyhurd
 * @version 3/21/2019.
 */
public abstract class TankBase implements IEncodeable {

    protected int tankID;
    protected Item item;

    protected TankBase(final int tankID, final Item item) {
        this.tankID = tankID;
        this.item = item;
    }

    /**
     * Gets the Tank ID.
     *
     * @return int.
     */
    public int getTankID() {
        return tankID;
    }

    /**
     * Gets the Tank's Item.
     *
     * @return Item.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets the Tank's Item.
     *
     * @param item Item
     */
    public void setItem(final Item item) {
        this.item = item;
    }

    /**
     * Checks if the tank is empty.
     *
     * @return boolean true if empty, else false.
     */
    public boolean isEmpty() {
        return item.getAmount() == 0;
    }

    @Override
    public abstract void encode(final Map<String, Object> json);

}
