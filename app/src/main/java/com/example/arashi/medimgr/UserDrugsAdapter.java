package com.example.arashi.medimgr;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by YYZeng on 2015/11/30.
 */
public class UserDrugsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<UserDrug> userDrugs;
    private ArrayList<String> apperence_url_list;


    private ProgressDialog simpleWaitDialog;
    private ImageView lblListItemImg;
    private TextView lblListHeader, lblListItem;


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

        lblListHeader = (TextView)convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String)getChild(groupPosition, childPosition);
        //sString childApprenceUrl = apperence_url_list.get(childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        lblListItemImg = (ImageView)convertView.findViewById(R.id.lblListItemImg);
        /*
        if (childApprenceUrl.length() > 0) {
            new ImageDownloader().execute(childApprenceUrl);
        }
*/
        lblListItem = (TextView)convertView.findViewById(R.id.lblListItem);
        lblListItem.setText(childTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    private class ImageDownloader extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            return downloadBitmap(params[0].toString());
        }

        @Override
        protected void onPreExecute() {
            simpleWaitDialog = ProgressDialog.show(context, "請稍後", "正在下載圖片...");
        }

        @Override
        protected void onPostExecute(Object result) {
            lblListItemImg.setImageBitmap((Bitmap)result);
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
