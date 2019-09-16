package com.nic.ODFPlusMonitoring.Utils;


import com.nic.ODFPlusMonitoring.Application.NICApplication;
import com.nic.ODFPlusMonitoring.R;

/**
 * Created by Achanthi Sundar  on 21/03/16.
 */
public class UrlGenerator {



    public static String getLoginUrl() {
        return NICApplication.getAppString(R.string.LOGIN_URL);
    }


    public static String getOpenUrl() {
        return NICApplication.getAppString(R.string.OPEN_SERVICES_URL);
    }

    public static String getMotivatorCategory() {
        return NICApplication.getAppString(R.string.ODF_OPEN_SERVICES_URL);
    }

    public static String getMotivatorSchedule() {
        return NICApplication.getAppString(R.string.ODF_SERVICES_TEST_URL);
    }

    public static String getTnrdHostName() {
        return NICApplication.getAppString(R.string.TNRD_HOST_NAME);
    }
}
