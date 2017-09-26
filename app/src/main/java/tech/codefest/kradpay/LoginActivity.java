package tech.codefest.kradpay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tech.codefest.kradpay.oauth2.client.OauthService;
import tech.codefest.kradpay.oauth2.constant.OauthConstant;
import tech.codefest.kradpay.oauth2.request.AccessTokenRequest;
import tech.codefest.kradpay.oauth2.response.AccessTokenResponse;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Calendar currentTime;
    private int hour;
    private int minute;
    private EditText txtUsernameSignIn;
    private EditText txtPasswordSignIn;
    private Button btnSignIn;
    private TextView btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsernameSignIn = (EditText) findViewById(R.id.txtUsernameSignIn);
        txtPasswordSignIn = (EditText) findViewById(R.id.txtPasswordSignIn);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccessToken();
            }
        });
        btnSignUp = (TextView) findViewById(R.id.btnsignup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });
        currentTime    = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY) ;
        minute = currentTime.get(Calendar.MINUTE) ;

    }
    public void getAccessToken() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final SharedPreferences prefs = this.getSharedPreferences(
                android.support.v7.appcompat.BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setClient_id(OauthConstant.CLIENT_ID);
        accessTokenRequest.setClient_secret(OauthConstant.CLIENT_SECRET);
        accessTokenRequest.setGrant_type("password");
        accessTokenRequest.setUsername(txtUsernameSignIn.getText().toString());
        accessTokenRequest.setPassword(txtPasswordSignIn.getText().toString());
        OauthService service = new OauthService();

        service.getAccessToken().getAccessToken(accessTokenRequest, new Callback<AccessTokenResponse>() {
            @Override
            public void success(AccessTokenResponse accessTokenResponse, Response response) {

                if (accessTokenResponse.getAccess_token() == null) {
                    Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_LONG).show();

                } else {

                    prefs.edit().putBoolean("oauth2.loggedin", true).apply();
                    prefs.edit().putString("oauth2.accesstoken", accessTokenResponse.getAccess_token()).apply();
                    prefs.edit().putString("oauth2.refreshtoken", accessTokenResponse.getRefresh_token()).apply();
                    prefs.edit().putInt("oauth2.expiresin", (accessTokenResponse.getExpires_in()/3600)+hour).apply();
                    prefs.edit().putString("oauth2.tokentype",accessTokenResponse.getToken_type()).apply();


                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), accessTokenResponse.getAccess_token(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_LONG).show();

            }
        });
    }





}
