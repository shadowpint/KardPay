package tech.codefest.kradpay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.codefest.kradpay.model.Offer;
import tech.codefest.kradpay.tasks.AsyncResponse;
import tech.codefest.kradpay.tasks.CartSave;
import tech.codefest.kradpay.tasks.DoTransaction;

public  class OfferPagerAdapter extends PagerAdapter implements CardAdapter, AsyncResponse {
    DoTransaction doTransaction ;
    CartSave cartSave ;
    private List<CardView> mViews;
    private List<Offer> mData;
    private float mBaseElevation;
    private Context context;
    boolean wrapInScrollView = true;
    private MaterialDialog dialog;
    private Map<String, String> mp;
    static final String TRANSACTION_URL = "https://transaction-brief.herokuapp.com/transaction/api/transaction/";
    String[] SPINNER_DATA = {"Andhra Bank","AXIS","Andhra Bank","Bank of Baroda","Bank of India","Bank of Maharashtra","Canara Bank","Citi Bank","Andhra Bank","Corporation Bank","Deutsche Bank","HDFC","ICICI","IndusInd Bank","Punjab National Bank","SBI"};
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button submit_btn,save_btn,add_btn,subtract_btn;
    private String jsrating;
    private EditText quantity_text ;
    private TextView item_buy,amount_text;


    private MaterialBetterSpinner materialBetterSpinner;
    
    private Response response;
    private String token;

    public OfferPagerAdapter(Context context,String token) {

        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.context = context;
        this.token=token;
    }

    public void addCardItem(Offer item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(final Offer item, View view) {
//        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        ImageView image=(ImageView)view.findViewById(R.id.image);
        Button btn=(Button)view.findViewById(R.id.button);
//        titleTextView.setText(item.getTitle());
        Glide.with(context).load(item.getImage()).into(image);
        contentTextView.setText(item.getTitle());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
buyDialog(item);







            }
        });
    }
    private void buyDialog(final Offer offer) {
        doTransaction =new DoTransaction (context);
        cartSave =new CartSave (context);
        final int[] num = {1};
        Log.e("buydialog","buydialog"+token);
        doTransaction.delegate = this;

        cartSave.delegate = this;
        mp = new HashMap<String, String>();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)


                .customView(R.layout.buy_view, wrapInScrollView);


        dialog = builder.build();


        dialog.show();
        View view = dialog.getCustomView();



        item_buy = (TextView) view.findViewById(R.id.item_buy);
        amount_text = (TextView) view.findViewById(R.id.amount);
        quantity_text = (EditText) view.findViewById(R.id.quantity);
       add_btn = (Button) view.findViewById(R.id.add);
        subtract_btn = (Button) view.findViewById(R.id.subtract);
        save_btn = (Button) view.findViewById(R.id.save);
        submit_btn = (Button) view.findViewById(R.id.submit);
        amount_text.setText(offer.getPrice());
        item_buy.setText(offer.getBrand()+" "+offer.getTitle());
        quantity_text.setText("1");

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                num[0]++;
                quantity_text.setText(String.valueOf(num[0]));
                amount_text.setText(String.valueOf(Float.parseFloat(offer.getPrice())*num[0]));


            }
        });
        subtract_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num[0]<1){

                    Toast.makeText(context, "Minimum Purchase Quantity is 1", Toast.LENGTH_SHORT).show();
                }
                else {
                    num[0]--;
                    quantity_text.setText(String.valueOf(num[0]));
                    amount_text.setText(String.valueOf(Float.parseFloat(offer.getPrice())*num[0]));
                }

            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                mp.put("amount", amount_text.getText().toString());
                mp.put("item", offer.getBrand()+""+offer.getTitle()+" of amount "+  amount_text.getText().toString()+" for "+quantity_text.getText().toString()+" pieces");
                mp.put("reward", "1.25");


                Gson gson = new Gson();
                jsrating = gson.toJson(mp);
                Log.e("data", jsrating);
                Log.e("tokenheader","tokenheader"+ token);
                dialog.dismiss();
                doTransaction.execute(jsrating,token);

            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartSave =new CartSave (context);
                dialog.dismiss();
                mp.put("item_id", offer.getId());


                Gson gson = new Gson();
                jsrating = gson.toJson(mp);
                Log.e("data", jsrating);
                Log.e("tokenheader","tokenheader"+ token);
                dialog.dismiss();
                cartSave.execute(jsrating,token);

            }
        });
    }


    @Override
    public void transactionFinish(String Response){
        Log.e("Transfer Response",Response);
        Toast.makeText(context, "Successfully Transfered", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void lastTransactionResult(String output) {
        throw new UnsupportedOperationException("MethodDefination.lastTransactionResult()");
    }

    @Override
    public void cartsaveResult(String Response) {
        Log.e("Transfer Response",Response);
        Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show();
    }


}
