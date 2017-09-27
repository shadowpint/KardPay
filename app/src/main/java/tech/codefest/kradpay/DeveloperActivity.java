package tech.codefest.kradpay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DeveloperActivity extends AppCompatActivity {
  private TextView niceCounter;
  private int niceCounterValue = 37;
private ImageView github;
    private ImageView playstore;
    private ImageView linkedin;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.developer_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      github = (ImageView)findViewById(R.id.github);
      playstore = (ImageView)findViewById(R.id.playstore);
      linkedin = (ImageView)findViewById(R.id.linkedin);
      github.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v){
              Intent intent = new Intent();
              intent.setAction(Intent.ACTION_VIEW);
              intent.addCategory(Intent.CATEGORY_BROWSABLE);
              intent.setData(Uri.parse("https://github.com/dominicneeraj"));
              startActivity(intent);
          }
      });
      playstore.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v){
              Intent intent = new Intent();
              intent.setAction(Intent.ACTION_VIEW);
              intent.addCategory(Intent.CATEGORY_BROWSABLE);
              intent.setData(Uri.parse("https://gist.github.com/dominicneeraj/71d528e58b295411eae8f5bd3a827ddf"));
              startActivity(intent);
          }
      });
      linkedin.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v){
              Intent intent = new Intent();
              intent.setAction(Intent.ACTION_VIEW);
              intent.addCategory(Intent.CATEGORY_BROWSABLE);
              intent.setData(Uri.parse("https://www.linkedin.com/in/dominicneeraj"));
              startActivity(intent);
          }
      });
    //in the toolbar

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
