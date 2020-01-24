package com.nic.ODFPlusMonitoring.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class ActivityCarriedOut extends AppCompatActivity {

    private ShimmerRecyclerView recyclerView;
    private PrefManager prefManager;
    public dbData dbData = new dbData(this);
    private ActivityCarriedOutAdapter activityCarriedOutAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carried_out);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);


        recyclerView = (ShimmerRecyclerView) findViewById(R.id.activity_history);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        new fetchActivityHistorytask().execute();
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
    }
}
