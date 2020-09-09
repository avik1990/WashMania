package com.app.washmania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUsModel {
    @SerializedName("ContactData")
    @Expose
    public ContactData contactData;

    public ContactData getContactData() {
        return contactData;
    }

    public void setContactData(ContactData contactData) {
        this.contactData = contactData;
    }

    public class ContactData {

        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("contact_person")
        @Expose
        public String contact_person;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("contact_no1")
        @Expose
        public String contactNo1;
        @SerializedName("contact_no2")
        @Expose
        public String contactNo2;
        @SerializedName("whatsapp_no")
        @Expose
        public String whatsappNo;
        @SerializedName("Ack")
        @Expose
        public Integer ack;
        @SerializedName("msg")
        @Expose
        public String msg;

        public String getContact_person() {
            return contact_person;
        }

        public void setContact_person(String contact_person) {
            this.contact_person = contact_person;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContactNo1() {
            return contactNo1;
        }

        public void setContactNo1(String contactNo1) {
            this.contactNo1 = contactNo1;
        }

        public String getContactNo2() {
            return contactNo2;
        }

        public void setContactNo2(String contactNo2) {
            this.contactNo2 = contactNo2;
        }

        public String getWhatsappNo() {
            return whatsappNo;
        }

        public void setWhatsappNo(String whatsappNo) {
            this.whatsappNo = whatsappNo;
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
}
