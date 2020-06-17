package com.example.testlite.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testlite.DatabaseModule.DatabaseConstant;
import com.example.testlite.PogoClasses.DeviceClass;
import com.example.testlite.R;
import com.example.testlite.adapter.AssociateListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.testlite.activity.AppHelper.sqlHelper;


public class AssociateFragment extends Fragment {
    Activity activity;
    Unbinder unbinder;
    @BindView(R.id.allDevice_list)
    ListView allDeviceList;
    AssociateListAdapter associateListAdapter;


    ArrayList<DeviceClass> deviceList;

    public AssociateFragment() {
        // Required empty public constructor
//        list = new ArrayList<>();
        deviceList = new ArrayList<>();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getDevice();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.all_device_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity=getActivity();
        if (activity==null)
            return view;
        associateListAdapter =new AssociateListAdapter(activity);
        allDeviceList.setAdapter(associateListAdapter);
        return view;
    }

    public void getDevice() {
        deviceList=new ArrayList<>();
        Cursor cursor=sqlHelper.getAllDevice(DatabaseConstant.ADD_DEVICE_TABLE);
        if (cursor.moveToFirst()) {
            do{
                DeviceClass deviceClass=new DeviceClass();
                deviceClass.setDeviceName(cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_NAME)));
                deviceClass.setDeviceUID(cursor.getLong(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_UID)));
//                deviceClass.setAirType(cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_AIR_TYPE)));
//                deviceClass.setDamperType(cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_DAMPER_TYPE)));
//                deviceClass.setAhuNumber(cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_AHU_NUMBER)));
//                deviceClass.setFlourNumber(cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_FLOUR_NUMBER)));
                deviceClass.setStatus(cursor.getInt(cursor.getColumnIndex(DatabaseConstant.COLUMN_DEVICE_STATUS))==1);
                deviceList.add(deviceClass);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        associateListAdapter.setList(deviceList);
    }

    @Override
    public void onResume() {
        getDevice();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
