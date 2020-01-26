package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsMod {
    @SerializedName("DressData")
    @Expose
    private List<DressDatum> dressData = null;
    @SerializedName("Ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<DressDatum> getDressData() {
        return dressData;
    }

    public void setDressData(List<DressDatum> dressData) {
        this.dressData = dressData;
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


    public class DressDatum {
        @SerializedName("dress_price")
        @Expose
        private String dress_price;
        @SerializedName("dress_id")
        @Expose
        private String dressId;
        @SerializedName("dress_name")
        @Expose
        private String dressName;
        @SerializedName("dress_photo")
        @Expose
        private String dressPhoto;
        @SerializedName("dress_for")
        @Expose
        private String dressFor;

        public String getDress_price() {
            return dress_price;
        }

        public void setDress_price(String dress_price) {
            this.dress_price = dress_price;
        }

        public String getDressId() {
            return dressId;
        }

        public void setDressId(String dressId) {
            this.dressId = dressId;
        }

        public String getDressName() {
            return dressName;
        }

        public void setDressName(String dressName) {
            this.dressName = dressName;
        }

        public String getDressPhoto() {
            return dressPhoto;
        }

        public void setDressPhoto(String dressPhoto) {
            this.dressPhoto = dressPhoto;
        }

        public String getDressFor() {
            return dressFor;
        }

        public void setDressFor(String dressFor) {
            this.dressFor = dressFor;
        }

    }

}
