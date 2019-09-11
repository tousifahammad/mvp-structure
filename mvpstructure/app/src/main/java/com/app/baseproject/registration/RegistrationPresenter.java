package com.app.baseproject.registration;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.baseproject.R;
import com.app.baseproject.baseclasses.BasePresenter;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.baseclasses.WebServices;
import com.app.baseproject.loaders.JSONFunctions;
import com.app.baseproject.login.LoginActivity;
import com.app.baseproject.utils.Alert;
import com.app.baseproject.utils.IntentController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegistrationPresenter extends BasePresenter {
    RegistrationActivity activity;

    public RegistrationPresenter(RegistrationActivity activity) {
        super(activity);
        this.activity = activity;
    }


    @Override
    public void getJSONResponseResult(String result, int url_no) {
        if (getpDialog().isShowing()) {
            getpDialog().dismiss();
        }
        switch (url_no) {
            case WebServices.url_no_registration:
                if (SharedMethods.isSuccess(result, activity)) {
                    responseRegistration(result);
                }
                break;

            case WebServices.url_no_registrationOTP:
                if (SharedMethods.isSuccess(result, activity)) {
                    responseRegistrationOTP(result);
                }
                break;
        }
    }


    void requestRegistration() {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setMessage("Registration on process");
            getpDialog().show();

            String url = WebServices.commonUrl + WebServices.registration;

            //String mobile_number = activity.selected_country_code + activity.et_mobile.getText().toString().trim();
            String mobile_number = activity.et_mobile.getText().toString().trim();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", activity.et_user_name.getText().toString().trim());
            hashMap.put("email", activity.et_email.getText().toString().trim());
            hashMap.put("mobile_number", mobile_number);
            hashMap.put("password", activity.et_password.getText().toString().trim());

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_registration);
        } else {
            Alert.showError(activity, activity.getString(R.string.no_internet));
        }
    }


    private void responseRegistration(String response) {
        System.out.println("Inside getLoginResponse method:" + response);
        try {
            JSONObject jobj = new JSONObject(response);
            Toast.makeText(activity, jobj.getString(WebServices.message), Toast.LENGTH_SHORT).show();
            IntentController.gotToActivityNoBack(activity, LoginActivity.class);

        } catch (JSONException ex) {
            Alert.showError(activity, ex.getMessage());
        }
    }


    void requestRegistrationOTP() {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setMessage("Sending OTP to the requested email id");
            getpDialog().show();

            String url = WebServices.commonUrl + WebServices.registrationOTP;

            HashMap<String, String> hashMap = new HashMap<>();
            //String mobile_number = activity.selected_country_code + activity.et_mobile.getText().toString().trim();
            String mobile_number = activity.et_mobile.getText().toString().trim();
            hashMap.put("mobile_number", mobile_number);
            hashMap.put("email", activity.et_email.getText().toString().trim());

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_registrationOTP);
        } else {
            Alert.showError(activity, activity.getString(R.string.no_internet));
        }
    }

    private void responseRegistrationOTP(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String otp = jsonObject.getString("otp");

            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            if (!otp.isEmpty()) {
                // open OTP dialog
                OTPDialog cdd = new OTPDialog(jsonObject.getString("otp"));
                cdd.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class OTPDialog extends Dialog implements View.OnClickListener {
        final String TAG = "AccountInfoDialog";
        private Button btn_verify;
        private EditText et_otp;
        private String otp;

        public OTPDialog(String otp) {
            super(activity);
            this.otp = otp;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_otp);
            et_otp = findViewById(R.id.et_otp);
            btn_verify = findViewById(R.id.btn_verify);
            btn_verify.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_verify:
                    String str_otp = et_otp.getText().toString();
                    if (str_otp.isEmpty()) {
                        Alert.showError(activity, "Please Enter OTP");

                    } else if (str_otp.equals(otp)) {
                        requestRegistration();
                        dismiss();
                    } else {
                        Alert.showError(activity, "Please Enter valid OTP");
                    }
                    break;
            }
        }
    }
}
