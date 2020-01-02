package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Adapter.ActivityListAdapter;
import com.nic.ODFPlusMonitoring.Adapter.CommonAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityScreen extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener {
    private PrefManager prefManager;
    private Spinner scheduleVillage_sp;
    public dbData dbData = new dbData(this);
    private ActivityListAdapter activityListAdapter;
    private RecyclerView activityRecycler;;
    private ImageView back_img,home_img;
    ArrayList<ODFMonitoringListValue> scheduleVillageList = new ArrayList<>();
    ArrayList<ODFMonitoringListValue> activityList = new ArrayList<>();
    private MyCustomTextView activity_tv,not_found_tv;
    private Animation animation;
    private ImageView arrowImage;
    private static ActivityScreen activityScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_village_activity_list);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        activityScreen = this;
        scheduleVillage_sp = (Spinner) findViewById(R.id.village_spinner);
        arrowImage = (ImageView) findViewById(R.id.arrow_image_up);
        activityRecycler = (RecyclerView) findViewById(R.id.activity_list);
        activity_tv = (MyCustomTextView) findViewById(R.id.activity_tv);
        not_found_tv = (MyCustomTextView) findViewById(R.id.not_found_tv);
        back_img = (ImageView) findViewById(R.id.back_img);
        home_img = (ImageView) findViewById(R.id.home_img);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityRecycler.setLayoutManager(mLayoutManager);
        activityRecycler.setItemAnimator(new DefaultItemAnimator());
        activityRecycler.setHasFixedSize(true);
        activityRecycler.setNestedScrollingEnabled(false);
        activityRecycler.setFocusable(false);
        activityListAdapter = new ActivityListAdapter(this, activityList, dbData);
        activityRecycler.setAdapter(activityListAdapter);
        back_img.setOnClickListener(this);
        home_img.setOnClickListener(this);

        scheduleVillage_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    activityList.clear();
                    activityListAdapter.notifyDataSetChanged();
                    activity_tv.setVisibility(View.GONE);
                } else {
                    prefManager.setDistrictCode(scheduleVillageList.get(position).getDistictCode());
                    prefManager.setBlockCode(scheduleVillageList.get(position).getBlockCode());
                    prefManager.setPvCode(scheduleVillageList.get(position).getPvCode());
                    activity_tv.setVisibility(View.VISIBLE);
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
            activityList = dbData.selectScheduleActivity(params[0],prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode());

            return activityList;
        }

        @Override
        protected void onPostExecute(ArrayList<ODFMonitoringListValue> pmgsyHabitationList) {
            super.onPostExecute(pmgsyHabitationList);
            activityListAdapter = new ActivityListAdapter(ActivityScreen.this,
                    activityList, dbData);
           // habitation_tv.setVisibility(View.VISIBLE);
            activityRecycler.setAdapter(activityListAdapter);
            if(activityList.size() > 0 ){
                not_found_tv.setVisibility(View.GONE);
                activity_tv.setVisibility(View.VISIBLE);
                showArrowImage();
            }
            else {
                not_found_tv.setVisibility(View.VISIBLE);
                activity_tv.setVisibility(View.GONE);
                activityListAdapter.notifyDataSetChanged();
            }

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
            String dcode = scheduleVillage.get(i).getDistictCode();
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

    public JSONObject motivatorScheduleFeedback(JSONObject dataset) {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());
        JSONObject savedDataSet = new JSONObject();
        try {
            savedDataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            savedDataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("ScheduleWiseFeedback", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), savedDataSet, "not cache", this);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savedDataSet;
    }
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            if ("ScheduleWiseFeedback".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Thanks For Your Valuable Feedback!");
                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    Utils.showAlert(this, "No Feedback Inserted!");

                }
            }
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
            case R.id.back_img:
                onBackPress();
                break;
            case R.id.home_img:
                homePage();
                break;
        }


    }

    public void homePage() {
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

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public static ActivityScreen getInstance() {
        return activityScreen;
    }
    @Override
    protected void onResume() {
        super.onResume();
        activityListAdapter.notifyDataSetChanged();
    }

    public void showArrowImage() {
        arrowImage.setVisibility(View.VISIBLE);
        animation = new AlphaAnimation((float) 3, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
        arrowImage.startAnimation(animation);
        arrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showNameChangeDialog(ActivityScreen.this, "ScheduleWiseFeedback", "", prefManager.getDistrictCode(), prefManager.getBlockCode(), prefManager.getPvCode(), getIntent().getStringExtra(AppConstant.KEY_SCHEDULE_ID));
            }
        });
    }
}
