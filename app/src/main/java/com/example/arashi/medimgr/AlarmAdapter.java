package com.example.arashi.medimgr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mist on 2015/11/24.
 */
public class AlarmAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private ArrayList<Alarm> alarmList;


    public AlarmAdapter(Context context, ArrayList<Alarm> alarmList) {
        myInflater = LayoutInflater.from(context);
        this.alarmList = alarmList;
    }


    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int i) {
        return alarmList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return alarmList.indexOf(getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = myInflater.inflate(R.layout.list_alarm, null);
        }

        ImageView alarmPic = (ImageView)view.findViewById(R.id.alarm_pic);
        TextView alarmName = (TextView)view.findViewById(R.id.alarm_name);
        TextView alarmTime = (TextView)view.findViewById(R.id.alarm_time);
        TextView alarmMusic = (TextView)view.findViewById(R.id.alarm_music);

        Alarm alarm = (Alarm)getItem(i);

        alarmPic.setImageResource(alarm.getPicture());
        alarmName.setText(alarm.getName());
        alarmTime.setText(alarm.getTime());
        alarmMusic.setText(alarm.getMusicPath());

        return view;
    }
}
