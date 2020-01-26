package com.app.washmania

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.app.washmania.model.ContactUsModel
import com.app.washmania.others.BaseActivity
import com.app.washmania.others.CircularTextView
import com.app.washmania.others.ConnectionDetector
import com.app.washmania.others.WMPreference.get_Cartount
import com.app.washmania.others.Utility
import com.app.washmania.retrofit.api.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContactUs : BaseActivity(), View.OnClickListener {


    lateinit var mContext: Context
    lateinit var btn_menu: ImageView
    lateinit var btn_back: ImageView
    lateinit var cd: ConnectionDetector
    lateinit var tv_pagename: TextView
    var aboutUs: ContactUsModel? = null
    lateinit var pDialog: ProgressDialog
    lateinit var tv_cartcount: CircularTextView
    lateinit var iv_cart: ImageView
    lateinit var tv_address: TextView
    lateinit var tv_email: TextView
    lateinit var tv_contactno1: TextView
    lateinit var tv_contactno2: TextView
    lateinit var tv_whatsapp: TextView




    override fun onResume() {
        super.onResume()
        tv_cartcount.setText(get_Cartount(mContext))
    }

    private fun setData() {
        tv_address.text = "Address : " + aboutUs!!.getContactData().getAddress()
        tv_email.setText("Email Address : " + aboutUs!!.getContactData().getEmail())
        tv_contactno1.setText("Contact No. 1 : " + aboutUs!!.getContactData().getContactNo1())
        tv_contactno2.setText("Contact No. 2 : " + aboutUs!!.getContactData().getContactNo2())
        tv_whatsapp.setText("Whatsapp No : " + aboutUs!!.getContactData().getWhatsappNo())
    }


    override fun initResources() {
        mContext = this
        cd = ConnectionDetector(mContext)
        pDialog = ProgressDialog(mContext)
        pDialog.setMessage("Loading...")
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setCancelable(false)

    }

    override fun initListeners() {
        val iv_home = findViewById(R.id.iv_home) as ImageView
        iv_home.setOnClickListener(View.OnClickListener {
            val i = Intent(mContext, DashboardActivity::class.java)
            startActivity(i)
            finishAffinity()
        })
        tv_pagename = findViewById(R.id.tv_pagename)
        btn_back = findViewById(R.id.btn_back)
        btn_menu = findViewById(R.id.btn_menu)
        btn_menu.visibility = View.GONE
        btn_back.setVisibility(View.VISIBLE)
        btn_menu.setOnClickListener(this)
        btn_back.setOnClickListener(this)
        tv_cartcount = findViewById(R.id.tv_cartcount) as CircularTextView
        val colorStr = getResources().getString(R.string.green_color)
        tv_cartcount.setSolidColor(colorStr)
        iv_cart = findViewById(R.id.iv_cart)

        tv_address = findViewById(R.id.tv_address)
        tv_email = findViewById(R.id.tv_email)
        tv_contactno1 = findViewById(R.id.tv_contactno1)
        tv_contactno2 = findViewById(R.id.tv_contactno2)
        tv_whatsapp = findViewById(R.id.tv_whatsapp)


        iv_cart.setOnClickListener(this)
        if (cd.isConnected) {
            parsejson()
        } else {
            Utility.showToastShort(mContext, getString(R.string.no_internet_msg))
        }

        val iv_phone = findViewById(R.id.iv_phone) as ImageView
        iv_phone.setOnClickListener(View.OnClickListener { Utility.CallContactNo(mContext) })
    }

    override fun getLayout(): Int {
        return R.layout.activity_contactus
    }


    override fun onClick(v: View) {
        if (v === btn_back) {
            finish()
            onBackPressed()
        } else if (v === iv_cart) {
            val i = Intent(mContext, ProductCart::class.java)
            i.putExtra("From", "Dashboard")
            startActivity(i)
        }
    }


    private fun parsejson() {
        pDialog.show()
        val BASE_URL = getResources().getString(R.string.base_url)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val redditAPI: ApiServices
        redditAPI = retrofit.create(ApiServices::class.java!!)
        val call = redditAPI.GetContactUs()
        call.enqueue(object : Callback<ContactUsModel> {

            override fun onResponse(call: Call<ContactUsModel>, response: retrofit2.Response<ContactUsModel>) {
                Log.d("String", "" + response)
                if (response.isSuccessful) {
                    aboutUs = response.body()
                    if (aboutUs != null) {
                        setData()
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<ContactUsModel>, t: Throwable) {
                pDialog.dismiss()
            }
        })
    }

}