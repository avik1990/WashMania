package com.app.washmania.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.washmania.R;
import com.app.washmania.adapter.ProductAdapter;
import com.app.washmania.model.ProductsMod;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentMen extends Fragment implements ProductAdapter.IntCartCount {

    View view;
    RecyclerView rv_recyclerview;
    SwipeRefreshLayout swipe_container;
    Context mContext;
    ProductsMod productsMod;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    String catId = "";
    onSomeEventListener someEventListener;
    int menCount = 0, womenCount = 0, kidsCount = 0;

    @Override
    public void returnCartCount(String count) {
        Utility.INSTANCE.showToastShort(mContext, "" + count);
    }

    public interface onSomeEventListener {
        void getCount(int menCount, int womenCount, int kidsCount);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmnet_men, container, false);
        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        catId = getArguments().getString("catId");

        initViews();

        if (cd.isConnected()) {
            parseJsonMen();
        } else {
            Utility.INSTANCE.showToastShort(mContext, getString(R.string.no_internet_msg));
        }

        return view;
    }

    private void initViews() {
        rv_recyclerview = view.findViewById(R.id.rv_recyclerview);
    }


    private void parseJsonMen() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<ProductsMod> call = redditAPI.GetProductListResponse(catId, "Men", WMPreference.INSTANCE.get_userId(mContext), WMPreference.INSTANCE.get_UniqueId(mContext));
        call.enqueue(new Callback<ProductsMod>() {

            @Override
            public void onResponse(Call<ProductsMod> call, retrofit2.Response<ProductsMod> response) {
                Log.d("String", "" + response);
                productsMod = response.body();
                if (productsMod.getAck() == 1) {
                    menCount = productsMod.getDressData().size();
                    if (productsMod.getDressData().size() > 0) {
                        inflateAdapter();
                    }
                } else {
                    menCount = 0;
                    Utility.INSTANCE.showToastShort(mContext, productsMod.getMsg());
                }

                //parseJsonWomen();
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProductsMod> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void parseJsonWomen() {
        //pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<ProductsMod> call = redditAPI.GetProductListResponse(catId, "Women", WMPreference.INSTANCE.get_userId(mContext), WMPreference.INSTANCE.get_UniqueId(mContext));
        call.enqueue(new Callback<ProductsMod>() {

            @Override
            public void onResponse(Call<ProductsMod> call, retrofit2.Response<ProductsMod> response) {
                Log.d("String", "" + response);
                productsMod = response.body();
                if (productsMod.getAck() == 1) {
                    womenCount = productsMod.getDressData().size();
                } else {
                    womenCount = 0;
                }
                parseJsonKids();
            }

            @Override
            public void onFailure(Call<ProductsMod> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void parseJsonKids() {
        //pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<ProductsMod> call = redditAPI.GetProductListResponse(catId, "Kids", WMPreference.INSTANCE.get_userId(mContext), WMPreference.INSTANCE.get_UniqueId(mContext));
        call.enqueue(new Callback<ProductsMod>() {

            @Override
            public void onResponse(Call<ProductsMod> call, retrofit2.Response<ProductsMod> response) {
                Log.d("String", "" + response);
                productsMod = response.body();
                if (productsMod.getAck() == 1) {
                    kidsCount = productsMod.getDressData().size();
                } else {
                    kidsCount = 0;
                }
                someEventListener.getCount(menCount, womenCount, kidsCount);
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProductsMod> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void inflateAdapter() {
        ProductAdapter ca = new ProductAdapter(productsMod.getDressData(), mContext, this);
        rv_recyclerview.setAdapter(ca);
    }
}