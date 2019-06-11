package com.nic.ODFPlusMonitoring.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nic.ODFPlusMonitoring.Activity.CameraScreen;
import com.nic.ODFPlusMonitoring.Activity.FullImageActivity;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.MyViewHolder>{

    private final dbData dbData;
    private Context context;
    private List<ODFMonitoringListValue> activityListValues;
    private PrefManager prefManager;
    public final String dcode,bcode,pvcode;

    public ActivityListAdapter(Context context, List<ODFMonitoringListValue> activityListValues, dbData dbData) {
        this.context = context;
        this.activityListValues = activityListValues;
        this.dbData = dbData;
        prefManager = new PrefManager(context);
        dcode = prefManager.getDistrictCode();
        bcode = prefManager.getBlockCode();
        pvcode = prefManager.getPvCode();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView activity_name, view_online_images, view_offline_images;
        private RelativeLayout start_layout, end_layout;
        private LinearLayout schedule;
        private ImageView sportsImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            sportsImage = (ImageView)itemView.findViewById(R.id.sportsImage);
            activity_name = (MyCustomTextView) itemView.findViewById(R.id.activity_name);
            view_online_images = (MyCustomTextView) itemView.findViewById(R.id.view_online_images);
            view_offline_images = (MyCustomTextView) itemView.findViewById(R.id.view_offline_images);
            start_layout = (RelativeLayout) itemView.findViewById(R.id.start_layout);
            end_layout = (RelativeLayout) itemView.findViewById(R.id.end_layout);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.activity_name.setText(activityListValues.get(position).getActivityName());
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
        holder.start_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String activity_id = String.valueOf(activityListValues.get(position).getActivityId());
                final String schedule_id = String.valueOf(activityListValues.get(position).getScheduleId());

                ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode,bcode,pvcode,schedule_id,activity_id,"Start");

                if(activityImage.size() > 0) {
                    for (int i=0;i<activityImage.size();i++){
                        if(activityImage.get(i).getType().equalsIgnoreCase("start")){
                            Utils.showAlert((Activity) context,"Already Captured");
                        }
                        else {
                            cameraScreen(position,"Start");
                        }
                    }
                }
                else {
                    cameraScreen(position,"Start");
                }


            }
        });

        holder.end_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String activity_id = String.valueOf(activityListValues.get(position).getActivityId());
                final String schedule_id = String.valueOf(activityListValues.get(position).getScheduleId());

                ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode,bcode,pvcode,schedule_id,activity_id,"End");

                if(activityImage.size() > 0) {
                    for (int i=0;i<activityImage.size();i++){
                        if(activityImage.get(i).getType().equalsIgnoreCase("end")){
                            Utils.showAlert((Activity) context,"Already Captured");
                        }
                        else {
                            cameraScreen(position,"End");
                        }
                    }
                }
                else {
                    cameraScreen(position,"End");
                }
            }
        });

        dbData.open();
        final String activity_id = String.valueOf(activityListValues.get(position).getActivityId());
        final String schedule_id = String.valueOf(activityListValues.get(position).getScheduleId());
        final String schedule_activity_id = String.valueOf(activityListValues.get(position).getScheduleActivityId());

        ArrayList<ODFMonitoringListValue> activityImageOffline = dbData.selectImageActivity(dcode,bcode,pvcode,schedule_id,activity_id,"");

        if(activityImageOffline.size() > 0) {
            holder.view_offline_images.setVisibility(View.VISIBLE);
        }
        else {
            holder.view_offline_images.setVisibility(View.GONE);
        }

        holder.view_offline_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOfflineImages(schedule_id,activity_id,"Offline");
            }
        });
        dbData.open();
        ArrayList<ODFMonitoringListValue> activityImageOnline = dbData.selectActivityPhoto(dcode,bcode,pvcode,schedule_id,schedule_activity_id);

        if(activityImageOnline.size() > 0) {
            holder.view_online_images.setVisibility(View.VISIBLE);
        }
        else {
            holder.view_online_images.setVisibility(View.GONE);
        }

        holder.view_online_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOfflineImages(schedule_id,schedule_activity_id,"Online");
            }
        });

    }

    public void viewOfflineImages(String schedule_id,String id,String OnOffType) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra(AppConstant.KEY_SCHEDULE_ID,schedule_id);
        if(OnOffType.equalsIgnoreCase("Offline")){
            intent.putExtra(AppConstant.KEY_ACTIVITY_ID,id);
        }
        else if(OnOffType.equalsIgnoreCase("Online")) {
            intent.putExtra(AppConstant.KEY_SCHEDULE_ACTIVITY_ID,id);
        }
        intent.putExtra("OnOffType",OnOffType);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    @Override
    public int getItemCount() {
        return activityListValues.size();
    }

    public void cameraScreen(int pos,String type){
        Activity activity = (Activity) context;
        Intent intent = new Intent(context,CameraScreen.class);
        intent.putExtra(AppConstant.KEY_TYPE, type);
        intent.putExtra(AppConstant.KEY_ACTIVITY_ID, String.valueOf(activityListValues.get(pos).getActivityId()));
        intent.putExtra(AppConstant.KEY_SCHEDULE_ID, String.valueOf(activityListValues.get(pos).getScheduleId()));
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
