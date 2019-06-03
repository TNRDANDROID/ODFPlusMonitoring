package com.nic.ODFPlusMonitoring.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

public class HomePage extends AppCompatActivity implements Api.ServerResponseListener {
    private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        getMotivatorSchedule();
    }

    public void getMotivatorSchedule() {
        try {
            new ApiService(this).makeJSONObjectRequest("MotivatorSchedule", Api.Method.POST, UrlGenerator.getMotivatorSchedule(), motivatorScheduleListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject motivatorScheduleListJsonParams() throws JSONException {

        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.motivatorScheduleListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("villageListDistrictWise", "" + authKey);
        return dataSet;

    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
//            if ("MotivatorSchedule".equals(urlType) && responseObj != null) {
//                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
////                    new MotivatorCategoryList().execute(responseObj.getJSONArray(AppConstant.JSON_DATA));
//                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("NO_RECORD")) {
//                    Log.d("Record", responseObj.getString(AppConstant.KEY_MESSAGE));
//                }
//                Log.d("MotivatorSchedule", "" + responseObj.getJSONArray(AppConstant.JSON_DATA));
//            }
            if ("MotivatorSchedule".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    loadMotivatorScheduleList(jsonObject.getJSONArray(AppConstant.JSON_DATA));
                }
                Log.d("MotivatorSchedule", "" + responseDecryptedBlockKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadMotivatorScheduleList(JSONArray jsonArray) {
        ODFMonitoringListValue odfMonitoringListValue = new ODFMonitoringListValue();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String scheduleId = jsonArray.getJSONObject(i).getString(AppConstant.KEY_MOTIVATOR_SCHEDULE_ID);
                JSONObject tSchedulejsonObject = jsonArray.getJSONObject(i).getJSONObject(AppConstant.KEY_MOTIVATOR_T_SCHEDULE);
                odfMonitoringListValue.setScheduleMasterId(tSchedulejsonObject.getString(AppConstant.KEY_SCHEDULE_MOTIVATOR_MASTER_ID));
                odfMonitoringListValue.setMotivatorId(tSchedulejsonObject.getString(AppConstant.KEY_SCHEDULE_MOTIVATOR_ID));
                odfMonitoringListValue.setScheduleFromDate(tSchedulejsonObject.getString(AppConstant.KEY_SCHEDULE_MOTIVATOR_FROM_DATE));
                odfMonitoringListValue.setScheduletoDate(tSchedulejsonObject.getString(AppConstant.KEY_SCHEDULE_MOTIVATOR_TO_DATE));
                odfMonitoringListValue.setScheduleDescription(tSchedulejsonObject.getString(AppConstant.KEY_SCHEDULE_MOTIVATOR_DESCRIPTION));
                JSONArray innerjson = tSchedulejsonObject.getJSONArray(AppConstant.KEY_T_SCHEDULE_VILLAGE);
                for (int j = 0; j < innerjson.length(); j++) {
                    odfMonitoringListValue.setScheduleDCode(innerjson.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                    odfMonitoringListValue.setScheduleBCode(innerjson.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                    odfMonitoringListValue.setSchedulePvCode(innerjson.getJSONObject(i).getString(AppConstant.PV_CODE));
                    odfMonitoringListValue.setSchedulePvName(innerjson.getJSONObject(i).getString(AppConstant.PV_NAME));
                    Log.d("name",""+innerjson.getJSONObject(i).getString(AppConstant.PV_NAME));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void OnError(VolleyError volleyError) {

    }
}
