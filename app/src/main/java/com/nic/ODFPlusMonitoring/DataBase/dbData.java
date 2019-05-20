package com.nic.ODFPlusMonitoring.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


import com.nic.ODFPlusMonitoring.Constant.AppConstant;
import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class dbData {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public dbData(Context context){
        this.dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if(dbHelper != null) {
            dbHelper.close();
        }
    }

    /****** DISTRICT TABLE *****/
    public ODFMonitoringListValue insertDistrict(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE,odfMonitoringListValue.getDistictCode());
        values.put(AppConstant.DISTRICT_NAME,odfMonitoringListValue.getDistrictName());

        long id = db.insert(DBHelper.DISTRICT_TABLE_NAME,null,values);
        Log.d("Inserted_id_district",String.valueOf(id));

        return odfMonitoringListValue;
    }

    public ArrayList<ODFMonitoringListValue> getAll_District() {

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.DISTRICT_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setDistictCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setDistrictName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /****** BLOCKTABLE *****/
    public ODFMonitoringListValue insertBlock(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE,odfMonitoringListValue.getDistictCode());
        values.put(AppConstant.BLOCK_CODE,odfMonitoringListValue.getBlockCode());
        values.put(AppConstant.BLOCK_NAME,odfMonitoringListValue.getBlockName());

        long id = db.insert(DBHelper.BLOCK_TABLE_NAME,null,values);
        Log.d("Inserted_id_block",String.valueOf(id));

        return odfMonitoringListValue;
    }

    public ArrayList<ODFMonitoringListValue> getAll_Block() {

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.BLOCK_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setDistictCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setBlockName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /****** VILLAGE TABLE *****/
    public ODFMonitoringListValue insertVillage(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE,odfMonitoringListValue.getDistictCode());
        values.put(AppConstant.BLOCK_CODE,odfMonitoringListValue.getBlockCode());
        values.put(AppConstant.PV_CODE,odfMonitoringListValue.getPvCode());
        values.put(AppConstant.PV_NAME,odfMonitoringListValue.getPvName());

        long id = db.insert(DBHelper.VILLAGE_TABLE_NAME,null,values);
        Log.d("Inserted_id_village",String.valueOf(id));

        return odfMonitoringListValue;
    }

    public ArrayList<ODFMonitoringListValue> getAll_Village() {

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.VILLAGE_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setDistictCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /****** BANK NAME TABLE *****/
    public ODFMonitoringListValue insertBankName(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.BANK_ID,odfMonitoringListValue.getBank_Id());
        values.put(AppConstant.OMC_NAME,odfMonitoringListValue.getOMC_Name());
        values.put(AppConstant.BANK_NAME,odfMonitoringListValue.getBank_Name());

        long id = db.insert(DBHelper.BANKLIST_TABLE_NAME,null,values);
        Log.d("Inserted_id_bankname",String.valueOf(id));

        return odfMonitoringListValue;
    }

    public ArrayList<ODFMonitoringListValue> getAll_BankName() {

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.BANKLIST_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setBank_Id(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.BANK_ID)));
                    card.setOMC_Name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.OMC_NAME)));
                    card.setBank_Name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BANK_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /****** BRANCH TABLE *****/
    public ODFMonitoringListValue insertBranchName(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.BANK_ID,odfMonitoringListValue.getBank_Id());
        values.put(AppConstant.BRANCH_ID,odfMonitoringListValue.getBranch_Id());
        values.put(AppConstant.BRANCH_NAME,odfMonitoringListValue.getBranch_Name());
        values.put(AppConstant.IFSC_CODE,odfMonitoringListValue.getIFSC_Code());

        long id = db.insert(DBHelper.BANKLIST_BRANCH_TABLE_NAME,null,values);
        Log.d("Inserted_id_branch",String.valueOf(id));

        return odfMonitoringListValue;
    }

    public ArrayList<ODFMonitoringListValue> getAll_BranchName() {

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.BANKLIST_BRANCH_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setBank_Id(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.BANK_ID)));
                    card.setBranch_Id(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.BRANCH_ID)));
                    card.setBranch_Name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BRANCH_NAME)));
                    card.setIFSC_Code(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.IFSC_CODE)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ODFMonitoringListValue insertCategoryList(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_MOTIVATOR_CATEGORY_ID,odfMonitoringListValue.getMotivatorCategoryId());
        values.put(AppConstant.KEY_MOTIVATOR_CATEGORY_NAME,odfMonitoringListValue.getMotivatorCategoryName());

        long id = db.insert(DBHelper.MOTIVATOR_CATEGORY_LIST_TABLE_NAME,null,values);
        Log.d("Inserted_idCategoryList",String.valueOf(id));

        return odfMonitoringListValue;
    }

    public ArrayList<ODFMonitoringListValue> getAllCategoryList() {

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.MOTIVATOR_CATEGORY_LIST_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ODFMonitoringListValue categoryList = new ODFMonitoringListValue();

                    categoryList.setMotivatorCategoryId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MOTIVATOR_CATEGORY_ID)));
                    categoryList.setMotivatorCategoryName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MOTIVATOR_CATEGORY_NAME)));


                    cards.add(categoryList);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }


}
