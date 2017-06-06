package com.bb.offerapp.fragment.viewpaper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.activity.OfferAppMainActivity;
import com.bb.offerapp.adapter.OrderRecyclerAdapterInB;
import com.bb.offerapp.adapter.OrderRecyclerAdapterInC;
import com.bb.offerapp.bean.OrderLists;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/5/18.
 */

public class OrderCFragment extends Fragment {
    private View view;
    private TextView textView;
    private RecyclerView mRecyclerView;
    private OrderRecyclerAdapterInC mRecyclerAdapterInC;
    private List<OrderLists> mItemInfoList;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_c_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.c_fragment_recycle);
        mItemInfoList = new ArrayList<>();
        textView = (TextView) view.findViewById(R.id.if_no_order_c);
        initData();
        initView_RecyclerView();
        return view;
    }

    private void initData() {
        List<OrderLists> orderlist = DataSupport.where("state= ?", "配送").order("id desc").find(OrderLists.class);
        for (OrderLists item : orderlist) {
            OrderLists Order0 = new OrderLists();
            Order0.setOrderNum(item.getOrderNum());
            Order0.setState(item.getState());
            Order0.setSendInfo(item.getSendInfo());
            Order0.setReciverInfo(item.getReciverInfo());
            Order0.setDate(item.getDate());
            mItemInfoList.add(Order0);
        }

    }


    private void initView_RecyclerView() {
        //设置线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerAdapterInC = new OrderRecyclerAdapterInC(mItemInfoList, getActivity());
        mRecyclerView.setAdapter(mRecyclerAdapterInC);

        //嵌套滑动 可以让其由于惯性滑动
        mRecyclerView.setNestedScrollingEnabled(false);

        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }

        mRecyclerAdapterInC.setOnListChangedListener(new OrderRecyclerAdapterInC.ListChangedListener() {
            @Override
            public void onListChangedClick() {
                refresh();
                Intent it = new Intent(getActivity(), OfferAppMainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, it, 0);
                NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(getActivity())
                        .setContentTitle("飞送")
                        .setContentText("您的订单已经送达")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.run_app_icon)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                manager.notify(300, notification);
                Toast.makeText(getActivity(), "请联系收货方取货", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void refresh(){
        mItemInfoList.clear();
        initData();
        mRecyclerAdapterInC.notifyDataSetChanged();
        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }
}
