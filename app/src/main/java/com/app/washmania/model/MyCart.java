package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyCart {

    public List<String> total_arr;
    @SerializedName("CartData")
    @Expose
    public List<CartDatum> cartData = null;
    @SerializedName("PriceData")
    @Expose
    public PriceData priceData;
    @SerializedName("Ack")
    @Expose
    public String ack;
    @SerializedName("msg")
    @Expose
    public String msg;

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

    public class PriceData {

        @SerializedName("total_price")
        @Expose
        public String totalPrice;
        @SerializedName("total_quantity")
        @Expose
        public String total_quantity;
        @SerializedName("delivery_charge")
        @Expose
        public String delivery_charge;
        @SerializedName("grand_total")
        @Expose
        public String grand_total;

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getTotal_quantity() {
            return total_quantity;
        }

        public void setTotal_quantity(String total_quantity) {
            this.total_quantity = total_quantity;
        }



        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getGrand_total() {
            return grand_total;
        }

        public void setGrand_total(String grand_total) {
            this.grand_total = grand_total;
        }
    }

    public class CartDatum {
        @SerializedName("cart_id")
        @Expose
        public String cartId;
        @SerializedName("dress_photo")
        @Expose
        public String dress_photo;
        @SerializedName("dress_name")
        @Expose
        public String dress_name;
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("unit_price")
        @Expose
        public String unitPrice;
        @SerializedName("subtotal")
        @Expose
        public String subtotal;
        @SerializedName("dress_category")
        @Expose
        public String dress_category;
        @SerializedName("dress_for")
        @Expose
        public String dress_for;

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

        public String getDress_photo() {
            return dress_photo;
        }

        public void setDress_photo(String dress_photo) {
            this.dress_photo = dress_photo;
        }

        public String getDress_name() {
            return dress_name;
        }

        public void setDress_name(String dress_name) {
            this.dress_name = dress_name;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }
    }


}
