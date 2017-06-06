package com.bb.offerapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.MsgAdapter;
import com.bb.offerapp.bean.Msg;
import com.bb.offerapp.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Chat extends BaseActivity {

    private List<Msg> msgList = new ArrayList<>();

    private EditText inputText;

    private Button send;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setFinishOnTouchOutside(false);

        initMsgs(); // 初始化消息数据
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容

                    Msg msg2 = new Msg("我收到了，正在为您解答!", Msg.TYPE_RECEIVED);
                    msgList.add(msg2);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                }
            }
        });
    }

    private void initMsgs() {
        Msg msg1 = new Msg("你好，二号客服为您服务。", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("请问有什么可以帮您？", Msg.TYPE_SENT);
        msgList.add(msg2);
    }

}
