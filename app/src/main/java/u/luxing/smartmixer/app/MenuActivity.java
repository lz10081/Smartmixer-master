package u.luxing.smartmixer.app;
/**
 * This class is designed help mainActivity
 * to be show as menu.
 *
 * @author Luxing Zeng
 * @version 4/1/2019.
 */
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifTextView;

public class MenuActivity extends AppCompatActivity {
    private Button buttonBluetooth;
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE = 1;
    GifTextView gifImageView;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        gifImageView = (GifTextView)findViewById(R.id.loading1);
        playGif();
        requestPermissions();
        new Timer().schedule(new TimerTask() {

            @Override

            public void run() {

                startActivity(new Intent(MenuActivity.this, Setup.class));

                finish();


            }

        }, 5000);
    }
    public void playGif(){
        Animation fadeout = new AlphaAnimation(1.f, 1.f);
        fadeout.setDuration(2500); // You can modify the duration here
        fadeout.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                gifImageView.setBackgroundResource(R.drawable.lo1);// image from https://dribbble.com/osvaldadi
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
     */
    public void requestPermissions() {

        if (ContextCompat.checkSelfPermission(MenuActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MenuActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(MenuActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE);


            } else {

                ActivityCompat.requestPermissions(MenuActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE);

            }
        }
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted, app can do its things

                } else {
                    // permission denied
                    // show a dialog box alerting user that permissions are required
                    // then request permissions again
                    final AlertDialog alertDialog = new AlertDialog.Builder(MenuActivity.this).create();
                    alertDialog.setTitle("Permissions required");
                    alertDialog.setMessage("Permissions required for the use of this app. Please allow permissions");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                            requestPermissions();
                        }
                    });
                    alertDialog.show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}