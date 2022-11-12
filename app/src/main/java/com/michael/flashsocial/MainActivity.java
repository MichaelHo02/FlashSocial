package com.michael.flashsocial;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.michael.flashsocial.fragment.HomeFragment;
import com.michael.flashsocial.fragment.LearningFragment;
import com.michael.flashsocial.fragment.StatFragment;

public class MainActivity extends AppCompatActivity {
    private NavigationBarView nbv;
    private HomeFragment homeFragment = new HomeFragment();
    private LearningFragment learningFragment = new LearningFragment();
    private StatFragment statFragment = new StatFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationInit();
    }

    private void navigationInit() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        nbv = findViewById(R.id.act_main_bnv);
        nbv.inflateMenu(R.menu.activity_main_bottom_navigation);
        nbv.setOnItemSelectedListener(this::handleSelectItem);
        changeFragment(R.id.act_main_fl, homeFragment);
    }

    private boolean handleSelectItem(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_main_i1:
                changeFragment(R.id.act_main_fl, homeFragment);
                return true;
            case R.id.act_main_i2:
                changeFragment(R.id.act_main_fl, learningFragment);
                return true;
            case R.id.act_main_i3:
                changeFragment(R.id.act_main_fl, statFragment);
                return true;
        }
        return false;
    }

    private void changeFragment(@IdRes int containerViewId, @NonNull androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment).commit();
    }
}