package u.luxing.smartmixer.app;

import android.net.Uri;


/**
 * This class is designed to recipe data local to the user android
 *
 * @author Luxing Zeng
 * @version 4/1/2019.
 */
public class RecipeContract {
    public static final String AUTHORITY = "u.luxing.smartmixer.app.RecipeContentProvider";

    public static final Uri RECIPE_URI =
            Uri.parse("content://"+AUTHORITY+"/recipes");
    public static final Uri Tank_URI =
            Uri.parse("content://"+AUTHORITY+"/Tank");

    public static final Uri ALL_URI =
            Uri.parse("content://"+AUTHORITY+"/");

    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String RECIPE = "recipe";
    public static final String Tankname1 = "tank1_name";
    public static final String Tankname2 = "tank2_name";
    public static final String Tankamount1 = "tank1_amount";
    public static final String Tankamount2 = "tank2_amount";
    public static final String STAT = "statistics";

}
