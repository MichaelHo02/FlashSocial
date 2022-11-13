package com.michael.flashsocial.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.michael.flashsocial.model.GroupItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        recycleViewInit();
        materialToolbar = view.findViewById(R.id.frag_home_toolbar);
        materialToolbar.inflateMenu(R.menu.fragment_home_menu);
        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.frag_home_add:
                        return navigateAddFolder();
                    case R.id.frag_home_setting:
                        return navigateSetting();
                }
                return false;
            }
        });
        return view;
    }

    private boolean navigateAddFolder() {
        Intent intent = new Intent(view.getContext(), FolderCreationActivity.class);
        startActivityForResult(intent, 100);
        return true;
    }

    private boolean navigateSetting() {
        return true;
    }

    private void recycleViewInit() {
        recyclerView = view.findViewById(R.id.frag_home_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        groupItemList = getGroupItemList();
        groupItemAdapter = new GroupItemAdapter(groupItemList);
        recyclerView.setAdapter(groupItemAdapter);
    }

    private List<GroupItem> getGroupItemList() {
        List<GroupItem> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new GroupItem("Name " + i, i, true));
        }
        return list;
    }
}


