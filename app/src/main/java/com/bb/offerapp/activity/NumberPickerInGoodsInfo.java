package com.bb.offerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;

public class NumberPickerInGoodsInfo extends BaseActivity {
    private NumberPicker numberPicker;
    private String resultWeight = "1";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_number);
        setFinishOnTouchOutside(false);
        numberPicker = (NumberPicker) findViewById(R.id.num_pick);
        button= (Button) findViewById(R.id.num_pick_ok);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(50);
        numberPicker.setValue(1);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                resultWeight = String.valueOf(newVal);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("weight",resultWeight);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("time", "onDestroy: ");
    }
}
