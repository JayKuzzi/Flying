package com.bb.offerapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.bb.offerapp.R;

/**
 * Created by bb on 2017/3/14.
 */

public class CityNameDialog extends Dialog{
    private EditText editText;

    private Button ok;
    private Button cancel;
    private String typeCityName;
    public CityNameDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_weather_city);
        initView();
    }


    private void initView() {
        editText= (EditText) findViewById(R.id.city_namee);
        ok= (Button) findViewById(R.id.ok);
        typeCityName=editText.getText().toString();
        cancel= (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public Button getOk() {
        return ok;
    }

    public EditText getEditText() {
        return editText;
    }



}
