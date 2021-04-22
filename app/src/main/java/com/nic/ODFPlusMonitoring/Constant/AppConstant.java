package com.nic.ODFPlusMonitoring.Constant;

/**
 * Created by User on 24/05/16.
 */
public class AppConstant {
    public static final String PREF_NAME = "NIC";
    public static String KEY_SERVICE_ID = "service_id";
    public static String KEY_APP_CODE = "appcode";
    public static String KEY_APP_ID = "app_id";
    public static String JSON_DATA = "JSON_DATA";
    public static String KEY_VERSION_CHECK= "version_check";
    public static String DATA_CONTENT = "data_content";
    public static String ENCODE_DATA = "enc_data";

    public static String KEY_DISTRICT_LIST_ALL = "district_list_all";
    public static String KEY_BLOCK_LIST_ALL = "block_list_all";
    public static String KEY_VILLAGE_LIST_ALL = "village_list_all";
    public static String KEY_BANK_NAME_LIST = "Generate_bank_name_json";
    public static String KEY_BANK_BRANCH_NAME_LIST = "Generate_branch_name_bank_wise_json";
    public static String KEY_BRANCH_DETAIL_BY_IFSC_CODE = "branchDetailsByIFSCCode";
    public static String KEY_VILLAGE_LIST_DISTRICT_BLOCK_WISE = "village_list_district_block_wise";

    public static String USER_LOGIN_KEY = "user_login_key";
    public static String KEY_USER_NAME = "user_name";
    public static String KEY_USER_PASSWORD = "user_pwd";
    public static String KEY_RESPONSE = "RESPONSE";
    public static String KEY_STATUS = "STATUS";
    public static String KEY_MESSAGE = "MESSAGE";
    public static String KEY_USER = "KEY";
    public static String USER_DATA = "user_data";

    public static String DISTRICT_CODE = "dcode";
    public static String DISTRICT_NAME = "dname";

    public static String BLOCK_CODE = "bcode";
    public static String PV_CODE = "pvcode";
    public static String BLOCK_NAME = "bname";
    public static String PV_NAME = "pvname";


    /******** BANK NAME TABLE *****/

    public static String BANK_ID = "bank_id";
    public static String OMC_NAME = "omc_name";
    public static String BANK_NAME = "bank_name";

    /******* BANK BRANCH TABLE ****/

    public static String BRANCH_ID = "branch_id";
    public static String BRANCH_NAME = "branch";
    public static String IFSC_CODE = "ifsc";


    public static String KEY_MOTIVATOR_CATEGORY = "motivator_category";
    public static String KEY_MOTIVATOR_CATEGORY_ID = "motivator_category_id";
    public static String KEY_MOTIVATOR_CATEGORY_NAME = "motivator_category_name";
    public static String KEY_MOTIVATOR_CATEGORY_NAME_FULL_FORM = "motivator_category_full_form";
    public static String FINANCIAL_YEAR = "fin_year";

    public static String KEY_REGISTER_SIGNUP = "signup";
    public static String KEY_MOTIVATOR_NAME = "motivator_name";
    public static String KEY_MOTIVATOR_ADDRESS = "motivator_address";
    public static String KEY_MOTIVATOR_MOBILE = "mobile_no";
    public static String KEY_MOTIVATOR_DOB = "motivator_dob";
    public static String KEY_MOTIVATOR_EMAIL = "motivator_email";
    public static String KEY_MOTIVATOR_IMAGE = "image";
    public static String KEY_REGISTER_DOB = "dob";
    public static String KEY_MOTIVATOR_OTHERS = "is_motivator_others";
    public static String KEY_REGISTER_MOBILE = "mobile";
    public static String KEY_REGISTER_EMAIL = "email";
    public static String KEY_REGISTER_ADDRESS = "address";
    public static String KEY_MOTIVATOR_PHOTO = "motivator_photo";
    public static String KEY_REGISTER_ACC_NO = "ac_number";
    public static String KEY_GENDER_CODE = "gender";
    public static String KEY_EDUCATION_CODE = "qualification_id";
    public static String KEY_MOTIVATOR_NO_OF_STATE_LEVEL_TRAINEE = "no_of_state_level_training_attended";
    public static String KEY_MOTIVATOR_NO_OF_DISTRICT_LEVEL_TRAINEE = "no_of_district_level_training_attended";
    public static String KEY_MOTIVATOR_NO_OF_BLOCK_LEVEL_TRAINEE = "no_of_block_level_training_attended";
    public static String KEY_REGISTER_CATEGORY = "category";
    public static String KEY_REGISTER_CATEGORY_OTHERS = "category_other";
    public static String KEY_REGISTER_MOTIVATOR_POSITION = "motivator_position";
    public static String KEY_REGISTER_IFSC_CODE = "ifsccode";
    public static String KEY_REGISTER_MOTIVATOR_ID = "motivator_id";
    public static String KEY_MOTIVATOR_SCHEDULE = "motivator_schedule";
    public static String KEY_MOTIVATOR_SCHEDULE_HISTORY = "motivator_schedule_history";

