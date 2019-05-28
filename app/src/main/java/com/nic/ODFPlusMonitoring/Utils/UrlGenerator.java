package com.nic.ODFPlusMonitoring.Utils;


/**
 * Created by Achanthi Sundar  on 21/03/16.
 */
public class UrlGenerator {



    public static String getLoginUrl() {
        return "https://www.tnrd.gov.in/project/webservices_forms/login_service/login_services.php";
    }

    public static String getServicesListUrl() {
        return "https://www.tnrd.gov.in/project/webservices_forms/master_services/master_services.php";
    }

    public static String getInspectionServicesListUrl() {
        return "https://www.tnrd.gov.in/project/webservices_forms/inspection/inspection_services.php";
    }

//    public static String getTnrdHostName() {
//        return "www.tnrd.gov.in";
//    }

    public static String getTnrdHostName() {
        return "www.tnrd.gov.in";
    }

    public static String getOpenUrl() {
        return "https://www.tnrd.gov.in/project/webservices_forms/open_services/open_services.php";
    }

    public static String getMotivatorCategory() {
        return "https://www.tnrd.gov.in/project/webservices_forms/odf/odf_open_services.php";
    }

    public static String getMotivatorSchedule() {
        return "https://www.tnrd.gov.in/project/webservices_forms/odf/odf_services.php";
    }
}
