package com.example.arashi.medimgr;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class EnterHandActivity extends ActionBarActivity {

    private ArrayAdapter<String> fromAdapter;
    private static final String[] fromList = {
            "衛署藥製", "衛署藥輸", "衛署成製", "衛署中藥輸",
            "衛署醫器製", "衛署醫器輸", "衛署粧製", "衛署粧輸",
            "衛署菌疫製", "衛署菌疫輸", "衛署色輸", "內衛藥製",
            "內衛藥輸", "內衛成製", "內衛菌疫製", "內衛菌疫輸",
            "內藥登", "署藥兼食製", "衛署成輸", "衛署罕藥輸",
            "衛署罕藥製", "罕菌疫輸", "罕菌疫製", "罕醫器輸",
            "罕醫器製", "衛署色製", "衛署粧陸輸", "衛署藥陸輸",
            "衛署醫器陸輸", "衛署醫器製壹", "衛署醫器輸壹", "衛署醫器陸輸壹",
            "衛部藥製", "衛部藥輸", "衛部成製", "衛部醫器製",
            "衛部醫器輸", "衛部粧製", "衛部粧輸", "衛部菌疫製",
            "衛部菌疫輸", "衛部色輸", "部藥兼食製", "衛部成輸",
            "衛部罕藥輸", "衛部罕藥製", "衛部罕菌疫輸", "衛部罕菌疫製",
            "衛部罕醫器輸", "衛部色製", "衛部粧陸輸", "衛部藥陸輸",
            "衛部醫器陸輸", "衛部醫器製壹", "衛部醫器輸壹", "衛部醫器陸輸壹",
            "衛署菌製"
    };

    private String currentFrom = fromList[0];
    private String currentNum = "";

    private Spinner from;
    private EditText number;
    private Button toPic, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_hand);

        from = (Spinner)findViewById(R.id.from);
        number = (EditText)findViewById(R.id.number);

        fromAdapter = new ArrayAdapter<String>(EnterHandActivity.this, android.R.layout.simple_list_item_1,fromList);

        from.setAdapter(fromAdapter);
        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentFrom = fromList[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        initButtons();
    }

    public void initButtons() {
        initToPicButton();
        initConfirmButton();
    }

    public void initToPicButton() {
        toPic = (Button)findViewById(R.id.to_pic);
        toPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterHandActivity.this, EnterPicActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void initConfirmButton() {
        confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNum = number.getText().toString();

                Intent intent = new Intent(EnterHandActivity.this, EnterResultActivity.class);
                intent.putExtra("FROM", currentFrom);
                intent.putExtra("NUM", currentNum);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enter_hand, menu);
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
