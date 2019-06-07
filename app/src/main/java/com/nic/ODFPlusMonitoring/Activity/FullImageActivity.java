package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.nic.ODFPlusMonitoring.Adapter.FullImageAdapter;
import com.nic.ODFPlusMonitoring.Adapter.ScheduleListAdapter;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;

import java.util.ArrayList;
import java.util.List;


public class FullImageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView toolBarLeft_icon, toolBarRight_icon;
    private RecyclerView image_preview_recyclerview;
    private PrefManager prefManager;
    private FullImageAdapter fullImageAdapter;
    private MyCustomTextView title_tv;
    private ImageView back_img,home_img;
    private dbData dbData = new dbData(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_recycler);
        intializeUI();
    }
    public void intializeUI() {
        prefManager = new PrefManager(this);
        image_preview_recyclerview = (RecyclerView) findViewById(R.id.image_preview_recyclerview);
        back_img = (ImageView) findViewById(R.id.back_img);
        home_img = (ImageView) findViewById(R.id.home_img);
        back_img.setOnClickListener(this);
        home_img.setOnClickListener(this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        image_preview_recyclerview.setLayoutManager(mLayoutManager);
        image_preview_recyclerview.setItemAnimator(new DefaultItemAnimator());
        image_preview_recyclerview.setHasFixedSize(true);
        image_preview_recyclerview.setNestedScrollingEnabled(false);
        image_preview_recyclerview.setFocusable(false);
        image_preview_recyclerview.setAdapter(fullImageAdapter);


        new fetchImagetask().execute();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPress();
                break;

            case R.id.home_img:
                homePage();
                break;
        }
    }

    public class fetchImagetask extends AsyncTask<Void, Void,
            ArrayList<ODFMonitoringListValue>> {
        @Override
        protected ArrayList<ODFMonitoringListValue> doInBackground(Void... params) {
            ArrayList<ODFMonitoringListValue> activityImage = new ArrayList<>();
            String schedule_id = getIntent().getStringExtra(AppConstant.KEY_SCHEDULE_ID);
            final String dcode = prefManager.getDistrictCode();
            final String bcode = prefManager.getBlockCode();
            final String pvcode = prefManager.getPvCode();
            String id = "";
            String OnOffType = getIntent().getStringExtra("OnOffType");

            if(OnOffType.equalsIgnoreCase("Online")){
                id = getIntent().getStringExtra(AppConstant.KEY_SCHEDULE_ACTIVITY_ID);
                dbData.open();
                activityImage = dbData.selectActivityPhoto(schedule_id,id);
            }
            else if(OnOffType.equalsIgnoreCase("Offline")){
                id = getIntent().getStringExtra(AppConstant.KEY_ACTIVITY_ID);
                dbData.open();
                activityImage = dbData.selectImageActivity(dcode,bcode,pvcode,schedule_id,id,"");
            }

            Log.d("IMAGE_COUNT", String.valueOf(activityImage.size()));
            return activityImage;
        }

        @Override
        protected void onPostExecute(ArrayList<ODFMonitoringListValue> imageList) {
            super.onPostExecute(imageList);
            fullImageAdapter = new FullImageAdapter(FullImageActivity.this,
                    imageList, dbData);
            image_preview_recyclerview.setAdapter(fullImageAdapter);
        }
    }

    public void homePage() {
        Intent intent = new Intent(this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


}
