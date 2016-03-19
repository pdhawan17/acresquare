package com.acresqaure.acresqaure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.acresqaure.acresqaure.Constants.TextConstants;
import com.acresqaure.acresqaure.HttpUtility.HttpUtility;
import com.acresqaure.acresqaure.HttpUtility.URLs;
import com.acresqaure.acresqaure.Listeners.ErrorDialogActionListener;
import com.acresqaure.acresqaure.Utility.Utility;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by parasdhawan on 19/02/16.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    TextView tvLogin,tvLoginWithFacebook;
    EditText etUserName,etPassword;

    public static final LoginFragment newInstance(String message)
    {
        LoginFragment f = new LoginFragment();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        getViewId(v);
        setClickListeners();
        return v;
    }

    public void getViewId(View view){
        tvLogin = (TextView)view.findViewById(R.id.tvLogin);
        tvLoginWithFacebook = (TextView)view.findViewById(R.id.tvLoginWithFacebook);
        etUserName = (EditText)view.findViewById(R.id.etUserName);
        etPassword = (EditText)view.findViewById(R.id.etPassword);
    }

    public void setClickListeners(){
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvLogin:
                Utility.hideSoftKeyboard(getActivity());
                if(isValidInput()){
                    if(Utility.isNetworkConnected(getActivity())) {
                        login();
                    }else {
                        Utility.showMessageDialog(getActivity(), TextConstants.NO_INTERNET_MSG);
                    }
                }else {

                }
                break;
        }
    }

    public void login(){
        Utility.showProgressDialog(getActivity());
        HttpUtility.PostRequest(getActivity(),URLs.LOGIN_URL, getJson(), new HttpUtility.ApiResponseListener() {
            @Override
            public void onResponse(String json) {
                Utility.dismissProgressDialog();
                if(Utility.isApiResponseSuccess(json)){
                    Intent intent=new Intent(getActivity(),AddNewListing.class);
                    getActivity().startActivity(intent);
                }else {
                    showErrorToast(json);
                }
            }

            @Override
            public void onError(VolleyError error) {
                Utility.dismissProgressDialog();
                showRetryDialog();
            }
        },true);
    }

    public void showRetryDialog(){
        Utility.showRetryDialog(getActivity(), new ErrorDialogActionListener() {
            @Override
            public void onRetry() {
                login();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void showErrorToast(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
            String status= jsonObject.optString("Status");
            if(!status.equals("")) {
                Utility.showMessageDialog(getActivity(),status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getJson(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("userEmail",etUserName.getText().toString().trim());
            jsonObject.put("userPassword",etPassword.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public boolean isValidInput(){
        boolean isValid=true;
        if(etUserName.getText().toString().trim().equals("")){
            Utility.showToast(getActivity(),"Please enter your user name");
            Utility.setEditTextWarning(etUserName,getActivity(),"Registered phone number");
            isValid= false;
        }else {
            Utility.removeEditTextWarning(etUserName);
        }
        if(etPassword.getText().toString().trim().equals("")){
            Utility.showToast(getActivity(),"Please enter your password");
            Utility.setEditTextWarning(etPassword,getActivity(),"Enter your password");
            isValid= false;
        }else {
            Utility.removeEditTextWarning(etPassword);
        }

        return isValid;
    }
}
