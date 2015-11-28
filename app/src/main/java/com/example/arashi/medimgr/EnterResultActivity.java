package com.example.arashi.medimgr;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;


public class EnterResultActivity extends ActionBarActivity {

    public static final int DRUG_DOSAGE = 0;
    public static final int DRUG_INGREDIENT = 1;
    public static final int DRUG_APPRENCE = 2;
    private static final String[] PARSE_CLASSES = {
            "drug_dosage", "drug_ingredient", "drug_apprence"};

    private int init_count = 0;
    private String drug_id,
            ch_name, indications,  // from class drug_dosage
            drug_ingredient,       // from class drug_ingredient
            apprence_url;          // from class drug_apprence

    private ProgressDialog simpleWaitDialog;

    private ImageView medi_img;
    private TextView medi_name;
    private EditText medi_count;
    private ToggleButton morning, noon, night, sleep;
    private Button finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_result);

        drug_id = getDrugIdFromBundle(savedInstanceState);
        initDrugInfo();
    }

    public String getDrugIdFromBundle(Bundle savedInstanceState) {
        String drug_id;
        String from = "";
        String num = "";

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                from = bundle.getString("FROM");
                num = bundle.getString("NUM");
            }
        }
        else {
            from = (String)savedInstanceState.getSerializable("FROM");
            num = (String)savedInstanceState.getSerializable("NUM");
        }

        drug_id = from + "字第" + num + "號";
        return drug_id;
    }


    public void initDrugInfo() {
        initDrugDosage("drug_id", drug_id); // init ch_name, drug_dosage, indications
        initDrugIngredient("drug_id", drug_id); // init drug_ingredient
        initDrugApprence("drug_id", drug_id); // init color, shape, apprence_url
    }

    public void initDrugDosage(String where, String equalTo) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASSES[DRUG_DOSAGE]);
        query.whereEqualTo(where, equalTo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    setDrugDosage(
                            object.getString("ch_name"),
                            object.getString("indications")
                    );
                } else {
                    Log.d("drug_dosage", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void initDrugIngredient(String where, String equalTo) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASSES[DRUG_INGREDIENT]);
        query.whereEqualTo(where, equalTo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    setDrugIngredient(object.getString("drug_ingredient"));
                } else {
                    Log.d("drug_ingredient", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void initDrugApprence(String where, String equalTo) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASSES[DRUG_APPRENCE]);
        query.whereEqualTo(where, equalTo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    setDrugApprence(apprence_url = object.getString("apprence_url"));
                } else {
                    Log.d("drug_apprence", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void setDrugDosage(String ch_name, String indications) {
        this.ch_name = ch_name;
        this.indications = indications;
        init_count++;
        checkDrugInitEnd();
    }

    public void setDrugIngredient(String drug_ingredient) {
        this.drug_ingredient = drug_ingredient;
        init_count++;
        checkDrugInitEnd();
    }

    public void setDrugApprence(String apprence_url) {
        this.apprence_url = apprence_url;
        init_count++;
        checkDrugInitEnd();
    }

    public void checkDrugInitEnd() {
        if (init_count != PARSE_CLASSES.length) return;
        init_count = 0;
        initView();
    }


    public void initView() {
        showMediData();
        showMediSettings();
        showFinishButton();
    }

    public void showMediData() {
        medi_name = (TextView)findViewById(R.id.medi_name);
        medi_name.setText(ch_name);

        medi_img = (ImageView)findViewById(R.id.medi_img);
        if (apprence_url.length() != 0) {
            new ImageDownloader().execute(apprence_url);
        }
    }

    public void showMediSettings() {
        medi_count = (EditText)findViewById(R.id.medi_count);

        morning = (ToggleButton)findViewById(R.id.morning);
        noon = (ToggleButton)findViewById(R.id.noon);
        night = (ToggleButton)findViewById(R.id.night);
        sleep = (ToggleButton)findViewById(R.id.sleep);
        morning.toggle();
        noon.toggle();
        night.toggle();
        sleep.toggle();
    }

    public void showFinishButton() {
        finish = (Button)findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enter_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ImageDownloader extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            return downloadBitmap(params[0].toString());
        }

        @Override
        protected void onPreExecute() {
            simpleWaitDialog = ProgressDialog.show(EnterResultActivity.this,
                    "Wait", "Downloading Image");
        }

        @Override
        protected void onPostExecute(Object result) {
            medi_img.setImageBitmap((Bitmap)result);
            simpleWaitDialog.dismiss();
        }

        private Bitmap downloadBitmap(String url) {
            final DefaultHttpClient client = new DefaultHttpClient();
            final HttpGet getRequest = new HttpGet(url);

            try {
                HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return null;
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }

            return null;
        }
    }
}
