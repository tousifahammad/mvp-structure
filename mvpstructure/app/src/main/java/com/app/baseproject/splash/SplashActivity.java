package com.app.baseproject.splash;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.baseproject.R;
import com.app.baseproject.home.HomeActivity;
import com.app.baseproject.login.LoginActivity;
import com.app.baseproject.splash.versionutils.Const;
import com.app.baseproject.splash.versionutils.VersionListener;
import com.app.baseproject.utils.IntentController;
import com.app.baseproject.utils.SP;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class SplashActivity extends AppCompatActivity implements VersionListener {
    String currentVersion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        checkUpdatedVersion();
        // gotoNextScreen(4000);
    }

    private void checkUpdatedVersion() {
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView tv_version = findViewById(R.id.tv_version);
            tv_version.setText("Version " + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //new GetVersionCode(this).execute();

        gotoNextScreen(2000);
    }

    @Override
    public void onGetResponse(String onlineVersion) {
        Log.d("1111", "Current version " + currentVersion + "......playstore version " + onlineVersion);
        if (currentVersion.compareTo(onlineVersion) >= 0) {
            gotoNextScreen(1000);
        } else {
            showUpdateDialog();
        }

    }

    private void showUpdateDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New Version Available")
                .setMessage("Please update to new version to continue use")
                .setCancelable(false)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + getPackageName())));
                        dialogInterface.dismiss();
                        finish();
                    }
                }).setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoNextScreen(1000);
                        // finish();
                    }
                }).create();
        dialog.show();
    }


    public static class GetVersionCode extends AsyncTask<Void, String, String> {

        private VersionListener listener;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        GetVersionCode(VersionListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e("inside", "doInBackground");
            String newVersion = null;

            try {
                Document document = Jsoup.connect(Const.LINK)
                        .timeout(30000)
                        .userAgent(Const.USER_AGENT)
                        .referrer(Const.REFERRER)
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText(Const.CURRENT_VERSION);
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

//Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)
                listener.onGetResponse(onlineVersion);

            }

        }

    }

    @SuppressLint("HandlerLeak")
    private void gotoNextScreen(long delay) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                IntentController.sendIntent(SplashActivity.this, HomeActivity.class);
//                if (SP.getStringPreference(SplashActivity.this, SP.user_id) != null) {
//                    IntentController.sendIntent(SplashActivity.this, HomeActivity.class);
//                    finish();
//                } else {
//                    IntentController.sendIntent(SplashActivity.this, LoginActivity.class);
//                }
                finish();
            }
        }.sendEmptyMessageDelayed(0, delay);
    }
}
