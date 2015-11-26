package com.example.arashi.medimgr;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;


public class MainActivity extends ActionBarActivity {

    private android.support.design.widget.TabLayout mediTabs;
    private TabLayout.Tab mediRecord, mediAlarms, mediAdd;
    // Clock control!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "uuWSfxnVrzL4DttA75rN4u4NOSscf2qygvF3tLvB", "QwcvUtm9ByTWTb4JuIZ0NsUQrVLn3p0nQNFRGztT");

        initMediTabs();
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
}
