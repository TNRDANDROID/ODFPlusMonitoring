package com.nic.ODFPlusMonitoring.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.nic.ODFPlusMonitoring.Activity.ActivityScreen;
import com.nic.ODFPlusMonitoring.Activity.AddParticipantsActivity;
import com.nic.ODFPlusMonitoring.Activity.CameraScreen;
import com.nic.ODFPlusMonitoring.Activity.FullImageActivity;
import com.nic.ODFPlusMonitoring.Activity.PendingScreen;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Dialog.MyDialog;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.MyViewHolder> {

    private final dbData dbData;
    private Context context;
    private List<ODFMonitoringListValue> activityListValues;
    private PrefManager prefManager;
    public final String dcode,bcode,pvcode;
    Dialog dialog;
    int pageNumber;
    // for Audio record
    MediaPlayer mediaPlayer ;
    ImageView start_record,stop_record,close,anim_img;
    TextView cancel_audio,submit_audio,pop_rec_msg;
    RelativeLayout anim_layout;
    Boolean isRecording=false;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    int pass,duration,due;
    private Handler mHandler;
    private Runnable mRunnable;
    Animation animation;
    Chronometer chronometer;
    int ClickedPosition;
    AlertDialog add_cpts_search_alert;
    Boolean isOpen=false;
    LinearLayout audio_layout;
    RelativeLayout submit_layout;
    Activity activity;


    public ActivityListAdapter(Activity activity,Context context, List<ODFMonitoringListValue> activityListValues, dbData dbData) {
        this.context = context;
        this.activity = activity;
        this.activityListValues = activityListValues;
        this.dbData = dbData;
        prefManager = new PrefManager(context);
        dcode = prefManager.getDistrictCode();
        bcode = prefManager.getBlockCode();
        pvcode = prefManager.getPvCode();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity, parent, false);
        return new MyViewHolder(itemView);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView activity_name, view_online_images, view_offline_images, activity_type_name;
        private RelativeLayout start_layout, end_layout,multiple_photo_layout;
        private LinearLayout schedule;
        private ImageView sportsImage,pdfImage,audio_record,audio_play;
        RelativeLayout activityLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            sportsImage = (ImageView)itemView.findViewById(R.id.sportsImage);
            audio_record = (ImageView)itemView.findViewById(R.id.audio_record);
            audio_play = (ImageView)itemView.findViewById(R.id.audio_play);
            pdfImage = (ImageView)itemView.findViewById(R.id.pdfDoc);
            activity_name = (MyCustomTextView) itemView.findViewById(R.id.activity_name);
            activity_type_name = (MyCustomTextView) itemView.findViewById(R.id.activity_type_name);
            view_online_images = (MyCustomTextView) itemView.findViewById(R.id.view_online_images);
            view_offline_images = (MyCustomTextView) itemView.findViewById(R.id.view_offline_images);
            start_layout = (RelativeLayout) itemView.findViewById(R.id.start_layout);
            end_layout = (RelativeLayout) itemView.findViewById(R.id.end_layout);
            multiple_photo_layout = (RelativeLayout) itemView.findViewById(R.id.multiple_photo_layout);
            activityLayout = (RelativeLayout) itemView.findViewById(R.id.activity_layout);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String activity_status = activityListValues.get(position).getActivityStatus();
        holder.activity_name.setText(activityListValues.get(position).getActivityName());
//        holder.activity_type_name.setText(activityListValues.get(position).getActivityTypeName());
        holder.activity_type_name.setText(activityListValues.get(position).getActivityTypeName()+" ("+indianCurrency(Double.parseDouble(activityListValues.get(position).getActivity_amount()))+")");

        holder.audio_record.setVisibility(View.GONE);
        if(activityListValues.get(position).getActivityTypeName().toLowerCase().contains("general")){
            holder.activity_type_name.setTextColor(context.getResources().getColor(R.color.dot_dark_screen5));
        }else {
            holder.activity_type_name.setTextColor(context.getResources().getColor(R.color.dot_dark_screen1));
        }
        if(activityListValues.get(position).getActivityName().length() > 35) {
            Utils.addReadMore(context, activityListValues.get(position).getActivityName(), holder.activity_name, 0);
        }

       /* if(activityListValues.get(position).getActivity_desc_audio_available().equalsIgnoreCase("Y")){
            holder.audio_play.setVisibility(View.VISIBLE);
        }else {
            holder.audio_play.setVisibility(View.GONE);
        }
        if(activityListValues.get(position).getActivity_desc_doc_available().equalsIgnoreCase("Y")){
            holder.pdfImage.setVisibility(View.VISIBLE);
        }else {
            holder.pdfImage.setVisibility(View.GONE);
        }*/

        if(position %2 == 1)
        {
            holder.sportsImage.setImageResource(R.drawable.img_cycling);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.sportsImage.setImageResource(R.drawable.img_golf);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        holder.start_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity_status.equalsIgnoreCase("y")) {
                    final String activity_id = String.valueOf(activityListValues.get(position).getActivityId());
                    final String schedule_id = String.valueOf(activityListValues.get(position).getScheduleId());

                    ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "Start");

                    if (activityImage.size() > 0) {
                        for (int i = 0; i < activityImage.size(); i++) {
                            if (activityImage.get(i).getType().equalsIgnoreCase("start")) {
                                Utils.showAlert((Activity) context, "Already Captured");
                            } else {
                                cameraScreen(position, "Start", "1");
                            }
                        }
                    } else {
                        cameraScreen(position, "Start", "1");
                    }


                } else {
                    Utils.showAlert((Activity) context, "Activity Completed");
                }
            }
        });

        holder.end_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity_status.equalsIgnoreCase("y")) {

                    final String activity_id = String.valueOf(activityListValues.get(position).getActivityId());
                    final String schedule_id = String.valueOf(activityListValues.get(position).getScheduleId());
                    ArrayList<ODFMonitoringListValue> activityImage1 = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "Start");

                    if (activityImage1.size() <= 0) {
                        Utils.showAlert((Activity) context, "Please Capture Image for Start Activity");
                    } else {
                        String StartTime=activityImage1.get(0).getDateTime();
                        System.out.println("StartTime >>"+StartTime);
                        boolean flag=Utils.duration(StartTime,activityListValues.get(position).getActivity_duration());

                        ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "End");

                        if (activityImage.size() > 0) {
                            for (int i = 0; i < activityImage.size(); i++) {
                                if (activityImage.get(i).getType().equalsIgnoreCase("end")) {
                                    Utils.showAlert((Activity) context, "Already Captured");
                                } else {
                                    if(flag){
                                        cameraScreen(position, "End", "1");
                                    }else {
                                        Utils.showAlert((Activity) context, "Activity time not completed!");
                                    }

                                }
                            }
                        } else {
                            if(flag){
                                cameraScreen(position, "End", "1");
                            }else {
                                Utils.showAlert((Activity) context, "Activity time not completed!");
                            }
                        }
                    }
                }else {
                        Utils.showAlert((Activity) context, "Activity Completed");
                    }
                }


        });

        dbData.open();
        final String activity_id = String.valueOf(activityListValues.get(position).getActivityId());
        final String schedule_id = String.valueOf(activityListValues.get(position).getScheduleId());
        final String schedule_activity_id = String.valueOf(activityListValues.get(position).getScheduleActivityId());

        ArrayList<ODFMonitoringListValue> activityImageOffline = dbData.selectImageActivity(dcode,bcode,pvcode,schedule_id,activity_id,"");

        if(activityImageOffline.size() > 0) {
            holder.view_offline_images.setVisibility(View.VISIBLE);
        }
        else {
            holder.view_offline_images.setVisibility(View.GONE);
        }

        holder.view_offline_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOfflineImages(schedule_id,activity_id,"Offline");
            }
        });
        dbData.open();
        ArrayList<ODFMonitoringListValue> activityImageOnline = dbData.selectActivityPhoto(dcode,bcode,pvcode,schedule_id,schedule_activity_id);

        if(activityImageOnline.size() > 0) {
            holder.view_online_images.setVisibility(View.VISIBLE);
//            for (int k = 0;k<activityImageOnline.size();k++) {
//                if(activityListValues.get(position).getImageAvailable().equalsIgnoreCase("Y")){
//                    holder.view_online_images.setVisibility(View.VISIBLE);
//                }
//                else {
//
//                }
//            }
        }else {
            holder.view_online_images.setVisibility(View.GONE);
        }

        holder.view_online_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOfflineImages(schedule_id,schedule_activity_id,"Online");
            }
        });
        holder.audio_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord(position);
            }
        });
        holder.audio_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityScreen)context).getFile("97"/*activity_id*/,"audio",activityListValues.get(position).getActivityName());

                /*String url= "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
                String url1= "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3";
                String url2= "http://10.163.19.140/mp3_audio/kalimba.mp3";
                Utils.playAudio(activity,url2);*/
            }
        });
        holder.pdfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityScreen)context).getFile("97"/*activity_id*/,"doc",activityListValues.get(position).getActivityName());

               /* String URL ="https://www.orimi.com/pdf-test.pdf";
                String URL2 ="http://www.africau.edu/images/default/sample.pdf";
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL2)));*/
            }
        });

        final Integer no_of_photos = activityListValues.get(position).getNoOfPhotos();

        if(no_of_photos > 2) {
            holder.multiple_photo_layout.setVisibility(View.VISIBLE);
        }
        else {
            holder.multiple_photo_layout.setVisibility(View.GONE);
        }

        holder.multiple_photo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity_status.equalsIgnoreCase("y")) {

                        ArrayList<ODFMonitoringListValue> activityImage1 = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "Start");

                        if (activityImage1.size() <= 0) {
                            Utils.showAlert((Activity) context, "Please Capture Image for Start Activity");
                        } else {
                            ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "Middle");

                            if (activityImage.size() > 0 && activityImage.size() == (no_of_photos - 2)) {
                                Utils.showAlert((Activity) context, "Limit Exceed");
                            } else if (activityImage.size() == 0 ) {
                                System.out.println("serialNo"+String.valueOf(activityImage.size() + 1));
                                cameraScreen(position, "Middle", String.valueOf(activityImage.size() + 1));
                            }else {
                                System.out.println("serialNo"+String.valueOf(activityImage.get(activityImage.size()-1).getSerialNo() + 1));
                                cameraScreen(position, "Middle", String.valueOf(activityImage.get(activityImage.size()-1).getSerialNo() + 1));
                            }
                        }

                    }
                else {
                    Utils.showAlert((Activity) context, "Activity Completed");
                }
            }
        });



        if(activity_status.equalsIgnoreCase("y")) {
//            for ( int i = 0; i < holder.activityLayout.getChildCount();  i++ ){
//                View view = holder.activityLayout.getChildAt(i);
//                view.setEnabled(false); // Or whatever you want to do with the view.
//            }
//            holder.start_layout.setEnabled(false);
//            holder.end_layout.setEnabled(false);
//            holder.multiple_photo_layout.setEnabled(false);
        }
        else if(activity_status.equalsIgnoreCase("N")){
            holder.start_layout.setEnabled(true);
            holder.end_layout.setEnabled(true);
            holder.multiple_photo_layout.setEnabled(true);
        }

        holder.activityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity_status.equalsIgnoreCase("y")) {
                   Utils.showAlert((Activity) context,"Activity Completed");
                }
            }
        });
    }

    public void viewOfflineImages(String schedule_id,String id,String OnOffType) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra(AppConstant.KEY_SCHEDULE_ID,schedule_id);
        intent.putExtra("OnOffType",OnOffType);
        if(OnOffType.equalsIgnoreCase("Offline")){
            intent.putExtra(AppConstant.KEY_ACTIVITY_ID,id);
            activity.startActivity(intent);
        }
        else if(OnOffType.equalsIgnoreCase("Online")) {
            intent.putExtra(AppConstant.KEY_SCHEDULE_ACTIVITY_ID,id);
            if(Utils.isOnline()){
                activity.startActivity(intent);
            }else {
                Utils.showAlert(activity,"Your Internet seems to be Offline.Images can be viewed only in Online mode.");
            }
        }


        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    @Override
    public int getItemCount() {
        return activityListValues.size();
    }

    public void cameraScreen(int pos,String type,String serial_number){
        Activity activity = (Activity) context;
        Intent intent = new Intent(context,CameraScreen.class);
        intent.putExtra(AppConstant.KEY_TYPE, type);
        intent.putExtra(AppConstant.KEY_SERIAL_NUMBER, serial_number);
        intent.putExtra(AppConstant.KEY_ACTIVITY_ID, String.valueOf(activityListValues.get(pos).getActivityId()));
        intent.putExtra(AppConstant.KEY_SCHEDULE_ID, String.valueOf(activityListValues.get(pos).getScheduleId()));
        intent.putExtra(AppConstant.KEY_ACTIVITY_NAME, activityListValues.get(pos).getActivityName());
        intent.putExtra(AppConstant.KEY_PURPOSE, "Insert");
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    //For Activity Audio
    private void startRecord(final int position) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            final LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View edit_cpt_list_layout = inflater.inflate(R.layout.pop_up_audio_recorder, null);
            start_record = (ImageView) edit_cpt_list_layout.findViewById(R.id.start_record);
            stop_record = (ImageView) edit_cpt_list_layout.findViewById(R.id.stop_record);
            anim_img = (ImageView) edit_cpt_list_layout.findViewById(R.id.anim_img);
            anim_layout = (RelativeLayout) edit_cpt_list_layout.findViewById(R.id.anim_layout);
            pop_rec_msg = (TextView) edit_cpt_list_layout.findViewById(R.id.pop_msg);
            close = (ImageView) edit_cpt_list_layout.findViewById(R.id.close);
            submit_audio=(TextView)edit_cpt_list_layout.findViewById(R.id.submit_test);
            cancel_audio=(TextView)edit_cpt_list_layout.findViewById(R.id.cancel_test);
            audio_layout=(LinearLayout) edit_cpt_list_layout.findViewById(R.id.audio_layout);
            submit_layout=(RelativeLayout) edit_cpt_list_layout.findViewById(R.id.submit_layout);
            chronometer = (Chronometer) edit_cpt_list_layout.findViewById(R.id.chronometerTimer);
            chronometer.setBase(SystemClock.elapsedRealtime());
            close.setVisibility(View.VISIBLE);
            submit_audio.setEnabled(true);
            submit_layout.setVisibility(View.GONE);
            submit_audio.setVisibility(View.GONE);
            audio_layout.setVisibility(View.GONE);
            final ImageView play_btn = (ImageView) edit_cpt_list_layout.findViewById(R.id.play_btn);
            final ImageView stop_btn = (ImageView) edit_cpt_list_layout.findViewById(R.id.stop_btn);
            final SeekBar seekbar = (SeekBar) edit_cpt_list_layout.findViewById(R.id.seekbar);
            final TextView mPass = (TextView) edit_cpt_list_layout.findViewById(R.id.mPass);
            final TextView mDuration = (TextView) edit_cpt_list_layout.findViewById(R.id.mDuration);
            final TextView mDue = (TextView) edit_cpt_list_layout.findViewById(R.id.mDue);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setView(edit_cpt_list_layout);
            dialogBuilder.setCancelable(true);
            add_cpts_search_alert = dialogBuilder.create();
            add_cpts_search_alert.show();
            add_cpts_search_alert.setCanceledOnTouchOutside(false);
            add_cpts_search_alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            add_cpts_search_alert.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
            stop_record.setEnabled(false);
            isOpen=true;
            random = new Random();
            start_record.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(checkPermission()) {
                        close.setVisibility(View.GONE);
                        add_cpts_search_alert.setCancelable(false);
                        isRecording=true;
                        isOpen=false;
                        AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp";
                        MediaRecorderReady();
                        try {

                            mediaRecorder.prepare();
                            mediaRecorder.start();

                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        ClickedPosition=position;
                        start_record.setVisibility(View.GONE);
                        stop_record.setVisibility(View.VISIBLE);
                        anim_layout.setVisibility(View.VISIBLE);
                        pop_rec_msg.setText("Stop Record");
                        animation = AnimationUtils.loadAnimation(context, R.anim.blink);
                        anim_img.startAnimation(animation);
                        start_record.setEnabled(false);
                        stop_record.setEnabled(true);

                    } else {
                        requestPermission();
                    }

                }
            });
            stop_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StopRecording(position);
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stop_record.setEnabled(true);
                    add_cpts_search_alert.dismiss();
                    isOpen=false;
                    stopPlaying();
                }
            });
            submit_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flagDialog(context,activity, "Are you sure to submit audio file?", position);
                }
            });

            cancel_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stop_record.setEnabled(true);
                    add_cpts_search_alert.dismiss();
                    isOpen=false;
                    stopPlaying();
                }
            });

            play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit_audio.setEnabled(false);
                    add_cpts_search_alert.setCancelable(false);
                    stop_btn.setVisibility(View.VISIBLE);
                    play_btn.setVisibility(View.GONE);
                    startPlaying();
                }

                private void startPlaying() {
                    if(mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

                    mDue.setVisibility(View.GONE);
                    mDuration.setVisibility(View.VISIBLE);
                    mPass.setVisibility(View.VISIBLE);
                    audio_layout.setEnabled(false);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try{
                        mediaPlayer.setDataSource(AudioSavePathInDevice);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        getAudioStats();
                        initializeSeekBar();
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }catch (SecurityException e){
                        e.printStackTrace();
                    }catch (IllegalStateException e){
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            stopPlaying();
                            submit_audio.setEnabled(true);
                            stop_btn.setVisibility(View.GONE);
                            play_btn.setVisibility(View.VISIBLE);
                            audio_layout.setEnabled(true);
                            add_cpts_search_alert.setCancelable(true);
                        }

                    });
                }

                private void initializeSeekBar() {
                    seekbar.setMax(mediaPlayer.getDuration()/1000);
                    mHandler = new Handler();
                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayer!=null){
                                int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000; // In milliseconds
                                seekbar.setProgress(mCurrentPosition);
                                getAudioStats();
                            }
                            mHandler.postDelayed(mRunnable,1000);
                        }
                    };
                    mHandler.postDelayed(mRunnable,1000);
                }

                private void getAudioStats() {
                    duration  = mediaPlayer.getDuration()/1000; // In milliseconds
                    due = (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition())/1000;
                    pass = duration - due;
                    mPass.setText("" + pass + " secs");
                    mDuration.setText("" + duration + " secs");
                    mDue.setText("" + due + " secs");
                }
            });

            stop_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit_audio.setEnabled(true);
                    stop_btn.setVisibility(View.GONE);
                    play_btn.setVisibility(View.VISIBLE);
                    StopPlaying();
                    add_cpts_search_alert.setCancelable(true);
                }

                private void StopPlaying() {
                    if(mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        MediaRecorderReady();
                        mediaPlayer = null;
                        if(mHandler!=null){
                            mHandler.removeCallbacks(mRunnable);
                        }
                    }

                }
            });
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if(mediaPlayer!=null && b){
                        mediaPlayer.seekTo(i*1000);
                    } }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
    private String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
            i++ ;
        }
        return stringBuilder.toString();
    }
    private void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
        System.out.println("AudioSavePathInDevice" + AudioSavePathInDevice);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void stopPlaying() {
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            MediaRecorderReady();
            mediaPlayer = null;
            if(mHandler!=null){
                mHandler.removeCallbacks(mRunnable);
            }
        }
    }

    private void StopRecording(int position) {
        if(!chronometer.getText().toString().equals("00:00")){
            isRecording=false;
            if(animation!=null){
                anim_img.getAnimation().cancel();
                anim_img.clearAnimation();
                animation.setAnimationListener(null);
            }else {}

            stop_record.setImageResource(R.drawable.stop_audio_record);
            pop_rec_msg.setVisibility(View.GONE);
            anim_layout.setVisibility(View.GONE);
            isOpen=false;
            close.setVisibility(View.VISIBLE);
            submit_audio.setVisibility(View.VISIBLE);
            cancel_audio.setVisibility(View.VISIBLE);
            submit_layout.setVisibility(View.VISIBLE);
            audio_layout.setVisibility(View.VISIBLE);
            mediaRecorder.stop();
            chronometer.stop();
            stop_record.setEnabled(false);
            start_record.setEnabled(true);
            add_cpts_search_alert.setCancelable(true);
        }
    }

    public  void flagDialog(Context context, final Activity activity, String message, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                stop_record.setEnabled(true);
                add_cpts_search_alert.dismiss();
                isOpen=false;
                if(!chronometer.getText().toString().equals("00:00")){
                    ODFMonitoringListValue odfMonitoringListValue = activityListValues.get(position);
                    odfMonitoringListValue.setActivityAudio(AudioSavePathInDevice);
                    odfMonitoringListValue.setAudioSize(chronometer.getText().toString());
                    activityListValues.set(position,odfMonitoringListValue);
                    notifyDataSetChanged();}
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
    public String indianCurrency(double money){
        String format = NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(money);
        System.out.println(format);
        return format;
    }

}
