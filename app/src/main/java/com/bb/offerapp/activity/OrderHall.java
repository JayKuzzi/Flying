package com.bb.offerapp.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.HallRecyclerAdapter;
import com.bb.offerapp.adapter.MyViewPaperAdapterInOrder;
import com.bb.offerapp.adapter.OrderRecyclerAdapter;
import com.bb.offerapp.bean.OrderLists;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.view.WrapContentHeightViewPager;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/5/18.
 */

public class OrderHall extends BaseActivity {

    TextView no_order;
    private RecyclerView mRecyclerView;
    private HallRecyclerAdapter hallRecyclerAdapter;
    private List<OrderLists> mItemInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhall);
        no_order = (TextView) findViewById(R.id.if_no_order_hall);
        mItemInfoList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.hall_recycle);
        initData();
        init();
    }



    private void initData() {
        List<OrderLists> orderlist = DataSupport.where("state= ?", "待抢").order("id desc").find(OrderLists.class);
        for (OrderLists item : orderlist) {
            OrderLists Order0 = new OrderLists();
            Order0.setOrderNum(item.getOrderNum());
            Order0.setState(item.getState());
            Order0.setSendInfo(item.getSendInfo());
            Order0.setReciverInfo(item.getReciverInfo());
            Order0.setDate(item.getDate());
            Order0.setOrderPrice(item.getOrderPrice());
            mItemInfoList.add(Order0);
        }

    }

    private void init() {
        hallRecyclerAdapter = new HallRecyclerAdapter(mItemInfoList, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(hallRecyclerAdapter);
        mRecyclerView.setHasFixedSize(true);
        //嵌套滑动 可以让其由于惯性滑动
        mRecyclerView.setNestedScrollingEnabled(false);

        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            no_order.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            no_order.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        else {
            switch (requestCode) {
                case 10:
                    Bundle bundle = data.getExtras();
                    String worker_name = bundle.getString("worker_info_name");
                    String worker_phone = bundle.getString("worker_info_phone");

                    List<OrderLists> orderlist = DataSupport.where("state= ?", "待抢").order("id desc").find(OrderLists.class);
                    OrderLists change = orderlist.get(hallRecyclerAdapter.getPosition());
                    OrderLists update_yiqiang = new OrderLists();
                    update_yiqiang.setState("已抢");
                    update_yiqiang.setWorkerInfo(worker_name + "," + worker_phone);
                    update_yiqiang.updateAll("orderNum = ?", change.getOrderNum());

                    mItemInfoList.clear();
                    initData();
                    hallRecyclerAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "已成功抢单", Toast.LENGTH_LONG).show();
                    if (mItemInfoList.isEmpty()) {
                        mRecyclerView.setVisibility(View.GONE);
                        no_order.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        no_order.setVisibility(View.GONE);
                    }

                    Intent it = new Intent(this, OfferAppMainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, it, 0);
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(this)
                            .setContentTitle("飞送")
                            .setContentText("您的订单已被接单")
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.run_app_icon)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .build();
                    manager.notify(100, notification);
                    break;

            }
        }
    }
}

