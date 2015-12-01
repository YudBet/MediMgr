package com.example.arashi.medimgr;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mist on 2015/11/30.
 */
public class ParselibAdapter {

    private static final String APPLICATION_ID = "uuWSfxnVrzL4DttA75rN4u4NOSscf2qygvF3tLvB";
    private static final String CLIENT_ID = "QwcvUtm9ByTWTb4JuIZ0NsUQrVLn3p0nQNFRGztT";

    public static final String PARSE_USER_DRUG_CLASS = "user_drug";
    public static final String[] PARSE_CLASSES = {
            "drug_dosage", "drug_ingredient", "drug_apprence"};
    public static final int DRUG_DOSAGE = 0;
    public static final int DRUG_INGREDIENT = 1;
    public static final int DRUG_APPRENCE = 2;

    // class user_drug keys
    public static final String USER_ID_KEY = "user_id";
    public static final String DRUG_ID_KEY = "drug_id";
    public static final String DRUG_TOTAL_KEY = "drug_total";
    public static final String DRUG_REMIND_KEY = "drug_remind";
    public static final String DUPLICATED_KEY = "duplicated";
    public static final String[] TIME_TAKE_KEYS = {"morning", "noon", "night", "sleep"};

    // class drug_dosage keys
    public static final String CH_NAME_KEY = "ch_name";
    public static final String INDICATIONS_KEY = "indications";

    // class drug_ingredient key
    public static final String INGREDIENT_KEY = "drug_ingredient";

    // class drug_apprance key
    public static final String APPRENCE_URL_KEY = "apprence_url";


    private String ch_name, indications,
            drug_ingredient,
            apprence_url;
    private boolean isDuplicated;

    private String user_id, drug_id;

    private int initCount = 0;
    private Context context;


    public ParselibAdapter(String user_id) {
        setUserID(user_id);
    }

    public void initialize(Context context) {
        Parse.initialize(context, APPLICATION_ID, CLIENT_ID);
    }


    public void initDrugInfo(String drug_id) {
        this.drug_id = drug_id;

        initDrugDosage(DRUG_ID_KEY, drug_id); // init ch_name, indications
        initDrugIngredient(DRUG_ID_KEY, drug_id); // init drug_ingredient
        initDrugApprence(DRUG_ID_KEY, drug_id); // init apprence_url
    }

