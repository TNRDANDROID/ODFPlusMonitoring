package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.nic.ODFPlusMonitoring.Adapter.CommonAdapter;
import com.nic.ODFPlusMonitoring.Adapter.NotificationAdapter;
import com.nic.ODFPlusMonitoring.Adapter.ScheduleListAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Dialog.MyDialog;
import com.nic.ODFPlusMonitoring.Model.NotificationList;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Support.ProgressHUD;
import com.nic.ODFPlusMonitoring.Utils.DateInterface;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomePage extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener, MyDialog.myOnClickListener, DateInterface {
    private PrefManager prefManager;
    private ImageView logout, refresh_icon, arrowImage, pro_img;
    private MyCustomTextView pro_tv, feedback_tv;
    public dbData dbData = new dbData(this);
    private RelativeLayout pro, feed,notification_layout,notify_history,add_participants;
    Handler myHandler = new Handler();
    private ScheduleListAdapter scheduleListAdapter;
    private ShimmerRecyclerView recyclerView;
    JSONObject datasetActivity = new JSONObject();
    private Button sync;
    private String isHome;
    public static HomePage homePage;
    private ProgressHUD progressHUD;
    private Animation animation,stb2;
    private LinearLayout activity_carried_out;
    private boolean recordContain = true;
    private AlertDialog alert;
    ArrayList<ODFMonitoringListValue> finYearList = new ArrayList<>();
    ArrayList<ODFMonitoringListValue> monthList = new ArrayList<>();
    Spinner fin_year,month_sp;
    String selected_fin_year,selected_month;

    BottomSheetBehavior sheetBehavior;
    RecyclerView recycler_view_notifications;
    TextView no_records;
    ArrayList<com.nic.ODFPlusMonitoring.Model.NotificationList> NotificationList;
    View bottomSheet;
    TextView close,notificationCount;
    int notification_read_id;
    String fromDate,toDate;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.home);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            isHome = bundle.getString("Home");
        }
        context=this;
        intializeUI();

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        mTextViewState.setText("Collapsed");
                        sheetBehavior.setPeekHeight(0);
                        getNotificationList();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        mTextViewState.setText("Dragging...");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
