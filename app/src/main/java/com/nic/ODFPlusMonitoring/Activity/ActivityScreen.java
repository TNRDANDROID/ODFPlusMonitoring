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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Adapter.ActivityListAdapter;
import com.nic.ODFPlusMonitoring.Adapter.CommonAdapter;
import com.nic.ODFPlusMonitoring.Adapter.ScheduleListAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Dialog.MyDialog;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityScreen extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener, MyDialog.myOnClickListener {
    private PrefManager prefManager;
    private Spinner scheduleVillage_sp;
    public dbData dbData = new dbData(this);
    private ActivityListAdapter activityListAdapter;
    private RecyclerView activityRecycler;;
    private ImageView back_img;
    ArrayList<ODFMonitoringListValue> scheduleVillageList = new ArrayList<>();
    ArrayList<ODFMonitoringListValue> activityList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_village_activity_list);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        scheduleVillage_sp = (Spinner) findViewById(R.id.village_spinner);
        activityRecycler = (RecyclerView) findViewById(R.id.activity_list);
        back_img = (ImageView) findViewById(R.id.back_img);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityRecycler.setLayoutManager(mLayoutManager);
        activityRecycler.setItemAnimator(new DefaultItemAnimator());
        activityRecycler.setHasFixedSize(true);
        activityRecycler.setNestedScrollingEnabled(false);
        activityRecycler.setFocusable(false);

        back_img.setOnClickListener(this);

        scheduleVillage_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
//                    sp_village.setClickable(false);
//                    sp_village.setVisibility(View.GONE);
                } else {
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put(AppConstant.KEY_PMGSY_DCODE,pmgsyVillageList.get(position).getPmgsyDcode());
//                        jsonObject.put(AppConstant.KEY_PMGSY_BCODE,pmgsyVillageList.get(position).getPmgsyBcode());
//                        jsonObject.put(AppConstant.KEY_PMGSY_PVCODE,pmgsyVillageList.get(position).getPmgsyPvcode());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    new fetchActivitytask().execute(String.valueOf(scheduleVillageList.get(position).getScheduleId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        loadVillageSpinner();
    }

    public class fetchActivitytask extends AsyncTask<String, Void,
            ArrayList<ODFMonitoringListValue>> {
        @Override
        protected ArrayList<ODFMonitoringListValue> doInBackground(String... params) {
            dbData.open();
            activityList = new ArrayList<>();
            activityList = dbData.selectScheduleActivity(params[0]);
            return activityList;
        }

        @Override
        protected void onPostExecute(ArrayList<ODFMonitoringListValue> pmgsyHabitationList) {
            super.onPostExecute(pmgsyHabitationList);
            activityListAdapter = new ActivityListAdapter(ActivityScreen.this,
                    activityList, dbData);
           // habitation_tv.setVisibility(View.VISIBLE);
            activityRecycler.setAdapter(activityListAdapter);
        }
    }

    public void loadVillageSpinner() {
        String motivator_id = prefManager.getMotivatorId();
        String schedule_id = getIntent().getStringExtra(AppConstant.KEY_SCHEDULE_ID);
        dbData.open();
        ArrayList<ODFMonitoringListValue> scheduleVillage = dbData.selectScheduledVillage(motivator_id,schedule_id);

        ODFMonitoringListValue roadListValue = new ODFMonitoringListValue();
        roadListValue.setPvName("Select Village");
        scheduleVillageList.add(roadListValue);

        for (int i=0;i<scheduleVillage.size();i++){
            ODFMonitoringListValue villageList = new ODFMonitoringListValue();

            Integer scheduleId = scheduleVillage.get(i).getScheduleId();
            Integer dcode = scheduleVillage.get(i).getDistictCode();
            String bcode = scheduleVillage.get(i).getBlockCode();
            String pvcode = scheduleVillage.get(i).getPvCode();
            String pvname = scheduleVillage.get(i).getPvName();


            villageList.setScheduleId(scheduleId);
            villageList.setDistictCode(dcode);
            villageList.setBlockCode(bcode);
            villageList.setPvCode(pvcode);
            villageList.setPvName(pvname);
            scheduleVillageList.add(villageList);
        }

        scheduleVillage_sp.setAdapter(new CommonAdapter(this, scheduleVillageList, "ScheduleVillage"));
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
//            scheduleListAdapter = new ScheduleListAdapter(ActivityScreen.this,
//                               scheduleList, dbData);
//            recyclerView.setAdapter(scheduleListAdapter);
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
