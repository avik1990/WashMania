package com.app.washmania.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.washmania.OrderDetailsActivity;
import com.app.washmania.R;
import com.app.washmania.fragments.FragmentAllOrder;
import com.app.washmania.model.CartDeleteAction;
import com.app.washmania.model.Myorders;
import com.app.washmania.model.Reviewmodel;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class MyOrderAdapterAll extends RecyclerView.Adapter<MyOrderAdapterAll.MyViewHolder> {
    private List<Myorders.OrderDatum> moviesList;
    Context mContext;
    private int amount = 0;
    Myorders.OrderDatum movie;
    MyViewHolder holder1;
    ProgressDialog progressDialog;
    ConnectionDetector cd;
    CartDeleteAction cartDeleteAction;
    Reviewmodel reviewmodel;
    FragmentAllOrder fragall;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOrderId, tvPrice, tvBookedOn, tvPaymentMode, tvPaymentStatus, tvPickuppBoy, tvDeliveryStatus, tvOrderIds;
        Button btn_cancel, btn_review, btn_viewreview;
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
            btn_review = view.findViewById(R.id.btn_review);
            btn_viewreview = view.findViewById(R.id.btn_viewreview);
            tvCartId = view.findViewById(R.id.tvCartId);
            tvOrderIds = view.findViewById(R.id.tvOrderIds);
            llInfo = view.findViewById(R.id.llInfo);
        }
    }

    public MyOrderAdapterAll(List<Myorders.OrderDatum> moviesList, Context mContext, FragmentAllOrder fragall) {
        this.moviesList = moviesList;
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        cd = new ConnectionDetector(mContext);
        this.fragall = fragall;
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

        if (movie.getDeliveryStatus().equalsIgnoreCase("Pending")) {
            holder.btn_cancel.setVisibility(View.VISIBLE);
        } else {
            holder.btn_cancel.setVisibility(View.GONE);
        }

        Log.e("Values", movie.getReviewPosted());
        if (movie.getReviewPosted().equalsIgnoreCase("YES")) {
            holder.btn_review.setVisibility(View.GONE);
            holder.btn_viewreview.setVisibility(View.VISIBLE);
        } else {
            holder.btn_review.setVisibility(View.VISIBLE);
            holder.btn_viewreview.setVisibility(View.GONE);
        }

        holder.tvOrderIds.setText(movie.getOrderId());
        holder.llInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, OrderDetailsActivity.class);
                i.putExtra("OrderId", holder.tvOrderIds.getText().toString().trim());
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
                                    openCancelDialog(holder.tvCartId.getText().toString().trim());
                                } else {
                                    Utility.INSTANCE.showToastShort(mContext, "No Internet Connection");
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        holder.btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.order_rating_popup, null);
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(view1);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                RatingBar rating = dialog.findViewById(R.id.rating);
                final EditText etComments = dialog.findViewById(R.id.etComments);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnReview = dialog.findViewById(R.id.btnReview);
                final int rate = rating.getNumStars();


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rate == 0) {
                            Utility.INSTANCE.showToastShort(mContext, "Enter Rating");
                            return;
                        }
                        if (etComments.getText().toString().trim().isEmpty()) {
                            Utility.INSTANCE.showToastShort(mContext, "Enter Comments");
                            return;
                        }

                        if (cd.isConnected()) {
                            PostRatings(holder.tvOrderIds.getText().toString().trim(), etComments.getText().toString().trim(), String.valueOf(rate));
                            dialog.dismiss();
                        }


                    }
                });

                dialog.show();
            }
        });
        holder.btn_viewreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnected()) {
                    ViewRatings(holder.tvOrderIds.getText().toString().trim());
                }

            }
        });
    }

    private void openCancelDialog(final String cart_id) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.order_comment_popup, null);
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(view1);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        final EditText etComments = dialog.findViewById(R.id.etComments);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnReview = dialog.findViewById(R.id.btnReview);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etComments.getText().toString().trim().isEmpty()) {
                    Utility.INSTANCE.showToastShort(mContext, "Enter Comments");
                    return;
                }

                if (cd.isConnected()) {
                    cancelfromcartItem(cart_id,etComments.getText().toString().trim());
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    private void cancelfromcartItem(String cart_id,String comments) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CartDeleteAction> call = redditAPI.GetCancelOrder(WMPreference.INSTANCE.get_userId(mContext), cart_id, WMPreference.INSTANCE.get_UniqueId(mContext),comments);
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        fragall.parsejson();
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


    private void PostRatings(String orderId, String comments, String rating) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CartDeleteAction> call = redditAPI.PostReview(WMPreference.INSTANCE.get_userId(mContext), orderId, comments, rating);
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        fragall.parsejson();
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
    }


    private void ViewRatings(String orderId) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Reviewmodel> call = redditAPI.ViewReview(WMPreference.INSTANCE.get_userId(mContext), orderId);
        call.enqueue(new Callback<Reviewmodel>() {

            @Override
            public void onResponse(Call<Reviewmodel> call, retrofit2.Response<Reviewmodel> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    reviewmodel = response.body();
                    progressDialog.dismiss();
                    if (reviewmodel.getReviewData().getAck().equals("1")) {
                        //Utility.INSTANCE.INSTANCE.showToastShort(mContext, reviewmodel.getReviewData().getMsg());
                        openViewratingDialog(reviewmodel.getReviewData().getRate(), reviewmodel.getReviewData().getReview());
                    } else {
                        Utility.INSTANCE.showToastShort(mContext, reviewmodel.getReviewData().getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<Reviewmodel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void openViewratingDialog(String rate, String comments) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.order_ratingview_popup, null);
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(view1);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        RatingBar rating = dialog.findViewById(R.id.rating);
        final TextView etComments = dialog.findViewById(R.id.tvComments);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        etComments.setText(comments);
        rating.setRating(Float.parseFloat(rate));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
