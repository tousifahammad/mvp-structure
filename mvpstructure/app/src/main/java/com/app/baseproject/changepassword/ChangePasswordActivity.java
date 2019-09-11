package com.app.baseproject.changepassword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.app.baseproject.R;
import com.app.baseproject.utils.Alert;

public class ChangePasswordActivity extends AppCompatActivity {
    //EditText et_mobile, et_password, et_conf_password;
    AppCompatEditText et_old_password, et_password, et_conf_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // set Toolbar
        setToolBar();

        initUI();
    }

    private void setToolBar() {
//        AppCompatTextView tv_tooltitle = findViewById(R.id.tv_app_name);
//        tv_tooltitle.setVisibility(View.VISIBLE);
//        tv_tooltitle.setText(getString(R.string.title_change_password));

        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void initUI() {
        et_old_password = findViewById(R.id.et_old_password);
        et_password = findViewById(R.id.et_new_password);
        et_conf_password = findViewById(R.id.et_confirm_password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onChangePasswordClicked(View view) {
        if (validateEveryField()) {
            new ChangePasswordPresenter(ChangePasswordActivity.this).requestCP();
        }
    }

    private boolean validateEveryField() {
        if (et_old_password.getText().toString().isEmpty()) {
            Alert.showError(this, "Old password : " + getString(R.string.field_empty));
            return false;

        } else if (et_password.getText().toString().isEmpty()) {
            Alert.showError(this, "Password : " + getString(R.string.field_empty));
            return false;

        } else if (et_password.getText().toString().length() < 6) {
            Alert.showError(this, "Password should contains at least 6 character");
            return false;

        } else if (et_conf_password.getText().toString().isEmpty()) {
            Alert.showError(this, "Confirm Password : " + getString(R.string.field_empty));
            return false;

        } else if (!et_password.getText().toString().trim().equals(et_conf_password.getText().toString().trim())) {
            Alert.showError(this, "Password and confirm password should match");
            return false;
        }
        return true;
    }

}
