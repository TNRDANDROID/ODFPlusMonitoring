package com.nic.ODFPlusMonitoring.Activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.VolleyError;

import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.ProgressHUD;
import com.nic.ODFPlusMonitoring.Utils.FontCache;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by AchanthiSundar on 28-12-2018.
 */

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private Button login_btn,signUp_btn,register_btn;
    private String name, pass, randString;

    EditText userName, password, reg_username, reg_password,
            reg_firstName, reg_lastName, reg_email, reg_confirmemail;

    TextInputLayout txtInLayoutUsername, txtInLayoutPassword, txtInLayoutRegPassword;
    CheckBox rememberMe;
    private ShowHidePasswordEditText passwordEditText;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject jsonObject;

    String sb;
    private PrefManager prefManager;
    private ProgressHUD progressHUD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.login_screen);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        intializeUI();
        getVillageList();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        login_btn = (Button) findViewById(R.id.login);
        signUp_btn = (Button) findViewById(R.id.signUp);
        userName = (EditText) findViewById(R.id.username);
        password = findViewById(R.id.password);

        txtInLayoutUsername = findViewById(R.id.txtInLayoutUsername);
        txtInLayoutPassword = findViewById(R.id.txtInLayoutPassword);
        rememberMe = findViewById(R.id.rememberMe);

//        passwordEditText = (ShowHidePasswordEditText) findViewById(R.id.password);


        login_btn.setOnClickListener(this);
        signUp_btn.setOnClickListener(this);

//        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        inputLayoutEmail.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.REGULAR));
//        inputLayoutPassword.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.REGULAR));
        login_btn.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));
//        inputLayoutEmail.setHintTextAppearance(R.style.InActive);
//        inputLayoutPassword.setHintTextAppearance(R.style.InActive);

//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                    checkLoginScreen();
//                }
//                return false;
//            }
//        });
//        passwordEditText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Avenir-Roman.ttf"));
        randString = Utils.randomChar();

        try {
            db.delete(DBHelper.DISTRICT_TABLE_NAME, null, null);
            db.delete(DBHelper.BLOCK_TABLE_NAME, null, null);
            db.delete(DBHelper.VILLAGE_TABLE_NAME, null, null);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                checkLoginScreen();
                break;
            case R.id.signUp:
                ClickSignUp();
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;
        String username = userName.getText().toString().trim();
        prefManager.setUserName(username);
        String password = passwordEditText.getText().toString().trim();


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
        final String password = passwordEditText.getText().toString().trim();
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

        String encryptUserPass = Utils.md5(passwordEditText.getText().toString().trim());
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

                        Log.d("userdata", "" + prefManager.getDistrictCode() + prefManager.getBlockCode() + prefManager.getPvCode() + prefManager.getDistrictName() + prefManager.getBlockName() + prefManager.getPvName() + prefManager.getLevels());
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
                    loadDistrictList(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("DistrictList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("BlockList".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    loadBlockList(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("BlockList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("VillageList".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    loadVillageList(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("VillageList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadDistrictList(JSONArray jsonArray) {
        progressHUD = ProgressHUD.show(this, "Loading...", true, false, null);
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String districtCode = jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE);
                String districtName = jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_NAME);

                ContentValues districtListValues = new ContentValues();
                districtListValues.put(AppConstant.DISTRICT_CODE, districtCode);
                districtListValues.put(AppConstant.DISTRICT_NAME, districtName);

                LoginScreen.db.insert(DBHelper.DISTRICT_TABLE_NAME, null, districtListValues);
                Log.d("LocalDBdistrictList", "" + districtListValues);

            }
        } catch (JSONException j) {
            j.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException a) {
            a.printStackTrace();
        }
        if (progressHUD != null) {
            progressHUD.cancel();
        }
    }

    private void loadBlockList(JSONArray jsonArray) {
        progressHUD = ProgressHUD.show(this, "Loading...", true, false, null);
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String districtCode = jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE);
                String blockCode = jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE);
                String blockName = jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_NAME);

                ContentValues blockListValues = new ContentValues();
                blockListValues.put(AppConstant.DISTRICT_CODE, districtCode);
                blockListValues.put(AppConstant.BLOCK_CODE, blockCode);
                blockListValues.put(AppConstant.BLOCK_NAME, blockName);

                LoginScreen.db.insert(DBHelper.BLOCK_TABLE_NAME, null, blockListValues);
                Log.d("LocalDBblockList", "" + blockListValues);

            }
        } catch (JSONException j) {
            j.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException a) {
            a.printStackTrace();
        }
        if (progressHUD != null) {
            progressHUD.cancel();
        }
    }

    private void loadVillageList(JSONArray jsonArray) {
        progressHUD = ProgressHUD.show(this, "Loading...", true, false, null);
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String districtCode = jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE);
                String blockCode = jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE);
                String pvcode = jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE);
                String pvname = jsonArray.getJSONObject(i).getString(AppConstant.PV_NAME);

                ContentValues villageListValues = new ContentValues();
                villageListValues.put(AppConstant.DISTRICT_CODE, districtCode);
                villageListValues.put(AppConstant.BLOCK_CODE, blockCode);
                villageListValues.put(AppConstant.PV_CODE, pvcode);
                villageListValues.put(AppConstant.PV_NAME, pvname);


                LoginScreen.db.insert(DBHelper.VILLAGE_TABLE_NAME, null, villageListValues);
                Log.d("LocalDBVilageList", "" + villageListValues);

            }

        } catch (JSONException j) {
            j.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException a) {
            a.printStackTrace();
        }
        if (progressHUD != null) {
            progressHUD.cancel();
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

}
