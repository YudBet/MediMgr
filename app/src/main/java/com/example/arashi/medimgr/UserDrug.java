package com.example.arashi.medimgr;

/**
 * Created by Mist on 2015/11/30.
 */
public class UserDrug {

    private String drug_id;
    private String ch_name;
    private String drug_ingredient;
    private String indications;
    private int drug_total, drug_remind;
    private boolean morning_take, noon_take, night_take, sleep_take;
    private boolean duplicated;

    public UserDrug(String drug_id, String ch_name, String drug_ingredient, String indications) {
        setDrugID(drug_id);
        setChName(ch_name);
        setDrugIngredient(drug_ingredient);
        setIndications(indications);
    }


    public void setDrugID(String drug_id) {
        this.drug_id = drug_id;
    }

    public void setChName(String ch_name) {
        this.ch_name = ch_name;
    }

    public void setDrugIngredient(String drug_ingredient) {
        this.drug_ingredient = drug_ingredient;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public void setDrugTotal(int drug_total) {
        this.drug_total = drug_total;
    }

    public void setDrugRemind(int drug_remind) {
        this.drug_remind = drug_remind;
    }

    public void setTimeTake(boolean[] time_take) {
        this.morning_take = time_take[0];
        this.noon_take = time_take[1];
        this.night_take = time_take[2];
        this.sleep_take = time_take[3];
    }

    public void setDuplicated(boolean duplicated) {
        this.duplicated = duplicated;
    }


    public String getDrugID() {
        return drug_id;
    }

    public String getChName() {
        return ch_name;
    }

    public String getDrugIngredient() {
        return drug_ingredient;
    }

    public String getIndications() {
        return indications;
    }

    public int getDrugTotal() {
        return drug_total;
    }

    public int getDrug_remind() {
        return drug_remind;
    }

    public boolean[] getTimeTake() {
        boolean[] time_take = new boolean[4];
        time_take[0] = morning_take;
        time_take[1] = noon_take;
        time_take[2] = night_take;
        time_take[3] = sleep_take;
        return time_take;
    }

    public boolean getDuplicated() {
        return duplicated;
    }
}
