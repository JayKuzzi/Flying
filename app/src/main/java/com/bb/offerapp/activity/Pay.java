package com.bb.offerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.bean.OrderLists;
import com.bb.offerapp.bean.User;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.Constant;
import com.bb.offerapp.util.WebService;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Random;


public class Pay extends BaseActivity implements
        View.OnClickListener {

    public static final int SUCCESS = 1;
    public static final int FALSE = 2;
    //订单信息变量
    String login_name;//下单人员名称，用于下单编号的生成；
    String send_info_name;
    String send_info_phone;
    String send_info_address;
    String receive_info_name;
    String receive_info_phone;
    String receive_info_address;
    String goods_info_name;
    String goods_info_minute;
    String goods_info_message;
    String goods_info_weight;
    String goods_info_hour_of_day;
    String random_distance2;
    private TextView payMoney;
    private Button ok;
    private RadioButton wechat, ali, paypal;
    private RadioGroup pay_method;
    private ProgressDialog progressDialog;
    private ProgressDialog loding;
    private String result_pay_method = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.SERVER_NOT_RETURN:
                    loding.dismiss();
                    ok.setEnabled(true);
                    Toast.makeText(Pay.this, "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.PAY_DONE:
                    progressDialog.dismiss();
                    loding = new ProgressDialog(Pay.this);
                    loding.setTitle("正在下单");
                    loding.setMessage("正在将数据写入数据库");
                    loding.setCancelable(false);
                    loding.show();
                    new Thread(new addOrderThread()).start();
                    break;
                case Constant.ADD_ORDER_SUCCESS:
                    loding.dismiss();
                    Toast.makeText(Pay.this, "下单成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("isSuccess", SUCCESS);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case Constant.ADD_ORDER_FAIL:
                    loding.dismiss();
                    Intent intent2 = new Intent();
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("isSuccess", FALSE);
                    intent2.putExtras(bundle2);
                    setResult(RESULT_OK, intent2);
                    finish();
                    break;
            }

        }
    };

    public static String getNowDate() {
        java.util.Date nowdate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(nowdate);
    }

    public static String getNowDateWithNo_ss() {
        java.util.Date nowdate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(nowdate);
    }

    public static String getNowDateWithNo_time() {
        java.util.Date nowdate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(nowdate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setFinishOnTouchOutside(false);
        init();


        ok.setOnClickListener(this);
        radioGroupDoSomething();
    }

    private void radioGroupDoSomething() {
        pay_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.wechat_pay:
                        wechat.setBackground(getResources().getDrawable(R.mipmap.select_wechatpay));
                        ali.setBackground(getResources().getDrawable(R.mipmap.zhifubao));
                        paypal.setBackground(getResources().getDrawable(R.mipmap.paypal));
                        result_pay_method = "微信";
                        break;
                    case R.id.ali_pay:
                        wechat.setBackground(getResources().getDrawable(R.mipmap.wechatpay));
                        ali.setBackground(getResources().getDrawable(R.mipmap.select_zhifubao));
                        paypal.setBackground(getResources().getDrawable(R.mipmap.paypal));
                        result_pay_method = "支付宝";
                        break;
                    case R.id.paypal_pay:
                        wechat.setBackground(getResources().getDrawable(R.mipmap.wechatpay));
                        ali.setBackground(getResources().getDrawable(R.mipmap.zhifubao));
                        paypal.setBackground(getResources().getDrawable(R.mipmap.select_paypal));
                        result_pay_method = "paypal";
                        break;
                }
            }
        });

    }

    private void init() {
        payMoney = (TextView) findViewById(R.id.pay_money);
        ok = (Button) findViewById(R.id.pay_ok);
        wechat = (RadioButton) findViewById(R.id.wechat_pay);
        ali = (RadioButton) findViewById(R.id.ali_pay);
        paypal = (RadioButton) findViewById(R.id.paypal_pay);
        pay_method = (RadioGroup) findViewById(R.id.pay_method);

        Intent intent = getIntent();
        send_info_name = intent.getExtras().getString("send_info_name");
        send_info_phone = intent.getExtras().getString("send_info_phone");
        send_info_address = intent.getExtras().getString("send_info_address");
        receive_info_name = intent.getExtras().getString("receive_info_name");
        receive_info_phone = intent.getExtras().getString("receive_info_phone");
        receive_info_address = intent.getExtras().getString("receive_info_address");
        goods_info_name = intent.getExtras().getString("goods_info_name");
        goods_info_minute = intent.getExtras().getString("goods_info_minute");
        goods_info_message = intent.getExtras().getString("goods_info_message");
        goods_info_weight = intent.getStringExtra("goods_info_weight");
        goods_info_hour_of_day = intent.getStringExtra("goods_info_hour_of_day");
        random_distance2 = intent.getStringExtra("goods_info_distance");
        payMoney.setText(getMoney(goods_info_weight, goods_info_hour_of_day ,random_distance2));
        login_name = intent.getStringExtra("login_name");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_ok:
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(Pay.this, "网络未连接哦", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if (result_pay_method.equals("")) {
                    Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
                } else {
                    ok.setEnabled(false);
                    progressDialog = new ProgressDialog(Pay.this);
                    progressDialog.setTitle("在线支付");
                    progressDialog.setMessage("模拟支付，您的存款不会因此而减少");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    handler.sendEmptyMessageDelayed(Constant.PAY_DONE, 3000);
                }
                break;
        }

    }

    public String getMoney(String weight, String hour, String distance) {
        String result = null;
        int total = 8;
        if (Integer.parseInt(distance) > 0 && Integer.parseInt(distance)  <= 2) {
            total += 0;
        } else if (Integer.parseInt(distance) > 2 && Integer.parseInt(distance) <= 10) {
            for (int i = 1; i <= (Integer.parseInt(distance) - 2); i++) {
                total += 2;
            }
        } else if (Integer.parseInt(distance) > 10 && Integer.parseInt(distance) <= 50) {
            for (int i = 1; i <= (Integer.parseInt(distance) - 10); i++) {
                total += 3;
            }
        }

        if (Integer.parseInt(weight) > 0 && Integer.parseInt(weight) <= 5) {
            total += 0;
        } else if (Integer.parseInt(weight) > 5 && Integer.parseInt(weight) <= 10) {
            for (int i = 1; i <= (Integer.parseInt(weight) - 5); i++) {
                total += 2;
            }
        } else if (Integer.parseInt(weight) > 10 && Integer.parseInt(weight) <= 50) {
            for (int i = 1; i <= (Integer.parseInt(weight) - 10); i++) {
                total += 3;
            }
        }

        if (Integer.parseInt(hour) >= 0 && Integer.parseInt(hour) < 7) {
            total += 8;
        } else if (Integer.parseInt(hour) >= 22 && Integer.parseInt(hour) < 24) {
            total += 4;
        }

        return result.valueOf(total);
    }


    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    private class addOrderThread implements Runnable {
        @Override
        public void run() {
            Message message = handler.obtainMessage();
            JSONObject object=new JSONObject();
            try {
                object.put("orderNum", login_name.toUpperCase() + getNowDate());
                object.put("date",getNowDateWithNo_ss());
                if(Integer.parseInt(goods_info_hour_of_day) < 10){
                    object.put("go_home_time",getNowDateWithNo_time()+" "+"0"+goods_info_hour_of_day + ":" + goods_info_minute);
                }else{
                    object.put("go_home_time",getNowDateWithNo_time()+" "+goods_info_hour_of_day + ":" + goods_info_minute);
                }
                object.put("distance", random_distance2);
                object.put("palWay", result_pay_method);
                object.put("orderPrice", payMoney.getText().toString());
                object.put("state", "待抢");
                object.put("weight", goods_info_weight);
                object.put("message", goods_info_message);
                object.put("workerInfo", "目前还没有被抢单");
                object.put("goodsInfo", goods_info_name);
                object.put("sendInfo_name", send_info_name);
                object.put("sendInfo_phone", send_info_phone);
                object.put("sendInfo", send_info_address);
                object.put("receiverInfo_name", receive_info_name);
                object.put("receiverInfo_phone", receive_info_phone);
                object.put("receiverInfo", receive_info_address);
                String result= WebService.executeHttpPost(object,"AddOrder");
                if(result.equals("")||result==null){
                    message.what=Constant.SERVER_NOT_RETURN;
                }else {
                    message.what=Integer.parseInt(result);
                }
                handler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
