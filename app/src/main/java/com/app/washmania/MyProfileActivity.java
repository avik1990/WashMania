package com.app.washmania;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.app.washmania.model.BaseResponse;
import com.app.washmania.model.MyProfile;
import com.app.washmania.model.ZipCodeVerify;
import com.app.washmania.others.CircularTextView;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    Button btn_register;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    MyProfile registration;
    BaseResponse baseResponse;
    ZipCodeVerify zipCodeVerify;
    EditText et_firstname;
    EditText et_lastname;
    EditText et_phoneno;
    EditText et_email;
    EditText et_address;
    EditText et_state;
    EditText et_city;
    EditText et_zipcode;

    String user_firstname;
    String user_lastname;
    String user_phoneno;
    String user_email;
    String user_address;
    String user_state;
    String user_city;
    String user_zipcode;

    CircularTextView tv_cartcount;
    ImageView iv_cart, btn_back, btn_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();
        getUserDetails();
    }

    private void initViews() {
        ImageView iv_home = findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DashboardActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_phoneno = findViewById(R.id.et_phoneno);
        et_email = findViewById(R.id.et_email);
        et_address = findViewById(R.id.et_address);
        et_state = findViewById(R.id.et_state);
        et_city = findViewById(R.id.et_city);
        et_zipcode = findViewById(R.id.et_zipcode);

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

        et_zipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 6) {
                    VerifyZipCodeparsejson(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        ImageView iv_phone=findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Utility.INSTANCE.INSTANCE.CallContactNo(mContext);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(WMPreference.INSTANCE.get_Cartount(mContext));
    }


    @Override
    public void onClick(View v) {
        if (v == btn_register) {
            if (et_firstname.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter First Name");
                return;
            }

            if (et_lastname.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Last Name");
                return;
            }

            if (et_phoneno.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Phone No.");
                return;
            }

            if (et_email.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Your Email");
                return;
            }

            if (!Utility.INSTANCE.isValidEmail(et_email.getText().toString())) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Valid Email");
                return;
            }


            if (et_address.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Address");
                return;
            }

            if (et_state.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter State");
                return;
            }

            if (et_city.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter City");
                return;
            }

            if (et_city.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter City");
                return;
            }

            if (et_zipcode.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter ZipCode");
                return;
            }

            user_email = et_email.getText().toString().trim();
            user_firstname = et_firstname.getText().toString().trim();
            user_lastname = et_lastname.getText().toString().trim();
            user_phoneno = et_phoneno.getText().toString().trim();
            user_email = et_email.getText().toString().trim();
            user_address = et_address.getText().toString().trim();
            user_state = et_state.getText().toString().trim();
            user_city = et_city.getText().toString().trim();
            user_zipcode = et_zipcode.getText().toString().trim();

            UpdateUserDetails();
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

    private void getUserDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);


        Call<MyProfile> call = redditAPI.GetMYProfile(WMPreference.INSTANCE.get_userId(mContext));
        call.enqueue(new Callback<MyProfile>() {

            @Override
            public void onResponse(Call<MyProfile> call, retrofit2.Response<MyProfile> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    if (registration.getAck() == 1) {
                        setData();
                    } else {

                        Utility.INSTANCE.showToastShort(mContext, registration.getMsg());
                    }
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MyProfile> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setData() {
        et_email.setText(registration.getUserData().get(0).getEmail());
        et_firstname.setText(registration.getUserData().get(0).getFname());
        et_lastname.setText(registration.getUserData().get(0).getLname());
        et_phoneno.setText(registration.getUserData().get(0).getPhone());
        et_address.setText(registration.getUserData().get(0).getAddress());
        et_state.setText(registration.getUserData().get(0).getState());
        et_city.setText(registration.getUserData().get(0).getCity());
        et_zipcode.setText(registration.getUserData().get(0).getZip());
    }


    private void VerifyZipCodeparsejson(final String zipcode) {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ZipCodeVerify> call = redditAPI.VerifyZipCode(zipcode);
        call.enqueue(new Callback<ZipCodeVerify>() {

            @Override
            public void onResponse(Call<ZipCodeVerify> call, retrofit2.Response<ZipCodeVerify> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    zipCodeVerify = response.body();
                    if (zipCodeVerify.getAck().equals("1")) {
                        btn_register.setEnabled(true);
                        //Utility.INSTANCE.showToastShort(mContext, zipCodeVerify.getMsg());
                    } else {
                        btn_register.setEnabled(false);
                        Utility.INSTANCE.showToastShort(mContext, zipCodeVerify.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ZipCodeVerify> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void UpdateUserDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<BaseResponse> call = redditAPI.UserUpdateDetails(WMPreference.INSTANCE.get_userId(mContext), user_firstname, user_lastname, user_phoneno, user_email, user_address, user_state, user_city, user_zipcode);
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    baseResponse = response.body();
                    if (baseResponse.getAck() == 1) {
                        Utility.INSTANCE.showToastShort(mContext, baseResponse.getMsg());
                    } else {
                        Utility.INSTANCE.showToastShort(mContext, baseResponse.getMsg());
                    }
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
