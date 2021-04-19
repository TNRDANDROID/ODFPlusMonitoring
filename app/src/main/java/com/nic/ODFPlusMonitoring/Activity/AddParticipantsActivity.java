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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nic.ODFPlusMonitoring.Adapter.AddParticipatesAdapterView;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddParticipantsActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_particption_screen);
        context=this;
        floatingActionButton=findViewById(R.id.add_participants);
        home=findViewById(R.id.home_img);


        add_participants_recyler=findViewById(R.id.add_participants_recyler);

        setAdapter();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStructureView(0,"","");
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

    public void setAdapter(){
        add_participants_recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addParticipatesAdapterView=new AddParticipatesAdapterView(this);
        add_participants_recyler.setAdapter(addParticipatesAdapterView);
    }
    public void addStructureView(int designation_id,String participates_name,String mobile_number){
        Spinner designation;
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

        final Dialog dialog = new Dialog(this,R.style.AppTheme);
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


        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JSONArray imageJson = new JSONArray();


                int childCount = mobileNumberLayout.getChildCount();
                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        JSONArray imageArray = new JSONArray();

                        View vv = mobileNumberLayout.getChildAt(i);
                        m_mobile=vv.findViewById(R.id.mobile_number);
                        m_name=vv.findViewById(R.id.name);
                        designation=vv.findViewById(R.id.designation_name);

                    }

                }
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                dialog.dismiss();
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


}
