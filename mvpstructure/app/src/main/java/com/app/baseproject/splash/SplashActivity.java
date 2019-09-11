package com.app.baseproject.splash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.app.baseproject.R;
import com.app.baseproject.home.HomeActivity;
import com.app.baseproject.login.LoginActivity;
import com.app.baseproject.utils.IntentController;
import com.app.baseproject.utils.SettingSharedPreferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        gotoNextScreen(2000);
    }

    @SuppressLint("HandlerLeak")
    private void gotoNextScreen(long delay) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                SettingSharedPreferences ssp = new SettingSharedPreferences(getApplicationContext());
                if (ssp.getEMAIL() != null) {
                    IntentController.sendIntent(SplashActivity.this, HomeActivity.class);
                    finish();
                } else {
                    IntentController.sendIntent(SplashActivity.this, LoginActivity.class);
                }
                finish();
            }
        }.sendEmptyMessageDelayed(0, delay);
    }
}
