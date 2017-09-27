package tech.codefest.kradpay;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import tech.codefest.kradpay.model.Offer;
import tech.codefest.kradpay.tasks.AsyncResponse;
import tech.codefest.kradpay.tasks.CartSave;
import tech.codefest.kradpay.tasks.DoTransaction;
import tech.codefest.kradpay.tasks.LastTransaction;
import tech.codefest.kradpay.uploadimage.ImageActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private static final String LAST_TRANSACTION_URL ="http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/lasttransaction/";
    public boolean isFStart;
    Context mcontext;
    DoTransaction doTransaction ;
    LastTransaction lastTransaction;
    CartSave cartSave =new CartSave(MainActivity.this);
    private String token;


    static final String BOOKMARK_URL = "https://news-brief.herokuapp.com/news/api/news/";
    ProgressBar progressBar;
    private RecyclerView recyclerView;


    private ProgressDialog pDialog;

    private Map<String, String> mp;
    private TextView niceCounter;
    private int niceCounterValue = 37;
    private DrawerView drawer;
    private ActionBarDrawerToggle drawerToggle;
    private Drawable myDrawable;
    private Bitmap theBitmap;
    private BitmapDrawable drawable;
    private static final String TAG = MainActivity.class.getSimpleName();

    private ViewPager mViewPager;
private TextView last_transaction;
    private OfferPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    static final String MAIN_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/offer/";
    static final String TRANSACTION_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/transaction/";
    private boolean mShowingFragments = false;
    private List<Offer> newsList;
    private Response response;
    private String transaction;
    private String transaction_item;
    private String transaction_amount;
    private Button pay_btn;
    private Button transfer_btn;
    boolean wrapInScrollView = true;
    private MaterialDialog dialog;
    String[] SPINNER_DATA = {"Andhra Bank","AXIS","Andhra Bank","Bank of Baroda","Bank of India","Bank of Maharashtra","Canara Bank","Citi Bank","Andhra Bank","Corporation Bank","Deutsche Bank","HDFC","ICICI","IndusInd Bank","Punjab National Bank","SBI"};

    String[] OPERATOR_DATA = {"Vodafone","Idea","Airtel","JIO","BSNL Mobile","Aircel","Telenor","Tata DoCoMo","MTS","MTNL"};

    private String contact_text;
    private String email_text;
    private String name_text;
    private String dob_text;
    private String bank_text;
    private String card_text;
    private String gender_text;
    private MaterialBetterSpinner materialBetterSpinner;




    private EditText firstname_edit;
    private EditText lastname_edit;
    private EditText dob_edit;
    private EditText contact_edit;


    private String first_name;
    private String last_name;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button submit_btn;
    private String jsrating;
    private EditText payee_edit;
    private EditText payee_contact_edit;
    private EditText account_no_edit;
    private EditText ifsc_edit;
    private EditText transfer_amount_edit;
    private EditText transfer_reason_edit;
    private EditText recharge_number_edit;
    private EditText recharge_amount_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer = (DrawerView) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


         lastTransaction =new LastTransaction (MainActivity.this);

       lastTransaction.delegate = this;
        mp = new HashMap<String, String>();
         pay_btn = (Button) findViewById(R.id.pay_button);
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            rechargeDialog();

            }
        });
        transfer_btn = (Button) findViewById(R.id.transfer_button);
        transfer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               transferDialog();

            }
        });
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.color_primary_dark));
        drawerLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_primary_dark));
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.closeDrawer(drawer);



