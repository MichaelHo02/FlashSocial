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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.database.PersonDB;
import com.michael.flashsocial.utils.DataConverter;
import com.michael.flashsocial.model.Person;
import com.michael.flashsocial.utils.NavigationUtil;
import com.michael.flashsocial.utils.RequestSignal;
import com.michael.flashsocial.utils.TextValidator;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

public class ItemCreationActivity extends AppCompatActivity implements CycleRule {
    MaterialToolbar materialToolbar;
    private MaterialButton avtBtnUpload;
    private MaterialButton avtBtnTake;
    private MaterialButton submitBtn;
    private ShapeableImageView shapeableImageView;
    Bitmap bitmap;

    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText dobInput;
    private MaterialAutoCompleteTextView roleInput;
    private TextInputEditText uniqueFeatureInput;

    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout roleLayout;
    private TextInputLayout uniqueFeatureLayout;

    private TextInputLayout dobLayoutInput;
    private MaterialDatePicker materialDatePicker;

    private boolean isUpdate = false;
    private Person person;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_creation);
        initUI();
        initUIAction();
        passProps();
    }

    private void passProps() {
        try {
            Intent prevIntent = getIntent();
            Bundle bundle = prevIntent.getExtras();
            person = (Person) bundle.get("data");
            bitmap = DataConverter.convertByteArrToBitmap(person.getAvatar());
            shapeableImageView.setImageBitmap(bitmap);
            firstNameInput.setText(person.getFirstName());
            lastNameInput.setText(person.getLastName());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            dobInput.setText(sdf.format(person.getDob()));
            roleInput.setText(person.getRole());
            uniqueFeatureInput.setText(person.getUniqueFeature());

            isUpdate = (boolean) bundle.get("isUpdate");
        } catch (Exception e) {
//            Snackbar.make(findViewById(R.id.act_item_creation_submit_btn), "Cannot render chosen person", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void initUI() {
        materialToolbar = findViewById(R.id.act_item_creation_toolbar);
        avtBtnUpload = findViewById(R.id.act_item_creation_avt_btn_upload);
        avtBtnTake = findViewById(R.id.act_item_creation_avt_btn_take);
        submitBtn = findViewById(R.id.act_item_creation_submit_btn);
        shapeableImageView = findViewById(R.id.act_item_creation_avt);
        firstNameInput = findViewById(R.id.act_item_creation_first_name);
        lastNameInput = findViewById(R.id.act_item_creation_last_name);
        dobInput = findViewById(R.id.act_item_creation_dob);
        roleInput = findViewById(R.id.act_item_creation_role);
        uniqueFeatureInput = findViewById(R.id.act_item_creation_unique_feature);
        dobLayoutInput = findViewById(R.id.act_item_creation_dob_layout);

        firstNameLayout = findViewById(R.id.act_item_creation_first_name_layout);
        lastNameLayout = findViewById(R.id.act_item_creation_last_name_layout);
        roleLayout = findViewById(R.id.act_item_creation_role_layout);
        uniqueFeatureLayout = findViewById(R.id.act_item_creation_unique_feature_layout);
        initCalendar();
    }

    private void initCalendar() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select the birthday");
        long today;
        if (dobInput.getText() == null || dobInput.getText().toString().isEmpty()) {
            today = MaterialDatePicker.todayInUtcMilliseconds();
        } else {
            String s = dobInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat(s.length() == 12 ? "MMM dd, yyyy" : "MMM d, yyyy");
            try {
                today = Objects.requireNonNull(sdf.parse(s)).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                today = MaterialDatePicker.todayInUtcMilliseconds();
            }
        }
        builder.setSelection(today);
        CalendarConstraints.Builder constraint = new CalendarConstraints.Builder();
        constraint.setValidator(DateValidatorPointBackward.now());
        builder.setCalendarConstraints(constraint.build());
        materialDatePicker = builder.build();
    }

    @Override
    public void initUIAction() {
        materialToolbar.setNavigationOnClickListener(this::handleNavigation);
        avtBtnUpload.setOnClickListener(this::handleAvtBtnUpload);
        avtBtnTake.setOnClickListener(this::handleAvtBtnTake);
        submitBtn.setOnClickListener(this::handleSubmitBtn);

        firstNameInput.addTextChangedListener(new TextValidator(findViewById(R.id.act_item_creation_first_name_layout)) {
            @Override
            public void validate(TextInputLayout textInputLayout, String text) {
                if (text == null) {
                    textInputLayout.setError("No Input");
                } else if (text.contains(" ")) {
                    textInputLayout.setError("Cannot contain space");
                } else if (text.trim().isEmpty()) {
                    textInputLayout.setError("Cannot be empty");
                } else {
                    textInputLayout.setError(null);
                }
            }
        });
        lastNameInput.addTextChangedListener(new TextValidator(findViewById(R.id.act_item_creation_last_name_layout)) {
            @Override
            public void validate(TextInputLayout textInputLayout, String text) {
                if (text == null) {
                    textInputLayout.setError("No Input");
                } else if (text.startsWith(" ")) {
                    textInputLayout.setError("Cannot start with space");
                } else if (text.endsWith(" ")) {
                    textInputLayout.setError("Cannot end with space");
                } else if (text.trim().isEmpty()) {
                    textInputLayout.setError("Cannot be empty");
                } else {
                    textInputLayout.setError(null);
                }
            }
        });
        roleInput.addTextChangedListener(new TextValidator(findViewById(R.id.act_item_creation_role_layout)) {
            @Override
            public void validate(TextInputLayout textInputLayout, String text) {
                if (text == null) {
                    textInputLayout.setError("No Input");
                } else if (text.startsWith(" ")) {
                    textInputLayout.setError("Cannot start with space");
                } else if (text.endsWith(" ")) {
                    textInputLayout.setError("Cannot end with space");
                } else if (text.trim().isEmpty()) {
                    textInputLayout.setError("Cannot be empty");
                } else {
                    textInputLayout.setError(null);
                }
            }
        });
        uniqueFeatureInput.addTextChangedListener(new TextValidator(findViewById(R.id.act_item_creation_unique_feature_layout)) {
            @Override
            public void validate(TextInputLayout textInputLayout, String text) {
                if (text == null) {
                    textInputLayout.setError("No Input");
                } else if (text.startsWith(" ")) {
                    textInputLayout.setError("Cannot start with space");
                } else if (text.endsWith(" ")) {
                    textInputLayout.setError("Cannot end with space");
                } else if (text.trim().isEmpty()) {
                    textInputLayout.setError("Cannot be empty");
                } else {
                    textInputLayout.setError(null);
                }
            }
        });

        dobLayoutInput.setEndIconOnClickListener(view -> materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER"));
        dobInput.setOnClickListener(view -> materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER"));
        materialDatePicker.addOnPositiveButtonClickListener(selection -> dobInput.setText(materialDatePicker.getHeaderText()));

        addAutoCompleteData();
    }

    private void addAutoCompleteData() {
        List<String> roles = PersonDB.getInstance(this).itemDao().getAllUniqueRole();
        Set<String> setRoles = new HashSet<>(roles);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, setRoles.toArray(new String[0]));
        roleInput.setAdapter(arrayAdapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void handleNavigation(View view) {
        navigateBack(getIntent());
    }

    private void handleSubmitBtn(View view) {
        try {
            if (firstNameLayout.getError() == null && lastNameLayout.getError() == null
                    && roleLayout.getError() == null && uniqueFeatureLayout.getError() == null &&
                    bitmap != null) {
                String fName = Objects.requireNonNull(firstNameInput.getText()).toString();
                String lname = Objects.requireNonNull(lastNameInput.getText()).toString();
                String dobStr = dobInput.getText().toString();
                SimpleDateFormat sdf;
                if (dobStr.length() == 12) {
                    sdf = new SimpleDateFormat("MMM dd, yyyy");
                } else {
                    sdf = new SimpleDateFormat("MMM d, yyyy");
                }
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                long dob = Objects.requireNonNull(sdf.parse(Objects.requireNonNull(dobInput.getText()).toString())).getTime();
                String role = roleInput.getText().toString();
                String uniqueFeature = Objects.requireNonNull(uniqueFeatureInput.getText()).toString();
                byte[] avt = DataConverter.compressImage(DataConverter.convertImageToByteArr(bitmap));
                if (fName.isEmpty() || lname.isEmpty() || role.isEmpty() || uniqueFeature.isEmpty() || dobInput.getText().toString().isEmpty()) {
                    Snackbar.make(submitBtn, "Missing fields please fulfilled", Snackbar.LENGTH_SHORT).setAnchorView(submitBtn).show();
                } else {
//                    NavigationUtil.hideSoftKeyboard(this);
                    if (isUpdate) {
                        person.setFirstName(fName.trim());
                        person.setLastName(lname.trim());
                        person.setDob(dob);
                        person.setAvatar(avt);
                        person.setRole(role.trim());
                        person.setUniqueFeature(uniqueFeature.trim());

                        Bundle bundle = new Bundle();
                        PersonDB.getInstance(this).itemDao().updateItem(person);
                        bundle.putSerializable("data", person);
                        Intent intent = getIntent().putExtras(bundle);
                        navigateBack(intent);
                    } else {
                        person = new Person(fName, lname, dob, avt, role, uniqueFeature, false, 0, 0);
                        PersonDB.getInstance(this).itemDao().insertItem(person);
                        navigateBack(getIntent());
                    }
                }
            } else {
                Snackbar.make(submitBtn, "Invalid fields or missing avatar", Snackbar.LENGTH_SHORT).setAnchorView(submitBtn).show();
            }
        } catch (NullPointerException e) {
            Snackbar.make(submitBtn, "Missing fields or avatar", Snackbar.LENGTH_SHORT).setAnchorView(submitBtn).show();
            e.printStackTrace();
        } catch (ParseException e) {
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