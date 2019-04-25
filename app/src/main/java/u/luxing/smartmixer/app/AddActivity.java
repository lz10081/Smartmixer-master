package u.luxing.smartmixer.app;
/**
 * This class is designed to add user recipe data
 * to be save into the database.
 *
 * @author Luxing Zeng
 * @version 4/1/2019.
 */

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {
    private SimpleCursorAdapter dataAdapter;

    long primaryKey;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // add toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        primaryKey = getIntent().getLongExtra("key", 1L);
        SharedPreferences myPrefs;
        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
        final String tanklname = myPrefs.getString("tanklname","def");
        final String tankrname = myPrefs.getString("tankrname","def");
        TextView tank1hasname = (TextView)findViewById(R.id.tank1hasname);
        TextView tank2hasname = (TextView)findViewById(R.id.tank2hasname);
        tank1hasname.setText(tanklname);
        tank2hasname.setText(tankrname);
    }

    /**
     *
     * @param view
     */
    // when Save button pressed
    public void Submit(View view) {
        SharedPreferences myPrefs;
        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        final String tanklname = myPrefs.getString("tanklname","def");
        final String tankrname = myPrefs.getString("tankrname","def");
        final Integer tankLamount = myPrefs.getInt("tanklam",0);
        final Integer tankRamount = myPrefs.getInt("tankram",0);
        // get String from editText boxes
        EditText editTextT = (EditText) findViewById(R.id.editTextTitle);
        EditText tank1 = (EditText) findViewById(R.id.tank1);
        EditText tank2 = (EditText) findViewById(R.id.tank2);
        // TODO if everything works refactor these lines out

        if(tank1.getText().toString().equals("") || tank2.getText().toString().equals("") ){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("You have to enter all input ");
            dlgAlert.setTitle("Error");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }else if(tankLamount < Integer.parseInt(tank1.getText().toString()) || tankRamount < Integer.parseInt(tank2.getText().toString()) ){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Amount must be less than the what you have in the set up");
            dlgAlert.setTitle("Error");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }else if(editTextT.getText().toString().equals("")){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Make sure you have a name for this recipe");
            dlgAlert.setTitle("Error");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }else
        {
            editor.putInt("tanklnumber", Integer.parseInt(tank1.getText().toString()));
            editor.putInt("tankrnumber", Integer.parseInt(tank2.getText().toString()));
            editor.apply();
            // post the recipe into the database
            // same as dbAdapter.insertRecipe(title, recipe);
            // dbAdapter.insertRecipe(editTextT.getText().toString(), editTextR.getText().toString());
            String title = editTextT.getText().toString();
            String recipe = title+"|"+tank1.getText().toString() +"|"+ tank2.getText().toString() +"|"+ tanklname + "|"+tankrname;
            editor.putString("title",title);
            ContentValues newValues = new ContentValues();
            newValues.put(RecipeContract.TITLE, title);
            newValues.put(RecipeContract.RECIPE, recipe);
            getContentResolver().insert(RecipeContract.RECIPE_URI, newValues);
            finish(); // done, close activity
        }

    }

    /**
     *
     * @param menu
     * @return true
     */
    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.backmenu, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return MenuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
             // back button pressed
                finish();
             return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
