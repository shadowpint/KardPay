package tech.codefest.kradpay.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static tech.codefest.kradpay.oauth2.constant.OauthConstant.LAST_TRANSACTION_URL;

/**
 * Created by DELL on 9/27/2017.
 */

public class LastTransaction extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;

    ProgressDialog dialog;
    private Context context;

    public LastTransaction(Context context){
        this.context = context;
    }
    //  Create a new boolean and preference and set it to true

    private Response response;

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
String token=params[0];
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
            Log.e("last transaction", "" + e.getLocalizedMessage());
        }


        return null;
    }
    @Override
    protected void onPostExecute(String Response) {
        super.onPostExecute(Response);

        delegate.lastTransactionResult(Response);







    }
}
