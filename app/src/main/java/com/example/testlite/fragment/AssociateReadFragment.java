package com.example.testlite.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.example.testlite.EncodeDecodeModule.ByteQueue;
import com.example.testlite.InterfaceModule.AdvertiseResultInterface;
import com.example.testlite.InterfaceModule.MyBeaconScanner;
import com.example.testlite.PogoClasses.BeconDeviceClass;
import com.example.testlite.PogoClasses.DeviceClass;
import com.example.testlite.R;
import com.example.testlite.ServiceModule.AdvertiseTask;
import com.example.testlite.ServiceModule.ScanningBeacon;
import com.example.testlite.activity.AppHelper;
import com.example.testlite.adapter.AssociateStatusAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AssociateReadFragment extends Fragment implements MyBeaconScanner, AdvertiseResultInterface {
    ArrayAdapter<CharSequence> adapter;
    int movement=150;
    ScanningBeacon scanningBeacon;
    boolean isAdvertisingFinished=false;
    AdvertiseTask advertiseTask;
    AnimatedProgress animatedProgress;
    String TAG=this.getClass().getSimpleName();
    Unbinder unbinder;
    Activity activity;
    AssociateStatusAdapter associateStatusAdapter;
    @BindView(R.id.allDevice_list)
    ListView allDeviceList;

    DeviceClass deviceClass;

    Handler handler ;
    private Runnable runnable= () -> {
        if(animatedProgress!=null)
        {
            animatedProgress.hideProgress();
        }

    };

    public AssociateReadFragment() {
        // Required empty public constructor
    }

    private void handlerProgressar() {
        animatedProgress.showProgress();
        handler = new Handler();
        handler.postDelayed(runnable, 15 * 1000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_device_list, container, false);
        activity = getActivity();
        unbinder = ButterKnife.bind(this, view);
//        deviceName.setText("Name  ="+"   "+deviceClass.getDeviceName());
        associateStatusAdapter=new AssociateStatusAdapter(activity);
        allDeviceList.setAdapter(associateStatusAdapter);
        scanningBeacon=new ScanningBeacon(activity);
        scanningBeacon.setMyBeaconScanner(this);

        animatedProgress=new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);

//        Toast.makeText(getActivity(),  PreferencesManager.getInstance(activity).getUniqueKey(), Toast.LENGTH_SHORT).show();

        ByteQueue byteQueue=new ByteQueue();
//        byteQueue.push(RELAY_STATUS);   //// Light Level Command method type
        byteQueue.pushU4B(deviceClass.getDeviceUID());
        Log.e("Relay_status",byteQueue.toString());
        advertiseTask=new AdvertiseTask(this,activity,5*1000);
        animatedProgress.setText("Scanning");
        advertiseTask.setByteQueue(byteQueue);
//        advertiseTask.setSearchRequestCode(RELAY_STATUS);
        advertiseTask.startAdvertising();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isAdvertisingFinished)
        {
            animatedProgress.setText("Scanning");
            scanningBeacon.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanningBeacon.stop();
        if(handler!=null)
            handler.removeCallbacks(runnable);
        unbinder.unbind();
    }

    @Override
    public void onSuccess(String message) {
        handlerProgressar();
        Log.e(TAG,"Scan Associate");

    }


    @Override
    public void onFailed(String errorMessage) {
        isAdvertisingFinished=true;
        scanningBeacon.start();
        animatedProgress.setText("Scanning");

    }

    @Override
    public void onStop() {
        scanningBeacon.stop();
        super.onStop();
    }

    @Override
    public void onStop(String stopMessage, int resultCode) {
        isAdvertisingFinished=true;
        animatedProgress.setText("Scanning");
        scanningBeacon.start();

    }

    @Override
    public void onBeaconFound(ArrayList<BeconDeviceClass> beaconList) {
        if(associateStatusAdapter==null)
            associateStatusAdapter=new AssociateStatusAdapter(activity);
        associateStatusAdapter.setArrayList(beaconList);

    }

    @Override
    public void noBeaconFound() {
//        scanningBeacon.stop();
//        noRecFound.setVisibility(View.VISIBLE);
//        Toast.makeText(activity, "No Response Found.", Toast.LENGTH_LONG).show();
        Log.w("AddDeviceFragment","No Beacon founded");
        if(!AppHelper.IS_TESTING)
            associateStatusAdapter.clearList();
//        Toast.makeText(activity, "No Response Found.", Toast.LENGTH_SHORT).show();

    }
    public void setDeviceData(DeviceClass deviceData) {
        this.deviceClass = deviceData;
    }
}
