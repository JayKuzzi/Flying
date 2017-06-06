package com.bb.offerapp.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bb.offerapp.R;
import com.bb.offerapp.activity.OrderDetail;
import com.bb.offerapp.bean.OrderLists;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * MyRecyclerAdapter
 */
public class OrderRecyclerAdapterInB extends RecyclerView.Adapter<OrderRecyclerAdapterInB.MyViewHolder> {
    private List<OrderLists> mItemInfoList;
    private Context context;
    int position;

    public int getPosition() {
        return position;
    }

    //监听，拿到BFragment中做回调
    private ListChangedListener listChangedListener;

    public interface ListChangedListener {
        public void onListChangedClick();
    }

    public void setOnListChangedListener (ListChangedListener  listChangedListener) {
        this.listChangedListener = listChangedListener;
    }

    //构造函数
    public OrderRecyclerAdapterInB(List<OrderLists> itemInfoList, Context context) {
        this.mItemInfoList = itemInfoList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        itemView = inflater.inflate(R.layout.list_item_b_card, parent, false);

        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<OrderLists> orderlist = DataSupport.where("state= ?", "已抢").order("id desc").find(OrderLists.class);
                position = viewHolder.getAdapterPosition();
                OrderLists clickItem = orderlist.get(position);
                Intent intent =new Intent(context, OrderDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("send_info_name", clickItem.getSendInfo_name());
                bundle.putString("send_info_phone", clickItem.getSendInfo_phone());
                bundle.putString("send_info_address", clickItem.getSendInfo());
                bundle.putString("receive_info_name", clickItem.getReceiverInfo_name());
                bundle.putString("receive_info_phone", clickItem.getReceiverInfo_phone());
                bundle.putString("receive_info_address", clickItem.getReceiverInfo());
                bundle.putString("goods_info_name", clickItem.getGoodsInfo());
                bundle.putString("goods_info_message", clickItem.getMessage());
                bundle.putString("time", clickItem.getDate());
                bundle.putString("pay_way", clickItem.getPalWay());
                bundle.putString("money",clickItem.getOrderPrice());
                bundle.putString("distance",clickItem.getDistance());
                bundle.putString("goods_info_weight", clickItem.getWeight());
                bundle.putString("num",clickItem.getOrderNum());
                bundle.putString("state",clickItem.getState());
                bundle.putString("worker_info",clickItem.getWorkerInfo());
                bundle.putString("go_home_time",clickItem.getGo_home_time());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        viewHolder.item_order_get_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                dialog1.setTitle("配送说明");
                dialog1.setMessage("务必保证1小时内送达，否则薪水不会到账。");
                dialog1.setCancelable(false);
                dialog1.setNegativeButton("在考虑下",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog1.setPositiveButton("我要配送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        List<OrderLists> orderlist = DataSupport.where("state= ?", "已抢").order("id desc").find(OrderLists.class);
                        OrderLists change = orderlist.get(viewHolder.getAdapterPosition());
                        OrderLists update_yiqiang = new OrderLists();
                        update_yiqiang.setState("配送");
                        update_yiqiang.updateAll("orderNum = ?", change.getOrderNum());

                        //此时回调此接口，做数据刷新操作
                        listChangedListener.onListChangedClick();
                    }
                });
                dialog1.show();


            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        OrderLists itemOrder = mItemInfoList.get(position);
        viewHolder.item_order_orderNum.setText(itemOrder.getOrderNum());
        viewHolder.item_order_state.setText(itemOrder.getState());
        viewHolder.item_order_sendInfo.setText(itemOrder.getSendInfo());
        viewHolder.item_order_receiveInfo.setText(itemOrder.getReciverInfo());
        viewHolder.item_order_date.setText(itemOrder.getDate());
    }

    @Override
    public int getItemCount() {
        if (mItemInfoList == null) {
            return 0;
        }
        return mItemInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        View item;//最外层子view

        TextView item_order_orderNum,item_order_state,item_order_sendInfo,item_order_receiveInfo,item_order_date,item_order_get_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            item_order_orderNum= (TextView) itemView.findViewById(R.id.item_order_orderNum_b);
            item_order_state= (TextView) itemView.findViewById(R.id.item_order_state_b);
            item_order_sendInfo= (TextView) itemView.findViewById(R.id.item_order_sendInfo_b);
            item_order_receiveInfo= (TextView) itemView.findViewById(R.id.item_order_receiveInfo_b);
            item_order_date= (TextView) itemView.findViewById(R.id.item_order_date_b);
            item_order_get_item = (TextView) itemView.findViewById(R.id.item_order_get_item_b);

        }
    }

}
