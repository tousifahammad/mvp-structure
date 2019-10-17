package com.app.baseproject.registration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.app.baseproject.R;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.utils.Alert;
import com.app.baseproject.utils.Validator;
import com.hbb20.CountryCodePicker;


public class RegistrationActivity extends AppCompatActivity {
    EditText et_user_name, et_email, et_mobile, et_password, et_conf_password;
    CountryCodePicker ccp;
    String selected_country_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // set Toolbar
        setToolBar();

        initUI();

        ccp = findViewById(R.id.ccp);
        ccp.setDefaultCountryUsingNameCode("AU");
        ccp.resetToDefaultCountry();
    }

    private void setToolBar() {
        findViewById(R.id.ib_reg_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initUI() {
        et_user_name = findViewById(R.id.et_user_name);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_password = findViewById(R.id.et_password);
        et_conf_password = findViewById(R.id.et_conf_password);
    }

    public void onRegistrationClick(View view) {
        if (validateEveryField()) {
            new RegistrationPresenter(RegistrationActivity.this).requestRegistrationOTP();
        }
    }

    public void onCountryPickerClick(View view) {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                selected_country_code = ccp.getSelectedCountryCodeWithPlus();
            }
        });
    }

    private boolean validateEveryField() {
        if (et_user_name.getText().toString().isEmpty()) {
            Alert.showError(this, "First name : " + getString(R.string.field_empty));
            return false;

        } else if (!Validator.validateName(et_user_name.getText().toString().trim())) {
            Alert.showError(this, "Invalid first name");
            return false;

        } else if (et_email.getText().toString().isEmpty()) {
            Alert.showError(this, "Email : " + getString(R.string.field_empty));
            return false;

        } else if (!Validator.validateEmail(et_email.getText().toString().trim())) {
            Alert.showError(this, "Invalid email");
            return false;

//        } else if (selected_country_code.isEmpty()) {
//            Alert.showError(this, "Please select country code");
//            return false;

        } else if (et_mobile.getText().toString().trim().length() != 9) {
            Alert.showError(this, "Please enter 9 digit mobile number");
            return false;

        } else if (et_password.getText().toString().isEmpty()) {
            Alert.showError(this, "Password : " + getString(R.string.field_empty));
            return false;

        } else if (et_password.getText().toString().length() < 6) {
            Alert.showError(this, "Password should contains at least 6 character");
            return false;

        } else if (!et_password.getText().toString().trim().equals(et_conf_password.getText().toString().trim())) {
            Alert.showError(this, "Passwords and confirm password should match");
            return false;
        }

        return true;
    }

}