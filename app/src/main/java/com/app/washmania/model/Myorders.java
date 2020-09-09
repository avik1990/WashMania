package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Myorders {

    @SerializedName("OrderData")
    @Expose
    private List<OrderDatum> orderData = null;
    @SerializedName("Ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<OrderDatum> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OrderDatum> orderData) {
        this.orderData = orderData;
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

    public class OrderDatum {
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("total_price")
        @Expose
        private String totalPrice;
        @SerializedName("order_date")
        @Expose
        private String orderDate;
        @SerializedName("pickup_boy")
        @Expose
        private String pickupBoy;
        @SerializedName("pickup_location")
        @Expose
        private String pickupLocation;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("delivery_status")
        @Expose
        private String deliveryStatus;
        @SerializedName("cancel_request_sent")
        @Expose
        private String cancelRequestSent;
        @SerializedName("review_posted")
        @Expose
        private String reviewPosted;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getPickupBoy() {
            return pickupBoy;
        }

        public void setPickupBoy(String pickupBoy) {
            this.pickupBoy = pickupBoy;
        }

        public String getPickupLocation() {
            return pickupLocation;
        }

        public void setPickupLocation(String pickupLocation) {
            this.pickupLocation = pickupLocation;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }

        public String getCancelRequestSent() {
            return cancelRequestSent;
        }

        public void setCancelRequestSent(String cancelRequestSent) {
            this.cancelRequestSent = cancelRequestSent;
        }

        public String getReviewPosted() {
            return reviewPosted;
        }

        public void setReviewPosted(String reviewPosted) {
            this.reviewPosted = reviewPosted;
        }

    }
}