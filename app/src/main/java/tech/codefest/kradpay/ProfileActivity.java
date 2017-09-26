package tech.codefest.kradpay;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
  private TextView niceCounter;
    private static final String TAG = ProfileActivity.class.getSimpleName();
  private int niceCounterValue = 37;
private ImageView github;
    static final String PROFILE_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/profile/";
    static final String CARD_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/card/";
    private ImageView playstore;
    private ImageView linkedin;
    private TextView contact;
    private TextView email;
    private TextView name;
    private TextView dob;
    private TextView gender;
    private TextView card;
    private TextView name1;
    private Button update_btn;
    private String contact_text;
    private String email_text;
    private String name_text;
    private String dob_text;
    private String gender_text;
    private String card_text;
    private String token;
    private Response response;
    private JSONObject userobject;
    boolean wrapInScrollView = true;
    private MaterialDialog dialog;
    MaterialBetterSpinner materialBetterSpinner ;


    private EditText firstname_edit;
    private EditText lastname_edit;
    private EditText dob_edit;
    private EditText contact_edit;
    private Button submit_btn;
    private Map<String, String> mp;
    private String jsrating;
    String[] SPINNER_DATA = {"Male","Female"};
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.profile_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      SharedPreferences prefs = this.getSharedPreferences(
              android.support.v7.appcompat.BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

      token = prefs.getString("oauth2.accesstoken", "");
      name = (TextView) findViewById(R.id.name_text);
        name1 = (TextView) findViewById(R.id.name1);
      email = (TextView) findViewById(R.id.email_text);
      dob = (TextView) findViewById(R.id.dob_text);
      contact = (TextView) findViewById(R.id.contact_text);
      gender = (TextView) findViewById(R.id.gender_text);
        card = (TextView) findViewById(R.id.card_text);
        update_btn = (Button) findViewById(R.id.update);
      github = (ImageView)findViewById(R.id.github);
        mp = new HashMap<String, String>();
        update_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               profileUpdate();
            }
        });
      github.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v){
              Intent intent = new Intent();
              intent.setAction(Intent.ACTION_VIEW);
              intent.addCategory(Intent.CATEGORY_BROWSABLE);
              intent.setData(Uri.parse("https://github.com/dominicneeraj"));
              startActivity(intent);
          }
      });

    //in the toolbar
      new GetProfileTask().execute();
        new GetCardTask().execute();
  }

    private void profileUpdate() {




        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)



                .customView(R.layout.profile_view, wrapInScrollView);



        dialog = builder.build();


        dialog.show();
        View view = dialog.getCustomView();

        materialBetterSpinner = (MaterialBetterSpinner)view.findViewById(R.id.gender_data);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);
        materialBetterSpinner.setText(gender_text);


        firstname_edit = (EditText)view.findViewById(R.id.first_name);
        lastname_edit = (EditText)view.findViewById(R.id.last_name);
        contact_edit = (EditText)view.findViewById(R.id.contact_data);
        dob_edit = (EditText)view.findViewById(R.id.dob_data);
        contact_edit.setText(card_text);
        firstname_edit.setText(name_text.split("\\s+")[0]);
        lastname_edit.setText(name_text.split("\\s+")[1]);
        dob_edit.setText(dob_text);

        dob_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dob_edit.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        submit_btn = (Button)view.findViewById(R.id.submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                mp.put("firstname",firstname_edit.getText().toString());
                mp.put("lastname",lastname_edit.getText().toString());
                mp.put("gender",materialBetterSpinner.getText().toString());
                mp.put("dob",dob_edit.getText().toString());
                mp.put("contact",contact_edit.getText().toString());

                Gson gson = new Gson();
                jsrating = gson.toJson(mp);
                Log.e(TAG, jsrating);
                new UpdateProfileTask().execute(jsrating);

            }
        });
    }






    class GetProfileTask extends AsyncTask<Void, Void, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */

        }

        @Nullable
        @Override
        protected String doInBackground(Void... params) {



            try {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");



                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .url(PROFILE_URL)

                        .build();
                response = client.newCall(request).execute();

                return response.body().string();
            } catch (@NonNull IOException e) {
                Log.e(TAG, "" + e.getLocalizedMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);



            Log.e(TAG, "Response");
            JSONObject jsonobject = null;
            try {
                jsonobject = new JSONObject(Response);
                Log.e(TAG, String.valueOf(jsonobject));
            } catch (JSONException e) {

            }



            try {

                if(jsonobject!=null){
                    name_text = jsonobject.getString("firstname")+" "+jsonobject.getString("lastname");
                    Log.e("Name", name_text);
                    contact_text= jsonobject.getString("contact");
                    dob_text = jsonobject.getString("dob");
                    gender_text = jsonobject.getString("gender");
                    userobject = jsonobject.getJSONObject("user");
                    email_text=userobject.getString("email");

                    name.setText(name_text);
                    name1.setText(name_text);
                    contact.setText("+91 "+contact_text);
                    dob.setText(dob_text);
                    gender.setText(gender_text);
                    email.setText(email_text);

                }
                else{
                    name_text = "";
                    contact_text= "";
                    dob_text = "";
                    gender_text = "";

                    email_text="";

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }
    class UpdateProfileTask extends AsyncTask<String, Void, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */

        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String da = params[0];
            Log.e("token", da);

            try {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");


                RequestBody body = RequestBody.create(JSON, da);
                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .url(PROFILE_URL)
                        .post(body)
                        .build();
                response = client.newCall(request).execute();

                return response.body().string();
            } catch (@NonNull IOException e) {
                Log.e(TAG, "" + e.getLocalizedMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);



            Log.e(TAG, "Response");
            JSONObject jsonobject = null;
            try {
                jsonobject = new JSONObject(Response);
                Log.e(TAG, String.valueOf(jsonobject));
            } catch (JSONException e) {

            }



            try {

                if(jsonobject!=null){
                    name_text = jsonobject.getString("firstname")+" "+jsonobject.getString("lastname");
                    Log.e("Name", name_text);
                    contact_text= jsonobject.getString("contact");
                    dob_text = jsonobject.getString("dob");
                    gender_text = jsonobject.getString("gender");
                     userobject = jsonobject.getJSONObject("user");
                    email_text=userobject.getString("email");

                    name.setText(name_text);
                    name1.setText(name_text);
                    contact.setText("+91 "+contact_text);
                    dob.setText(dob_text);
                    gender.setText(gender_text);
                    email.setText(email_text);

                }
                else{
                    name_text = "";
                    contact_text= "";
                    dob_text = "";
                    gender_text = "";

                    email_text="";

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }

    class GetCardTask extends AsyncTask<Void, Void, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */

        }

        @Nullable
        @Override
        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .url(CARD_URL)
                        .build();
                response = client.newCall(request).execute();

                return response.body().string();
            } catch (@NonNull IOException e) {
                Log.e(TAG, "" + e.getLocalizedMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);



            Log.e(TAG, "Response");
            JSONObject jsonobject = null;
            try {
                jsonobject = new JSONObject(Response);
                Log.e(TAG, String.valueOf(jsonobject));
            } catch (JSONException e) {

            }



            try {

                if(jsonobject!=null){
                    card_text = jsonobject.getString("cardnumber");

                    card.setText("xxxx-"+card_text.substring(card_text.length()-4)+" "+jsonobject.getString("card_type"));
                }
                else{
                    card_text = "";

                    card.setText("xxxx-");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        if (i == android.R.id.home) {

            finish();
            return true;

        }

        else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}
