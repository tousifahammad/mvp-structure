package com.app.baseproject.home;

import android.widget.Toast;
import com.app.baseproject.R;
import com.app.baseproject.baseclasses.BasePresenter;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.baseclasses.WebServices;
import com.app.baseproject.loaders.JSONFunctions;
import java.util.HashMap;

public class HomePresenter extends BasePresenter {
    HomeActivity activity;

    public HomePresenter(HomeActivity activity) {
        super(activity);
        this.activity = activity;
    }


    @Override
    public void getJSONResponseResult(String result, int url_no) {
        if (getpDialog().isShowing()) {
            getpDialog().dismiss();
        }
        switch (url_no) {
            case WebServices.request_url_no_1:
                if (SharedMethods.isSuccess(result, activity)) {
                    //getSsp().setEMERGENCY_CONTACT_LIST(result);
                }
                break;
        }
    }

    public void requestContacts() {
        if (JSONFunctions.isInternetOn(activity)) {
            getpDialog().setMessage("Getting your emergency contacts from the server");
            getpDialog().show();

            HashMap<String, String> hashMap = new HashMap<>();
            //hashMap.put("user_id", getSsp().getUSERID());

            getJfns().makeHttpRequest(WebServices.customer_login, "POST", hashMap, false, WebServices.request_url_no_1);

        } else {
            Toast.makeText(activity, activity.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }
}
