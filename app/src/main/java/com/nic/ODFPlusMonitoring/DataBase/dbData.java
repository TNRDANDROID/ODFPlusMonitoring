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

import java.io.ByteArrayOutputStream;
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

        /********************* t_schedule table *********************************/
    public ArrayList<ODFMonitoringListValue> getAllSchedule(String motivator_id) {

        String selection = "motivator_id = ?";
        String[] selectionArgs = new String[]{motivator_id};

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
             cursor = db.query(DBHelper.SCHEDULE,
                   new String[] {"*"}, selection, selectionArgs, null, null, "schedule_id");
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setScheduleId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEDULE_ID)));
                    card.setScheduleMasterId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEDULE_MASTER_ID)));
                    card.setMotivatorId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MOTIVATOR_ID)));
                    card.setScheduleFromDate(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_FROM_DATE)));
                    card.setScheduletoDate(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_TO_DATE)));
                    card.setScheduleDescription(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEDULE_DESCRIPTION)));

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

    public ODFMonitoringListValue insertSchedule(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_SCHEDULE_ID,odfMonitoringListValue.getScheduleId());
        values.put(AppConstant.KEY_SCHEDULE_MASTER_ID,odfMonitoringListValue.getScheduleMasterId());
        values.put(AppConstant.KEY_MOTIVATOR_ID,odfMonitoringListValue.getMotivatorId());
        values.put(AppConstant.KEY_FROM_DATE,odfMonitoringListValue.getScheduleFromDate());
        values.put(AppConstant.KEY_TO_DATE,odfMonitoringListValue.getScheduletoDate());
        values.put(AppConstant.KEY_SCHEDULE_DESCRIPTION,odfMonitoringListValue.getScheduleDescription());
        values.put(AppConstant.KEY_TOTAL_ACTIVITY,odfMonitoringListValue.getTotalActivity());
        values.put(AppConstant.KEY_COMPLETED_ACTIVITY,odfMonitoringListValue.getCompletedActivity());
        values.put(AppConstant.KEY_PENDING_ACTIVITY,odfMonitoringListValue.getPendingActivity());

        long id = db.insert(DBHelper.SCHEDULE,null,values);
        Log.d("Inserted_id_schedule",String.valueOf(id));
        return odfMonitoringListValue;
    }

    public void deleteScheduleTable() {
        db.execSQL("delete from "+ DBHelper.SCHEDULE);
    }

    /********************* t_schedule_village table *********************************/
    public ArrayList<ODFMonitoringListValue> selectScheduledVillage(String motivator_id,String schedule_id) {

        String selection = "motivator_id = ? and schedule_id = ?";
        String[] selectionArgs = new String[]{motivator_id,schedule_id};

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(DBHelper.SCHEDULE_VILLAGE,
                    new String[] {"*"}, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setVillageLinkId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEDULE_VILLAGE_LINK_ID)));
                    card.setScheduleId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEDULE_ID)));
                    card.setMotivatorId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MOTIVATOR_ID)));
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

    public ODFMonitoringListValue insertScheduleVillage(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_SCHEDULE_VILLAGE_LINK_ID,odfMonitoringListValue.getVillageLinkId());
        values.put(AppConstant.KEY_SCHEDULE_ID,odfMonitoringListValue.getScheduleId());
        values.put(AppConstant.KEY_MOTIVATOR_ID,odfMonitoringListValue.getMotivatorId());
        values.put(AppConstant.DISTRICT_CODE,odfMonitoringListValue.getDistictCode());
        values.put(AppConstant.BLOCK_CODE,odfMonitoringListValue.getBlockCode());
        values.put(AppConstant.PV_CODE,odfMonitoringListValue.getPvCode());
        values.put(AppConstant.PV_NAME,odfMonitoringListValue.getPvName());

        long id = db.insert(DBHelper.SCHEDULE_VILLAGE,null,values);
        Log.d("Inserted_id_Vill_Sche",String.valueOf(id));
        return odfMonitoringListValue;
    }

    public void deleteScheduleVillageTable() {
        db.execSQL("delete from "+ DBHelper.SCHEDULE_VILLAGE);
    }
    /********************* t_scheduled_activity table *********************************/
    public ArrayList<ODFMonitoringListValue> selectScheduleActivity(String schedule_id) {

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = "schedule_id = ?";
        String[] selectionArgs = new String[]{schedule_id};

        try {
            cursor = db.query(DBHelper.SCHEDULED_ACTIVITY,
                    new String[] {"*"}, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setScheduleActivityId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEDULE_ACTIVITY_ID)));
                    card.setScheduleId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEDULE_ID)));
                    card.setActivityId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ACTIVITY_ID)));
                    card.setActivityName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ACTIVITY_NAME)));
                    card.setPlaceOfActivity(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PLACE_OF_ACTIVITY)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
              Log.d("Exception" ,e.toString());
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ODFMonitoringListValue insertScheduleActivity(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_SCHEDULE_ACTIVITY_ID,odfMonitoringListValue.getScheduleActivityId());
        values.put(AppConstant.KEY_SCHEDULE_ID,odfMonitoringListValue.getScheduleId());
        values.put(AppConstant.KEY_ACTIVITY_ID,odfMonitoringListValue.getActivityId());
        values.put(AppConstant.KEY_ACTIVITY_NAME,odfMonitoringListValue.getActivityName());
        values.put(AppConstant.KEY_PLACE_OF_ACTIVITY,odfMonitoringListValue.getPlaceOfActivity());

        long id = db.insert(DBHelper.SCHEDULED_ACTIVITY,null,values);
        Log.d("Inserted_id_sche_Activ",String.valueOf(id));
        return odfMonitoringListValue;
    }

    public void deleteScheduleActivityTable() {
        db.execSQL("delete from "+ DBHelper.SCHEDULED_ACTIVITY);
    }

    /************************************** Activity Photos *************************************************/

    public ODFMonitoringListValue insertActivityPhotos(ODFMonitoringListValue odfMonitoringListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_SCHEDULE_ACTIVITY_ID,odfMonitoringListValue.getScheduleActivityId());
        values.put(AppConstant.KEY_SCHEDULE_ID,odfMonitoringListValue.getScheduleId());
        values.put(AppConstant.KEY_LATITUDE,odfMonitoringListValue.getLatitude());
        values.put(AppConstant.KEY_LONGITUDE,odfMonitoringListValue.getLongitude());
        values.put(AppConstant.KEY_TYPE,odfMonitoringListValue.getType());
        values.put(AppConstant.KEY_IMAGE_REMARK,odfMonitoringListValue.getImageRemark());

        Bitmap bitmap = odfMonitoringListValue.getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageInByte = baos.toByteArray();
        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        values.put(AppConstant.KEY_IMAGE,image_str);

        long id = db.insert(DBHelper.SCHEDULED_ACTIVITY_PHOTOS,null,values);
        Log.d("Inserted_id_Activ_photo",String.valueOf(id));
        return odfMonitoringListValue;
    }

    /************************** Get Saved Activity Images ***************************/

    public ArrayList<ODFMonitoringListValue> getSavedActivity() {

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(DBHelper.SAVE_ACTIVITY,
                    new String[]{"*"}, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGE));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setScheduleMasterId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SCHEDULE_MASTER_ID)));
                    card.setMotivatorId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MOTIVATOR_ID)));
                    card.setActivityId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ACTIVITY_ID)));
                    card.setScheduleId(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_SCHEDULE_ID)));
                    card.setDistictCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LATITUDE)));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LONGITUDE)));
                    card.setType(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_TYPE)));
                    card.setDateTime(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DATE_TIME)));
                    card.setImageRemark(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_IMAGE_REMARK)));
                    card.setImage(decodedByte);

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

    public ArrayList<ODFMonitoringListValue> selectImageActivity(String dcode,String bcode, String pvcode,String schedule_id,String activity_id,String type) {

        ArrayList<ODFMonitoringListValue> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = null;
        String[] selectionArgs = null;

        if(!type.isEmpty()){
            selection = "dcode = ? and bcode = ? and pvcode = ? and schedule_id = ? and activity_id = ? and type = ?";
            selectionArgs = new String[]{dcode,bcode,pvcode,schedule_id,activity_id,type};
        }
        else {
            selection = "dcode = ? and bcode = ? and pvcode = ? and schedule_id = ? and activity_id = ?";
            selectionArgs = new String[]{dcode,bcode,pvcode,schedule_id,activity_id};
        }



        try {
            cursor = db.query(DBHelper.SAVE_ACTIVITY,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGE));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    ODFMonitoringListValue card = new ODFMonitoringListValue();
                    card.setMotivatorId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MOTIVATOR_ID)));
                    card.setActivityId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ACTIVITY_ID)));
                    card.setScheduleId(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_SCHEDULE_ID)));
                    card.setDistictCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LATITUDE)));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LONGITUDE)));
                    card.setType(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_TYPE)));
                    card.setDateTime(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DATE_TIME)));
                    card.setImageRemark(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_IMAGE_REMARK)));
                    card.setImage(decodedByte);

                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("Exception" , e.toString());
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public void deleteSavedActivity() {
        db.execSQL("delete from "+ DBHelper.SAVE_ACTIVITY);
    }
}
