package tech.codefest.kradpay;

import android.app.ProgressDialog;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WalletActivity extends AppCompatActivity {
    private static final String TAG = WalletActivity.class.getSimpleName();
    private static final CardType[] SUPPORTED_CARD_TYPES = { CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY };

    String[] SPINNER_DATA = {"VISA","MASTERCARD","AMEX","DINERS_CLUB","JCB","MAESTRO","UNIONPAY"};
    private String token;
    private SupportedCardTypesView mSupportedCardTypesView;
    static final String WALLET_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/wallet/";
    static final String TRANSACTION_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/transaction/";
    static final String CARD_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/card/";
    static final String REDEEM_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/redeem/";
    protected CardForm mCardForm;
  private TextView reward_text;
    private TextView amount_text;
  private int niceCounterValue = 37;
private ImageView github;
    private ImageView playstore;
    private ImageView linkedin;

    private MaterialDialog dialog;
    boolean wrapInScrollView = true;
    private Response response;
    private String reward;
    private String amount;
    MaterialBetterSpinner materialBetterSpinner ;
    private String selected;
    private EditText card_number;
    private EditText expiry_date;
    private EditText cvv;
    private Button submit_btn;

    private Button card_update_btn;
    private Button refresh_btn;
    private Button redeem_btn;
    private Map<String, String> mp;
    private String jsrating;
    private String  cardnumber;
    private String  expiry_month;
    private String  expiry_year;
    private String  date;
    private String  cvv_data;
    private String  card_type;
    private TextView carddetail;
    @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.wallet_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = this.getSharedPreferences(
                android.support.v7.appcompat.BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        token = prefs.getString("oauth2.accesstoken", "");
        mp = new HashMap<String, String>();
        new GetBalanceTask().execute();
        new GetCardTask().execute();
        refresh_btn = (Button)findViewById(R.id.refresh);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetBalanceTask().execute();
            }
        });
        card_update_btn = (Button)findViewById(R.id.cardupdate);
        card_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardAdd();
            }
        });

        redeem_btn = (Button)findViewById(R.id.redeem);
        redeem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetRedeemTask().execute();
            }
        });
        BarChart barChart = (BarChart) findViewById(R.id.chart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(8f, 0));
        entries.add(new BarEntry(2f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(20f, 3));
        entries.add(new BarEntry(15f, 4));
        entries.add(new BarEntry(19f, 5));

        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("2016");
        labels.add("2015");
        labels.add("2014");
        labels.add("2013");
        labels.add("2012");
        labels.add("2011");

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of lables into chart

        barChart.setDescription("Set Bar Chart Description");  // set the description

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(5000);
        reward_text = (TextView) findViewById(R.id.rewards);
        amount_text = (TextView) findViewById(R.id.balance);
        carddetail = (TextView) findViewById(R.id.carddetail);

    //in the toolbar

  }
    class GetBalanceTask extends AsyncTask<Void, Void, String> {



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
                        .url(WALLET_URL)
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
            JSONArray jsonarray = null;
            try {
                jsonarray = new JSONArray(Response);
                Log.e(TAG, String.valueOf(jsonarray.getJSONObject(0)));
            } catch (JSONException e) {
                cardAdd();
            }



            try {
                if(jsonarray.getJSONObject(0)==null){

                    cardAdd();
                }
                else {
                    reward = jsonarray.getJSONObject(0).getString("treward");
                    amount = jsonarray.getJSONObject(0).getString("amount");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            reward_text.setText("Rewards : "+reward+" "+"points");
            amount_text.setText("Balance : "+getString(R.string.Rs)+" "+amount);



        }
    }
    class GetRedeemTask extends AsyncTask<Void, Void, String> {



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
                        .url(REDEEM_URL)
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

                    reward = jsonobject.getString("treward");
                    amount = jsonobject.getString("amount");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            reward_text.setText("Rewards : "+reward+" "+"points");
            amount_text.setText("Balance : "+getString(R.string.Rs)+" "+amount);



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
                    cardnumber = jsonobject.getString("cardnumber");
                    expiry_month= jsonobject.getString("expiry_month");
                    expiry_year = jsonobject.getString("expiry_year");
                    cvv_data = jsonobject.getString("cvv");
                    card_type = jsonobject.getString("card_type");
                    date=expiry_month+"/"+ expiry_year;


                        carddetail.setText("xxxx-"+cardnumber.substring(cardnumber.length()-4)+" "+card_type);
                        amount_text.setText("Balance : "+getString(R.string.Rs)+" "+amount);
                    }
                else{
                        cardnumber = "";
                        expiry_month= "";
                        expiry_year = "";
                        cvv_data = "";
                        card_type = "";
                        date=expiry_month+"/"+ expiry_year;

            }


            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }
    private void cardAdd() {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)



                .customView(R.layout.custom_view, wrapInScrollView);



        dialog = builder.build();


        dialog.show();
        View view = dialog.getCustomView();

        materialBetterSpinner = (MaterialBetterSpinner)view.findViewById(R.id.card_type);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WalletActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);
        materialBetterSpinner.setText(card_type);


        card_number = (EditText)view.findViewById(R.id.card_number);
        expiry_date = (EditText)view.findViewById(R.id.exp_date);
        cvv = (EditText)view.findViewById(R.id.cvv);

card_number.setText(cardnumber);
        expiry_date.setText(date);

        cvv.setText(cvv_data);


        submit_btn = (Button)view.findViewById(R.id.submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                mp.put("cardnumber",card_number.getText().toString());
                mp.put("expiry_month",expiry_date.getText().toString().substring(0,2));
                mp.put("expiry_year",expiry_date.getText().toString().substring(3,7));
                mp.put("cvv",cvv.getText().toString());
                mp.put("card_type",materialBetterSpinner.getText().toString());

                Gson gson = new Gson();
                jsrating = gson.toJson(mp);
                Log.e(TAG, jsrating);
                new CreateAccountTask().execute(jsrating);

                Log.e(TAG, cardnumber+" cardnumber");
                if(cardnumber==null){
                reward_text.setText("Rewards : 200 points");
                amount_text.setText("Balance : "+getString(R.string.Rs)+" "+"5000");}
                carddetail.setText("xxxx-"+card_number.getText().toString().substring(card_number.getText().toString().length()-4)+" "+materialBetterSpinner.getText().toString());
            }
        });
    }
    class CreateAccountTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(WalletActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Loading");
            dialog.show();

        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String da = params[0];
            Log.e("token", da);
            if (token != null){
                try {
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON
                            = MediaType.parse("application/json; charset=utf-8");


                    RequestBody body = RequestBody.create(JSON, da);
                    Request request = new Request.Builder()
                            .header("Content-Type", "application/json")
                            .header("Authorization", "Bearer " + token)
                            .url(CARD_URL)
                            .post(body)
                            .build();
                    response = client.newCall(request).execute();

                    return response.body().string();
                } catch (@NonNull IOException e) {
                    Log.e(TAG, "" + e.getLocalizedMessage());
                }
            }else{
                Intent intent = new Intent(WalletActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }

            return null;
        }


        @Override
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);
            dialog.dismiss();
            Toast.makeText(WalletActivity.this, "Account Created/Updated", Toast.LENGTH_LONG).show();
            new GetBalanceTask().execute();






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
