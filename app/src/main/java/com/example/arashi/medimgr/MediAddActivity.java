package com.example.arashi.medimgr;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MediAddActivity extends ActionBarActivity {

    private android.support.design.widget.TabLayout tabs_hp, tabs_pq;
    private TabLayout.Tab enterHand, enterPush, enterPic, enterQRCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_add);

        initTabs();
    }

    public void initTabs() {
        initTabs_HP();
        initTabs_PQ();
    }

    public void initTabs_HP() {
        tabs_hp = (android.support.design.widget.TabLayout) findViewById(R.id.tabs_hp);

        enterHand = tabs_hp.newTab().setCustomView(R.layout.enter_tab_hand);
        enterPush = tabs_hp.newTab().setCustomView(R.layout.enter_tab_push);

        tabs_hp.addTab(enterHand);
        tabs_hp.addTab(enterPush);

        tabs_hp.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                startEnterActivity(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                startEnterActivity(pos);
            }
        });
    }

    public void initTabs_PQ() {
        tabs_pq = (android.support.design.widget.TabLayout) findViewById(R.id.tabs_pq);

        enterPic = tabs_pq.newTab().setCustomView(R.layout.enter_tab_pic);
        enterQRCode = tabs_pq.newTab().setCustomView(R.layout.enter_tab_qrcode);

        tabs_pq.addTab(enterPic);
        tabs_pq.addTab(enterQRCode);

        tabs_pq.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                startEnterActivity(pos + 2);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                startEnterActivity(pos + 2);
            }
        });
    }

    public void startEnterActivity(int tabPosition) {
        Intent intent = new Intent();

        switch (tabPosition) {
            case 0:
                intent = new Intent(MediAddActivity.this, EnterHandActivity.class);
                break;
            case 1:
                intent = new Intent(MediAddActivity.this, EnterPushActivity.class);
                break;
            case 2:
                intent = new Intent(MediAddActivity.this, EnterPicActivity.class);
                break;
            case 3:
                intent = new Intent(MediAddActivity.this, EnterQRCodeActivity.class);
                break;
            default: break;
        }
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medi_add, menu);
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
