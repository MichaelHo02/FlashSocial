package com.michael.flashsocial.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.michael.flashsocial.R;
import com.michael.flashsocial.activity.FolderCreationActivity;
import com.michael.flashsocial.adapter.GroupItemAdapter;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.database.GroupItemDB;
import com.michael.flashsocial.model.GroupItem;
import com.michael.flashsocial.utils.NavigationUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CycleRule {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private MaterialToolbar materialToolbar;
    private View view;

    private List<GroupItem> groupItemList;
    private GroupItemAdapter groupItemAdapter;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        initUIAction();
        return view;
    }

    @Override
    public void initUI() {
        materialToolbar = view.findViewById(R.id.frag_home_toolbar);
        recyclerView = view.findViewById(R.id.frag_home_rv);
        recycleViewInit();
        toolbarViewInit();
    }

    @Override
    public void initUIAction() {
        materialToolbar.setOnMenuItemClickListener(this::handleMenuItemClick);
    }

    private void toolbarViewInit() {
        materialToolbar.inflateMenu(R.menu.fragment_home_menu);
    }

    private void recycleViewInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        groupItemList = getGroupItemList();
        groupItemAdapter = new GroupItemAdapter(groupItemList);
        recyclerView.setAdapter(groupItemAdapter);
    }

    private boolean handleMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.frag_home_add:
                return navigateAddFolder();
            case R.id.frag_home_setting:
                return navigateSetting();
        }
        return false;
    }

    private boolean navigateAddFolder() {
        NavigationUtil.navigateActivity(this, view.getContext(), FolderCreationActivity.class, 100);
        return true;
    }

    private List<GroupItem> getGroupItemList() {
        return GroupItemDB.getInstance(this.getContext()).groupItemDao().getAll();
    }

    private boolean navigateSetting() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            groupItemList = getGroupItemList();
            groupItemAdapter.setData(groupItemList);

        }
    }
}


