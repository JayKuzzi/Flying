package com.bb.offerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.bb.offerapp.R;


/**
 * Created by bb on 2017/3/15.
 */

public class Welcome extends AppCompatActivity {
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_welcome, null);
        setContentView(view);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Intent intent = new Intent(Welcome.this, OfferAppMainActivity.class);
                startActivity(intent);
                finish();
            }
        };

//        //渐变展示启动屏
//        AlphaAnimation aa = new AlphaAnimation(0.4f, 1.0f);
//        aa.setDuration(2000);
//        view.startAnimation(aa);
//        aa.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationEnd(Animation arg0) {
////                handler.sendEmptyMessageDelayed(0,1000);
////                Intent intent = new Intent(Welcome.this, OfferAppMainActivity.class);
////                startActivity(intent);
////                finish();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//        });

        handler.sendEmptyMessageDelayed(0,2000);
    }


}



