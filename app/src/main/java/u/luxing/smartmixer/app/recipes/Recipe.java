package u.luxing.smartmixer.app.recipes;

import u.luxing.smartmixer.app.gson.IEncodeable;


import java.security.InvalidKeyException;
import java.util.*;

/**
 * Class for handling recipe creation.
 *
 * @author hockeyhurd
 * @version 10/8/18
 */
public class Recipe implements IEncodeable {

    private String name;
    private EnumCupSize cupSize;
    private Set<Ingredient> ingredients;

  //  private static final Logger logger = Module.getInstance().getLogger();

    /**
     * Creates a recipe.
     *
     * @param name String recipe name.
     * @param cupSize EnumCupSize cup size.
     * @param ingredients Ingredient list.
     */
    public Recipe(final String name, final EnumCupSize cupSize, final Ingredient... ingredients) {
        this.name = name;
        this.cupSize = cupSize;
        this.ingredients = new HashSet<>();

        for (final Ingredient ingredient : ingredients) {
            this.ingredients.add(ingredient);
        }
    }

    @SuppressWarnings("unchecked")
    public Recipe(final Map<String, Object> json) throws InvalidKeyException {
        if (!json.containsKey("Name") || !json.containsKey("Cup Size") || !json.containsKey("Ingredients"))
            throw new InvalidKeyException();

        this.name = json.get("Name").toString();
        this.cupSize = EnumCupSize.getCupSize((int) (double) json.get("Cup Size"));

        this.ingredients = new HashSet<>();
        final List<Map<String, Object>> ingredientsList = (List<Map<String, Object>>) json.get("Ingredients");

        for (final Map<String, Object> itemMap : ingredientsList) {
            final Ingredient ingredient = new Ingredient(itemMap);
            this.ingredients.add(ingredient);
        }
    }

    /**
     * Gets the Recipe's name.
     *
     * @return String name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the ingredients needed for the recipe.
     *
     * @return Set of Ingredient(s).
     */
    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Attempts to validate a given recipe.
     *
     * @return boolean result.
     */
    public boolean validate() {
        if (ingredients.isEmpty())
            return false;

        int sum = 0;

        for (final Ingredient ingredient : ingredients) {
            if (!ingredient.validate())
                return false;

            sum += ingredient.getQuantity();
        }

        if (sum > cupSize.getMax())
            return false;

        return true;
    }

    @Override
    public void encode(final Map<String, Object> json) {
        // This map is for this internal class.
        final Map<String, Object> thisMap = new HashMap<>();

        // encode name.
        thisMap.put("Name", name);

        // encode cup size.
        thisMap.put("Cup Size", cupSize.getAmount());

        // List of ingredients and their associative mappings.
        final List<Map<String, Object>> ingredientsList = new ArrayList<>(ingredients.size());

        for (final Ingredient ingredient : ingredients) {
            // Mapping for individual ingredient.
            final Map<String, Object> ingredientMap = new HashMap<>();
            ingredient.encode(ingredientMap);

            // Add mapping to the list after encoding.
            ingredientsList.add(ingredientMap);
        }

        // Add ingredients list to our class mapping.
        thisMap.put("Ingredients", ingredientsList);

        // Associate this recipe within the recipe.
        json.put("Recipe", thisMap);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Recipe))
            return false;

        final Recipe recipe = (Recipe) obj;

        // return Objects.equals(name, recipe.name) && Objects.equals(ingredients, recipe.ingredients);
        if (!name.equals(recipe.name))
            return false;

        for (final Ingredient ingredient : ingredients) {
            boolean found = false;

            for (final Ingredient otherIngredient : recipe.ingredients) {
                if (ingredient.equals(otherIngredient)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
               // logger.error("Could not find ingredient: " + ingredient.getItem().getName());
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Recipe \"");
        builder.append(name);
        builder.append("\": {");

        for (final Ingredient ingredient : ingredients) {
            builder.append(ingredient.toString());
        }

        builder.append(" }");

        return builder.toString();
    }
}
