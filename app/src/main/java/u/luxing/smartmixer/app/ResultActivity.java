package u.luxing.smartmixer.app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import pl.droidsonroids.gif.GifTextView;
import u.luxing.smartmixer.app.gson.GsonWriter;
import u.luxing.smartmixer.app.recipes.EnumCupSize;
import u.luxing.smartmixer.app.recipes.Ingredient;
import u.luxing.smartmixer.app.recipes.Item;
import u.luxing.smartmixer.app.recipes.Recipe;

/**
 * This class is designed show recipe data after it saved in the database
 *
 * @author Luxing Zeng
 * @version 4/1/2019.
 */

// cc http://solderer.tv/data-transfer-between-android-and-arduino-via-bluetooth/
public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "";
    //    private DatabaseAdapter dbAdapter;
    private SimpleCursorAdapter dataAdapter;

    long primaryKey;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "B8:27:EB:A8:96:AB";
    String[] array;
    String[] namearray;
    ImageButton btnsent;

    GifTextView gifImageView;
    /*
     * This Activity displays the individual recipe
     */

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        gifImageView = (GifTextView)findViewById(R.id.result1);
        playGif();

        // add toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        primaryKey = getIntent().getLongExtra("key", 1L);
        queryContentProvider(primaryKey);
        btnsent = (ImageButton) findViewById(R.id.sent);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        SharedPreferences myPrefs;
        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
     /*
        //final String tanklname = myPrefs.getString("tanklname","def");
       // final String tankrname = myPrefs.getString("tankrname","def");
        final int tanklnumber = myPrefs.getInt("tanklnumber",0);
        final int tankrnumber = myPrefs.getInt("tankrnumber",0);

        final int tanklam = myPrefs.getInt("tanklam",0);
        final int tankram = myPrefs.getInt("tankram",0);

        final String title = myPrefs.getString("title","def");
        */
      //  final EditText title = (EditText)findViewById(R.id.textRecipe);
        btnsent.setOnClickListener(
                new View.OnClickListener() {
            public void onClick(View v) {
                String js = "";
                JSONObject Recipe = new JSONObject();
                String tanklam = (array[1].toString());
                String tankram = (array[2].toString());
                String tanklname = (array[3].toString());
                String tankrname = (array[4].toString());
                final Item oj = new Item(tanklname, Integer.parseInt(tanklam));
                final Item vodka = new Item(tankrname, Integer.parseInt(tankram));
                final u.luxing.smartmixer.app.recipes.Recipe recipe = new Recipe(array[0].toString(), EnumCupSize.SOLO, new Ingredient(oj, Integer.parseInt(tanklam)), new Ingredient(vodka, Integer.parseInt(tankram)));
                //  final Map<String, Object> json = new TreeMap<>();
                //recipe.encode(json);
                GsonWriter gs = new GsonWriter();
                try {
                    js =  gs.write(recipe);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendData(js);
                sendData("\u0004\u0004");
                finish();
                //Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void playGif(){
        Animation fadeout = new AlphaAnimation(1.f, 1.f);
        fadeout.setDuration(2500); // You can modify the duration here
        fadeout.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                gifImageView.setBackgroundResource(R.drawable.cc);// image from https://dribbble.com/osvaldadi
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });
        gifImageView.startAnimation(fadeout);
    }

    /**
     *
     * @param device
     * @return BluetoothSocket
     * @throws IOException
     */
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    /**
     *
     * @param title
     * @param message
     */
    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     *
     * @param message
     */
    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "data: " + message );
        try {
            outStream.write(msgBuffer);
            Log.d(TAG, "success: " + msgBuffer );
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    /**
     *
     *
     * @param key
     */
    public void queryContentProvider(long key){

        String[] projection = new String[] {
                RecipeContract._ID,
                RecipeContract.TITLE,
                RecipeContract.RECIPE
        };

        String colsToDisplay [] = new String[] {
                RecipeContract.TITLE,
                RecipeContract.RECIPE
        };

        int[] colResIds = new int[] {
                R.id.textTitle,

        };

        String whereClause = RecipeContract._ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(key)};

        Cursor cursor = getContentResolver().query(RecipeContract.RECIPE_URI, projection, whereClause, whereArgs, null);

        int titleColumn = cursor.getColumnIndex(RecipeContract.TITLE);
        int recipeColumn = cursor.getColumnIndex(RecipeContract.RECIPE);
        cursor.moveToFirst();
        if (cursor != null ){
            String title = cursor.getString(titleColumn);
            String recipe = cursor.getString(recipeColumn);
            TextView titlebox = (TextView) findViewById(R.id.textTitle);
            TextView n1 = (TextView) findViewById(R.id.n1);
            TextView r1 = (TextView) findViewById(R.id.r1);
            TextView n2 = (TextView) findViewById(R.id.n2);
            TextView r2 = (TextView) findViewById(R.id.r2);
            array = recipe.split("\\|");
            namearray = title.split("\\|");
            titlebox.setText(title);
            n1.setText(array[3]);
            r1.setText(array[1]+" mL");
            n2.setText(array[4]);
            r2.setText(array[2]+" mL");

        }
    }

    /**
     *
     * @param view
     */
    public void delete(View view) {
        String whereClause = RecipeContract._ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(primaryKey)};
        getContentResolver().delete(RecipeContract.RECIPE_URI, whereClause, whereArgs);
        finish();
    }

    // Toolbar

    /**
     *
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.backmenu, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return boolean
     */

    /**
     *
     * @param item
     * @return boolean
     */
 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                // back button pressed
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
