package com.example.testlite.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.example.testlite.EncodeDecodeModule.ByteQueue;
import com.example.testlite.InterfaceModule.AdvertiseResultInterface;
import com.example.testlite.InterfaceModule.ReceiverResultInterface;
import com.example.testlite.PogoClasses.BeconDeviceClass;
import com.example.testlite.ServiceModule.ScannerTask;
import com.example.testlite.activity.AppHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class AssociateStatusAdapter extends BaseAdapter implements AdvertiseResultInterface, ReceiverResultInterface {
    Activity activity;
    ArrayList<BeconDeviceClass> arrayList;
    ScannerTask scannerTask;
    AnimatedProgress animatedProgress;
    String TAG = this.getClass().getSimpleName();
    boolean isAdvertisingFinished = false;

    public AssociateStatusAdapter(@NonNull Activity context) {
        activity = context;
        arrayList = new ArrayList<>();
        scannerTask = new ScannerTask(activity, this);
        animatedProgress = new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);
        if (AppHelper.IS_TESTING) {
            setArrayList();
        }
    }

    public void clearList() {
        if (this.arrayList == null)
            this.arrayList = new ArrayList<>();
        this.arrayList.clear();
        notifyDataSetChanged();

    }

    public void setArrayList(ArrayList<BeconDeviceClass> arrayList) {
//        if(this.arrayList==null)
//            this.arrayList=new ArrayList<>();
//        this.arrayList.clear();
        this.arrayList = arrayList;
        notifyDataSetChanged();

    }

    public void setArrayList() {
        for (int i = 0; i <= 20; i++) {
            BeconDeviceClass beconDeviceClass = new BeconDeviceClass();
            beconDeviceClass.setBeaconUID(i + 10);
            beconDeviceClass.setDeviceUid((i + 10) + "");
            beconDeviceClass.setDeriveType(0x01);
            arrayList.add(beconDeviceClass);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public BeconDeviceClass getItem(int position) {
        if (arrayList.size() <= position)
            return null;
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }



    @Override
    public void onSuccess(String message) {
        animatedProgress.showProgress();
        Log.w(TAG, "Advertising start");
    }

    @Override
    public void onFailed(String errorMessage) {
        if (animatedProgress == null)
            return;
        Toast.makeText(activity, "Advertising Failed.", Toast.LENGTH_SHORT).show();
        animatedProgress.hideProgress();
    }

    @Override
    public void onStop(String stopMessage, int resultCode) {
        scannerTask = new ScannerTask(activity, this);
        scannerTask.setRequestCode(resultCode);
        scannerTask.start();
        isAdvertisingFinished = true;
        Log.w(TAG, "Advertising stop" + resultCode);
    }



    @Override
    public void onScanSuccess(int successCode, ByteQueue byteQueue) {
        if (animatedProgress == null)
            return;
        animatedProgress.hideProgress();

    }

    @Override
    public void onScanFailed(int errorCode) {
        if (animatedProgress == null)
            return;
        animatedProgress.hideProgress();

    }

    static class ViewHolder {
//        @BindView(R.id.name_image)
//        ImageView review1;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

