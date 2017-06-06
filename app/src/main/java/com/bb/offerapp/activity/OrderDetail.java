package com.bb.offerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.view.MyTextView;

public class OrderDetail extends BaseActivity {
    private TextView money,distance,weight,num,state,date,pay_way,pay_money,worker,go_home_time;
    private MyTextView send_add,send_name_phone,receive_add,receive_name_phone,goods_name,goods_message;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_order_detail);
        init();

        Intent intent = getIntent();
        money.setText("总价:"+intent.getExtras().getString("money")+"元");
        distance.setText("距离:"+intent.getExtras().getString("distance")+"公里");
        weight.setText("重量:"+intent.getExtras().getString("goods_info_weight")+"公斤");
        num.setText(intent.getExtras().getString("num"));
        state.setText(intent.getExtras().getString("state"));
        date.setText("下单时间："+intent.getExtras().getString("time"));
        pay_way.setText("支付方式："+intent.getExtras().getString("pay_way")+"移动支付");
        pay_money.setText("实付款："+intent.getExtras().getString("money")+"元");
        send_add.setText(intent.getExtras().getString("send_info_address"));
        send_name_phone.setText(intent.getExtras().getString("send_info_name")+","+intent.getExtras().getString("send_info_phone"));
        receive_add.setText(intent.getExtras().getString("receive_info_address"));
        receive_name_phone.setText(intent.getExtras().getString("receive_info_name")+","+intent.getExtras().getString("receive_info_phone"));
        goods_name.setText("物品名称："+intent.getExtras().getString("goods_info_name"));
        goods_message.setText("备注信息："+intent.getExtras().getString("goods_info_message"));
        worker.setText("配送员信息："+intent.getExtras().getString("worker_info"));
        go_home_time.setText("取件时间："+intent.getExtras().getString("go_home_time"));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(OrderDetail.this,cal.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        imageView= (ImageView) findViewById(R.id.detail_cal);
        money= (TextView) findViewById(R.id.detail_money);
        distance= (TextView) findViewById(R.id.detail_distance);
        weight= (TextView) findViewById(R.id.detail_weight);
        num= (TextView) findViewById(R.id.detail_num);
        state= (TextView) findViewById(R.id.detail_state);
        date= (TextView) findViewById(R.id.detail_date);
        pay_way= (TextView) findViewById(R.id.detail_pay_way);
        pay_money= (TextView) findViewById(R.id.detail_pay_money);
        send_add= (MyTextView) findViewById(R.id.detail_send_add);
        send_name_phone= (MyTextView) findViewById(R.id.detail_send_name_phone);
        receive_add= (MyTextView) findViewById(R.id.detail_receive_add);
        receive_name_phone= (MyTextView) findViewById(R.id.detail_receive_name_phone);
        goods_name= (MyTextView) findViewById(R.id.detail_goods_name);
        goods_message= (MyTextView) findViewById(R.id.detail_goods_message);
        worker= (TextView) findViewById(R.id.detail_worker);
        go_home_time= (TextView) findViewById(R.id.detail_go_home);
    }
}
