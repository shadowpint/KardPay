package tech.codefest.kradpay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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

import tech.codefest.kradpay.model.Transaction;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class TransactionActivity extends AppCompatActivity {
    public boolean isFirstStart;
    Context mcontext;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String token;

    private static com.squareup.okhttp.Response response;
    private static final String TRANSACTION_URL ="http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/transaction/";
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList;
    private ProgressDialog pDialog;

    private Map<String, String> mp;
    private TextView niceCounter;
    private int niceCounterValue = 37;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transactions");
        SharedPreferences prefs = this.getSharedPreferences(
                android.support.v7.appcompat.BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        token = prefs.getString("oauth2.accesstoken", "");



        transactionList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TransactionActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new TransactionAdapter(TransactionActivity.this, transactionList,token);
        mp = new HashMap<String, String>();
        recyclerView.setAdapter(adapter);


        Log.e(TAG, "True");

        new GetMyTransactionTask().execute();






    }


    class GetMyTransactionTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(TransactionActivity.this);
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
                        .url(TRANSACTION_URL)
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
            transactionList.clear();

            Log.e(TAG, "Response");
            JSONArray jsonarray = null;
            try {
                jsonarray = new JSONArray(Response);
                Log.e(TAG, String.valueOf(jsonarray.getJSONObject(0)));
            } catch (JSONException e) {
                Toast.makeText(TransactionActivity.this,"No Details", Toast.LENGTH_SHORT).show();
            }

            for (int i = 0; i < jsonarray.length(); i++) {
                Transaction transaction = new Transaction();
                JSONObject json = null;

                try {
                    json = jsonarray.getJSONObject(i);
                    transaction.setId(json.getString("id"));
                    transaction.setItem(json.getString("item"));

                    transaction.setAmount(json.getString("amount"));
                    transaction.setTr_date(json.getString("tr_date"));
                    transaction.setTr_id(json.getString("tr_id"));
                    transaction.setReward(json.getString("reward"));



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                transactionList.add(transaction);
            }


            adapter.notifyDataSetChanged();


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

