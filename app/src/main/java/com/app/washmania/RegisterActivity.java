package com.app.washmania;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.washmania.model.LoginResponse;
import com.app.washmania.model.RegistrationResponse;
import com.app.washmania.model.ZipCodeVerify;
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

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    Context mContext;

    Button btn_register;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    RegistrationResponse registration;
    LoginResponse loginResponse;
    ZipCodeVerify zipCodeVerify;
    EditText et_firstname;
    EditText et_lastname;
    EditText et_phoneno;
    EditText et_email;
    EditText et_password;
    EditText et_confirm_password;
    EditText et_address;
    EditText et_state;
    EditText et_city;
    EditText et_zipcode;

    String user_firstname;
    String user_lastname;
    String user_phoneno;
    String user_email;
    String user_password;
    String user_confirm_password;
    String user_address;
    String user_state;
    String user_city;
    String user_zipcode;
    private LocationManager locationManager;
    String provider = "";
    LocationListener locationListener;
    double laitutude, longitude;
    Location location;
    Geocoder geocoder;
    List<Address> addresses;
    Button tv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        locationListener = this;

        initViews();
        init();
    }

    private void initViews() {
        tv_location = findViewById(R.id.tv_location);
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_phoneno = findViewById(R.id.et_phoneno);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_address = findViewById(R.id.et_address);
        et_state = findViewById(R.id.et_state);
        et_city = findViewById(R.id.et_city);
        et_zipcode = findViewById(R.id.et_zipcode);
        tv_location.setOnClickListener(this);

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
    }


    @Override
    public void onClick(View v) {
        if (v == btn_register) {
            if (et_firstname.getText().toString().isEmpty()) {
                Utility.INSTANCE.INSTANCE.showToastShort(mContext, "Please Enter First Name");
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

            if (et_password.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Password");
                return;
            }

            if (et_confirm_password.getText().toString().isEmpty()) {
                Utility.INSTANCE.showToastShort(mContext, "Please Enter Confirm Password");
                return;
            }
            if (!et_password.getText().toString().equals(et_confirm_password.getText().toString())) {
                Utility.INSTANCE.showToastShort(mContext, "Password And Confirm Password doesn't matches");
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
            user_password = et_password.getText().toString().trim();


            user_firstname = et_firstname.getText().toString().trim();
            user_lastname = et_lastname.getText().toString().trim();
            user_phoneno = et_phoneno.getText().toString().trim();
            user_email = et_email.getText().toString().trim();
            user_password = et_password.getText().toString().trim();
            user_confirm_password = et_confirm_password.getText().toString().trim();
            user_address = et_address.getText().toString().trim();
            user_state = et_state.getText().toString().trim();
            user_city = et_city.getText().toString().trim();
            user_zipcode = et_zipcode.getText().toString().trim();

            verifyUser();

        } else if (v == tv_location) {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {

                            if (location != null) {

                                onLocationChanged(location);

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

                            } else {
                                Toast.makeText(mContext, "Turn On Location...", Toast.LENGTH_SHORT).show();
                                //latituteField.setText("Location not available");
                                // longitudeField.setText("Location not available");
                            }
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

    private void verifyUser() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Log.d("Values", "&fname" + user_firstname + "&lname" + user_lastname + "&phone" + user_phoneno + "&email" + user_email + "&password" + user_password + "&confirm_password" + user_confirm_password + "&address" + user_address + "&state" + user_state + "&city" + user_city + "&zip" + user_zipcode + "&flag" + WMPreference.INSTANCE.get_checkClicked(mContext) + "&unique_id" + WMPreference.INSTANCE.get_UniqueId(mContext));
        Call<RegistrationResponse> call = redditAPI.UserRegistration(user_firstname, user_lastname, user_phoneno, user_email, user_password, user_confirm_password, user_address, user_state, user_city, user_zipcode, WMPreference.INSTANCE.get_checkClicked(mContext), WMPreference.INSTANCE.get_UniqueId(mContext));
        call.enqueue(new Callback<RegistrationResponse>() {

            @Override
            public void onResponse(Call<RegistrationResponse> call, retrofit2.Response<RegistrationResponse> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    pDialog.dismiss();
                    if (registration.getAck() == 1) {
                        WMPreference.INSTANCE.set_userId(mContext, registration.getUserId());
                        Intent intent = new Intent(mContext, OTPActivity.class);
                        startActivity(intent);
                    } else {
                        Utility.INSTANCE.showToastShort(mContext, registration.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void LoginUser() {
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<LoginResponse> call = redditAPI.UserLogin(user_phoneno, user_password, WMPreference.INSTANCE.get_checkClicked(mContext), WMPreference.INSTANCE.get_UniqueId(mContext));
        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    loginResponse = response.body();
                    if (loginResponse.getAck() == 1) {
                        if (loginResponse.getLoginData().size() > 0) {
                            WMPreference.INSTANCE.set_firstuserName(mContext, loginResponse.getLoginData().get(0).getFname());
                            WMPreference.INSTANCE.set_lastName(mContext, loginResponse.getLoginData().get(0).getLname());
                            WMPreference.INSTANCE.set_userEmail(mContext, loginResponse.getLoginData().get(0).getEmail());
                            WMPreference.INSTANCE.set_userPhone(mContext, loginResponse.getLoginData().get(0).getPhone());
                            WMPreference.INSTANCE.set_userId(mContext, loginResponse.getLoginData().get(0).getUserId());

                            WMPreference.INSTANCE.set_address(mContext, loginResponse.getLoginData().get(0).getAddress());
                            WMPreference.INSTANCE.set_city(mContext, loginResponse.getLoginData().get(0).getCity());
                            WMPreference.INSTANCE.set_state(mContext, loginResponse.getLoginData().get(0).getState());
                            WMPreference.INSTANCE.set_Zip(mContext, loginResponse.getLoginData().get(0).getZip());


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

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
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


    @Override
    public void onLocationChanged(Location location) {
        laitutude = (location.getLatitude());
        longitude = (location.getLongitude());
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
}
