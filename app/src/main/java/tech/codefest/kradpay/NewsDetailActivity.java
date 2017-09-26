package tech.codefest.kradpay;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Neeraj on 3/19/2017.
 */

public class NewsDetailActivity extends AppCompatActivity {
    private WebView web;
    private String title;
    private String url1;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_detail_layout);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        Bundle extras = getIntent().getExtras();
        if (extras!= null) {

            title = extras.getString("title");

             url1 = extras.getString("url");



        }



        setContentView(R.layout.news_detail_layout);
        // Inflation

        web = (WebView) findViewById(R.id.news_detail_webview);
        web.setWebViewClient(new WebViewClient() {

            // This method will be triggered when the Page Started Loading

            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }

            // This method will be triggered when the Page loading is completed

            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
            }

            // This method will be triggered when error page appear

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                // You can redirect to your own page instead getting the default
                // error page
                Toast.makeText(NewsDetailActivity.this,
                        "The Requested Page Does Not Exist", Toast.LENGTH_LONG).show();
                web.loadUrl("http://www.os-templates.com/page-templates/404-templates/404-20");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        web.loadUrl(url1);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);







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

