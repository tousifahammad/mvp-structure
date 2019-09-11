package com.app.baseproject.login;

import com.app.baseproject.R;
import com.app.baseproject.baseclasses.BasePresenter;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.baseclasses.WebServices;
import com.app.baseproject.home.HomeActivity;
import com.app.baseproject.loaders.JSONFunctions;
import com.app.baseproject.utils.Alert;
import com.app.baseproject.utils.IntentController;
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

            String url = WebServices.commonUrl + WebServices.login;

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("email", username);
            hashMap.put("password", password);

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_login);
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
            case WebServices.url_no_login:
                if (SharedMethods.isSuccess(result, activity)) {
                    getLoginResponse(result);
                }
                break;
        }
    }

    private void getLoginResponse(String response) {
        try {
            JSONObject jobj = new JSONObject(response);
            JSONObject jsonObject = jobj.getJSONObject(WebServices.data);

            if (jsonObject != null) {
                getSsp().setUSERID(jsonObject.getString("id"));
                getSsp().setUSERNAME(jsonObject.getString("name"));
                getSsp().setEMAIL(jsonObject.getString("email"));
                getSsp().setPROFILE_PIC(jsonObject.getString("profile_pic"));

                getSsp().setLOGIN_RESPONSE(response);

                JSONArray jsonArray = jsonObject.getJSONArray("dureslist");
                //Log.d("1111", "getLoginResponse: " + jsonArray);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        switch (id) {
                            case "1":
                                getSsp().setHEADPHONE_ID(id);
                                break;
                            case "2":
                                getSsp().setSHAKE_ID(id);
                                break;
                            case "3":
                                getSsp().setTOGGLE_ID(id);
                                break;
                            case "4":
                                getSsp().setCHECK_IN_ID(id);
                                break;
                            case "5":
                                getSsp().setSOS_ID(id);
                                break;
                            case "6":
                                getSsp().setLOCATION_ID(id);
                                break;
                        }
                    }
                }

                // delete config only if new user login in same device or keep settings for same user login
                if (!getSsp().getUSERID().equals(getSsp().getPREVIOUS_USER_ID())) {
                    getSsp().duressConfigEditor.clear().commit();
                }
                getSsp().setPREVIOUS_USER_ID(getSsp().getUSERID());

                IntentController.gotToActivityNoBack(activity, HomeActivity.class);
            }
        } catch (JSONException ex) {
            Alert.showError(activity, ex.getMessage());
        }
    }
}
