package com.acresqaure.acresqaure.HttpUtility;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.paxcel.volley.AppController;

import org.json.JSONObject;

import java.io.File;
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
                    return headers;
                }
            };
            AppController.getInstance().addToRequestQueue(request);
    }

    public static void PostRequest(String url,String body, final ApiResponseListener apiResponseListener){

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
        });
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

        MultipartRequest multipartRequest =new MultipartRequest(url, file, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                apiResponseListener.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                apiResponseListener.onError(volleyError);
            }
        });

        AppController.getInstance().addToRequestQueue(multipartRequest);

    }
}
