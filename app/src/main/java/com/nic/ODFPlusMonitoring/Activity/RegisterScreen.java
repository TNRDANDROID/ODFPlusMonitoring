package com.nic.ODFPlusMonitoring.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.ODFPlusMonitoring.Adapter.AutoSuggestAdapter;
import com.nic.ODFPlusMonitoring.Adapter.CommonAdapter;
import com.nic.ODFPlusMonitoring.Api.Api;
import com.nic.ODFPlusMonitoring.Api.ApiService;
import com.nic.ODFPlusMonitoring.Api.ServerResponse;
import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.DataBase.DBHelper;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;
import com.nic.ODFPlusMonitoring.Session.PrefManager;
import com.nic.ODFPlusMonitoring.Support.MyCustomTextView;
import com.nic.ODFPlusMonitoring.Support.MyEditTextView;
import com.nic.ODFPlusMonitoring.Support.ProgressHUD;
import com.nic.ODFPlusMonitoring.Utils.CameraUtils;
import com.nic.ODFPlusMonitoring.Utils.UrlGenerator;
import com.nic.ODFPlusMonitoring.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.BANKLIST_BRANCH_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.BANKLIST_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.BLOCK_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.DISTRICT_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.MOTIVATOR_CATEGORY_LIST_TABLE_NAME;
import static com.nic.ODFPlusMonitoring.DataBase.DBHelper.VILLAGE_TABLE_NAME;

