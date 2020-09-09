package com.app.washmania;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.alexandroid.gps.GpsStatusDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChangeLocationActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, GpsStatusDetector.GpsStatusDetectorCallBack {

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
    String user_landmark = "";
    CircularTextView tv_cartcount;
    ImageView iv_cart, btn_back, btn_menu;
    Button fabLocation;
    private LocationManager locationManager;
    String provider = "";
    LocationListener locationListener;
    double laitutude, longitude;
    Location location;
    Geocoder geocoder;
    List<Address> addresses;
    EditText et_landmark;
    private GpsStatusDetector mGpsStatusDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addloc);
        mContext = this;
        locationListener = this;
        cd = new ConnectionDetector(mContext);
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();
        init();
        getUserDetails();
    }


    @SuppressLint("MissingPermission")
    private void init() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);
        // Initialize the location fields
        if (location != null) {
            // System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            //latituteField.setText("Location not available");
            // longitudeField.setText("Location not available");
        }

    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
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
        et_landmark = findViewById(R.id.et_landmark);
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_phoneno = findViewById(R.id.et_phoneno);
        et_email = findViewById(R.id.et_email);
        et_address = findViewById(R.id.et_address);
        et_state = findViewById(R.id.et_state);
        et_city = findViewById(R.id.et_city);
        et_zipcode = findViewById(R.id.et_zipcode);
        fabLocation = findViewById(R.id.fabLocation);

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
        fabLocation.setOnClickListener(this);

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

        ImageView iv_phone = findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Utility.INSTANCE.INSTANCE.CallContactNo(mContext);
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(WMPreference.INSTANCE.get_Cartount(mContext));
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }


    @Override
    public void onClick(View v) {
        if (v == btn_register) {
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
            user_landmark = et_landmark.getText().toString().trim();

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
        } else if (v == fabLocation) {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            //Utility.INSTANCE.showToastShort(mContext,"Hewllw");
                           // if (location != null) {
                                try {
                                    onLocationChanged(location);
                                }catch (Exception e){

                                }
                                try {
                                    geocoder = new Geocoder(mContext, Locale.getDefault());
                                    addresses = geocoder.getFromLocation(laitutude, longitude, 1);
                                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = "";

                                    try {
                                        postalCode = addresses.get(0).getPostalCode();
                                        if (postalCode.isEmpty()) {
                                            for (int i = 0; i < 5; i++) {
                                                if (!addresses.get(i).getPostalCode().isEmpty()) {
                                                    postalCode = addresses.get(i).getPostalCode();
                                                    break;
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }

                                    et_address.setText(address);
                                    et_state.setText(state);
                                    et_city.setText(city);
                                    et_zipcode.setText(postalCode);
                                    //String ad = address + " " + city + " " + state + " " + country + " " + postalCode;
                                    //Log.e("vall", ad);
                                } catch (Exception e) {

                                }

                           /* } else {
                                //latituteField.setText("Location not available");
                                // longitudeField.setText("Location not available");
                            }*/
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                                // open device settings when the permission is
                                // denied permanently
                                openSettings();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
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
        et_landmark.setText(registration.getUserData().get(0).getLandmark());

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

        Call<BaseResponse> call = redditAPI.UserUpdateDetails(WMPreference.INSTANCE.get_userId(mContext), user_firstname, user_lastname, user_phoneno, user_email, user_address, user_state, user_city, user_zipcode, user_landmark);
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    baseResponse = response.body();
                    if (baseResponse.getAck() == 1) {
                        Utility.INSTANCE.showToastShort(mContext, "Location Updated Successfully");
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

    @Override
    public void onLocationChanged(Location location) {
        laitutude = (location.getLatitude());
        longitude = (location.getLongitude());
        // Utility.INSTANCE.showToastShort(mContext, "" + laitutude + " : " + longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
    }

    @Override
    public void onGpsSettingStatus(boolean enabled) {
    }

    @Override
    public void onGpsAlertCanceledByUser() {

    }


}
