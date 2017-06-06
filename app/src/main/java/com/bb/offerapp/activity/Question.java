package com.bb.offerapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bb.offerapp.R;
import com.bb.offerapp.adapter.QuestionRecyclerAdapter;
import com.bb.offerapp.bean.QuestionBean;
import com.bb.offerapp.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class Question extends BaseActivity{
    private RecyclerView recyclerView;
    private QuestionRecyclerAdapter questionRecyclerAdapter;
    private List<QuestionBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initData();
        init();
    }

    private void initData() {
        mData = new ArrayList<>();
        QuestionBean item =new QuestionBean();
        item.setTittle("服务时间？");
        item.setContent("飞送提供24小时服务，节假日正常服务，全年无休，您有需要可以随时下单；客服人员的工作时间是早8：00至晚9：00，在此期间我们全程监控您的订单，可以确保安全快速完成。");
        mData.add(item);
        QuestionBean item2 =new QuestionBean();
        item2.setTittle("时效是怎样的，是否可以预约？");
        item2.setContent("配送时效：5公里内60分钟送达，每增加5公里增加30分钟；飞送是即时服务，下单后一般15分钟内上门取件。平台支持预约取件。");
        mData.add(item2);
        QuestionBean item3 =new QuestionBean();
        item3.setTittle("关于物品包装问题？");
        item3.setContent("飞送目前不提供物品包装设施，蛋糕、鲜花、贵重物品等，如递送物品需冷藏或特殊照顾的，建议您自行包装，便于飞送携带。");
        mData.add(item3);
        QuestionBean item4 =new QuestionBean();
        item4.setTittle("可以配送小宠物、贵重物品吗？");
        item4.setContent("飞送可以为您配送宠物，请注意：宠物、贵重物品等特殊物品，需自行做好保护措施；如宠物要放到结实的笼子里便于达达携带，贵重物品应密封包装好；这些特殊物品如因事先没有做妥善的保护措施，非飞送问题而出现物品损坏或引发不良后果，飞送将不做赔付，请您谅解。");
        mData.add(item4);
        QuestionBean item5 =new QuestionBean();
        item5.setTittle("是否支持到付？");
        item5.setContent("通过微信下单需直接使用微信支付，飞送目前不支持到付！");
        mData.add(item5);
        QuestionBean item6 =new QuestionBean();
        item6.setTittle("是否做加盟或者代理？");
        item6.setContent("为保证用户的极致体验，飞送目前不做加盟或代理，如有需要我们会及时在各平台告知大家，感谢您的继续关注和支持！");
        mData.add(item6);
        QuestionBean item7 =new QuestionBean();
        item7.setTittle("是否支持代购或垫付？");
        item7.setContent("飞送不支持大额垫付；若您通过微信下单需要小额代购或垫付，请在订单备注内说明您的需求，如果有飞送愿意为您服务，会在抢单后先跟寄件人联系，到时您可以跟飞送详细沟通代购事宜。");
        mData.add(item7);
        QuestionBean item8 =new QuestionBean();
        item8.setTittle("无法准确测量物品重量怎么办？");
        item8.setContent("您如果不方便测量，可以预估，请尽量保证重量相符，具体以达达现场确认为主；如果差距较大，您需要当场支付飞送续重费用现金；如果您拒绝支付续重费用；飞送也可能会拒绝为您服务，感谢您的理解！");
        mData.add(item8);
        QuestionBean item9 =new QuestionBean();
        item9.setTittle("订单取消及退款？");
        item9.setContent("订单取消后会进入自动退款流程，用微信钱包支付的订单会在3个工作日之内退款成功，微信支付的用户可以在微信钱包的交易管理里查看所有进出帐纪录。");
        mData.add(item9);
        QuestionBean item10 =new QuestionBean();
        item10.setTittle("连锁店或企业如需合作？");
        item10.setContent("合作咨询，请发送“城市、公司、业务、手机号”到 200826311@qq.com 会有专人在三个工作日之内与您联系，可能会有更适合您的活动和服务；希望有飞送的助力，让您的生意更红火！");
        mData.add(item10);
        QuestionBean item11 =new QuestionBean();
        item11.setTittle("新用户可享受什么优惠？");
        item11.setContent("新用户注册即送20元优惠券。");
        mData.add(item11);


    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.question_recycle);
        questionRecyclerAdapter = new QuestionRecyclerAdapter(mData, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(questionRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        //嵌套滑动 可以让其由于惯性滑动
        recyclerView.setNestedScrollingEnabled(false);
    }




}
