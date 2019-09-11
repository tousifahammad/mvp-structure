package com.app.baseproject.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.app.baseproject.R;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.forgotpassword.ForgotActivity;
import com.app.baseproject.home.HomeActivity;
import com.app.baseproject.registration.RegistrationActivity;
import com.app.baseproject.utils.IntentController;

public class LoginActivity extends AppCompatActivity {
    AppCompatEditText et_username, et_password;
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_email_login);
        et_password = findViewById(R.id.et_password_login);
        presenter = new LoginPresenter(LoginActivity.this);
    }


    public void loginClicked(View view) {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (validateEveryField(username, password)) {
            //presenter.login(username, password);
            IntentController.sendIntent(LoginActivity.this, HomeActivity.class);
        }
    }

    public void onRegistrationClicked(View view) {
        IntentController.sendIntent(LoginActivity.this, RegistrationActivity.class);
    }

    public void onForgetClicked(View view) {
        IntentController.sendIntent(LoginActivity.this, ForgotActivity.class);
    }

    public void onDuressClicked(View view) {
        //IntentController.sendIntent(LoginActivity.this, DuressConfSettActivity.class);
    }

    private boolean validateEveryField(String username, String password) {
        if (username.isEmpty()) {
            et_username.setError("Username : " + getString(R.string.field_empty));
            return false;

        } else if (!SharedMethods.validateEmail(username)) {
            et_username.setError(getString(R.string.invalid));
            return false;

        } else if (password.isEmpty()) {
            et_password.setError("Password : " + getString(R.string.field_empty));
            return false;
        } else {
            return true;
        }
    }


}
