package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartAdditionModel {
    @SerializedName("Ack")
    @Expose
    public String ack;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("cart_total_count")
    @Expose
    public String cart_total_count;

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCart_total_count() {
        return cart_total_count;
    }

    public void setCart_total_count(String cart_total_count) {
        this.cart_total_count = cart_total_count;
    }
}
