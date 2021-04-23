package com.nic.ODFPlusMonitoring.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nic.ODFPlusMonitoring.Activity.AddParticipantsActivity;
import com.nic.ODFPlusMonitoring.Dialog.MyDialog;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                    viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONArray jsonArray=new JSONArray();
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("contact_person_id",participatesList.get(i).getParticipants_id());
                                jsonObject.put("name_of_contact_person",participatesList.get(i).getExist_participate_name());
                                jsonObject.put("mobileno",participatesList.get(i).getExist_participates_mobile());
                                jsonObject.put("contact_person_type_id",participatesList.get(i).getDesignation_code());
                                jsonObject.put("deleted","Y");
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            exitDialog("Are you sure you delete the person "+participatesList.get(i).getExist_participate_name()+"?", jsonArray);
                        }
                    });
    }

    @Override
    public int getItemCount() {
        return participatesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView designation_name,participates_name,mobile_number,edit_fun;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            designation_name=itemView.findViewById(R.id.designation_name);
            participates_name=itemView.findViewById(R.id.participates_name);
            mobile_number=itemView.findViewById(R.id.mobile_number);
            edit_fun=itemView.findViewById(R.id.edit_fun);
            delete=itemView.findViewById(R.id.delete);
        }
    }
    public void exitDialog(String message, final JSONArray jsonArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.setCancelable(false);
        alertDialog.show();

        MyCustomTextView tv_message = (MyCustomTextView) dialogView.findViewById(R.id.tv_message);
        tv_message.setText(message);

        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddParticipantsActivity)context).deleteParticipatesApiService(jsonArray);
                alertDialog.dismiss();
            }
        });
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

}
