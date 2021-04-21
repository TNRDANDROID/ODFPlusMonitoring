package com.nic.ODFPlusMonitoring.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nic.ODFPlusMonitoring.Model.NotificationList;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.SummaryViewHolder> {
    private Context context;
    ArrayList<NotificationList> notificationList;
    String type;

    public NotificationAdapter(Context context, ArrayList<NotificationList> notificationList, String type) {
        this.context = context;
        this.notificationList = notificationList;
        this.type = type;
    }

    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.recycler_notification_item, viewGroup, false);
        SummaryViewHolder mainHolder = new SummaryViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return mainHolder;
    }

    @Override
    public void onBindViewHolder(final SummaryViewHolder holder,final int position) {

        try {
                holder.notification_title.setText(notificationList.get(position).getNotification());
                holder.notification_date.setText(notificationList.get(position).getNotification_date());
            if(type.equalsIgnoreCase("NotificationList")){
                if(notificationList.get(position).getNotification().length() > 0) {
                    Utils.addReadMoreNotification(context, notificationList.get(position).getNote_entry_id(), notificationList.get(position).getNotification(), holder.notification_title);
                }
            }else {
                if(notificationList.get(position).getNotification().length() > 35) {
                    Utils.addReadMore(context,notificationList.get(position).getNotification(), holder.notification_title,3);
                }
            }

        } catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return notificationList.size();
    }

    class SummaryViewHolder extends RecyclerView.ViewHolder {

        TextView notification_title, notification_date/*,notification_time,notification_message*/;

        SummaryViewHolder(View view) {
            super(view);

            notification_title = (TextView) view.findViewById(R.id.tittle);
//            notification_message = (TextView) view.findViewById(R.id.description);
            notification_date = (TextView) view.findViewById(R.id.date);
//            notification_time = (TextView) view.findViewById(R.id.time);

        }

    }


}