/**
 * Created by AchanthiSundar on 28-12-2018.
 */

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private Button btn_register;
    private Handler handler = new Handler();
    private MyEditTextView motivator_name, motivator_address, motivator_mobileNO, motivator_email_id, motivator_state_level_tv, motivator_position_tv;
    private AppCompatAutoCompleteTextView motivator_bank_tv, motivator_account_tv, motivator_branch_tv;
    private MyCustomTextView motivator_ifsc_tv;
    private static MyCustomTextView motivator_dob_tv;
    private RelativeLayout dob_layout, edit_image;
    private LinearLayout position_layout;
    private Spinner sp_block, sp_district, sp_village, sp_category;
    private PrefManager prefManager;
    private List<ODFMonitoringListValue> Block = new ArrayList<>();
    private List<ODFMonitoringListValue> District = new ArrayList<>();
    private List<ODFMonitoringListValue> Village = new ArrayList<>();
    private List<ODFMonitoringListValue> Category = new ArrayList<>();
    private List<ODFMonitoringListValue> BankDetails = new ArrayList<>();
    private List<ODFMonitoringListValue> BranchDetails = new ArrayList<>();
    private ProgressHUD progressHUD;
    private AutoSuggestAdapter autoSuggestAdapter;
    private ImageView arrowImage,arrowImageUp;
    private ScrollView  scrollView;
    private CircleImageView profile_image,profile_image_preview;
    String pref_Block,pref_district, pref_Village;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    List<String> array = new ArrayList<String>();
    List<String> brancharray = new ArrayList<String>();
    private Animation animation;
    private LinearLayout childlayout;
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;


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
        sp_category = (Spinner) findViewById(R.id.category);
        motivator_address = (MyEditTextView) findViewById(R.id.motivator_address);
        motivator_mobileNO = (MyEditTextView) findViewById(R.id.motivator_mobile_no);
        motivator_email_id = (MyEditTextView) findViewById(R.id.motivator_email_id);
        motivator_state_level_tv = (MyEditTextView) findViewById(R.id.motivator_state_level_tv);
        motivator_account_tv = (AppCompatAutoCompleteTextView) findViewById(R.id.motivator_account_tv);
        motivator_bank_tv = (AppCompatAutoCompleteTextView) findViewById(R.id.motivator_bank_tv);
        motivator_branch_tv = (AppCompatAutoCompleteTextView) findViewById(R.id.motivator_branch_tv);
        motivator_ifsc_tv = (MyCustomTextView) findViewById(R.id.motivator_ifsc_tv);
        motivator_dob_tv = (MyCustomTextView) findViewById(R.id.motivator_dob_tv);
        motivator_position_tv = (MyEditTextView) findViewById(R.id.motivator_position_tv);
        scrollView = (ScrollView)findViewById(R.id.scroll_view) ;
        arrowImage = (ImageView) findViewById(R.id.arrow_image) ;
        arrowImageUp = (ImageView)findViewById(R.id.arrow_image_up) ;
        childlayout = (LinearLayout)findViewById(R.id.child_view);
        dob_layout = (RelativeLayout) findViewById(R.id.dob_layout);
        edit_image = (RelativeLayout) findViewById(R.id.edit_image);
        position_layout = (LinearLayout) findViewById(R.id.position_layout);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profile_image_preview = (CircleImageView) findViewById(R.id.profile_image_preview);
        arrowImage.setOnClickListener(this);
        arrowImageUp.setOnClickListener(this);
        dob_layout.setOnClickListener(this);
        edit_image.setOnClickListener(this);

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
                if (position == 0) {
                    sp_block.setClickable(false);
                    sp_block.setVisibility(View.GONE);
                } else {
                    sp_block.setClickable(true);
                    sp_block.setVisibility(View.VISIBLE);
                }
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
                if (position == 0) {
                    sp_village.setClickable(false);
                    sp_village.setVisibility(View.GONE);
                } else {
                    sp_village.setClickable(true);
                    sp_village.setVisibility(View.VISIBLE);
                }
                pref_Block = Block.get(position).getBlockName();
                prefManager.setBlockName(pref_Block);
                prefManager.setKeySpinnerSelectedBlockcode(Block.get(position).getBlockCode());

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
                prefManager.setKeySpinnerSelectedPvcode(Village.get(position).getVillageListPvCode());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Category.get(position).getMotivatorCategoryId() == 5) {
                    position_layout.setVisibility(View.VISIBLE);
                } else {
                    position_layout.setVisibility(View.GONE);

                }
                prefManager.setSpinnerSelectedCategoryName(Category.get(position).getMotivatorCategoryName());
                prefManager.setSpinnerSelectedCategoryId(Category.get(position).getMotivatorCategoryId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadOfflineDistrictListDBValues();
        loadCategoryListDBValues();

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

    public void loadCategoryListDBValues() {
        Cursor CategoryListCursor = getRawEvents("Select * from " + MOTIVATOR_CATEGORY_LIST_TABLE_NAME, null);
        Category.clear();
        ODFMonitoringListValue ODFMonitoringListValue = new ODFMonitoringListValue();
        ODFMonitoringListValue.setMotivatorCategoryName("Select Category");
        Category.add(ODFMonitoringListValue);
        if (CategoryListCursor.getCount() > 0) {
            if (CategoryListCursor.moveToFirst()) {
                do {
                    ODFMonitoringListValue categoryList = new ODFMonitoringListValue();
                    int categoryId = CategoryListCursor.getInt(CategoryListCursor.getColumnIndexOrThrow(AppConstant.KEY_MOTIVATOR_CATEGORY_ID));
                    String categoryName = CategoryListCursor.getString(CategoryListCursor.getColumnIndexOrThrow(AppConstant.KEY_MOTIVATOR_CATEGORY_NAME));
                    categoryList.setMotivatorCategoryId(categoryId);
                    categoryList.setMotivatorCategoryName(categoryName);
                    Category.add(categoryList);
                } while (CategoryListCursor.moveToNext());
            }
        }
        sp_category.setAdapter(new CommonAdapter(this, Category, "CategoryList"));
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
            case R.id.edit_image:
                getCameraPermission();
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
                        Log.d("ODF", "" + (motivator_bank_tv.getText().toString()));
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
                            prefManager.setKeyAutocompleteSelectedBranchID(BranchDetails.get(i).getBranch_Id());
                            prefManager.setKeyAutocompleteSelectedIfscCode((BranchDetails.get(i).getIFSC_Code()));
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
        if (profile_image.getDrawable() != null) {
            if (!motivator_name.getText().toString().isEmpty()) {
                if (!"Select District".equalsIgnoreCase(District.get(sp_district.getSelectedItemPosition()).getDistrictName())) {
                    if (!"Select Block".equalsIgnoreCase(Block.get(sp_block.getSelectedItemPosition()).getBlockName())) {
                        if (!"Select Village".equalsIgnoreCase(Village.get(sp_village.getSelectedItemPosition()).getVillageListPvName())) {
                            if (!motivator_address.getText().toString().isEmpty()) {
                                if (!motivator_mobileNO.getText().toString().isEmpty()) {
                                    if (Utils.isValidMobile(motivator_mobileNO.getText().toString())) {
                                        if (!motivator_email_id.getText().toString().isEmpty()) {
                                            if (Utils.isEmailValid(motivator_email_id.getText().toString())) {
                                                if (!motivator_account_tv.getText().toString().isEmpty()) {
                                                    if (!motivator_bank_tv.getText().toString().isEmpty()) {
                                                        if (!motivator_branch_tv.getText().toString().isEmpty()) {
                                                            if (!motivator_ifsc_tv.getText().toString().isEmpty()) {
                                                                if (!motivator_dob_tv.getText().toString().isEmpty()) {
                                                                    if (!motivator_state_level_tv.getText().toString().isEmpty()) {
                                                                        if (!"Select Category".equalsIgnoreCase(Category.get(sp_category.getSelectedItemPosition()).getMotivatorCategoryName())) {
                                                                            if ((prefManager.getSpinnerSelectedCategoryName()).equalsIgnoreCase("others")) {
                                                                                if (!motivator_position_tv.getText().toString().isEmpty()) {
                                                                                    signUP();
                                                                                } else {
                                                                                    Utils.showAlert(this, "Enter the Position ");
                                                                                }
                                                                            } else {
                                                                                signUP();
                                                                            }
                                                                        } else {
                                                                            Utils.showAlert(this, "Select the Category ");
                                                                        }
                                                                    } else {
                                                                        Utils.showAlert(this, "Enter the No Of State Level Training Attended ");
                                                                    }
                                                                } else {
                                                                    Utils.showAlert(this, "Select the Date Of Birth!");
                                                                }
                                                            } else {
                                                                Utils.showAlert(this, "Enter the IFSC Code!");
                                                            }
                                                        } else {
                                                            Utils.showAlert(this, "Select the Branch Name!");
                                                        }
                                                    } else {
                                                        Utils.showAlert(this, "Select the Bank Name!");
                                                    }
                                                } else {
                                                    Utils.showAlert(this, "Enter the Account No!");
                                                }
                                            } else {
                                                Utils.showAlert(this, "Enter the Valid Email Id!");
                                            }
                                        } else {
                                            Utils.showAlert(this, "Enter the Email Id!");
                                        }
                                    } else {
                                        Utils.showAlert(this, "Enter the Valid Mobile No");
                                    }
                                } else {
                                    Utils.showAlert(this, "Enter the Mobile No!");
                                }
                            } else {
                                Utils.showAlert(this, "Enter the Address!");
                            }
                        } else {
                            Utils.showAlert(this, "Select Village!");
                        }
                    } else {
                        Utils.showAlert(this, "Select Block!");
                    }
                } else {
                    Utils.showAlert(this, "Select District!");
                }
            } else {
                Utils.showAlert(this, "Enter the Name!");
            }
        } else {
            Utils.showAlert(this, "Capture the Profile Image First!");
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

    private void getCameraPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CameraUtils.checkPermissions(RegisterScreen.this)) {
                captureImage();
            } else {
                requestCameraPermission(MEDIA_TYPE_IMAGE);
            }
        } else {
            captureImage();
        }


    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(this, file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            } else {
//                                captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(RegisterScreen.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void previewCapturedImage() {
        try {
            // hide video preview
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            profile_image_preview.setVisibility(View.GONE);
            profile_image.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // video successfully recorded
                // preview the recorded video
//                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void signUP() {
        try {
            new ApiService(this).makeJSONObjectRequest("Register", Api.Method.POST, UrlGenerator.getMotivatorCategory(), dataTobeSavedJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public JSONObject dataTobeSavedJsonParams() throws JSONException {

        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_REGISTER_SIGNUP);
        dataSet.put(AppConstant.KEY_MOTIVATOR_NAME, motivator_name.getText().toString());
        dataSet.put(AppConstant.KEY_REGISTER_DOB, motivator_dob_tv.getText().toString());
        dataSet.put(AppConstant.KEY_REGISTER_MOBILE, motivator_mobileNO.getText().toString());
        dataSet.put(AppConstant.KEY_REGISTER_EMAIL, motivator_email_id.getText().toString());
        dataSet.put(AppConstant.KEY_REGISTER_ADDRESS, motivator_address.getText().toString());
        dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());
        dataSet.put(AppConstant.BLOCK_CODE, prefManager.getKeySpinnerSelectedBlockcode());
        dataSet.put(AppConstant.PV_CODE, prefManager.getKeySpinnerSelectedPVcode());
        dataSet.put(AppConstant.KEY_MOTIVATOR_PHOTO, image_str.trim());
        dataSet.put(AppConstant.KEY_REGISTER_ACC_NO, motivator_account_tv.getText().toString());
        dataSet.put(AppConstant.BANK_ID, prefManager.getKeyAutocompleteSelectedBankID());
        dataSet.put(AppConstant.BRANCH_ID, prefManager.getKeyAutocompleteSelectedBranchID());
        dataSet.put(AppConstant.KEY_REGISTER_IFSC_CODE, prefManager.getKeyAutocompleteSelectedIfscCode());
        dataSet.put(AppConstant.KEY_MOTIVATOR_NO_OF_STATE_LEVEL_TRAINEE, motivator_state_level_tv.getText().toString());
        if ((prefManager.getSpinnerSelectedCategoryName()).equalsIgnoreCase("others")) {
            dataSet.put(AppConstant.KEY_REGISTER_CATEGORY_OTHERS, prefManager.getSpinnerSelectedCategoryId());
            dataSet.put(AppConstant.KEY_REGISTER_MOTIVATOR_POSITION, motivator_position_tv.getText().toString());
        } else {
            dataSet.put(AppConstant.KEY_REGISTER_CATEGORY, prefManager.getSpinnerSelectedCategoryId());
        }
        Log.d("RegisterDataSet", "" + dataSet);
        String authKey = dataSet.toString();
        int maxLogSize = 2000;
        for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > authKey.length() ? authKey.length() : end;
            Log.v("to_send+_plain", authKey.substring(start, end));
        }
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status = responseObj.getString(AppConstant.KEY_STATUS);
            String response = responseObj.getString(AppConstant.KEY_RESPONSE);
            if ("Register".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    JSONObject jsonObject = responseObj.getJSONObject(AppConstant.JSON_DATA);
                    String Motivatorid = jsonObject.getString(AppConstant.KEY_REGISTER_MOTIVATOR_ID);
                    Log.d("motivatorid",""+Motivatorid);
                    Utils.showAlert(this,"You have been successfully registered!");
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    handler.postDelayed(runnable,2000);

                } else {
                    Utils.showAlert(this, "Status Fail");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}