package com.app.washmania

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.app.washmania.model.AboutUsmodel
import com.app.washmania.others.BaseActivity
import com.app.washmania.others.CircularTextView
import com.app.washmania.others.ConnectionDetector
import com.app.washmania.others.Utility.CallContactNo
import com.app.washmania.others.Utility.showToastShort
import com.app.washmania.retrofit.api.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Color
import android.graphics.Color.parseColor
import android.view.Gravity
import android.widget.LinearLayout
import com.app.washmania.others.Utility
import kotlinx.android.synthetic.main.activity_pickup.*

class PickupActivity : BaseActivity(), View.OnClickListener {

    lateinit var mContext: Context
    lateinit var btn_menu: ImageView
    lateinit var btn_back: ImageView
    lateinit var cd: ConnectionDetector
    lateinit var tv_pagename: TextView
    lateinit var tv_title: TextView
    lateinit var tv_desc: TextView
    var aboutUs: AboutUsmodel? = null
    lateinit var pDialog: ProgressDialog
    lateinit var tv_cartcount: CircularTextView
    lateinit var iv_cart: ImageView
    var calendar: Calendar? = null
    var listDates: MutableList<String> = arrayListOf()
    var tag=0
    override fun initResources() {
        mContext = this
        cd = ConnectionDetector(mContext)
        pDialog = ProgressDialog(mContext)
        pDialog.setMessage("Loading...")
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setCancelable(false)
    }

    override fun initListeners() {
        calendar = Calendar.getInstance(TimeZone.getDefault())
        val iv_home = findViewById(R.id.iv_home) as ImageView
        iv_home.setOnClickListener(View.OnClickListener { CallContactNo(mContext) })

        tv_pagename = findViewById(R.id.tv_pagename)
        btn_back = findViewById(R.id.btn_back)
        btn_menu = findViewById(R.id.btn_menu)
        btn_menu.visibility = View.GONE
        btn_back.visibility = View.VISIBLE
        btn_menu.setOnClickListener(this)
        btn_back.setOnClickListener(this)
        tv_cartcount = findViewById(R.id.tv_cartcount) as CircularTextView
        val colorStr = resources.getString(R.string.green_color)
        tv_cartcount.setSolidColor(colorStr)
        tv_title = findViewById(R.id.tv_title)
        iv_cart = findViewById(R.id.iv_cart)
        iv_cart.setOnClickListener(this)
        /*if (cd.isConnected) {
            parsejson()
        } else {
            showToastShort(mContext, getString(R.string.no_internet_msg))
        }*/

        val iv_phone = findViewById(R.id.iv_phone) as ImageView
        iv_phone.setOnClickListener(View.OnClickListener { CallContactNo(mContext) })

        generateCalender()
    }

    override fun getLayout(): Int {
        return R.layout.activity_pickup
    }

    override fun onClick(v: View?) {
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
        val BASE_URL = resources.getString(R.string.base_url)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val redditAPI: ApiServices
        redditAPI = retrofit.create(ApiServices::class.java!!)
        val call = redditAPI.GetAboutUS()
        call.enqueue(object : Callback<AboutUsmodel> {

            override fun onResponse(call: Call<AboutUsmodel>, response: retrofit2.Response<AboutUsmodel>) {
                Log.d("String", "" + response)
                if (response.isSuccessful) {
                    aboutUs = response.body()
                    if (aboutUs != null) {
                        setData()
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<AboutUsmodel>, t: Throwable) {
                pDialog.dismiss()
            }
        })
    }

    private fun setData() {
        tv_title.text = aboutUs!!.getAboutData().contentTitle
        tv_desc.text = aboutUs!!.getAboutData().getContent()
    }


    private fun generateCalender() {
        listDates!!.clear()
        val currentYear = calendar!!.get(Calendar.YEAR)
        val currentMonth = calendar!!.get(Calendar.MONTH) + 1
        val currentDay = calendar!!.get(Calendar.DAY_OF_MONTH)
        Log.e("currentDay", "" + currentDay)
        val cal = Calendar.getInstance()
        //cal.set(Calendar.MONTH, 1);
        cal.set(currentYear, currentMonth, 0)
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        Log.e("currentDa", "" + maxDay)
        val df = SimpleDateFormat("MMM dd, yyyy")
        print(df.format(cal.time))
        for (i in currentDay..maxDay) {
            cal.set(Calendar.DAY_OF_MONTH, i)
            listDates!!.add(df.format(cal.time))
            //Log.e("DaysofMonth", df.format(cal.time))
            //System.out.print(", " + df.format(cal.getTime()));
        }

        inflateLayout()

    }

    private fun inflateLayout() {
        val buttonLayoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        v_container.removeAllViews()
        buttonLayoutParams.setMargins(5, 5, 5, 5)
        for (i in 0 until listDates!!.size) {
            val tv = TextView(applicationContext)
            tv.text = listDates!![i]
            tv.height = 100
            tv.textSize = 16.0f
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.parseColor("#000000"))
            tv.background = resources.getDrawable(R.drawable.rounded_corner_yellow)
            tv.id = i + 1
            tv.layoutParams = buttonLayoutParams
            tv.tag = i
            tv.setPadding(20, 10, 20, 10)
            v_container.addView(tv)

            if(i!=tag){
                tv.setTextColor(Color.parseColor("#000000"))
                tv.background = resources.getDrawable(R.drawable.rounded_corner_yellow)
            }else{
                tv.setTextColor(Color.parseColor("#ffffff"))
                tv.background = resources.getDrawable(R.drawable.rounded_corner_selected)
            }

            tv.setOnClickListener(View.OnClickListener {
                tag= tv.tag as Int
                inflateLayout()
            })
        }
    }

}