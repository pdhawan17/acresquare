package com.acresqaure.acresqaure;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class SplashScreen extends Activity {

    ImageView ivLogo;
    long SPLASH_TIME_OUT= 3*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getViewId();
        startAnimation();
        startSplashScreenTimer();
    }

    public void getViewId(){
        ivLogo=(ImageView)findViewById(R.id.ivLogo);
    }

    public void startAnimation(){
        AlphaAnimation  blinkanimation= new AlphaAnimation(1, 0.5f);
        blinkanimation.setDuration(800);
        blinkanimation.setInterpolator(new LinearInterpolator());
        blinkanimation.setRepeatCount(Animation.INFINITE);
        blinkanimation.setRepeatMode(Animation.REVERSE);
        ivLogo.startAnimation(blinkanimation);
    }

    public void startSplashScreenTimer(){
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, RegisterScreen.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
