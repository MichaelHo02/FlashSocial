package com.michael.flashsocial.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public abstract class TextValidator implements TextWatcher {
    private final TextInputLayout textInputLayout;

    public TextValidator(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    public abstract void validate(TextInputLayout textInputLayout, String text);

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String text = textInputLayout.getEditText().getText().toString();
        validate(textInputLayout, text);
    }
}
