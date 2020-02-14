package com.nic.ODFPlusMonitoring.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ODFPlusMonitoring";
    private static final int DATABASE_VERSION = 3;

    public static final String DISTRICT_TABLE_NAME = "ODF_DistrictTable";
    public static final String BLOCK_TABLE_NAME = "ODF_BlockTable";
    public static final String VILLAGE_TABLE_NAME = "ODF_villageTable";
    public static final String BANKLIST_TABLE_NAME = "ODF_BankName";
    public static final String BANKLIST_BRANCH_TABLE_NAME = "ODF_BankName_Branch";
    public static final String MOTIVATOR_CATEGORY_LIST_TABLE_NAME = "ODF_MotivatorCategory_List";
    public static final String SCHEDULE = "t_schedule";
    public static final String SCHEDULE_VILLAGE = "t_schedule_village";
    public static final String SCHEDULED_ACTIVITY = "t_scheduled_activity";
    public static final String ACTIVITY_COMPLETED = "t_activity_completed";
    public static final String SCHEDULED_ACTIVITY_PHOTOS = "t_scheduled_activity_photos";
    public static final String SAVE_ACTIVITY = "save_activity";
    public static final String MOTIVATOR_PROFILE = "motivator_profile";

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DISTRICT_TABLE_NAME + " ("
                + "dcode INTEGER," +
                "dname TEXT)");

        db.execSQL("CREATE TABLE " + BLOCK_TABLE_NAME + " ("
                + "dcode INTEGER," +
                "bcode INTEGER," +
                "bname varchar(32))");

        db.execSQL("CREATE TABLE " + VILLAGE_TABLE_NAME  + " ("
                + "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "pvname varchar(32))");

        db.execSQL("CREATE TABLE " + BANKLIST_TABLE_NAME  + " ("
                + "bank_id INTEGER," +
                "omc_name TEXT," +
                "bank_name TEXT)");

        db.execSQL("CREATE TABLE " + BANKLIST_BRANCH_TABLE_NAME  + " ("
                + "bank_id INTEGER," +
                "branch_id INTEGER," +
                "branch TEXT," +
                "ifsc TEXT)");

        db.execSQL("CREATE TABLE " + MOTIVATOR_CATEGORY_LIST_TABLE_NAME + " ("
                + "motivator_category_id INTEGER," +
                "motivator_category_name TEXT)");

        db.execSQL("CREATE TABLE " + SCHEDULE + " ("
                + "schedule_id INTEGER," +
                "schedule_master_id INTEGER," +
                "motivator_id INTEGER," +
                "from_date date," +
                "to_date date," +
                "schedule_description TEXT," +
                "total_activity INTEGER," +
                "completed_activity INTEGER," +
                "pending_activity INTEGER)");

        db.execSQL("CREATE TABLE " + SCHEDULE_VILLAGE + " ("
                + "schedule_village_link_id INTEGER," +
                "schedule_id INTEGER," +
                "motivator_id INTEGER," +
                "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "pvname TEXT)");

        db.execSQL("CREATE TABLE " + SCHEDULED_ACTIVITY + " ("
                + "schedule_activity_id INTEGER," +
                "schedule_id INTEGER," +
                "activity_id INTEGER," +
                "activity_status TEXT," +
                "activity_name TEXT," +
                "activity_type_name TEXT," +
                "no_of_photos INTEGER," +
                "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "place_of_activity TEXT)");

        db.execSQL("CREATE TABLE " + ACTIVITY_COMPLETED + " ("
                + "schedule_activity_id INTEGER," +
                "schedule_id INTEGER," +
                "activity_id INTEGER," +
                "activity_status TEXT," +
                "activity_name TEXT," +
                "activity_start TEXT," +
                "activity_end TEXT," +
                "activity_type_name TEXT," +
                "no_of_photos INTEGER," +
                "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "place_of_activity TEXT)");

        db.execSQL("CREATE TABLE " + SCHEDULED_ACTIVITY_PHOTOS + " ("
                + "schedule_activity_id INTEGER," +
                "schedule_id INTEGER," +
                "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "lat TEXT," +
                "long TEXT," +
                "type TEXT," +
                "image_available TEXT," +
                "image BLOB," +
                "remark TEXT)");

        db.execSQL("CREATE TABLE " + SAVE_ACTIVITY + " ("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT," +
                 "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "activity_id INTEGER," +
                "schedule_id INTEGER," +
                "motivator_id INTEGER," +
                "schedule_master_id INTEGER," +
                "image BLOB," +
                "lat TEXT," +
                "long TEXT," +
                "type TEXT," +
                "serial_number INTEGER," +
                "datetime TEXT," +
                "activity_name TEXT," +
                "remark TEXT)");

        db.execSQL("CREATE TABLE " + MOTIVATOR_PROFILE + " ("
                + "motivator_name TEXT," +
                "dcode INTEGER," +
                "bcode INTEGER," +
                "mobile_no INTEGER," +
                "image BLOB," +
                "motivator_email TEXT," +
                "motivator_address TEXT," +
                "motivator_bank_name TEXT," +
                "motivator_branch_name TEXT," +
                "motivator_dob TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + DISTRICT_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BLOCK_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + VILLAGE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BANKLIST_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BANKLIST_BRANCH_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MOTIVATOR_CATEGORY_LIST_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE);
            db.execSQL("DROP TABLE IF EXISTS " + SCHEDULED_ACTIVITY);
            db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_VILLAGE);
            db.execSQL("DROP TABLE IF EXISTS " + SCHEDULED_ACTIVITY_PHOTOS);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_ACTIVITY);
            db.execSQL("DROP TABLE IF EXISTS " + MOTIVATOR_PROFILE);
            db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_COMPLETED);
            onCreate(db);
        }
    }


}
