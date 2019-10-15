package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {
    @SerializedName("CategoryData")
    @Expose
    public List<CategoryDatum> categoryData = null;
    @SerializedName("Ack")
    @Expose
    public Integer ack;
    @SerializedName("msg")
    @Expose
    public String msg;

    public List<CategoryDatum> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(List<CategoryDatum> categoryData) {
        this.categoryData = categoryData;
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


    public class CategoryDatum {

        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("category_name")
        @Expose
        public String categoryName;
        @SerializedName("category_photo")
        @Expose
        public String categoryPhoto;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryPhoto() {
            return categoryPhoto;
        }

        public void setCategoryPhoto(String categoryPhoto) {
            this.categoryPhoto = categoryPhoto;
        }

    }

}
