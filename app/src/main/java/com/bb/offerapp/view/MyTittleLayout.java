package com.bb.offerapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.TestArrayAdapter;

/**
 * Created by bb on 2017/4/18.
 */

//自定义布局，并写逻辑 方便以后复用逻辑。    相比include引入布局，不用每次都写逻辑。
public class MyTittleLayout extends LinearLayout{
    private ImageView bt1,bt2,bt3;
    TextView textView;
    LinearLayout linearLayout;
    EditText editText;
    Spinner spinner;

    public EditText getEditText() {
        return editText;
    }

    private TestArrayAdapter mAdapter ;
    private String [] mStringArray;

    public ImageView getBt1() {
        return bt1;
    }

    public ImageView getBt2() {
        return bt2;
    }

    public ImageView getBt3() {
        return bt3;
    }

    public TextView getTextView() {
        return textView;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public MyTittleLayout(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.mytittle,this);
        bt1= (ImageView) findViewById(R.id.tittle_back);
        bt2= (ImageView) findViewById(R.id.tittle_edit);
        textView= (TextView) findViewById(R.id.tittle_tittle);
        bt3= (ImageView) findViewById(R.id.tittle_done);
        linearLayout= (LinearLayout) findViewById(R.id.tittle_search);
        editText= (EditText) findViewById(R.id.tittle_edit_text);
        spinner= (Spinner) findViewById(R.id.tittle_spinner);
        mStringArray=getResources().getStringArray(R.array.type);
        //使用自定义的ArrayAdapter
        mAdapter = new TestArrayAdapter(getContext(),mStringArray);
        spinner.setAdapter(mAdapter);



        bt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bt2.setVisibility(GONE);
                textView.setVisibility(GONE);
                linearLayout.setVisibility(VISIBLE);
                bt3.setVisibility(VISIBLE);
            }
        });
    }



}
