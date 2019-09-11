package com.app.baseproject.changepassword;

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

public class ChangePasswordPresenter extends BasePresenter {
    ChangePasswordActivity activity;

    public ChangePasswordPresenter(ChangePasswordActivity activity) {
        super(activity);
        this.activity = activity;
    }

    void requestCP() {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setTitle("Please wait...");
            getpDialog().setMessage("While we are updating your password");
            getpDialog().show();

            String url = WebServices.commonUrl + WebServices.change_password;

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user_id", getSsp().getUSERID());
            hashMap.put("oldpassword", activity.et_old_password.getText().toString().trim());
            hashMap.put("newpassword", activity.et_password.getText().toString().trim());

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_change_password);
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
            case WebServices.url_no_change_password:
                if (SharedMethods.isSuccess(result, activity)) {
                    responseCP(result);
                }
                break;
        }
    }

    private void responseCP(String response) {
        try {
            JSONObject jobj = new JSONObject(response);

            Toast.makeText(activity, jobj.getString(WebServices.message), Toast.LENGTH_SHORT).show();

            // login using new password
            getSsp().logoutFunction();

            IntentController.gotToActivityNoBack(activity, LoginActivity.class);

        } catch (JSONException ex) {
            Alert.showError(activity, ex.getMessage());
        }
    }
}
