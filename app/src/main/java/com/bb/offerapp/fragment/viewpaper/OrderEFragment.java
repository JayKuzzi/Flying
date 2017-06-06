package com.bb.offerapp.fragment.viewpaper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.OrderRecyclerAdapterInD;
import com.bb.offerapp.adapter.OrderRecyclerAdapterInE;
import com.bb.offerapp.bean.OrderLists;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/5/18.
 */

public class OrderEFragment extends Fragment {
    private View view;
    private TextView textView;

    private RecyclerView mRecyclerView;
    private OrderRecyclerAdapterInE mRecyclerAdapterInE;
    private List<OrderLists> mItemInfoList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_e_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.e_fragment_recycle);
        mItemInfoList = new ArrayList<>();

        textView = (TextView) view.findViewById(R.id.if_no_order_e);
        initData();
        initView_RecyclerView();

        return view;
    }

    private void initData() {
        List<OrderLists> orderlist = DataSupport.where("state= ?", "完成").order("id desc").find(OrderLists.class);
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
        mRecyclerAdapterInE = new OrderRecyclerAdapterInE(mItemInfoList, getActivity());
        mRecyclerView.setAdapter(mRecyclerAdapterInE);

        //嵌套滑动 可以让其由于惯性滑动
        mRecyclerView.setNestedScrollingEnabled(false);

        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }

    }

    public void refresh() {
        mItemInfoList.clear();
        initData();
        mRecyclerAdapterInE.notifyDataSetChanged();
        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }
}
