package com.nic.ODFPlusMonitoring.Model;

import android.graphics.Bitmap;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class ODFMonitoringListValue {

    private int distictCode;
    private String districtName;

    private String blockCode;
    private String VillageListDistrictCode;
    private String VillageListBlockCode;
    private String Description;
    private String Latitude;
    private String selectedBlockCode;

    private int Bank_Id;
    private String OMC_Name;
    private int Branch_Id;
    private String Branch_Name;
    private String PvCode;
    private String PvName;

    private String blockName;



    private String Bank_Name;
    private String IFSC_Code;

    private String Name;
    private String VillageListPvName;
    private String VillageListPvCode;
    private int motivatorCategoryId;
    private String scheduleMasterId;
    private String motivatorId;
    private String scheduleFromDate;
    private String scheduletoDate;
    private String scheduleDCode;
    private String scheduleBCode;
    private String schedulePvCode;

    public String getScheduleDCode() {
        return scheduleDCode;
    }

    public void setScheduleDCode(String scheduleDCode) {
        this.scheduleDCode = scheduleDCode;
    }

    public String getScheduleBCode() {
        return scheduleBCode;
    }

    public void setScheduleBCode(String scheduleBCode) {
        this.scheduleBCode = scheduleBCode;
    }

    public String getSchedulePvCode() {
        return schedulePvCode;
    }

    public void setSchedulePvCode(String schedulePvCode) {
        this.schedulePvCode = schedulePvCode;
    }

    public String getSchedulePvName() {
        return schedulePvName;
    }

    public void setSchedulePvName(String schedulePvName) {
        this.schedulePvName = schedulePvName;
    }

    private String schedulePvName;

    public String getScheduleFromDate() {
        return scheduleFromDate;
    }

    public void setScheduleFromDate(String scheduleFromDate) {
        this.scheduleFromDate = scheduleFromDate;
    }

    public String getScheduletoDate() {
        return scheduletoDate;
    }

    public void setScheduletoDate(String scheduletoDate) {
        this.scheduletoDate = scheduletoDate;
    }

    public String getScheduleMasterId() {
        return scheduleMasterId;
    }

    public void setScheduleMasterId(String scheduleMasterId) {
        this.scheduleMasterId = scheduleMasterId;
    }

    public String getMotivatorId() {
        return motivatorId;
    }

    public void setMotivatorId(String motivatorId) {
        this.motivatorId = motivatorId;
    }



    public String getScheduleDescription() {
        return scheduleDescription;
    }

    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
    }

    private String scheduleDescription;

    public int getMotivatorCategoryId() {
        return motivatorCategoryId;
    }

    public void setMotivatorCategoryId(int motivatorCategoryId) {
        this.motivatorCategoryId = motivatorCategoryId;
    }

    public String getMotivatorCategoryName() {
        return motivatorCategoryName;
    }

    public void setMotivatorCategoryName(String motivatorCategoryName) {
        this.motivatorCategoryName = motivatorCategoryName;
    }

    private String motivatorCategoryName;

    public String getPvName() {
        return PvName;
    }

    public void setPvName(String pvName) {
        PvName = pvName;
    }

    public String getBank_Name() {
        return Bank_Name;
    }

    public void setBank_Name(String bank_Name) {
        Bank_Name = bank_Name;
    }


    public int getBranch_Id() {
        return Branch_Id;
    }

    public void setBranch_Id(int branch_Id) {
        Branch_Id = branch_Id;
    }

    public String getBranch_Name() {
        return Branch_Name;
    }

    public void setBranch_Name(String branch_Name) {
        Branch_Name = branch_Name;
    }

    public String getIFSC_Code() {
        return IFSC_Code;
    }

    public void setIFSC_Code(String IFSC_Code) {
        this.IFSC_Code = IFSC_Code;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public int getDistictCode() {
        return distictCode;
    }

    public void setDistictCode(int distictCode) {
        this.distictCode = distictCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getSelectedBlockCode() {
        return selectedBlockCode;
    }

    public void setSelectedBlockCode(String selectedBlockCode) {
        this.selectedBlockCode = selectedBlockCode;
    }




    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    private String Longitude;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    private Bitmap Image;

    public String getVillageListDistrictCode() {
        return VillageListDistrictCode;
    }

    public void setVillageListDistrictCode(String villageListDistrictCode) {
        VillageListDistrictCode = villageListDistrictCode;
    }

    public String getVillageListBlockCode() {
        return VillageListBlockCode;
    }

    public void setVillageListBlockCode(String villageListBlockCode) {
        VillageListBlockCode = villageListBlockCode;
    }

    public String getPvCode() {
        return PvCode;
    }

    public void setPvCode(String pvCode) {
        this.PvCode = pvCode;
    }

    public String getOMC_Name() {
        return OMC_Name;
    }

    public void setOMC_Name(String OMC_Name) {
        this.OMC_Name = OMC_Name;
    }

    public int getBank_Id() {
        return Bank_Id;
    }

    public void setBank_Id(int bank_Id) {
        Bank_Id = bank_Id;
    }

    public String getVillageListPvName() {
        return VillageListPvName;
    }

    public void setVillageListPvName(String villageListPvName) {
        VillageListPvName = villageListPvName;
    }

    public String getVillageListPvCode() {
        return VillageListPvCode;
    }

    public void setVillageListPvCode(String villageListPvCode) {
        VillageListPvCode = villageListPvCode;
    }
}