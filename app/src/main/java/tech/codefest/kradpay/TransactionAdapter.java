package tech.codefest.kradpay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.gson.Gson;
import tech.codefest.kradpay.model.Transaction;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Neeraj on 19/03/17.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    static final String BOOKMARK_URL = "https://transaction-brief.herokuapp.com/transaction/api/transaction/";
    private String TAG = TransactionAdapter.class.getSimpleName();
    private Context mContext;
    private List<Transaction> transactionList;
   public HashMap<String, String> ratingquery = new HashMap<String, String>();
    private Map<String, String> mp;
    private String jsrating;

    private String token;
    private Response response;
    private Date date;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item,amount,transaction_id,reward,transaction_date;




        public MyViewHolder(View view) {
            super(view);
            item = (TextView) view.findViewById(R.id.item);
            amount= (TextView) view.findViewById(R.id.amount);
            transaction_id = (TextView) view.findViewById(R.id.transaction_id);
            reward = (TextView) view.findViewById(R.id.reward);
            transaction_date= (TextView) view.findViewById(R.id.transaction_date);


        }
    }


    public TransactionAdapter(Context mContext, List<Transaction> transactionList, String token) {
        this.mContext = mContext;
        this.transactionList = transactionList;
        this.token=token;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        mp = new HashMap<String, String>();
         final Transaction transaction = transactionList.get(position);
        holder.item.setText(transaction.getItem());
        holder.amount.setText(transaction.getAmount());
        holder.transaction_id.setText(transaction.getTr_id());
        holder.reward.setText(transaction.getReward());
        Calendar mydate = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            date = dateFormat.parse(transaction.getTr_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mydate.setTime(date);
        holder.transaction_date.setText(mydate.get(Calendar.DAY_OF_MONTH)+"."+mydate.get(Calendar.MONTH)+"."+mydate.get(Calendar.YEAR));




//        // loading transaction cover using Glide library
//        Glide.with(mContext).load(transaction.getUrl()).into(holder.thumbnail);





  }


    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
