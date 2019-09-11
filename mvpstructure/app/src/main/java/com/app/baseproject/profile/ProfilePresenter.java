package com.app.baseproject.profile;

import android.util.Log;
import android.widget.Toast;

import com.app.baseproject.R;
import com.app.baseproject.baseclasses.BasePresenter;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.baseclasses.WebServices;
import com.app.baseproject.home.HomeActivity;
import com.app.baseproject.loaders.JSONFunctions;
import com.app.baseproject.utils.Alert;
import com.app.baseproject.utils.IntentController;
import com.app.baseproject.utils.SettingSharedPreferences;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfilePresenter extends BasePresenter {
    ProfileActivity activity;

    public ProfilePresenter(ProfileActivity activity) {
        super(activity);
        this.activity = activity;
    }


    @Override
    public void getJSONResponseResult(String result, int url_no) {
        switch (url_no) {
            case WebServices.url_no_profile:
                if (SharedMethods.isSuccess(result, activity)) {
                    responseProfileDetails(result);
                }
                break;

            case WebServices.url_no_country_list:
                if (getpDialog().isShowing()) {
                    getpDialog().dismiss();
                }
                if (SharedMethods.isSuccess(result, activity)) {
                    responseCountryList(result);
                }
                break;

            case WebServices.url_no_state_list:
                if (getpDialog().isShowing()) {
                    getpDialog().dismiss();
                }
                responseStateList(result);
                break;

            case WebServices.url_no_suburb_list:
                if (getpDialog().isShowing()) {
                    getpDialog().dismiss();
                }
                responseSuburbList(result);
                break;

            case WebServices.url_no_updateProfile:
                if (getpDialog().isShowing()) {
                    getpDialog().dismiss();
                }
                if (SharedMethods.isSuccess(result, activity)) {
                    try {
                        Toast.makeText(activity, new JSONObject(result).getString(WebServices.message), Toast.LENGTH_SHORT).show();
                        //for first time profile open in edit mode
                        if (getSsp().getIS_FIRST_TIME_PROFILE()) {
                            getSsp().setIS_FIRST_TIME_PROFILE(false);
                        }
                        IntentController.gotToActivityNoBack(activity, HomeActivity.class);
                    } catch (Exception e) {
                        Alert.showError(activity, e.getMessage());
                    }
                }
                break;
        }
    }


    public void requestProfileDetails() {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setMessage("Getting your profile details from the server");
            getpDialog().show();

            String url = WebServices.commonUrl + WebServices.profile;

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user_id", getSsp().getUSERID());

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_profile);

        } else {
            Alert.showError(activity, activity.getString(R.string.no_internet));
        }
    }

    private void responseProfileDetails(String response) {
        try {
            JSONArray jsonArray = SharedMethods.getDataArray(response, activity);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    activity.et_name.setText(jsonObject2.getString("name"));
                    activity.et_address1.setText(jsonObject2.getString("address1"));
                    activity.et_address2.setText(jsonObject2.getString("address2"));
                    activity.et_mobile.setText(jsonObject2.getString("mobile_number"));
                    activity.et_email.setText(jsonObject2.getString("email"));
                    activity.et_post_code.setText(jsonObject2.getString("post_code"));
                    activity.et_medicare_no.setText(jsonObject2.getString("medical_no"));
                    activity.tv_dob.setText(jsonObject2.getString("dob"));
                    activity.et_fb_id.setText(jsonObject2.getString("facebook_id"));
                    activity.et_twitter.setText(jsonObject2.getString("twitter_id"));

                    activity.tv_dob.setText(jsonObject2.getString("dob"));
                    activity.et_fb_id.setText(jsonObject2.getString("facebook_id"));
                    activity.et_twitter.setText(jsonObject2.getString("twitter_id"));
                    //et_emergency_contacts

                    String profile_pic = jsonObject2.getString("profile_pic");

                    if (!profile_pic.isEmpty()) {
                        Glide.with(activity).load(profile_pic)
                                //.placeholder(R.mipmap.birpro)
                                .override(400, 400) // resizing
                                .into(activity.iv_profile_pic);
                    }

                    activity.country_id = jsonObject2.getString("country_id");
                    activity.state_id = jsonObject2.getString("state_id");
                    activity.suburb_id = jsonObject2.getString("suburb_id");

                    //activity.country_name = jsonObject2.getString("country");
                    //activity.country_name = jsonObject2.getString("country");
                    //activity.state_name = jsonObject2.getString("state");
                    //activity.suburb_name = jsonObject2.getString("suburb");

                    requestCountryList();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void requestCountryList() {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setMessage("Getting countries from the server");

            String url = WebServices.commonUrl + WebServices.country_list;
            getJfns().makeHttpRequest(url, "POST", WebServices.url_no_country_list);

        } else {
            Alert.showError(activity, activity.getString(R.string.no_internet));
        }
    }

    private void responseCountryList(String response) {
        try {
            JSONArray jsonArray = SharedMethods.getDataArray(response, activity);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    activity.countryList.add(jsonObject.getString("name"));
                }
                activity.counties = jsonArray;
                activity.setSpinnerAdapter("countries");
            }
        } catch (Exception ex) {
            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void requestStateList() {
        if (JSONFunctions.isInternetOn(activity)) {

            String url = WebServices.commonUrl + WebServices.state_list;

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("country_id", activity.country_id);

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_state_list);

            getpDialog().setMessage("Getting states from the server");
            if (!getpDialog().isShowing()) {
                getpDialog().show();
            }

        } else {
            Alert.showError(activity, activity.getString(R.string.no_internet));
        }
    }

    private void responseStateList(String response) {
        try {
            JSONArray jsonArray = SharedMethods.getDataArray(response, activity);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    activity.stateList.add(jsonObject.getString("name"));
                }
                activity.states = jsonArray;
            } else {
                activity.stateList.add("Unavailable");
            }

            activity.setSpinnerAdapter("states");

        } catch (Exception ex) {
            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void requestSuburbList() {
        if (JSONFunctions.isInternetOn(activity)) {
            String url = WebServices.commonUrl + WebServices.suburb_list;
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("state_id", activity.state_id);

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_suburb_list);

            getpDialog().setMessage("Getting suburbs from the server");
            if (!getpDialog().isShowing()) {
                getpDialog().show();
            }
        } else {
            Alert.showError(activity, activity.getString(R.string.no_internet));
        }
    }

    private void responseSuburbList(String response) {
        try {
            JSONArray jsonArray = SharedMethods.getDataArray(response, activity);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    activity.suburbList.add(jsonObject.getString("name"));
                }
                activity.suburbs = jsonArray;

            } else {
                activity.suburbList.add("Unavailable");
            }

            activity.setSpinnerAdapter("suburbs");

        } catch (Exception ex) {
            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void requestProfileUpload() {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setMessage("Updating your profile");
            getpDialog().show();

            String url = WebServices.commonUrl + WebServices.updateProfile;
            //user_id,email,name,address1,address2,country_id,state_id,suburb_id,post_code,mobile_number,taxfile_no
            //medical_no,medical_history,dob,facebook_id,twitter_id,profile_pic

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user_id", getSsp().getUSERID());
            hashMap.put("name", activity.et_name.getText().toString().trim());
            hashMap.put("email", activity.et_email.getText().toString().trim());
            hashMap.put("mobile_number", activity.et_mobile.getText().toString().trim());
            hashMap.put("address1", activity.et_address1.getText().toString().trim());
            hashMap.put("address2", activity.et_address2.getText().toString().trim());
            hashMap.put("country_id", activity.country_id);
            hashMap.put("state_id", activity.state_id);
            hashMap.put("suburb_id", activity.suburb_id);
            hashMap.put("post_code", activity.et_post_code.getText().toString().trim());
            hashMap.put("medical_no", activity.et_medicare_no.getText().toString().trim());
            hashMap.put("medical_history", activity.str_medical_history);
            hashMap.put("dob", activity.tv_dob.getText().toString().trim());
            hashMap.put("facebook_id", activity.et_fb_id.getText().toString().trim());
            hashMap.put("twitter_id", activity.et_twitter.getText().toString().trim());
            hashMap.put("profile_pic", activity.str_profile_pic);

            Log.d("1111", "requestProfileUpload: " + hashMap.toString());

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_updateProfile);

        } else {
            Alert.showError(activity, activity.getString(R.string.no_internet));
        }
    }
}
