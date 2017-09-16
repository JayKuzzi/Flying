package com.bb.offerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.MyViewPaperAdapterInOrder;
import com.bb.offerapp.adapter.SearchRecyclerAdapter;
import com.bb.offerapp.bean.OrderLists;
import com.bb.offerapp.fragment.viewpaper.OrderAFragment;
import com.bb.offerapp.fragment.viewpaper.OrderBFragment;
import com.bb.offerapp.fragment.viewpaper.OrderCFragment;
import com.bb.offerapp.fragment.viewpaper.OrderDFragment;
import com.bb.offerapp.fragment.viewpaper.OrderEFragment;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.Constant;
import com.bb.offerapp.util.WebService;
import com.bb.offerapp.view.MyTittleLayout;
import com.bb.offerapp.view.WrapContentHeightViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/5/18.
 */

public class OrderList extends BaseActivity {
    public static String login_name;//接收当前登录的用户名，在Fragment中做查询订单使用
    public MyViewPaperAdapterInOrder myViewPaperAdapter;
    MyTittleLayout myTittleLayout;
    ScrollView scrollView;
    String selected_state;
    RecyclerView recyclerView;
    private TabLayout tabLayout;
    private WrapContentHeightViewPager viewPager;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private List<OrderLists> mItemInfoList;
    private String[] tabTittle = {"待接单", "已抢单", "飞送中", "待取货", "已完成"};

