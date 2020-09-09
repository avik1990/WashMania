package com.app.washmania;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.app.washmania.adapter.CartAdapter;
import com.app.washmania.model.BaseResponse;
import com.app.washmania.model.MyCart;
import com.app.washmania.model.ZipCodeVerify;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductCart extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ImageView btn_menu, btn_back;
    ConnectionDetector cd;
    TextView tv_pagename;
    ProgressDialog pDialog;
    MyCart myCart;
    public static List<MyCart.CartDatum> listmycart = new ArrayList<>();
    RecyclerView rl_cart;
    LinearLayout footer;
    RelativeLayout footerBtn;
    TextView  tv_taxpercentage, tv_grandtotdal,tv_totalcost,tv_grandtotal;
    Button btn_checkout;
    ZipCodeVerify zipCodeVerify;
    FrameLayout cartvie;
    TextView tv_delivery,tvQuantity;
    String isQuickDelivery = "0";
    Button btn_addMoreService;
    TextView tv_deliverycharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cart);
        cd = new ConnectionDetector(mContext);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);


        rl_cart = findViewById(R.id.rl_cart);
        rl_cart.setLayoutManager(new LinearLayoutManager(mContext));
        tv_delivery = findViewById(R.id.tv_delivery);
        if (cd.isConnected()) {
            LoadCartProduct();
        } else {
            Utility.INSTANCE.showToastShort(mContext, getResources().getString(R.string.no_internet_msg));
        }
        initView();
    }

    private void initView() {
        ImageView iv_home = findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DashboardActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
        tv_totalcost= findViewById(R.id.tv_totalcost);
        tv_grandtotal= findViewById(R.id.tv_grandtotal);
        tvQuantity= findViewById(R.id.tv_totalquantity);
        tv_pagename = findViewById(R.id.tv_pagename);
       // tv_totalprice = findViewById(R.id.tv_totalprice);
        tv_deliverycharge= findViewById(R.id.tv_deliverycharge);
        tv_taxpercentage = findViewById(R.id.tv_taxpercentage);
        tv_grandtotdal = findViewById(R.id.tv_grandtotdal);
        footer = findViewById(R.id.footer);
        footerBtn = findViewById(R.id.footerBtn);
        btn_checkout = findViewById(R.id.btn_checkout);
        btn_addMoreService= findViewById(R.id.btn_addMoreService);
        cartvie = findViewById(R.id.cartvie);
        cartvie.setVisibility(View.GONE);
        btn_back = findViewById(R.id.btn_back);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);
        btn_addMoreService.setOnClickListener(this);
        tv_pagename.setText("My Basket");
        tvQuantity.setText("0");

        ImageView iv_phone = findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Utility.INSTANCE.CallContactNo(mContext);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
            onBackPressed();
        } else if (v == btn_checkout) {
            if (WMPreference.INSTANCE.getisVerified(mContext)) {
                Intent i = new Intent(mContext, CheckoutActivity.class);
                i.putExtra("quick_delivery", isQuickDelivery);
                startActivity(i);
            } else {
                WMPreference.INSTANCE.set_checkClicked(mContext, "1");
                Intent i = new Intent(mContext, LoginActivity.class);
                startActivity(i);
            }
        } else if (v == btn_addMoreService) {
            Intent i = new Intent(mContext, DashboardActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }


    public void LoadCartProduct() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<MyCart> call = redditAPI.GetMyCart(WMPreference.INSTANCE.get_userId(mContext), WMPreference.INSTANCE.get_UniqueId(mContext));
        call.enqueue(new Callback<MyCart>() {

            @Override
            public void onResponse(Call<MyCart> call, retrofit2.Response<MyCart> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    myCart = response.body();
                    if (myCart.getAck().equals("1")) {
                        WMPreference.INSTANCE.set_Cartount(mContext, myCart.getPriceData().getTotal_quantity());
                        rl_cart.setVisibility(View.VISIBLE);
                        listmycart = myCart.getCartData();
                        inflateCartAdapter();
                    } else {
                        WMPreference.INSTANCE.set_Cartount(mContext, "0");
                        footerBtn.setVisibility(View.GONE);
                        footer.setVisibility(View.GONE);
                        Utility.INSTANCE.showToastShort(mContext, myCart.getMsg());
                        btn_checkout.setVisibility(View.GONE);
                        rl_cart.setVisibility(View.GONE);
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyCart> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void inflateCartAdapter() {
        CartAdapter mAdapter = new CartAdapter(listmycart, mContext);
        rl_cart.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        tv_totalcost.setText("\u20B9" + " " + myCart.getPriceData().totalPrice);
        tv_deliverycharge.setText("\u20B9" + " " + myCart.getPriceData().delivery_charge);
        tvQuantity.setText(myCart.getPriceData().total_quantity);
        tv_grandtotal.setText("\u20B9" + " " +myCart.getPriceData().getGrand_total());

        footer.setVisibility(View.VISIBLE);
    }
}
