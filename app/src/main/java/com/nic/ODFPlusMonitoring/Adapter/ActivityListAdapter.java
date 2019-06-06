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

import java.util.ArrayList;
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
                cameraScreen(position,"Start");
            }
        });

        holder.end_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraScreen(position,"End");
            }
        });

        holder.view_online_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dbData.open();
        final String activity_id = String.valueOf(activityListValues.get(position).getActivityId());
        final String schedule_id = String.valueOf(activityListValues.get(position).getScheduleId());
        final String dcode = prefManager.getDistrictCode();
        final String bcode = prefManager.getBlockCode();
        final String pvcode = prefManager.getPvCode();

        ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode,bcode,pvcode,schedule_id,activity_id);

        if(activityImage.size() > 0) {
            holder.view_offline_images.setVisibility(View.VISIBLE);
            for (int i=0;i<activityImage.size();i++){
                if(activityImage.get(i).getType().equalsIgnoreCase("start")){
                    holder.start_layout.setEnabled(false);
                }
            }
        }
        else {
            holder.view_offline_images.setVisibility(View.GONE);
        }

        holder.view_offline_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOfflineImages(schedule_id,activity_id);
            }
        });

    }

    public void viewOfflineImages(String schedule_id,String activity_id) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra(AppConstant.KEY_SCHEDULE_ID,schedule_id);
        intent.putExtra(AppConstant.KEY_ACTIVITY_ID,activity_id);
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
