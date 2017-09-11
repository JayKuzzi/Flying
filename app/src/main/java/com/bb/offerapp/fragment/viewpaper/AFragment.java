package com.bb.offerapp.fragment.viewpaper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.activity.GoodsInfo;
import com.bb.offerapp.activity.OfferAppMainActivity;
import com.bb.offerapp.activity.Pay;
import com.bb.offerapp.activity.ReceiveInfo;
import com.bb.offerapp.activity.SendInfo;

import com.bb.offerapp.view.MyTextView;


import java.util.Random;

/**
 * Created by bb on 2016/11/18.
 */
public class AFragment extends Fragment implements View.OnClickListener {
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
    String random_distance;
    Random random =new Random();
    private View view;
    private MyTextView send, receive, goodsInfo;
    private TextView priceInfo;
    private Button createOrder;
    private String state;//订单状态
    private String workerInfo;//配送员信息


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycle_a_fragment, container, false);
        init();
        send.setOnClickListener(this);
        receive.setOnClickListener(this);
        goodsInfo.setOnClickListener(this);
        createOrder.setOnClickListener(this);
        priceInfo.setOnClickListener(this);
        return view;
    }



    private void init() {
        send = (MyTextView) view.findViewById(R.id.home_send);
        receive = (MyTextView) view.findViewById(R.id.home_receive);
        goodsInfo = (MyTextView) view.findViewById(R.id.home_goodsinfo);
        createOrder = (Button) view.findViewById(R.id.create_order);
        priceInfo = (TextView) view.findViewById(R.id.price_info);
        priceInfo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        priceInfo.setTextColor(Color.BLUE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_send:
                OfferAppMainActivity main = (OfferAppMainActivity) getActivity();
                login_name = main.getLogin_name().getText().toString();
                if (login_name.equals("未登录")) {
                    Toast.makeText(getActivity(), "请先登录再进行此操作", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), SendInfo.class);
                    startActivityForResult(intent, 300);
                }
                break;
            case R.id.home_receive:
                OfferAppMainActivity main2 = (OfferAppMainActivity) getActivity();
                login_name = main2.getLogin_name().getText().toString();
                if (login_name.equals("未登录")) {
                    Toast.makeText(getActivity(), "请先登录再进行此操作", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent2 = new Intent(getActivity(), ReceiveInfo.class);
                    startActivityForResult(intent2, 400);
                }
                break;
            case R.id.home_goodsinfo:
                OfferAppMainActivity main3 = (OfferAppMainActivity) getActivity();
                login_name = main3.getLogin_name().getText().toString();
                if (login_name.equals("未登录")) {
                    Toast.makeText(getActivity(), "请先登录再进行此操作", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent3 = new Intent(getActivity(), GoodsInfo.class);
                    startActivityForResult(intent3, 500);
                }
                break;
            case R.id.create_order:
                OfferAppMainActivity main4 = (OfferAppMainActivity) getActivity();
                login_name = main4.getLogin_name().getText().toString();
                if (login_name.equals("未登录")) {
                    Toast.makeText(getActivity(), "请先登录再进行此操作", Toast.LENGTH_SHORT).show();
                } else if (send.getText().toString().equals("物品从哪里寄出") ||
                        receive.getText().toString().equals("物品寄到哪里去") ||
                        goodsInfo.getText().toString().equals("物品、重量、时间、备注")) {
                    Toast.makeText(getActivity(), "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    random_distance=random_distance.valueOf(random.nextInt(20)+1);
                    Intent pay = new Intent(getActivity(), Pay.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("send_info_name", send_info_name);
                    bundle.putString("send_info_phone", send_info_phone);
                    bundle.putString("send_info_address", send_info_address);
                    bundle.putString("receive_info_name", receive_info_name);
                    bundle.putString("receive_info_phone", receive_info_phone);
                    bundle.putString("receive_info_address", receive_info_address);
                    bundle.putString("goods_info_name", goods_info_name);
                    bundle.putString("goods_info_minute", goods_info_minute);
                    bundle.putString("goods_info_message", goods_info_message);
                    bundle.putString("goods_info_weight", goods_info_weight);
                    bundle.putString("goods_info_hour_of_day", goods_info_hour_of_day);
                    //随机30公里以内的距离
                    bundle.putString("goods_info_distance",random_distance);
                    bundle.putString("login_name", login_name);
                    pay.putExtras(bundle);
                    startActivityForResult(pay, 600);
                    Toast.makeText(getActivity(), "随机产生公里数为"+random_distance, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.price_info:
                OfferAppMainActivity offerAppMainActivity = (OfferAppMainActivity) getActivity();
                offerAppMainActivity.getViewPager().setCurrentItem(1);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != getActivity().RESULT_OK)
            return;
        else {
            switch (requestCode) {
                case 300:
                    Bundle bundle = data.getExtras();
                    send_info_name = data.getStringExtra("send_info_name");
                    send_info_phone = data.getStringExtra("send_info_phone");
                    send_info_address = bundle.getString("send_info_address");
                    send.setText(send_info_address + "  " + send_info_name + "  " + send_info_phone);
                    break;
                case 400:
                    Bundle bundle2 = data.getExtras();
                    receive_info_name = data.getStringExtra("receive_info_name");
                    receive_info_phone = data.getStringExtra("receive_info_phone");
                    receive_info_address = bundle2.getString("receive_info_address");
                    receive.setText(receive_info_address + "  " + receive_info_name + "  " + receive_info_phone);
                    break;
                case 500:
                    Bundle bundle3 = data.getExtras();
                    goods_info_name = bundle3.getString("goods_info_name");
                    goods_info_weight = bundle3.getString("goods_info_weight");
                    goods_info_hour_of_day = bundle3.getString("goods_info_hour_of_day");
                    goods_info_minute = bundle3.getString("goods_info_minute");
                    goods_info_message = bundle3.getString("goods_info_message");
                    goodsInfo.setText("物品名称：" + goods_info_name + "  重量：" + goods_info_weight + " Kg" +
                            "  取件时间：" + goods_info_hour_of_day + ":" + goods_info_minute + "  备注信息："
                            + goods_info_message);
                    break;
                case 600:
                    Bundle bundle4 = data.getExtras();
                    int isDone = bundle4.getInt("isSuccess");
                    if (isDone == Pay.SUCCESS) {
                        send.setText("物品从哪里寄出");
                        receive.setText("物品寄到哪里去");
                        goodsInfo.setText("物品、重量、时间、备注");
                    }

            }

        }

    }
}
