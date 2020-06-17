package com.example.testlite.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testlite.PogoClasses.DeviceClass;
import com.example.testlite.R;
import com.example.testlite.activity.HelperActivity;
import com.example.testlite.constant.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AssociateListAdapter extends BaseAdapter {
    Dialog pinStatus_dialog;
    Dialog choose_dialog;
    Dialog attribute_dialog;
    Activity activity;
    ArrayList<DeviceClass> arrayList;
    DeviceClass deviceClass;
    String TAG = this.getClass().getSimpleName();
    int selectedPosition = -1;

    public AssociateListAdapter(@NonNull Activity context) {
        activity = context;
        arrayList = new ArrayList<>();
//        scannerTask=new ScannerTask(activity,this);
//        animatedProgress=new AnimatedProgress(activity);
//        animatedProgress.setCancelable(false);

    }

    public void setList(List<DeviceClass> arrayList1) {
        arrayList.clear();
        arrayList.addAll(arrayList1);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public DeviceClass getItem(int position) {
        if (arrayList.size() <= position)
            return null;
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.associate_list_adapter, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);
        DeviceClass deviceClass = arrayList.get(position);
        viewHolder.dashboard_deviceName.setText(deviceClass.getDeviceName());
//        viewHolder.icon_delete.setOnClickListener(view -> {
//            deleteDialog(position);

//        });
//        viewHolder.read.setOnClickListener(v -> {
//           Intent intent = new Intent(activity, HelperActivity.class);
//            intent.putExtra(Constants.MAIN_KEY, Constants.READ_ASSOCIATE);
//            intent.putExtra(Constants.LIGHT_DETAIL_KEY, arrayList.get(position));
//            activity.startActivity(intent);
//
//        });

        viewHolder.add.setOnClickListener(v -> {
            Intent intent = new Intent(activity, HelperActivity.class);
            intent.putExtra(Constants.MAIN_KEY, Constants.ADD_ASSOCIATE);
            intent.putExtra(Constants.LIGHT_DETAIL_KEY, arrayList.get(position));
            activity.startActivity(intent);

        });

        return convertView;
    }





//    void deleteDialog(int position) {
//        selectedPosition = position;
//        DeviceClass deviceClass = arrayList.get(position);
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setMessage("Are you sure you want to delete " + deviceClass.getDeviceName())
//                .setTitle("Remove FDU");
//        builder.setPositiveButton("delete", (dialog1, id) -> {
//            dialog1.dismiss();
//            if (sqlHelper.deleteDevice(deviceClass.getDeviceUID()) > 0) {
//                Toast.makeText(activity, "FDU Deleted.", Toast.LENGTH_SHORT).show();
//                activity.onBackPressed();
//            } else
//                Toast.makeText(activity, "Some Error to Delete FDU", Toast.LENGTH_SHORT).show();
//
//        });
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            dialog.dismiss();
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    static class ViewHolder {

        @BindView(R.id.dashboard_deviceName)
        TextView dashboard_deviceName;
        @BindView(R.id.read)
        Button read;
        @BindView(R.id.add)
        Button add;
//        @BindView(R.id.icon_delete)
//        ImageView icon_delete;
//        @BindView(R.id.detail_circle_size)
//        ImageButton detail_circle_size;

        ViewHolder(View view) {

            ButterKnife.bind(this, view);

        }
    }


}


