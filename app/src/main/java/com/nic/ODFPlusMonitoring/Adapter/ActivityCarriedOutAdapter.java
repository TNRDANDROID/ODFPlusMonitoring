package com.nic.ODFPlusMonitoring.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Utils.Utils;

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
        private TextView activity_name, activity_status, activity_start_date_time, activity_end_date_time;
        private LinearLayout start_time_layout,end_time_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            activity_name = (TextView) itemView.findViewById(R.id.activity_name);
            start_time_layout = (LinearLayout) itemView.findViewById(R.id.start_time_layout);
            end_time_layout = (LinearLayout) itemView.findViewById(R.id.end_time_layout);
            activity_status = (TextView) itemView.findViewById(R.id.activity_status);
            activity_start_date_time = (TextView) itemView.findViewById(R.id.activity_start_date_time);
            activity_end_date_time = (TextView) itemView.findViewById(R.id.activity_end_date_time);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String from_date = scheduleListValues.get(position).getActivityStart();
        String to_date = scheduleListValues.get(position).getActivityEnd();


        holder.activity_name.setText(scheduleListValues.get(position).getActivityName());
        if(scheduleListValues.get(position).getActivityName().length() > 35) {
            Utils.addReadMore(context, scheduleListValues.get(position).getActivityName(), holder.activity_name, 1);
        }
        if (scheduleListValues.get(position).getActivityStatus().equalsIgnoreCase("Y")) {
            holder.activity_status.setText("Completed");
        } else if (scheduleListValues.get(position).getActivityStatus().equalsIgnoreCase("N") &&
                scheduleListValues.get(position).getRejected_status().equalsIgnoreCase("N") ) {
            holder.activity_status.setText("Incomplete");
        } else if (scheduleListValues.get(position).getActivityStatus().equalsIgnoreCase("N") &&
                scheduleListValues.get(position).getRejected_status().equalsIgnoreCase("Y") ) {
            holder.activity_status.setText("Rejected");
            holder.activity_status.setTextColor(context.getResources().getColor(R.color.subscription_type_red_color));
        }

        if ( from_date != null && (!from_date.equalsIgnoreCase(""))) {
            holder.activity_start_date_time.setText(Utils.parseDateForChart(from_date));
            holder.start_time_layout.setVisibility(View.VISIBLE);
        }
        else{
            holder.start_time_layout.setVisibility(View.GONE);
        }

        if ( to_date!= null && (!to_date.equalsIgnoreCase(""))) {
            holder.activity_end_date_time.setText( Utils.parseDateForChart(to_date));
            holder.end_time_layout.setVisibility(View.VISIBLE);
        }else{
            holder.end_time_layout.setVisibility(View.GONE);
        }

    }





    @Override
    public int getItemCount() {
        return scheduleListValues.size();
    }

}
