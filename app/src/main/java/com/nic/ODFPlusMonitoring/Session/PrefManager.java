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
    private static final String app_Version = "app_Version";
    private static final String KEY_USER_AUTH_KEY = "auth_key";
    private static final String KEY_USER_PASS_KEY = "pass_key";
    private static final String KEY_ENCRYPT_PASS = "pass";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_PASSWORD = "UserPassword";
    private static final String KEY_DECRYPT_KEY = "Decrypt_Key";
    private static final String KEY_DISTRICT_CODE = "District_Code";
    private static final String KEY_BLOCK_CODE = "Block_Code";
    private static final String KEY_PV_CODE = "Pv_Code";
    private static final String KEY_DISTRICT_NAME = "District_Name";
    private static final String KEY_BLOCK_NAME = "Block_Name";
    private static final String KEY_PV_NAME = "Pv_Name";
    private static final String KEY_VILLAGE_LIST_PV_NAME = "Village_List_Pv_Name";
    private static final String KEY_LEVELS = "Levels";
    private static final String SERVICE_AUTH_KEY = "service_auth_key";
    private static final String KEY_SPINNER_SELECTED_BLOCKCODE = "spinner_selected_block_code";
    private static final String KEY_SPINNER_SELECTED_PVCODE = "spinner_selected_pv_code";
    private static final String KEY_SPINNER_SELECTED_SCHEME_SEQ_ID = "spinner_selected_scheme_seq_Id";
    private static final String KEY_SPINNER_SELECTED_FINYEAR = "spinner_selected_finyear";
    private static final String KEY_SCHEME_NAME = "Scheme_Name";
    private static final String KEY_FINANCIALYEAR_NAME = "FinancialYear_Name";
    private static final String KEY_ACTION_PROJECT_NAME = "Action_Project_Name";
    private static final String KEY_ACTION_WORKID = "Action_WorkId";
    private static final String KEY_ACTION_AMOUNT= "Action_Amount";
    private static final String KEY_ACTION_STAGE_LEVEL = "Action_Stage_Level";
    private static final String KEY_DELETE_ID = "deleteId";
    private static final String KEY_BLOCK_CODE_JSON = "block_code_json";
    private static final String KEY_VILLAGE_CODE_JSON = "village_code_json";
    private static final String KEY_SCHEME_SEQUENTIAL_ID_JSON = "SchemeSeqId_json";
    private static final String KEY_FIN_YEAR_JSON = "fin_year_json";


    private static final String IMEI = "imei";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getKeyDeleteId() {
        return pref.getString(KEY_DELETE_ID,null);
    }

    public void setKeyDeleteId(String deleteId) {
        editor.putString(KEY_DELETE_ID,deleteId);
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


    public Object setPvName(Object key) {
        editor.putString(KEY_PV_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getPvName() {
        return pref.getString(KEY_PV_NAME, null);
    }

    public void setVillageListPvName(String key) {
        editor.putString(KEY_VILLAGE_LIST_PV_NAME,  key);
        editor.commit();
    }

    public String getVillageListPvName() {
        return pref.getString(KEY_VILLAGE_LIST_PV_NAME, null);
    }



    public Object setLevels(Object key) {
        editor.putString(KEY_LEVELS, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getLevels() {
        return pref.getString(KEY_LEVELS, null);
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

    public void setKeySpinnerSelectedSchemeSeqId(String userName) {
        editor.putString(KEY_SPINNER_SELECTED_SCHEME_SEQ_ID, userName);
        editor.commit();
    }

    public String   getKeySpinnerSelectedSchemeSeqId() {
        return pref.getString(KEY_SPINNER_SELECTED_SCHEME_SEQ_ID, null);
    }

    public void setKeySpinnerSelectedFinyear(String userName) {
        editor.putString(KEY_SPINNER_SELECTED_FINYEAR, userName);
        editor.commit();
    }

    public String   getKeySpinnerSelectedFinyear() {
        return pref.getString(KEY_SPINNER_SELECTED_FINYEAR, null);
    }
    public void setServiceAuthKey(String key) {
        editor.putString(SERVICE_AUTH_KEY, key);
        editor.commit();
    }

    public String getServiceAuthKey() {
        return pref.getString(SERVICE_AUTH_KEY, null);
    }

    public  void setSchemeName(String key) {
        editor.putString(KEY_SCHEME_NAME,key);
        editor.commit();
    }

    public String getSchemeName() {return pref.getString(KEY_SCHEME_NAME,null);}

    public void setFinancialyearName(String key) {
        editor.putString(KEY_FINANCIALYEAR_NAME,key);
        editor.commit();
    }

    public String getFinancialyearName() {return pref.getString(KEY_FINANCIALYEAR_NAME,null);}


    public void setKeyActionAmount(String key) {
        editor.putString(KEY_ACTION_AMOUNT,key);
        editor.commit();
    }

    public String getKeyActionAmount() {return pref.getString(KEY_ACTION_AMOUNT,null);}


    public void setKeyActionProjectName(String key) {
        editor.putString(KEY_ACTION_PROJECT_NAME,key);
        editor.commit();
    }

    public String getKeyActionProjectName() {return pref.getString(KEY_ACTION_PROJECT_NAME,null);}


    public void setKeyActionStageLevel(String key) {
        editor.putString(KEY_ACTION_STAGE_LEVEL,key);
        editor.commit();
    }

    public String getKeyActionStageLevel() {return pref.getString(KEY_ACTION_STAGE_LEVEL,null);}

    public void setKeyActionWorkid(String key) {
        editor.putString(KEY_ACTION_WORKID,key);
        editor.commit();
    }

    public String getKeyActionWorkid() {return pref.getString(KEY_ACTION_WORKID,null);}

    public void clearSharedPreferences(Context context) {
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor.clear();
        editor.apply();
    }

    public String getApp_Version() {
        return pref.getString(app_Version, null);
    }

    public void setApp_Version(String appVersion) {
        editor.putString(app_Version, appVersion);
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
        //return true;
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

    public void setSchemeSeqIdJson(JSONArray jsonarray) {
        editor.putString(KEY_SCHEME_SEQUENTIAL_ID_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getSchemeSeqIdJsonList() {
        return pref.getString(KEY_SCHEME_SEQUENTIAL_ID_JSON, null);
    }

    public JSONArray getSchemeSeqIdJson() {
        JSONArray jsonData = null;
        String strJson = getSchemeSeqIdJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefSchemeIDJson",""+jsonData);
        return jsonData;
    }

    public void setFinYearJson(JSONArray jsonarray) {
        editor.putString(KEY_FIN_YEAR_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getFinYearJsonList() {
        return pref.getString(KEY_FIN_YEAR_JSON, null);
    }

    public JSONArray getFinYearJson() {
        JSONArray jsonData = null;
        String strJson = getFinYearJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefJson",""+jsonData);
        return jsonData;
    }
}
