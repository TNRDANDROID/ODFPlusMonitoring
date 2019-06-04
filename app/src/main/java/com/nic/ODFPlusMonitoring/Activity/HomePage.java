package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Adapter.ScheduleListAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Dialog.MyDialog;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener, MyDialog.myOnClickListener {
    private PrefManager prefManager;
    private ImageView logout;
    public dbData dbData = new dbData(this);
    private ScheduleListAdapter scheduleListAdapter;
    private RecyclerView recyclerView;;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        logout = (ImageView) findViewById(R.id.logout);
        recyclerView = (RecyclerView) findViewById(R.id.scheduleList);
        logout.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        new fetchScheduletask().execute();
    }

    public class fetchScheduletask extends AsyncTask<JSONObject, Void,
            ArrayList<ODFMonitoringListValue>> {
        @Override
        protected ArrayList<ODFMonitoringListValue> doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> scheduleList = new ArrayList<>();
            scheduleList = dbData.getAllSchedule();
            Log.d("SCHEDULE_COUNT", String.valueOf(scheduleList.size()));
            return scheduleList;
        }

        @Override
        protected void onPostExecute(ArrayList<ODFMonitoringListValue> scheduleList) {
            super.onPostExecute(scheduleList);
            scheduleListAdapter = new ScheduleListAdapter(HomePage.this,
                               scheduleList, dbData);
            recyclerView.setAdapter(scheduleListAdapter);
        }
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
//            if ("MotivatorSchedule".equals(urlType) && responseObj != null) {
//                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
////                    new MotivatorCategoryList().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
//                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
//                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
//                }
//                Log.d("MotivatorSchedule", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                closeApplication();
                break;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    private void closeApplication() {
        new MyDialog(this).exitDialog(this, "Are you sure you want to Logout?", "Logout");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new MyDialog(this).exitDialog(this, "Are you sure you want to exit ?", "Exit");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonClick(AlertDialog alertDialog, String type) {
        alertDialog.dismiss();
        if ("Exit".equalsIgnoreCase(type)) {
            onBackPressed();
        } else {

            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", false);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }
}
