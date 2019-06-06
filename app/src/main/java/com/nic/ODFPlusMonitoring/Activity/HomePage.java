package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
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
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener, MyDialog.myOnClickListener {
    private PrefManager prefManager;
    private ImageView logout;
    public dbData dbData = new dbData(this);
    private ScheduleListAdapter scheduleListAdapter;
    private ShimmerRecyclerView recyclerView;
    JSONObject datasetActivity = new JSONObject();
    private MyCustomTextView sync;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        logout = (ImageView) findViewById(R.id.logout);
        sync = (MyCustomTextView) findViewById(R.id.sync);
        recyclerView = (ShimmerRecyclerView) findViewById(R.id.scheduleList);
        logout.setOnClickListener(this);
        sync.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new fetchScheduletask().execute();
            }
        }, 2000);

    }

    public void syncButtonVisibility() {
        dbData.open();
        ArrayList<ODFMonitoringListValue> activityCount = dbData.getSavedActivity();

        if (activityCount.size() > 0) {
            sync.setVisibility(View.VISIBLE);
        }else {
            sync.setVisibility(View.GONE);
        }
    }

    public class fetchScheduletask extends AsyncTask<Void, Void,
            ArrayList<ODFMonitoringListValue>> {
        @Override
        protected ArrayList<ODFMonitoringListValue> doInBackground(Void... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> scheduleList = new ArrayList<>();
            scheduleList = dbData.getAllSchedule(prefManager.getMotivatorId());
            Log.d("SCHEDULE_COUNT", String.valueOf(scheduleList.size()));
            return scheduleList;
        }

        @Override
        protected void onPostExecute(ArrayList<ODFMonitoringListValue> scheduleList) {
            super.onPostExecute(scheduleList);
            scheduleListAdapter = new ScheduleListAdapter(HomePage.this,
                               scheduleList, dbData);
            recyclerView.setAdapter(scheduleListAdapter);
            recyclerView.showShimmerAdapter();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCards();
                }
            }, 2000);
        }
    }
    private void loadCards() {

        recyclerView.hideShimmerAdapter();
        syncButtonVisibility();
    }

    public class toUploadActivityTask extends AsyncTask<Void, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                dbData.open();
                ArrayList<ODFMonitoringListValue> saveActivityLists = dbData.getSavedActivity();
                JSONArray saveAcivityArray = new JSONArray();
                if (saveActivityLists.size() > 0) {
                    for (int i = 0; i < saveActivityLists.size(); i++) {
                        JSONObject activityJson = new JSONObject();
                        activityJson.put(AppConstant.KEY_MOTIVATOR_ID, saveActivityLists.get(i).getMotivatorId());
                        activityJson.put(AppConstant.KEY_SCHEDULE_ID, saveActivityLists.get(i).getScheduleId());
                        activityJson.put(AppConstant.KEY_ACTIVITY_ID, saveActivityLists.get(i).getActivityId());
                        activityJson.put(AppConstant.DISTRICT_CODE, saveActivityLists.get(i).getDistictCode());
                        activityJson.put(AppConstant.BLOCK_CODE, saveActivityLists.get(i).getBlockCode());
                        activityJson.put(AppConstant.PV_CODE, saveActivityLists.get(i).getPvCode());
                        activityJson.put(AppConstant.KEY_LATITUDE, saveActivityLists.get(i).getLatitude());
                        activityJson.put(AppConstant.KEY_LONGITUDE, saveActivityLists.get(i).getLongitude());
                        activityJson.put(AppConstant.KEY_TYPE, saveActivityLists.get(i).getType());
                        activityJson.put(AppConstant.KEY_DATE_TIME, saveActivityLists.get(i).getDateTime());
                        activityJson.put(AppConstant.KEY_IMAGE_REMARK, saveActivityLists.get(i).getImageRemark());

                        Bitmap bitmap = saveActivityLists.get(i).getImage();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        byte[] imageInByte = baos.toByteArray();
                        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                        activityJson.put(AppConstant.KEY_IMAGE,image_str);

                        saveAcivityArray.put(activityJson);
                    }
                }

                datasetActivity = new JSONObject();
                try {
                    datasetActivity.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ACTIVITY_IMAGE_SAVE);
                    datasetActivity.put(AppConstant.KEY_TRACK_DATA, saveAcivityArray);

//                    String authKey = datasetTrack.toString();
//                    int maxLogSize = 2000;
//                    for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
//                        int start = i * maxLogSize;
//                        int end = (i + 1) * maxLogSize;
//                        end = end > authKey.length() ? authKey.length() : end;
//                        Log.v("to_send_plain", authKey.substring(start, end));
//                    }
//
//                    String authKey1 = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), saveLatLongData.toString());
//
//                    for(int i = 0; i <= authKey1.length() / maxLogSize; i++) {
//                        int start = i * maxLogSize;
//                        int end = (i+1) * maxLogSize;
//                        end = end > authKey.length() ? authKey1.length() : end;
//                        Log.v("to_send_encryt", authKey1.substring(start, end));
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return datasetActivity;
        }

        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            syncData();
        }
    }

    public void syncData() {
        try {
            new ApiService(this).makeJSONObjectRequest("saveActivityImage", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), saveActivityistJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject saveActivityistJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), datasetActivity.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saveActivityImage", "" + authKey);
        return dataSet;
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

            case R.id.sync:
                if(Utils.isOnline()){
                    new toUploadActivityTask().execute();
                }
                else {
                    Utils.showAlert(this,"Please Turn on Your Mobile Data to Upload");
                }
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
