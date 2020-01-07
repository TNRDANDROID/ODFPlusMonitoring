package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewEditProfileScreen extends AppCompatActivity implements View.OnClickListener,Api.ServerResponseListener {
    private TextView back_img,motivator_Name,motivator_add,motivator_mob,motivator_email,motivator_dob,motivator_Name_tv,motivator_email_tv;
    private ImageView motivator_img,arrowImage,arrowImageUp;
    private CoordinatorLayout profile;
    private NestedScrollView nested_scroll;
    private PrefManager prefManager;
    private Animation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prefManager = new PrefManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        back_img = (TextView) findViewById(R.id.back_img);
        profile = (CoordinatorLayout) findViewById(R.id.pro_parent);
        motivator_Name = (TextView) findViewById(R.id.motivator_name);
        motivator_Name_tv = (TextView) findViewById(R.id.motivator_name_tv);
        motivator_email = (TextView) findViewById(R.id.motivator_email);
        motivator_email_tv = (TextView) findViewById(R.id.motivator_email_tv);
        motivator_dob = (TextView) findViewById(R.id.motivator_dob);
        motivator_add = (TextView) findViewById(R.id.motivator_address);
        motivator_mob = (TextView) findViewById(R.id.motivator_mob);
        arrowImage = (ImageView) findViewById(R.id.arrow_image);
        arrowImageUp = (ImageView) findViewById(R.id.arrow_image_up);
        motivator_img = (ImageView) findViewById(R.id.motivator_image);
        nested_scroll = (NestedScrollView) findViewById(R.id.nested_scroll);
        back_img.setOnClickListener(this);
        getMotivatorProfile();
        showArrowImage();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPress();
                break;
        }
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

    public static class fromDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        static Calendar cldr = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), this, year,
                    month, day);
            cldr.set(year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Do something with the date chosen by the user
//            motivator_dob_tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            String start_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            cldr.set(Calendar.YEAR, year);
            cldr.set(Calendar.MONTH, (monthOfYear));
            cldr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Log.d("startdate", "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }

    }

    public void getMotivatorProfile() {
        try {
            new ApiService(this).makeJSONObjectRequest("MotivatorProfile", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), motivatorProfileJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject motivatorProfileJsonParams() throws JSONException {

        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.motivatorProfileJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("motivatorProfile", "" + authKey);
        return dataSet;

    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            if ("MotivatorProfile".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                    generateImageArrayList(jsonObject.getJSONArray(AppConstant.JSON_DATA));
                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")) {

                    Utils.showAlert(this,"No Record Found!");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void generateImageArrayList(JSONArray jsonArray){
        if(jsonArray.length() > 0){

            for(int i = 0; i < jsonArray.length(); i++ ) {
                try {
                    String motivatorName = jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTIVATOR_NAME);
                    String motivatorAdd = jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTIVATOR_ADDRESS);
                    String motivatorMob = jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTIVATOR_MOBILE);
                    String motivatorMail = jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTIVATOR_EMAIL);
                    String motivatorDOB = jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTIVATOR_DOB);


                    byte[] decodedString = Base64.decode(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                   motivator_Name.setText(motivatorName);
                   motivator_Name_tv.setText(motivatorName);
                   motivator_add.setText(motivatorAdd);
                   motivator_mob.setText(motivatorMob);
                   motivator_email.setText(motivatorMail);
                   motivator_email_tv.setText(motivatorMail);
                   motivator_dob.setText(Utils.changeDate(motivatorDOB));

                   motivator_img.setImageBitmap(decodedByte);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    public void showArrowImage() {
        arrowImage.setVisibility(View.VISIBLE);
        animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
        arrowImage.startAnimation(animation);
    }



    @Override
    public void OnError(VolleyError volleyError) {

    }
}
