package com.michael.flashsocial.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.android.material.color.DynamicColors;
import com.google.android.material.navigation.NavigationBarView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.fragment.HomeFragment;
import com.michael.flashsocial.fragment.LearningFragment;
import com.michael.flashsocial.fragment.StatFragment;
import com.michael.flashsocial.utils.NavigationUtil;

public class MainActivity extends AppCompatActivity implements CycleRule {
    private NavigationBarView nbv;
    private final HomeFragment homeFragment = HomeFragment.newInstance();
    private final LearningFragment learningFragment = LearningFragment.newInstance();
    private final StatFragment statFragment = StatFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initUIAction();
    }

    @Override
    public void initUI() {
        nbv = findViewById(R.id.act_main_bnv);
        navigationInit();
    }

    @Override
    public void initUIAction() {
        nbv.setOnItemSelectedListener(this::handleSelectItem);
    }

    private void navigationInit() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        nbv.inflateMenu(R.menu.activity_main_bottom_navigation);
        NavigationUtil.changeFragment(this, R.id.act_main_fl, homeFragment);
    }

    private boolean handleSelectItem(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_main_i1:
                NavigationUtil.changeFragment(this, R.id.act_main_fl, homeFragment);
                return true;
            case R.id.act_main_i2:
                NavigationUtil.changeFragment(this, R.id.act_main_fl, learningFragment);
                return true;
            case R.id.act_main_i3:
                NavigationUtil.changeFragment(this, R.id.act_main_fl, statFragment);
                return true;
        }
        return false;
    }
}