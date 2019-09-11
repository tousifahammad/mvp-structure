package com.app.baseproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingSharedPreferences {
    private final String USERID = "user_id";
    private final String PREVIOUS_USER_ID = "previous_user_id";
    private final String PROFILE_PIC = "profile_pic";
    private final String EMAIL = "email";
    private final String NAME = "name";
    private final String LOGIN_RESPONSE = "login_response";
    private final String IS_FIRST_TIME_PROFILE = "is_first_time_profile";

    private final String HEADPHONE_CONFIG = "headphone_config";
    private final String HEADPHONE_CONFIG_SWITCH = "headphone_config_switch";
    private final String SHAKE_CONFIG = "shake_config";
    private final String SHAKE_CONFIG_SWITCH = "shake_config_switch";
    private final String TOGGLE_SWITCH_ON = "toggle_switch_on";
    private final String TOGGLE_CONFIGURED = "toggle_configured";
    private final String SOS_SWITCH_ON = "sos_switch_on";
    private final String LOCATION_SWITCH_ON = "location_switch_on";
    private final String CHECK_IN_SWITCH_ON = "check_in_switch_on";
    private final String BIOMETRIC_SWITCH_ON = "biometric_switch_on";
    private final String IS_FINGERPRINT_CHECKIN_ENABLED = "is_fingerprint_checkin_enabled";

    private final String PIN = "pin";
    private final String CHECK_IN_TIME = "check_in_time";
    private final String EMERGENCY_CONTACT_LIST = "emergency_contact_list";

    private final String HEADPHONE_ID = "headphone_id";
    private final String SHAKE_ID = "shake_id";
    private final String TOGGLE_ID = "toggle_id";
    private final String CHECK_IN_ID = "check_in_id";
    private final String SOS_ID = "sos_id";
    private final String LOCATION_ID = "location_id";

    Context context;
    SharedPreferences customerloginPreferences, duressConfigPref;
    public SharedPreferences.Editor customerEditorLogin, duressConfigEditor;

    public SettingSharedPreferences(Context context) {
        this.context = context;

        customerloginPreferences = context.getSharedPreferences("Customer Login", Context.MODE_PRIVATE);
        customerEditorLogin = customerloginPreferences.edit();

        duressConfigPref = context.getSharedPreferences("duress_config", Context.MODE_PRIVATE);
        duressConfigEditor = duressConfigPref.edit();
    }


    public void setUSERID(String userId) {
        customerEditorLogin.putString(USERID, userId).commit();
    }

    public void setUSERNAME(String userName) {
        customerEditorLogin.putString(NAME, userName).commit();
    }

    public void setPROFILE_PIC(String value) {
        customerEditorLogin.putString(PROFILE_PIC, value).commit();
    }

    public void setEMAIL(String agentCode) {
        customerEditorLogin.putString(EMAIL, agentCode).commit();
    }

    public void setHEADPHONE_CONFIG(boolean value) {
        duressConfigEditor.putBoolean(HEADPHONE_CONFIG, value).commit();
    }

    public void setHEADPHONE_CONFIG_SWITCH(boolean value) {
        duressConfigEditor.putBoolean(HEADPHONE_CONFIG_SWITCH, value).commit();
    }

    public void setSHAKE_CONFIG(boolean value) {
        duressConfigEditor.putBoolean(SHAKE_CONFIG, value).commit();
    }

    public void setSHAKE_CONFIG_SWITCH(boolean value) {
        duressConfigEditor.putBoolean(SHAKE_CONFIG_SWITCH, value).commit();
    }


    public String getUSERID() {
        return customerloginPreferences.getString(USERID, null);
    }


    public String getNAME() {
        return customerloginPreferences.getString(NAME, null);
    }

    public String getPROFILE_PIC() {
        return customerloginPreferences.getString(PROFILE_PIC, null);
    }

    public String getEMAIL() {
        return customerloginPreferences.getString(EMAIL, null);
    }


    public void setEMERGENCY_CONTACT_LIST(String response) {
        customerEditorLogin.putString(EMERGENCY_CONTACT_LIST, response).commit();
    }

    public String getEMERGENCY_CONTACT_LIST() {
        return customerloginPreferences.getString(EMERGENCY_CONTACT_LIST, null);
    }

    public void setLOGIN_RESPONSE(String response) {
        customerEditorLogin.putString(LOGIN_RESPONSE, response).commit();
    }

    public String getLOGIN_RESPONSE() {
        return customerloginPreferences.getString(LOGIN_RESPONSE, null);
    }

    public void setIS_FIRST_TIME_PROFILE(boolean value) {
        customerEditorLogin.putBoolean(IS_FIRST_TIME_PROFILE, value).commit();
    }

    public boolean getIS_FIRST_TIME_PROFILE() {
        return customerloginPreferences.getBoolean(IS_FIRST_TIME_PROFILE, true);
    }


    public boolean getHEADPHONE_CONFIG() {
        return duressConfigPref.getBoolean(HEADPHONE_CONFIG, false);
    }

    public boolean getHEADPHONE_CONFIG_SWITCH() {
        return duressConfigPref.getBoolean(HEADPHONE_CONFIG_SWITCH, false);
    }

    public boolean getSHAKE_CONFIG() {
        return duressConfigPref.getBoolean(SHAKE_CONFIG, false);
    }

    public boolean getSHAKE_CONFIG_SWITCH() {
        return duressConfigPref.getBoolean(SHAKE_CONFIG_SWITCH, false);
    }


    public void setTOGGLE_SWITCH_ON(boolean value) {
        duressConfigEditor.putBoolean(TOGGLE_SWITCH_ON, value).commit();
    }

    public boolean getTOGGLE_SWITCH_ON() {
        return duressConfigPref.getBoolean(TOGGLE_SWITCH_ON, false);
    }

    public void setTOGGLE_CONFIGURED(boolean value) {
        duressConfigEditor.putBoolean(TOGGLE_CONFIGURED, value).commit();
    }

    public boolean getTOGGLE_CONFIGURED() {
        return duressConfigPref.getBoolean(TOGGLE_CONFIGURED, false);
    }

    public void setSOS_SWITCH_ON(boolean value) {
        duressConfigEditor.putBoolean(SOS_SWITCH_ON, value).commit();
    }

    public boolean getSOS_SWITCH_ON() {
        return duressConfigPref.getBoolean(SOS_SWITCH_ON, false);
    }

    public void setLOCATION_SWITCH_ON(boolean value) {
        duressConfigEditor.putBoolean(LOCATION_SWITCH_ON, value).commit();
    }

    public boolean getLOCATION_SWITCH_ON() {
        return duressConfigPref.getBoolean(LOCATION_SWITCH_ON, false);
    }

    public void setCHECK_IN_SWITCH_ON(boolean value) {
        duressConfigEditor.putBoolean(CHECK_IN_SWITCH_ON, value).commit();
    }

    public boolean getCHECK_IN_SWITCH_ON() {
        return duressConfigPref.getBoolean(CHECK_IN_SWITCH_ON, false);
    }

    public void setPIN(String value) {
        duressConfigEditor.putString(PIN, value).commit();
    }

    public String getPIN() {
        return duressConfigPref.getString(PIN, null);
    }

    public void setCHECK_IN_TIME(long value) {
        duressConfigEditor.putLong(CHECK_IN_TIME, value).commit();
    }

    public long getCHECK_IN_TIME() {
        return duressConfigPref.getLong(CHECK_IN_TIME, 0);
    }

    public void setBIOMETRIC_SWITCH_ON(boolean value) {
        duressConfigEditor.putBoolean(BIOMETRIC_SWITCH_ON, value).commit();
    }

    public boolean getBIOMETRIC_SWITCH_ON() {
        return duressConfigPref.getBoolean(BIOMETRIC_SWITCH_ON, false);
    }

    public void setIS_FINGERPRINT_CHECKIN_ENABLED(boolean value) {
        duressConfigEditor.putBoolean(IS_FINGERPRINT_CHECKIN_ENABLED, value).commit();
    }

    public boolean getIS_FINGERPRINT_CHECKIN_ENABLED() {
        return duressConfigPref.getBoolean(IS_FINGERPRINT_CHECKIN_ENABLED, false);
    }

    //=====================
    public void setHEADPHONE_ID(String value) {
        duressConfigEditor.putString(HEADPHONE_ID, value).commit();
    }

    public String getHEADPHONE_ID() {
        return duressConfigPref.getString(HEADPHONE_ID, null);
    }

    public void setSHAKE_ID(String value) {
        duressConfigEditor.putString(SHAKE_ID, value).commit();
    }

    public String getSHAKE_ID() {
        return duressConfigPref.getString(SHAKE_ID, null);
    }

    public void setTOGGLE_ID(String value) {
        duressConfigEditor.putString(TOGGLE_ID, value).commit();
    }

    public String getTOGGLE_ID() {
        return duressConfigPref.getString(TOGGLE_ID, null);
    }

    public void setCHECK_IN_ID(String value) {
        duressConfigEditor.putString(CHECK_IN_ID, value).commit();
    }

    public String getCHECK_IN_ID() {
        return duressConfigPref.getString(CHECK_IN_ID, null);
    }

    public void setSOS_ID(String value) {
        duressConfigEditor.putString(SOS_ID, value).commit();
    }

    public String getSOS_ID() {
        return duressConfigPref.getString(SOS_ID, null);
    }

    public void setLOCATION_ID(String value) {
        duressConfigEditor.putString(LOCATION_ID, value).commit();
    }

    public String getLOCATION_ID() {
        return duressConfigPref.getString(LOCATION_ID, null);
    }


    public void setPREVIOUS_USER_ID(String userId) {
        duressConfigEditor.putString(PREVIOUS_USER_ID, userId).commit();
    }

    public String getPREVIOUS_USER_ID() {
        return duressConfigPref.getString(PREVIOUS_USER_ID, null);
    }

    public boolean logoutFunction() {
        customerEditorLogin.clear().commit();

        if (getUSERID() == null) {
            return true;
        } else {
            return false;
        }
    }

}
