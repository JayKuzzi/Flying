package com.bb.offerapp.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.HallRecyclerAdapter;
import com.bb.offerapp.adapter.MyViewPaperAdapterInOrder;
import com.bb.offerapp.adapter.OrderRecyclerAdapter;
import com.bb.offerapp.bean.OrderLists;
import com.bb.offerapp.fragment.viewpaper.OrderAFragment;
import com.bb.offerapp.util.BaseActivity;
import com.bb.offerapp.util.Constant;
import com.bb.offerapp.util.WebService;
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

public class OrderHall extends BaseActivity {

    TextView no_order;
    private RecyclerView mRecyclerView;
    private HallRecyclerAdapter hallRecyclerAdapter;
    private List<OrderLists> mItemInfoList;

    private ProgressDialog queryDialog;
    private ProgressDialog updateDialog; //抢单等待窗口
    private String result;//服务器返回的查询结果

    private String workerInfo;//要抢单的派送人员信息
    private String updateOrderNum;//要抢单的单号

    public static String login_name;//接收当前登录的用户名，在抢单中作为抢单配送员姓名
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SERVER_NOT_RETURN:
                    queryDialog.dismiss();
                    Toast.makeText(OrderHall.this, "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.QUERY_LIST_SUCCESS:
                    queryDialog.dismiss();
                    Toast.makeText(OrderHall.this, "查询成功", Toast.LENGTH_SHORT).show();
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
                    init();
                    break;
                case Constant.QUERY_LIST_NULL:
                    queryDialog.dismiss();
                    Toast.makeText(OrderHall.this, "服务器中暂无订单", Toast.LENGTH_SHORT).show();
                    break;

                case Constant.SERVER_NOT_RETURN_FOR_UD:
                    updateDialog.dismiss();
                    Toast.makeText(OrderHall.this, "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.UPDATE_ORDER_SUCCESS:
                    updateDialog.dismiss();
                    refresh();
                    Toast.makeText(OrderHall.this, "抢单成功", Toast.LENGTH_SHORT).show();
                    sendNotifyToUser();
                    break;
                case Constant.UPDATE_ORDER_FAIL:
                    updateDialog.dismiss();
                    Toast.makeText(OrderHall.this, "抢单失败 请检查服务器", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhall);
        login_name=getIntent().getStringExtra("login_name");
        no_order = (TextView) findViewById(R.id.if_no_order_hall);
        mItemInfoList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.hall_recycle);
        initData();
    }



    private void initData() {
        if (!checkNetwork()) {
            Toast toast = Toast.makeText(OrderHall.this, "网络未连接 从数据库获取信息失败", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        queryDialog = new ProgressDialog(OrderHall.this);
        queryDialog.setTitle("正在载入");
        queryDialog.setMessage("正在从数据库读取数据 请稍后");
        queryDialog.setCancelable(false);
        queryDialog.show();
        new Thread(new queryOrderThread()).start();
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


    public void refresh() {
        mItemInfoList.clear();
        initData();
        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            no_order.setVisibility(View.VISIBLE);
        } else {
            hallRecyclerAdapter.notifyDataSetChanged();
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
                    if (!checkNetwork()) {
                        Toast toast = Toast.makeText(OrderHall.this, "网络未连接哦", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                    Bundle bundle = data.getExtras();
                    String worker_name = bundle.getString("worker_info_name");
                    String worker_phone = bundle.getString("worker_info_phone");
                    workerInfo = worker_name + "," + worker_phone;
                    updateOrderNum =hallRecyclerAdapter.getUpdateOrderNum();
                    updateDialog = new ProgressDialog(OrderHall.this);
                    updateDialog.setTitle("正在抢单");
                    updateDialog.setMessage("正在写入数据库 请稍后");
                    updateDialog.setCancelable(false);
                    updateDialog.show();
                    new Thread(new updateOrderThread()).start();
                    break;
            }
        }
    }


    public void sendNotifyToUser(){
        Intent it = new Intent(this, OfferAppMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, it, 0);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("飞送")
                .setContentText("您的订单已被接收")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.run_app_icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();
        manager.notify(100, notification);
    }
    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    public class queryOrderThread implements Runnable {
        @Override
        public void run() {
            String queryOrderPath = "http://" + Constant.MY_SERVER_IP + "/offerapp/QueryAllWaitOrders";
            Message message = new Message();
            result = WebService.executeHttpGet(queryOrderPath);
            if(result==null||result.equals("")){
                message.what = Constant.SERVER_NOT_RETURN;
            }
            else if (result.trim().equals("0")) {
                message.what = Constant.QUERY_LIST_NULL;
            } else {
                message.what = Constant.QUERY_LIST_SUCCESS;
            }
            handler.sendMessage(message);
        }
    }

    private class updateOrderThread implements Runnable {
        @Override
        public void run() {
            Message message = handler.obtainMessage();
            JSONObject object=new JSONObject();
            try {
                object.put("orderNum", updateOrderNum);
                object.put("workerInfo", workerInfo);
                String result= WebService.executeHttpPost(object,"UpdateOrder");
                if(result.equals("")||result==null){
                    message.what=Constant.SERVER_NOT_RETURN_FOR_UD;
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

