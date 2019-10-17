package com.app.baseproject.login;

import com.app.baseproject.R;
import com.app.baseproject.baseclasses.BasePresenter;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.baseclasses.WebServices;
import com.app.baseproject.home.HomeActivity;
import com.app.baseproject.loaders.JSONFunctions;
import com.app.baseproject.utils.Alert;
import com.app.baseproject.utils.IntentController;
import com.app.baseproject.utils.SP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class LoginPresenter extends BasePresenter {
    LoginActivity activity;

    public LoginPresenter(LoginActivity activity) {
        super(activity);
        this.activity = activity;
    }

    void login(String username, String password) {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setMessage("Connecting you to the server");

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("email", username);
            hashMap.put("password", password);

            getJfns().makeHttpRequest(WebServices.customer_login, "POST", hashMap, false, WebServices.request_url_no_1);
            getpDialog().show();
        } else {
            Alert.showError(activity, activity.getString(R.string.no_internet));
        }
    }


    @Override
    public void getJSONResponseResult(String result, int url_no) {
        if (getpDialog().isShowing()) {
            getpDialog().dismiss();
        }
        switch (url_no) {
            case WebServices.request_url_no_1:
                if (SharedMethods.isSuccess(result, activity)) {
                    getLoginResponse(result);
                }
                break;
        }
    }

    private void getLoginResponse(String response) {
        try {
            JSONArray jsonArray = SharedMethods.getDataArray(response, activity);

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject user_jo = jsonArray.getJSONObject(i);
                    SP.setStringPreference(activity, SP.user_id, user_jo.getString("id"));
                    SP.setStringPreference(activity, SP.name, user_jo.getString("name"));
                    SP.setStringPreference(activity, SP.mobile, user_jo.getString("mobile"));

                    IntentController.sendIntent(activity, HomeActivity.class);
                    activity.finish();
                }
            }
        } catch (JSONException ex) {
            Alert.showError(activity, ex.getMessage());
        }
    }
}
