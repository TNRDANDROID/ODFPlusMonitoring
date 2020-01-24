package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.content.Intent;
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
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.nic.ODFPlusMonitoring.Adapter.ActivityCarriedOutAdapter;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.Utils;
import com.nic.ODFPlusMonitoring.DataBase.dbData;

import java.util.ArrayList;

public class ActivityCarriedOut extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private PrefManager prefManager;
    public dbData dbData = new dbData(this);
    private ActivityCarriedOutAdapter activityCarriedOutAdapter;
    private ImageView back_img,home_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carried_out);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);


        recyclerView = (RecyclerView) findViewById(R.id.activity_history);
        back_img = (ImageView) findViewById(R.id.back_img);
        home_img = (ImageView) findViewById(R.id.home_img);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        back_img.setOnClickListener(this);
        home_img.setOnClickListener(this);

        new fetchActivityHistorytask().execute();
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

 }
