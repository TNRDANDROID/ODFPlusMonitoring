package com.nic.ODFPlusMonitoring.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ODFPlusMonitoring";
    private static final int DATABASE_VERSION = 1;

    public static final String DISTRICT_TABLE_NAME = "ODF_DistrictTable";
    public static final String BLOCK_TABLE_NAME = "ODF_BlockTable";
    public static final String VILLAGE_TABLE_NAME = "ODF_villageTable";
    public static final String BANKLIST_TABLE_NAME = "ODF_BankName";
    public static final String BANKLIST_BRANCH_TABLE_NAME = "ODF_BankName_Branch";
    public static final String MOTIVATOR_CATEGORY_LIST_TABLE_NAME = "ODF_MotivatorCategory_List";
    public static final String SCHEDULE = "t_schedule";
    public static final String SCHEDULE_VILLAGE = "t_schedule_village";
    public static final String SCHEDULED_ACTIVITY = "t_scheduled_activity";
    public static final String SCHEDULED_ACTIVITY_PHOTOS = "t_scheduled_activity_photos";

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
                "activity_name TEXT," +
                "place_of_activity TEXT)");

        db.execSQL("CREATE TABLE " + SCHEDULED_ACTIVITY_PHOTOS + " ("
                + "schedule_activity_id INTEGER," +
                "schedule_id INTEGER," +
                "motivator_id INTEGER," +
                "image BLOB," +
                "start_lat TEXT," +
                "start_long BLOB," +
                "start_end_flag INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
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
            onCreate(db);
        }
    }


}
