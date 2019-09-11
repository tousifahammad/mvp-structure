package com.app.baseproject.baseclasses;

import android.app.ProgressDialog;
import android.content.Context;

import com.app.baseproject.loaders.JSONFunctions;
import com.app.baseproject.utils.SettingSharedPreferences;

public abstract class BasePresenter implements JSONFunctions.OnJSONResponseListener{
    private JSONFunctions jfns;
    private ProgressDialog pDialog;
    private SettingSharedPreferences ssp;

    public BasePresenter(Context context){
        jfns=new JSONFunctions(BasePresenter.this);
        pDialog=new ProgressDialog(context);
        ssp=new SettingSharedPreferences(context);
    }

    public JSONFunctions getJfns() {
        return jfns;
    }

    public ProgressDialog getpDialog() {
        return pDialog;
    }

    public SettingSharedPreferences getSsp() {
        return ssp;
    }
}
