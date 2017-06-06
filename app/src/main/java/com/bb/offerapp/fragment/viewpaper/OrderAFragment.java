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
import com.bb.offerapp.adapter.OrderRecyclerAdapter;
import com.bb.offerapp.adapter.OrderRecyclerAdapterInB;
import com.bb.offerapp.bean.OrderLists;
import com.bb.offerapp.bean.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/5/18.
 */

public class OrderAFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private OrderRecyclerAdapter mRecyclerAdapter;
    private TextView textView;
    private List<OrderLists> mItemInfoList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_a_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.a_fragment_recycle);
        mItemInfoList = new ArrayList<>();
        textView= (TextView) view.findViewById(R.id.if_no_order_a);
        initData();
        initView_RecyclerView();

        return view;
    }

    private void initData() {
        List<OrderLists> orderlist = DataSupport.where("state= ?", "待抢").order("id desc").find(OrderLists.class);
            for (OrderLists item:orderlist) {
                OrderLists Order0 = new OrderLists();
                Order0.setOrderNum(item.getOrderNum());
                Order0.setState(item.getState());
                Order0.setSendInfo(item.getSendInfo());
                Order0.setReciverInfo(item.getReciverInfo());
                Order0.setDate(item.getDate());
                mItemInfoList.add(Order0);
            }
    }

    public void initView_RecyclerView() {

        //设置线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerAdapter = new OrderRecyclerAdapter(mItemInfoList,getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        //嵌套滑动 可以让其由于惯性滑动
        mRecyclerView.setNestedScrollingEnabled(false);

        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }

        mRecyclerAdapter.setOnListChangedListener(new OrderRecyclerAdapter.ListChangedListener() {
            @Override
            public void onListChangedClick() {
                refresh();
                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void refresh(){
        mItemInfoList.clear();
        initData();
        mRecyclerAdapter.notifyDataSetChanged();
        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }


}
