package com.bb.offerapp.fragment.viewpaper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.activity.OfferAppMainActivity;
import com.bb.offerapp.activity.OrderList;
import com.bb.offerapp.adapter.OrderRecyclerAdapterInC;
import com.bb.offerapp.bean.OrderLists;
import com.bb.offerapp.util.Constant;
import com.bb.offerapp.util.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/5/18.
 */

public class OrderCFragment extends MyFragment {
    private View view;
    private TextView textView;
    private RecyclerView mRecyclerView;
    private OrderRecyclerAdapterInC mRecyclerAdapterInC;
    private List<OrderLists> mItemInfoList;
    public static boolean flag;//第一次做懒加载不要在做选中tab重新刷新数据了 因为会有两次请求数据。
    private String result;//服务器返回的查询结果
    private String waitGetOrderNum;//要将订单置为待取的订单编号
    private ProgressDialog queryDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SERVER_NOT_RETURN:
                    queryDialog.dismiss();
                    Toast.makeText(getActivity(), "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    flag=true;
                    break;
                case Constant.QUERY_LIST_SUCCESS:
                    queryDialog.dismiss();
                    Toast.makeText(getActivity(), "飞送单 查询成功", Toast.LENGTH_SHORT).show();
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
                    flag=true;
                    break;
                case Constant.QUERY_LIST_NULL:
                    queryDialog.dismiss();
                    Toast.makeText(getActivity(), "飞送单 暂无订单", Toast.LENGTH_SHORT).show();
                    flag=true;
                    break;

                case Constant.SERVER_NOT_RETURN_FOR_UD:
                    mRecyclerAdapterInC.sendingDialog.dismiss();
                    Toast.makeText(getActivity(), "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.UPDATE_ORDER_SUCCESS:
                    mRecyclerAdapterInC.sendingDialog.dismiss();
                    refresh();
                    Toast.makeText(getActivity(), "成功送达 请联系收件人取件", Toast.LENGTH_SHORT).show();
                    sendNotifyToUser();
                    break;
                case Constant.UPDATE_ORDER_FAIL:
                    mRecyclerAdapterInC.sendingDialog.dismiss();
                    Toast.makeText(getActivity(), "送达处理失败 请检查服务器", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_c_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.c_fragment_recycle);
        mItemInfoList = new ArrayList<>();
        textView = (TextView) view.findViewById(R.id.if_no_order_c);
        return view;
    }

    private void initData() {
        if (!checkNetwork()) {
            Toast toast = Toast.makeText(getActivity(), "网络未连接哦", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        queryDialog = new ProgressDialog(getActivity());
        queryDialog.setTitle("正在载入");
        queryDialog.setMessage("从数据库读取飞送单 请稍后");
        queryDialog.setCancelable(false);
        queryDialog.show();
        new Thread(new queryOrderThread()).start();

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
            public void onListChangedClick(String getOrderNum) {
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(getActivity(), "网络未连接哦", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                waitGetOrderNum=getOrderNum;
                new Thread(new updateOrderThread()).start();
            }

        });
    }


    private void sendNotifyToUser(){
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


    public void refresh(){
        mItemInfoList.clear();
        initData();
        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerAdapterInC.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData() {
        Toast.makeText(getActivity(), "3可见 正在懒加载数据", Toast.LENGTH_SHORT).show();
        initData();
    }




    private class queryOrderThread implements Runnable {
        @Override
        public void run() {
            String state = "%E9%85%8D%E9%80%81";//表示浏览器编码--"配送"
            String queryOrderPath = "http://" + Constant.MY_SERVER_IP + "/offerapp/QueryOrder" + "?account=" + OrderList.login_name.toUpperCase()+"&workerName=" + OrderList.login_name+"&state="+state;
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
                object.put("waitGetOrderNum", waitGetOrderNum);
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


    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

}
