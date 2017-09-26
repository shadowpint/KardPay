package tech.codefest.kradpay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tech.codefest.kradpay.model.Offer;

/**
 * Created by Neeraj on 19/03/17.
 */
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    static final String BOOKMARK_URL = "https://offer-brief.herokuapp.com/offer/api/offer/";
    private String TAG = OfferAdapter.class.getSimpleName();
    private Context mContext;
    private List<Offer> offerList;
   public HashMap<String, String> ratingquery = new HashMap<String, String>();
    private Map<String, String> mp;
    private String jsrating;

    private String token;
    private Response response;
    private Date date;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title,detail;
        public Button redeem;




        public MyViewHolder(View view) {
            super(view);
            image= (ImageView) view.findViewById(R.id.offer_image);
            title = (TextView) view.findViewById(R.id.title);
            detail= (TextView) view.findViewById(R.id.detail);

            redeem = (Button) view.findViewById(R.id.redeem);



        }
    }


    public OfferAdapter(Context mContext, List<Offer> offerList, String token) {
        this.mContext = mContext;
        this.offerList = offerList;
        this.token=token;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        mp = new HashMap<String, String>();
         final Offer offer = offerList.get(position);
        Glide.with(mContext).load(offer.getImage()).into(holder.image);
        holder.title.setText(offer.getTitle());
        holder.detail.setText(offer.getDetail());

        holder.redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(offer.getRedeem()));
                mContext.startActivity(intent);






            }
        });




//        // loading offer cover using Glide library
//        Glide.with(mContext).load(offer.getUrl()).into(holder.thumbnail);





  }


    @Override
    public int getItemCount() {
        return offerList.size();
    }
}
