package com.app.baseproject.loaders.fileupload;

/**
 * Created by Suvo on 10-May-16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class JSONFunctions extends JsonHttpResponseHandler{


    public OnLoopjJSONResponseListener ojrl;

    private AsyncHttpClient client;
    private RequestParams params;
  //  private JSONObject jsonParams;
    private String method;
    private String url;
    private HashMap<String,String> hm=null;
    private HashMap<String,File> hmFile=null;
    int url_no;
    boolean isMultipart=false;

    public JSONFunctions(OnLoopjJSONResponseListener ojrl){
        this.ojrl=ojrl;
    }



    private void setupClientParams(){
        System.out.println("Inside setupClientParams method");
        client = new AsyncHttpClient();
        params = new RequestParams();
        //jsonParams=new JSONObject();
    }

    private void setupParams(){
        System.out.println("Inside setupParams method");
        if(hm!=null) {
            for (Map.Entry<String, String> entry : hm.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                params.put(key, value);
               /* try {
                    jsonParams.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }
        if(hmFile!=null){
            for(Map.Entry<String,File> entry:hmFile.entrySet()){
                String key = entry.getKey();
                File value = entry.getValue();
                try {
                    params.put(key, value);
                } catch (FileNotFoundException e) {
                    System.out.println("FileNotFound exception thrown from setupParams method");
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized void makeRequest(int url_no){
        System.out.println("Inside makeRequest method");
        this.url_no=url_no;
        if(isMultipart)
            params.setForceMultipartEntityContentType(true);
        if (method.equals("POST")) {
            // request method is POST
            client.post(url, params, this);

        } else if (method.equals("GET")) {
            // request method is GET
            client.get(url, params, this );

        }
    }

    private void callRequest(int url_no){
        System.out.println("Inside callRequest method");
        setupClientParams();
        setupParams();
        makeRequest(url_no);
    }

    public void makeHttpRequest(String url, String method, int url_no){
        System.out.println("Inside makeHttpRequest method");
        this.method=method;
        this.url=url;

        callRequest(url_no);
    }

    public void makeHttpRequest(String url, String method, HashMap<String,String> hmString, HashMap<String,File> hmFile, int url_no){
        System.out.println("Inside makeHttpRequest method");
        this.hm=hmString;
        this.hmFile=hmFile;
        this.method=method;
        this.url=url;

        callRequest(url_no);
    }

    public void makeHttpRequest(String url, String method, HashMap<String, String> hm, boolean isMultipart, int url_no) {
        // Making HTTP request

        System.out.println("Inside makeHttpRequest method");
        this.hm=hm;
        this.method=method;
        this.isMultipart=isMultipart;
        this.url=url;

        callRequest(url_no);

    }




    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        System.out.println("Inside onSuccess with JSONObject:"+response.toString()+statusCode);
        ojrl.getLoopjJSONResponseResult(response.toString(),url_no);
    }

   @Override
   public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        System.out.println("Inside onSuccess with JSONArray:"+response.toString()+statusCode);
        ojrl.getLoopjJSONResponseResult(response.toString(),url_no);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        System.out.println("Inside onSuccess with String:"+responseString+"StatusCode: "+statusCode);
        ojrl.getLoopjJSONResponseResult(responseString,url_no);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
        System.out.println("Inside onFailure with String, Statuscode is:"+statusCode+",String is: "+res );
        ojrl.getLoopjJSONResponseResult(null,url_no);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
      //  super.onFailure(statusCode, headers, throwable, errorResponse);
        System.out.println("Inside onFailure with JSONObject, Statuscode is:"+statusCode);
        ojrl.getLoopjJSONResponseResult(null,url_no);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
     //   super.onFailure(statusCode, headers, throwable, errorResponse);
        System.out.println("Inside onFailure with JSONArray, Statuscode is:"+statusCode);
        ojrl.getLoopjJSONResponseResult(null,url_no);
    }

    public interface OnLoopjJSONResponseListener{
        public void getLoopjJSONResponseResult(String result, int url_no);
    }






    public static boolean isInternetOn(Context context){

        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
        }

    public void setImageFromUrl(Context context, String url, Integer errorImage, ImageView imgvw, boolean isCircular) {
        CustomCircularImageViewTarget target=new CustomCircularImageViewTarget(context,imgvw,isCircular);
        if (errorImage != null) {
            Glide.with(context).load(url).asBitmap().centerCrop().error(errorImage).into(target);
        }else{
            Glide.with(context).load(url).asBitmap().into(target);
        }
    }

    public void setImageFromUrl(Fragment fragment, String url, Integer errorImage, ImageView imgvw, boolean isCircular){
        CustomCircularImageViewTarget target=new CustomCircularImageViewTarget(fragment,imgvw,isCircular);
        if (errorImage != null) {
            Glide.with(fragment).load(url).asBitmap().centerCrop().error(errorImage).into(target);
        }else{
            Glide.with(fragment).load(url).asBitmap().into(target);
        }
    }



    private class CustomCircularImageViewTarget extends BitmapImageViewTarget {
        ImageView imgvw;
        boolean isCircular=false;
        Context context=null;
        Fragment fragment=null;
        public CustomCircularImageViewTarget(Context context, ImageView view, boolean isCircular) {
            super(view);
            CustomCircularImageViewTarget.this.imgvw=view;
            CustomCircularImageViewTarget.this.isCircular=isCircular;
            CustomCircularImageViewTarget.this.context=context;
        }

        public CustomCircularImageViewTarget(Fragment fragment, ImageView view, boolean isCircular) {
            super(view);
            CustomCircularImageViewTarget.this.imgvw=view;
            CustomCircularImageViewTarget.this.isCircular=isCircular;
            CustomCircularImageViewTarget.this.fragment=fragment;
        }



        @Override
        protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable=null;
            if(context!=null) {
                circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
            }else if(fragment!=null) {
                circularBitmapDrawable = RoundedBitmapDrawableFactory.create(fragment.getResources(), resource);
            }
            if(isCircular)
                circularBitmapDrawable.setCircular(true);
            if(circularBitmapDrawable!=null)
                imgvw.setImageDrawable(circularBitmapDrawable);
        }

    }

}


