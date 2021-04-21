package com.nic.ODFPlusMonitoring.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.NotificationList;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Support.MyEditTextView;
import com.nic.ODFPlusMonitoring.Support.ProgressHUD;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

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

    private Button btn_sign_in;
    private String name, pass, randString;

    private MyEditTextView userName;
    private MyEditTextView passwordEditText;
    private LinearLayout sign_up;
    private int setPType;
    private ImageView redEye;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject jsonObject;
    private MyCustomTextView versionNumber;

    private PrefManager prefManager;
    private ProgressHUD progressHUD;
    public dbData dbData = new dbData(this);
    private String isLogin;
    private static LoginScreen instance;


    public static LoginScreen getInstance() {
        return instance;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.login_screen);
        instance = this;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            isLogin = bundle.getString("Login");
        }
        intializeUI();
        if (Utils.isOnline()) {
            if ((!isLogin.equalsIgnoreCase("Login"))) {
                fetchAllResponseFromApi();
            }
            // to avoid insertion of data while back
//            Cursor toCheck = getRawEvents("SELECT * FROM " + DBHelper.BANKLIST_TABLE_NAME, null);
//            toCheck.moveToFirst();
//            if (toCheck.getCount() < 1) {
//                fetchAllResponseFromApi();
//            }
        }
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        userName = (MyEditTextView) findViewById(R.id.user_name);
        redEye = (ImageView) findViewById(R.id.red_eye);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        sign_up = (LinearLayout) findViewById(R.id.sign_up);
        passwordEditText = (MyEditTextView) findViewById(R.id.passwordEditText);
        versionNumber = (MyCustomTextView) findViewById(R.id.tv_version_number);

        btn_sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);

        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    checkLoginScreen();
                }
                return false;
            }
        });
        passwordEditText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Avenir-Roman.ttf"));
        randString = Utils.randomChar();
        setPType = 1;
        redEye.setOnClickListener(this);

        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            versionNumber.setText("Version" + " " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showPassword() {
        if (setPType == 1) {
            setPType = 0;
            passwordEditText.setTransformationMethod(null);
            if (passwordEditText.getText().length() > 0) {
                passwordEditText.setSelection(passwordEditText.getText().length());
                redEye.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24px);
            }
        } else {
            setPType = 1;
            passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            if (passwordEditText.getText().length() > 0) {
                passwordEditText.setSelection(passwordEditText.getText().length());
                redEye.setBackgroundResource(R.drawable.ic_baseline_visibility_24px);
            }
        }

    }

    public void fetchAllResponseFromApi(){
        if (!prefManager.isLoggedIn() && (!isLogin.equalsIgnoreCase("Login"))) {
            Utils.clearApplicationData(this);
        }
        getMotivatorCategoryList();
        getVillageList();
        getDistrictList();
        getBlockList();
        getBankNameList();
        getBankBranchList();
        getGenderList();
        getDesignationList();
        getEducationalQualificationList();
        //getParticipationList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                checkLoginScreen();
                break;
            case R.id.sign_up:
                ClickSignUp();
                break;
            case R.id.red_eye:
                showPassword();
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
       /* userName.setText("9751995897"); //prod
        passwordEditText.setText("odf65#$");*/

        userName.setText("9843476693"); //loc
        passwordEditText.setText("odf64#$");

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
        Log.d("params", "" +params);


        return params;
    }

    //The method for opening the registration page and another processes or checks for registering
    private void ClickSignUp() {
        Intent intent  = new Intent(LoginScreen.this,RegisterScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
    public void getGenderList() {
        try {
            new ApiService(this).makeJSONObjectRequest("Gender", Api.Method.POST, UrlGenerator.getMotivatorCategory(), genderParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getDesignationList() {
        try {
            new ApiService(this).makeJSONObjectRequest("DesignationList", Api.Method.POST, UrlGenerator.getMotivatorCategory(), DesignationListParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getEducationalQualificationList() {
        try {
            new ApiService(this).makeJSONObjectRequest("EducationalQualification", Api.Method.POST, UrlGenerator.getMotivatorCategory(), EducationalQualificationParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject genderParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "gender");
        return dataSet;
    }



    public JSONObject DesignationListParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "contact_person_type");
        return dataSet;
    }
    public JSONObject EducationalQualificationParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "educational_qualification");
        return dataSet;
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
            String status = null;
            String response = null;
         //   String message = responseObj.getString(AppConstant.KEY_MESSAGE);
           /* if (!"MotivatorSchedule".equals(urlType)){
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
            }*/
            if ("LoginScreen".equals(urlType)) {
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK")) {
                    Log.d("Lresponse", "" + response);
                    if (response.equals("LOGIN_SUCCESS")) {
                        String key = responseObj.getString(AppConstant.KEY_USER);
                        String user_data = responseObj.getString(AppConstant.USER_DATA);
                        String decryptedKey = Utils.decrypt(prefManager.getEncryptPass(), key);
                        String userDataDecrypt = Utils.decrypt(prefManager.getEncryptPass(), user_data);
                        Log.d("userdatadecry", "" + userDataDecrypt);
                        jsonObject = new JSONObject(userDataDecrypt);

                        prefManager.setUserPassKey(decryptedKey);
                        prefManager.setMotivatorId(String.valueOf(jsonObject.get(AppConstant.KEY_MOTIVATOR_ID)));
                        prefManager.setIsLoggedIn(true);
//                        showHomeScreen();

                        String result=jsonObject.getString("odf_test_qualified");
                        if(result.equals("N")){
                            Utils.showAlertResult(LoginScreen.this,"Attend The ODF Plus Qualifying Test");
                        }else if(result.equals("Y")){
                            showHomeScreen();
                        }



                    } else {
                        if (response.equals("LOGIN_FAILED")) {
                            Utils.showAlert(this, "Invalid UserName Or Password");
                        }
                    }
                }

            }
            if ("MotivatorCategoryList".equals(urlType) && responseObj != null) {
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new MotivatorCategoryList().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("MotivatorCategoryList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }
            if ("DistrictList".equals(urlType) && responseObj != null) {
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                   new  InsertDistrictTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("DistrictList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("BlockList".equals(urlType) && responseObj != null) {
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new  InsertBlockTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("BlockList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("VillageList".equals(urlType) && responseObj != null) {
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new  InsertVillageTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("VillageList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("BankNameList".equals(urlType) && responseObj != null) {
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new  InsertBankNameTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("BankNameList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }

            if ("BankBranchList".equals(urlType) && responseObj != null) {
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    new  InsertBankBranchTask().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
                }
                Log.d("BankBranchList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
            }
            if ("Gender".equals(urlType) && responseObj != null) {
               /* String s="{\"STATUS\":\"SUCCESS\",\"JSON_DATA\":[{\"gender_code\":\"F\",\"gender_name_en\":\"Female\",\"gender_name_ta\":\"Female\"},{\"gender_code\":\"M\",\"gender_name_en\":\"Male\",\"gender_name_ta\":\"Male\"},{\"gender_code\":\"T\",\"gender_name_en\":\"Transgender\",\"gender_name_ta\":\"Transgender\"}]}";
                responseObj=new JSONObject();*/
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK")&& response.equalsIgnoreCase("OK")){
                    JSONArray jsonarray = responseObj.getJSONArray(AppConstant.JSON_DATA);
                    prefManager.setGenderList(jsonarray.toString());
                    Log.d("Gender", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
                }

            }
            if ("EducationalQualification".equals(urlType) && responseObj != null) {
                /*String s="{\"STATUS\":\"SUCCESS\",\"JSON_DATA\":[{\"education_code\":\"1\",\"education_name\":\"09th Pass\"},{\"education_code\":\"2\",\"education_name\":\"10th Pass\"},{\"education_code\":\"3\",\"education_name\":\"12th Pass\"},{\"education_code\":\"4\",\"education_name\":\"Degree Holder\"}]}";
                responseObj=new JSONObject();*/
                status  = responseObj.getString(AppConstant.KEY_STATUS);
               response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK")&& response.equalsIgnoreCase("OK")) {
                    JSONArray jsonarray = responseObj.getJSONArray(AppConstant.JSON_DATA);
                    prefManager.setEducationalQualification(jsonarray.toString());
                    Log.d("EducationalQua", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
                }
            }
            if ("DesignationList".equals(urlType) && responseObj != null) {
                /*String s="{\"STATUS\":\"SUCCESS\",\"JSON_DATA\":[{\"designation_code\":\"1\",\"designation_name\":\"Anganwadi Worker\"},{\"designation_code\":\"2\",\"designation_name\":\"VHN\"},{\"designation_code\":\"3\",\"designation_name\":\"School Teacher\"},{\"designation_code\":\"4\",\"designation_name\":\"Village Pt. Sec\"}]}";
                responseObj=new JSONObject();*/
                status  = responseObj.getString(AppConstant.KEY_STATUS);
                response = responseObj.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK")&& response.equalsIgnoreCase("OK")) {
                    JSONArray jsonarray = responseObj.getJSONArray(AppConstant.JSON_DATA);
                    prefManager.setDesignationList(jsonarray.toString());
                    Log.d("DesignationList", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
                }
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
                            districtListValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
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
                            blocktListValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
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
                            villageListValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
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
                            motivatorCategoryListValue.setMotivatorCategoryName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTIVATOR_CATEGORY_NAME_FULL_FORM)+"("+jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTIVATOR_CATEGORY_NAME)+")");


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

    public void showHomeScreen() {
        Intent intent = new Intent(LoginScreen.this,AddParticipantsActivity.class);
        intent.putExtra("Home", "Login");
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
