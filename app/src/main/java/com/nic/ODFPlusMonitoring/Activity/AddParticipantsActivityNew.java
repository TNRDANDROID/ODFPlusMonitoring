package com.nic.ODFPlusMonitoring.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nic.ODFPlusMonitoring.Adapter.AddParticipatesAdapterView;
import com.nic.ODFPlusMonitoring.Adapter.CommonAdapter;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddParticipantsActivityNew extends AppCompatActivity {
    Context context;
    FloatingActionButton floatingActionButton;
    ImageView home;
    RecyclerView add_participants_recyler;
    private PrefManager prefManager;
    ArrayList<ODFMonitoringListValue> ParticipatesList;
    AddParticipantsAdapterView addParticipantsAdapterView;
    ArrayList<ODFMonitoringListValue> designationList;
    CommonAdapter commonAdapter;

    String selecteddesignationID;
    Button save;

    TextView participates_name,mobile_number;
    Spinner designation;
    ImageView imageView_close;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_particption_screen);
        context=this;
        prefManager = new PrefManager(this);
        floatingActionButton=findViewById(R.id.add_participants);
        home=findViewById(R.id.home_img);

        ParticipatesList=new ArrayList<ODFMonitoringListValue>();

        add_participants_recyler=findViewById(R.id.add_participants_recyler);
        //save=findViewById(R.id.btn_save_participates);
        add_participants_recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ODFMonitoringListValue Detail = new ODFMonitoringListValue();
        Detail.setExist_participate_name("");
        Detail.setExist_participates_mobile("");
        Detail.setDesignation_code("0");
        ParticipatesList.add(Detail);
        addParticipantsAdapterView= new AddParticipantsAdapterView(context,ParticipatesList);
        add_participants_recyler.setAdapter(addParticipantsAdapterView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParticipatesList.size()>0){
                    if(ParticipatesList.size()<4) {
                        if (checkItems()) {
                            ODFMonitoringListValue Detail = new ODFMonitoringListValue();
                            Detail.setExist_participate_name("");
                            Detail.setExist_participates_mobile("");
                            Detail.setDesignation_code("0");
                            ParticipatesList.add(Detail);
                            addParticipantsAdapterView.notifyItemInserted(ParticipatesList.size() - 1);
                        } else {
                            Utils.showAlert(AddParticipantsActivityNew.this, "Please fill all the view!");

                        }
                    }
                    else {
                        Utils.showAlert(AddParticipantsActivityNew.this, "Exceed the Limit!");
                    }

                }
                else {

                        ODFMonitoringListValue Detail = new ODFMonitoringListValue();
                        Detail.setExist_participate_name("");
                        Detail.setExist_participates_mobile("");
                        Detail.setDesignation_code("0");
                        ParticipatesList.add(Detail);
                        addParticipantsAdapterView = new AddParticipantsAdapterView(context, ParticipatesList);
                        add_participants_recyler.setAdapter(addParticipantsAdapterView);



                }
                /*addParticipantsAdapterView= new AddParticipantsAdapterView(context,ParticipatesList);
                add_participants_recyler.setAdapter(addParticipantsAdapterView);*/
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHomeScreen();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItems();
            }
        });

    }
    private void showHomeScreen() {
        if (ParticipatesList.size()>0){
            Intent intent = new Intent(AddParticipantsActivityNew.this, HomePage.class);
            intent.putExtra("Home", "Login");
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        else {
            Utils.showAlert(AddParticipantsActivityNew.this,"Add Participants");
        }
    }


    public class AddParticipantsAdapterView extends RecyclerView.Adapter<AddParticipantsAdapterView.ViewHolder> {

        Context context;
        ArrayList<ODFMonitoringListValue> participatesList;

        public AddParticipantsAdapterView(Context context, ArrayList<ODFMonitoringListValue> participatesList) {
            this.context = context;
            this.participatesList=participatesList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_participates_view, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
           //functions

            loadDesignationList(viewHolder.designation);

        }

        @Override
        public int getItemCount() {
            return participatesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView participates_name,mobile_number;
            Spinner designation;
            ImageView imageView_close;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                designation=itemView.findViewById(R.id.designation);
                participates_name=itemView.findViewById(R.id.name);
                mobile_number=itemView.findViewById(R.id.mobile_number);
                imageView_close=itemView.findViewById(R.id.imageView_close);

                imageView_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        removeItems(getAdapterPosition());
                    }
                });

                designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selecteddesignationID=(designationList.get(position).getDesignation_code());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            public void removeItems(int position){
                participatesList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, participatesList.size());
                notifyItemRangeRemoved(position,participatesList.size());

            }

        }
        private void loadDesignationList(Spinner designation) {
            try {
                JSONArray jsonarray=new JSONArray(prefManager.getDesignationList());
                designationList=new ArrayList<>();
                if(jsonarray != null && jsonarray.length() >0) {
                    ODFMonitoringListValue ODFMonitoringListValue = new ODFMonitoringListValue();
                    ODFMonitoringListValue.setDesignation_name("Select Designation");
                    ODFMonitoringListValue.setDesignation_code("0");
                    designationList.add(ODFMonitoringListValue);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String designation_code = jsonobject.getString("contact_person_type_id");
                        String designation_name = (jsonobject.getString("contact_person_type_name"));
                        /*String gender_name_ta = (jsonobject.getString("gender_name_ta"));*/
                        ODFMonitoringListValue roadListValue = new ODFMonitoringListValue();
                        roadListValue.setDesignation_code(designation_code);
                        roadListValue.setDesignation_name(designation_name);

                        designationList.add(roadListValue);
                    }
                    commonAdapter=new CommonAdapter(context, designationList, "DesignationList");
                    designation.setAdapter(commonAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public void saveItems() {
        add_participants_recyler.getChildCount();
        int childCount = add_participants_recyler.getChildCount();
        try {
            JSONArray imageJson = new JSONArray();
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    JSONObject jsonObject = new JSONObject();
                    View vv = add_participants_recyler.getChildAt(i);
                    mobile_number = vv.findViewById(R.id.mobile_number);
                    participates_name = vv.findViewById(R.id.name);
                    designation = vv.findViewById(R.id.designation);
                    if (Utils.isOnline()) {
                        jsonObject.put("contact_person_id", "");
                        jsonObject.put("name_of_contact_person", participates_name.getText().toString());
                        jsonObject.put("mobileno", mobile_number.getText().toString());
                        jsonObject.put("contact_person_type_id", designationList.get(designation.getSelectedItemPosition()).getDesignation_code());
                        /*jsonObject.put("deleted","N");*/
                        imageJson.put(jsonObject);
                    }


                }

            }

        } catch (JSONException e) {
        }


    }
    public boolean checkItems() {
        add_participants_recyler.getChildCount();
        int childCount = add_participants_recyler.getChildCount();
        try {
            JSONArray imageJson = new JSONArray();
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    JSONObject jsonObject = new JSONObject();
                    View vv = add_participants_recyler.getChildAt(i);
                    mobile_number = vv.findViewById(R.id.mobile_number);
                    participates_name = vv.findViewById(R.id.name);
                    designation = vv.findViewById(R.id.designation);
                    if (Utils.isOnline()) {
                        jsonObject.put("contact_person_id", "");
                        jsonObject.put("name_of_contact_person", participates_name.getText().toString());
                        jsonObject.put("mobileno", mobile_number.getText().toString());
                        jsonObject.put("contact_person_type_id", designationList.get(designation.getSelectedItemPosition()).getDesignation_code());
                        /*jsonObject.put("deleted","N");*/
                        imageJson.put(jsonObject);
                    }


                }
            }

            try {
                boolean flag =false;
                JSONObject jsonObject1=new JSONObject();
                for (int i=0;i<imageJson.length();i++){
                    jsonObject1=imageJson.getJSONObject(i);
                    if(!jsonObject1.getString("name_of_contact_person").equals("")&&!jsonObject1.getString("mobileno").equals("")&&!jsonObject1.getString("contact_person_type_id").equals("0")){
                        flag=true;
                    }
                    else {
                        flag=false;
                        break;
                    }
                }
                if (flag){
                   return true;
                }
                else {
                    return false;

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (JSONException e) {
        }

        return false;

    }



}