drawer.setBackgroundColor(ContextCompat.getColor(this, R.color.color_primary_dark));
        drawer.addItem(new DrawerItem()
                .setDrawerTheme(
                        new DrawerTheme(this)
                                .setBackgroundColorRes(R.color.transparent)
                                .setTextColorPrimaryRes(R.color.text_color_primary_3)
                                .setTextColorSecondaryRes(R.color.text_color_secondary_3)
                )
                .setTextPrimary("My Wallet")
                .setImage(ContextCompat.getDrawable(this, R.drawable.wallet_1))

        );

        drawer.addItem(new DrawerItem()
                .setDrawerTheme(
                        new DrawerTheme(this)
                                .setBackgroundColorRes(R.color.transparent)
                                .setTextColorPrimaryRes(R.color.text_color_primary_3)
                                .setTextColorSecondaryRes(R.color.text_color_secondary_3)
                )
                .setTextPrimary("Profile")
                .setImage(ContextCompat.getDrawable(this, R.drawable.account_white))

        );






        drawer.addItem(new DrawerItem()
                .setDrawerTheme(
                        new DrawerTheme(this)
                                .setBackgroundColorRes(R.color.transparent)
                                .setTextColorPrimaryRes(R.color.text_color_primary_3)
                                .setTextColorSecondaryRes(R.color.text_color_secondary_3)
                )
                .setTextPrimary("Transactions")
                .setImage(ContextCompat.getDrawable(this, R.drawable.transaction))
        );

        drawer.addItem(new DrawerItem()
                .setDrawerTheme(
                        new DrawerTheme(this)
                                .setBackgroundColorRes(R.color.transparent)
                                .setTextColorPrimaryRes(R.color.text_color_primary_3)
                                .setTextColorSecondaryRes(R.color.text_color_secondary_3)
                )
                .setTextPrimary("Offers")
                .setImage(ContextCompat.getDrawable(this, R.drawable.offer))

        );
        drawer.addItem(new DrawerItem()
                .setDrawerTheme(
                        new DrawerTheme(this)
                                .setBackgroundColorRes(R.color.transparent)
                                .setTextColorPrimaryRes(R.color.text_color_primary_3)
                                .setTextColorSecondaryRes(R.color.text_color_secondary_3)
                )
                .setTextPrimary("Transfer")
                .setImage(ContextCompat.getDrawable(this, R.drawable.transfer))

        );
        drawer.addItem(new DrawerItem()
                .setDrawerTheme(
                        new DrawerTheme(this)
                                .setBackgroundColorRes(R.color.transparent)
                                .setTextColorPrimaryRes(R.color.text_color_primary_3)
                                .setTextColorSecondaryRes(R.color.text_color_secondary_3)
                )
                .setTextPrimary("Pay")
                .setImage(ContextCompat.getDrawable(this, R.drawable.pay))

        );
        drawer.addItem(new DrawerItem()
                .setDrawerTheme(
                        new DrawerTheme(this)
                                .setBackgroundColorRes(R.color.transparent)
                                .setTextColorPrimaryRes(R.color.text_color_primary_3)
                                .setTextColorSecondaryRes(R.color.text_color_secondary_3)
                )
                .setTextPrimary("Contact")
                .setImage(ContextCompat.getDrawable(this, R.drawable.contact_icon))

        );


        drawer.setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long id, int position) {
                drawer.selectItem(position);
                switch (position) {

                    case 0:
                        Intent a = new Intent(MainActivity.this, WalletActivity.class);
                        startActivity(a);


                        break;

                    case 1:

                        Intent b = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(b);

                        break;
                    case 2:

                        Intent c = new Intent(MainActivity.this, TransactionActivity.class);
                        startActivity(c);

                        break;
                    case 3:

                        Intent d = new Intent(MainActivity.this, OfferActivity.class);
                        startActivity(d);

                        break;
                    case 4:
                        drawerLayout.closeDrawer(drawer);
                        transferDialog();

                        break;
                    case 5:
                        drawerLayout.closeDrawer(drawer);
                        rechargeDialog();

                        break;
                    case 6:

                        Intent g = new Intent(MainActivity.this, DeveloperActivity.class);
                        startActivity(g);

                        break;
                }
            }
        });







        drawer.addProfile(new DrawerProfile()
                .setId(1)
                .setDrawerTheme(
                        new DrawerTheme(this)
                                .setBackgroundColorRes(R.color.transparent)
                                .setTextColorPrimaryRes(R.color.text_color_primary_3)
                                .setTextColorSecondaryRes(R.color.text_color_secondary_3)
                )
                .setBackground(ContextCompat.getDrawable(this, R.drawable.credit))


        );




        drawer.setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
            @Override
            public void onClick(DrawerProfile profile, long id) {
                Toast.makeText(MainActivity.this, "Clicked profile *" + id, Toast.LENGTH_SHORT).show();
            }
        });
        drawer.setOnProfileSwitchListener(new DrawerProfile.OnProfileSwitchListener() {
            @Override
            public void onSwitch(DrawerProfile oldProfile, long oldId, DrawerProfile newProfile, long newId) {
                Toast.makeText(MainActivity.this, "Switched from profile *" + oldId + " to profile *" + newId, Toast.LENGTH_SHORT).show();
            }
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Intro App Initialize SharedPreferences
                SharedPreferences getSharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                isFStart = getSharedPreferences.getBoolean("fStart", true);

                //  Check either activity or app is open very first time or not and do action
                if (isFStart) {

                    //  Launch application introduction screen
                    Intent i = new Intent(MainActivity.this, MyIntro.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getSharedPreferences.edit();
                    e.putBoolean("fStart", false);
                    e.apply();
                }
            }
        });
        t.start();


        last_transaction=(TextView)findViewById(R.id.transaction_button);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        SharedPreferences prefs = this.getSharedPreferences(
                android.support.v7.appcompat.BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        token = prefs.getString("oauth2.accesstoken", "");

        mCardAdapter = new OfferPagerAdapter(MainActivity.this,token);









        Log.e(TAG, "True");

        new GetDataTask().execute();
        lastTransaction.execute(token);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected( MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.action_item1:
                                Intent j = new Intent(MainActivity.this, TransactionActivity.class);
                                startActivity(j);


                                break;

                            case R.id.action_item2:

                                Intent in = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(in);

                                break;
                            case R.id.action_item3:

                                Intent i = new Intent(MainActivity.this, WalletActivity.class);
                                startActivity(i);

                                break;
                        }

                        return true;
                    }
                });



    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void rechargeDialog() {

        doTransaction =new DoTransaction (MainActivity.this);
        doTransaction.delegate = this;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)


                .customView(R.layout.recharge_view, wrapInScrollView);


        dialog = builder.build();


        dialog.show();
        View view = dialog.getCustomView();

        materialBetterSpinner = (MaterialBetterSpinner) view.findViewById(R.id.operator);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, OPERATOR_DATA);

        materialBetterSpinner.setAdapter(adapter);



        recharge_number_edit = (EditText) view.findViewById(R.id.number);
        recharge_amount_edit = (EditText) view.findViewById(R.id.amount);






        submit_btn = (Button) view.findViewById(R.id.submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                mp.put("amount", recharge_amount_edit.getText().toString());
                mp.put("item", "Recharge of "+recharge_amount_edit.getText().toString()+" for Number "+recharge_number_edit.getText().toString());
                mp.put("reward", "1");


                Gson gson = new Gson();
                jsrating = gson.toJson(mp);
                Log.e(TAG, jsrating);
                dialog.dismiss();
                doTransaction.execute(jsrating,token);


            }
        });
    }
    private void transferDialog() {

        doTransaction =new DoTransaction (MainActivity.this);
        doTransaction.delegate = this;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)


                .customView(R.layout.transfer_view, wrapInScrollView);


        dialog = builder.build();


        dialog.show();
        View view = dialog.getCustomView();

        materialBetterSpinner = (MaterialBetterSpinner) view.findViewById(R.id.bank);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);

        payee_edit = (EditText) view.findViewById(R.id.payee_name);
        payee_contact_edit = (EditText) view.findViewById(R.id.payee_contact);
        account_no_edit = (EditText) view.findViewById(R.id.account_no);
        ifsc_edit = (EditText) view.findViewById(R.id.ifsc);

        transfer_amount_edit = (EditText) view.findViewById(R.id.transfer_amount);
        transfer_reason_edit = (EditText) view.findViewById(R.id.transfer_reason);



        submit_btn = (Button) view.findViewById(R.id.submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                mp.put("amount", transfer_amount_edit.getText().toString());
                mp.put("item", "Transfer to "+payee_edit.getText().toString()+" with Account Number "+account_no_edit.getText().toString()+" of amount "+ transfer_amount_edit.getText().toString()+" for "+transfer_reason_edit.getText().toString());
                mp.put("reward", "1");


                Gson gson = new Gson();
                jsrating = gson.toJson(mp);
                Log.e(TAG, jsrating);
                dialog.dismiss();
                doTransaction.execute(jsrating,token);


            }
        });
    }

    @Override
    public void transactionFinish(String Response){
        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void lastTransactionResult(String Response){

        Log.e(TAG, "Response lastTransaction"+Response);
        JSONObject jsonobject = null;
        try {
            jsonobject = new JSONObject(Response);
            Log.e(TAG, String.valueOf(jsonobject));
        } catch (JSONException e) {
            last_transaction.setText("You Haven't madeAny Transaction \n No details Found");
        }



        try {
            if(jsonobject==null){
                last_transaction.setText("You Haven't madeAny Transaction \n No details Found");
            }
            else{
                transaction_item=jsonobject.getString("item");
                transaction_amount=jsonobject.getString("amount");
                last_transaction.setText("Last Transaction \n"+transaction_item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void cartsaveResult(String Response){

        Log.e(TAG, "Response");
        Toast.makeText(MainActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();


    }

    class GetDataTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Loading");
            dialog.show();
        }

        @Nullable
        @Override
        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .url(MAIN_URL)
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
            dialog.dismiss();



            JSONArray jsonarray = null;
            if(Response!=null){
                Log.e(TAG, Response);
                try {
                    jsonarray = new JSONArray(Response);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Connection Problem", Toast.LENGTH_LONG).show();
                }

            }
            else
                Toast.makeText(getApplicationContext(), "Connection Problem", Toast.LENGTH_LONG).show();

            try{
                for (int i = 0; i < jsonarray.length(); i++) {

                    JSONObject json = null;

                    try {
                        json = jsonarray.getJSONObject(i);
                        mCardAdapter.addCardItem(new Offer(json.getString("id"),json.getString("image"), json.getString("title"),json.getString("price"),json.getString("brand"),json.getString("detail"), json.getString("redeem"),json.getString("percent")));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }}
            catch(NullPointerException e){


                Toast.makeText(getApplicationContext(), "Connection Problem", Toast.LENGTH_LONG).show();
            }

            mCardAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mCardAdapter);



            mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
            mCardShadowTransformer.enableScaling(true);
            mViewPager.setOffscreenPageLimit(3);

        }
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {

            case R.id.action_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);


    }
}

