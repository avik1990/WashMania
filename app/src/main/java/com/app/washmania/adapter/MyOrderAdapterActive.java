package com.app.washmania.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.washmania.OrderDetailsActivity;
import com.app.washmania.R;
import com.app.washmania.fragments.FragmentActiveOrder;
import com.app.washmania.fragments.FragmentAllOrder;
import com.app.washmania.fragments.FragmentPastOrder;
import com.app.washmania.model.CartDeleteAction;
import com.app.washmania.model.Myorders;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;
import com.app.washmania.retrofit.api.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class MyOrderAdapterActive extends RecyclerView.Adapter<MyOrderAdapterActive.MyViewHolder> {
    private List<Myorders.OrderDatum> moviesList;
    Context mContext;
    private int amount = 0;
    Myorders.OrderDatum movie;
    MyViewHolder holder1;
    ProgressDialog progressDialog;
      ConnectionDetector cd;
    CartDeleteAction cartDeleteAction;
    FragmentActiveOrder fragmentActiveOrder;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOrderId, tvPrice, tvBookedOn, tvPaymentMode, tvPaymentStatus, tvPickuppBoy, tvDeliveryStatus,tvOrderIds;
        Button btn_cancel;
        TextView tvCartId;
        LinearLayout llInfo;

        public MyViewHolder(View view) {
            super(view);
            tvOrderId = view.findViewById(R.id.tvOrderId);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvBookedOn = view.findViewById(R.id.tvBookedOn);
            tvPaymentMode = view.findViewById(R.id.tvPaymentMode);
            tvPaymentStatus = view.findViewById(R.id.tvPaymentStatus);
            tvPickuppBoy = view.findViewById(R.id.tvPickuppBoy);
            tvDeliveryStatus = view.findViewById(R.id.tvDeliveryStatus);
            btn_cancel = view.findViewById(R.id.btn_cancel);
            tvCartId = view.findViewById(R.id.tvCartId);
            btn_cancel.setVisibility(View.VISIBLE);
            tvOrderIds= view.findViewById(R.id.tvOrderIds);
            llInfo = view.findViewById(R.id.llInfo);
        }
    }

    public MyOrderAdapterActive(List<Myorders.OrderDatum> moviesList, Context mContext, FragmentActiveOrder fragmentActiveOrder) {
        this.moviesList = moviesList;
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        cd = new ConnectionDetector(mContext);
        this.fragmentActiveOrder = fragmentActiveOrder;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_myorders, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder1 = holder;
        movie = moviesList.get(position);
        holder.tvOrderId.setText("Order Id: " + movie.getOrderId());
        holder.tvPrice.setText("\u20B9" + " " + movie.getTotalPrice());
        holder.tvBookedOn.setText("Booked On: " + movie.getOrderDate());
        holder.tvPaymentMode.setText("Payment Mode: " + movie.getPaymentMode());
        holder.tvPaymentStatus.setText("Payment Status: " + movie.getPaymentStatus());
        holder.tvPickuppBoy.setText("Pick up boy: " + movie.getPickupBoy());
        holder.tvDeliveryStatus.setText("Delivery Status: " + movie.getDeliveryStatus());
        holder.tvCartId.setText(movie.getOrderId());


        holder.tvOrderIds.setText(movie.getOrderId());
        holder.llInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext, OrderDetailsActivity.class);
                i.putExtra("OrderId",holder.tvOrderIds.getText().toString().trim());
                mContext.startActivity(i);
            }
        });

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("You want to cancel this order.")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (cd.isConnected()) {
                                    dialog.dismiss();
                                    deletefromcart(holder.tvCartId.getText().toString().trim());
                                } else {
                                    Utility.INSTANCE.showToastShort(mContext, "No Internet Connection");
                                }
                            }
                        })

                        //A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    private void deletefromcart(String cart_id) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CartDeleteAction> call = redditAPI.GetCancelOrder(WMPreference.INSTANCE.get_userId(mContext), cart_id, WMPreference.INSTANCE.get_UniqueId(mContext));
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        fragmentActiveOrder.parsejson();
                    } else {
                        Utility.INSTANCE.showToastShort(mContext, cartDeleteAction.getMsg());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    /*private void addTocart(String dressId, String quantity, String isCartAdd) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CartDeleteAction> call = redditAPI.AddToCart(WMPreference.INSTANCE.get_userId(mContext), WMPreference.INSTANCE.get_UniqueId(mContext), quantity, isCartAdd, dressId);
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        Utility.INSTANCE.INSTANCE.showToastShort(mContext, cartDeleteAction.getMsg());
                    } else {
                        Utility.INSTANCE.showToastShort(mContext, cartDeleteAction.getMsg());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }*/
}
