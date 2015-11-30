package com.example.arashi.medimgr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YYZeng on 2015/11/30.
 */
public class UserDrugsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<UserDrug> userDrugs;
    private ArrayList<String> apperence_url_list;

    public UserDrugsAdapter(Context context, ArrayList<UserDrug> userDrugs, ArrayList<String> apperence_url_list) {
        this.context = context;
        this.userDrugs = userDrugs;
        this.apperence_url_list = apperence_url_list;
    }


    @Override
    public int getGroupCount() {
        return userDrugs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return userDrugs.get(groupPosition).getIndications();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return userDrugs.get(groupPosition).getChName();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView)convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String)getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView lblListItem = (TextView)convertView.findViewById(R.id.lblListItem);
        lblListItem.setText(childTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
