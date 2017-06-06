package com.bb.offerapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
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
import com.bb.offerapp.bean.QuestionBean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * MyRecyclerAdapter
 */
public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.MyViewHolder> {

    private List<QuestionBean> mItemInfoList;
    private Context context;

    //构造函数
    public QuestionRecyclerAdapter(List<QuestionBean> itemInfoList, Context context) {
        this.mItemInfoList = itemInfoList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.list_item_question_card, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.question_content.getVisibility() == View.GONE){
                    viewHolder.question_content.setVisibility(View.VISIBLE);
                }else if(viewHolder.question_content.getVisibility() == View.VISIBLE){
                    viewHolder.question_content.setVisibility(View.GONE);
                }
            }
        });
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        QuestionBean question = mItemInfoList.get(position);
        viewHolder.question_tittle.setText(question.getTittle());
        viewHolder.question_content.setText(question.getContent());
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
        TextView question_tittle,question_content;
        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            question_tittle = (TextView) itemView.findViewById(R.id.question_tittle);
            question_content = (TextView) itemView.findViewById(R.id.question_content);
        }
    }

}
