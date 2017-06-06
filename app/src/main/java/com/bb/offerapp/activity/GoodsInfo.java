package com.bb.offerapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.ReadContactMsg;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class GoodsInfo extends BaseActivity implements
        View.OnClickListener {

    EditText name, message;
    TextView weight, time;
    Button ok;
    ImageView tips1, tips2, tips3;
    String weight_result = null;
    String hour_of_day = null;
    String minute = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        init();
        weight.setOnClickListener(this);
        tips1.setOnClickListener(this);
        tips2.setOnClickListener(this);
        tips3.setOnClickListener(this);
        ok.setOnClickListener(this);
        time.setOnClickListener(this);
        message.setOnClickListener(this);
        setFinishOnTouchOutside(false);
    }

    private void init() {
        name = (EditText) findViewById(R.id.goods_info_name);
        weight = (TextView) findViewById(R.id.goods_info_weight);
        time = (TextView) findViewById(R.id.goods_info_time);
        tips1 = (ImageView) findViewById(R.id.goods_info_get_tips);
        tips2 = (ImageView) findViewById(R.id.goods_info_get_tips2);
        tips3 = (ImageView) findViewById(R.id.goods_info_get_tips3);
        message = (EditText) findViewById(R.id.goods_info_message);
        ok = (Button) findViewById(R.id.goods_info_ok);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_info_weight:
                Intent intent = new Intent(GoodsInfo.this, NumberPickerInGoodsInfo.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.goods_info_time:
                Intent intent2 = new Intent(GoodsInfo.this, TimePickerInGoodsInfo.class);
                startActivityForResult(intent2, 2);
                break;
            case R.id.goods_info_get_tips:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(GoodsInfo.this, R.style.AlertDialogCustom);
                dialog1.setTitle("物品注意事项");
                dialog1.setMessage("国家法律法规禁止寄递的物品：爆炸性、易燃性、腐蚀性、放射性和毒性等危险物品；反动报刊、书籍、窗口或者淫秽物品； 各种货币； 妨害公共卫生的物品；容易腐烂的物品；");
                dialog1.setCancelable(false);
                dialog1.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog1.show();
                break;
            case R.id.goods_info_get_tips2:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(GoodsInfo.this, R.style.AlertDialogCustom);
                dialog2.setTitle("重量加价信息");
                dialog2.setMessage("重量                          价格\n0kg<w≤2kg            不加价\n2kg<w≤10kg          每增1kg加2元\n10kg<w≤50kg        每增1kg加3元");
                dialog2.setCancelable(false);
                dialog2.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog2.show();
                break;
            case R.id.goods_info_get_tips3:
                AlertDialog.Builder dialog3 = new AlertDialog.Builder(GoodsInfo.this, R.style.AlertDialogCustom);
                dialog3.setTitle("夜间加价信息");
                dialog3.setMessage("时间段                    服务费\n00:00 ~ 07:00         8元\n22:00 ~ 24:00         4元");
                dialog3.setCancelable(false);
                dialog3.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog3.show();
                break;
            case R.id.goods_info_ok:
                if (name.getText().toString().equals("") ||
                        weight.getText().toString().equals("")||
                        time.getText().toString().equals("")) {
                    Toast.makeText(GoodsInfo.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().length() < 2) {
                    Toast.makeText(GoodsInfo.this, "物品信息至少两位", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent3 = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_info_name", name.getText().toString());
                    bundle.putString("goods_info_weight", weight_result);
                    bundle.putString("goods_info_hour_of_day", hour_of_day);
                    bundle.putString("goods_info_minute", minute);
                    if(message.getText().toString().equals("")){
                        bundle.putString("goods_info_message", "无");
                    }else{
                        bundle.putString("goods_info_message", message.getText().toString());
                    }
                    intent3.putExtras(bundle);
                    setResult(RESULT_OK, intent3);
                    finish();
                }
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                Bundle bundle = data.getExtras();
                weight_result = bundle.getString("weight");
                weight.setText(weight_result + " Kg");
                break;
            case 2:
                Bundle bundle2 = data.getExtras();
                if (bundle2.getString("time_hour") == null) {
                    Calendar c = Calendar.getInstance();
                    hour_of_day = hour_of_day.valueOf(c.get(Calendar.HOUR_OF_DAY));
                    if(c.get(Calendar.MINUTE)>=10){
                        minute = minute.valueOf(c.get(Calendar.MINUTE));
                    }else{
                        minute = "0"+minute.valueOf(c.get(Calendar.MINUTE));
                    }
                    time.setText(hour_of_day + ":" + minute);
                } else {
                    hour_of_day= bundle2.getString("time_hour");
                    minute = bundle2.getString("time_minute");
                    time.setText(hour_of_day + ":" + minute);
                }
                break;
            default:
                break;
        }
    }


}
