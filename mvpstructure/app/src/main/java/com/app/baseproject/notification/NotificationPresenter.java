package com.app.baseproject.notification;

import com.app.baseproject.R;
import com.app.baseproject.baseclasses.BasePresenter;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.baseclasses.WebServices;
import com.app.baseproject.loaders.JSONFunctions;
import com.app.baseproject.utils.Alert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NotificationPresenter extends BasePresenter {
    private NotificationActivity activity;

    public NotificationPresenter(NotificationActivity activity) {
        super(activity);
        this.activity = activity;
    }

    void requestNotifications() {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setMessage("Getting notifications from the server. Please wait...");

            String url = WebServices.commonUrl + WebServices.notification_list;

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user_id", getSsp().getUSERID());

            getJfns().makeHttpRequest(url, "POST", hashMap, false, WebServices.url_no_notification_list);
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
            case WebServices.url_no_notification_list:
                responseNotifications(result);
                break;
        }
    }

    private void responseNotifications(String response) {
        try {
            JSONArray jsonArray = SharedMethods.getDataArray(response, activity);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String notification_id = jsonObject.getString("notification_id");
                    String message = jsonObject.getString("message");
                    String image = jsonObject.getString("image");
                    String date = jsonObject.getString("date");
                    activity.notification_list.add(new Notification(notification_id, message, image, date));
                }
                activity.setAdapter();
            }
        } catch (JSONException ex) {
            Alert.showError(activity, ex.getMessage());
        }
    }

}
