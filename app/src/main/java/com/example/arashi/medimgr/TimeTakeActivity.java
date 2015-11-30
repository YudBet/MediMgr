package com.example.arashi.medimgr;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class TimeTakeActivity extends ActionBarActivity {

    private ParselibAdapter parselibAdapter;

    private ArrayList<String> ch_name_list, indications_list, drug_ingredient_list, apprence_url_list;
    private ArrayList<Integer> drug_total_list, drug_remind_list, is_duplicated_list;

    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_take);

        parselibAdapter = MainActivity.getParseAdapter();

        setUserDrugFromBundle(savedInstanceState);

        text = (TextView)findViewById(R.id.text);
        String msg = "";
        for (String ch_name : ch_name_list) {
            msg += ch_name + " | ";
        }
        text.setText(msg);
    }

    public void setUserDrugFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                this.ch_name_list = bundle.getStringArrayList(ParselibAdapter.CH_NAME_KEY);
                this.indications_list = bundle.getStringArrayList(ParselibAdapter.INDICATIONS_KEY);
                this.drug_ingredient_list = bundle.getStringArrayList(ParselibAdapter.INGREDIENT_KEY);
                this.apprence_url_list = bundle.getStringArrayList(ParselibAdapter.APPRENCE_URL_KEY);
                this.drug_total_list = bundle.getIntegerArrayList(ParselibAdapter.DRUG_TOTAL_KEY);
                this.drug_remind_list = bundle.getIntegerArrayList(ParselibAdapter.DRUG_REMIND_KEY);
                this.is_duplicated_list = bundle.getIntegerArrayList(ParselibAdapter.DUPLICATED_KEY);
            }
        }
        else {
            this.ch_name_list = (ArrayList<String>)savedInstanceState.getSerializable(ParselibAdapter.CH_NAME_KEY);
            this.indications_list = (ArrayList<String>)savedInstanceState.getSerializable(ParselibAdapter.INDICATIONS_KEY);
            this.drug_ingredient_list = (ArrayList<String>)savedInstanceState.getSerializable(ParselibAdapter.INGREDIENT_KEY);
            this.apprence_url_list = (ArrayList<String>)savedInstanceState.getSerializable(ParselibAdapter.APPRENCE_URL_KEY);
            this.drug_total_list = (ArrayList<Integer>)savedInstanceState.getSerializable(ParselibAdapter.DRUG_TOTAL_KEY);
            this.drug_remind_list = (ArrayList<Integer>)savedInstanceState.getSerializable(ParselibAdapter.DRUG_REMIND_KEY);
            this.is_duplicated_list = (ArrayList<Integer>)savedInstanceState.getSerializable(ParselibAdapter.DUPLICATED_KEY);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_take, menu);
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
