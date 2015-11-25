package com.example.arashi.medimgr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MediAlarmsActivity extends ActionBarActivity {

    private ArrayList<Alarm> alarmList = new ArrayList<Alarm>();

    private ListView alarms;
    private static AlarmAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_alarms);

        alarms = (ListView)findViewById(R.id.alarms);

        alarmList.add(new Alarm(R.drawable.morning, "早餐", "07:00"));
        alarmList.add(new Alarm(R.drawable.noon, "午餐", "12:00"));
        alarmList.add(new Alarm(R.drawable.night, "晚餐", "17:00"));
        alarmList.add(new Alarm(R.drawable.sleep, "睡前", "23:00"));

        adapter = new AlarmAdapter(this, alarmList);

        alarms.setAdapter(adapter);
        alarms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startAlarmPreferencesActivity(i);
            }
        });
    }

    public void startAlarmPreferencesActivity(int alarmPosition) {

    }


    public static AlarmAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medi_alarms, menu);
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
