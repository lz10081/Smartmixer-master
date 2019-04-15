package u.luxing.smartmixer.app;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
/**
 * This class is designed help with our database
 * make sure the data format
 * @author Luxing Zeng
 * @version 4/1/2019.
 */

public class RecipeContentProvider extends ContentProvider{

    private DBHelper dbHelper = null;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "recipes", 1);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "recipes/#", 2);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "titles/", 3);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "*", 4);
        uriMatcher.addURI(RecipeContract.AUTHORITY, "Tank", 5);
    }

    /**
     *
     * @return boolean
     */
    @Override
    public boolean onCreate() {
        this.dbHelper = new DBHelper(this.getContext(), "recipeDB", null, 7);
        return true;
    }

    /**
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return Cursor
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch(uriMatcher.match(uri)) {
            case 2:
                selection = "_ID = " + uri.getLastPathSegment();
            case 1:
                return db.query("recipes", projection, selection, selectionArgs, null, null, sortOrder);
            case 3:
                String q3 = "SELECT title FROM recipes";
                return db.rawQuery(q3, selectionArgs);
            case 4:
                String q4 = "SELECT title FROM recipes WHERE _ID = " + uri.getLastPathSegment();
                return db.rawQuery(q4, selectionArgs);
            case 5:
                String q5 = "SELECT * FROM recipes";
                return db.rawQuery(q5, selectionArgs);
            default:
                return null;
        }
    }

    @Override
    public String getType( Uri uri) {

        String contentType;

        if (uri.getLastPathSegment()==null) {
            // cursor pointing to multiple items
            contentType = "vnd.android.cursor.dir/MyProvider.data.text";
        }
        else {
            // cursor pointing to single item
            contentType = "vnd.android.cursor.item/MyProvider.data.text";
        }

        return contentType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;
        switch (uriMatcher.match(uri)) {
            default:
                tableName = "recipes";
                break;
        }
        long id = db.insert(tableName, null, values);
        db.close();
        Uri newuri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(newuri, null);
        return newuri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;
        switch (uriMatcher.match(uri)) {
            default:
                tableName = "recipes";
                break;
        }
        db.delete(tableName, selection, selectionArgs);
        db.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[]
            selectionArgs) {
        throw new UnsupportedOperationException("not implemented");
    }


}
