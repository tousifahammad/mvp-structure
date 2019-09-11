package com.app.baseproject.forgotpassword;

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

public class ForgotPresenter extends BasePresenter {
    ForgotActivity activity;

    public ForgotPresenter(ForgotActivity activity) {
        super(activity);
        this.activity = activity;
    }

    void requestForgotPassword() {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setTitle("Please wait...");
            getpDialog().setMessage("Sending new password to requested email id");
            getpDialog().show();

            String url = WebServices.commonUrl + WebServices.forgot_Password;

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("email", activity.et_email_id.getText().toString().trim());

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_forgot_Password);

        } else {
            Toast.makeText(activity, activity.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void getJSONResponseResult(String result, int url_no) {
        if (getpDialog().isShowing()) {
            getpDialog().dismiss();
        }
        switch (url_no) {
            case WebServices.url_no_forgot_Password:
                if (SharedMethods.isSuccess(result, activity))
                    responseForgotPassword(result);
                break;
        }
    }

    private void responseForgotPassword(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Toast.makeText(activity, jsonObject.getString(WebServices.message), Toast.LENGTH_LONG).show();
            IntentController.sendIntent(activity, LoginActivity.class);
        } catch (JSONException ex) {
            Alert.showError(activity, ex.getMessage());
        }
    }
}
