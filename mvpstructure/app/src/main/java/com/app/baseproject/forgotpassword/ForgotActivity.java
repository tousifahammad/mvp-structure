package com.app.baseproject.forgotpassword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;

import com.app.baseproject.R;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.registration.RegistrationActivity;
import com.app.baseproject.utils.Alert;
import com.app.baseproject.utils.IntentController;

public class ForgotActivity extends AppCompatActivity {
    EditText et_email_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        // set Toolbar
        setToolBar();

        initUI();
    }

    private void setToolBar() {
        AppCompatTextView tv_tooltitle = findViewById(R.id.tv_app_name);
        tv_tooltitle.setVisibility(View.VISIBLE);
        tv_tooltitle.setText(getString(R.string.title_forgot_password));
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void initUI() {

        et_email_id = findViewById(R.id.et_email_id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onRegistrationClicked(View view) {
        IntentController.sendIntent(ForgotActivity.this, RegistrationActivity.class);
    }

    public void onResetClick(View view) {
        if (validateEveryField()) {
            new ForgotPresenter(ForgotActivity.this).requestForgotPassword();
        }
    }

    private boolean validateEveryField() {
        if (et_email_id.getText().toString().isEmpty()) {
            Alert.showError(this, "Please enter email");
            return false;
        } else if (!SharedMethods.validateEmail(et_email_id.getText().toString().trim())) {
            Alert.showError(this, "Please enter valid email id");
            return false;
        }

        return true;
    }
}

