package com.michael.flashsocial.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.database.GroupItemDB;
import com.michael.flashsocial.model.GroupItem;
import com.michael.flashsocial.utils.NavigationUtil;

public class FolderCreationActivity extends AppCompatActivity implements CycleRule {
    MaterialToolbar materialToolbar;
    TextInputEditText nameField;
    MaterialButton submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_creation);

        initUI();
        initUIAction();
    }

    @Override
    public void initUI() {
        materialToolbar = findViewById(R.id.act_folder_creation_toolbar);
        nameField = findViewById(R.id.act_folder_creation_name_field);
        submitBtn = findViewById(R.id.act_folder_creation_submit_btn);
    }

    @Override
    public void initUIAction() {
        materialToolbar.setNavigationOnClickListener(this::handleNavigation);
        submitBtn.setOnClickListener(this::handleSubmission);
    }

    private void handleSubmission(View view) {
        if (nameField.getText() == null) return;
        String folderName = nameField.getText().toString().trim();
        if (folderName.isEmpty()) return;
        GroupItem groupItem = new GroupItem(folderName, 0, false);
        GroupItemDB.getInstance(this).groupItemDao().create(groupItem);
        nameField.setText("");
        NavigationUtil.hideSoftKeyboard(this);
        navigateBack(getIntent());
    }

    private void handleNavigation(View view) {
        navigateBack(getIntent());
    }

    private void navigateBack(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}