package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ZipCodemodel {

    @SerializedName("ZipData")
    @Expose
    private List<ZipDatum> zipData = null;
    @SerializedName("Ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<ZipDatum> getZipData() {
        return zipData;
    }

    public void setZipData(List<ZipDatum> zipData) {
        this.zipData = zipData;
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

    public class ZipDatum {

        @SerializedName("zipcode_id")
        @Expose
        private String zipcodeId;
        @SerializedName("available_zipcode")
        @Expose
        private String availableZipcode;

        public String getZipcodeId() {
            return zipcodeId;
        }

        public void setZipcodeId(String zipcodeId) {
            this.zipcodeId = zipcodeId;
        }

        public String getAvailableZipcode() {
            return availableZipcode;
        }

        public void setAvailableZipcode(String availableZipcode) {
            this.availableZipcode = availableZipcode;
        }

    }
}
