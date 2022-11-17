package com.michael.flashsocial.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.model.Person;
import com.michael.flashsocial.utils.DataConverter;
import com.michael.flashsocial.utils.NavigationUtil;
import com.michael.flashsocial.utils.RequestSignal;

import java.text.SimpleDateFormat;

public class ItemDetailActivity extends AppCompatActivity implements CycleRule {
    private MaterialToolbar materialToolbar;
    private ShapeableImageView avtView;
    private MaterialTextView fullNameView;
    private MaterialTextView accuracyView;
    private MaterialTextView dobView;
    private MaterialTextView roleView;
    private MaterialTextView uniqueFeatureView;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        initUI();
        initUIAction();
        try {
            Intent prevIntent = getIntent();
            Bundle bundle = prevIntent.getExtras();
            person = (Person) bundle.get("data");
            replaceInfo(person);
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.nestedScrollView), "Cannot render chosen person", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void replaceInfo(@NonNull Person person) {
        Log.e("Render info", person.getFirstName());
        avtView.setImageBitmap(DataConverter.convertByteArrToBitmap(person.getAvatar()));
        fullNameView.setText(person.getFirstName() + ", " + person.getLastName());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        dobView.setText(sdf.format(person.getDob().getTime()));
        roleView.setText(person.getRole());
        uniqueFeatureView.setText(person.getUniqueFeature());
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
        materialToolbar.setOnMenuItemClickListener(this::handleMenu);
    }

    private boolean handleMenu(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_item_detail_edit:
                return navigateEditItem();
            case R.id.act_item_detail_setting:
                return navigateSetting();
        }
        return false;
    }

    private boolean navigateSetting() {
        return true;
    }

    private boolean navigateEditItem() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", person);
        bundle.putBoolean("isUpdate", true);
        NavigationUtil.navigateActivity(
                this,
                ItemDetailActivity.this,
                ItemCreationActivity.class,
                RequestSignal.ITEM_DETAIL_EDIT_ACTIVITY,
                bundle
        );
        return true;
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
        getMenuInflater().inflate(R.menu.activity_item_detail_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestSignal.ITEM_DETAIL_EDIT_ACTIVITY && resultCode == Activity.RESULT_OK) {
            try {
                Log.e("intent data", String.valueOf(data.getExtras().get("data") != null));
                replaceInfo((Person) data.getExtras().get("data"));
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.nestedScrollView), "Cannot render chosen person", Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}