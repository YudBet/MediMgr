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

    private static ParselibAdapter parselibAdapter;

    private String drug_id, drug_ingredient, indications, ch_name = "", apprence_url = "";
    private boolean isDuplicated = false;
    private int drug_total = 0;
    private boolean[] time_take = new boolean[4]; // [0, 1, 2, 3] -> [morning, noon, night, sleep]

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

        parselibAdapter = MainActivity.getParseAdapter();

        setDrugDataFromBundle(savedInstanceState);
        initView();
    }


    public void setDrugDataFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                this.drug_id = bundle.getString("DRUG_ID");
                this.drug_ingredient = bundle.getString("INGREDIENT");
                this.indications = bundle.getString("INDICATIONS");
                this.ch_name = bundle.getString("CH_NAME");
                this.apprence_url = bundle.getString("APPRENCE_URL");
                this.isDuplicated = bundle.getBoolean("IS_DUPLICATED");
            }
        }
        else {
            this.drug_id = (String)savedInstanceState.getSerializable("DRUG_ID");
            this.drug_ingredient = (String)savedInstanceState.getSerializable("INGREDIENT");
            this.indications = (String)savedInstanceState.getSerializable("INDICATIONS");
            this.ch_name = (String)savedInstanceState.getSerializable("CH_NAME");
            this.apprence_url = (String)savedInstanceState.getSerializable("APPRENCE_URL");
            this.isDuplicated = (boolean)savedInstanceState.getSerializable("IS_DUPLICATED");
        }
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
                drug_total = Integer.parseInt(medi_count.getText().toString());
                time_take[0] = morning.isChecked();
                time_take[1] = noon.isChecked();
                time_take[2] = night.isChecked();
                time_take[3] = sleep.isChecked();

                UserDrug userDrug = new UserDrug(drug_id, ch_name, drug_ingredient, indications);
                userDrug.setDrugTotal(drug_total);
                userDrug.setDrugRemind(drug_total);
                userDrug.setTimeTake(time_take);
                userDrug.setDuplicated(isDuplicated);

                parselibAdapter.enterUserDrug(userDrug);
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
                    "請稍後", "正在下載圖片...");
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
