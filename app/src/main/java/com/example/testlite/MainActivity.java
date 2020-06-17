package com.example.testlite;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlite.InterfaceModule.AdvertiseResultInterface;
import com.example.testlite.ReceiverModule.BLEBroadcastReceiver;
import com.example.testlite.ServiceModule.AdvertiseTask;
import com.example.testlite.ServiceModule.ScannerTask;
import com.example.testlite.activity.HelperActivity;
import com.example.testlite.app.PreferencesManager;
import com.example.testlite.constant.Constants;
import com.example.testlite.fragment.AddDeviceFragment;
import com.niftymodaldialogeffects.Effectstype;
import com.niftymodaldialogeffects.NiftyDialogBuilder;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AdvertiseResultInterface, BeaconConsumer, MonitorNotifier {
    @BindView(R.id.my_network)
    LinearLayout my_network;
    @BindView(R.id.smart_device)
    LinearLayout smartDevice;
    @BindView(R.id.associate)
    LinearLayout associate;
    @BindView(R.id.title)
    TextView title;
    public BeaconManager beaconManager;
    ScannerTask scannerTask;
    BLEBroadcastReceiver bleBroadcastReceiver;
    private BackgroundPowerSaver backgroundPowerSaver;
    AdvertiseTask advertiseTask;
    String TAG="MainActivity";
    protected LocationManager locationManager;
    NiftyDialogBuilder dialogBuilder;
    Dialog cancel_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        title.setText("Home");
//        smartDevice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                keyDialog();
//            }
//        });

        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            Log.d(TAG, "SDK "+ Build.VERSION.SDK_INT+" App Permissions:");
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    int grantResult = this.checkPermission(p, android.os.Process.myPid(), android.os.Process.myUid());
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, p+"PERMISSION_GRANTED");
                    }
                    else {
                        Log.d(TAG, p+"PERMISSION_DENIED: "+grantResult);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Cannot get permissions due to error", e);
        }

        enableBT();
        backgroundPowerSaver = new BackgroundPowerSaver(this);

//        initiateBeaconService();
        bleBroadcastReceiver=new BLEBroadcastReceiver();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bleBroadcastReceiver, filter);
    }



    @OnClick({R.id.my_network,R.id.smart_device,R.id.dashboard,R.id.group,R.id.demo,R.id.associate})
    public void onViewClicked(View view) {

        Intent intent = new Intent(this, HelperActivity.class);
        switch (view.getId()) {
            case R.id.my_network:
                intent.putExtra(Constants.MAIN_KEY, Constants.MY_NETWORK_CODE);
                break;
            case R.id.smart_device:
//               keyDialog();
                intent.putExtra(Constants.MAIN_KEY, Constants.SMART_DEVICE_CODE);
                break;
            case R.id.dashboard:
                intent.putExtra(Constants.MAIN_KEY, Constants.DASHBOARD_CODE);
                break;
            case R.id.group:
                intent.putExtra(Constants.MAIN_KEY, Constants.GROUP_CODE);
                break;
            case R.id.demo:
                intent.putExtra(Constants.MAIN_KEY, Constants.DEMO_CODE);
                break;

            case R.id.associate:
                intent.putExtra(Constants.MAIN_KEY, Constants.ASSOCIATE);
                break;
        }
        startActivity(intent);
    }



    private void keyDialog(){
            hideKeyboard();
            cancel_dialog = new Dialog(MainActivity.this);
            cancel_dialog.setCanceledOnTouchOutside(true);
            cancel_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            cancel_dialog.setContentView(R.layout.unike_key_dialog);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.80);
            cancel_dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            cancel_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Button btn_submit = cancel_dialog.findViewById(R.id.btn_submit);
            EditText remark_et = cancel_dialog.findViewById(R.id.remark_et);


            btn_submit.setOnClickListener(v -> cancel_dialog.dismiss());
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard();
//                     PreferencesManager.getInstance(MainActivity.this).setUniqueKey(remark_et.getText().toString().trim());
                        Log.e("uniqueKey>>>>>>", remark_et.getText().toString().trim());
                        String inputKey = remark_et.getText().toString().trim();
                        String first,second,third,fourth;
                        first = inputKey.substring(0,1);
                        second = inputKey.substring(1,2);
                        third = inputKey.substring(2,3);
                        fourth = inputKey.substring(3,4);
                        Log.e("ScanningBeacon>>>>>>", first + "," + second + "," + third + "," + fourth);
                    if(!remark_et.getText().toString().equalsIgnoreCase("")) {
                        Intent intent = new Intent(getApplicationContext(), HelperActivity.class);
                        intent.putExtra("Unique_Key",remark_et.getText().toString().trim());
                        intent.putExtra("first_key",first);
                        intent.putExtra("second_key",second);
                        intent.putExtra("third_key",third);
                        intent.putExtra("fourth_key",fourth);
                        intent.putExtra(Constants.MAIN_KEY, Constants.SMART_DEVICE_CODE);
                        startActivity(intent);

                        cancel_dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter Unique Key", Toast.LENGTH_SHORT).show();
                    }

                }



//                    if (inputKey.length()>4){
//
//                }
            });

            cancel_dialog.show();


    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableBT();
        isLocationEnable();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager)this .getSystemService(Context.LOCATION_SERVICE);
        }


    }

    public void isLocationEnable() {
        initializeLocationManager();
        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.e(TAG, "cannot get current location");
            dialogBuilder
                    .withTitle("Enable Location")
                    .withEffect(Effectstype.Newspager)
                    .withMessage("Your location is disable.Please enable it to better scanning.")
                    .withButton1Text("OK")
                    .setButton1Click(v -> {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialogBuilder.dismiss();
                    })
                    .show();
            // no network provider is enabled
        }
        // First get location from Network Provider

    }

    @Override
    protected void onDestroy() {
//        disableBT();
        if (bleBroadcastReceiver!=null)
        {
            unregisterReceiver(bleBroadcastReceiver);
        }
//        stopService();
//        beaconManager.unbind(this);
        super.onDestroy();
    }
//    public void disableBT() {
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (mBluetoothAdapter.isEnabled()){
//            mBluetoothAdapter.disable();
//        }
//    }
    public void enableBT(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }
    }


    @Override
    public void onSuccess(String message) {
        Log.e("MainActivity",message+"");

    }

    @Override
    public void onFailed(String errorMessage) {
        Log.e("MainActivity onScanF",errorMessage+"");

    }

    @Override
    public void onStop(String stopMessage, int resultCode) {
        Log.e("MainActivity onStop",stopMessage+"");

    }

    @Override
    public void onBeaconServiceConnect() {
        Log.e("AppHelper","onBeaconServiceConnect");

    }

    @Override
    public void didEnterRegion(Region region) {
        Log.e("MainActivity ","didEnterRegion");

    }

    @Override
    public void didExitRegion(Region region) {
        Log.e("MainActivity ","didExitRegion");

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        Log.e("MainActivity ","didDetermineStateForRegion");

    }
}
