package com.michael.flashsocial.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.michael.flashsocial.R;
import com.michael.flashsocial.activity.ItemCreationActivity;
import com.michael.flashsocial.activity.ItemDetailActivity;
import com.michael.flashsocial.adapter.PersonAdapter;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.custom_rule.IClickCallback;
import com.michael.flashsocial.database.PersonDB;
import com.michael.flashsocial.model.Person;
import com.michael.flashsocial.utils.NavigationUtil;
import com.michael.flashsocial.utils.RequestSignal;

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

    private List<Person> personList;
    private PersonAdapter personAdapter;

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
        personList = getItemList();
        personAdapter = new PersonAdapter(personList, person -> {
            Intent intent = new Intent(view.getContext(), ItemDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", person);
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestSignal.ITEM_DETAIL_ACTIVITY);
        });
        recyclerView.setAdapter(personAdapter);
    }

    private boolean handleMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.frag_home_add:
                return navigateAddItem();
            case R.id.frag_home_setting:
                return navigateSetting();
        }
        return false;
    }

    private boolean navigateAddItem() {
        NavigationUtil.navigateActivity(
                this,
                view.getContext(),
                ItemCreationActivity.class,
                RequestSignal.ITEM_CREATION_ACTIVITY
        );
        return true;
    }

    private List<Person> getItemList() {
        return PersonDB.getInstance(this.getContext()).itemDao().getAllPeople();
    }

    private boolean navigateSetting() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestSignal.ITEM_CREATION_ACTIVITY || requestCode == RequestSignal.ITEM_DETAIL_ACTIVITY) {
                personList = PersonDB.getInstance(this.getContext()).itemDao().getAllPeople();
                personAdapter.setData(personList);
            }
        }
    }
}


