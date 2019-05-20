package com.nic.ODFPlusMonitoring.Activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyEditTextView;
import com.nic.ODFPlusMonitoring.Support.ProgressHUD;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by AchanthiSundar on 28-12-2018.
 */

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private Button btn_sign_in,btn_sign_up;
    private String name, pass, randString;

    private MyEditTextView userName;
    private ShowHidePasswordEditText showHidePasswordEditText;
    CheckBox rememberMe;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject jsonObject;

    String sb;
    private PrefManager prefManager;
    private ProgressHUD progressHUD;
    public dbData dbData = new dbData(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.login_screen);
        intializeUI();
        if (Utils.isOnline()) {
            fetchAllResponseFromApi();
            // to avoid insertion of data while back
//            Cursor toCheck = getRawEvents("SELECT * FROM " + DBHelper.BANKLIST_TABLE_NAME, null);
//            toCheck.moveToFirst();
//            if (toCheck.getCount() < 1) {
//                fetchAllResponseFromApi();
//            }
        }

    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        userName = (MyEditTextView) findViewById(R.id.user_name);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        showHidePasswordEditText = (ShowHidePasswordEditText) findViewById(R.id.password);

        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);

        showHidePasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        userName.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.REGULAR));
//        showHidePasswordEditText.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.REGULAR));
//        btn_sign_in.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));
//        btn_sign_up.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));

        showHidePasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    checkLoginScreen();
                }
                return false;
            }
        });
        showHidePasswordEditText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Avenir-Roman.ttf"));
        randString = Utils.randomChar();


    }

    public void fetchAllResponseFromApi(){
        getMotivatorCategoryList();
        getVillageList();
        getDistrictList();
        getBlockList();
        getBankNameList();
        getBankBranchList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                checkLoginScreen();
                break;
            case R.id.btn_sign_up:
                ClickSignUp();
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;
        String username = userName.getText().toString().trim();
        prefManager.setUserName(username);
        String password = showHidePasswordEditText.getText().toString().trim();


        if (username.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the username");
        } else if (password.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the password");
        }
        return valid;
    }

    private void checkLoginScreen() {

        final String username = userName.getText().toString().trim();
        final String password = showHidePasswordEditText.getText().toString().trim();
        prefManager.setUserPassword(password);

        if (Utils.isOnline()) {
            if (!validate())
                return;
            else if (prefManager.getUserName().length() > 0 && password.length() > 0) {
                new ApiService(this).makeRequest("LoginScreen", Api.Method.POST, UrlGenerator.getLoginUrl(), loginParams(), "not cache", this);
            } else {
                Utils.showAlert(this, "Please enter your username and password!");
            }
        } else {
            //Utils.showAlert(this, getResources().getString(R.string.no_internet));
            AlertDialog.Builder ab = new AlertDialog.Builder(
                    LoginScreen.this);
            ab.setMessage("Internet Connection is not avaliable..Please Turn ON Network Connection OR Continue With Off-line Mode..");
            ab.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });
            ab.setNegativeButton("Continue With Off-Line",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            offline_mode(username, password);
                        }
                    });
            ab.show();
        }
    }


    public Map<String, String> loginParams() {
        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID, "login");


        String random = Utils.randomChar();

        params.put(AppConstant.USER_LOGIN_KEY, random);
        Log.d("randchar", "" + random);

        params.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        Log.d("user", "" + userName.getText().toString().trim());

        String encryptUserPass = Utils.md5(showHidePasswordEditText.getText().toString().trim());
        prefManager.setEncryptPass(encryptUserPass);
        Log.d("md5", "" + encryptUserPass);

        String userPass = encryptUserPass.concat(random);
        Log.d("userpass", "" + userPass);
        String sha256 = Utils.getSHA(userPass);
        Log.d("sha", "" + sha256);

        params.put(AppConstant.KEY_USER_PASSWORD, sha256);





        Log.d("user", "" + userName.getText().toString().trim());


        return params;
    }

    //The method for opening the registration page and another processes or checks for registering
    private void ClickSignUp() {
        Intent intent  = new Intent(LoginScreen.this,RegisterScreen.class);
        startActivity(intent);
    }

    public void getDistrictList() {
        try {
            new ApiService(this).makeJSONObjectRequest("DistrictList", Api.Method.POST, UrlGenerator.getOpenUrl(), districtListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBlockList() {
        try {
            new ApiService(this).makeJSONObjectRequest("BlockList", Api.Method.POST, UrlGenerator.getOpenUrl(), blockListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getVillageList() {
        try {
            new ApiService(this).makeJSONObjectRequest("VillageList", Api.Method.POST, UrlGenerator.getOpenUrl(), villageListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBankNameList() {
        try {
            new ApiService(this).makeJSONObjectRequest("BankNameList", Api.Method.POST, UrlGenerator.getOpenUrl(), bankNameListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBankBranchList() {
        try {
            new ApiService(this).makeJSONObjectRequest("BankBranchList", Api.Method.POST, UrlGenerator.getOpenUrl(), bankbranchNameListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getMotivatorCategoryList() {
        try {
            new ApiService(this).makeJSONObjectRequest("MotivatorCategoryList", Api.Method.POST, UrlGenerator.getMotivatorCategory(), motivatorCategoryListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public JSONObject districtListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID,AppConstant.KEY_DISTRICT_LIST_ALL);
        Log.d("object", "" + dataSet);
        return dataSet;
    }

    public JSONObject blockListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID,AppConstant.KEY_BLOCK_LIST_ALL);
        Log.d("object", "" + dataSet);
        return dataSet;
    }

    public JSONObject villageListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID,AppConstant.KEY_VILLAGE_LIST_ALL);
        Log.d("object", "" + dataSet);
        return dataSet;
    }

    public JSONObject bankNameListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID,AppConstant.KEY_BANK_NAME_LIST);
        Log.d("object", "" + dataSet);
        return dataSet;
    }

    public JSONObject bankbranchNameListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID,AppConstant.KEY_BANK_BRANCH_NAME_LIST);
        Log.d("object", "" + dataSet);
        return dataSet;
    }

    public JSONObject motivatorCategoryListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID,AppConstant.KEY_MOTIVATOR_CATEGORY);
        Log.d("object", "" + dataSet);
        return dataSet;
    }
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status = responseObj.getString(AppConstant.KEY_STATUS);
            String response = responseObj.getString(AppConstant.KEY_RESPONSE);
         //   String message = responseObj.getString(AppConstant.KEY_MESSAGE);
            if ("LoginScreen".equals(urlType)) {
                if (status.equalsIgnoreCase("OK")) {
                    if (response.equals("LOGIN_SUCCESS")) {
                        String key = responseObj.getString(AppConstant.KEY_USER);
                        String user_data = responseObj.getString(AppConstant.USER_DATA);
                        String decryptedKey = Utils.decrypt(prefManager.getEncryptPass(), key);
                        String userDataDecrypt = Utils.decrypt(prefManager.getEncryptPass(), user_data);
                        Log.d("userdatadecry", "" + userDataDecrypt);
                        jsonObject = new JSONObject(userDataDecrypt);

                        Log.d("userdata", "" + prefManager.getDistrictCode() + prefManager.getBlockCode() + prefManager.getPvCode() + prefManager.getDistrictName() + prefManager.getBlockName() );
                        prefManager.setUserPassKey(decryptedKey);
                        showHomeScreen();
                    } else {
                        if (response.equals("LOGIN_FAILED")) {
                            Utils.showAlert(this, "Invalid UserName Or Password");
                        }
                    }
                }

            }
            if ("DistrictList".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                   new  InsertDistrictTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("DistrictList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("BlockList".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new  InsertBlockTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("BlockList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("VillageList".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new  InsertVillageTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("VillageList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("BankNameList".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new  InsertBankNameTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("BankNameList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("BankBranchList".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new  InsertBankBranchTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("BankBranchList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("MotivatorCategoryList".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new MotivatorCategoryList().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("MotivatorCategoryList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class InsertDistrictTask extends AsyncTask<JSONArray ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> districtlist_count = dbData.getAll_District();
            if (districtlist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = params[0];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ODFMonitoringListValue districtListValue = new ODFMonitoringListValue();
                        try {
                            districtListValue.setDistictCode(jsonArray.getJSONObject(i).getInt(AppConstant.DISTRICT_CODE));
                            districtListValue.setDistrictName(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_NAME));

                            dbData.insertDistrict(districtListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }

    public class InsertBlockTask extends AsyncTask<JSONArray ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> blocklist_count = dbData.getAll_Block();
            if (blocklist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = params[0];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ODFMonitoringListValue blocktListValue = new ODFMonitoringListValue();
                        try {
                            blocktListValue.setDistictCode(jsonArray.getJSONObject(i).getInt(AppConstant.DISTRICT_CODE));
                            blocktListValue.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                            blocktListValue.setBlockName(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_NAME));

                            dbData.insertBlock(blocktListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }

    public class InsertVillageTask extends AsyncTask<JSONArray ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> villagelist_count = dbData.getAll_Village();
            if (villagelist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = params[0];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ODFMonitoringListValue villageListValue = new ODFMonitoringListValue();
                        try {
                            villageListValue.setDistictCode(jsonArray.getJSONObject(i).getInt(AppConstant.DISTRICT_CODE));
                            villageListValue.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                            villageListValue.setPvCode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                            villageListValue.setPvName(jsonArray.getJSONObject(i).getString(AppConstant.PV_NAME));

                            dbData.insertVillage(villageListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }

    public class InsertBankNameTask extends AsyncTask<JSONArray ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> banknamelist_count = dbData.getAll_BankName();
            if (banknamelist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = params[0];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ODFMonitoringListValue bankListValue = new ODFMonitoringListValue();
                        try {
                            bankListValue.setBank_Id(jsonArray.getJSONObject(i).getInt(AppConstant.BANK_ID));
                            bankListValue.setOMC_Name(jsonArray.getJSONObject(i).getString(AppConstant.OMC_NAME));
                            bankListValue.setBank_Name(jsonArray.getJSONObject(i).getString(AppConstant.BANK_NAME));

                            dbData.insertBankName(bankListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }

    public class InsertBankBranchTask extends AsyncTask<JSONArray ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> branchnamelist_count = dbData.getAll_BranchName();
            if (branchnamelist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = params[0];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ODFMonitoringListValue branchListValue = new ODFMonitoringListValue();
                        try {
                            branchListValue.setBank_Id(jsonArray.getJSONObject(i).getInt(AppConstant.BANK_ID));
                            branchListValue.setBranch_Id(jsonArray.getJSONObject(i).getInt(AppConstant.BRANCH_ID));
                            branchListValue.setBranch_Name(jsonArray.getJSONObject(i).getString(AppConstant.BRANCH_NAME));
                            branchListValue.setIFSC_Code(jsonArray.getJSONObject(i).getString(AppConstant.IFSC_CODE));

                            dbData.insertBranchName(branchListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHUD = ProgressHUD.show(LoginScreen.this, "Downloading", true, false, null);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressHUD!=null){
                progressHUD.cancel();
            }

        }

    }

    public class MotivatorCategoryList extends AsyncTask<JSONArray, Void, Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> categoryListCount = dbData.getAllCategoryList();
            if (categoryListCount.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = params[0];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ODFMonitoringListValue motivatorCategoryListValue = new ODFMonitoringListValue();
                        try {
                            motivatorCategoryListValue.setMotivatorCategoryId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_MOTIVATOR_CATEGORY_ID));
                            motivatorCategoryListValue.setMotivatorCategoryName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTIVATOR_CATEGORY_NAME));

                            dbData.insertCategoryList(motivatorCategoryListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(this, "Login Again");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showHomeScreen();
//    }

    private void showHomeScreen() {
        Intent intent = new Intent(LoginScreen.this,AppVersionActivity.class);

        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void offline_mode(String name, String pass) {
        String userName = prefManager.getUserName();
        String password = prefManager.getUserPassword();
        if (name.equals(userName) && pass.equals(password)) {
            showHomeScreen();
        } else {
            Utils.showAlert(this, "No data available for offline. Please Turn On Your Network");
        }
    }

    public Cursor getRawEvents(String sql, String string) {
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }


}
