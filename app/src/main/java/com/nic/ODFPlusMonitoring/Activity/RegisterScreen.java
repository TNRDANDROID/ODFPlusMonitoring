package com.nic.ODFPlusMonitoring.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.nic.ODFPlusMonitoring.Adapter.AutoSuggestAdapter;
import com.nic.ODFPlusMonitoring.Adapter.CommonAdapter;
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
import java.util.Calendar;
import java.util.List;

import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.BANKLIST_BRANCH_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.BANKLIST_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.BLOCK_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.DISTRICT_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.VILLAGE_TABLE_NAME;

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
    private static MyCustomTextView motivator_dob_tv;
    private RelativeLayout dob_layout;
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
    List<Integer> banK_id_array = new ArrayList<Integer>();
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
        sp_block = (Spinner) findViewById(R.id.block);
        sp_district = (Spinner) findViewById(R.id.district);
        sp_village = (Spinner) findViewById(R.id.village);
        motivator_address = (MyEditTextView) findViewById(R.id.motivator_address);
        motivator_mobileNO = (MyEditTextView) findViewById(R.id.motivator_mobile_no);
        motivator_account_tv = (AppCompatAutoCompleteTextView) findViewById(R.id.motivator_account_tv);
        motivator_bank_tv = (AppCompatAutoCompleteTextView) findViewById(R.id.motivator_bank_tv);
        motivator_branch_tv = (AppCompatAutoCompleteTextView) findViewById(R.id.motivator_branch_tv);
        motivator_ifsc_tv = (MyCustomTextView) findViewById(R.id.motivator_ifsc_tv);
        motivator_dob_tv = (MyCustomTextView) findViewById(R.id.motivator_dob_tv);
        scrollView = (ScrollView)findViewById(R.id.scroll_view) ;
        arrowImage = (ImageView) findViewById(R.id.arrow_image) ;
        arrowImageUp = (ImageView)findViewById(R.id.arrow_image_up) ;
        childlayout = (LinearLayout)findViewById(R.id.child_view);
        dob_layout = (RelativeLayout) findViewById(R.id.dob_layout);
        arrowImage.setOnClickListener(this);
        arrowImageUp.setOnClickListener(this);
        dob_layout.setOnClickListener(this);
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
        sp_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                pref_district = District.get(position).getDistrictName();
                prefManager.setDistrictName(pref_district);
                blockFilterSpinner((District.get(position).getDistictCode()));
                prefManager.setDistrictCode(District.get(position).getDistictCode());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                pref_Block = Block.get(position).getBlockName();
                prefManager.setBlockName(pref_Block);
                villageFilterSpinner(Block.get(position).getBlockCode());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pref_Village = Village.get(position).getVillageListPvName();
                prefManager.setVillageListPvName(pref_Village);
