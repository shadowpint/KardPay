package tech.codefest.kradpay;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualRecognitionOptions;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import tech.codefest.kradpay.uploadimage.ImageActivity;

public class ProfileActivity extends AppCompatActivity {
  private TextView niceCounter;
    private static final String TAG = ProfileActivity.class.getSimpleName();
  private int niceCounterValue = 37;
private ImageView github;
    static final String PROFILE_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/profile/";
    static final String CARD_URL = "http://kradapi-semimountainous-bachelorhood.mybluemix.net/krad/api/card/";
    static final String IMAGE_URL = "https://gateway-a.watsonplatform.net/visual-recognition/api/v3/detect_faces?api_key=ed4ed7937a8e5bfee805b16f7ccdc0fcc492d587&version=2016-05-20";
    private ImageView playstore;
    private ImageView linkedin;
    private TextView contact;
    private TextView email;
    private TextView name;
    private TextView dob;
    private TextView gender;
    private TextView card;
    private TextView name1;
    private Button update_btn;
    private String contact_text;
    private String email_text;
    private String name_text;
    private String dob_text;
    private String gender_text;
    private String card_text;
    private String token;
    private Response response;
    private JSONObject userobject;
    boolean wrapInScrollView = true;
    private MaterialDialog dialog;
    MaterialBetterSpinner materialBetterSpinner ;


    private EditText firstname_edit;
    private EditText lastname_edit;
    private EditText dob_edit;
    private EditText contact_edit;
    private Button submit_btn;
    private Map<String, String> mp;
    private String jsrating;
    String[] SPINNER_DATA = {"Male","Female"};
    private int mYear;
    private int mMonth;
    private int mDay;
    private String first_name;
    private String last_name;
    private static final int RC_TAKE_PICTURE = 101;
    private static final int RC_STORAGE_PERMS = 102;
    //file uri
    private static final String KEY_FILE_URI = "key_file_uri";
    //download file uri
    private static final String KEY_DOWNLOAD_URL = "key_download_url";



    private Uri mFileUri = null;
    private StorageReference mStorageRef;
    private Uri mDownloadUrl = null;
    private ProgressDialog mProgressDialog;
    private String display_picture;
    private String display_url;
    private File file;

