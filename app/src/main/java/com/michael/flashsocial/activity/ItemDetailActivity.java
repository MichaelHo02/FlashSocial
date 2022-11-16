package com.michael.flashsocial.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.model.Person;
import com.michael.flashsocial.utils.DataConverter;

import java.text.SimpleDateFormat;

public class ItemDetailActivity extends AppCompatActivity implements CycleRule {
    private MaterialToolbar materialToolbar;
    private ShapeableImageView avtView;
    private MaterialTextView fullNameView;
    private MaterialTextView accuracyView;
    private MaterialTextView dobView;
    private MaterialTextView roleView;
    private MaterialTextView uniqueFeatureView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        initUI();
        initUIAction();
        replaceInfo();
    }

    private void replaceInfo() {
        try {
            Intent prevIntent = getIntent();
            Bundle bundle = prevIntent.getExtras();
            Person person = (Person) bundle.get("data");
            avtView.setImageBitmap(DataConverter.convertByteArrToBitmap(person.getAvatar()));
            fullNameView.setText(person.getFirstName() + ", " + person.getLastName());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            dobView.setText(sdf.format(person.getDob().getTime()));
            roleView.setText(person.getRole());
            uniqueFeatureView.setText(person.getUniqueFeature());
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.nestedScrollView), "Cannot render chosen person", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    @Override
    public void initUI() {
        materialToolbar = findViewById(R.id.act_item_detail_toolbar);
        materialToolbar.setTitle("Personal Detail");
        setSupportActionBar(materialToolbar);

        avtView = findViewById(R.id.act_item_detail_avt);
        fullNameView = findViewById(R.id.act_item_detail_full_name);
        accuracyView = findViewById(R.id.act_item_detail_accuracy);
        dobView = findViewById(R.id.act_item_detail_dob);
        roleView = findViewById(R.id.act_item_detail_role);
        uniqueFeatureView = findViewById(R.id.act_item_detail_unique_feature);
    }

    @Override
    public void initUIAction() {
        materialToolbar.setNavigationOnClickListener(this::handleNavigation);
    }

    private void handleNavigation(View view) {
        navigateBack(getIntent());
    }

    private void navigateBack(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.fragment_home_menu, menu);
        return true;
    }
}