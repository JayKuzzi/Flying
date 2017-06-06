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

public class ChangePassDialog extends Dialog{
    private EditText editText;

    private Button ok;
    private Button cancel;
    private String newPass;
    public ChangePassDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_change_pass);
        initView();
    }


    private void initView() {
        editText= (EditText) findViewById(R.id.change_pass);
        ok= (Button) findViewById(R.id.change_ok);
        newPass=editText.getText().toString();
        cancel= (Button) findViewById(R.id.change_cancel);
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