    @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.profile_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      SharedPreferences prefs = this.getSharedPreferences(
              android.support.v7.appcompat.BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

      token = prefs.getString("oauth2.accesstoken", "");
        mStorageRef = FirebaseStorage.getInstance().getReference();
      name = (TextView) findViewById(R.id.name_text);
        name1 = (TextView) findViewById(R.id.name1);
      email = (TextView) findViewById(R.id.email_text);
      dob = (TextView) findViewById(R.id.dob_text);
      contact = (TextView) findViewById(R.id.contact_text);
      gender = (TextView) findViewById(R.id.gender_text);
        card = (TextView) findViewById(R.id.card_text);
        update_btn = (Button) findViewById(R.id.update);
      github = (ImageView)findViewById(R.id.github);
        mp = new HashMap<String, String>();
        update_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               profileUpdate();
            }
        });
      github.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v){
              launchCamera();
          }
      });

    //in the toolbar
      new GetProfileTask().execute();
        new GetCardTask().execute();
  }
    @AfterPermissionGranted(RC_STORAGE_PERMS)
    private void launchCamera() {
        Log.d(TAG, "launchCamera");

        // Check that we have permission to read images from external storage.
        String perm = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_STORAGE_PERMS, perm);
            return;
        }

        // Choose file storage location, must be listed in res/xml/file_paths.xml
        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
         file = new File(dir, UUID.randomUUID().toString() + ".jpg");
        try {
            // Create directory if it does not exist.
            if (!dir.exists()) {
                dir.mkdir();
            }
            boolean created = file.createNewFile();
            Log.d(TAG, "file.createNewFile:" + file.getAbsolutePath() + ":" + created);
        } catch (IOException e) {
            Log.e(TAG, "file.createNewFile" + file.getAbsolutePath() + ":FAILED", e);
        }

        // Create content:// URI for file, required since Android N
        // See: https://developer.android.com/reference/android/support/v4/content/FileProvider.html
        mFileUri = FileProvider.getUriForFile(this,
                "tech.codefest.kradpay.fileprovider", file);

        // Create and launch the intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

        startActivityForResult(takePictureIntent, RC_TAKE_PICTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                if (mFileUri != null) {
                    Log.w("File Name", String.valueOf(mFileUri));
                    new CheckImageTask().execute(String.valueOf(file));

                } else {
                    Log.w(TAG, "File URI is null");
                }
            } else {

            }
        }
    }



    // [START upload_from_uri]
    private void uploadFromUri(Uri fileUri) {

        String m_path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getAbsolutePath();
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // [START get_child_ref]
        // Get a reference to store file at photos/<FILENAME>.jpg
        final StorageReference photoRef = mStorageRef.child("photos")
                .child(fileUri.getLastPathSegment());

        //final StorageReference photoRefs =m_path;
        // [END get_child_ref]

        // Upload file to Firebase Storage
        // [START_EXCLUDE]

        // [END_EXCLUDE]
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        photoRef.putFile(fileUri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Upload succeeded
                        Log.d(TAG, "uploadFromUri:onSuccess");

                        // Get the public download URL
                        mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl();

                        mp.put("pic_url", String.valueOf(mDownloadUrl));


                        Gson gson = new Gson();
                        display_picture = gson.toJson(mp);

                  new UpdateProfileTask().execute(display_picture);
                        // [START_EXCLUDE]



                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);

                        mDownloadUrl = null;

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        Toast.makeText(ProfileActivity.this, "Error: upload failed",
                                Toast.LENGTH_SHORT).show();

                        // [END_EXCLUDE]
                    }
                });
    }
    // [END upload_from_uri]

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }









    private void profileUpdate() {


        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)


                .customView(R.layout.profile_view, wrapInScrollView);


        dialog = builder.build();


        dialog.show();
        View view = dialog.getCustomView();

        materialBetterSpinner = (MaterialBetterSpinner) view.findViewById(R.id.gender_data);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);
        materialBetterSpinner.setText(gender_text);


        firstname_edit = (EditText) view.findViewById(R.id.first_name);
        lastname_edit = (EditText) view.findViewById(R.id.last_name);
        contact_edit = (EditText) view.findViewById(R.id.contact_data);
        dob_edit = (EditText) view.findViewById(R.id.dob_data);
        contact_edit.setText(card_text);


            firstname_edit.setText(first_name);
            lastname_edit.setText(last_name);




        dob_edit.setText(dob_text);

        dob_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dob_edit.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        submit_btn = (Button) view.findViewById(R.id.submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                mp.put("firstname", firstname_edit.getText().toString());
                mp.put("lastname", lastname_edit.getText().toString());
                mp.put("gender", materialBetterSpinner.getText().toString());
                mp.put("dob", dob_edit.getText().toString());
                mp.put("contact", contact_edit.getText().toString());

                Gson gson = new Gson();
                jsrating = gson.toJson(mp);
                Log.e(TAG, jsrating);
                new UpdateProfileTask().execute(jsrating);

            }
        });
    }


    class CheckImageTask extends AsyncTask<String, Void, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
