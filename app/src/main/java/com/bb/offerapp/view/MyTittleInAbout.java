package com.bb.offerapp.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bb.offerapp.R;

/**
 * Created by bb on 2017/4/18.
 */

//自定义布局，并写逻辑 方便以后复用逻辑。    相比include引入布局，不用每次都写逻辑。
public class MyTittleInAbout extends LinearLayout{
    private ImageView bt1;
    public MyTittleInAbout(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.mytittle_about,this);
        bt1= (ImageView) findViewById(R.id.tittle_about_back);
        bt1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭当前
                ((Activity)getContext()).finish();
            }
        });
    }
}
