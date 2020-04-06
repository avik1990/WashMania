package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsModel {

    @SerializedName("CartData")
    @Expose
    private List<CartDatum> cartData = null;
    @SerializedName("PriceData")
    @Expose
    private PriceData priceData;
    @SerializedName("Ack")
    @Expose
    private String ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<CartDatum> getCartData() {
        return cartData;
    }

    public void setCartData(List<CartDatum> cartData) {
        this.cartData = cartData;
    }

    public PriceData getPriceData() {
        return priceData;
    }

    public void setPriceData(PriceData priceData) {
        this.priceData = priceData;
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
    public class CartDatum {

        @SerializedName("cart_id")
        @Expose
        private String cartId;
        @SerializedName("dress_category")
        @Expose
        private String dress_category;
        @SerializedName("dress_name")
        @Expose
        private String dressName;
        @SerializedName("dress_price")
        @Expose
        private String dressPrice;
        @SerializedName("dress_photo")
        @Expose
        private String dressPhoto;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("subtotal")
        @Expose
        private String subtotal;
        @SerializedName("dress_for")
        @Expose
        private String dress_for;


        public String getDress_for() {
            return dress_for;
        }

        public void setDress_for(String dress_for) {
            this.dress_for = dress_for;
        }

        public String getDress_category() {
            return dress_category;
        }

        public void setDress_category(String dress_category) {
            this.dress_category = dress_category;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getDressName() {
            return dressName;
        }

        public void setDressName(String dressName) {
            this.dressName = dressName;
        }

        public String getDressPrice() {
            return dressPrice;
        }

        public void setDressPrice(String dressPrice) {
            this.dressPrice = dressPrice;
        }

        public String getDressPhoto() {
            return dressPhoto;
        }

        public void setDressPhoto(String dressPhoto) {
            this.dressPhoto = dressPhoto;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

    }

    public class PriceData {

        @SerializedName("total_price")
        @Expose
        private String totalPrice;
        @SerializedName("total_quantity")
        @Expose
        private String totalQuantity;
        @SerializedName("delivery_charge")
        @Expose
        private String deliveryCharge;
        @SerializedName("grand_total")
        @Expose
        private String grandTotal;

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(String totalQuantity) {
            this.totalQuantity = totalQuantity;
        }

        public String getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(String deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public String getGrandTotal() {
            return grandTotal;
        }

        public void setGrandTotal(String grandTotal) {
            this.grandTotal = grandTotal;
        }

    }
}

