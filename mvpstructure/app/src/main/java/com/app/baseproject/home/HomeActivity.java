package com.app.baseproject.home;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baseproject.R;
import com.app.baseproject.notification.NotificationActivity;
import com.app.baseproject.login.LoginActivity;
import com.app.baseproject.profile.ProfileActivity;
import com.app.baseproject.utils.Alert;
import com.app.baseproject.utils.IntentController;
import com.app.baseproject.utils.SP;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private DrawerLayout drawer;
    private ImageView hamburger_menu, iv_sos;
    private CardView home_toolcard;
    private NavigationView mNavigationView;
    TextView tv_headphone, tv_shake, tv_location, tv_toggle, tv_check_in, tv_checkin_time, tv_timer_one, tv_timer_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        initNavdrawer();

        // set Toolbar
        initUI();

        // set Toolbar
        setToolBar();

    }


    private void initUI() {
        tv_headphone = findViewById(R.id.tv_headphone);
        tv_shake = findViewById(R.id.tv_shake);
        tv_location = findViewById(R.id.tv_location);
        tv_toggle = findViewById(R.id.tv_toggle);
        tv_check_in = findViewById(R.id.tv_check_in);
        tv_checkin_time = findViewById(R.id.tv_checkin_time);
        tv_timer_one = findViewById(R.id.tv_timer_one);
        tv_timer_two = findViewById(R.id.tv_timer_two);
        iv_sos = findViewById(R.id.iv_sos);
    }

    private void setToolBar() {
        AppCompatTextView tv_tool_title = findViewById(R.id.tv_app_name);
        tv_tool_title.setVisibility(View.VISIBLE);
        tv_tool_title.setText(getString(R.string.title_home));

        findViewById(R.id.ib_back).setVisibility(View.GONE);
    }


    private void initNavdrawer() {
        drawer = findViewById(R.id.drawer_layout);
        hamburger_menu = findViewById(R.id.ib_menu);
        home_toolcard = findViewById(R.id.home_toolcard);
        mNavigationView = findViewById(R.id.nav_view);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        home_toolcard.setLayoutParams(params);

        hamburger_menu.setImageResource(R.mipmap.ic_launcher);
        hamburger_menu.setVisibility(View.VISIBLE);
        hamburger_menu.setOnClickListener(this);

        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);

        View hView = mNavigationView.inflateHeaderView(R.layout.nav_header_home);

        TextView tv_nav_name = hView.findViewById(R.id.tv_nav_name);
        TextView tv_nav_email = hView.findViewById(R.id.tv_nav_email);
        CircleImageView iv_nav_profile_pic = hView.findViewById(R.id.iv_nav_profile_pic);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            IntentController.sendIntent(HomeActivity.this, HomeActivity.class);

        } else if (id == R.id.nav_profile) {
            IntentController.sendIntent(HomeActivity.this, ProfileActivity.class);

        } else if (id == R.id.nav_settings) {
            //IntentController.sendIntent(HomeActivity.this, SettingsActivity.class);

        } else if (id == R.id.nav_contacts) {
            //IntentController.sendIntent(HomeActivity.this, ContactsActivity.class);

        } else if (id == R.id.nav_notification) {
            IntentController.sendIntent(HomeActivity.this, NotificationActivity.class);

        } else if (id == R.id.nav_feedback) {
            //IntentController.sendIntent(HomeActivity.this, ReferActivity.class);

        } else if (id == R.id.nav_help) {
            //IntentController.sendIntent(HomeActivity.this, ReferralEarningActivity.class);

        } else if (id == R.id.nav_logout) {
            logoutPopUp();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_menu:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
        }
    }


    //============= button clicked ==============
    public void onSOSClicked(View view) {
        Toast.makeText(this, "onSOSClicked", Toast.LENGTH_SHORT).show();
    }
    // ========================button click ============================


    private void logoutPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_text))
                .setCancelable(false);
        builder.setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (SP.logoutFunction(HomeActivity.this)) {
                        //stop all services

                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Alert.showError(HomeActivity.this, e.getMessage());
                }

            }
        });
        builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void openCamera() {
        try {
            //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            //startActivity(intent);

            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, 1);
            }
        } catch (Exception e) {
            Alert.showError(this, "Error : " + e.getMessage());
        }
    }
}