    private String sendinfo_name,receiverinfo_name,date,state,ordernum;//要查询的5个变量
    private ProgressDialog queryDialog;
    private String result;//查询的结果
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SERVER_NOT_RETURN:
                    queryDialog.dismiss();
                    Toast.makeText(OrderList.this, "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.QUERY_LIST_SUCCESS:
                    queryDialog.dismiss();
                    Toast.makeText(OrderList.this, "搜索成功 有您要的订单", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    JSONArray myJsonArray = null;
                    try {
                        myJsonArray = new JSONArray(result);
                        for (int i = 0; i < myJsonArray.length(); i++) {
                            JSONObject object = myJsonArray.getJSONObject(i);
                            OrderLists Order0 = new OrderLists();
                            Order0.setOrderNum(object.getString("orderNum"));
                            Order0.setDate(object.getString("date"));
                            Order0.setGo_home_time(object.getString("go_home_time"));
                            Order0.setDistance(object.getString("distance"));
                            Order0.setPalWay(object.getString("palWay"));
                            Order0.setOrderPrice(object.getString("orderPrice"));
                            Order0.setState(object.getString("state"));
                            Order0.setWeight(object.getString("weight"));
                            Order0.setMessage(object.getString("message"));
                            Order0.setWorkerInfo(object.getString("workerInfo"));
                            Order0.setGoodsInfo(object.getString("goodsInfo"));
                            Order0.setSendInfo_name(object.getString("sendInfo_name"));
                            Order0.setSendInfo_phone(object.getString("sendInfo_phone"));
                            Order0.setSendInfo(object.getString("sendInfo"));
                            Order0.setReceiverInfo_name(object.getString("receiverInfo_name"));
                            Order0.setReceiverInfo_phone(object.getString("receiverInfo_phone"));
                            Order0.setReciverInfo(object.getString("receiverInfo"));
                            mItemInfoList.add(Order0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    initView_RecyclerView();
                    if(!mItemInfoList.isEmpty()){
                        searchRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case Constant.QUERY_LIST_NULL:
                    queryDialog.dismiss();
                    Toast.makeText(OrderList.this, "搜索成功 暂无您要订单", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        myTittleLayout = (MyTittleLayout) findViewById(R.id.my_tittle_layout);
        scrollView = (ScrollView) findViewById(R.id.scroll_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_search);
        mItemInfoList = new ArrayList<>();
        login_name = getIntent().getStringExtra("login_name");
        initView_TabLayout();
        initView_ViewPager();


        myTittleLayout.getBt1().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myTittleLayout.getLinearLayout().getVisibility() == View.VISIBLE) {
                    myTittleLayout.getBt2().setVisibility(View.VISIBLE);
                    myTittleLayout.getTextView().setVisibility(View.VISIBLE);
                    myTittleLayout.getLinearLayout().setVisibility(View.GONE);
                    myTittleLayout.getBt3().setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
            }
        });
        myTittleLayout.getBt3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(OrderList.this, "网络未连接哦", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if (selected_state.equals("寄方")) {
                    if (myTittleLayout.getEditText().getText().toString().equals("")) {
                        Toast.makeText(OrderList.this, "请输入寄方姓名", Toast.LENGTH_SHORT).show();
                    } else {
                        sendinfo_name=myTittleLayout.getEditText().getText().toString();
                        receiverinfo_name=null;
                        date=null;
                        state=null;
                        ordernum=null;
                        mItemInfoList.clear();
                        queryDialog = new ProgressDialog(OrderList.this);
                        queryDialog.setTitle("正在载入");
                        queryDialog.setMessage("正在根据寄方姓名关键字从数据库读取 请稍后");
                        queryDialog.setCancelable(false);
                        queryDialog.show();
                        new Thread(new queryOrderThread()).start();
                    }
                }
                if (selected_state.equals("收方")) {
                    if (myTittleLayout.getEditText().getText().toString().equals("")) {
                        Toast.makeText(OrderList.this, "请输入收方姓名", Toast.LENGTH_SHORT).show();
                    } else {
                        sendinfo_name=null;
                        receiverinfo_name=myTittleLayout.getEditText().getText().toString();
                        date=null;
                        state=null;
                        ordernum=null;
                        mItemInfoList.clear();

                        queryDialog = new ProgressDialog(OrderList.this);
                        queryDialog.setTitle("正在载入");
                        queryDialog.setMessage("正在根据寄方姓名关键字从数据库读取 请稍后");
                        queryDialog.setCancelable(false);
                        queryDialog.show();
                        new Thread(new queryOrderThread()).start();
                    }
                }
                if (selected_state.equals("日期")) {
                    if (myTittleLayout.getEditText().getText().toString().equals("")) {
                        Toast.makeText(OrderList.this, "请输入日期", Toast.LENGTH_SHORT).show();
                    } else {
                        sendinfo_name=null;
                        receiverinfo_name=null;
                        date=myTittleLayout.getEditText().getText().toString();
                        state=null;
                        ordernum=null;
                        mItemInfoList.clear();

                        queryDialog = new ProgressDialog(OrderList.this);
                        queryDialog.setTitle("正在载入");
                        queryDialog.setMessage("正在根据寄方姓名关键字从数据库读取 请稍后");
                        queryDialog.setCancelable(false);
                        queryDialog.show();
                        new Thread(new queryOrderThread()).start();
                    }
                }
                if (selected_state.equals("状态")) {
                    if (myTittleLayout.getEditText().getText().toString().equals("")) {
                        Toast.makeText(OrderList.this, "请输入状态", Toast.LENGTH_SHORT).show();
                    } else {
                        sendinfo_name=null;
                        receiverinfo_name=null;
                        date=null;
                        state=myTittleLayout.getEditText().getText().toString();
                        ordernum=null;
                        mItemInfoList.clear();
                        queryDialog = new ProgressDialog(OrderList.this);
                        queryDialog.setTitle("正在载入");
                        queryDialog.setMessage("正在根据寄方姓名关键字从数据库读取 请稍后");
                        queryDialog.setCancelable(false);
                        queryDialog.show();
                        new Thread(new queryOrderThread()).start();
                    }
                }
                if (selected_state.equals("单号")) {
                    if (myTittleLayout.getEditText().getText().toString().equals("")) {
                        Toast.makeText(OrderList.this, "请输入单号", Toast.LENGTH_SHORT).show();
                    } else {
                        sendinfo_name=null;
                        receiverinfo_name=null;
                        date=null;
                        state=null;
                        ordernum=myTittleLayout.getEditText().getText().toString().toUpperCase();
                        mItemInfoList.clear();

                        queryDialog = new ProgressDialog(OrderList.this);
                        queryDialog.setTitle("正在载入");
                        queryDialog.setMessage("正在根据寄方姓名关键字从数据库读取 请稍后");
                        queryDialog.setCancelable(false);
                        queryDialog.show();
                        new Thread(new queryOrderThread()).start();
                    }
                }

            }
        });

        myTittleLayout.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.type);
                if (languages[pos].equals("单号")) {
                    myTittleLayout.getEditText().setHint("请输入单号关键字");
                    selected_state = "单号";
                } else if (languages[pos].equals("寄方")) {
                    myTittleLayout.getEditText().setHint("请输入寄方姓名");
                    selected_state = "寄方";
                } else if (languages[pos].equals("收方")) {
                    myTittleLayout.getEditText().setHint("请输入收方姓名");
                    selected_state = "收方";
                } else if (languages[pos].equals("日期")) {
                    myTittleLayout.getEditText().setHint("如：2017-01-01");
                    selected_state = "日期";
                } else if (languages[pos].equals("状态")) {
                    myTittleLayout.getEditText().setHint("请输入订单状态");
                    selected_state = "状态";
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
                switch (position) {
                    case 0:
                        if(OrderAFragment.flag){
                            myViewPaperAdapter.getOrderAFragment().refresh();
                        }
                        break;
                    case 1:
                        if(OrderBFragment.flag){
                            myViewPaperAdapter.getOrderBFragment().refresh();

                        }
                        break;
                    case 2:
                        if(OrderCFragment.flag) {
                            myViewPaperAdapter.getOrderCFragment().refresh();
                        }
                        break;
                    case 3:
                        if(OrderDFragment.flag) {
                            myViewPaperAdapter.getOrderDFragment().refresh();
                        }
                        break;
                    case 4:
                        if(OrderEFragment.flag) {
                            myViewPaperAdapter.getOrderEFragment().refresh();
                        }
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(4);
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
            if (myTittleLayout.getLinearLayout().getVisibility() == View.VISIBLE) {
                myTittleLayout.getBt2().setVisibility(View.VISIBLE);
                myTittleLayout.getTextView().setVisibility(View.VISIBLE);
                myTittleLayout.getLinearLayout().setVisibility(View.GONE);
                myTittleLayout.getBt3().setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
            } else {
                finish();
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
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
                Intent intent = new Intent(OrderList.this, OrderDetail.class);
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
                bundle.putString("money", mItemInfoList.get(position).getOrderPrice());
                bundle.putString("distance", mItemInfoList.get(position).getDistance());
                bundle.putString("goods_info_weight", mItemInfoList.get(position).getWeight());
                bundle.putString("num", mItemInfoList.get(position).getOrderNum());
                bundle.putString("state", mItemInfoList.get(position).getState());
                bundle.putString("worker_info", mItemInfoList.get(position).getWorkerInfo());
                bundle.putString("go_home_time", mItemInfoList.get(position).getGo_home_time());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    private class queryOrderThread implements Runnable {
        @Override
        public void run() {
            Message message = handler.obtainMessage();
            JSONObject object=new JSONObject();
            try {
                object.put("account", OrderList.login_name);
                object.put("sendinfo_name", sendinfo_name);
                object.put("receiverinfo_name", receiverinfo_name);
                object.put("state", state);
                object.put("ordernum", ordernum);
                object.put("date", date);
                result= WebService.executeHttpPost(object,"SearchOrder");
                if(result==null||result.equals("")){
                    message.what = Constant.SERVER_NOT_RETURN;
                }
                else if (result.trim().equals("0")) {
                    message.what = Constant.QUERY_LIST_NULL;
                } else {
                    message.what = Constant.QUERY_LIST_SUCCESS;
                }
                handler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
