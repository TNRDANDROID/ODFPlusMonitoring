package com.nic.ODFPlusMonitoring.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nic.ODFPlusMonitoring.Adapter.AddParticipatesAdapterView;
import com.nic.ODFPlusMonitoring.R;

public class AddParticipantsActivity extends AppCompatActivity {
    Context context;
    private AlertDialog alert;
    FloatingActionButton floatingActionButton;
    ImageView home;
    AddParticipatesAdapterView addParticipatesAdapterView;
    RecyclerView add_participants_recyler;
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
}
