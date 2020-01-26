package com.app.washmania;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.app.washmania.model.CartDeleteAction;
import com.app.washmania.others.CircularTextView;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Feedbackactivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    EditText et_name;
    EditText et_email;
    EditText et_phoneno;
    Button btn_placeorder;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    String user_name, user_email, user_phone, user_comment;
    CartDeleteAction registration;
    TextView tv_pagename;
    FrameLayout cartvie;
    ImageView btn_menu, btn_back;
    EditText et_comment;
    CircularTextView tv_cartcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();

    }

    private void initViews() {
        ImageView iv_home=findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,DashboardActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
        cartvie = findViewById(R.id.cartvie);
        cartvie.setVisibility(View.GONE);
        tv_pagename = findViewById(R.id.tv_pagename);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_phoneno = findViewById(R.id.et_phoneno);
        btn_placeorder = findViewById(R.id.btn_placeorder);
        //tv_pagename.setText("Feedback");
        btn_placeorder.setOnClickListener(this);

        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setVisibility(View.VISIBLE);

        et_comment = findViewById(R.id.et_comment);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        tv_cartcount = (CircularTextView) findViewById(R.id.tv_cartcount);
        tv_cartcount.setVisibility(View.VISIBLE);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);


        if (WMPreference.INSTANCE.getisVerified(mContext)) {
            et_name.setText(WMPreference.INSTANCE.get_firstName(mContext) + " " + WMPreference.INSTANCE.get_lastName(mContext));
            et_email.setText(WMPreference.INSTANCE.get_userEmail(mContext));
            et_phoneno.setText(WMPreference.INSTANCE.get_userPhone(mContext));
        }

        ImageView iv_phone=findViewById(R.id.iv_phone);
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
        if (v == btn_placeorder) {
            if (et_name.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Name");
                return;
            }

            if (et_email.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Email");
                return;
            }

            if (!Utility.INSTANCE.isValidEmail(et_email.getText().toString())) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Valid Email");
                return;
            }

            if (et_phoneno.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Phone No.");
                return;
            }
            if (et_comment.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Comment");
                return;
            }

            user_comment = et_comment.getText().toString().trim();
            user_name = et_name.getText().toString().trim();
            user_email = et_email.getText().toString().trim();
            user_phone = et_phoneno.getText().toString().trim();


            postShippingDetails();

        } else if (v == btn_back) {
            onBackPressed();
            finish();
        }
    }

    private void postShippingDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<CartDeleteAction> call = redditAPI.PostFeedback(WMPreference.INSTANCE.get_userId(mContext), user_name, user_email, user_phone, user_comment);
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    if (registration.getAck().equals("1")) {
                        Utility.INSTANCE.showToastShort(mContext, registration.getMsg());
                        onBackPressed();
                    } else {
                        Utility.INSTANCE.showToastShort(mContext, registration.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(WMPreference.INSTANCE.get_Cartount(mContext));
    }
}
