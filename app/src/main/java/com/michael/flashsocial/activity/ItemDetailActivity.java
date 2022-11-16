package com.michael.flashsocial.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.appbar.MaterialToolbar;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.CycleRule;

public class ItemDetailActivity extends AppCompatActivity implements CycleRule {
    private MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        initUI();
        initUIAction();
    }

    @Override
    public void initUI() {
        materialToolbar = findViewById(R.id.act_item_detail_toolbar);
        materialToolbar.setTitle("Personal Detail");
        setSupportActionBar(materialToolbar);
    }

    @Override
    public void initUIAction() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.fragment_home_menu, menu);
        return true;
    }
}