//                    prefManager.setKeySpinnerSelectedPvcode(Village.get(position).getVillageListPvCode());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadOfflineDistrictListDBValues();

    }

    public void loadOfflineDistrictListDBValues() {
        Cursor DistrictList = getRawEvents("Select * from " + DISTRICT_TABLE_NAME, null);
        District.clear();
        ODFMonitoringListValue ODFMonitoringListValue = new ODFMonitoringListValue();
        ODFMonitoringListValue.setDistrictName("Select District");
        District.add(ODFMonitoringListValue);
        if (DistrictList.getCount() > 0) {
            if (DistrictList.moveToFirst()) {
                do {
                    ODFMonitoringListValue districtList = new ODFMonitoringListValue();
                    String districtCode = DistrictList.getString(DistrictList.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE));
                    String districtName = DistrictList.getString(DistrictList.getColumnIndexOrThrow(AppConstant.DISTRICT_NAME));
                    districtList.setDistictCode(Integer.parseInt(districtCode));
                    districtList.setDistrictName(districtName);
                    District.add(districtList);
                } while (DistrictList.moveToNext());
            }
        }
        sp_district.setAdapter(new CommonAdapter(this, District, "DistrictList"));
    }

    public void blockFilterSpinner(int filterBlock) {

        String blocksql = "SELECT * FROM " + BLOCK_TABLE_NAME + " WHERE dcode = " + filterBlock;
        Log.d("blocksql", blocksql);
        Cursor BlockList = getRawEvents(blocksql, null);
        Block.clear();
        ODFMonitoringListValue blockListValue = new ODFMonitoringListValue();
        blockListValue.setBlockName("Select Block");
        Block.add(blockListValue);
        if (BlockList.getCount() > 0) {
            if (BlockList.moveToFirst()) {
                do {
                    ODFMonitoringListValue blockList = new ODFMonitoringListValue();
                    String districtCode = BlockList.getString(BlockList.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE));
                    String blockCode = BlockList.getString(BlockList.getColumnIndexOrThrow(AppConstant.BLOCK_CODE));
                    String blockName = BlockList.getString(BlockList.getColumnIndexOrThrow(AppConstant.BLOCK_NAME));
                    blockList.setDistictCode(Integer.parseInt(districtCode));
                    blockList.setBlockCode(blockCode);
                    blockList.setBlockName(blockName);
                    Block.add(blockList);
                } while (BlockList.moveToNext());
            }
        }
        sp_block.setAdapter(new CommonAdapter(this, Block, "BlockList"));
    }

    public void villageFilterSpinner(String filterVillage) {
        String villageSql = "SELECT * FROM " + VILLAGE_TABLE_NAME + " WHERE dcode = " + prefManager.getDistrictCode() + " and bcode = " + filterVillage;
        Log.d("villageSql", "" + villageSql);
        Cursor VillageList = getRawEvents(villageSql, null);
        Village.clear();
        ODFMonitoringListValue villageListValue = new ODFMonitoringListValue();
        villageListValue.setVillageListPvName("Select Village");
        Village.add(villageListValue);
        if (VillageList.getCount() > 0) {
            if (VillageList.moveToFirst()) {
                do {
                    ODFMonitoringListValue villageList = new ODFMonitoringListValue();
                    String districtCode = VillageList.getString(VillageList.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE));
                    String blockCode = VillageList.getString(VillageList.getColumnIndexOrThrow(AppConstant.BLOCK_CODE));
                    String pvCode = VillageList.getString(VillageList.getColumnIndexOrThrow(AppConstant.PV_CODE));
                    String pvname = VillageList.getString(VillageList.getColumnIndexOrThrow(AppConstant.PV_NAME));

                    villageList.setVillageListDistrictCode(districtCode);
                    villageList.setVillageListBlockCode(blockCode);
                    villageList.setVillageListPvCode(pvCode);
                    villageList.setVillageListPvName(pvname);

                    Village.add(villageList);
                    Log.d("spinnersize", "" + Village.size());
                } while (VillageList.moveToNext());
            }
        }
        sp_village.setAdapter(new CommonAdapter(this, Village, "VillageList"));
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
            case R.id.dob_layout:
                showStartDatePickerDialog();
                break;
        }
    }

    public void autoCompleteApiBankName() {
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        motivator_bank_tv.setThreshold(1);
        motivator_bank_tv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        String input = motivator_bank_tv.getText().toString();
                        prefManager.setKeyAutocompleteSelectedBankName(input);
                       Log.d("ODF",""+(motivator_bank_tv.getText().toString()));
                  //     Log.d("BANK_ID",""+BankDetails.get(position).getBank_Id());

//                        String selection = (String) parent.getItemAtPosition(position);
//                        int pos = -1;

                        for (int i = 0; i < array.size(); i++) {
                            Log.d("array",String.valueOf(array.get(i)));
                            Log.d("arraysize",String.valueOf(array.size()));
                            Log.d("arraylrngth",String.valueOf(BankDetails.size()));
                            Log.d("arraylrngth",String.valueOf(BankDetails.get(i).getBank_Name()));
                            getId(BankDetails.get(i).getBank_Id());
                                break;
                        }
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
                brancharray.clear();
                motivator_branch_tv.setText("");
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
        motivator_branch_tv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        for (int i = 0; i < brancharray.size(); i++) {
                            Log.d("arraylrngth",String.valueOf(BranchDetails.get(i).getIFSC_Code()));
                            motivator_ifsc_tv.setText(BranchDetails.get(i).getIFSC_Code());
                                break;
                        }
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
                brancharray.clear();
                motivator_ifsc_tv.setText("");
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
        array.clear();
        String like_query = "SELECT * FROM " + BANKLIST_TABLE_NAME + " where bank_name LIKE '" + text + "%'";
        Log.d("AutoSearchQuery", "" + like_query);
        Cursor BankList = getRawEvents(like_query, null);
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
        motivator_bank_tv.setAdapter(autoSuggestAdapter);
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
        motivator_branch_tv.setAdapter(autoSuggestAdapter);
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

    public void showStartDatePickerDialog() {
        DialogFragment newFragment = new fromDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

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
            motivator_dob_tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            String start_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            cldr.set(Calendar.YEAR, year);
            cldr.set(Calendar.MONTH, (monthOfYear));
            cldr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Log.d("startdate", "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }

    }

}
