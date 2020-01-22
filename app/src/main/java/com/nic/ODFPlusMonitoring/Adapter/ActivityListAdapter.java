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
        private MyCustomTextView activity_name, view_online_images, view_offline_images, activity_type_name;
        private RelativeLayout start_layout, end_layout,multiple_photo_layout;
        private LinearLayout schedule;
        private ImageView sportsImage;
        RelativeLayout activityLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            sportsImage = (ImageView)itemView.findViewById(R.id.sportsImage);
            activity_name = (MyCustomTextView) itemView.findViewById(R.id.activity_name);
            activity_type_name = (MyCustomTextView) itemView.findViewById(R.id.activity_type_name);
            view_online_images = (MyCustomTextView) itemView.findViewById(R.id.view_online_images);
            view_offline_images = (MyCustomTextView) itemView.findViewById(R.id.view_offline_images);
            start_layout = (RelativeLayout) itemView.findViewById(R.id.start_layout);
            end_layout = (RelativeLayout) itemView.findViewById(R.id.end_layout);
            multiple_photo_layout = (RelativeLayout) itemView.findViewById(R.id.multiple_photo_layout);
            activityLayout = (RelativeLayout) itemView.findViewById(R.id.activity_layout);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String activity_status = activityListValues.get(position).getActivityStatus();
        holder.activity_name.setText(activityListValues.get(position).getActivityName());
        holder.activity_type_name.setText(activityListValues.get(position).getActivityTypeName());
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
                if (!activity_status.equalsIgnoreCase("y")) {
                    final String activity_id = String.valueOf(activityListValues.get(position).getActivityId());
                    final String schedule_id = String.valueOf(activityListValues.get(position).getScheduleId());

                    ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "Start");

                    if (activityImage.size() > 0) {
                        for (int i = 0; i < activityImage.size(); i++) {
                            if (activityImage.get(i).getType().equalsIgnoreCase("start")) {
                                Utils.showAlert((Activity) context, "Already Captured");
                            } else {
                                cameraScreen(position, "Start", "1");
                            }
                        }
                    } else {
                        cameraScreen(position, "Start", "1");
                    }


                } else {
                    Utils.showAlert((Activity) context, "Activity Completed");
                }
            }
        });

        holder.end_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity_status.equalsIgnoreCase("y")) {

                    final String activity_id = String.valueOf(activityListValues.get(position).getActivityId());
                    final String schedule_id = String.valueOf(activityListValues.get(position).getScheduleId());
                    ArrayList<ODFMonitoringListValue> activityImage1 = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "Start");

                    if (activityImage1.size() <= 0) {
                        Utils.showAlert((Activity) context, "Please Capture Image for Start Activity");
                    } else {
                        ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "End");

                        if (activityImage.size() > 0) {
                            for (int i = 0; i < activityImage.size(); i++) {
                                if (activityImage.get(i).getType().equalsIgnoreCase("end")) {
                                    Utils.showAlert((Activity) context, "Already Captured");
                                } else {
                                    cameraScreen(position, "End", "1");
                                }
                            }
                        } else {
                            cameraScreen(position, "End", "1");
                        }
                    }
                }else {
                        Utils.showAlert((Activity) context, "Activity Completed");
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
//            for (int k = 0;k<activityImageOnline.size();k++) {
//                if(activityListValues.get(position).getImageAvailable().equalsIgnoreCase("Y")){
//                    holder.view_online_images.setVisibility(View.VISIBLE);
//                }
//                else {
//
//                }
//            }
        }else {
            holder.view_online_images.setVisibility(View.GONE);
        }

        holder.view_online_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOfflineImages(schedule_id,schedule_activity_id,"Online");
            }
        });

        final Integer no_of_photos = activityListValues.get(position).getNoOfPhotos();

        if(no_of_photos > 2) {
            holder.multiple_photo_layout.setVisibility(View.VISIBLE);
        }
        else {
            holder.multiple_photo_layout.setVisibility(View.GONE);
        }

        holder.multiple_photo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity_status.equalsIgnoreCase("y")) {
                    ArrayList<ODFMonitoringListValue> activityImage1 = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "Start");

                    if (activityImage1.size() <= 0) {
                        Utils.showAlert((Activity) context, "Please Capture Image for Start Activity");
                    } else {
                        ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "Middle");

                        if (activityImage.size() > 0 && activityImage.size() == (no_of_photos - 2)) {
                            Utils.showAlert((Activity) context, "Limit Exceed");
                        } else {
                            cameraScreen(position, "Middle", String.valueOf(activityImage.size() + 1));
                        }
                    }

                } else {
                    Utils.showAlert((Activity) context, "Activity Completed");
                }
            }
        });



        if(activity_status.equalsIgnoreCase("y")) {
//            for ( int i = 0; i < holder.activityLayout.getChildCount();  i++ ){
//                View view = holder.activityLayout.getChildAt(i);
//                view.setEnabled(false); // Or whatever you want to do with the view.
//            }
//            holder.start_layout.setEnabled(false);
//            holder.end_layout.setEnabled(false);
//            holder.multiple_photo_layout.setEnabled(false);
        }
        else if(activity_status.equalsIgnoreCase("N")){
            holder.start_layout.setEnabled(true);
            holder.end_layout.setEnabled(true);
            holder.multiple_photo_layout.setEnabled(true);
        }

        holder.activityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity_status.equalsIgnoreCase("y")) {
                   Utils.showAlert((Activity) context,"Activity Completed");
                }
            }
        });
    }

    public void viewOfflineImages(String schedule_id,String id,String OnOffType) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra(AppConstant.KEY_SCHEDULE_ID,schedule_id);
        intent.putExtra("OnOffType",OnOffType);
        if(OnOffType.equalsIgnoreCase("Offline")){
            intent.putExtra(AppConstant.KEY_ACTIVITY_ID,id);
            activity.startActivity(intent);
        }
        else if(OnOffType.equalsIgnoreCase("Online")) {
            intent.putExtra(AppConstant.KEY_SCHEDULE_ACTIVITY_ID,id);
            if(Utils.isOnline()){
                activity.startActivity(intent);
            }else {
                Utils.showAlert(activity,"Your Internet seems to be Offline.Images can be viewed only in Online mode.");
            }
        }


        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    @Override
    public int getItemCount() {
        return activityListValues.size();
    }

    public void cameraScreen(int pos,String type,String serial_number){
        Activity activity = (Activity) context;
        Intent intent = new Intent(context,CameraScreen.class);
        intent.putExtra(AppConstant.KEY_TYPE, type);
        intent.putExtra(AppConstant.KEY_SERIAL_NUMBER, serial_number);
        intent.putExtra(AppConstant.KEY_ACTIVITY_ID, String.valueOf(activityListValues.get(pos).getActivityId()));
        intent.putExtra(AppConstant.KEY_SCHEDULE_ID, String.valueOf(activityListValues.get(pos).getScheduleId()));
        intent.putExtra(AppConstant.KEY_ACTIVITY_NAME, activityListValues.get(pos).getActivityName());
        intent.putExtra(AppConstant.KEY_PURPOSE, "Insert");
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
