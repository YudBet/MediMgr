package com.example.arashi.medimgr;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class EnterResultActivity extends ActionBarActivity {

    private static final String[] PARSE_CLASSES = {
            "drug_dosaage", "drug_ingredient", "drug_apprence"};
    public static final int DRUG_DOSAGE = 0;
    public static final int DRUG_INGREDIENT = 1;
    public static final int DRUG_APPRENCE = 2;

    private String drug_id,
            ch_name, drug_dosage, indications,  // from class drug_dosage
            drug_ingredient,                    // from class drug_ingredient
            color, shape, apprence_url;         // from class drug_apprence

    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_result);

        drug_id = getDrugIdFromBundle(savedInstanceState);

        initDrugInfo();

        text = (TextView)findViewById(R.id.text);
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
                    ch_name = object.getString("ch_name");
                    drug_dosage = object.getString("drug_dosage");
                    indications = object.getString("indications");
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
                    drug_ingredient = object.getString("drug_ingredient");
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
                    color = object.getString("color");
                    shape = object.getString("shape");
                    apprence_url = object.getString("apprence_url");
                } else {
                    Log.d("drug_apprence", "Error: " + e.getMessage());
                }
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
}
