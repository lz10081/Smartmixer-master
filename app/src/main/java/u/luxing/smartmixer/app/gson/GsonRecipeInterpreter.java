package u.luxing.smartmixer.app.gson;


import u.luxing.smartmixer.app.recipes.Recipe;

import java.security.InvalidKeyException;
import java.util.Map;

/**
 * GSON interpreter for Recipes.
 *
 * @author hockeyhurd
 * @version 10/12/2018.
 */
public final class GsonRecipeInterpreter implements u.luxing.smartmixer.app.gson.IGsonInterpreter<Recipe, GsonRecipeInterpreter.RecipeResponse> {

    @Override
    public Class<GsonRecipeInterpreter.RecipeResponse> getResponseClass() {
        return RecipeResponse.class;
    }

    @Override
    public Recipe interpret(final Map<String, Object> json) {
        try {
            return new Recipe(json);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final class RecipeResponse implements IGsonResponder<Map<String, Object>> {
        Map<String, Object> Recipe;

        @Override
        public Map<String, Object> getResponse() {
            return Recipe;
        }
    }

}
