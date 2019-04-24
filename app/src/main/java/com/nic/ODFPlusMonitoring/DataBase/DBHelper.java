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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + DISTRICT_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BLOCK_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + VILLAGE_TABLE_NAME);
            onCreate(db);
        }
    }


}
