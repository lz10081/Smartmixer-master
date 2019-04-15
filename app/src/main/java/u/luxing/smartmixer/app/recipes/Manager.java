package u.luxing.smartmixer.app.recipes;



import java.util.Map;
import java.util.TreeMap;

/**
 * Class for handling Recipe and Item management.
 *
 * @author hockeyhurd
 * @version 10/8/18
 */
public final class Manager {

    private static final Manager inst = new Manager();
   // private static final Logger logger = Module.getInstance().getLogger();

    private Manager() {
        init();
    }

    /**
     * Get the Manager's instance.
     *
     * @return Manager.
     */
    public static Manager getInstance() {
        return inst;
    }

    /**
     * Initializes the Manager.
     */
    private void init() {

    }

    public static final class ItemManager {

        // Recipe map with (name, Item) pairs.
        private final Map<String, Item> items;

        private static final ItemManager inst = new ItemManager();

        private ItemManager() {
            items = new TreeMap<>();
        }

        /**
         * Gets the instance of the ItemManager.
         *
         * @return ItemManager instance.
         */
        public static ItemManager getInstance() {
            return inst;
        }

        /**
         * Adds an Item to the ItemManager.
         *
         * @param item Item to add.
         */
        public void add(final Item item) {
            items.put(item.getName(), item);
        }

        /**
         * Gets the Item by its name.
         *
         * @param name String name of Item.
         * @return Item if found, else returns null.
         */
        public Item get(final String name) {
            return items.get(name);
        }

        /**
         * Removes an Item by its name.
         *
         * @param name String name of Item to find.
         * @return boolean result of removal.
         */
        public boolean remove(final String name) {
            final int countBefore = items.size();
            items.remove(name);

            return items.size() < countBefore;
        }

        @Override
        public String toString() {
            return "ItemManager{" + "items=" + items.toString() + '}';
        }
    }

    public static final class RecipeManager {

        // Recipe map with (name, Recipe) pairs.
        private final Map<String, Recipe> recipes;

        private static final RecipeManager inst = new RecipeManager();

        private RecipeManager() {
            recipes = new TreeMap<>();
        }

        /**
         * Gets the instance of the RecipeManager.
         *
         * @return RecipeManager instance.
         */
        public static RecipeManager getInstance() {
            return inst;
        }

        /**
         * Adds a Recipe to the RecipeManager.
         *
         * @param recipe Recipe to add.
         */
        public void add(final Recipe recipe) {
            recipes.put(recipe.getName(), recipe);
        }

        /**
         * Gets the Recipe by its name.
         *
         * @param name String name of Recipe.
         * @return Recipe if found, else returns null.
         */
        public Recipe get(final String name) {
            return recipes.get(name);
        }

        /**
         * Removes a Recipe by its name.
         *
         * @param name String name of Recipe.
         * @return boolean result of removal.
         */
        public boolean remove(final String name) {
            final int countBefore = recipes.size();

            recipes.remove(name);

            return recipes.size() < countBefore;
        }

        /**
         * Validates all recipes.
         *
         * @return boolean result.
         */
        public boolean validate() {
            for (final Recipe recipe : recipes.values()) {
                if (!recipe.validate()) {
                    //logger.error(String.format("Recipe %s is NOT valid...", recipe.getName()));
                    return false;
                }
            }

            //logger.info("RecipeManager is validated");

            return true;
        }

        @Override
        public String toString() {
            return "RecipeManager{" + "recipes=" + recipes.toString() + '}';
        }
    }
}
