package u.luxing.smartmixer.app;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import u.luxing.smartmixer.app.gson.GsonWriter;
import u.luxing.smartmixer.app.recipes.Item;
/**
 * This class is designed sent tank setup data to the maker
 *
 * @author Luxing Zeng
 * @version 4/1/2019.
 */

public class Setup extends AppCompatActivity {

    private static final String TAG = "bluetooth1";

    Button btnOn;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address = "B8:27:EB:A8:96:AB";

    /** Called when the activity is first created. */
    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup);

        btnOn = (Button) findViewById(R.id.setupsave);

        final EditText tankln = (EditText)findViewById(R.id.tankLname);//tank left name
        final EditText tankrn = (EditText)findViewById(R.id.tankRname);
        final EditText tankla = (EditText)findViewById(R.id.tankLamount);//tank let amount
        final EditText tankra = (EditText)findViewById(R.id.tankRamount);






        btAdapter = BluetoothAdapter.getDefaultAdapter();

        checkBTState();
        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if(tankln.getText().toString().equals("") || tankln.getText().toString().equals("") ){
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Setup.this);
                    dlgAlert.setMessage("You have to enter all set up");
                    dlgAlert.setTitle("Error");
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog
                                }
                            });
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }else if (tankla.getText().toString().equals("") || tankra.getText().toString().equals("")){
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Setup.this);
                    dlgAlert.setMessage("You have to enter all set up");
                    dlgAlert.setTitle("Error");
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog
                                }
                            });
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
                else{
                    SharedPreferences myPrefs;
                    myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.putString("tanklname", tankln.getText().toString());
                    editor.putString("tankrname", tankrn.getText().toString());
                    editor.putInt("tanklam", Integer.parseInt(tankla.getText().toString()));
                    editor.putInt("tankram", Integer.parseInt(tankra.getText().toString()));
                    editor.apply();
                    String valueL= tankla.getText().toString();
                    int finalValueL=Integer.parseInt(valueL);
                    String valueR= tankra.getText().toString();
                    int finalValueR=Integer.parseInt(valueR);

                    Item itemL = new Item( tankln.getText().toString(), finalValueL);
                    Item itemR = new Item( tankrn.getText().toString(), finalValueR);
                    final u.luxing.smartmixer.app.TankData.FakeTank ltank = new TankData.FakeTank(0,itemL);
                    final u.luxing.smartmixer.app.TankData.FakeTank rtank = new TankData.FakeTank(1,itemR);
                    List tanklist = new ArrayList();
                    tanklist.add(ltank);
                    tanklist.add(rtank);
                    final TankData tankdata = new TankData(tanklist);

                    String js = "";
                    GsonWriter gs = new GsonWriter();
                    try {
                        js =  gs.write(tankdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sendData(js);
                    sendData("\u0004\u0004");
                    finish();
                    startActivity(new Intent(Setup.this, MainActivity.class));
                }
            }
        });


    }

    /**
     *
     * @param device
     * @return
     * @throws IOException
     */
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
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

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }
}
