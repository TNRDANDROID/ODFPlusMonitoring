package com.nic.ODFPlusMonitoring.Model;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class ODFMonitoringListValue {

    private String distictCode;
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

    private Integer scheduleId;
    private Integer scheduleMasterId;
    private Integer motivatorId;
    private String scheduleFromDate;
    private String scheduletoDate;
    private String scheduleDescription;
    private Integer totalActivity;
    private Integer completedActivity;
    private Integer pendingActivity;

    private Integer VillageLinkId;
    private Integer scheduleActivityId;
    private Integer ActivityId;
    private String ActivityName;
    private String placeOfActivity;
    private Integer noOfPhotos;
    private Integer serialNo;

    private String type;
    private String imageRemark;
    private String dateTime;
    private String imageAvailable;
    private String activityStatus;

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }
    public String getImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(String imageAvailable) {
        this.imageAvailable = imageAvailable;
    }

    public String getType() {
        return type;
    }

    public void setType(String pointType) {
        this.type = pointType;
    }

    public String getImageRemark() {
        return imageRemark;
    }

    public void setImageRemark(String imageRemark) {
        this.imageRemark = imageRemark;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getScheduleActivityId() {
        return scheduleActivityId;
    }

    public void setScheduleActivityId(Integer scheduleActivityId) {
        this.scheduleActivityId = scheduleActivityId;
    }

    public Integer getActivityId() {
        return ActivityId;
    }

    public void setActivityId(Integer activityId) {
        ActivityId = activityId;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public String getPlaceOfActivity() {
        return placeOfActivity;
    }

    public void setPlaceOfActivity(String placeOfActivity) {
        this.placeOfActivity = placeOfActivity;
    }

    public Integer getNoOfPhotos() {
        return noOfPhotos;
    }

    public void setNoOfPhotos(Integer noOfPhotos) {
        this.noOfPhotos = noOfPhotos;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getVillageLinkId() {
        return VillageLinkId;
    }

    public void setVillageLinkId(Integer villageLinkId) {
        VillageLinkId = villageLinkId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getScheduleMasterId() {
        return scheduleMasterId;
    }

    public void setScheduleMasterId(Integer scheduleMasterId) {
        this.scheduleMasterId = scheduleMasterId;
    }

    public Integer getMotivatorId() {
        return motivatorId;
    }

    public void setMotivatorId(Integer motivatorId) {
        this.motivatorId = motivatorId;
    }

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

    public Integer getTotalActivity() {
        return totalActivity;
    }

    public void setTotalActivity(Integer totalActivity) {
        this.totalActivity = totalActivity;
    }

    public Integer getCompletedActivity() {
        return completedActivity;
    }

    public void setCompletedActivity(Integer completedActivity) {
        this.completedActivity = completedActivity;
    }

    public Integer getPendingActivity() {
        return pendingActivity;
    }

    public void setPendingActivity(Integer pendingActivity) {
        this.pendingActivity = pendingActivity;
    }

    public String getScheduleDescription() {
        return scheduleDescription;
    }

    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
    }

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


    public String getDistictCode() {
        return distictCode;
    }

    public void setDistictCode(String distictCode) {
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