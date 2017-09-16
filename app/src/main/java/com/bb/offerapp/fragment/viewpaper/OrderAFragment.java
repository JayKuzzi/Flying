package com.bb.offerapp.fragment.viewpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.offerapp.R;
import com.bb.offerapp.activity.OrderList;
import com.bb.offerapp.adapter.OrderRecyclerAdapter;
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

public class OrderAFragment extends MyFragment {
    private View view;
    private RecyclerView mRecyclerView;
    private OrderRecyclerAdapter mRecyclerAdapter;
    private TextView textView;
    private List<OrderLists> mItemInfoList;
    private ProgressDialog queryDialog;
    private String result;//服务器返回的查询结果
    public static boolean flag;//第一次做懒加载不要在做选中tab重新刷新数据了 因为会有两次请求数据。
    private String waitDeleteOrderNum;//要删除的订单编号
    public Handler handler = new Handler() {
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
                    Toast.makeText(getActivity(), "待抢单 查询成功", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "待抢单 暂无订单", Toast.LENGTH_SHORT).show();
                    flag=true;
                    break;
                case Constant.SERVER_NOT_RETURN_FOR_DE:
                    mRecyclerAdapter.deleteDialog.dismiss();
                    Toast.makeText(getActivity(), "服务器无返回 请检查异常", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.DELETE_ORDER_SUCCESS:
                    mRecyclerAdapter.deleteDialog.dismiss();
                    refresh();
                    Toast.makeText(getActivity(), "成功取消", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.DELETE_ORDER_FAIL:
                    mRecyclerAdapter.deleteDialog.dismiss();
                    Toast.makeText(getActivity(), "取消失败 请检查服务器", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void loadData() {
        Toast.makeText(getActivity(), "1可见 正在懒加载数据", Toast.LENGTH_SHORT).show();
        initData();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_a_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.a_fragment_recycle);
        mItemInfoList = new ArrayList<>();
        textView = (TextView) view.findViewById(R.id.if_no_order_a);
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
        queryDialog.setMessage("从数据库读取待抢单 请稍后");
        queryDialog.setCancelable(false);
        queryDialog.show();
        new Thread(new queryOrderThread()).start();
    }

    public void initView_RecyclerView() {

        //设置线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerAdapter = new OrderRecyclerAdapter(mItemInfoList, getActivity());
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
            public void onListChangedClick(String DeleteOrderNum) {
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(getActivity(), "网络未连接哦", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                waitDeleteOrderNum=DeleteOrderNum;
                new Thread(new deleteOrderThread()).start();
            }
        });
    }

    public void refresh() {
        mItemInfoList.clear();
        initData();
        if (mItemInfoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }




    private class queryOrderThread implements Runnable {
        @Override
        public void run() {
            String state = "%E5%BE%85%E6%8A%A2";//表示浏览器编码--"待抢"
            String queryOrderPath = "http://" + Constant.MY_SERVER_IP + "/offerapp/QueryOrder" + "?account=" + OrderList.login_name.toUpperCase() + "&state="+state;
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

    private class deleteOrderThread implements Runnable {
        @Override
        public void run() {
            String deleteOrderPath = "http://" + Constant.MY_SERVER_IP + "/offerapp/DeleteOrder" + "?orderNum=" + waitDeleteOrderNum;
            Message message = new Message();
            result = WebService.executeHttpGet(deleteOrderPath);
            if(result==null||result.equals("")){
                message.what = Constant.SERVER_NOT_RETURN_FOR_DE;
            }
            else {
                message.what=Integer.parseInt(result.trim());
            }
            handler.sendMessage(message);
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
