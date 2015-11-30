package com.example.arashi.medimgr;

import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static ParselibAdapter parselibAdapter;

    private Button morning, noon, night, sleep;

    private android.support.design.widget.TabLayout mediTabs;
    private TabLayout.Tab mediRecord, mediAlarms, mediAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        parselibAdapter = new ParselibAdapter(android_id);
        parselibAdapter.initialize(this);

        initTimeTakeButtons();
        initMediTabs();
    }

    public void initTimeTakeButtons() {
        morning = (Button)findViewById(R.id.morning);
        noon = (Button)findViewById(R.id.noon);
        night = (Button)findViewById(R.id.night);
        sleep = (Button)findViewById(R.id.sleep);

        morning.setOnClickListener(this);
        noon.setOnClickListener(this);
        night.setOnClickListener(this);
        sleep.setOnClickListener(this);
    }

    public void initMediTabs() {
        mediTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);

        mediRecord = mediTabs.newTab().setCustomView(R.layout.medi_tab_record);
        mediAlarms = mediTabs.newTab().setCustomView(R.layout.medi_tab_alarms);
        mediAdd = mediTabs.newTab().setCustomView(R.layout.medi_tab_add);

        mediTabs.addTab(mediRecord);
        mediTabs.addTab(mediAlarms);
        mediTabs.addTab(mediAdd);

        mediTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                startMediActivity(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                startMediActivity(pos);
            }
        });
    }

    public void startMediActivity(int tabPosition) {
        Intent intent = new Intent();

        switch (tabPosition) {
            case 0:
                intent = new Intent(MainActivity.this, MediRecordActivity.class);
                break;
            case 1:
                intent = new Intent(MainActivity.this, MediAlarmsActivity.class);
                break;
            case 2:
                intent = new Intent(MainActivity.this, MediAddActivity.class);
                break;
            default: break;
        }
        startActivity(intent);
    }


    public static ParselibAdapter getParseAdapter() {
        return parselibAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        int time_take = UserDrug.MORNING_TAKE;

        int id = v.getId();
        switch (id) {
            case R.id.morning:
                time_take = UserDrug.MORNING_TAKE;
                break;
            case R.id.noon:
                time_take = UserDrug.NOON_TAKE;
                break;
            case R.id.night:
                time_take = UserDrug.NIGHT_TAKE;
                break;
            case R.id.sleep:
                time_take = UserDrug.SLEEP_TAKE;
                break;
            default: break;
        }

        parselibAdapter.setContext(MainActivity.this);
        parselibAdapter.initUserDrug(time_take);
    }
}
