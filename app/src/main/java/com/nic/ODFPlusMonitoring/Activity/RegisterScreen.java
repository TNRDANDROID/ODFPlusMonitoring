package com.nic.ODFPlusMonitoring.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Adapter.AutoSuggestAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Support.MyEditTextView;
import com.nic.ODFPlusMonitoring.Support.ProgressHUD;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.BANKLIST_BRANCH_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.BANKLIST_TABLE_NAME;

/**
 * Created by AchanthiSundar on 28-12-2018.
 */

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private Button btn_register;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private MyEditTextView motivator_name, motivator_address, motivator_mobileNO;
    private AppCompatAutoCompleteTextView motivator_bank_tv, motivator_account_tv, motivator_branch_tv;
    private MyCustomTextView motivator_ifsc_tv;

    private Spinner sp_block,sp_district, sp_village;
    private PrefManager prefManager;
    private List<ODFMonitoringListValue> Block = new ArrayList<>();
    private List<ODFMonitoringListValue> District = new ArrayList<>();
    private List<ODFMonitoringListValue> Village = new ArrayList<>();
    private List<ODFMonitoringListValue> BankDetails = new ArrayList<>();
    private List<ODFMonitoringListValue> BranchDetails = new ArrayList<>();
    private ProgressHUD progressHUD;
    private AutoSuggestAdapter autoSuggestAdapter;
    private ImageView arrowImage,arrowImageUp;
    private ScrollView  scrollView;

    String pref_Block,pref_district, pref_Village;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject jsonObject;
    List<String> array = new ArrayList<String>();
    List<String> brancharray = new ArrayList<String>();
    private Animation animation;
private LinearLayout childlayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.fragment_signup);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        btn_register = (Button) findViewById(R.id.btn_register);
        motivator_name = (MyEditTextView) findViewById(R.id.motivator_name);
        motivator_address = (MyEditTextView) findViewById(R.id.motivator_address);
        motivator_mobileNO = (MyEditTextView) findViewById(R.id.motivator_mobile_no);
        motivator_account_tv = (AppCompatAutoCompleteTextView) findViewById(R.id.motivator_account_tv);
        motivator_bank_tv = (AppCompatAutoCompleteTextView) findViewById(R.id.motivator_bank_tv);
        motivator_branch_tv = (AppCompatAutoCompleteTextView) findViewById(R.id.motivator_branch_tv);
        motivator_ifsc_tv = (MyCustomTextView) findViewById(R.id.motivator_ifsc_tv);
        scrollView = (ScrollView)findViewById(R.id.scroll_view) ;
        arrowImage = (ImageView) findViewById(R.id.arrow_image) ;
        arrowImageUp = (ImageView)findViewById(R.id.arrow_image_up) ;
        childlayout = (LinearLayout)findViewById(R.id.child_view);
        arrowImage.setOnClickListener(this);
        arrowImageUp.setOnClickListener(this);
        if (childlayout.getMeasuredHeight() > scrollView.getMeasuredHeight()) {
            showArrowImage();
        }
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        btn_register.setOnClickListener(this);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diffBottom = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                if (diffBottom == 0) {
                    arrowImage.setVisibility(View.GONE);
                    arrowImage.clearAnimation();
                } else if (scrollView.getScrollY() == 0) {
                    arrowImageUp.setVisibility(View.GONE);
                    arrowImageUp.clearAnimation();
                } else {
                    showArrowImage();
                    showUpArrowImage();
                }
            }
        });
//        getBankNameList();
//        getBankBranchList();
        autoCompleteApiBankName();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                validateMotivatorDetails();
                break;
            case R.id.arrow_image:
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                break;
            case R.id.arrow_image_up:
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                break;
        }
    }

    public void autoCompleteApiBankName() {
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        motivator_bank_tv.setThreshold(1);
        motivator_bank_tv.setAdapter(autoSuggestAdapter);
        motivator_bank_tv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        prefManager.setKeyAutocompleteSelectedBankName(motivator_bank_tv.getText().toString());
                        getId(BankDetails.get(position).getBank_Id());
                       Log.d("ODF",""+(motivator_bank_tv.getText().toString()));
                  //     Log.d("BANK_ID",""+BankDetails.get(position).getBank_Id());

//                        String selection = (String) parent.getItemAtPosition(position);
//                        int pos = -1;
//
//                        for (int i = 0; i < array.size(); i++) {
//                            if (array.get(i).equals(selection)) {
//                                pos = i;
//                                break;
//                            }
//                        }
//                        System.out.println("Position " + pos); //check it now in Logcat
                    }
                });

        motivator_bank_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                loadBankName(motivator_bank_tv.getText().toString());
