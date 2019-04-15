package u.luxing.smartmixer.app.recipes;

import u.luxing.smartmixer.app.gson.IEncodeable;
import u.luxing.smartmixer.app.gson.IEncodeable;


import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class for defining an Ingredient for a Recipe.
 *
 * @author hockeyhurd
 * @version 10/8/18
 */
public class Ingredient implements IEncodeable {

    private final Item item;
    private final int quantity;

    /**
     * @param item Item.
     * @param quantity int.
     */
    public Ingredient(final Item item, final int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    @SuppressWarnings("unchecked")
    Ingredient(final Map<String, Object> json) throws InvalidKeyException {
        if (!json.containsKey("Ingredient"))
            throw new InvalidKeyException();

        final Map<String, Object> thisMap = (Map<String, Object>) json.get("Ingredient");

        if (!thisMap.containsKey("Item") || !thisMap.containsKey("Quantity"))
            throw new InvalidKeyException();

        final Map<String, Object> subMap = (Map<String, Object>) thisMap.get("Item");
        this.item = new Item(subMap);
        this.quantity = (int) (double) thisMap.get("Quantity");
    }

    /**
     * Gets the ingredient's Item.
     *
     * @return Item.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets the int amount of liquid.
     *
     * @return int.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Validates the ingredient by checking
     * if there is enough liquid.
     *
     * @return boolean result.
     */
    public boolean validate() {
        return item.canDrain(quantity);
    }

    @Override
    public void encode(final Map<String, Object> json) {
        final Map<String, Object> thisMap = new HashMap<>();
        final Map<String, Object> subMap = new HashMap<>();

        item.encode(subMap);

        thisMap.put("Item", subMap);
        thisMap.put("Quantity", quantity);

        json.put("Ingredient", thisMap);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Ingredient))
            return false;

        final Ingredient that = (Ingredient) obj;

        return quantity == that.quantity && item.matches(that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, quantity);
    }

    @Override
    public String toString() {
        return "Ingredient{" + "item=" + item + ", quantity=" + quantity + '}';
    }
}
