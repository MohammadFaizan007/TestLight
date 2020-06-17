package com.example.testlite.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.CustomProgress.CustomDialog.AnimatedProgress;
import com.example.testlite.PogoClasses.DeviceClass;
import com.example.testlite.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateGroupAdapter extends BaseAdapter  {
    Activity activity;
    ArrayList<DeviceClass> arrayList;
    ArrayList<DeviceClass> filterarrayList;

    ArrayList<String> selectedLight;

    AnimatedProgress animatedProgress;

    public CreateGroupAdapter(@NonNull Activity context) {
        activity = context;
        arrayList = new ArrayList<>();
        selectedLight=new ArrayList<>();
        filterarrayList = new ArrayList<>();
        animatedProgress = new AnimatedProgress(activity);
        //progressDialog.setCanceledOnTouchOutside(false);
    }
    public void setList(ArrayList<DeviceClass> arrayList)
    {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedLight() {
        return selectedLight;
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

    public void showDialog()
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.light_device_details);
        dialog.show();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.create_group_adapter, parent, false);}
                    DeviceClass deviceClass=arrayList.get(position);
                 deviceClass.setStatus(selectedLight.contains(String.valueOf(position)));
            ViewHolder viewHolder=new ViewHolder(convertView);
            viewHolder.customizeDevice.setOnClickListener(view -> showDialog());
            viewHolder.deviceLight.setText(deviceClass.getDeviceName());
            viewHolder.circleView.setBackground(activity.getResources().getDrawable(deviceClass.getMasterStatus()==0?R.drawable.white_circle_border:R.drawable.yellow_circle));
            if (selectedLight.contains(String.valueOf(position)))
            {
                viewHolder.selectDevice.setText("Remove");
            }
            else {
                viewHolder.selectDevice.setText("Select");
            }
            viewHolder.selectDevice.setOnClickListener(view ->
            {


                if (selectedLight.contains(String.valueOf(position)))
                {
                    viewHolder.selectDevice.setText("Select");

                    selectedLight.remove(String.valueOf(position));
//                    Toast.makeText(activity, "Will be soon.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(selectedLight.size()>=1)
                    {
                        Toast.makeText(activity, "Maximum one value", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    selectedLight.add(String.valueOf(position));
                    viewHolder.selectDevice.setText("Remove");
                }
            });

        return convertView;
    }

    void showAlert(String message, String title) {
        if (title.length() < 1)
            title = "Alert";
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton("Remove", (dialog1, id) -> {
            // User clicked OK button
//            acceptRequest(2,position);
            dialog1.dismiss();
            Toast.makeText(activity, "Will be soon", Toast.LENGTH_SHORT).show();

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }








    static class ViewHolder {
        @BindView(R.id.customize_device)
        Button customizeDevice;
        @BindView(R.id.select_device)
        Button selectDevice;

        @BindView(R.id.review_1)
        ImageButton circleView;

        @BindView(R.id.create_group_light)
        TextView deviceLight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
