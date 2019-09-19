package com.nic.ODFPlusMonitoring.Adapter;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nic.ODFPlusMonitoring.Activity.PendingScreen;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PendingScreenAdapter extends RecyclerView.Adapter<PendingScreenAdapter.MyViewHolder> {

    private com.nic.ODFPlusMonitoring.DataBase.dbData dbData;
    private Context context;
    private List<ODFMonitoringListValue> pendingListValues;
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject datasetActivity = new JSONObject();

    public PendingScreenAdapter(Context context, List<ODFMonitoringListValue> pendingListValues, dbData dbData) {
        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context = context;
        this.pendingListValues = pendingListValues;
        this.dbData = dbData;
        prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_screen_adapter, parent, false);
        return new PendingScreenAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView activity_name, village_tv;
        private RelativeLayout upload;

        public MyViewHolder(View itemView) {
            super(itemView);
            activity_name = (TextView) itemView.findViewById(R.id.activity_name);
            village_tv = (TextView) itemView.findViewById(R.id.village_name);
            upload = (RelativeLayout) itemView.findViewById(R.id.upload);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.village_tv.setText(String.valueOf(pendingListValues.get(position).getPvName()));
        holder.activity_name.setText(String.valueOf(pendingListValues.get(position).getActivityName()));
        final String dcode = pendingListValues.get(position).getDistictCode();
        final String bcode = pendingListValues.get(position).getBlockCode();
        final String pvcode = pendingListValues.get(position).getPvCode();
        final Integer scheduleId = pendingListValues.get(position).getScheduleId();
        final Integer activityId = pendingListValues.get(position).getActivityId();
        final Integer scheduleMasterId = pendingListValues.get(position).getScheduleMasterId();

        holder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline()) {
                    ODFMonitoringListValue roadListValue = new ODFMonitoringListValue();
                    roadListValue.setDistictCode(dcode);
                    roadListValue.setBlockCode(bcode);
                    roadListValue.setPvCode(pvcode);
                    roadListValue.setScheduleId(scheduleId);
                    roadListValue.setActivityId(activityId);
//                    prefManager.setDistrictCode(dcode);
//                    prefManager.setBlockCode(bcode);
//                    prefManager.setPvCode(pvcode);
//                    prefManager.setKeyPmgsyHabcode(String.valueOf(scheduleId));
//                    prefManager.setKeyPmgsyHabcode(String.valueOf(activityId));
//                    prefManager.setKeyPmgsyHabcode(String.valueOf(scheduleMasterId));
//                    prefManager.setKeyPmgsyDeleteId(String.valueOf(position));
                    prefManager.setDeletedkeyList(roadListValue);
                   new toUploadActivityTask().execute(roadListValue);
                } else {
                    Activity activity = (Activity) context;
                    Utils.showAlert(activity, "Turn On Mobile Data To Synchronize!");
                }
            }
        });

    }

    public class toUploadActivityTask extends AsyncTask<ODFMonitoringListValue, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(ODFMonitoringListValue... values) {
            try {
                dbData.open();
                ArrayList<ODFMonitoringListValue> saveActivityLists = dbData.getSavedActivity("upload",values[0]);
                JSONArray saveAcivityArray = new JSONArray();
                if (saveActivityLists.size() > 0) {
                    for (int i = 0; i < saveActivityLists.size(); i++) {
                        JSONObject activityJson = new JSONObject();
                        activityJson.put(AppConstant.KEY_MOTIVATOR_ID, saveActivityLists.get(i).getMotivatorId());
                        activityJson.put(AppConstant.KEY_SCHEDULE_ID, saveActivityLists.get(i).getScheduleId());
                        activityJson.put(AppConstant.KEY_ACTIVITY_ID, saveActivityLists.get(i).getActivityId());
                        activityJson.put(AppConstant.KEY_SCHEDULE_MASTER_ID, saveActivityLists.get(i).getScheduleMasterId());
                        activityJson.put(AppConstant.DISTRICT_CODE, String.valueOf(saveActivityLists.get(i).getDistictCode()));
                        activityJson.put(AppConstant.BLOCK_CODE, saveActivityLists.get(i).getBlockCode());
                        activityJson.put(AppConstant.PV_CODE, saveActivityLists.get(i).getPvCode());
                        activityJson.put(AppConstant.KEY_LATITUDE, saveActivityLists.get(i).getLatitude());
                        activityJson.put(AppConstant.KEY_LONGITUDE, saveActivityLists.get(i).getLongitude());
                        activityJson.put(AppConstant.KEY_TYPE, saveActivityLists.get(i).getType());
                        activityJson.put(AppConstant.KEY_DATE_TIME, saveActivityLists.get(i).getDateTime());
                        activityJson.put(AppConstant.KEY_IMAGE_REMARK, saveActivityLists.get(i).getImageRemark());
                        activityJson.put(AppConstant.KEY_SERIAL_NUMBER, saveActivityLists.get(i).getSerialNo());

                        Bitmap bitmap = saveActivityLists.get(i).getImage();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        byte[] imageInByte = baos.toByteArray();
                        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                        activityJson.put(AppConstant.KEY_IMAGE,image_str);

                        saveAcivityArray.put(activityJson);
                    }
                }

                datasetActivity = new JSONObject();
                try {
                    datasetActivity.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ACTIVITY_IMAGE_SAVE);
                    datasetActivity.put(AppConstant.KEY_TRACK_DATA, saveAcivityArray);

                    String authKey = datasetActivity.toString();
                    int maxLogSize = 2000;
                    for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > authKey.length() ? authKey.length() : end;
                        Log.v("to_send_plain", authKey.substring(start, end));
                    }

                    String authKey1 = Utils.encrypt(prefManager.getUserPassKey(), context.getResources().getString(R.string.init_vector), datasetActivity.toString());

                    for(int i = 0; i <= authKey1.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i+1) * maxLogSize;
                        end = end > authKey.length() ? authKey1.length() : end;
                        Log.v("to_send_encryt", authKey1.substring(start, end));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return datasetActivity;
        }

        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            ((PendingScreen)context).syncData(dataset);
        }
    }
    @Override
    public int getItemCount() {
        return pendingListValues.size();
    }

}

