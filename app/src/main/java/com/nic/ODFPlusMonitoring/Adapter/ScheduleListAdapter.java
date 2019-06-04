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


import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.MyViewHolder>{

    private final dbData dbData;
    private Context context;
    private List<ODFMonitoringListValue> scheduleListValues;
  //  private PrefManager prefManager;

    public ScheduleListAdapter(Context context, List<ODFMonitoringListValue> scheduleListValues, dbData dbData) {
        this.context = context;
        this.scheduleListValues = scheduleListValues;
        this.dbData = dbData;
  //      prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView schedule_tv,from_date_tv,to_date_tv,schedule_description_tv;
        private LinearLayout schedule;

        public MyViewHolder(View itemView) {
            super(itemView);
            schedule_tv = (MyCustomTextView)itemView.findViewById(R.id.schedule_tv);
            from_date_tv = (MyCustomTextView)itemView.findViewById(R.id.from_date_tv);
            to_date_tv = (MyCustomTextView)itemView.findViewById(R.id.to_date_tv);
            schedule_description_tv = (MyCustomTextView)itemView.findViewById(R.id.schedule_description_tv);
            schedule = (LinearLayout)itemView.findViewById(R.id.schedule);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        String from_date = scheduleListValues.get(position).getScheduleFromDate();
        String to_date = scheduleListValues.get(position).getScheduletoDate();

        holder.schedule_tv.setText("Schedule "+scheduleListValues.get(position).getScheduleId());
        holder.schedule_description_tv.setText(scheduleListValues.get(position).getScheduleDescription());
        holder.from_date_tv.setText(Utils.formatDate(from_date));
        holder.to_date_tv.setText(Utils.formatDate(to_date));

        holder.schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(position);
            }
        });
    }

    public void openActivity(int pos){
        Log.d("schedule_id", String.valueOf(scheduleListValues.get(pos).getScheduleId()));
        Log.d("motivator", String.valueOf(scheduleListValues.get(pos).getMotivatorId()));
    }



    @Override
    public int getItemCount() {
        return scheduleListValues.size();
    }

}
