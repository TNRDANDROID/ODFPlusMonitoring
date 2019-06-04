package com.nic.ODFPlusMonitoring.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.nic.ODFPlusMonitoring.Activity.CameraScreen;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.MyViewHolder>{

    private final dbData dbData;
    private Context context;
    private List<ODFMonitoringListValue> activityListValues;
   private PrefManager prefManager;

    public ActivityListAdapter(Context context, List<ODFMonitoringListValue> activityListValues, dbData dbData) {
        this.context = context;
        this.activityListValues = activityListValues;
        this.dbData = dbData;
        prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView activity_name;
        private RelativeLayout start_activity,end_activity;
        private LinearLayout schedule;
        private ImageView sportsImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            sportsImage = (ImageView)itemView.findViewById(R.id.sportsImage);
            activity_name = (MyCustomTextView) itemView.findViewById(R.id.activity_name);
            start_activity = (RelativeLayout) itemView.findViewById(R.id.start_activity);
            end_activity = (RelativeLayout) itemView.findViewById(R.id.end_activity);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(position %2 == 1)
        {
            holder.sportsImage.setImageResource(R.drawable.img_cycling);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.sportsImage.setImageResource(R.drawable.img_golf);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        holder.start_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraScreen(position,"Start");
            }
        });

        holder.end_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraScreen(position,"End");
            }
        });
        holder.activity_name.setText(activityListValues.get(position).getActivityName());
    }


    @Override
    public int getItemCount() {
        return activityListValues.size();
    }

    public void cameraScreen(int pos,String type){
        Activity activity = (Activity) context;
        Intent intent = new Intent(context,CameraScreen.class);
        intent.putExtra(AppConstant.KEY_POINT_TYPE,type);
        intent.putExtra(AppConstant.KEY_ACTIVITY_ID,activityListValues.get(pos).getActivityId());
        intent.putExtra(AppConstant.KEY_SCHEDULE_ID,activityListValues.get(pos).getScheduleId());
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