showProgressDialog();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String da = params[0];
            Log.e("token1", da);
            File file = new File(da);
            VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
            service.setApiKey("ed4ed7937a8e5bfee805b16f7ccdc0fcc492d587");


            VisualRecognitionOptions options = new VisualRecognitionOptions.Builder()
                    .images(new File(da))
                    .build();
            DetectedFaces result = service.detectFaces(options).execute();

            Log.e("token1", String.valueOf(result));


            return String.valueOf(result);
        }

        @Override
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);
hideProgressDialog();


            Log.e(TAG, "Response"+Response);
            JSONObject jsonobject = null;
            try {
                jsonobject = new JSONObject(Response);
                Log.e(TAG, String.valueOf(jsonobject.getJSONArray("images").getJSONObject(0).getJSONArray("faces").length()));
                if(jsonobject.getJSONArray("images").getJSONObject(0).getJSONArray("faces").length()>0){
                uploadFromUri(mFileUri);
                }
                else{
                    Toast.makeText(ProfileActivity.this, "No face Detected, Please try Again", Toast.LENGTH_SHORT).show();


                    launchCamera();}
            } catch (JSONException e) {


            }









        }
    }



    class GetProfileTask extends AsyncTask<Void, Void, String> {



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
                MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");



                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .url(PROFILE_URL)

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



            Log.e(TAG, "Response" +Response);
            JSONObject jsonobject = null;
            try {
                jsonobject = new JSONObject(Response);
                Log.e(TAG, String.valueOf(jsonobject));
                    first_name=jsonobject.getString("firstname");
                last_name=jsonobject.getString("lastname");
                    name_text = first_name+" "+last_name;
                    Log.e("Name", name_text);
                    contact_text= jsonobject.getString("contact");
                    dob_text = jsonobject.getString("dob");
                    gender_text = jsonobject.getString("gender");
                    userobject = jsonobject.getJSONObject("user");
                    email_text=userobject.getString("email");
                display_url=jsonobject.getString("pic_url");



                if(display_url!=null) {
                    Glide.with(ProfileActivity.this).load(display_url).asBitmap().centerCrop().into(new BitmapImageViewTarget(github) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(ProfileActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            github.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                }
                else {github.setImageResource(R.drawable.github_logo);
                }
                    name.setText(name_text);
                    name1.setText(name_text);
                    contact.setText("+91 "+contact_text);
                    dob.setText(dob_text);
                    gender.setText(gender_text);
                    email.setText(email_text);


            } catch (JSONException e) {
                first_name="";
                last_name="";

                contact_text= "";
                dob_text = "";
                gender_text = "";

                email_text="";
profileUpdate();

            }


















        }
    }
    class UpdateProfileTask extends AsyncTask<String, Void, String> {



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
            String da = params[0];
            Log.e("token", da);

            try {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");


                RequestBody body = RequestBody.create(JSON, da);
                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .url(PROFILE_URL)
                        .post(body)
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


            }



            try {

                if(jsonobject!=null){
                    name_text = jsonobject.getString("firstname")+" "+jsonobject.getString("lastname");
                    Log.e("Name", name_text);
                    contact_text= jsonobject.getString("contact");
                    dob_text = jsonobject.getString("dob");
                    gender_text = jsonobject.getString("gender");
                     userobject = jsonobject.getJSONObject("user");
                    email_text=userobject.getString("email");
                    display_url=jsonobject.getString("pic_url");
                    name.setText(name_text);
                    name1.setText(name_text);
                    contact.setText("+91 "+contact_text);
                    dob.setText(dob_text);
                    gender.setText(gender_text);
                    email.setText(email_text);
                    if(display_url!=null) {
                        Glide.with(ProfileActivity.this).load(display_url).asBitmap().centerCrop().into(new BitmapImageViewTarget(github) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(ProfileActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                github.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                    }
                    else {github.setImageResource(R.drawable.github_logo);
                    }

                }
                else{

                    name_text = "";
                    contact_text= "";
                    dob_text = "";
                    gender_text = "";

                    email_text="";
                    name.setText(name_text);
                    name1.setText(name_text);
                    contact.setText("+91 "+contact_text);
                    dob.setText(dob_text);
                    gender.setText(gender_text);
                    email.setText(email_text);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }

    class GetCardTask extends AsyncTask<Void, Void, String> {



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
                        .url(CARD_URL)
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

            }



            try {

                if(jsonobject!=null){
                    card_text = jsonobject.getString("cardnumber");

                    card.setText("xxxx-"+card_text.substring(card_text.length()-4)+" "+jsonobject.getString("card_type"));
                }
                else{
                    card_text = "";

                    card.setText("xxxx-");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
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
