package com.app.washmania;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.app.washmania.model.LoginResponse;
import com.app.washmania.model.ZipCodemodel;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;
import com.google.android.flexbox.FlexboxLayout;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    EditText et_phoneno;
    EditText et_password;
    Button btn_login;
    ConnectionDetector cd;
    TextView btn_forgotpassword;
    ProgressDialog pDialog;
    String user_phone, user_password;
    LoginResponse registration;
    ZipCodemodel zipCodemodel;
    List<String> list_text = new ArrayList<>();
    RelativeLayout rl_signup;
    ImageView show_pass_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();

    }

    private void initViews() {
        show_pass_btn=findViewById(R.id.show_pass_btn);
        et_phoneno = findViewById(R.id.et_phoneno);
        et_password = findViewById(R.id.et_password);
        rl_signup = findViewById(R.id.rl_signup);
        btn_login = findViewById(R.id.btn_login);
        btn_forgotpassword = findViewById(R.id.tv_forgotpass);

        btn_forgotpassword.setOnClickListener(this);
        show_pass_btn.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        rl_signup.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btn_login) {
            if (et_phoneno.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Your Phone No.");
                return;
            }

            if (et_password.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Password");
                return;
            }

            user_phone = et_phoneno.getText().toString().trim();
            user_password = et_password.getText().toString().trim();

            verifyUser();

        } else if (v == rl_signup) {
            fetchZipCode();
        } else if (v == btn_forgotpassword) {
            Intent i = new Intent(mContext, ForgotPasswordActivity.class);
            startActivity(i);
        }else if(v==show_pass_btn){
            if(et_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                show_pass_btn.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                //Show Password
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                show_pass_btn.setImageResource(R.drawable.ic_visibility_black_24dp);
                //Hide Password
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private void verifyUser() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        // set your desired log level
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<LoginResponse> call = redditAPI.UserLogin(user_phone, user_password, WMPreference.INSTANCE.get_checkClicked(mContext), WMPreference.INSTANCE.get_UniqueId(mContext));

        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    Log.d("Stringjwgerje", "" + registration);
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


    private void fetchZipCode() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ZipCodemodel> call = redditAPI.GetZipCodeList();
        call.enqueue(new Callback<ZipCodemodel>() {

            @Override
            public void onResponse(Call<ZipCodemodel> call, retrofit2.Response<ZipCodemodel> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    zipCodemodel = response.body();
                    if (zipCodemodel.getAck() == 1) {
                        if (zipCodemodel.getZipData().size() > 0) {
                            list_text.clear();
                            for (int i = 0; i < zipCodemodel.getZipData().size(); i++) {
                                list_text.add(zipCodemodel.getZipData().get(i).getAvailableZipcode());
                            }
                            showCustomDialog();
                        }
                    } else {
                        Utility.INSTANCE.showToastShort(mContext, zipCodemodel.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ZipCodemodel> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        FlexboxLayout container = dialogView.findViewById(R.id.v_container);
        Button btn_proceed = dialogView.findViewById(R.id.btn_proceed);
        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);


        inflatelayout(container);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent i = new Intent(mContext, RegisterActivity.class);
                startActivity(i);
            }
        });

    }


    private void inflatelayout(FlexboxLayout container) {
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(5, 5, 5, 5);
        for (int i = 0; i < list_text.size(); i++) {
            final TextView tv = new TextView(getApplicationContext());
            tv.setText(list_text.get(i));
            tv.setHeight(70);
            tv.setTextSize(16.0f);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setBackground(getResources().getDrawable(R.drawable.rounded_corner_flex));
            tv.setId(i + 1);
            tv.setLayoutParams(buttonLayoutParams);
            tv.setTag(i);
            tv.setPadding(20, 10, 20, 10);
            container.addView(tv);
        }

    }


}
