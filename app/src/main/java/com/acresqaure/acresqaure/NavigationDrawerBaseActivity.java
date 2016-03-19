package com.acresqaure.acresqaure;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class NavigationDrawerBaseActivity extends Activity {

    LinearLayout llContent;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_base);
        llContent = (LinearLayout)findViewById(R.id.llContent);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
    }

    public void setContent(int layoutId){
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(layoutId,null);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        llContent.addView(view);
    }

    public void openNavigationDrawer(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }
}
