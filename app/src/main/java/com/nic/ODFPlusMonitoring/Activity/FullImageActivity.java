package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Adapter.FullImageAdapter;
import com.nic.ODFPlusMonitoring.Adapter.ScheduleListAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Fragment.SlideshowDialogFragment;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FullImageActivity extends AppCompatActivity implements View.OnClickListener,Api.ServerResponseListener {
    private ImageView toolBarLeft_icon, toolBarRight_icon;
    private RecyclerView image_preview_recyclerview;
    private PrefManager prefManager;
    private FullImageAdapter fullImageAdapter;
    private MyCustomTextView title_tv;
    private ImageView back_img,home_img;
    private dbData dbData = new dbData(this);
    private  String schedule_id;
    private static  ArrayList<ODFMonitoringListValue> activityImage = new ArrayList<>();
    final Handler handler = new Handler();
    public String OnOffType;


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

        schedule_id = getIntent().getStringExtra(AppConstant.KEY_SCHEDULE_ID);
        OnOffType = getIntent().getStringExtra("OnOffType");


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        image_preview_recyclerview.setLayoutManager(mLayoutManager);
        image_preview_recyclerview.setItemAnimator(new DefaultItemAnimator());
        image_preview_recyclerview.setHasFixedSize(true);
        image_preview_recyclerview.setNestedScrollingEnabled(false);
        image_preview_recyclerview.setFocusable(false);
        image_preview_recyclerview.setAdapter(fullImageAdapter);

        if(OnOffType.equalsIgnoreCase("Online")){
            if (Utils.isOnline()) {
                getOnlineImage();
            }
        }
        else {
            new fetchImagetask().execute();
        }

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

            final String dcode = prefManager.getDistrictCode();
            final String bcode = prefManager.getBlockCode();
            final String pvcode = prefManager.getPvCode();
            String id = "";

            if(OnOffType.equalsIgnoreCase("Offline")){
                id = getIntent().getStringExtra(AppConstant.KEY_ACTIVITY_ID);
                dbData.open();
                activityImage = new ArrayList<>();
                activityImage = dbData.selectImageActivity(dcode,bcode,pvcode,schedule_id,id,"");
            }

            Log.d("IMAGE_COUNT", String.valueOf(activityImage.size()));
            return activityImage;
        }

        @Override
        protected void onPostExecute(final ArrayList<ODFMonitoringListValue> imageList) {
            super.onPostExecute(imageList);
            setAdapter();
        }
    }

    public void setAdapter(){
        fullImageAdapter = new FullImageAdapter(FullImageActivity.this,
                activityImage, dbData);
        image_preview_recyclerview.addOnItemTouchListener(new FullImageAdapter.RecyclerTouchListener(getApplicationContext(), image_preview_recyclerview, new FullImageAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", activityImage);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        image_preview_recyclerview.setAdapter(fullImageAdapter);
    }

    public void getOnlineImage() {
        try {
            new ApiService(this).makeJSONObjectRequest("OnlineImage", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), ImagesJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject ImagesJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), ImagesListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("utils_ImageEncrydataSet", "" + authKey);
        return dataSet;
    }

    public JSONObject ImagesListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ACTIVITY_IMAGE_VIEW);
        dataSet.put(AppConstant.KEY_SCHEDULE_ID,schedule_id);
        dataSet.put(AppConstant.KEY_SCHEDULE_ACTIVITY_ID, getIntent().getStringExtra(AppConstant.KEY_SCHEDULE_ACTIVITY_ID));
        Log.d("utils_imageDataset", "" + dataSet);
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("OnlineImage".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    generateImageArrayList(jsonObject.getJSONArray(AppConstant.JSON_DATA));
                }
                Log.d("resp_OnlineImage", "" + responseDecryptedBlockKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void generateImageArrayList(JSONArray jsonArray){
        if(jsonArray.length() > 0){
            activityImage = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++ ) {
                try {
                    ODFMonitoringListValue imageOnline = new ODFMonitoringListValue();
                    imageOnline.setImageRemark(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE_REMARK));
                    imageOnline.setActivityName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ACTIVITY_NAME));
                    imageOnline.setDateTime(jsonArray.getJSONObject(i).getString("ins_date"));
                    imageOnline.setType(jsonArray.getJSONObject(i).getString("activity_type"));

                    byte[] decodedString = Base64.decode(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    imageOnline.setImage(decodedByte);

                    activityImage.add(imageOnline);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            setAdapter();
        }
    }

    public void homePage() {
        Intent intent = new Intent(this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Home", "Home");
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
