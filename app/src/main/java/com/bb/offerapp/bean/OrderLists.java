package com.bb.offerapp.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by bb on 2017/5/17.
 */

public class OrderLists extends DataSupport {
    private String orderNum;//订单编号
    private String goodsInfo;//物品信息
    private String sendInfo;//发送信息
    private String sendInfo_name;//发送信息
    private String sendInfo_phone;//发送信息
    private String receiverInfo_name;//接收信息
    private String receiverInfo_phone;//接收信息
    private String receiverInfo;//接收信息
    private String date;//订单日期
    private String go_home_time;//取件时间
    private String state;//订单状态
    private String orderPrice;//订单价格
    private String palWay;//支付方式
    private String workerInfo;//配送员信息
    private String weight;//物品重量
    private String distance;//距离
    private String message;//备注

    public OrderLists() {
    }

    public OrderLists(String goodsInfo, String sendInfo_name, String receiverInfo_name, String palWay, String weight, String message) {
        this.goodsInfo = goodsInfo;
        this.sendInfo_name = sendInfo_name;
        this.receiverInfo_name = receiverInfo_name;
        this.palWay = palWay;
        this.weight = weight;
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        OrderLists another = (OrderLists) obj;

        return goodsInfo.equals(another.getGoodsInfo()) && sendInfo_name.equals(another.getSendInfo_name()) &&
                receiverInfo_name.equals(another.getReceiverInfo_name()) && palWay.equals(another.getPalWay()) &&
                weight.equals(another.getWeight()) && message.equals(another.getMessage());
    }

    public String getGo_home_time() {
        return go_home_time;
    }

    public void setGo_home_time(String go_home_time) {
        this.go_home_time = go_home_time;
    }

    public String getSendInfo_name() {
        return sendInfo_name;
    }

    public void setSendInfo_name(String sendInfo_name) {
        this.sendInfo_name = sendInfo_name;
    }

    public String getSendInfo_phone() {
        return sendInfo_phone;
    }

    public void setSendInfo_phone(String sendInfo_phone) {
        this.sendInfo_phone = sendInfo_phone;
    }

    public String getReceiverInfo_name() {
        return receiverInfo_name;
    }

    public void setReceiverInfo_name(String receiverInfo_name) {
        this.receiverInfo_name = receiverInfo_name;
    }

    public String getReceiverInfo_phone() {
        return receiverInfo_phone;
    }

    public void setReceiverInfo_phone(String receiverInfo_phone) {
        this.receiverInfo_phone = receiverInfo_phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(String receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    public String getWorkerInfo() {
        return workerInfo;
    }

    public void setWorkerInfo(String workerInfo) {
        this.workerInfo = workerInfo;
    }


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getSendInfo() {
        return sendInfo;
    }

    public void setSendInfo(String sendInfo) {
        this.sendInfo = sendInfo;
    }

    public String getReciverInfo() {
        return receiverInfo;
    }

    public void setReciverInfo(String reciverInfo) {
        this.receiverInfo = reciverInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getPalWay() {
        return palWay;
    }

    public void setPalWay(String palWay) {
        this.palWay = palWay;
    }
}
