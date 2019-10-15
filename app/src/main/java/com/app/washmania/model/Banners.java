package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Banners {

    @SerializedName("BannerData")
    @Expose
    private List<BannerDatum> bannerData = null;
    @SerializedName("Ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<BannerDatum> getBannerData() {
        return bannerData;
    }

    public void setBannerData(List<BannerDatum> bannerData) {
        this.bannerData = bannerData;
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

    public class BannerDatum {

        @SerializedName("banner_id")
        @Expose
        private String bannerId;
        @SerializedName("banner_image")
        @Expose
        private String bannerPhoto;

        public String getBannerId() {
            return bannerId;
        }

        public void setBannerId(String bannerId) {
            this.bannerId = bannerId;
        }

        public String getBannerPhoto() {
            return bannerPhoto;
        }

        public void setBannerPhoto(String bannerPhoto) {
            this.bannerPhoto = bannerPhoto;
        }

    }
}
