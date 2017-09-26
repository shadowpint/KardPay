package tech.codefest.kradpay;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Response;

/**
 * Created by Neeraj on 3/19/2017.
 */

public class OfferDetailActivity extends AppCompatActivity {
    MediaPlayer mediaplayer;
    private static Response response;
    private String TAG = MainActivity.class.getSimpleName();

    static final String MAIN_URL = "http://starlord.hackerearth.com/edfora/cokestudio";
    ProgressBar progressBar;
    private RecyclerView recyclerView;
//    private SongsAdapter adapter;
//    private List<Song> songList;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_detail_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();



        try {
            Glide.with(this).load(R.drawable.offer1).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Offer Detail");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

//    class GetDataTask extends AsyncTask<Void, Void, String> {
//
//        ProgressDialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            /**
//             * Progress Dialog for User Interaction
//             */
//            dialog = new ProgressDialog(OfferDetailActivity.this);
//            dialog.setTitle("Please wait...");
//            dialog.setMessage("Loading");
//            dialog.show();
//        }
//
//        @Nullable
//        @Override
//        protected String doInBackground(Void... params) {
//
//            try {
//                OkHttpClient client = new OkHttpClient();
//
//
//
//
//                Request request = new Request.Builder()
//                        .url(MAIN_URL)
//                        .build();
//                response = client.newCall(request).execute();
//
//                return response.body().string();
//            } catch (@NonNull IOException e) {
//                Log.e(TAG, "" + e.getLocalizedMessage());
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String Response) {
//            super.onPostExecute(Response);
//            dialog.dismiss();
//            JSONArray jsonarray = null;
//            try {
//                jsonarray = new JSONArray(Response);
//                Log.e(TAG, String.valueOf(jsonarray));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            for (int i = 0; i < jsonarray.length(); i++) {
//                Song song = new Song();
//                JSONObject json = null;
//                try {
//                    json = jsonarray.getJSONObject(i);
//                    song.setSong(json.getString("song"));
//                    song.setUrl(json.getString("url"));
//                    song.setArtists(json.getString("artists"));
//                    song.setCover(json.getString("cover_image"));
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                songList.add(song);
//            }
//
//
//            adapter.notifyDataSetChanged();
//
//
//
//        }
//    }

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

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
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
