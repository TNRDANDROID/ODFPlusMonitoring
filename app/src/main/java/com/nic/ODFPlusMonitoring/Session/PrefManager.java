package com.nic.ODFPlusMonitoring.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nic.ODFPlusMonitoring.Constant.AppConstant;

import org.json.JSONArray;


/**
 * Created by AchanthiSudan on 11/01/19.
 */
public class PrefManager {

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String APP_KEY = "AppKey";
    private static final String KEY_USER_AUTH_KEY = "auth_key";
    private static final String KEY_USER_PASS_KEY = "pass_key";
    private static final String KEY_ENCRYPT_PASS = "pass";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_PASSWORD = "UserPassword";
    private static final String KEY_DISTRICT_CODE = "District_Code";
    private static final String KEY_BLOCK_CODE = "Block_Code";
    private static final String KEY_PV_CODE = "Pv_Code";
    private static final String KEY_DISTRICT_NAME = "District_Name";
    private static final String KEY_BLOCK_NAME = "Block_Name";
    private static final String KEY_VILLAGE_LIST_PV_NAME = "Village_List_Pv_Name";
    private static final String KEY_SPINNER_SELECTED_BLOCKCODE = "spinner_selected_block_code";
    private static final String KEY_SPINNER_SELECTED_PVCODE = "spinner_selected_pv_code";
    private static final String KEY_AUTOCOMPLETE_SELECTED_BANK_NAME = "autocomplete_selected_bank_name";
    private static final String KEY_AUTOCOMPLETE_SELECTED_BANK_ID = "autocomplete_selected_bank_id";
    private static final String KEY_SPINNER_SELECTED_CATEGORY_ID= "spinner_selected_category_id";
    private static final String KEY_SPINNER_SELECTED_CATEGORY_OTHERS_ID= "spinner_selected_category_others_id";
    private static final String KEY_BLOCK_CODE_JSON = "block_code_json";
    private static final String KEY_VILLAGE_CODE_JSON = "village_code_json";
    private static final String KEY_AUTOCOMPLETE_SELECTED_BRANCH_ID = "autocomplete_selected_branch_id";
    private static final String KEY_AUTOCOMPLETE_SELECTED_IFSC_CODE = "autocomplete_selected_ifsc_code";
    private static final String KEY_SPINNER_SELECTED_CATEGORY_NAME = "spinner_selected_category_name";
    private static final String KEY_ACTIVITY_NAME= "activity_name";


    private static final String IMEI = "imei";
    private static final String MOTIVATOR_ID = "motivator_id";
    private static final String SCHEDULE_MASTER_ID = "schedule_master_id";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public Integer getScheduleMasterId() {
        return pref.getInt(SCHEDULE_MASTER_ID,0);
    }

    public void setScheduleMasterId(Integer scheduleMasterId) {
        editor.putInt(SCHEDULE_MASTER_ID,scheduleMasterId);
        editor.commit();
    }

    public String getIMEI() {
        return pref.getString(IMEI,null);
    }

    public void setImei(String imei) {
        editor.putString(IMEI,imei);
        editor.commit();
    }

    public void setAppKey(String appKey) {
        editor.putString(APP_KEY, appKey);
        editor.commit();
    }

    public String getAppKey() {
        return pref.getString(APP_KEY, null);
    }


    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    public void setUserAuthKey(String userAuthKey) {
        editor.putString(KEY_USER_AUTH_KEY, userAuthKey);
        editor.commit();
    }

    public String getUserAuthKey() {
        return pref.getString(KEY_USER_AUTH_KEY, null);
    }

    public void setUserPassKey(String userPassKey) {
        editor.putString(KEY_USER_PASS_KEY, userPassKey);
        editor.commit();
    }

    public String getMotivatorId() {
        return pref.getString(MOTIVATOR_ID, null);
    }

    public void setMotivatorId(String userPassKey) {
        editor.putString(MOTIVATOR_ID, userPassKey);
        editor.commit();
    }

    public String getUserPassKey() {
        return pref.getString(KEY_USER_PASS_KEY, null);
    }


    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public String   getUserName() { return pref.getString(KEY_USER_NAME, null); }

    public void setUserPassword(String userPassword) {
        editor.putString(KEY_USER_PASSWORD, userPassword);
        editor.commit();
    }

    public String   getUserPassword() { return pref.getString(KEY_USER_PASSWORD, null); }


    public void setEncryptPass(String pass) {
        editor.putString(KEY_ENCRYPT_PASS, pass);
        editor.commit();
    }

    public String getEncryptPass() {
        return pref.getString(KEY_ENCRYPT_PASS, null);
    }