    /******************* t_schedule **********************/

    public static String KEY_T_SCHEDULE = "t_schedule";
    public static String KEY_SCHEDULE_ID = "schedule_id";
    public static String KEY_SCHEDULE_MASTER_ID = "schedule_master_id";
    public static String KEY_MOTIVATOR_ID = "motivator_id";
    public static String KEY_FROM_DATE = "from_date";
    public static String KEY_TO_DATE = "to_date";
    public static String KEY_SCHEDULE_DESCRIPTION = "schedule_description";
    public static String KEY_TOTAL_ACTIVITY = "total_activity";
    public static String KEY_COMPLETED_ACTIVITY = "completed_activity";
    public static String KEY_PENDING_ACTIVITY = "pending_activity";

    /******************* t_schedule_village **********************/

    public static String KEY_T_SCHEDULE_VILLAGE = "t_schedule_village";
    public static String KEY_SCHEDULE_VILLAGE_LINK_ID = "schedule_village_link_id";

    /******************* t_scheduled_activity **********************/

    public static String KEY_T_SCHEDULE_ACTIVITY = "t_scheduled_activity";
    public static String KEY_SCHEDULE_ACTIVITY_ID = "schedule_activity_id";
    public static String KEY_ACTIVITY_ID = "activity_id";
    public static String KEY_PLACE_OF_ACTIVITY = "place_of_activity";
    public static String KEY_ACTIVITY_NAME = "activity_name";
    public static String KEY_NO_OF_PHOTOS = "no_of_photos";
    public static String KEY_TYPE = "type";
    public static String KEY_LATITUDE = "lat";
    public static String KEY_LONGITUDE = "long";
    public static String KEY_IMAGE = "image";
    public static String KEY_AUDIO = "audio";
    public static String KEY_IMAGE_REMARK = "remark";
    public static String KEY_DATE_TIME = "datetime";
    public static String KEY_SERIAL_NUMBER = "serial_number";
    public static String KEY_ACTIVITY_STATUS = "activity_status";
    public static String KEY_ACTIVITY_REJECTED_STATUS = "previously_rejected";
    public static String KEY_ACTIVITY_START = "activity_start";
    public static String KEY_ACTIVITY_END = "activity_end";
    public static String KEY_ACTIVITY_DURATION = "activity_duration";
    public static String KEY_ACTIVITY_AMOUNT = "activity_amount";
    public static String KEY_ACTIVITY_DOC_AVAILABLE = "activity_desc_doc_available";
    public static String KEY_ACTIVITY_AUDIO_AVAILABLE = "activity_desc_audio_available";

    public static String KEY_TRACK_DATA = "track_data";
    public static String KEY_ACTIVITY_IMAGE_SAVE = "activity_image_save";

    /******************* t_scheduled_activity_photos **********************/

    public static String KEY_T_SCHEDULE_ACTIVITY_PHOTOS = "t_scheduled_activity_photos";
    public static String KEY_IMAGE_AVAILABLE = "image_available";
    public static String KEY_ACTIVITY_IMAGE_VIEW = "activity_image_view";
    public static String KEY_ACTIVITY_TYPE_NAME = "activity_type_name";
    public static String KEY_ACTIVITY_FEEDBACK = "activity_feedback";
    public static String KEY_ACTIVITY_SCHEDULE_FEEDBACK = "activity_schedule_feedback";
    public static String KEY_MOTIVATOR_PROFILE = "motivator_profile";
    public static String KEY_PURPOSE = "purpose";
    public static String KEY_PHOTO_ID = "id";



}
