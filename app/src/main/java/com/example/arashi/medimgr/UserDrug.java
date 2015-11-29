package com.example.arashi.medimgr;

/**
 * Created by Mist on 2015/11/30.
 */
public class UserDrug {

    private String drug_id;
    private String drug_ingredient;
    private int drug_total;
    private boolean morning_take, noon_take, night_take, sleep_take;

    public UserDrug(String drug_id, String drug_ingredient) {
        setDrugID(drug_id);
        setDrugIngredient(drug_ingredient);
    }


    public void setDrugID(String drug_id) {
        this.drug_id = drug_id;
    }

    public void setDrugIngredient(String drug_ingredient) {
        this.drug_ingredient = drug_ingredient;
    }

    public void setDrugTotal(int drug_total) {
        this.drug_total = drug_total;
    }

    public void setTimeTake(boolean[] time_take) {
        this.morning_take = time_take[0];
        this.noon_take = time_take[1];
        this.night_take = time_take[2];
        this.sleep_take = time_take[3];
    }
}
