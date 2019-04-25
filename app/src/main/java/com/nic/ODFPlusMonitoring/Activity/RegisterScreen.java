package com.nic.ODFPlusMonitoring.Activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.nic.ODFPlusMonitoring.Model.BlockListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyEditTextView;
import com.nic.ODFPlusMonitoring.Support.ProgressHUD;
import com.nic.ODFPlusMonitoring.Utils.FontCache;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AchanthiSundar on 28-12-2018.
 */

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private Button btn_register;

    MyEditTextView motivator_name, motivator_address, motivator_mobileNO, motivator_accountNO;

    private Spinner sp_block,sp_district, sp_village;
    private PrefManager prefManager;
    private List<BlockListValue> Block = new ArrayList<>();
    private List<BlockListValue> District = new ArrayList<>();
    private List<BlockListValue> Village = new ArrayList<>();
    private ProgressHUD progressHUD;
    private ImageView back_img,homeimg;

    String pref_Block,pref_district, pref_Village;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject jsonObject;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.fragment_signup);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        btn_register = (Button) findViewById(R.id.btn_register);
        motivator_name = (MyEditTextView) findViewById(R.id.motivator_name);
        motivator_address = (MyEditTextView) findViewById(R.id.motivator_address);
        motivator_mobileNO = (MyEditTextView) findViewById(R.id.motivator_mobile_no);
        motivator_accountNO = (MyEditTextView) findViewById(R.id.motivator_account_tv);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                validateMotivatorDetails();
                break;
        }
    }




    //The method for opening the registration page and another processes or checks for registering
    private void validateMotivatorDetails() {

    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status = responseObj.getString(AppConstant.KEY_STATUS);
            String response = responseObj.getString(AppConstant.KEY_RESPONSE);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }







    @Override
    public void OnError(VolleyError volleyError) {
        volleyError.printStackTrace();
        Utils.showAlert(this, "Login Again");
    }

}
