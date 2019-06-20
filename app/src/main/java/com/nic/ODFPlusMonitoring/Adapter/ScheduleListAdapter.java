package com.nic.ODFPlusMonitoring.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.Random;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.MyViewHolder>{

    private final dbData dbData;
    private Context context;
    private List<ODFMonitoringListValue> scheduleListValues;
    private PrefManager prefManager;

    public ScheduleListAdapter(Context context, List<ODFMonitoringListValue> scheduleListValues, dbData dbData) {
        this.context = context;
        this.scheduleListValues = scheduleListValues;
        this.dbData = dbData;
       prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView schedule_tv,from_date_tv,to_date_tv,schedule_description_tv;
        private LinearLayout schedule, vertical_tv;
        private CardView district_card;
        int[] androidColors;

        public MyViewHolder(View itemView) {
            super(itemView);
            androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            schedule_tv = (MyCustomTextView)itemView.findViewById(R.id.schedule_tv);
            from_date_tv = (MyCustomTextView)itemView.findViewById(R.id.from_date_tv);
            to_date_tv = (MyCustomTextView)itemView.findViewById(R.id.to_date_tv);
            schedule_description_tv = (MyCustomTextView)itemView.findViewById(R.id.schedule_description_tv);
            schedule = (LinearLayout)itemView.findViewById(R.id.schedule);
            vertical_tv = (LinearLayout) itemView.findViewById(R.id.vertical_tv);
            district_card = (CardView) itemView.findViewById(R.id.district_card);
            schedule_tv.setVisibility(View.GONE);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (position % 2 == 1) {
            holder.vertical_tv.setBackgroundResource(R.drawable.gradient_diff);
            holder.district_card.setBackgroundResource(R.drawable.gradient_blue);


        } else {
            holder.vertical_tv.setBackgroundResource(R.drawable.gradient_pink);
            holder.district_card.setBackgroundResource(R.drawable.gradient);

        }
//        int remainder = holder.getAdapterPosition() % holder.androidColors.length;
//        mView.setCardBackgroundColor(Color.parseColor(holder.androidColors.get(remainder)));
//        int randomAndroidColor = holder.androidColors[new Random().nextInt(holder.androidColors.length)];
//        Drawable background = holder.district_card.getBackground();
//        if (background instanceof ShapeDrawable) {
//            ((ShapeDrawable)background).getPaint().setColor(randomAndroidColor);
//        } else if (background instanceof GradientDrawable) {
//            ((GradientDrawable)background).setColor(randomAndroidColor);
//        } else if (background instanceof ColorDrawable) {
//            ((ColorDrawable)background).setColor(randomAndroidColor);
//
//        int randomAndroidColor = holder.androidColors[new Random().nextInt(holder.androidColors.length)];
//        holder.district_card.setBackgroundColor(randomAndroidColor);

        String from_date = scheduleListValues.get(position).getScheduleFromDate();
        String to_date = scheduleListValues.get(position).getScheduletoDate();


//        holder.schedule_tv.setText("Schedule "+scheduleListValues.get(position).getScheduleId());
        holder.schedule_description_tv.setText(scheduleListValues.get(position).getScheduleDescription());
        holder.from_date_tv.setText(Utils.formatDate(from_date));
        holder.to_date_tv.setText(Utils.formatDate(to_date));

        holder.schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date from =  new SimpleDateFormat("yyyy-MM-dd").parse(scheduleListValues.get(position).getScheduleFromDate());
                    Date to =  new SimpleDateFormat("yyyy-MM-dd").parse(scheduleListValues.get(position).getScheduletoDate());
                    Date current =  new SimpleDateFormat("yyyy-MM-dd").parse(Utils.getCurrentDate());
// Date current = new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-30");

                    if(from.after(current)){
                        Utils.showAlert((Activity) context,"Your Schedule is not Started");
                    }
                    else if (from.compareTo(current) * current.compareTo(to) >= 0) {
                        openActivity(position);
                    }
                    else {
                        Utils.showAlert((Activity) context,"Your Schedule is Expired");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void openActivity(int pos){
        prefManager.setScheduleMasterId(scheduleListValues.get(pos).getScheduleMasterId());
        Log.d("schedule_id", String.valueOf(scheduleListValues.get(pos).getScheduleId()));
        Log.d("motivator", String.valueOf(scheduleListValues.get(pos).getMotivatorId()));
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, ActivityScreen.class);
        intent.putExtra(AppConstant.KEY_SCHEDULE_ID,String.valueOf(scheduleListValues.get(pos).getScheduleId()));
        intent.putExtra(AppConstant.KEY_MOTIVATOR_ID,String.valueOf(scheduleListValues.get(pos).getMotivatorId()));
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


    }



    @Override
    public int getItemCount() {
        return scheduleListValues.size();
    }

}
