package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Adapter.AddParticipatesAdapterView;
import com.nic.ODFPlusMonitoring.Adapter.CommonAdapter;
import com.nic.ODFPlusMonitoring.Adapter.NotificationAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.Model.NotificationList;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddParticipantsActivity extends AppCompatActivity implements Api.ServerResponseListener {
    Context context;
    private AlertDialog alert;
    FloatingActionButton floatingActionButton;
    ImageView home;
    AddParticipatesAdapterView addParticipatesAdapterView;
    RecyclerView add_participants_recyler;

    private List<View> viewArrayList = new ArrayList<>();
    Spinner designation;
    EditText m_name,m_mobile;
    ScrollView scrollView;
    JSONObject appParticipatesJson;
    private PrefManager prefManager;
    ArrayList<ODFMonitoringListValue> designationList;
    String selectedDesignation,selectedDesignationId;
    Dialog dialog;
    ArrayList<ODFMonitoringListValue> ParticipatesList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_particption_screen);
        context=this;
        prefManager = new PrefManager(this);
        floatingActionButton=findViewById(R.id.add_participants);
        home=findViewById(R.id.home_img);


        add_participants_recyler=findViewById(R.id.add_participants_recyler);

        //setAdapter();
        LoadParticipationDetails();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addStructureView(0,"","");
                imageWithDescription();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHomeScreen();
            }
        });


    }
    public void addParticipatesApiService() {
        try {
            new ApiService(this).makeJSONObjectRequest("AddParticipates", Api.Method.POST, UrlGenerator.getOpenUrl(), addParticipatesJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject addParticipatesJsonParams() {
        JSONObject jsonObject=new JSONObject();
        String authKey1 = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), appParticipatesJson.toString());

        try {
            jsonObject.put(AppConstant.KEY_USER_NAME,prefManager.getUserName());
            jsonObject.put(AppConstant.DATA_CONTENT,authKey1);
            Log.d("addPartJsonParams", "" +jsonObject);

        }catch (JSONException e){

        }       return jsonObject;
    }

    public void setAdapter(){
        add_participants_recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addParticipatesAdapterView=new AddParticipatesAdapterView(this,ParticipatesList);
        add_participants_recyler.setAdapter(addParticipatesAdapterView);
    }
    public void addStructureView(int designation_id,String participates_name,String mobile_number){
        Spinner designation;
        loadDesignationList(0);
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            final LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View view = inflater.inflate(R.layout.pop_up_add_other_particepants, null);
            TextView header;
            EditText m_name,m_mobile;
            ImageView close;
            RelativeLayout camera_activity;
            Button submit;

            header = (TextView) view.findViewById(R.id.header);
            designation = (Spinner) view.findViewById(R.id.designation);
            m_name = (EditText) view.findViewById(R.id.name);
            m_mobile = (EditText) view.findViewById(R.id.mobile_number);
            submit = (Button) view.findViewById(R.id.btnBuy);
            close = (ImageView) view.findViewById(R.id.close);

            m_name.setText(participates_name);
            m_mobile.setText(mobile_number);


            close.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_close));

            designation.setAdapter(new CommonAdapter(this,designationList,"DesignationList") );

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setView(view);
            alert = dialogBuilder.create();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alert.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            lp.windowAnimations = R.style.DialogAnimation;
            alert.getWindow().setAttributes(lp);
            alert.show();
            alert.setCanceledOnTouchOutside(true);
            alert.setCancelable(true);
            alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void showHomeScreen() {
        Intent intent = new Intent(AddParticipantsActivity.this,HomePage.class);
        intent.putExtra("Home", "Login");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void imageWithDescription() {
        appParticipatesJson=new JSONObject();
        dialog = new Dialog(this,R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_participates);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();


        final LinearLayout mobileNumberLayout = (LinearLayout) dialog.findViewById(R.id.mobile_number_layout);

        Button done = (Button) dialog.findViewById(R.id.btn_save_inspection);
        done.setGravity(Gravity.CENTER);
        done.setVisibility(View.VISIBLE);
        TextView close_id=dialog.findViewById(R.id.close_id);
        close_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    JSONArray imageJson = new JSONArray();
                    int childCount = mobileNumberLayout.getChildCount();
                    if (childCount > 0) {
                        for (int i = 0; i < childCount; i++) {
                            JSONObject jsonObject = new JSONObject();
                            View vv = mobileNumberLayout.getChildAt(i);
                            m_mobile=vv.findViewById(R.id.mobile_number);
                            m_name=vv.findViewById(R.id.name);
                            designation=vv.findViewById(R.id.designation);
                            if(!m_mobile.getText().toString().equals("")&&!m_name.getText().toString().equals("")&&
                                    designation.getSelectedItemPosition()!=0) {
                                if (Utils.isOnline()) {
//                                jsonObject.put("id",i);
                                    jsonObject.put("participants_name",m_name.getText().toString());
                                    jsonObject.put("participants_mobileno",m_mobile.getText().toString());
                                    jsonObject.put("participants_designation_id",designation.getSelectedItemPosition());
                                    imageJson.put(jsonObject);
                                }
                            }
                        else {
                                Utils.showAlert(AddParticipantsActivity.this,"Please fill all details in all view!");
                                break;
                            }

                        }
                        try {
                            appParticipatesJson.put(AppConstant.KEY_SERVICE_ID,"other_participants");
                            appParticipatesJson.put("other_participants",imageJson);
                            addParticipatesApiService();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //focusOnView(scrollView);

            }
        });

        Button btnAddMobile = (Button) dialog.findViewById(R.id.btn_add);

        viewArrayList.clear();
        btnAddMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    updateView(AddParticipantsActivity.this, mobileNumberLayout);
            }
        });

    }

    private final void focusOnView(final ScrollView your_scrollview) {
        your_scrollview.post(new Runnable() {
            @Override
            public void run() {
                your_scrollview.fullScroll(View.FOCUS_DOWN);
                //your_scrollview.scrollTo(0, your_EditBox.getY());
            }
        });
    }

    //Method for update single view based on email or mobile type
    public View updateView(final Activity activity, final LinearLayout emailOrMobileLayout) {
        final View hiddenInfo = activity.getLayoutInflater().inflate(R.layout.add_participates_view, emailOrMobileLayout, false);
        final ImageView imageView_close = (ImageView) hiddenInfo.findViewById(R.id.imageView_close);
        designation = (Spinner) hiddenInfo.findViewById(R.id.designation);
        m_name = (EditText) hiddenInfo.findViewById(R.id.name);
        m_mobile = (EditText) hiddenInfo.findViewById(R.id.mobile_number);
        final EditText myEditTextView = (EditText) hiddenInfo.findViewById(R.id.description);
        loadDesignationList(1);
        designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDesignation=(designationList.get(position).getGenderEn());
                selectedDesignationId=(designationList.get(position).getGenderCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (viewArrayList.size() != 1) {
                        ((LinearLayout) hiddenInfo.getParent()).removeView(hiddenInfo);
                        viewArrayList.remove(hiddenInfo);
                    }

                } catch (IndexOutOfBoundsException a) {
                    a.printStackTrace();
                }
            }
        });
        emailOrMobileLayout.addView(hiddenInfo);

        View vv = emailOrMobileLayout.getChildAt(viewArrayList.size());
        //myEditTextView1.setSelection(myEditTextView1.length());
        viewArrayList.add(hiddenInfo);
        return hiddenInfo;
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
            try {
                JSONObject responseObj = serverResponse.getJsonResponse();
                String urlType = serverResponse.getApi();
                String status = null;
                String response = null;
                if ("AddParticipates".equals(urlType)) {
                    String s="{\"STATUS\":\"SUCCESS\",\"RESPONSE\":\"OK\",\"MESSAGE\":\"Participants data added succussfully!\"}";
                    responseObj=new JSONObject(s);
                    status  = responseObj.getString(AppConstant.KEY_STATUS);
//                    response = responseObj.getString(AppConstant.KEY_RESPONSE);
                    if (status.equalsIgnoreCase("SUCCESS")) {
                        Utils.showAlert(AddParticipantsActivity.this,responseObj.getString("MESSAGE"));
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        dialog.dismiss();
                    }
                }



            }catch (JSONException e){

            }
    }

    private void loadDesignationList(int number) {
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
                    String designation_code = jsonobject.getString("designation_code");
                    String designation_name = (jsonobject.getString("designation_name"));
                    /*String gender_name_ta = (jsonobject.getString("gender_name_ta"));*/
                    ODFMonitoringListValue roadListValue = new ODFMonitoringListValue();
                    roadListValue.setDesignation_code(designation_code);
                    roadListValue.setDesignation_name(designation_name);

                    designationList.add(roadListValue);
                }
                if(number==1) {
                    designation.setAdapter(new CommonAdapter(this, designationList, "DesignationList"));
                }
                else {

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    private void LoadParticipationDetails() {
        ParticipatesList = new ArrayList<>();
        try {
            JSONArray jsonarray=new JSONArray(prefManager.getParticipatesList());
            if(jsonarray != null && jsonarray.length() >0) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    ODFMonitoringListValue Detail = new ODFMonitoringListValue();
                    Detail.setExist_designation_name(jsonobject.getString("participants_designation_Name"));
                    Detail.setExist_participate_name(jsonobject.getString("participants_name"));
                    Detail.setExist_participates_mobile(jsonobject.getString("participants_mobileno"));
                    ParticipatesList.add(Detail);
                }
            }
            if(ParticipatesList != null && ParticipatesList.size() >0) {
                setAdapter();
            }else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
