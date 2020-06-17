package com.example.testlite.fragment;


import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.example.testlite.EncodeDecodeModule.ByteQueue;
import com.example.testlite.InterfaceModule.AdvertiseResultInterface;
import com.example.testlite.InterfaceModule.ReceiverResultInterface;
import com.example.testlite.PogoClasses.DeviceClass;
import com.example.testlite.R;
import com.example.testlite.ServiceModule.AdvertiseTask;
import com.example.testlite.ServiceModule.ScannerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.testlite.EncodeDecodeModule.RxMethodType.ADD_ASSOCIATE;

public class AssociateAddFragment extends Fragment implements AdvertiseResultInterface, ReceiverResultInterface {
    DeviceClass deviceClass;
    String TAG = this.getClass().getSimpleName();
    Activity activity;
    Unbinder unbinder;
    int requestCode;
    AnimatedProgress animatedProgress;
    ScannerTask scannerTask;
    @BindView(R.id.uid_no)
    EditText uid_no;

    public AssociateAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_associate, container, false);
        activity = getActivity();
        unbinder = ButterKnife.bind(this, view);
        scannerTask = new ScannerTask(activity, this);
        animatedProgress = new AnimatedProgress(activity);
        animatedProgress.setCancelable(false);
        return view;
    }

    public void setDeviceData(DeviceClass deviceData) {
        this.deviceClass = deviceData;
    }


    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                AdvertiseTask advertiseTask = new AdvertiseTask(activity);
                ByteQueue byteQueue = new ByteQueue();
                byteQueue.push(ADD_ASSOCIATE);
                byteQueue.pushU4B(Long.valueOf(uid_no.getText().toString().trim()));

                advertiseTask = new AdvertiseTask(AssociateAddFragment.this, activity, 5 * 1000);
                animatedProgress.setText("Uploading");
                advertiseTask.setByteQueue(byteQueue);
                Log.e("Check>>>>", byteQueue.toString());
                advertiseTask.setSearchRequestCode(ADD_ASSOCIATE);
                advertiseTask.startAdvertising();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSuccess(String message) {
        animatedProgress.showProgress();
        Log.w(TAG, "Uploading");

    }

    @Override
    public void onFailed(String errorMessage) {
        if (animatedProgress == null)
            return;
        Toast.makeText(activity, "Uploading", Toast.LENGTH_SHORT).show();
        animatedProgress.hideProgress();
        activity.onBackPressed();
        Log.w(TAG, "onScanFailed " + errorMessage);

    }

    @Override
    public void onStop(String stopMessage, int resultCode) {
        if (animatedProgress != null)
            animatedProgress.hideProgress();
        activity.onBackPressed();
        ContentValues contentValues = new ContentValues();


    }

    @Override
    public void onScanSuccess(int successCode, ByteQueue byteQueue) {
        if (animatedProgress == null)
            return;
        animatedProgress.hideProgress();
        activity.onBackPressed();

    }

    @Override
    public void onScanFailed(int errorCode) {
        if (animatedProgress == null)
            return;
        animatedProgress.hideProgress();
        activity.onBackPressed();

    }
    @Override
    public void onResume() {
        super.onResume();
//        hideKeyboard();
    }

}
