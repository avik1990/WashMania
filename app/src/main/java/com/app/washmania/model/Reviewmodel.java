package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reviewmodel {
    @SerializedName("ReviewData")
    @Expose
    private ReviewData reviewData;

    public ReviewData getReviewData() {
        return reviewData;
    }

    public void setReviewData(ReviewData reviewData) {
        this.reviewData = reviewData;
    }
    public class ReviewData {

        @SerializedName("review")
        @Expose
        private String review;
        @SerializedName("rate")
        @Expose
        private String rate;
        @SerializedName("Ack")
        @Expose
        private String ack;
        @SerializedName("msg")
        @Expose
        private String msg;

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

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

    }

}
