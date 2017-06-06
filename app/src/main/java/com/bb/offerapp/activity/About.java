package com.bb.offerapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;

/**
 * Created by bb on 2017/3/15.
 */

public class About extends BaseActivity {
    private ImageView imageView;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        imageView= (ImageView) findViewById(R.id.author);
        linearLayout= (LinearLayout) findViewById(R.id.group);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayout.getVisibility() == View.GONE){
                    linearLayout.setVisibility(View.VISIBLE);
                }else if(linearLayout.getVisibility() == View.VISIBLE){
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
    }
}
