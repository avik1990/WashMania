package com.app.washmania

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.app.washmania.adapter.NewsAdapter
import com.app.washmania.adapter.SliderPagerAdapter
import com.app.washmania.model.Banners
import com.app.washmania.model.Category
import com.app.washmania.others.BaseActivity
import com.app.washmania.others.Preference.get_firstName
import com.app.washmania.others.Preference.getisVerified
import com.app.washmania.others.Preference.set_checkClicked
import com.app.washmania.others.Utility
import com.app.washmania.others.Utility.openNavDrawer
import com.app.washmania.retrofit.api.ApiServices
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard_inc.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class DashboardActivity : BaseActivity(), NewsAdapter.onRowItemSelected,
    NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    var headerView: View? = null
    lateinit var context: Context
    lateinit var newsAdapter: NewsAdapter
    var navigationView: NavigationView? = null
    var btn_menu: ImageView? = null
    internal var banners: Banners? = null
    var slider_image_list: ArrayList<String> = arrayListOf()
    internal var page_position = 0
    lateinit var pDialog: ProgressDialog
    var dots: Array<ImageView?>? = null
    lateinit var sliderPagerAdapter: SliderPagerAdapter
    internal var getCategory: Category? = null

    companion object {
        val CLASS_NAME = "DashboardActivity"
    }

    override fun initResources() {
        context = this
        Fresco.initialize(context)
        pDialog = ProgressDialog(context)
        pDialog.setMessage("Loading...")
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setCancelable(false)
        navigationView = findViewById(R.id.nav_view)
        btn_menu = findViewById(R.id.btn_menu)
        navigationView!!.setNavigationItemSelectedListener(this)
        navigationView!!.itemIconTintList = null
        btn_menu!!.setOnClickListener(this)


    }

    private fun inflateAdapter(listNews: List<Category.CategoryDatum>) {
        newsAdapter = NewsAdapter(context, listNews, this)
        val mLayoutManager = GridLayoutManager(context, 2)
        rlRecycler.layoutManager = mLayoutManager
        rlRecycler.adapter = newsAdapter
    }


    override fun initListeners() {
        if (!getisVerified(context)) {
            val nav_Menu = navigationView!!.getMenu()
            nav_Menu.findItem(R.id.nav_myprofile).isVisible = false
            nav_Menu.findItem(R.id.nav_changepass).isVisible = false
            nav_Menu.findItem(R.id.nav_myprofile).isVisible = false
            nav_Menu.findItem(R.id.nav_myorders).isVisible = false
        } else {
            val nav_Menu = navigationView!!.getMenu()
            nav_Menu.findItem(R.id.nav_myprofile).isVisible = true
            nav_Menu.findItem(R.id.nav_changepass).isVisible = true
            nav_Menu.findItem(R.id.nav_myprofile).isVisible = true
            nav_Menu.findItem(R.id.nav_myorders).isVisible = true
        }

        if (getisVerified(context)) {
            headerView = navigationView!!.inflateHeaderView(R.layout.nav_header_main)
            val txt_name = headerView!!.findViewById(R.id.tv_user_name) as TextView
            val ivNavDrawer = findViewById(R.id.iv_product_photo) as ImageView
            txt_name.setText(get_firstName(context))
        } else {
            headerView = navigationView!!.inflateHeaderView(R.layout.nav_header_main_before_login)
            val btn_login = headerView!!.findViewById(R.id.btn_login) as Button
            val btn_register = headerView!!.findViewById(R.id.btn_register) as Button
            btn_login.setOnClickListener(View.OnClickListener {
                set_checkClicked(context, "0")
                val i = Intent(context, LoginActivity::class.java)
                startActivity(i)
            })
            btn_register.setOnClickListener(View.OnClickListener {
                // fetchZipCode()
                set_checkClicked(context, "0")
                val i = Intent(context, RegisterActivity::class.java)
                startActivity(i)
            })
        }

        getBannerData()
    }

    override fun getLayout(): Int {
        return R.layout.activity_dashboard_inc
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        openNavDrawer(id, context)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View?) {
        if (v === btn_menu) {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun getPosition(id: String) {
        val i = Intent(context, SubCategoryActivity::class.java)
        i.putExtra("cat_id", id)
        startActivity(i)
    }

    private fun getBannerData() {
        pDialog.show()
        val BASE_URL = resources.getString(R.string.base_url)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val redditAPI: ApiServices
        redditAPI = retrofit.create<ApiServices>(ApiServices::class.java!!)
        val call = redditAPI.GetBanners()
        call.enqueue(object : Callback<Banners> {

            override fun onResponse(call: Call<Banners>, response: retrofit2.Response<Banners>) {
                Log.d("ResponseString__", "" + response)
                if (response.isSuccessful()) {
                    banners = response.body()

                    if (banners!!.ack == 1) {
                        if (banners!!.bannerData.size > 0) {
                            for (i in 0 until banners!!.bannerData.size) {
                                slider_image_list!!.add(banners!!.bannerData[i].bannerPhoto)
                            }
                            Utility.showToastShort(context, "" + slider_image_list!!.size)
                            initslider()
                            addBottomDots(0)
                        } else {
                            rl_image.setVisibility(View.GONE)
                        }
                    } else {
                        rl_image.setVisibility(View.GONE)
                    }

                    getCategoryData()
                }
            }

            override fun onFailure(call: Call<Banners>, t: Throwable) {
                pDialog.dismiss()
            }
        })
    }


    private fun initslider() {
        try {
            sliderPagerAdapter = SliderPagerAdapter(this@DashboardActivity, slider_image_list)
            vp_slider.setAdapter(sliderPagerAdapter)
            sliderPagerAdapter.notifyDataSetChanged()

            vp_slider.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    addBottomDots(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
            val handler = Handler()
            val update = Runnable {
                if (page_position == slider_image_list!!.size) {
                    page_position = 0
                } else {
                    page_position = page_position + 1
                }
                vp_slider.setCurrentItem(page_position, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun addBottomDots(currentPage: Int) {
        try {
            dots = Array<ImageView?>(slider_image_list!!.size) { null }
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(7, 0, 7, 0)
            ll_dots.removeAllViews()
            for (i in dots!!.indices) {
                dots!![i] = ImageView(this)
                dots!![i]!!.setBackgroundResource(R.drawable.dot)
                dots!![i]!!.setLayoutParams(lp)
                ll_dots.addView(dots!![i])
            }

            if (dots!!.size > 0)
                dots!![currentPage]!!.setBackgroundResource(R.drawable.dot_selected)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getCategoryData() {
        val BASE_URL = resources.getString(R.string.base_url)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val redditAPI: ApiServices
        redditAPI = retrofit.create(ApiServices::class.java)
        val call = redditAPI.GeCategory()
        call.enqueue(object : Callback<Category> {

            override fun onResponse(call: Call<Category>, response: retrofit2.Response<Category>) {
                Log.d("String", "" + response)
                if (response.isSuccessful()) {
                    getCategory = response.body()
                    if (getCategory!!.getCategoryData().size > 0) {
                        inflateAdapter(getCategory!!.getCategoryData())
                    }
                }
                pDialog.dismiss();
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                pDialog.dismiss()
            }
        })
    }


}