//            handler.removeMessages(TRIGGER_AUTO_COMPLETE);
//            handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
//                    AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {
             }
        });
    }
    public void getId(int id){
        Log.d("branch_id",""+id);
        prefManager.setKeyAutocompleteSelectedBankID(id);
        autoCompleteApiBankBranchName();
    }

    public void autoCompleteApiBankBranchName() {
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        motivator_branch_tv.setThreshold(1);
        motivator_branch_tv.setAdapter(autoSuggestAdapter);
        motivator_branch_tv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

//                        String selection = (String) parent.getItemAtPosition(position);
//                        int pos = -1;
//
//                        for (int i = 0; i < array.size(); i++) {
//                            if (array.get(i).equals(selection)) {
//                                pos = i;
//                                break;
//                            }
//                        }
//                        System.out.println("Position " + pos); //check it now in Logcat
                    }
                });

        motivator_branch_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                loadBankBranchName(motivator_branch_tv.getText().toString());
//            handler.removeMessages(TRIGGER_AUTO_COMPLETE);
//            handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
//                    AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }



    //The method for opening the registration page and another processes or checks for registering
    private void validateMotivatorDetails() {

    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status = responseObj.getString(AppConstant.KEY_STATUS);
            String response = responseObj.getString(AppConstant.KEY_RESPONSE);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadBankName(String text) {
        String like_query = "SELECT * FROM " + BANKLIST_TABLE_NAME + " where bank_name LIKE '" + text + "%'";
        Log.d("AutoSearchQuery", "" + like_query);
        Cursor BankList = getRawEvents(like_query, null);
        array.clear();
        BankDetails.clear();
        if (BankList.getCount() > 0) {
            if (BankList.moveToFirst()) {
                do {
                    ODFMonitoringListValue odfMonitoringListValue = new ODFMonitoringListValue();
                    Integer bank_id = BankList.getInt(BankList.getColumnIndexOrThrow(AppConstant.BANK_ID));
                    String omc_name = BankList.getString(BankList.getColumnIndexOrThrow(AppConstant.OMC_NAME));
                    String bank_name = BankList.getString(BankList.getColumnIndexOrThrow(AppConstant.BANK_NAME));
                    odfMonitoringListValue.setBank_Id(bank_id);
                    odfMonitoringListValue.setOMC_Name(omc_name);
                    odfMonitoringListValue.setBank_Name(bank_name);
                    array.add(bank_name);
                    BankDetails.add(odfMonitoringListValue);
                } while (BankList.moveToNext());
            }
        }
        autoSuggestAdapter.setData(array);
        autoSuggestAdapter.notifyDataSetChanged();
    }

    public void loadBankBranchName(String text) {
        Integer  selectedBank_id = prefManager.getKeyAutocompleteSelectedBankID();
        String like_query = "SELECT * FROM " + BANKLIST_BRANCH_TABLE_NAME + " where bank_id = "+selectedBank_id+" and branch LIKE '" + text + "%'";
        Log.d("AutoSearchId", "" + like_query);
        Cursor BankList = getRawEvents(like_query, null);
        brancharray.clear();
        BranchDetails.clear();
        if (BankList.getCount() > 0) {
            if (BankList.moveToFirst()) {
                do {
                    ODFMonitoringListValue odfMonitoringListValue = new ODFMonitoringListValue();
                    Integer bank_id  = BankList.getInt(BankList
                            .getColumnIndexOrThrow(AppConstant.BANK_ID));
                    Integer branch_id  =BankList.getInt(BankList
                            .getColumnIndexOrThrow(AppConstant.BRANCH_ID));
                    String branch_name  =   BankList.getString(BankList
                            .getColumnIndexOrThrow(AppConstant.BRANCH_NAME));
                    String ifsc_code =   BankList.getString(BankList
                            .getColumnIndexOrThrow(AppConstant.IFSC_CODE));
                    odfMonitoringListValue.setBank_Id(bank_id);
                    odfMonitoringListValue.setBranch_Id(branch_id);
                    odfMonitoringListValue.setBranch_Name(branch_name);
                    odfMonitoringListValue.setIFSC_Code(ifsc_code);
                    brancharray.add(branch_name);
                    BranchDetails.add(odfMonitoringListValue);
                } while (BankList.moveToNext());
            }
        }
        autoSuggestAdapter.setData(brancharray);
        autoSuggestAdapter.notifyDataSetChanged();
    }

    public Cursor getRawEvents(String sql, String string) {
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
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

    public void showUpArrowImage() {
        arrowImageUp.setVisibility(View.VISIBLE);
        animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
        arrowImageUp.startAnimation(animation);
    }


    @Override
    public void OnError(VolleyError volleyError) {
        volleyError.printStackTrace();
        Utils.showAlert(this, "Login Again");
    }

}