    public Object setDistrictCode(Object key) {
        editor.putString(KEY_DISTRICT_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDistrictCode() {
        return pref.getString(KEY_DISTRICT_CODE, null);
    }


    public Object setBlockCode(Object key) {
        editor.putString(KEY_BLOCK_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getBlockCode() {
        return pref.getString(KEY_BLOCK_CODE, null);
    }



    public Object setPvCode(Object key) {
        editor.putString(KEY_PV_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getPvCode() {
        return pref.getString(KEY_PV_CODE, null);
    }




    public Object setDistrictName(Object key) {
        editor.putString(KEY_DISTRICT_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDistrictName() {
        return pref.getString(KEY_DISTRICT_NAME, null);
    }

    public Object setBlockName(Object key) {
        editor.putString(KEY_BLOCK_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getBlockName() {
        return pref.getString(KEY_BLOCK_NAME, null);
    }


    public void setVillageListPvName(String key) {
        editor.putString(KEY_VILLAGE_LIST_PV_NAME,  key);
        editor.commit();
    }

    public String getVillageListPvName() {
        return pref.getString(KEY_VILLAGE_LIST_PV_NAME, null);
    }


    public void setKeySpinnerSelectedBlockcode(String userName) {
        editor.putString(KEY_SPINNER_SELECTED_BLOCKCODE, userName);
        editor.commit();
    }

    public String   getKeySpinnerSelectedBlockcode() {
        return pref.getString(KEY_SPINNER_SELECTED_BLOCKCODE, null);
    }

    public void setKeySpinnerSelectedPvcode(String userName) {
        editor.putString(KEY_SPINNER_SELECTED_PVCODE, userName);
        editor.commit();
    }

    public String   getKeySpinnerSelectedPVcode() {
        return pref.getString(KEY_SPINNER_SELECTED_PVCODE, null);
    }

    public void setKeyAutocompleteSelectedBankName(String userName) {
        editor.putString(KEY_AUTOCOMPLETE_SELECTED_BANK_NAME, userName);
        editor.commit();
    }

    public String getKeyAutocompleteSelectedBankName() {
        return pref.getString(KEY_AUTOCOMPLETE_SELECTED_BANK_NAME, null);
    }


    public Integer setKeyAutocompleteSelectedBankID(Integer userName) {
        editor.putInt(KEY_AUTOCOMPLETE_SELECTED_BANK_ID, userName);
        editor.commit();
        return userName;
    }

    public Integer getKeyAutocompleteSelectedBankID() {
        return pref.getInt(KEY_AUTOCOMPLETE_SELECTED_BANK_ID, 0);
    }

    public Integer setSpinnerSelectedCategoryId(Integer userName) {
        editor.putInt(KEY_SPINNER_SELECTED_CATEGORY_ID, userName);
        editor.commit();
        return userName;
    }

    public Integer getSpinnerSelectedCategoryId() {
        return pref.getInt(KEY_SPINNER_SELECTED_CATEGORY_ID, 0);
    }


    public void clearSharedPreferences(Context context) {
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor.clear();
        editor.apply();
    }


    public void setBlockCodeJson(JSONArray jsonarray) {
        editor.putString(KEY_BLOCK_CODE_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getBlockCodeJsonList() {
        return pref.getString(KEY_BLOCK_CODE_JSON, null);
    }

    public JSONArray getBlockCodeJson() {
        JSONArray jsonData = null;
        String strJson = getBlockCodeJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefBlockJson",""+jsonData);
        return jsonData;
    }

    public void setVillagePvCodeJson(JSONArray jsonarray) {
        editor.putString(KEY_VILLAGE_CODE_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getVillagePvCodeJsonList() {
        return pref.getString(KEY_VILLAGE_CODE_JSON, null);
    }

    public JSONArray getVillagePvCodeJson() {
        JSONArray jsonData = null;
        String strJson = getVillagePvCodeJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefVillageJson",""+jsonData);
        return jsonData;
    }
    public Integer setKeyAutocompleteSelectedBranchID(Integer userName) {
        editor.putInt(KEY_AUTOCOMPLETE_SELECTED_BRANCH_ID, userName);
        editor.commit();
        return userName;
    }

    public Integer getKeyAutocompleteSelectedBranchID() {
        return pref.getInt(KEY_AUTOCOMPLETE_SELECTED_BRANCH_ID, 0);
    }

    public void setKeyAutocompleteSelectedIfscCode(String userName) {
        editor.putString(KEY_AUTOCOMPLETE_SELECTED_IFSC_CODE, userName);
        editor.commit();
    }

    public String getKeyAutocompleteSelectedIfscCode() {
        return pref.getString(KEY_AUTOCOMPLETE_SELECTED_IFSC_CODE, null);
    }

    public void setSpinnerSelectedCategoryName(String userName) {
        editor.putString(KEY_SPINNER_SELECTED_CATEGORY_NAME, userName);
        editor.commit();
    }

    public String getSpinnerSelectedCategoryName() {
        return pref.getString(KEY_SPINNER_SELECTED_CATEGORY_NAME, null);
    }
    public void setActivityName(String userName) {
        editor.putString(KEY_ACTIVITY_NAME, userName);
        editor.commit();
    }

    public String   getActivityName() {
        return pref.getString(KEY_ACTIVITY_NAME, null);
    }
}
