package com.nic.ODFPlusMonitoring.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nic.ODFPlusMonitoring.Activity.CameraScreen;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.dbData;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import java.util.ArrayList;


public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<ODFMonitoringListValue> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDescription,lblDate,lblType;
    private RelativeLayout editImageViewLayout;
    private int selectedPosition = 0;
    private String dcode,bcode,pvcode,activity_id,schedule_id;
    private PrefManager prefManager;
    public dbData dbData;
    private Context context;

    public static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        prefManager = new PrefManager(getActivity());
        this.context = getActivity();
        dbData = new dbData(getActivity());
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        editImageViewLayout = (RelativeLayout) v.findViewById(R.id.edit_image_layout);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDescription = (TextView) v.findViewById(R.id.description);
        lblType = (TextView) v.findViewById(R.id.type);
        lblDate = (TextView) v.findViewById(R.id.date);
//        if (getArguments() != null) {
//            images = (ArrayList<ODFMonitoringListValue>) getArguments().getSerializable("images");
//        }

        images = prefManager.getLocalSaveHaccpList();
        selectedPosition = getArguments().getInt("position");
        dcode = getArguments().getString(AppConstant.DISTRICT_CODE);
        bcode = getArguments().getString(AppConstant.BLOCK_CODE);
        pvcode = getArguments().getString(AppConstant.PV_CODE);
        activity_id = getArguments().getString(AppConstant.KEY_ACTIVITY_ID);
        schedule_id = getArguments().getString(AppConstant.KEY_SCHEDULE_ID);


        Log.i(TAG, "position: " + selectedPosition);
        Log.i(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());

        final ODFMonitoringListValue image = images.get(position);
        lblTitle.setText(image.getActivityName());
        if(!image.getImageRemark().equalsIgnoreCase("")){
            lblDescription.setVisibility(View.VISIBLE);
            lblDescription.setText(image.getImageRemark());
        }else{
            lblDescription.setVisibility(View.GONE);
        }
        lblType.setText(image.getType());
        String date = Utils.parseDateForChart(image.getDateTime());
        lblDate.setText(date);
        if(getArguments().getString("OnOffType").equalsIgnoreCase("Online")){
            editImageViewLayout.setVisibility(View.GONE);
        }
        else {
            editImageViewLayout.setVisibility(View.VISIBLE);
        }
        editImageViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbData.open();
                ArrayList<ODFMonitoringListValue> activityImage = dbData.selectImageActivity(dcode, bcode, pvcode, schedule_id, activity_id, "End");
                if (activityImage.size() > 0) {
                    Utils.showAlert(getActivity(), "Your edit option is closed because you are captured end photo!");
                } else {
                    Context context = v.getContext();
                    Integer photoId = image.getPhotoID();
                    Intent intent = new Intent(context, CameraScreen.class);
                    intent.putExtra(AppConstant.DISTRICT_CODE, dcode);
                    intent.putExtra(AppConstant.BLOCK_CODE, bcode);
                    intent.putExtra(AppConstant.PV_CODE, pvcode);
                    intent.putExtra(AppConstant.KEY_ACTIVITY_ID, activity_id);
                    intent.putExtra(AppConstant.KEY_SCHEDULE_ID, schedule_id);
                    intent.putExtra(AppConstant.KEY_PURPOSE, "Update");
                    intent.putExtra(AppConstant.KEY_PHOTO_ID, String.valueOf(photoId));
                    Log.d("dcode", "" + dcode + bcode + pvcode + activity_id + schedule_id + photoId);
                    Log.d("photoId", "" + photoId);
                    context.startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            ODFMonitoringListValue image = images.get(position);

            Glide.with(getActivity()).load(image.getImage())
                    .thumbnail(0.5f)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//    }
}
