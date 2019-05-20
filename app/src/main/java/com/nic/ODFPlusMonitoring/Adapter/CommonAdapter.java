package com.nic.ODFPlusMonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nic.ODFPlusMonitoring.Model.ODFMonitoringListValue;
import com.nic.ODFPlusMonitoring.R;

import java.util.List;

/**
 * Created by shanmugapriyan on 25/05/16.
 */
public class CommonAdapter extends BaseAdapter {
    private List<ODFMonitoringListValue> ODFMonitoringListValue;
    private Context mContext;
    private String type;


    public CommonAdapter(Context mContext, List<ODFMonitoringListValue> ODFMonitoringListValue, String type) {
        this.ODFMonitoringListValue = ODFMonitoringListValue;
        this.mContext = mContext;
        this.type = type;
    }


    public int getCount() {
        return ODFMonitoringListValue.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.spinner_drop_down_item, parent, false);
//        TextView tv_type = (TextView) view.findViewById(R.id.tv_spinner_item);
        View view = inflater.inflate(R.layout.spinner_value, parent, false);
        TextView tv_type = (TextView) view.findViewById(R.id.spinner_list_value);
        ODFMonitoringListValue ODFMonitoringList = ODFMonitoringListValue.get(position);
        if(type.equalsIgnoreCase("DistrictList")) {
            tv_type.setText(ODFMonitoringList.getDistrictName());
        } else if (type.equalsIgnoreCase("BlockList")) {
            tv_type.setText(ODFMonitoringList.getBlockName());
        }else if(type.equalsIgnoreCase("VillageList")){
            tv_type.setText(ODFMonitoringList.getVillageListPvName());
        } else if (type.equalsIgnoreCase("CategoryList")) {
            tv_type.setText(ODFMonitoringList.getMotivatorCategoryName());
        }
        return view;
    }
}
