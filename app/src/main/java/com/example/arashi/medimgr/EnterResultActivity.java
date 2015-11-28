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

import com.parse.FindCallback;
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
import java.util.List;


public class EnterResultActivity extends ActionBarActivity {

    public static final int DRUG_DOSAGE = 0;
    public static final int DRUG_INGREDIENT = 1;
    public static final int DRUG_APPRENCE = 2;
    private static final String[] PARSE_CLASSES = {
            "drug_dosage", "drug_ingredient", "drug_apprence"};

    private int user_id = 0;

    private int init_count = 0;
    private String drug_id,
            ch_name, indications,  // from class drug_dosage
            drug_ingredient,       // from class drug_ingredient
            apprence_url;          // from class drug_apprence
    public boolean isDuplicated = false;

    private ProgressDialog simpleWaitDialog;

    private TextView duplicated_text;
    private ImageView duplicated_img;

    private TextView medi_name;
    private ImageView medi_img;
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

        checkDrugDuplicate("user_id", user_id);
    }

    public void checkDrugDuplicate(String where, int equalTo) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("user_drug");
        query.whereEqualTo(where, equalTo).whereEqualTo("drug_ingredient", drug_ingredient);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    isDuplicated = (objects.size() != 0);
                    initView();
                } else {
                    Log.d("user_drug", "Error: " + e.getMessage());
                }
            }
        });
    }


    public void initView() {
        showMediData();
        showMediSettings();
        showMediDuplicated();
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

    public void showMediDuplicated() {
        duplicated_img = (ImageView)findViewById(R.id.duplicated_img);
        duplicated_text = (TextView)findViewById(R.id.duplicated_text);
        duplicated_img.setVisibility(View.VISIBLE);
        duplicated_text.setVisibility(View.VISIBLE);
        if (isDuplicated) {
            duplicated_img.setImageResource(R.drawable.cancel_24);
            duplicated_text.setText("該藥品發生重複用藥情形，\n建議與醫師討論是否用藥。");
        }
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
                    "請稍後", "正在下在圖片...");
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
