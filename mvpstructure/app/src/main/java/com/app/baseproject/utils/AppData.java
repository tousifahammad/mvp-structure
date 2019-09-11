package com.app.baseproject.utils;



public class AppData {
    public static String name = null;

    // DuressConfSettActivity
    public static boolean is_configuring;

    //HeadphoneActivity
    public static boolean headphone_plugged_in;

    //ShakeActivity
    public static boolean is_shaking;

    //ToggleActivity
    public static boolean is_volume_key_up;

    //Checkin Activity
    public static long check_in_time;
    public static final long check_in_verify_time = 30 * 1000; //ib milisec
    public static boolean is_fingerprint_checking;

    public static final long location_sending_time = 1 * 1000; //ib milisec

}
