package com.nic.ODFPlusMonitoring.Model;

import android.graphics.Bitmap;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class BlockListValue {

    private String distictCode;
    private String blockCode;
    private String VillageListDistrictCode;
    private String VillageListBlockCode;
    private String Description;
    private String Latitude;
    private String observation;
    private String date_of_inspection;
    private String inspection_remark;


    private String VillageListPvCode;
    private String VillageListPvName;


    private String pvCode;
    private String blockName;
    private String schemeName;
    private String projectName;
    private String schemeSequentialID;
    private String selectedBlockCode;
    private String schemeID;
    private String workGroupID;
    private String workTypeID;
    private String workID;
    private String workName;
    private String asAmount;
    private String tsAmount;
    private String isHighValue;
    private String workStageCode;
    private String workStageOrder;
    private String workStageName;
    private String projectID;
    private String observationName;
    private int observationID;
    private int inspectionID;
    private String CreatedDate;
    private String CreatedIpAddress;
    private String CreatedUserName;
    private String Actionresult;
    public boolean setItemSelected;
    private String OnlineInspectID;
    private String detail;
    private int ActionID;
    private String inspectedOffUserId;
    private String inspectedOffName;
    private String inspectedOffDesignName;
    private String ActionOffName;
    private String ActionOffDesignName;
    private String districtName;

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getInspectedOffUserId() {
        return inspectedOffUserId;
    }

    public void setInspectedOffUserId(String inspectedOffUserId) {
        this.inspectedOffUserId = inspectedOffUserId;
    }

    public String getInspectedOffName() {
        return inspectedOffName;
    }

    public void setInspectedOffName(String inspectedOffName) {
        this.inspectedOffName = inspectedOffName;
    }

    public String getInspectedOffDesignName() {
        return inspectedOffDesignName;
    }

    public void setInspectedOffDesignName(String inspectedOffDesignName) {
        this.inspectedOffDesignName = inspectedOffDesignName;
    }
    public String getActionOffName() {
        return ActionOffName;
    }

    public void setActionOffName(String actionOffName) {
        this.ActionOffName = actionOffName;
    }
    public String getActionOffDesignName() {
        return ActionOffDesignName;
    }

    public void setActionOffDesignName(String actionOffDesignName) {
        this.ActionOffDesignName = actionOffDesignName;
    }


    public int getActionID() {
        return ActionID;
    }

    public void setActionID(int actionID) {
        ActionID = actionID;
    }

    public String getDate_of_Action() {
        return Date_of_Action;
    }

    public void setDate_of_Action(String date_of_Action) {
        Date_of_Action = date_of_Action;
    }

    public String getAction_remark() {
        return Action_remark;
    }

    public void setAction_remark(String action_remark) {
        Action_remark = action_remark;
    }

    private String Date_of_Action;
    private String Action_remark;
    private String Delete_Flag;

    public String getDelete_Flag() {
        return Delete_Flag;
    }

    public void setDelete_Flag(String delete_Flag) {
        Delete_Flag = delete_Flag;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }





    public String getActionresult() {
        return Actionresult;
    }

    public void setActionresult(String actionresult) {
        Actionresult = actionresult;
    }



    public String getOnlineInspectID() {
        return OnlineInspectID;
    }

    public void setOnlineInspectID(String onlineInspectID) {
        OnlineInspectID = onlineInspectID;
    }


    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreatedIpAddress() {
        return CreatedIpAddress;
    }

    public void setCreatedIpAddress(String createdIpAddress) {
        CreatedIpAddress = createdIpAddress;
    }

    public String getCreatedUserName() {
        return CreatedUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        CreatedUserName = createdUserName;
    }



    public int getInspectionID() {
        return inspectionID;
    }

    public void setInspectionID(int inspectionID) {
        this.inspectionID = inspectionID;
    }




//    private String inspectionID;

    public int getObservationID() {
        return observationID;
    }

    public void setObservationID(int observationID) {
        this.observationID = observationID;
    }


//    public BlockListValue(String bcode, String scheme_id, String work_id, String as_value, String is_high_value) {
//        this.blockCode = bcode;
//        this.schemeID = scheme_id;
//        this.workID = work_id;
//        this.asAmount = as_value;
//        this.isHighValue = is_high_value;
//    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    private String financialYear;

    public String getSchemeSequentialID() {
        return schemeSequentialID;
    }

    public void setSchemeSequentialID(String schemeSequentialID) {
        this.schemeSequentialID = schemeSequentialID;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Name;

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectID() {
        return projectID;
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

    public String getSchemeID() {
        return schemeID;
    }

    public void setSchemeID(String schemeID) {
        this.schemeID = schemeID;
    }

    public void setWorkGroupID(String workGroupID) {
        this.workGroupID = workGroupID;
    }

    public String getWorkGroupID() {
        return workGroupID;
    }

    public void setWorkTypeID(String workTypeID) {
        this.workTypeID = workTypeID;
    }

    public String getWorkTypeID() {
        return workTypeID;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getAsAmount() {
        return asAmount;
    }

    public void setAsAmount(String asAmount) {
        this.asAmount = asAmount;
    }

    public String getTsAmount() {
        return tsAmount;
    }

    public void setTsAmount(String tsAmount) {
        this.tsAmount = tsAmount;
    }

    public String getIsHighValue() {
        return isHighValue;
    }

    public void setIsHighValue(String isHighValue) {
        this.isHighValue = isHighValue;
    }

    public void setWorkStageCode(String workStageCode) {
        this.workStageCode = workStageCode;
    }

    public String getWorkStageCode() {
        return workStageCode;
    }

    public void setWorkStageOrder(String workStageOrder) {
        this.workStageOrder = workStageOrder;
    }

    public String getWorkStageOrder() {
        return workStageOrder;
    }

    public void setWorkStageName(String workStageName) {
        this.workStageName = workStageName;
    }

    public String getWorkStageName() {
        return workStageName;
    }

    public void setObservationName(String observationName) {
        this.observationName = observationName;
    }

    public String getObservationName() {
        return observationName;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getDate_of_inspection() {
        return date_of_inspection;
    }

    public void setDate_of_inspection(String date_of_inspection) {
        this.date_of_inspection = date_of_inspection;
    }

    public String getInspection_remark() {
        return inspection_remark;
    }

    public void setInspection_remark(String inspection_remark) {
        this.inspection_remark = inspection_remark;
    }

//    public String getInspectionID() {
//        return inspectionID;
//    }
//
//    public void setInspectionID(String inspectionID) {
//        this.inspectionID = inspectionID;
//    }

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

    public String getVillageListPvCode() {
        return VillageListPvCode;
    }

    public void setVillageListPvCode(String villageListPvCode) {
        VillageListPvCode = villageListPvCode;
    }

    public String getVillageListPvName() {
        return VillageListPvName;
    }

    public void setVillageListPvName(String villageListPvName) {
        VillageListPvName = villageListPvName;
    }

    public String getPvCode() {
        return pvCode;
    }

    public void setPvCode(String pvCode) {
        this.pvCode = pvCode;
    }

    public boolean isSetItemSelected() {
        return setItemSelected;
    }

    public void setSetItemSelected(boolean setItemSelected) {
        this.setItemSelected = setItemSelected;
    }

}