package com.acresqaure.acresqaure.HttpUtility;

import android.content.Context;

import com.acresqaure.acresqaure.Constants.GlobalData;
import com.acresqaure.acresqaure.Constants.SharedPreferencesKey;
import com.acresqaure.acresqaure.Utility.SharedPrefUtility;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.paxcel.volley.AppController;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Paras on 13-10-2015.
 */
public class HttpUtility {

    public static void GetRequest(String URL, final Map<String,String> params,
                                  final Map<String, String> headers,final ApiResponseListener apiResponse){

        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    apiResponse.onResponse(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    apiResponse.onError(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }

                @Override
                 public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> apiHeaders= new HashMap<>();
                    apiHeaders.putAll(headers);
                    apiHeaders.put("Cookie",GlobalData.session);
                    return apiHeaders;
                }
            };
            AppController.getInstance().addToRequestQueue(request);
    }

    public static void PostRequest(final Context context,String url,String body, final ApiResponseListener apiResponseListener, final boolean isLogin){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                url,body, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                String json=jsonObject.toString();
                apiResponseListener.onResponse(json);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                 apiResponseListener.onError(volleyError);
            }
        })

        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if(isLogin) {
                    Map<String, String> responseHeaders = response.headers;
                    Iterator myVeryOwnIterator = responseHeaders.keySet().iterator();
                    while (myVeryOwnIterator.hasNext()) {
                        String key = (String) myVeryOwnIterator.next();
                        String value = (String) responseHeaders.get(key);
                        if (key.equals("set-cookie")) {
                            String[] arr = value.split(":");
                            GlobalData.session = arr[0];
                            SharedPrefUtility sharedPrefUtility=new SharedPrefUtility(context);
                            sharedPrefUtility.saveString(SharedPreferencesKey.SESSION_KEY,GlobalData.session);
                        }
                    }
                }
                return super.parseNetworkResponse(response);
            }
            @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> sessionHashmap= new HashMap<>();
                if(!isLogin) {
                    sessionHashmap.put("Cookie", GlobalData.session);
                }
                return sessionHashmap;
            }};
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public interface ApiResponseListener{
        void onResponse(String json);
        void onError(VolleyError error);
    }

    public static void uploadImageRequest(String url,File file, final ApiResponseListener apiResponseListener){
            /*imagereq multipartRequest=new imagereq(url, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                apiResponseListener.onError(volleyError);
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    apiResponseListener.onResponse(s);
                }
            },file,"");*/

        /*MultipartRequest multipartRequest =new MultipartRequest(url, file, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                apiResponseListener.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                apiResponseListener.onError(volleyError);
            }
        });*/

        MultipartRequest3 multipartRequest=new MultipartRequest3(url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        },file,null);

        AppController.getInstance().addToRequestQueue(multipartRequest);

    }
}
