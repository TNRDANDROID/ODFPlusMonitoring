package com.nic.ODFPlusMonitoring.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nic.ODFPlusMonitoring.Activity.AddParticipantsActivity;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;

import java.util.ArrayList;

public class AddParticipatesAdapterView extends RecyclerView.Adapter<AddParticipatesAdapterView.ViewHolder> {

    Context context;
    ArrayList<ODFMonitoringListValue> participatesList;

    public AddParticipatesAdapterView(Context context, ArrayList<ODFMonitoringListValue> participatesList) {
        this.context = context;
        this.participatesList=participatesList;
    }

    @NonNull
    @Override
    public AddParticipatesAdapterView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_participates_recyler_item_view, viewGroup, false);
        return new AddParticipatesAdapterView.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddParticipatesAdapterView.ViewHolder viewHolder, final int i) {
        viewHolder.designation_name.setText(participatesList.get(i).getExist_designation_name());
        viewHolder.participates_name.setText(participatesList.get(i).getExist_participate_name());
        viewHolder.mobile_number.setText(participatesList.get(i).getExist_participates_mobile());
                    viewHolder.edit_fun.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((AddParticipantsActivity)context).addStructureView(participatesList.get(i).getDesignation_code(),participatesList.get(i).getExist_participate_name(),participatesList.get(i).getExist_participates_mobile(),participatesList.get(i).getParticipants_id());
                        }
                    });
    }

    @Override
    public int getItemCount() {
        return participatesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView designation_name,participates_name,mobile_number,edit_fun;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            designation_name=itemView.findViewById(R.id.designation_name);
            participates_name=itemView.findViewById(R.id.participates_name);
            mobile_number=itemView.findViewById(R.id.mobile_number);
            edit_fun=itemView.findViewById(R.id.edit_fun);
        }
    }
}
