package com.bb.offerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;

public class TimePickerInGoodsInfo extends BaseActivity {
    private TimePicker timePicker;
    private String resultTime_hour,resultTime_minute;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_time);
        setFinishOnTouchOutside(false);
        timePicker = (TimePicker) findViewById(R.id.time_pick);
        button= (Button) findViewById(R.id.time_pick_ok);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                resultTime_hour=resultTime_hour.valueOf(hourOfDay);
                if(minute>=10){
                    resultTime_minute=resultTime_hour.valueOf(minute);
                }else{
                    resultTime_minute="0"+resultTime_hour.valueOf(minute);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("time_hour",resultTime_hour);
                bundle.putString("time_minute",resultTime_minute);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
