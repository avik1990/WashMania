package com.app.washmania;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.washmania.adapter.ViewPagerAdapter;
import com.app.washmania.fragments.*;
import com.app.washmania.model.ProductsMod;
import com.app.washmania.others.CircularTextView;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener, FragmentMen.onSomeEventListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    //Fragments

    FragmentAllOrder menFragment;
    FragmentActiveOrder womenFragment;
    FragmentPastOrder kidFragment;
    CircularTextView tv_cartcount;
    String[] tabTitle = {"All", "Active", "Past"};
    //int[] unreadCount = {12, 0, 1};
    Context mContext;
    String catID = "", catName = "";
    ImageView iv_cart, btn_back, btn_menu;
    int menCount = 0, womenCount = 0, kidsCount = 0;
    int[] unreadCount = {menCount, womenCount, kidsCount};
    TextView tv_pagename;
    TextView tv_count;
    ProductsMod productsMod;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_without_icon);
        mContext = this;

        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();
        initTabs();
    }

    private void initViews() {
        //Initializing viewPager
        tv_pagename = findViewById(R.id.tv_pagename);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setVisibility(View.VISIBLE);
        btn_back.setOnClickListener(this);
        tv_cartcount = findViewById(R.id.tv_cartcount);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
        tv_pagename.setText("My Orders");
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
    }

    private void initTabs() {
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        try {
            setupTabIcons();
        } catch (Exception e) {
            e.printStackTrace();
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        menFragment = new FragmentAllOrder();
        womenFragment = new FragmentActiveOrder();
        kidFragment = new FragmentPastOrder();
        adapter.addFragment(menFragment, "All");
        adapter.addFragment(womenFragment, "Active");
        adapter.addFragment(kidFragment, "Past");
        menFragment.setArguments(bundle);
        womenFragment.setArguments(bundle);
        kidFragment.setArguments(bundle);
        viewPager.setAdapter(adapter);
    }

    private View prepareTabView(int pos) {
        //unreadCount = new int[]{menCount, womenCount, kidsCount};
        View view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_count = view.findViewById(R.id.tv_count);
        tv_count.setVisibility(View.GONE);
        tv_title.setText(tabTitle[pos]);
        /*if (unreadCount[pos] > 0) {
            tv_count.setVisibility(View.GONE);
            tv_count.setText("" + unreadCount[pos]);
        } else
            tv_count.setText("0");*/


        return view;
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabTitle.length; i++) {
            /*TabLayout.Tab tabitem = tabLayout.newTab();
            tabitem.setCustomView(prepareTabView(i));
            tabLayout.addTab(tabitem);*/
            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
            onBackPressed();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else if (v == iv_cart) {
            Intent i = new Intent(mContext, ProductCart.class);
            i.putExtra("From", "Dashboard");
            startActivity(i);
        }
    }


    /*@Override
    public void getMenCount(String s) {
        menCount = Integer.parseInt(s);
        tabLayout.getTabAt(0).setCustomView(prepareTabView(0));
    }*/

    @Override
    public void getCount(int menCount, int womenCount, int kidsCount) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(WMPreference.INSTANCE.get_Cartount(mContext));
    }

    ///



}
