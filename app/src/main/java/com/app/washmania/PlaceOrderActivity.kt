package com.app.washmania

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import com.app.washmania.model.BaseResponse
import com.app.washmania.model.MyProfile
import com.app.washmania.model.Timermodel
import com.app.washmania.others.*
import com.app.washmania.others.Utility.CallContactNo
import com.app.washmania.others.Utility.showToastShort
import com.app.washmania.retrofit.api.ApiServices
import com.app.washmania.retrofit.api.Constants
import kotlinx.android.synthetic.main.activity_placeorder.*
import net.alexandroid.gps.GpsStatusDetector
import net.alexandroid.gps.GpsStatusDetector.GpsStatusDetectorCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class PlaceOrderActivity : BaseActivity(), View.OnClickListener,
    GpsStatusDetectorCallBack {

    lateinit var mContext: Context
    lateinit var btn_menu: ImageView
    lateinit var btn_back: ImageView
    internal var registration: MyProfile? = null
    internal var timermodel: Timermodel? = null

    internal var baseResponse: BaseResponse? = null

    lateinit var tv_address: EditText
    lateinit var tv_flatno: EditText
    lateinit var tv_pickupinstruction: EditText

    lateinit var cd: ConnectionDetector
    lateinit var tv_pagename: TextView
    lateinit var tv_changeAddress: TextView

    lateinit var pDialog: ProgressDialog
    lateinit var tv_cartcount: CircularTextView
    lateinit var iv_cart: ImageView
    var calendar: Calendar? = null
    var listDates: MutableList<String> = arrayListOf()
    var listTime: MutableList<String> = arrayListOf()
    var tag = 0
    var tag1 = 0

    lateinit var btn_placeoreder: Button
    private var mGpsStatusDetector: GpsStatusDetector? = null

    var stTime: String = ""
    var stDate: String = ""
    var year: String = ""

    override fun initResources() {
        mContext = this
        cd = ConnectionDetector(mContext)
        pDialog = ProgressDialog(mContext)
        pDialog.setMessage("Loading...")
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setCancelable(false)
        year = Calendar.getInstance().get(Calendar.YEAR).toString()
        mGpsStatusDetector = GpsStatusDetector(this)
        mGpsStatusDetector!!.checkGpsStatus()
        getUserDetails()
    }

    override fun initListeners() {
        calendar = Calendar.getInstance(TimeZone.getDefault())
        val iv_home = findViewById(R.id.iv_home) as ImageView
        iv_home.setOnClickListener(View.OnClickListener { CallContactNo(mContext) })

        tv_changeAddress = findViewById(R.id.tv_changeAddress)
        tv_address = findViewById(R.id.tv_address)
        tv_flatno = findViewById(R.id.tv_flatno)
        tv_pickupinstruction = findViewById(R.id.tv_pickupinstruction)
        btn_placeoreder = findViewById(R.id.btn_placeoreder)
        btn_placeoreder.setOnClickListener(this)
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
        iv_cart = findViewById(R.id.iv_cart)
        iv_cart.setOnClickListener(this)
        tv_changeAddress.setOnClickListener(this)
        val iv_phone = findViewById(R.id.iv_phone) as ImageView
        iv_phone.setOnClickListener(View.OnClickListener { CallContactNo(mContext) })

        generateCalender()

    }

    private fun generateTimer() {
        inflateLayoutTimer()
    }


    private fun getUserDetails() {
        pDialog.show()
        val BASE_URL = resources.getString(R.string.base_url)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val redditAPI: ApiServices
        redditAPI = retrofit.create(ApiServices::class.java)


        val call = redditAPI.GetMYProfile(WMPreference.get_userId(mContext))
        call.enqueue(object : Callback<MyProfile> {

            override fun onResponse(
                call: Call<MyProfile>,
                response: retrofit2.Response<MyProfile>
            ) {
                Log.d("String", "" + response)
                if (response.isSuccessful) {
                    registration = response.body()
                    if (registration!!.ack == 1) {
                        // setData()
                    }
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<MyProfile>, t: Throwable) {
                pDialog.dismiss()
            }
        })
    }


    override fun getLayout(): Int {
        return R.layout.activity_placeorder
    }

    override fun onClick(v: View?) {
        when {
            v === btn_back -> {
                finish()
                onBackPressed()
            }
            v === iv_cart -> {
                val i = Intent(mContext, ProductCart::class.java)
                i.putExtra("From", "Dashboard")
                startActivity(i)
            }
            v == btn_placeoreder -> {
                if (tv_flatno.text.isEmpty()) {
                    showToastShort(mContext, "Please Enter Flat / Apartment no")
                    return
                }

                if (stDate.isEmpty()) {
                    showToastShort(mContext, "Please Select Date")
                    return
                }

                if (stTime.isEmpty()) {
                    showToastShort(mContext, "Please Select Time")
                    return
                }
                stDate = stDate.replace("\n", " ") + " " + year

                postShippingData()

            }
            v == tv_changeAddress -> {
                val i = Intent(mContext, ChangeLocationActivity::class.java)
                startActivity(i)
            }
        }
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
        //val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        Log.e("currentDa", "" + maxDay)
        //val df = SimpleDateFormat("MMM dd, yyyy")

        val df = SimpleDateFormat("dd\nMMM")

        print(df.format(cal.time))
        var j = 0
        for (i in currentDay..maxDay) {
            j++
            if (j == 6) {
                break
            }
            cal.set(Calendar.DAY_OF_MONTH, i)
            listDates!!.add(df.format(cal.time))
        }

        inflateLayout()
    }

    private fun inflateLayout() {
        val buttonLayoutParams =
            LinearLayout.LayoutParams(150, 150)
        v_container.removeAllViews()
        buttonLayoutParams.setMargins(5, 5, 5, 5)
        stDate = listDates[0]

        for (i in 0 until listDates!!.size) {
            val tv = TextView(applicationContext)
            tv.text = listDates!![i]
            tv.height = 100
            tv.textSize = 14.0f
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.parseColor("#000000"))
            tv.background = resources.getDrawable(R.drawable.badge_background_unselected)
            tv.id = i + 1
            tv.layoutParams = buttonLayoutParams
            tv.tag = i
            tv.setPadding(20, 10, 20, 10)
            v_container.addView(tv)

            if (i != tag) {
                tv.setTextColor(Color.parseColor("#000000"))
                tv.background = resources.getDrawable(R.drawable.badge_background_unselected)
            } else {
                tv.setTextColor(Color.parseColor("#ffffff"))
                tv.background = resources.getDrawable(R.drawable.badge_background_1)
                stDate = tv.text.toString()
                callTimerApi()
            }

            tv.setOnClickListener(View.OnClickListener {
                tag = tv.tag as Int
                stDate = tv.text.toString()
                inflateLayout()
                callTimerApi()
            })
        }
    }

    private fun callTimerApi() {
        stDate = stDate.replace("\n", " ") + " " + year
        Log.e("stDate",stDate)
        getTimerDetails()
    }


    private fun getTimerDetails() {
        pDialog.show()
        val BASE_URL = resources.getString(R.string.base_url)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val redditAPI: ApiServices
        redditAPI = retrofit.create(ApiServices::class.java)


        val call = redditAPI.GetPickupDate(Utility.getFormattedDate(stDate))
        call.enqueue(object : Callback<Timermodel> {

            override fun onResponse(
                call: Call<Timermodel>,
                response: retrofit2.Response<Timermodel>
            ) {
                if (response.isSuccessful) {
                    timermodel = response.body()
                    if (timermodel!!.ack == 1) {
                        listTime!!.clear()
                        listTime = timermodel!!.pickupTimeData
                        generateTimer()
                    } else {
                        v_containertime.removeAllViews()
                        stTime = "";
                        showToastShort(mContext, "No Time Slot Available")
                    }
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<Timermodel>, t: Throwable) {
                pDialog.dismiss()
            }
        })
    }


    private fun inflateLayoutTimer() {
        val buttonLayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        v_containertime.removeAllViews()
        buttonLayoutParams.setMargins(5, 5, 5, 5)
        stTime = listTime.get(0)
        for (i in 0 until listTime!!.size) {
            val tv = TextView(applicationContext)
            tv.text = listTime!![i]
            tv.height = 100
            tv.textSize = 16.0f
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.parseColor("#000000"))
            tv.background = resources.getDrawable(R.drawable.rounded_corner_yellow)
            tv.id = i + 1
            tv.layoutParams = buttonLayoutParams
            tv.tag = i
            tv.setPadding(20, 10, 20, 10)
            v_containertime.addView(tv)

            if (i != tag1) {
                tv.setTextColor(Color.parseColor("#000000"))
                tv.background = resources.getDrawable(R.drawable.rounded_corner_yellow)
            } else {
                tv.setTextColor(Color.parseColor("#ffffff"))
                tv.background = resources.getDrawable(R.drawable.rounded_corner_selected)
            }

            tv.setOnClickListener(View.OnClickListener {
                tag1 = tv.tag as Int
                stTime = tv.text.toString()
                inflateLayoutTimer()
            })
        }
    }

    override fun onResume() {
        super.onResume()
        tv_cartcount.text = WMPreference.get_Cartount(mContext)
    }


    private fun postShippingData() {
        pDialog.show()
        val BASE_URL = resources.getString(R.string.base_url)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(Constants.setTimeOutForDatapost())
            .build()

        val redditAPI: ApiServices
        redditAPI = retrofit.create(ApiServices::class.java)


        val call = redditAPI.CheckoutCall(
            registration!!.userData[0].fname,
            registration!!.userData[0].lname,
            registration!!.userData[0].email,
            registration!!.userData[0].phone,
            registration!!.userData[0].address,
            registration!!.userData[0].city,
            registration!!.userData[0].state,
            registration!!.userData[0].zip,
            WMPreference.get_userId(mContext),
            WMPreference.get_UniqueId(mContext),
            "",
            tv_flatno.text.toString(),
            tv_pickupinstruction.text.toString(),
            Utility.getFormattedDate(stDate),
            stTime,
            registration!!.userData[0].landmark
        )

        call.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: retrofit2.Response<BaseResponse>
            ) {
                Log.d("String", "" + response)
                baseResponse = response.body()
                if (baseResponse!!.ack == 1) {
                    showDialog(mContext, baseResponse!!.getMsg())
                } else {
                    showToastShort(mContext, baseResponse!!.getMsg())
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                pDialog.dismiss()
            }
        })
    }

    fun showDialog(activity: Context, message: String) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_thankyou_layout)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val et_pincode = dialog.findViewById(R.id.et_pincode) as TextView
        et_pincode.setText(message)
        val mDialogOk = dialog.findViewById(R.id.frmOk) as FrameLayout
        mDialogOk.setOnClickListener(View.OnClickListener {
            WMPreference.setUniqueKey(mContext, false)
            WMPreference.set_Cartount(mContext, "0")

            val i = Intent(mContext, DashboardActivity::class.java)
            startActivity(i)
            finishAffinity()
        })

        dialog.show()
    }

    override fun onGpsAlertCanceledByUser() {
    }

    override fun onGpsSettingStatus(enabled: Boolean) {
    }


}