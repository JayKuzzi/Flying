package com.bb.offerapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bb.offerapp.R;
import com.bb.offerapp.activity.OrderDetail;
import com.bb.offerapp.activity.OrderHall;
import com.bb.offerapp.activity.WorkerInfo;
import com.bb.offerapp.bean.OrderLists;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * MyRecyclerAdapter
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int DAI_JIE = 0;
    public static final int YI_QIANG = 1;
    public static final int PEI_SONG = 2;
    public static final int DAI_QU = 3;
    public static final int WAN_CHENG = 4;
    int position;//点击的位置，方便拿去做数据库修改
    private OnItemClickListener mOnItemClickListener = null;
    private List<OrderLists> mItemInfoList;
    private Context context;
    //构造函数
    public SearchRecyclerAdapter(List<OrderLists> itemInfoList, Context context) {
        this.mItemInfoList = itemInfoList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemInfoList.get(position).getState().equals("待抢")) {
            return DAI_JIE;
        } else if (mItemInfoList.get(position).getState().equals("已抢")) {
            return YI_QIANG;
        } else if (mItemInfoList.get(position).getState().equals("配送")) {
            return PEI_SONG;
        } else if (mItemInfoList.get(position).getState().equals("待取")) {
            return DAI_QU;
        } else if (mItemInfoList.get(position).getState().equals("完成")) {
            return WAN_CHENG;
        }
        return 5;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == DAI_JIE) {
            itemView = inflater.inflate(R.layout.list_item_a_card, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取position
                        mOnItemClickListener.onItemClick(v, (int) v.getTag());
                    }
                }
            });
            return new MyViewHolderA(itemView);
        } else if (viewType == YI_QIANG) {
            itemView = inflater.inflate(R.layout.list_item_b_card, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取position
                        mOnItemClickListener.onItemClick(v, (int) v.getTag());
                    }
                }
            });
            return new MyViewHolderB(itemView);
        } else if (viewType == PEI_SONG) {
            itemView = inflater.inflate(R.layout.list_item_c_card, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取position
                        mOnItemClickListener.onItemClick(v, (int) v.getTag());
                    }
                }
            });
            return new MyViewHolderC(itemView);
        } else if (viewType == DAI_QU) {
            itemView = inflater.inflate(R.layout.list_item_d_card, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取position
                        mOnItemClickListener.onItemClick(v, (int) v.getTag());
                    }
                }
            });
            return new MyViewHolderD(itemView);
        } else if (viewType == WAN_CHENG) {
            itemView = inflater.inflate(R.layout.list_item_e_card, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取position
                        mOnItemClickListener.onItemClick(v, (int) v.getTag());
                    }
                }
            });
            return new MyViewHolderE(itemView);
        }


        return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof MyViewHolderA) {
            OrderLists itemOrder = mItemInfoList.get(position);
            ((MyViewHolderA) viewHolder).item_order_orderNum.setText(itemOrder.getOrderNum());
            ((MyViewHolderA) viewHolder).item_order_state.setText(itemOrder.getState());
            ((MyViewHolderA) viewHolder).item_order_sendInfo.setText(itemOrder.getSendInfo());
            ((MyViewHolderA) viewHolder).item_order_receiveInfo.setText(itemOrder.getReciverInfo());
            ((MyViewHolderA) viewHolder).item_order_date.setText(itemOrder.getDate());
            ((MyViewHolderA) viewHolder).item_order_get_item.setVisibility(View.GONE);
        } else if (viewHolder instanceof MyViewHolderB) {
            OrderLists itemOrder = mItemInfoList.get(position);
            ((MyViewHolderB) viewHolder).item_order_orderNum.setText(itemOrder.getOrderNum());
            ((MyViewHolderB) viewHolder).item_order_state.setText(itemOrder.getState());
            ((MyViewHolderB) viewHolder).item_order_sendInfo.setText(itemOrder.getSendInfo());
            ((MyViewHolderB) viewHolder).item_order_receiveInfo.setText(itemOrder.getReciverInfo());
            ((MyViewHolderB) viewHolder).item_order_date.setText(itemOrder.getDate());
            ((MyViewHolderB) viewHolder).item_order_get_item.setVisibility(View.GONE);

        } else if (viewHolder instanceof MyViewHolderC) {
            OrderLists itemOrder = mItemInfoList.get(position);
            ((MyViewHolderC) viewHolder).item_order_orderNum.setText(itemOrder.getOrderNum());
            ((MyViewHolderC) viewHolder).item_order_state.setText(itemOrder.getState());
            ((MyViewHolderC) viewHolder).item_order_sendInfo.setText(itemOrder.getSendInfo());
            ((MyViewHolderC) viewHolder).item_order_receiveInfo.setText(itemOrder.getReciverInfo());
            ((MyViewHolderC) viewHolder).item_order_date.setText(itemOrder.getDate());
            ((MyViewHolderC) viewHolder).item_order_get_item.setVisibility(View.GONE);

        } else if (viewHolder instanceof MyViewHolderD) {
            OrderLists itemOrder = mItemInfoList.get(position);
            ((MyViewHolderD) viewHolder).item_order_orderNum.setText(itemOrder.getOrderNum());
            ((MyViewHolderD) viewHolder).item_order_state.setText(itemOrder.getState());
            ((MyViewHolderD) viewHolder).item_order_sendInfo.setText(itemOrder.getSendInfo());
            ((MyViewHolderD) viewHolder).item_order_receiveInfo.setText(itemOrder.getReciverInfo());
            ((MyViewHolderD) viewHolder).item_order_date.setText(itemOrder.getDate());
            ((MyViewHolderD) viewHolder).item_order_get_item.setVisibility(View.GONE);

        } else if (viewHolder instanceof MyViewHolderE) {
            OrderLists itemOrder = mItemInfoList.get(position);
            ((MyViewHolderE) viewHolder).item_order_orderNum.setText(itemOrder.getOrderNum());
            ((MyViewHolderE) viewHolder).item_order_state.setText(itemOrder.getState());
            ((MyViewHolderE) viewHolder).item_order_sendInfo.setText(itemOrder.getSendInfo());
            ((MyViewHolderE) viewHolder).item_order_receiveInfo.setText(itemOrder.getReciverInfo());
            ((MyViewHolderE) viewHolder).item_order_date.setText(itemOrder.getDate());
        }

        viewHolder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        if (mItemInfoList == null) {
            return 0;
        }
        return mItemInfoList.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolderA extends RecyclerView.ViewHolder {
        View item;//最外层子view

        TextView item_order_orderNum, item_order_state, item_order_sendInfo, item_order_receiveInfo,
                item_order_date, item_order_get_item;

        public MyViewHolderA(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            item_order_orderNum = (TextView) itemView.findViewById(R.id.item_order_orderNum);
            item_order_state = (TextView) itemView.findViewById(R.id.item_order_state);
            item_order_sendInfo = (TextView) itemView.findViewById(R.id.item_order_sendInfo);
            item_order_receiveInfo = (TextView) itemView.findViewById(R.id.item_order_receiveInfo);
            item_order_date = (TextView) itemView.findViewById(R.id.item_order_date);
            item_order_get_item = (TextView) itemView.findViewById(R.id.item_order_get_item_a);
        }
    }

    class MyViewHolderB extends RecyclerView.ViewHolder {
        View item;//最外层子view

        TextView item_order_orderNum, item_order_state, item_order_sendInfo, item_order_receiveInfo,
                item_order_date, item_order_get_item;

        public MyViewHolderB(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            item_order_orderNum = (TextView) itemView.findViewById(R.id.item_order_orderNum_b);
            item_order_state = (TextView) itemView.findViewById(R.id.item_order_state_b);
            item_order_sendInfo = (TextView) itemView.findViewById(R.id.item_order_sendInfo_b);
            item_order_receiveInfo = (TextView) itemView.findViewById(R.id.item_order_receiveInfo_b);
            item_order_date = (TextView) itemView.findViewById(R.id.item_order_date_b);
            item_order_get_item = (TextView) itemView.findViewById(R.id.item_order_get_item_b);
        }
    }

    class MyViewHolderC extends RecyclerView.ViewHolder {
        View item;//最外层子view

        TextView item_order_orderNum, item_order_state, item_order_sendInfo, item_order_receiveInfo,
                item_order_date, item_order_get_item;

        public MyViewHolderC(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            item_order_orderNum = (TextView) itemView.findViewById(R.id.item_order_orderNum_c);
            item_order_state = (TextView) itemView.findViewById(R.id.item_order_state_c);
            item_order_sendInfo = (TextView) itemView.findViewById(R.id.item_order_sendInfo_c);
            item_order_receiveInfo = (TextView) itemView.findViewById(R.id.item_order_receiveInfo_c);
            item_order_date = (TextView) itemView.findViewById(R.id.item_order_date_c);
            item_order_get_item = (TextView) itemView.findViewById(R.id.item_order_get_item_c);
        }
    }

    class MyViewHolderD extends RecyclerView.ViewHolder {
        View item;//最外层子view

        TextView item_order_orderNum, item_order_state, item_order_sendInfo, item_order_receiveInfo,
                item_order_date, item_order_get_item;

        public MyViewHolderD(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            item_order_orderNum = (TextView) itemView.findViewById(R.id.item_order_orderNum_d);
            item_order_state = (TextView) itemView.findViewById(R.id.item_order_state_d);
            item_order_sendInfo = (TextView) itemView.findViewById(R.id.item_order_sendInfo_d);
            item_order_receiveInfo = (TextView) itemView.findViewById(R.id.item_order_receiveInfo_d);
            item_order_date = (TextView) itemView.findViewById(R.id.item_order_date_d);
            item_order_get_item = (TextView) itemView.findViewById(R.id.item_order_get_item_d);
        }
    }

    class MyViewHolderE extends RecyclerView.ViewHolder {
        View item;//最外层子view

        TextView item_order_orderNum, item_order_state, item_order_sendInfo, item_order_receiveInfo,
                item_order_date;

        public MyViewHolderE(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            item_order_orderNum = (TextView) itemView.findViewById(R.id.item_order_orderNum_e);
            item_order_state = (TextView) itemView.findViewById(R.id.item_order_state_e);
            item_order_sendInfo = (TextView) itemView.findViewById(R.id.item_order_sendInfo_e);
            item_order_receiveInfo = (TextView) itemView.findViewById(R.id.item_order_receiveInfo_e);
            item_order_date = (TextView) itemView.findViewById(R.id.item_order_date_e);
        }
    }


}
