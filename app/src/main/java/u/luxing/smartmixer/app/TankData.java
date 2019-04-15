package u.luxing.smartmixer.app;



import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import u.luxing.smartmixer.app.recipes.Item;
import u.luxing.smartmixer.app.gson.IEncodeable;
/**
 * This class is designed to encode tank specific data
 * to be sent over a Network (Bluetooth).
 *
 * @author hockeyhurd
 * @version 3/21/2019.
 */
public class TankData implements IEncodeable {

  //  private static final Logger logger = Module.getInstance().getLogger();

    private final List<TankBase> tanks;

    public TankData(final List<TankBase> tanks) {
        this.tanks = tanks;
    }

    @SuppressWarnings("unchecked")
    public TankData(final Map<String, Object> json) {
        if (json.size() != 2)
            throw new RuntimeException("Invalid JSON mapping for tank");

        // final Map<String, Object> thisMap = (Map<String, Object>) json.get("TankData");
        final List<Map<String, Object>> tankList = (List<Map<String, Object>>) json.get("List");
        final int count = (int) ((Double) json.get("Count")).doubleValue();
        tanks = new ArrayList<>(count);

        for (final Map<String, Object> mapping : tankList) {
            final TankBase tankBase = new FakeTank(mapping);
            tanks.add(tankBase);
        }
    }

    public List<TankBase> getTanks() {
        return tanks;
    }

    @Override
    public void encode(final Map<String, Object> json) {
        final Map<String, Object> thisMap = new TreeMap<>();
        // thisMap.put("Tanks", tanks);
        thisMap.put("Count", tanks.size());

        final List<Map<String, Object>> encodings = new ArrayList<>(tanks.size());

        for (final TankBase tank : tanks) {
            final Map<String, Object> tankMap = new TreeMap<>();
            tank.encode(tankMap);

            encodings.add(tankMap);
        }

        thisMap.put("List", encodings);

        json.put("TankData", thisMap);
    }

    public static final class FakeTank extends TankBase {

        private int quantity;

        public FakeTank(final int tankID, final Item item, final int quantity) {
            super(tankID, item);

            super.item.setAmount(quantity);
            this.quantity = quantity;
        }

        public FakeTank(final int tankID, final Item item) {
            super(tankID, item);

            this.quantity = item.getAmount();
        }

        public FakeTank(final Map<String, Object> json) {
            super(-1, null);

            @SuppressWarnings("unchecked")
            final Map<String, Object> thisMap = (Map<String, Object>) json.get("Tank");

            tankID = (int) ((Double) thisMap.get("TankID")).doubleValue();
            quantity = (int) ((Double) thisMap.get("Quantity")).doubleValue();

            @SuppressWarnings("unchecked")
            final Map<String, Object> itemMap = (Map<String, Object>) thisMap.get("Item");

            try {
                item = new Item(itemMap);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            item.setAmount(quantity);
        }

        public int getQuantity() {
            return quantity;
        }

        @Override
        public void encode(final Map<String, Object> json) {
            final Map<String, Object> thisMap = new TreeMap<>();

            thisMap.put("TankID", tankID);
            thisMap.put("Quantity", quantity);

            final Map<String, Object> itemMap = new TreeMap<>();
            item.encode(itemMap);
            thisMap.put("Item", itemMap);

            json.put("Tank", thisMap);
        }
    }

}
