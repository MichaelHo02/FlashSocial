package com.michael.flashsocial.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.database.ItemDB;
import com.michael.flashsocial.model.DataConverter;
import com.michael.flashsocial.model.Item;
import com.michael.flashsocial.utils.NavigationUtil;
import com.michael.flashsocial.utils.RequestSignal;

import java.io.IOException;

public class ItemCreationActivity extends AppCompatActivity implements CycleRule {
    private MaterialButton avtBtnUpload;
    private MaterialButton avtBtnTake;
    private MaterialButton submitBtn;
    private ShapeableImageView shapeableImageView;
    Bitmap bitmap;

    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText dobInput;
    private AutoCompleteTextView roleInput;
    private TextInputEditText uniqueFeatureInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_creation);
        initUI();
        initUIAction();
    }

    @Override
    public void initUI() {
        avtBtnUpload = findViewById(R.id.act_item_creation_avt_btn_upload);
        avtBtnTake = findViewById(R.id.act_item_creation_avt_btn_take);
        submitBtn = findViewById(R.id.act_item_creation_submit_btn);
        shapeableImageView = findViewById(R.id.act_item_creation_avt);
        firstNameInput = findViewById(R.id.act_item_creation_first_name);
        lastNameInput = findViewById(R.id.act_item_creation_last_name);
        dobInput = findViewById(R.id.act_item_creation_dob);
        roleInput = findViewById(R.id.act_item_creation_role);
        uniqueFeatureInput = findViewById(R.id.act_item_creation_unique_feature);
    }

    @Override
    public void initUIAction() {
        avtBtnUpload.setOnClickListener(this::handleAvtBtnUpload);
        avtBtnTake.setOnClickListener(this::handleAvtBtnTake);
        submitBtn.setOnClickListener(this::handleSubmitBtn);
    }

    private void handleSubmitBtn(View view) {
        try {
            String fName = firstNameInput.getText().toString();
            String lname = lastNameInput.getText().toString();
//            Date dob = new SimpleDateFormat("MM/dd/yy").parse(dobInput.getText().toString());
            String role = roleInput.getText().toString();
            String uniqueFeature = uniqueFeatureInput.getText().toString();
            Item item = new Item(fName, lname, DataConverter.convertImageToByteArr(bitmap), role, uniqueFeature);
            ItemDB.getInstance(this).itemDao().insertItem(item);
            NavigationUtil.hideSoftKeyboard(this);
            navigateBack(getIntent());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void navigateBack(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void handleAvtBtnUpload(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RequestSignal.ITEM_CREATION_UPLOAD_PHOTO);
    }

    private void handleAvtBtnTake(View view) {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, RequestSignal.ITEM_CREATION_PERMISSION_CAM);
        } else {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camIntent, RequestSignal.ITEM_CREATION_TAKE_PHOTO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestSignal.ITEM_CREATION_PERMISSION_CAM) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camIntent, RequestSignal.ITEM_CREATION_TAKE_PHOTO);
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == RequestSignal.ITEM_CREATION_TAKE_PHOTO) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bitmap = photo;
            shapeableImageView.setImageBitmap(photo);
        } else if (requestCode == RequestSignal.ITEM_CREATION_UPLOAD_PHOTO) {
            Uri selectedImg = data.getData();
            shapeableImageView.setImageURI(selectedImg);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}