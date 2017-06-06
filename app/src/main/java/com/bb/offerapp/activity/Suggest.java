package com.bb.offerapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;

public class Suggest extends BaseActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        setFinishOnTouchOutside(false);

        button= (Button) findViewById(R.id.suggest_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Suggest.this, "意见已发送，3个工作日内给您答复", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
