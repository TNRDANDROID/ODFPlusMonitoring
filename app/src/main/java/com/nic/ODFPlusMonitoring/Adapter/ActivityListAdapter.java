package com.nic.ODFPlusMonitoring.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nic.ODFPlusMonitoring.Activity.CameraScreen;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.MyViewHolder>{

    private final dbData dbData;
    private Context context;
    private List<ODFMonitoringListValue> activityListValues;
  //  private PrefManager prefManager;

    public ActivityListAdapter(Context context, List<ODFMonitoringListValue> activityListValues, dbData dbData) {
        this.context = context;
        this.activityListValues = activityListValues;
        this.dbData = dbData;
  //      prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView schedule_tv,from_date_tv,to_date_tv,schedule_description_tv;
        private LinearLayout schedule;

        public MyViewHolder(View itemView) {
            super(itemView);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
    }


    @Override
    public int getItemCount() {
        return activityListValues.size();
    }

}
