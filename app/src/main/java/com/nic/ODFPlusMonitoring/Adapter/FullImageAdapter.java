package com.nic.ODFPlusMonitoring.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;

import java.util.List;

public class FullImageAdapter extends RecyclerView.Adapter<FullImageAdapter.MyViewHolder> {

    private Context context;
    private PrefManager prefManager;
    private List<ODFMonitoringListValue> imagePreviewlistvalues;
    private final dbData dbData;

    public FullImageAdapter(Context context, List<ODFMonitoringListValue> imagePreviewlistvalues, dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.imagePreviewlistvalues = imagePreviewlistvalues;
    }

    @Override
    public FullImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_full_image, parent, false);
        return new  MyViewHolder(itemView);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView preview_Full_imageview;
        private MyCustomTextView description,title;


        public MyViewHolder(View itemView) {
            super(itemView);
            preview_Full_imageview = (ImageView) itemView.findViewById(R.id.preview_Full_imageview);
            description = (MyCustomTextView) itemView.findViewById(R.id.description);
            title = (MyCustomTextView) itemView.findViewById(R.id.title);


        }


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.description.setText(imagePreviewlistvalues.get(position).getImageRemark());
        holder.preview_Full_imageview.setImageBitmap(imagePreviewlistvalues.get(position).getImage());
        holder.title.setText(imagePreviewlistvalues.get(position).getType()+"Activity");




    }

    @Override
    public int getItemCount() {
        return imagePreviewlistvalues.size();
    }
}
