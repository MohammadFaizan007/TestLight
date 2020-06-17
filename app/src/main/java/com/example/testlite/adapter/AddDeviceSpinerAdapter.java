package com.example.testlite.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testlite.DatabaseModule.DatabaseConstant;
import com.example.testlite.PogoClasses.GroupDetailsClass;
import com.example.testlite.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.testlite.activity.AppHelper.sqlHelper;


public class AddDeviceSpinerAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<GroupDetailsClass> groupDetailsClasses;

    ProgressDialog progressDialog;
    ArrayList<String> selectedLight;

    public AddDeviceSpinerAdapter(@NonNull Activity context) {
        activity = context;
        groupDetailsClasses = new ArrayList<>();
        selectedLight=new ArrayList<>();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCanceledOnTouchOutside(false);
        getAllGroups();
    }
    public void getAllGroups()
    {
        groupDetailsClasses.clear();
        Cursor cursor = sqlHelper.getAllGroup();
        if (cursor.moveToFirst())
        {
            do {
                GroupDetailsClass groupData = new GroupDetailsClass();
                groupData.setGroupId(cursor.getInt(cursor.getColumnIndex(DatabaseConstant.COLUMN_GROUP_ID)));
                groupData.setGroupDimming(cursor.getInt(cursor.getColumnIndex(DatabaseConstant.COLUMN_GROUP_PROGRESS)));
                groupData.setGroupName(cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_GROUP_NAME)));
                groupData.setGroupStatus(cursor.getInt(cursor.getColumnIndex(DatabaseConstant.COLUMN_GROUP_STATUS)) == 1);
                groupDetailsClasses.add(groupData);
                // do what ever you want here
            }
            while (cursor.moveToNext());
        }
//
        cursor.close();
    }

    @Override
    public int getCount() {
        return groupDetailsClasses.size();
    }

    @Override
    public GroupDetailsClass getItem(int position) {
        if (groupDetailsClasses.size() <= position)
            return null;
        return groupDetailsClasses.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.add_device_spiner_adapter, parent, false);
        }

        GroupDetailsClass deviceClass=groupDetailsClasses.get(position);

        ViewHolder viewHolder=new ViewHolder(convertView);
//        viewHolder.customizeDevice.setOnClickListener(view -> showDialog());
        viewHolder.deviceLight.setText(deviceClass.getGroupName());

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.spinner_group_name)
        TextView deviceLight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
