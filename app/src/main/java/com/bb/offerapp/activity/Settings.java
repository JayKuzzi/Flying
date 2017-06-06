package com.bb.offerapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.QuestionRecyclerAdapter;
import com.bb.offerapp.bean.QuestionBean;
import com.bb.offerapp.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class Settings extends BaseActivity implements View.OnClickListener{
    private LinearLayout suggest,chat,about;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }


    private void init() {
        suggest= (LinearLayout) findViewById(R.id.settings_suggest);
        chat= (LinearLayout) findViewById(R.id.settings_chat);
        about= (LinearLayout) findViewById(R.id.settings_about);
        suggest.setOnClickListener(this);
        chat.setOnClickListener(this);
        about.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_suggest:
                Intent intent =new Intent(Settings.this,Suggest.class);
                startActivity(intent);
                break;
            case R.id.settings_chat:
                Intent intent2 =new Intent(Settings.this,Chat.class);
                startActivity(intent2);
                break;
            case R.id.settings_about:
                Intent intent3 =new Intent(Settings.this,About.class);
                startActivity(intent3);
                break;
        }
    }
}