//                        mTextViewState.setText("Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
//                        mTextViewState.setText("Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
//                        mTextViewState.setText("Settling...");
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        stb2 = AnimationUtils.loadAnimation(this, R.anim.stb2);
        homePage = this;
        logout = (ImageView) findViewById(R.id.logout);
        pro_img = (ImageView) findViewById(R.id.pro_img);
        close = (TextView) findViewById(R.id.close);
        notificationCount = (TextView) findViewById(R.id.notificationCount);
        pro_tv = (MyCustomTextView) findViewById(R.id.pro_tv);
        feedback_tv = (MyCustomTextView) findViewById(R.id.feedback_tv);
        arrowImage = (ImageView) findViewById(R.id.arrow_image_up);
        refresh_icon = (ImageView) findViewById(R.id.refresh_icon);
        pro = (RelativeLayout) findViewById(R.id.pro);
        feed = (RelativeLayout) findViewById(R.id.feed);
        notification_layout = (RelativeLayout) findViewById(R.id.notify);
        notify_history = (RelativeLayout) findViewById(R.id.notify_history);
        add_participants = (RelativeLayout) findViewById(R.id.add_participants);
        sync = (Button) findViewById(R.id.sync);
        recyclerView = (ShimmerRecyclerView) findViewById(R.id.scheduleList);
        activity_carried_out = (LinearLayout)findViewById(R.id.activity_carried_out);

        recycler_view_notifications=(RecyclerView)findViewById(R.id.recycler_view_notifications);
        no_records=(TextView)findViewById(R.id.no_records);
        bottomSheet=(NestedScrollView) findViewById(R.id.bottomSheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setPeekHeight(0);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_notifications.setLayoutManager(mLayoutManager);

        bottomSheet.setVisibility(View.VISIBLE);
        notification_layout.setVisibility(View.GONE);

        logout.setOnClickListener(this);
        sync.setOnClickListener(this);
        refresh_icon.setOnClickListener(this);
        pro.setOnClickListener(this);
        activity_carried_out.setOnClickListener(this);
        feed.setOnClickListener(this);
        notification_layout.setOnClickListener(this);
        notify_history.setOnClickListener(this);
        add_participants.setOnClickListener(this);
        close.setOnClickListener(this);
        if(Utils.isOnline()){
            if(!isHome.equalsIgnoreCase("Home")){
                refreshScreenCallApi();
            }
        }else{
            Utils.showAlert(this,getResources().getString(R.string.no_internet));
        }

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager1);
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
        ArrayList<ODFMonitoringListValue> activityCount = dbData.getSavedActivity("count",new ODFMonitoringListValue());

        if (activityCount.size() > 0) {
            sync.setVisibility(View.VISIBLE);
        }else {
            sync.setVisibility(View.GONE);
        }
    }

    public void startAnimation() {

    }

    @Override
    public void getDate(String dateValue) {
        String[] separated = dateValue.split(":");
        fromDate = separated[0]; // this will contain "Fruit"
        toDate = separated[1];
        if(Utils.isOnline()) {
            getNotificationHistoryList();
        }
        else {
            Utils.showAlert(HomePage.this,"No Internet Connection");
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
        showArrowImage();
        startAnimation();
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            if ("MotivatorSchedule".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    loadMotivatorScheduleList(jsonObject.getJSONArray(AppConstant.JSON_DATA));
                    clearAnimations();
                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD") && jsonObject.getString("MESSAGE").equalsIgnoreCase("NO_RECORD")) {
                    Utils.showAlert(this, "No Record Found!");
                    clearAnimations();
                }
                Log.d("MotivatorSchedule", "" + responseDecryptedBlockKey);
                String authKey = responseDecryptedBlockKey;
                int maxLogSize = 2000;
                for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > authKey.length() ? authKey.length() : end;
                    Log.v("to_send_plain", authKey.substring(start, end));
                }
            }
            else if ("GeneralFeedback".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Thanks For Your Valuable Feedback!");
                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    Utils.showAlert(this, "No Feedback Inserted!");

                }
            }
            else if ("MotivatorScheduleHistory".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    loadScheduleCompleteList(jsonObject.getJSONArray(AppConstant.JSON_DATA));
                    clearAnimations();
                    recordContain = true;
                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD") && jsonObject.getString("MESSAGE").equalsIgnoreCase("NO_RECORD")) {
                    recordContain = false;
//                    Utils.showAlert(this, "No Record Found!");
                    clearAnimations();
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
            else if ("NotificationList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("NotificationList>>", jsonObject.toString());
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    try {
                    if(jsonObject.getJSONArray(AppConstant.JSON_DATA) != null && jsonObject.getJSONArray(AppConstant.JSON_DATA).length()>0) {
                        if(jsonObject.getJSONArray(AppConstant.JSON_DATA) != null && jsonObject.getJSONArray(AppConstant.JSON_DATA).length()>0){
                            JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.JSON_DATA);
                        LoadNotificationDetails(jsonarray, "NotificationList");
                        Log.d("NotificationList", jsonObject.getJSONArray(AppConstant.JSON_DATA).toString());
                        notification_layout.setVisibility(View.VISIBLE);
                        animation = AnimationUtils.loadAnimation(context, R.anim.blink);
                        notification_layout.startAnimation(animation);
                    } }else {
                        recycler_view_notifications.setAdapter(null);
                        no_records.setVisibility(View.VISIBLE);
                        notification_layout.setVisibility(View.GONE);
                        notificationCount.setText("0");
                    }
                    }catch (Exception e){
                        recycler_view_notifications.setAdapter(null);
                        no_records.setVisibility(View.VISIBLE);
                        notification_layout.setVisibility(View.GONE);
                        notificationCount.setText("0");
                        e.printStackTrace();

                    }
                }
            } else if ("NotificationHistoryList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    try {

                        if(jsonObject.getJSONArray(AppConstant.JSON_DATA) != null && jsonObject.getJSONArray(AppConstant.JSON_DATA).length()>0){
                            JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.JSON_DATA);
                            LoadNotificationDetails(jsonarray,"NotificationHistoryList");
                            Log.d("NotificationHistoryList",jsonObject.getJSONArray(AppConstant.JSON_DATA).toString());
                        }else {
                            Utils.showAlert(HomePage.this,"No Record Found!");
                            recycler_view_notifications.setAdapter(null);
                            no_records.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        Utils.showAlert(HomePage.this,"No Record Found!");
                        recycler_view_notifications.setAdapter(null);
                        no_records.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }

                }
            }else if ("NotificationRead".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    //Utils.showAlert(this, "Thanks For Your Valuable Feedback!");
                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    //Utils.showAlert(this, "No Feedback Inserted!");

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadScheduleCompleteList(JSONArray jsonArray) {

        dbData.open();
        if (Utils.isOnline()) {
            dbData.deleteActivityCompletedTable();
        }
        dbData.open();
        ArrayList<ODFMonitoringListValue> schedule_count = dbData.getAllSchedule(prefManager.getMotivatorId());
      //  if(schedule_count.size() <= 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                ODFMonitoringListValue odfMonitoringListValue = new ODFMonitoringListValue();
                try {
                    String scheduleId = jsonArray.getJSONObject(i).getString(AppConstant.KEY_SCHEDULE_ID);

                    JSONArray activityArray = jsonArray.getJSONObject(i).getJSONArray(AppConstant.KEY_T_SCHEDULE_ACTIVITY);
                    Log.d("MotSchHistoryList>>", "" + activityArray);
                    new InsertActivityCompletedTask().execute(activityArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
      //  }

    }

    private void LoadNotificationDetails(JSONArray jsonarray,String type) {
        NotificationList = new ArrayList<NotificationList>();
        try {
            if(jsonarray != null && jsonarray.length() >0) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    NotificationList Detail = new NotificationList();
                    Detail.setNote_entry_id(jsonobject.getString("note_entry_id"));
                    Detail.setNotification(jsonobject.getString("notification"));
                    Detail.setNotification_date(jsonobject.getString("note_date"));
                    NotificationList.add(Detail);
                }
//                SortAndReverseList(NotificationList);
            }
            if(NotificationList != null && NotificationList.size() >0) {
                NotificationAdapter adapter = new NotificationAdapter(this,NotificationList,type);
                adapter.notifyDataSetChanged();
                recycler_view_notifications.setAdapter(adapter);
                recycler_view_notifications.setVisibility(View.VISIBLE);
                no_records.setVisibility(View.GONE);
                if(type.equalsIgnoreCase("NotificationList")){
                    notificationCount.setText(String.valueOf(NotificationList.size()));
                }else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }else {
                recycler_view_notifications.setVisibility(View.GONE);
                no_records.setVisibility(View.VISIBLE);
                if(type.equalsIgnoreCase("NotificationList")){
                    notificationCount.setText("0");
                }else { }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SortAndReverseList( ArrayList<NotificationList> historyList) {
        Collections.sort(historyList, new Comparator<NotificationList>() {
            @Override
            public int compare(NotificationList o1, NotificationList o2) {
                String date1=o1.getNotification_date();
                String date2=o2.getNotification_date();
                int compareResult = 0;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    Date arg0Date = format.parse(date1);
                    Date arg1Date = format.parse(date2);
                    compareResult = arg0Date.compareTo(arg1Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    compareResult = date2.compareTo(date1);
                }
                return compareResult;
            }

        });
        // Collections.reverse(studentActivityDetails);
    }



    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:

                dbData.open();
                ArrayList<ODFMonitoringListValue> activityCount = dbData.getSavedActivity("count",new ODFMonitoringListValue());
                if(!Utils.isOnline()) {
                    Utils.showAlert(this,"Logging out while offline may leads to loss of data!");
                }else {
                    if (!(activityCount.size() > 0 )) {
                        closeApplication();
                    }else{
                        Utils.showAlert(this,"Sync all the data before logout!");
                    }
                }

                break;

            case R.id.sync:
//                if(Utils.isOnline()){
//                    new toUploadActivityTask().execute();
//                }
//                else {
//                    Utils.showAlert(this,"Please Turn on Your Mobile Data to Upload");
//                }
                openPendingScreen();
                break;
            case R.id.refresh_icon:
                if (Utils.isOnline()) {
                    setAnimationView();
                    refreshScreenCallApi();
                } else {
                    Utils.showAlert(this, getResources().getString(R.string.no_internet));
                }
                break;
            case R.id.pro:
                if (Utils.isOnline()) {
                    openMotivatorProfileView();
                } else {
                    Utils.showAlert(this, "Your internet seems to be offline! profile can be seen only in online mode");
                }
                break;
            case R.id.activity_carried_out:
                openActivityCarriedOut();
               /* if(recordContain) {
                    openActivityCarriedOut();
//                    getActivityList();
                }else{
                    Utils.showAlert(this, "No Record Found!");
                }*/
                break;
            case R.id.notify:
                if(!notificationCount.getText().toString().equalsIgnoreCase("0")){
                    getNotificationList();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    Utils.showAlert(HomePage.this,"No Record Found!");
                }

//                sheetBehavior.setPeekHeight(180);
                break;
            case R.id.close:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.notify_history:
                Utils.showDatePickerDialog(context);
                break;
            case R.id.add_participants:
                Intent intent = new Intent(HomePage.this,AddParticipantsActivity.class);
                intent.putExtra("Home", "Home");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;
        }


    }

    public void openMotivatorProfileView() {
        Intent intent = new Intent(this, ViewEditProfileScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void openActivityCarriedOut() {
        Intent intent = new Intent(this, ActivityCarriedOut.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onBackPressed() {
        if(sheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
                new MyDialog(this).exitDialog(this, "Are you sure you want to exit ?", "Exit");
            /*super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);*/
    }}

    public void refreshScreenCallApi() {
//        setAnimationView();
        fetchApi();
    }

    public void fetchApi() {
        getMotivatorSchedule();
//        getMotivatorScheduleHistory();
        getNotificationList();
    }

    public void openPendingScreen() {
        Intent intent = new Intent(this, PendingScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void closeApplication() {
        new MyDialog(this).exitDialog(this, "Are you sure you want to Logout?", "Logout");
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(sheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }else {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    new MyDialog(this).exitDialog(this, "Are you sure you want to exit ?", "Exit");
                    return false;
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }
*/

    @Override
    public void onButtonClick(AlertDialog alertDialog, String type) {
        alertDialog.dismiss();
        if ("Exit".equalsIgnoreCase(type)) {
//            onBackPressed();
            super.onBackPressed();
            setResult(Activity.RESULT_CANCELED);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else {

            Intent intent = new Intent(this, LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", false);
            intent.putExtra("Login", "");
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }
    public void getMotivatorSchedule() {
        try {
            new ApiService(this).makeJSONObjectRequest("MotivatorSchedule", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), motivatorScheduleListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getNotificationList() {
        try {
            new ApiService(this).makeJSONObjectRequest("NotificationList", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), encryptedNotificationParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getNotificationHistoryList() {
        try {
            new ApiService(this).makeJSONObjectRequest("NotificationHistoryList", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), encryptedNotificationHistoryListParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JSONObject encryptedNotificationParams() throws JSONException {

        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), notificationParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("NewNotification", "" + dataSet);
        return dataSet;

    }
    public JSONObject encryptedNotificationHistoryListParams() throws JSONException {

        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), NotificationHistoryListParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("NotificationHistoryList", "" + dataSet);
        return dataSet;

    }

    public JSONObject NotificationHistoryListParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "getNotificationsOld");
        dataSet.put("app_id", "10");
        dataSet.put("from_date", updateLabel(fromDate));
        dataSet.put("to_date", updateLabel(toDate));
        return dataSet;
    }
    public JSONObject notificationParams () throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "getNotificationsNew");
        dataSet.put("app_id", "10");
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

    public JSONObject motivatorScheduleListJsonParams() throws JSONException {

        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.motivatorScheduleListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("scheduleListWise", "" + dataSet);
        return dataSet;

    }

    public void getMotivatorScheduleHistory() {
        try {
            new ApiService(this).makeJSONObjectRequest("MotivatorScheduleHistory", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), motivatorScheduleHistoryJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject motivatorScheduleHistoryJsonParams() throws JSONException {

        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.motivatorScheduleHistoryJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("MotivatorSchHistory", "" + dataSet);
        return dataSet;

    }

    public JSONObject motivatorFeedback(JSONObject dataset) {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());
        JSONObject savedDataSet = new JSONObject();
        try {
            savedDataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            savedDataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("GeneralFeedback", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), savedDataSet, "not cache", this);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savedDataSet;
    }



    public void loadMotivatorScheduleList(JSONArray jsonArray) {

        dbData.open();
        if (Utils.isOnline()) {
            dbData.deleteScheduleTable();
            dbData.deleteScheduleVillageTable();
            dbData.deleteScheduleActivityTable();
            dbData.deleteActivityPhotos();
        }
        dbData.open();
        ArrayList<ODFMonitoringListValue> schedule_count = dbData.getAllSchedule(prefManager.getMotivatorId());
        if(schedule_count.size() <= 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                ODFMonitoringListValue odfMonitoringListValue = new ODFMonitoringListValue();
                try {
                    String scheduleId = jsonArray.getJSONObject(i).getString(AppConstant.KEY_SCHEDULE_ID);
                    JSONObject tSchedulejsonObject = jsonArray.getJSONObject(i).getJSONObject(AppConstant.KEY_T_SCHEDULE);

                    odfMonitoringListValue.setScheduleId(tSchedulejsonObject.getInt(AppConstant.KEY_SCHEDULE_ID));
                    odfMonitoringListValue.setScheduleMasterId(tSchedulejsonObject.getInt(AppConstant.KEY_SCHEDULE_MASTER_ID));
                    odfMonitoringListValue.setMotivatorId(tSchedulejsonObject.getInt(AppConstant.KEY_MOTIVATOR_ID));
                    odfMonitoringListValue.setScheduleFromDate(tSchedulejsonObject.getString(AppConstant.KEY_FROM_DATE));
                    odfMonitoringListValue.setScheduletoDate(tSchedulejsonObject.getString(AppConstant.KEY_TO_DATE));
                    odfMonitoringListValue.setScheduleDescription(tSchedulejsonObject.getString(AppConstant.KEY_SCHEDULE_DESCRIPTION));
                    odfMonitoringListValue.setTotalActivity(tSchedulejsonObject.getInt(AppConstant.KEY_TOTAL_ACTIVITY));
                    odfMonitoringListValue.setPendingActivity(tSchedulejsonObject.getInt(AppConstant.KEY_PENDING_ACTIVITY));
                    odfMonitoringListValue.setCompletedActivity(tSchedulejsonObject.getInt(AppConstant.KEY_COMPLETED_ACTIVITY));
                    dbData.insertSchedule(odfMonitoringListValue);

                    JSONArray villageArray = jsonArray.getJSONObject(i).getJSONArray(AppConstant.KEY_T_SCHEDULE_VILLAGE);
                    new InsertScheduleVillageTask().execute(villageArray);

                    JSONArray activityArray = jsonArray.getJSONObject(i).getJSONArray(AppConstant.KEY_T_SCHEDULE_ACTIVITY);
                    new InsertScheduleActivityTask().execute(activityArray);

                    JSONArray photosArray = jsonArray.getJSONObject(i).getJSONArray(AppConstant.KEY_T_SCHEDULE_ACTIVITY_PHOTOS);

                    new InsertActivityPhotosTask().execute(photosArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            new fetchScheduletask().execute();
        }

    }

    public class  InsertScheduleVillageTask extends AsyncTask<JSONArray ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            if (params.length > 0) {
                JSONArray jsonArray = params[0];
                for (int i = 0; i < jsonArray.length(); i++) {
                    ODFMonitoringListValue schedulevillageValue = new ODFMonitoringListValue();
                    try {
                        schedulevillageValue.setVillageLinkId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_SCHEDULE_VILLAGE_LINK_ID));
                        schedulevillageValue.setScheduleId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_SCHEDULE_ID));
                        schedulevillageValue.setMotivatorId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_MOTIVATOR_ID));
                        schedulevillageValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                        schedulevillageValue.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                        schedulevillageValue.setPvCode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                        schedulevillageValue.setPvName(jsonArray.getJSONObject(i).getString(AppConstant.PV_NAME));
                        dbData.insertScheduleVillage(schedulevillageValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            return null;
        }
    }

    public class InsertScheduleActivityTask extends AsyncTask<JSONArray ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            if (params.length > 0) {
                JSONArray jsonArray = params[0];
                for (int i = 0; i < jsonArray.length(); i++) {
                    ODFMonitoringListValue scheduleActivityValue = new ODFMonitoringListValue();
                    try {
                        scheduleActivityValue.setScheduleActivityId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_SCHEDULE_ACTIVITY_ID));
                        scheduleActivityValue.setScheduleId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_SCHEDULE_ID));
                        scheduleActivityValue.setActivityId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_ACTIVITY_ID));
                        scheduleActivityValue.setActivityName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_NAME));
                        scheduleActivityValue.setActivityTypeName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_TYPE_NAME));
                        scheduleActivityValue.setPlaceOfActivity(jsonArray.getJSONObject(i).getString(AppConstant.KEY_PLACE_OF_ACTIVITY));
                        scheduleActivityValue.setNoOfPhotos(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_NO_OF_PHOTOS));
                        scheduleActivityValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                        scheduleActivityValue.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                        scheduleActivityValue.setPvCode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                        scheduleActivityValue.setActivityStatus(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_STATUS));
                        scheduleActivityValue.setActivity_duration(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_DURATION));
                        scheduleActivityValue.setActivity_amount(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_AMOUNT));
                        scheduleActivityValue.setActivity_desc_doc_available(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_DOC_AVAILABLE));
                        scheduleActivityValue.setActivity_desc_audio_available(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_AUDIO_AVAILABLE));

                        dbData.insertScheduleActivity(scheduleActivityValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }
    }

    public class InsertActivityPhotosTask extends AsyncTask<JSONArray ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            if (params.length > 0) {
                JSONArray jsonArray = params[0];
                for (int i = 0; i < jsonArray.length(); i++) {
                    ODFMonitoringListValue activityPhotosValue = new ODFMonitoringListValue();
                    try {
                        activityPhotosValue.setScheduleActivityId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_SCHEDULE_ACTIVITY_ID));
                        activityPhotosValue.setScheduleId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_SCHEDULE_ID));
                        activityPhotosValue.setLatitude(jsonArray.getJSONObject(i).getString("location_lat"));
                        activityPhotosValue.setLongitude(jsonArray.getJSONObject(i).getString("location_long"));
                        activityPhotosValue.setType(jsonArray.getJSONObject(i).getString("activity_type"));
                        activityPhotosValue.setImageRemark(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE_REMARK));
                        activityPhotosValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                        activityPhotosValue.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                        activityPhotosValue.setPvCode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                        activityPhotosValue.setImageAvailable(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE_AVAILABLE));


                        dbData.insertActivityPhotos(activityPhotosValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//                progressHUD.cancel();

            clearAnimations();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setAnimationView();
//            progressHUD = ProgressHUD.show(HomePage.this, "Downloading", true, false, null);
        }
    }

    public class InsertActivityCompletedTask extends AsyncTask<JSONArray ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            dbData.open();
            if (params.length > 0) {
                JSONArray jsonArray = params[0];
                for (int i = 0; i < jsonArray.length(); i++) {
                    ODFMonitoringListValue scheduleActivityValue = new ODFMonitoringListValue();
                    try {
                        scheduleActivityValue.setScheduleActivityId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_SCHEDULE_ACTIVITY_ID));
                        scheduleActivityValue.setScheduleId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_SCHEDULE_ID));
                        scheduleActivityValue.setActivityId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_ACTIVITY_ID));
                        scheduleActivityValue.setActivityName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_NAME));
                        scheduleActivityValue.setActivityStart(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_START));
                        scheduleActivityValue.setActivityEnd(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_END));
                        scheduleActivityValue.setActivityTypeName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_TYPE_NAME));
                        scheduleActivityValue.setPlaceOfActivity(jsonArray.getJSONObject(i).getString(AppConstant.KEY_PLACE_OF_ACTIVITY));
                        scheduleActivityValue.setDistictCode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                        scheduleActivityValue.setBlockCode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                        scheduleActivityValue.setPvCode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                        scheduleActivityValue.setActivityStatus(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_STATUS));
                        scheduleActivityValue.setRejected_status(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_REJECTED_STATUS));

                        dbData.insertActivityCompleted(scheduleActivityValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                ActivityCarriedOut.getInstance().getInCompleteActivityList();
            }
            return null;
        }
    }

    public void setAnimationView() {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        refresh_icon.startAnimation(rotation);
    }

    public void clearAnimations() {
        refresh_icon.clearAnimation();
    }
    public static HomePage getInstance() {
        return homePage;
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncButtonVisibility();
    }

    public void showArrowImage() {
        pro.setVisibility(View.VISIBLE);
        feed.setVisibility(View.VISIBLE);
        animation = new AlphaAnimation((float) 3, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the

       /* pro_img.startAnimation(stb2);
        pro_tv.setTranslationX(800);
        pro_tv.setAlpha(0);
        pro_tv.animate().translationX(0).alpha(1).setDuration(1000).start();

        feedback_tv.setTranslationX(800);
        feedback_tv.setAlpha(0);
        feedback_tv.animate().translationX(0).alpha(1).setDuration(1000).start();
        arrowImage.startAnimation(animation);
        feedback_tv.startAnimation(animation);*/


        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isOnline()) {
                    Utils.showNameChangeDialog(HomePage.this, "General", "", "", "", "", "");
                }else{
                    Utils.showAlert(HomePage.this, "Your internet seems to be offline! feedback can be post only in online mode");

                }
            }
        });
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
                            openActivityCarriedOut();
                            alert.dismiss();
                        }else {
                            Utils.showAlert(HomePage.this,"Select Month!");
                        }
                    }else {
                        Utils.showAlert(HomePage.this,"Select Year!");
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

    public void sendNotificationReadStatus() {
        try {
            new ApiService(this).makeJSONObjectRequest("NotificationRead", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), sendNotificationReadStatusJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject sendNotificationReadStatusJsonParams() throws JSONException {

        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), sendNotificatinReadStatusNormaJson().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("setNotificationRead", "" + dataSet);
        return dataSet;

    }
    public JSONObject sendNotificatinReadStatusNormaJson(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(AppConstant.KEY_SERVICE_ID,"setNotificationRead");
            jsonObject.put(AppConstant.KEY_APP_ID,"10");
            jsonObject.put("note_entry_id",notification_read_id);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void notification_read_status(int notification_id){
        notification_read_id=notification_id;
        sendNotificationReadStatus();
    }


}
