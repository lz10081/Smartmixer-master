package u.luxing.smartmixer.app;

/**
 * This class is designed help save the recipe data
 *
 * @author Luxing Zeng
 * @version 4/1/2019.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String KEY_ROWID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_RECIPE = "recipe";
    private static final String KEY_Tankname1 = "tank1_name";
    private static final String KEY_Tankname2 = "tank2_name";
    private static final String KEY_Tankamount1 = "tank1_amount";
    private static final String KEY_Tankamount2 = "tank2_amount";
    private static final String SQLITE_TABLE = "recipes";

    private static final String SQLITE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_TITLE + " TEXT, "
                    + KEY_Tankname1 + " TEXT, "
                    + KEY_Tankname2 + " TEXT, "
                    + KEY_Tankamount1 + " TEXT, "
                    + KEY_Tankamount2 + " TEXT, "
                    + KEY_RECIPE + " TEX ); ";

    /**
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create recipes table and appropriate columns
        db.execSQL(SQLITE_CREATE);

    }

    /**
     *
     * @param db
     * @param oldver
     * @param newver
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldver, int newver) {
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }
}