package tech.codefest.kradpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

/**
 * Created by dominicneeraj on 08/09/17.
 */
public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();
    private final CountDownLatch timeoutLatch = new CountDownLatch(1);
    public LinearLayout linearLayout;
    public static int splashImage;
    private Thread thread;
    private Calendar currentTime;
    public boolean isFirstStart;
    Context mcontext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




        SharedPreferences prefs = this.getSharedPreferences(
                android.support.v7.appcompat.BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        currentTime    = Calendar.getInstance();
        int current_hour = currentTime.get(Calendar.HOUR_OF_DAY) ;
        int current_minute = currentTime.get(Calendar.MINUTE) ;
        int created_hour = (prefs.getInt("oauth2.expiresin",0));
        int created_minute = (prefs.getInt("oauth2.expiresin",0));
        String token = (prefs.getString("oauth2.accesstoken", ""));
        Log.e(LOG_TAG, String.valueOf(created_minute));
        Log.e(LOG_TAG, String.valueOf(current_minute));
        Log.e(LOG_TAG, token);



        if(isNetworkAvailable()){

            if(!token.isEmpty()& current_hour<=created_hour) {
                Log.e(LOG_TAG, "MainActivity");
                goMain();
            }
            else {
                Log.e(LOG_TAG, "LoginActivity");
                goSignIn();

            }


        }


        else {
            //Notify user they aren't connected
            Toast.makeText(getApplicationContext(), "You aren't connected to the internet.", Toast.LENGTH_SHORT).show();
            //close the app
            finish();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * Starts an activity after the splash timeout.
     * @param intent the intent to start the activity.
     */

    private void goAfterSplashTimeout(final Intent intent) {
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                // wait for the splash timeout expiry or for the user to tap.


                SplashActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        startActivity(intent);
                        // finish should always be called on the main thread.
                        finish();
                    }
                });
            }
        });
        thread.start();
    }
    /**
     * Go to the main activity after the splash timeout has expired.
     */
    protected void goMain() {
        Log.d(LOG_TAG, "Launching Main Activity...");
        Intent i = new Intent(SplashActivity.this, MainActivity.class);

        startActivity(i);

        // close this activity
        finish();
    }
    protected void goSignIn() {
        Log.d(LOG_TAG, "Launching Sign-in Activity...");
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);

        startActivity(i);

        // close this activity
        finish();
    }
    /**
     * Go to the sign in activity after the splash timeout has expired.
     */

}