    private void initDrugDosage(String where, String equalTo) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASSES[DRUG_DOSAGE]);
        query.whereEqualTo(where, equalTo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ch_name = object.getString(CH_NAME_KEY);
                    indications = object.getString(INDICATIONS_KEY);
                    waitDrugInitEndThenCheckDuplicate();
                } else {
                    Log.d("drug_dosage", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void initDrugIngredient(String where, String equalTo) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASSES[DRUG_INGREDIENT]);
        query.whereEqualTo(where, equalTo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    drug_ingredient = object.getString(INGREDIENT_KEY);
                    waitDrugInitEndThenCheckDuplicate();
                } else {
                    Log.d("drug_ingredient", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void initDrugApprence(String where, String equalTo) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASSES[DRUG_APPRENCE]);
        query.whereEqualTo(where, equalTo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    apprence_url = object.getString(APPRENCE_URL_KEY);
                    waitDrugInitEndThenCheckDuplicate();
                } else {
                    Log.d("drug_apprence", "Error: " + e.getMessage());
                }
            }
        });
    }


    private void waitDrugInitEndThenCheckDuplicate() {
        initCount++;
        if (initCount != PARSE_CLASSES.length) return;
        initCount = 0;

        checkDrugDuplicate();
    }

    private void checkDrugDuplicate() {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_USER_DRUG_CLASS);
        query.whereEqualTo(USER_ID_KEY, user_id).whereEqualTo(INGREDIENT_KEY, drug_ingredient);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    isDuplicated = (objects.size() != 0);
                    onDrugInitTotalEnd();
                } else {
                    Log.d("user_drug", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void onDrugInitTotalEnd() {
        Intent intent = new Intent(context, EnterResultActivity.class);
        intent.putExtra(DRUG_ID_KEY, drug_id);
        intent.putExtra(INGREDIENT_KEY, drug_ingredient);
        intent.putExtra(INDICATIONS_KEY, indications);
        intent.putExtra(CH_NAME_KEY, ch_name);
        intent.putExtra(APPRENCE_URL_KEY, apprence_url);
        intent.putExtra(DUPLICATED_KEY, isDuplicated);
        context.startActivity(intent);

        ((EnterHandActivity)context).finish();
    }


    private ArrayList<String> ch_name_list, indications_list, drug_ingredient_list, apprence_url_list;
    private ArrayList<Integer> drug_total_list, drug_remind_list, is_duplicated_list;

    public void initUserDrug(int time_take) {
        ch_name_list = new ArrayList<String>();
        indications_list = new ArrayList<String>();
        drug_ingredient_list = new ArrayList<String>();
        apprence_url_list = new ArrayList<String>();
        drug_total_list = new ArrayList<Integer>();
        drug_remind_list = new ArrayList<Integer>();
        is_duplicated_list = new ArrayList<Integer>();

        initUserDrug(USER_ID_KEY, user_id, TIME_TAKE_KEYS[time_take]);
    }

    private void initUserDrug(String where, String equalTo, String time_take_key) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_USER_DRUG_CLASS);
        query.whereEqualTo(where, equalTo).whereEqualTo(time_take_key, true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                for (ParseObject obj : objects) {
                    ch_name_list.add(obj.getString(CH_NAME_KEY));
                    indications_list.add(obj.getString(INDICATIONS_KEY) + "");
                    drug_ingredient_list.add(obj.getString(INGREDIENT_KEY) + "");
                    apprence_url_list.add(obj.getString(APPRENCE_URL_KEY) + "");
                    drug_total_list.add(obj.getInt(DRUG_TOTAL_KEY));
                    drug_remind_list.add(obj.getInt(DRUG_REMIND_KEY));
                    is_duplicated_list.add(obj.getBoolean(DUPLICATED_KEY) ? 1 : 0);
                }

                onUserDrugInitTotalEnd();
            }
        });
    }

    private void onUserDrugInitTotalEnd() {
        Intent intent = new Intent(context, TimeTakeActivity.class);

        intent.putStringArrayListExtra(CH_NAME_KEY, ch_name_list);
        intent.putStringArrayListExtra(INDICATIONS_KEY, indications_list);
        intent.putStringArrayListExtra(INGREDIENT_KEY, drug_ingredient_list);
        intent.putStringArrayListExtra(APPRENCE_URL_KEY, apprence_url_list);
        intent.putIntegerArrayListExtra(DRUG_TOTAL_KEY, drug_total_list);
        intent.putIntegerArrayListExtra(DRUG_REMIND_KEY, drug_remind_list);
        intent.putIntegerArrayListExtra(DUPLICATED_KEY, is_duplicated_list);

        context.startActivity(intent);
    }


    public void enterUserDrug(UserDrug userDrug) {
        ParseObject user_drug = new ParseObject(PARSE_USER_DRUG_CLASS);
        user_drug.put(USER_ID_KEY, user_id);
        user_drug.put(DRUG_ID_KEY, drug_id);
        user_drug.put(DRUG_TOTAL_KEY, userDrug.getDrugTotal());
        user_drug.put(DRUG_REMIND_KEY, userDrug.getDrug_remind());
        user_drug.put(DUPLICATED_KEY, userDrug.getDuplicated());

        boolean[] time_take = userDrug.getTimeTake();
        user_drug.put(TIME_TAKE_KEYS[UserDrug.MORNING_TAKE], time_take[UserDrug.MORNING_TAKE]);
        user_drug.put(TIME_TAKE_KEYS[UserDrug.NOON_TAKE], time_take[UserDrug.NOON_TAKE]);
        user_drug.put(TIME_TAKE_KEYS[UserDrug.NIGHT_TAKE], time_take[UserDrug.NIGHT_TAKE]);
        user_drug.put(TIME_TAKE_KEYS[UserDrug.SLEEP_TAKE], time_take[UserDrug.SLEEP_TAKE]);

        user_drug.put(CH_NAME_KEY, userDrug.getChName());
        user_drug.put(INGREDIENT_KEY, userDrug.getDrugIngredient());
        user_drug.put(INDICATIONS_KEY, userDrug.getIndications());

        user_drug.saveInBackground();
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void setUserID(String user_id) {
        this.user_id = user_id;
    }
}
