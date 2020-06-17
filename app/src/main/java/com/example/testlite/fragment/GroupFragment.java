package com.example.testlite.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


import androidx.fragment.app.Fragment;

import com.example.testlite.R;
import com.example.testlite.activity.HelperActivity;
import com.example.testlite.adapter.GroupAdapter;
import com.example.testlite.constant.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GroupFragment extends Fragment {

    @BindView(R.id.group_device_list)
    ListView groupDeviceList;
    GroupAdapter groupAdapter;
    Activity activity;
    Unbinder unbinder;
    @BindView(R.id.create_new_group)
    Button createNewGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = getActivity();
        if (activity == null)
            return view;
        groupAdapter = new GroupAdapter(activity);
        groupDeviceList.setAdapter(groupAdapter);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (groupAdapter!=null)
            groupAdapter.getAllGroups();
    }

    @OnClick(R.id.create_new_group)
    public void onViewClicked()
    {

        Intent intent = new Intent(activity, HelperActivity.class);
        intent.putExtra(Constants.MAIN_KEY, Constants.CREATE_GROUP);
        activity.startActivity(intent);
    }
}
