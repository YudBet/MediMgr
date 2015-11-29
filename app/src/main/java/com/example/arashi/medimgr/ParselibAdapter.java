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

import java.util.List;

/**
 * Created by Mist on 2015/11/30.
 */
public class ParselibAdapter {

    public static final String APPLICATION_ID = "uuWSfxnVrzL4DttA75rN4u4NOSscf2qygvF3tLvB";
    public static final String CLIENT_ID = "QwcvUtm9ByTWTb4JuIZ0NsUQrVLn3p0nQNFRGztT";

    public static final String PARSE_USER_DRUG_CLASS = "user_drug";
    public static final String[] PARSE_CLASSES = {
            "drug_dosage", "drug_ingredient", "drug_apprence"};
    public static final int DRUG_DOSAGE = 0;
    public static final int DRUG_INGREDIENT = 1;
    public static final int DRUG_APPRENCE = 2;

    private static final String USER_ID_KEY = "user_id";
    private static final String DRUG_ID_KEY = "drug_id";

    private static final String CH_NAME_KEY = "ch_name"; // class drug_dosage keys
    private static final String INDICATIONS_KEY = "indications";
    private static final String INGREDIENT_KEY = "drug_ingredient"; // class drug_ingredient key
    private static final String APPRENCE_URL_KEY = "apprence_url"; // class drug_apprance key

    private String ch_name, indications, // from class drug_dosage
            drug_ingredient, // from class drug_ingredient
            apprence_url; // from class drug_apprence
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
                    drug_ingredient =  object.getString(INGREDIENT_KEY);
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
        intent.putExtra("DRUG_ID", drug_id);
        intent.putExtra("CH_NAME", ch_name);
        intent.putExtra("APPRENCE_URL", apprence_url);
        intent.putExtra("INGREDIENT", drug_ingredient);
        intent.putExtra("IS_DUPLICATED", isDuplicated);
        context.startActivity(intent);

        ((EnterHandActivity)context).finish();
    }


    public void enterUserDrug(UserDrug userDrug) {

    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void setUserID(String user_id) {
        this.user_id = user_id;
    }
}
