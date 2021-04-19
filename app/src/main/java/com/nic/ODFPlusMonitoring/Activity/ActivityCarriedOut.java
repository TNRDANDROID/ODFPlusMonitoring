package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Adapter.ActivityCarriedOutAdapter;
import com.nic.ODFPlusMonitoring.Adapter.CommonAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Utils.DateInterface;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActivityCarriedOut extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener, DateInterface {

    private RecyclerView recyclerView;
    private PrefManager prefManager;
    public dbData dbData = new dbData(this);
    private ActivityCarriedOutAdapter activityCarriedOutAdapter;
    private ImageView back_img,home_img,search_img;
    RelativeLayout complete,incomplete,rejected,searchLayout,date_layout;
    ArrayList<ODFMonitoringListValue> finYearList = new ArrayList<>();
    ArrayList<ODFMonitoringListValue> monthList = new ArrayList<>();
    Spinner fin_year,month_sp;
    View v_inCom,v_Com,v_Rej;
    int status=0;
    private AlertDialog alert;
    String selected_fin_year,selected_month;
    Button continue_btn;
    NestedScrollView scroll_view;
    TextView date,not_found_tv;
    LinearLayout tabSelection;
    String fromDate,toDate;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carried_out);
        intializeUI();
    }

    public void intializeUI() {
        context=this;
        prefManager = new PrefManager(this);


        recyclerView = (RecyclerView) findViewById(R.id.activity_history);
        back_img = (ImageView) findViewById(R.id.back_img);
        home_img = (ImageView) findViewById(R.id.home_img);
        search_img = (ImageView) findViewById(R.id.search_img);
        complete = (RelativeLayout) findViewById(R.id.complete);
        incomplete = (RelativeLayout) findViewById(R.id.incomplete);
        rejected = (RelativeLayout) findViewById(R.id.rejected);
        tabSelection = (LinearLayout) findViewById(R.id.tabSelection);
        scroll_view = (NestedScrollView) findViewById(R.id.scroll_view);
        searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        date_layout = (RelativeLayout) findViewById(R.id.date_layout);
        date = (TextView) findViewById(R.id.date);
        not_found_tv = (TextView) findViewById(R.id.not_found_tv);
        continue_btn = (Button) findViewById(R.id.continue_btn);
        v_inCom = (View) findViewById(R.id.v_1);
        v_Com = (View) findViewById(R.id.v_2);
        v_Rej = (View) findViewById(R.id.v_3);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        back_img.setOnClickListener(this);
        home_img.setOnClickListener(this);
        search_img.setOnClickListener(this);
        continue_btn.setOnClickListener(this);
        complete.setOnClickListener(this);
        incomplete.setOnClickListener(this);
        rejected.setOnClickListener(this);
        date_layout.setOnClickListener(this);

//        new fetchActivityHistorytask().execute();
        v_inCom.setVisibility(View.VISIBLE);
        v_Com.setVisibility(View.GONE);

        searchLayout.setVisibility(View.VISIBLE);
        tabSelection.setVisibility(View.GONE);
        scroll_view.setVisibility(View.GONE);

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
            case R.id.search_img:
                getActivityList();
                break;
            case R.id.complete:
                getCompleteActivityList();
                break;
            case R.id.incomplete:
                getInCompleteActivityList();
                break;
            case R.id.rejected:
                getRejectedActivityList();
                break;
            case R.id.continue_btn:
                 getActivityList();
                break;
            case R.id.date_layout:
                Utils.showDatePickerDialog(context);
                break;
        }

    }

    private void getCompleteActivityList() {
        v_inCom.setVisibility(View.GONE);
        v_Rej.setVisibility(View.GONE);
        v_Com.setVisibility(View.VISIBLE);
        scroll_view.setVisibility(View.VISIBLE);
        searchLayout.setVisibility(View.GONE);
        tabSelection.setVisibility(View.VISIBLE);
        status=1;
        new fetchFilteredActivityHistorytask().execute();
    }

    private void getInCompleteActivityList() {
        scroll_view.setVisibility(View.VISIBLE);
        tabSelection.setVisibility(View.VISIBLE);
        searchLayout.setVisibility(View.GONE);
        v_inCom.setVisibility(View.VISIBLE);
        v_Rej.setVisibility(View.GONE);
        v_Com.setVisibility(View.GONE);
        status=2;
        new fetchFilteredActivityHistorytask().execute();
    }
    private void getRejectedActivityList() {
        scroll_view.setVisibility(View.VISIBLE);
        tabSelection.setVisibility(View.VISIBLE);
        searchLayout.setVisibility(View.GONE);
        v_Rej.setVisibility(View.VISIBLE);
        v_inCom.setVisibility(View.GONE);
        v_Com.setVisibility(View.GONE);
        status=3;
        new fetchFilteredActivityHistorytask().execute();
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
    public class fetchActivityHistorytask extends AsyncTask<Void, Void,
                ArrayList<ODFMonitoringListValue>> {
        @Override
        protected ArrayList<ODFMonitoringListValue> doInBackground(Void... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> activityList = new ArrayList<>();
            activityList = dbData.getAllActivity();
            Log.d("ACTIVITY_HISTORY_COUNT", String.valueOf(activityList.size()));
            return activityList;
        }

        @Override
        protected void onPostExecute(ArrayList<ODFMonitoringListValue> scheduleList) {
            super.onPostExecute(scheduleList);
            activityCarriedOutAdapter = new ActivityCarriedOutAdapter(ActivityCarriedOut.this,
                    scheduleList, dbData);
            recyclerView.setAdapter(activityCarriedOutAdapter);


        }

    }
    public class fetchFilteredActivityHistorytask extends AsyncTask<Void, Void,
                ArrayList<ODFMonitoringListValue>> {
        @Override
        protected ArrayList<ODFMonitoringListValue> doInBackground(Void... params) {
            dbData.open();
            ArrayList<ODFMonitoringListValue> activityList = new ArrayList<>();
            if(status == 1){
                activityList = dbData.getFilteredActivity(1);
            }else if(status == 2){
                activityList = dbData.getFilteredActivity(2);
            } else if(status == 3){
                activityList = dbData.getFilteredActivity(3);
            }

            Log.d("ACTIVITY_HISTORY_COUNT", String.valueOf(activityList.size()));
            return activityList;
        }

        @Override
        protected void onPostExecute(ArrayList<ODFMonitoringListValue> scheduleList) {
            super.onPostExecute(scheduleList);
            if(scheduleList.size() >0){
                activityCarriedOutAdapter = new ActivityCarriedOutAdapter(ActivityCarriedOut.this,
                        scheduleList, dbData);
                recyclerView.setAdapter(activityCarriedOutAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                not_found_tv.setVisibility(View.GONE);
            }else {
                recyclerView.setVisibility(View.GONE);
                not_found_tv.setVisibility(View.VISIBLE);
            }

        }

    }
    public void getActivityList(){
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            final LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View view = inflater.inflate(R.layout.pop_up_get_activity, null);
            ImageView close,submit;

            close = (ImageView) view.findViewById(R.id.close);
            submit = (ImageView) view.findViewById(R.id.submit);
            fin_year = (Spinner) view.findViewById(R.id.fin_year);
            month_sp = (Spinner) view.findViewById(R.id.month_sp);
            loadfinYearList();
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( fin_year.getSelectedItem()!= null && !selected_fin_year.equals("Select Year")){
                        if( month_sp.getSelectedItem()!= null && !selected_month.equals("Select Month")){
                            getInCompleteActivityList();
                            alert.dismiss();
                        }else {
                            Utils.showAlert(ActivityCarriedOut.this,"Select Month!");
                        }
                    }else {
                        Utils.showAlert(ActivityCarriedOut.this,"Select Year!");
                    }

                }
            });
            fin_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    String fin_year = parent.getSelectedItem().toString();
                    selected_fin_year = finYearList.get(position).getPvName();
                    System.out.println("fin_year>"+selected_fin_year);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            month_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    String month = parent.getSelectedItem().toString();
                    selected_month = monthList.get(position).getPvName();
                    System.out.println("month>"+selected_month);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(view);
            alert = dialogBuilder.create();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alert.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            lp.windowAnimations = R.style.DialogAnimation;
            alert.getWindow().setAttributes(lp);
            alert.show();
            alert.setCanceledOnTouchOutside(true);
            alert.setCancelable(true);
            alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadfinYearList() {
        ODFMonitoringListValue roadListValue = new ODFMonitoringListValue();
        roadListValue.setPvName("Select Year");
        finYearList.add(roadListValue);

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2010; i <= thisYear; i++) {
            ODFMonitoringListValue villageList = new ODFMonitoringListValue();
            villageList.setPvName(Integer.toString(i));
            finYearList.add(villageList);
        }

        ODFMonitoringListValue mValue = new ODFMonitoringListValue();
        mValue.setPvName("Select Month");
        monthList.add(mValue);
        List<String> tags = Arrays.asList(getResources().getStringArray(R.array.month));
        for (int i = 0; i < tags.size(); i++) {
            ODFMonitoringListValue villageList = new ODFMonitoringListValue();
            villageList.setPvName(tags.get(i));
            monthList.add(villageList);
        }

        fin_year.setAdapter(new CommonAdapter(this, finYearList, "ScheduleVillage"));
        month_sp.setAdapter(new CommonAdapter(this, monthList, "ScheduleVillage"));
    }
    @Override
    public void getDate(String dateValue) {
        String[] separated = dateValue.split(":");
        fromDate = separated[0]; // this will contain "Fruit"
        toDate = separated[1];
        date.setText(fromDate+" to "+toDate);
        getInCompleteActivityList();
        /*if(Utils.isOnline()) {
              getMotivatorScheduleHistory();
        }
        else {
            Utils.showAlert(ActivityCarriedOut.this,"No Internet Connection");
        }*/
    }
    public void getMotivatorScheduleHistory() {
        try {
            new ApiService(this).makeJSONObjectRequest("MotivatorScheduleHistory", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), motivatorScheduleHistoryJsonParams(), "not cache", ActivityCarriedOut.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject motivatorScheduleHistoryJsonParams() throws JSONException {

        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), motivatorScheduleHistoryParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("MotivatorSchHistory", "" + authKey);
        return dataSet;

    }
    public JSONObject motivatorScheduleHistoryParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_MOTIVATOR_SCHEDULE_HISTORY);
        dataSet.put("fromdate", updateLabel(fromDate));
        dataSet.put("todate", updateLabel(toDate));
        Log.d("object", "" + dataSet);
        return dataSet;
    }
    private String updateLabel(String dateStr) {
        String myFormat="";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = format.parse(dateStr);
            System.out.println(date1);
            String dateTime = format.format(date1);
            System.out.println("Current Date Time : " + dateTime);
            myFormat = dateTime; //In which you need put here
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myFormat;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
               if ("MotivatorScheduleHistory".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    HomePage.getInstance().loadScheduleCompleteList(jsonObject.getJSONArray(AppConstant.JSON_DATA));
                    HomePage.getInstance().clearAnimations();
                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD") && jsonObject.getString("MESSAGE").equalsIgnoreCase("NO_RECORD")) {
                    HomePage.getInstance().clearAnimations();
                }
                Log.d("MotivatorSchHistory", "" + responseDecryptedBlockKey);
                String authKey = responseDecryptedBlockKey;
                int maxLogSize = 2000;
                for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > authKey.length() ? authKey.length() : end;
                    Log.v("to_send_plain", authKey.substring(start, end));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }
}
