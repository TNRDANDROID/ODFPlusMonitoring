package com.nic.ODFPlusMonitoring.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nic.ODFPlusMonitoring.Activity.ActivityScreen;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActivityCarriedOutAdapter extends RecyclerView.Adapter<ActivityCarriedOutAdapter.MyViewHolder>{

    private final dbData dbData;
    private Context context;
    private List<ODFMonitoringListValue> scheduleListValues;
    private PrefManager prefManager;

    public ActivityCarriedOutAdapter(Context context, List<ODFMonitoringListValue> scheduleListValues, dbData dbData) {
        this.context = context;
        this.scheduleListValues = scheduleListValues;
        this.dbData = dbData;
       prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_carried_out_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView schedule_tv,from_date_tv,to_date_tv,schedule_description_tv;
        private LinearLayout schedule, vertical_tv;
        private CardView district_card;
        int[] androidColors;

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
        return scheduleListValues.size();
    }

}
