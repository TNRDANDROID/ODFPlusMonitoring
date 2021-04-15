package com.nic.ODFPlusMonitoring.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nic.ODFPlusMonitoring.Activity.HomePage;
import com.nic.ODFPlusMonitoring.Activity.PendingScreen;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Dialog.MyDialog;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;

import java.util.List;

public class FullImageAdapter extends RecyclerView.Adapter<FullImageAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private PrefManager prefManager;
    private List<ODFMonitoringListValue> imagePreviewlistvalues;
    private final dbData dbData;

    public FullImageAdapter(Activity activity,Context context, List<ODFMonitoringListValue> imagePreviewlistvalues, dbData dbData) {

        this.context = context;
        this.activity = activity;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.imagePreviewlistvalues = imagePreviewlistvalues;
    }

    @Override
    public FullImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);
        return new  MyViewHolder(itemView);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail,delete;
        private MyCustomTextView description,title;


        public MyViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            delete = (ImageView) itemView.findViewById(R.id.delete);
//            description = (MyCustomTextView) itemView.findViewById(R.id.description);
//            title = (MyCustomTextView) itemView.findViewById(R.id.title);


        }


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//        holder.description.setText(imagePreviewlistvalues.get(position).getImageRemark());
//        holder.thumbnail.setImageBitmap(imagePreviewlistvalues.get(position).getImage());
//        holder.title.setText(imagePreviewlistvalues.get(position).getType()+" Activity");
        Glide.with(context).load(imagePreviewlistvalues.get(position).getImage())
                .thumbnail(0.5f)
                .into(holder.thumbnail);
        if(imagePreviewlistvalues.get(position).getType().equals("Start")){
            holder.delete.setVisibility(View.GONE);
        }else if(imagePreviewlistvalues.get(position).getType().equals("End")){
            holder.delete.setVisibility(View.GONE);
        }else if(imagePreviewlistvalues.get(position).getType().equals("Middle")){
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag =false;
                flag=new MyDialog(activity).flagDialog(activity, "Are you sure to delete?", "Delete");
                if(flag){
                    String dcode = imagePreviewlistvalues.get(position).getDistictCode();
                    String bcode = imagePreviewlistvalues.get(position).getBlockCode();
                    String pvcode = imagePreviewlistvalues.get(position).getPvCode();
                    String activity_id = String.valueOf(imagePreviewlistvalues.get(position).getActivityId());
                    String schedule_id = String.valueOf(imagePreviewlistvalues.get(position).getScheduleId());
                    String photo_id = String.valueOf(imagePreviewlistvalues.get(position).getPhotoID());
                    long id = PendingScreen.db.delete(DBHelper.SAVE_ACTIVITY,
                            "id = ? and dcode = ? and bcode = ? and pvcode = ? and schedule_id = ? and activity_id = ?",
                            new String[] {photo_id,dcode,bcode,pvcode,schedule_id,activity_id});
                    notifyDataSetChanged();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return imagePreviewlistvalues.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FullImageAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FullImageAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
