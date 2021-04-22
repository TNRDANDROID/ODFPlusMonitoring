package com.nic.ODFPlusMonitoring.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nic.ODFPlusMonitoring.Utils.Utils;

public class TimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Do whatever you need to
        Utils.showAlert((Activity) context,"Time is changed");

    }


}
