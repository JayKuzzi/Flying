package com.bb.offerapp.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.MyViewPaperAdapterInOrder;
import com.bb.offerapp.adapter.OrderRecyclerAdapterInB;
import com.bb.offerapp.adapter.SearchRecyclerAdapter;
import com.bb.offerapp.bean.OrderLists;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.view.MyTextView;
import com.bb.offerapp.view.MyTittleLayout;
import com.bb.offerapp.view.WrapContentHeightViewPager;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/5/18.
 */

public class OrderList extends BaseActivity {
    private TabLayout tabLayout;
    private WrapContentHeightViewPager viewPager;
    private MyViewPaperAdapterInOrder myViewPaperAdapter;
    MyTittleLayout myTittleLayout;
    ScrollView scrollView;
    String selected_state;
    RecyclerView recyclerView;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private List<OrderLists> mItemInfoList;


    private String[] tabTittle = {"待接单","已抢单","飞送中","待取货","已完成"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        myTittleLayout= (MyTittleLayout) findViewById(R.id.my_tittle_layout);
        scrollView= (ScrollView) findViewById(R.id.scroll_layout);
        recyclerView= (RecyclerView) findViewById(R.id.recycle_search);
        mItemInfoList = new ArrayList<>();

        initData("sendinfo_name like ?","汪");
        initView_RecyclerView();

        initView_TabLayout();
        initView_ViewPager();


        myTittleLayout.getBt1().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myTittleLayout.getLinearLayout().getVisibility() == View.VISIBLE){
                    myTittleLayout.getBt2().setVisibility(View.VISIBLE);
                    myTittleLayout.getTextView().setVisibility(View.VISIBLE);
                    myTittleLayout.getLinearLayout().setVisibility(View.GONE);
                    myTittleLayout.getBt3().setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                }else{
                    finish();
                }
            }
        });
        myTittleLayout.getBt3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_state.equals("寄方")){
                    if(myTittleLayout.getEditText().getText().toString().equals("")){
                        Toast.makeText(OrderList.this, "请输入寄方姓名", Toast.LENGTH_SHORT).show();
                    }else{
                        refreshing("sendinfo_name like ?","%"+myTittleLayout.getEditText().getText().toString()+"%");
                        if(mItemInfoList.isEmpty()){
                        Toast.makeText(OrderList.this, "数据库中没有您要的订单", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(OrderList.this, "有您要的订单", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if(selected_state.equals("收方")){
                    if(myTittleLayout.getEditText().getText().toString().equals("")){
                        Toast.makeText(OrderList.this, "请输入收方姓名", Toast.LENGTH_SHORT).show();
                    }else{
                        refreshing("receiverinfo_name like ?","%"+myTittleLayout.getEditText().getText().toString()+"%");
                        if(mItemInfoList.isEmpty()){
                            Toast.makeText(OrderList.this, "数据库中没有您要的订单", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(OrderList.this, "有您要的订单", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if(selected_state.equals("日期")){
                    if(myTittleLayout.getEditText().getText().toString().equals("")){
                        Toast.makeText(OrderList.this, "请输入日期", Toast.LENGTH_SHORT).show();
                    }else{
                        refreshing("date like ?","%"+myTittleLayout.getEditText().getText().toString()+"%");
                        if(mItemInfoList.isEmpty()){
                            Toast.makeText(OrderList.this, "数据库中没有您要的订单", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(OrderList.this, "有您要的订单", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if(selected_state.equals("状态")){
                    if(myTittleLayout.getEditText().getText().toString().equals("")){
                        Toast.makeText(OrderList.this, "请输入状态", Toast.LENGTH_SHORT).show();
                    }else{
                        refreshing("state = ?",myTittleLayout.getEditText().getText().toString());
                        if(mItemInfoList.isEmpty()){
                            Toast.makeText(OrderList.this, "数据库中没有您要的订单", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(OrderList.this, "有您要的订单", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if(selected_state.equals("单号")){
                    if(myTittleLayout.getEditText().getText().toString().equals("")){
                        Toast.makeText(OrderList.this, "请输入单号", Toast.LENGTH_SHORT).show();
                    }else{
                        refreshing("ordernum like ?","%"+myTittleLayout.getEditText().getText().toString()+"%");
                        if(mItemInfoList.isEmpty()){
                            Toast.makeText(OrderList.this, "数据库中没有您要的订单", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(OrderList.this, "有您要的订单", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                recyclerView.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
            }
        });

        myTittleLayout.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.type);
                if(languages[pos].equals("单号")){
                    myTittleLayout.getEditText().setHint("请输入单号关键字");
                    selected_state="单号";
                }else if(languages[pos].equals("寄方")){
                    myTittleLayout.getEditText().setHint("请输入寄方姓名");
                    selected_state="寄方";
                }else if(languages[pos].equals("收方")){
                    myTittleLayout.getEditText().setHint("请输入收方姓名");
                    selected_state="收方";
                }else if(languages[pos].equals("日期")){
                    myTittleLayout.getEditText().setHint("如：2017-01-01");
                    selected_state="日期";
                }else if(languages[pos].equals("状态")){
                    myTittleLayout.getEditText().setHint("请输入订单状态");
                    selected_state="状态";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initView_ViewPager() {
        viewPager = (WrapContentHeightViewPager) findViewById(R.id.orderlist_viewpaper);
        viewPager.setAdapter(myViewPaperAdapter = new MyViewPaperAdapterInOrder(getSupportFragmentManager(), tabTittle));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
                switch (position){
                    case 0:
                        myViewPaperAdapter.getOrderAFragment().refresh();
                        break;
                    case 1:
                        myViewPaperAdapter.getOrderBFragment().refresh();
                        break;
                    case 2:
                        myViewPaperAdapter.getOrderCFragment().refresh();
                        break;
                    case 3:
                        myViewPaperAdapter.getOrderDFragment().refresh();
                        break;
                    case 4:
                        myViewPaperAdapter.getOrderEFragment().refresh();
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView_TabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.orderlist_tablayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，MODE_FIXED是固定的,不能超出屏幕，MODE_SCROLLABLE可超出屏幕范围滚动的
        tabLayout.addTab(tabLayout.newTab().setText("待接单"));
        tabLayout.addTab(tabLayout.newTab().setText("已抢单"));
        tabLayout.addTab(tabLayout.newTab().setText("飞送中"));
        tabLayout.addTab(tabLayout.newTab().setText("待取货"));
        tabLayout.addTab(tabLayout.newTab().setText("已完成"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //关联viewPager
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(myTittleLayout.getLinearLayout().getVisibility() == View.VISIBLE){
                myTittleLayout.getBt2().setVisibility(View.VISIBLE);
                myTittleLayout.getTextView().setVisibility(View.VISIBLE);
                myTittleLayout.getLinearLayout().setVisibility(View.GONE);
                myTittleLayout.getBt3().setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
            }else{
                finish();
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }



    private void initData(String condition1,String condition2) {
        List<OrderLists> orderlist = DataSupport.where(condition1, condition2).order("id desc").find(OrderLists.class);
        for (OrderLists item : orderlist) {
            OrderLists Order0 = new OrderLists();
            Order0.setOrderNum(item.getOrderNum());
            Order0.setState(item.getState());
            Order0.setSendInfo(item.getSendInfo());
            Order0.setSendInfo_name(item.getSendInfo_name());
            Order0.setSendInfo_phone(item.getSendInfo_phone());
            Order0.setReciverInfo(item.getReciverInfo());
            Order0.setReceiverInfo_name(item.getReceiverInfo_name());
            Order0.setReceiverInfo_phone(item.getReceiverInfo_phone());
            Order0.setDate(item.getDate());
            Order0.setGo_home_time(item.getGo_home_time());
            Order0.setWeight(item.getWeight());
            Order0.setDistance(item.getDistance());
            Order0.setPalWay(item.getPalWay());
            Order0.setGoodsInfo(item.getGoodsInfo());
            Order0.setWorkerInfo(item.getWorkerInfo());
            Order0.setMessage(item.getMessage());
            Order0.setOrderPrice(item.getOrderPrice());
            mItemInfoList.add(Order0);
        }
    }


    private void initView_RecyclerView() {
        //设置线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        searchRecyclerAdapter = new SearchRecyclerAdapter(mItemInfoList, this);
        recyclerView.setAdapter(searchRecyclerAdapter);
        //嵌套滑动 可以让其由于惯性滑动
        recyclerView.setNestedScrollingEnabled(false);

        searchRecyclerAdapter.setOnItemClickListener(new SearchRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent =new Intent(OrderList.this, OrderDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("send_info_name", mItemInfoList.get(position).getSendInfo_name());
                bundle.putString("send_info_phone", mItemInfoList.get(position).getSendInfo_phone());
                bundle.putString("send_info_address", mItemInfoList.get(position).getSendInfo());
                bundle.putString("receive_info_name", mItemInfoList.get(position).getReceiverInfo_name());
                bundle.putString("receive_info_phone", mItemInfoList.get(position).getReceiverInfo_phone());
                bundle.putString("receive_info_address", mItemInfoList.get(position).getReceiverInfo());
                bundle.putString("goods_info_name", mItemInfoList.get(position).getGoodsInfo());
                bundle.putString("goods_info_message", mItemInfoList.get(position).getMessage());
                bundle.putString("time", mItemInfoList.get(position).getDate());
                bundle.putString("pay_way", mItemInfoList.get(position).getPalWay());
                bundle.putString("money",mItemInfoList.get(position).getOrderPrice());
                bundle.putString("distance",mItemInfoList.get(position).getDistance());
                bundle.putString("goods_info_weight", mItemInfoList.get(position).getWeight());
                bundle.putString("num",mItemInfoList.get(position).getOrderNum());
                bundle.putString("state",mItemInfoList.get(position).getState());
                bundle.putString("worker_info",mItemInfoList.get(position).getWorkerInfo());
                bundle.putString("go_home_time",mItemInfoList.get(position).getGo_home_time());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    public void refreshing(String condition1,String condition2){
        mItemInfoList.clear();
        initData(condition1,condition2);
        searchRecyclerAdapter.notifyDataSetChanged();
    }
}
