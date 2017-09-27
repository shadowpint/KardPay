package tech.codefest.kradpay.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static tech.codefest.kradpay.oauth2.constant.OauthConstant.CART_URL;

/**
 * Created by DELL on 9/27/2017.
 */

public class CartSave extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    static final String TRANSACTION_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/transaction/";
    ProgressDialog dialog;
    private Context context;

    public CartSave(Context context){
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
        dialog = new ProgressDialog(context);
        dialog.setTitle("Saving to Cart...");
        dialog.setMessage("Loading");
        dialog.show();
    }

    @Nullable
    @Override
    protected String doInBackground(String... params) {
        String da = params[0];
        String token=params[1];
        Log.e("token", da);

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");


            RequestBody body = RequestBody.create(JSON, da);
            Request request = new Request.Builder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .url(CART_URL)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();

            return response.body().string();
        } catch (@NonNull IOException e) {
            Log.e("cartsavetask", "" + e.getLocalizedMessage());
        }


        return null;
    }

    @Override
    protected void onPostExecute(String Response) {
        super.onPostExecute(Response);
        dialog.dismiss();
        delegate.cartsaveResult(Response);







    }
}
