package com.app.washmania;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.app.washmania.model.LoginResponse;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    EditText et_email;
    Button btn_forgot;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    String user_email;
    LoginResponse registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();

    }

    private void initViews() {
        et_email = findViewById(R.id.et_email);
        btn_forgot = findViewById(R.id.btn_forgot);

        btn_forgot.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btn_forgot) {
            if (et_email.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Your OTP");
                return;
            }
            /*if (!Utility.INSTANCE.isValidEmail(et_email.getText().toString())) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Valid Email");
                return;
            }*/
            user_email = et_email.getText().toString().trim();

            verifyPhoneNo();

        }

    }

    private void verifyPhoneNo() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<LoginResponse> call = redditAPI.VerifyOTP(user_email, WMPreference.INSTANCE.get_userId(mContext), WMPreference.INSTANCE.get_UniqueId(mContext));
        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                Log.d("ResponseOTP", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    if (registration.getAck() == 1) {
                        if (registration.getLoginData().size() > 0) {
                            WMPreference.INSTANCE.setisVerified(mContext, true);
                            WMPreference.INSTANCE.set_firstuserName(mContext, registration.getLoginData().get(0).getFname());
                            WMPreference.INSTANCE.set_lastName(mContext, registration.getLoginData().get(0).getLname());
                            WMPreference.INSTANCE.set_userEmail(mContext, registration.getLoginData().get(0).getEmail());
                            WMPreference.INSTANCE.set_userPhone(mContext, registration.getLoginData().get(0).getPhone());
                            WMPreference.INSTANCE.set_userId(mContext, registration.getLoginData().get(0).getUserId());
                            WMPreference.INSTANCE.set_address(mContext, registration.getLoginData().get(0).getAddress());
                            WMPreference.INSTANCE.set_city(mContext, registration.getLoginData().get(0).getCity());
                            WMPreference.INSTANCE.set_state(mContext, registration.getLoginData().get(0).getState());
                            WMPreference.INSTANCE.set_Zip(mContext, registration.getLoginData().get(0).getZip());
                            Intent intent = new Intent(mContext, DashboardActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    } else {
                        Utility.INSTANCE.showToastShort(mContext, registration.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
