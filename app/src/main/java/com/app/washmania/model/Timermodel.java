package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Timermodel {

    @SerializedName("PickupTimeData")
    @Expose
    public List<String> pickupTimeData = null;
    @SerializedName("Ack")
    @Expose
    public Integer ack;
    @SerializedName("msg")
    @Expose
    public String msg;

    public List<String> getPickupTimeData() {
        return pickupTimeData;
    }

    public void setPickupTimeData(List<String> pickupTimeData) {
        this.pickupTimeData = pickupTimeData;
    }

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
        this.ack = ack;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
