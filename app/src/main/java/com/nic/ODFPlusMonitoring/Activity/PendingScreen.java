package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Adapter.PendingScreenAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.ProgressHUD;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PendingScreen extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener {

    private RecyclerView pendingRecycler;
    public com.nic.ODFPlusMonitoring.DataBase.dbData dbData = new dbData(this);
    ArrayList<ODFMonitoringListValue> pendingList = new ArrayList<>();
    private PendingScreenAdapter pendingScreenAdapter;
    private ImageView back_img, home_img;
    private RelativeLayout sync_data;
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private ProgressHUD progressHUD;
    Handler myHandler = new Handler();
    public HomePage homePage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_screen);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pendingRecycler = (RecyclerView) findViewById(R.id.pending_list);
        back_img = (ImageView) findViewById(R.id.back_img);
        home_img = (ImageView) findViewById(R.id.home_img);
        sync_data = (RelativeLayout) findViewById(R.id.upload);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        pendingRecycler.setLayoutManager(mLayoutManager);
        pendingRecycler.setItemAnimator(new DefaultItemAnimator());
        pendingRecycler.setHasFixedSize(true);
        pendingRecycler.setNestedScrollingEnabled(false);
        pendingRecycler.setFocusable(false);
        pendingScreenAdapter = new PendingScreenAdapter(this, pendingList, dbData);
        pendingRecycler.setAdapter(pendingScreenAdapter);

        back_img.setOnClickListener(this);
        home_img.setOnClickListener(this);
        sync_data.setOnClickListener(this);
        new fetchpendingtask().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPress();
                break;
            case R.id.home_img:
                dashboard();
                break;
        }
    }


    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void dashboard() {
        Intent intent = new Intent(this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Home", "Home");
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public class fetchpendingtask extends AsyncTask<JSONObject, Void,
            ArrayList<ODFMonitoringListValue>> {
        @Override
        protected ArrayList<ODFMonitoringListValue> doInBackground(JSONObject... params) {
            dbData.open();
            pendingList = new ArrayList<>();
            pendingList = dbData.getSavedActivity();
            return pendingList;
        }

        @Override
        protected void onPostExecute(ArrayList<ODFMonitoringListValue> pendingList) {
            super.onPostExecute(pendingList);
            pendingScreenAdapter = new PendingScreenAdapter(PendingScreen.this,
                    pendingList, dbData);
            pendingRecycler.setAdapter(pendingScreenAdapter);
        }
    }


    public JSONObject syncData(JSONObject dataset) {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());
        JSONObject savedDataSet = new JSONObject();
        try {
            savedDataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            savedDataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveActivityImage", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), savedDataSet, "not cache", this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savedDataSet;
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            Runtime.getRuntime().gc();
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("saveActivityImage".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Activity Image Saved");
                    dbData.open();
                    dbData.deleteSavedActivity();
                    HomePage.getInstance().getMotivatorSchedule();
                }
                Log.d("savedImage", "" + responseDecryptedBlockKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
