package u.luxing.smartmixer.app.recipes;

import u.luxing.smartmixer.app.gson.IEncodeable;


import java.security.InvalidKeyException;
import java.util.Map;
import java.util.Objects;

/**
 * Class for handling items the machine has access to
 * i.e. liquid tanks.
 *
 * @author hockeyhurd
 * @version 10/8/18
 */
public class Item implements IEncodeable {

    private final String name;
    private int amount;

    /**
     * @param name String item name.
     * @param amount int amount of liquid (mL).
     */
    public Item(final String name, final int amount) {
        this.name = name;
        this.amount = amount;
    }

    /**
     * Copy constructor.
     *
     * @param item Item to copy.
     */
    public Item(final Item item) {
        this.name = item.name;
        this.amount = item.amount;
    }

    public Item(final Map<String, Object> json) throws InvalidKeyException {
        if (!json.containsKey("Name"))
            throw new InvalidKeyException();

        this.name = json.get("Name").toString();
        this.amount = 0;
    }

    /**
     * Creates a copy of the Item instance.
     *
     * @return Item.
     */
    public Item copy() {
        return new Item(this);
    }

    /**
     * Name of the Item.
     *
     * @return String.
     */
    public String getName() {
        return name;
    }

    /**
     * Amount of liquid.
     *
     * @return int.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of liquid.
     *
     * @param amount int amount.
     */
    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public boolean canFill(final int amount) {
        // return fill(amount, true) > this.amount;
        return true;
    }

    public int fill(final int amount, final boolean simulate) {
        if (simulate)
            return this.amount + amount;

        this.amount += amount;

        return this.amount;
    }

    public boolean canDrain(final int amount) {
        return drain(amount, true) >= 0;
    }

    public int drain(final int amount, final boolean simulate) {
        if (simulate)
            return this.amount - amount;

        this.amount -= amount;

        return this.amount;
    }

    @Override
    public void encode(final Map<String, Object> json) {
        json.put("Name", name);
    }

    public boolean matches(final Item other) {
        return name.equals(other.name);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Item))
            return false;

        final Item item = (Item) obj;

        return amount == item.amount && name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount);
    }

    @Override
    public String toString() {
        return "Item{" + "name='" + name + '\'' + ", amount=" + amount + '}';
    }
}
