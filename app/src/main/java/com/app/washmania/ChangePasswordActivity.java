package com.app.washmania;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.app.washmania.model.BaseResponse;
import com.app.washmania.others.CircularTextView;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    EditText et_oldpassword, et_newpass, etconfirmpass;
    Button btn_forgot;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    String user_oldpass, user_newpass;
    BaseResponse registration;

    CircularTextView tv_cartcount;
    ImageView iv_cart, btn_back, btn_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(WMPreference.INSTANCE.get_Cartount(mContext));

    }

    private void initViews() {

        et_oldpassword = findViewById(R.id.et_oldpassword);
        et_newpass = findViewById(R.id.et_newpass);
        etconfirmpass = findViewById(R.id.etconfirmpass);


        btn_forgot = findViewById(R.id.btn_forgot);
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
        btn_forgot.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btn_forgot) {
            if (et_oldpassword.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Your Old Password");
                return;
            }

            if (et_newpass.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Your New Password");
                return;
            }
            if (etconfirmpass.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Your New Confirm Password");
                return;
            }

            if (!etconfirmpass.getText().toString().equalsIgnoreCase(et_newpass.getText().toString().trim())) {
                Utility.INSTANCE.showToastShort(mContext, "New Password & Confirm Password doesn't matches");
                return;
            }


            user_oldpass = et_oldpassword.getText().toString().trim();
            user_newpass = et_newpass.getText().toString().trim();

            verifyUser();

        }
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

    private void verifyUser() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<BaseResponse> call = redditAPI.ChangePassword(user_oldpass, user_newpass, user_newpass, WMPreference.INSTANCE.get_userId(mContext));
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                Log.d("String", "" + response);
                registration = response.body();
                if (registration.getAck() == 1) {
                    Utility.INSTANCE.showToastShort(mContext, registration.getMsg());
                } else {
                    Utility.INSTANCE.showToastShort(mContext, registration.getMsg());
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
