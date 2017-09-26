package tech.codefest.kradpay;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import tech.codefest.kradpay.model.Offer;
import tech.codefest.kradpay.uploadimage.ImageActivity;

import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class MainActivity extends AppCompatActivity {
    private static final String LAST_TRANSACTION_URL ="http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/lasttransaction/";
    public boolean isFStart;
    Context mcontext;

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
    private boolean mShowingFragments = false;
    private List<Offer> newsList;
    private Response response;
    private String transaction;
    private String transaction_item;
    private String transaction_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer = (DrawerView) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
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

                        Intent e = new Intent(MainActivity.this, WalletActivity.class);
                        startActivity(e);

                        break;
                    case 5:

                        Intent f = new Intent(MainActivity.this, WalletActivity.class);
                        startActivity(f);

                        break;
                    case 6:

                        Intent g = new Intent(MainActivity.this, WalletActivity.class);
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

        mCardAdapter = new OfferPagerAdapter(MainActivity.this);
        new GetDataTask().execute();



        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mViewPager.setAdapter(mCardAdapter);





        Log.e(TAG, "True");

        new GetDataTask().execute();
new GetMyNewsTask().execute();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected( MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.action_item1:
                                Intent j = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(j);


                                break;

                            case R.id.action_item2:

                                Intent in = new Intent(MainActivity.this, ImageActivity.class);
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
    class GetMyNewsTask extends AsyncTask<Void, Void, String> {



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
                        .url(LAST_TRANSACTION_URL)
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
                last_transaction.setText("You Haven't madeAny Transaction \n No details Found");
            }



                try {
                   if(jsonobject==null){
                       last_transaction.setText("You Haven't madeAny Transaction \n No details Found");
                   }
                    else{
                    transaction_item=jsonobject.getString("item");
                    transaction_amount=jsonobject.getString("amount");
                       last_transaction.setText("Last Tranasaction \n"+transaction_item+"of amount "+transaction_amount);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }





        }
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
                        mCardAdapter.addCardItem(new Offer(json.getString("id"),json.getString("image"), json.getString("title"),json.getString("detail"), json.getString("redeem"),json.getString("percent")));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }}
            catch(NullPointerException e){


                Toast.makeText(getApplicationContext(), "Connection Problem", Toast.LENGTH_LONG).show();
            }

            mCardAdapter.notifyDataSetChanged();
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
            mCardShadowTransformer.enableScaling(true);
            mViewPager.setOffscreenPageLimit(3);

        }
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

//            if (includeEdge) {
//                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//                outRect.right = (column ) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//
//                if (position < spanCount) { // top edge
//                    outRect.top = spacing;
//                }
//                outRect.bottom = spacing; // item bottom
//            } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)

//            }
        }
    }

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

