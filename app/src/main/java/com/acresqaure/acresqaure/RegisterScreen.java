package com.acresqaure.acresqaure;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acresqaure.acresqaure.Adapters.ViewPagerAdapterSignUp;
import com.acresqaure.acresqaure.Utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class RegisterScreen extends FragmentActivity implements View.OnClickListener {

    ViewPagerAdapterSignUp pageAdapter;
    ViewPager pager;
    LinearLayout llTopBg;
    TextView tvLogin,tvSignUp;

    final static int IMAGE_PICK_REQUEST_CODE =102;
    final static int IMAGE_CAPTURE_REQUEST_CODE=103;

    SignUpFragment signUpFragment;
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        getViewIds();
        setViewPager();
        setOnClickListner();
    }

    public void getViewIds(){
        pager = (ViewPager)findViewById(R.id.viewPager);
        llTopBg = (LinearLayout)findViewById(R.id.llTopBg);
        tvLogin = (TextView)findViewById(R.id.tvLogin);
        tvSignUp = (TextView)findViewById(R.id.tvSignUp);
    }


    public void setOnClickListner(){
        tvSignUp.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvLogin:
                openFragment(false);
                break;
            case R.id.tvSignUp:
                openFragment(true);
                break;
        }
    }

    public void openFragment(boolean openSignUp){
        Utility.hideSoftKeyboard(this);
        if(openSignUp){
            pager.setCurrentItem(0, true);
        }else {
            pager.setCurrentItem(1, true);
        }

    }

    // View Pager Configs------------------------------------------------------------

    public void setViewPager(){
        List<Fragment> fragments = getFragments();
        pageAdapter = new ViewPagerAdapterSignUp(this.getSupportFragmentManager(), fragments);
        pager.setAdapter(pageAdapter);
        setPageChangeListener();
        pager.setCurrentItem(1);
    }

    public void setPageChangeListener(){
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    llTopBg.setBackgroundResource(R.drawable.top_bg_left);
                } else {
                    llTopBg.setBackgroundResource(R.drawable.top_bg_right);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();

        signUpFragment=SignUpFragment.newInstance("Fragment 1");
        loginFragment=LoginFragment.newInstance("Fragment 2");

        fList.add(signUpFragment);
        fList.add(loginFragment);
        return fList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            signUpFragment.setImagePickedFromGalary(data);
        }else if(requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            signUpFragment.setCameraCapturedImage(data);
        }
    }



}
