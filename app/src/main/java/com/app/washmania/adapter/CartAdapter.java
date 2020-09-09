package com.app.washmania.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.app.washmania.ProductCart;
import com.app.washmania.R;
import com.app.washmania.model.CartDeleteAction;
import com.app.washmania.model.MyCart;
import com.app.washmania.others.ConnectionDetector;
import com.app.washmania.others.Utility;
import com.app.washmania.others.WMPreference;
import com.app.washmania.retrofit.api.ApiServices;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private List<MyCart.CartDatum> moviesList;
    Context mContext;
    private int amount = 0;
    MyCart.CartDatum movie;
    MyViewHolder holder1;
    ProgressDialog progressDialog;
    String cartstringjson = "";
    String cart_id = "";
    ConnectionDetector cd;
    CartDeleteAction cartDeleteAction;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_productname, tv_size, tv_producttype, tv_price, tv_packetsize, tv_position;
        ImageView iv_sub, iv_add;
        TextView et_qty;
        ImageView iv_product;
        ProgressBar progressbar;
        TextView tv_id;
        Button btn_delete;

        public MyViewHolder(View view) {
            super(view);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
            tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            tv_position = (TextView) view.findViewById(R.id.tv_position);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            iv_sub = (ImageView) view.findViewById(R.id.iv_sub);
            iv_add = (ImageView) view.findViewById(R.id.iv_add);
            et_qty = view.findViewById(R.id.et_qty);
            iv_product = (ImageView) view.findViewById(R.id.iv_product);
            progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
            btn_delete = view.findViewById(R.id.btn_delete);
            btn_delete.setVisibility(View.VISIBLE);
            //btn_update = view.findViewById(R.id.btn_update);
            tv_packetsize = view.findViewById(R.id.tv_packetsize);

            // this.myCustomEditTextListener = myCustomEditTextListener;
            //et_qty.addTextChangedListener(myCustomEditTextListener);
        }
    }

    public CartAdapter(List<MyCart.CartDatum> moviesList, Context mContext) {
        this.moviesList = moviesList;
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        cd = new ConnectionDetector(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder1 = holder;
        movie = moviesList.get(position);
        holder.tv_productname.setText(movie.getDress_name());
        Log.e("DressName",movie.getDress_name());
        holder.tv_id.setText(movie.getCartId());
        holder.et_qty.setText(String.valueOf(movie.quantity));

        holder.tv_price.setText("\u20B9" + " " + String.valueOf(movie.unitPrice));

        holder.tv_position.setText(movie.getCartId());

        try {
            Picasso.with(mContext)
                    .load(movie.getDress_photo())
                    .into(holder.iv_product, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.iv_product.setImageResource(R.mipmap.ic_launcher);
                        }
                    });
        } catch (Exception e) {
        }

        try {
            if (!holder.et_qty.getText().equals("0") && !holder.et_qty.getText().toString().isEmpty()) {
                amount = Integer.parseInt(holder.et_qty.getText().toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("You want to remove this item.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (cd.isConnected()) {
                                    dialog.dismiss();
                                    deletefromcart(holder.tv_position.getText().toString().trim());
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


        holder.iv_sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cd.isConnected()) {
                    if (holder.iv_sub.isPressed()) {
                        if (holder.et_qty.getText().toString().equals("0")) {
                            return;
                        }
                        if (!holder.et_qty.getText().toString().isEmpty()) {
                            amount = Integer.parseInt(holder.et_qty.getText().toString());
                        } else {
                            amount = 0;
                        }
                        if (amount > 0) {
                            amount -= 1;
                        }
                        if (amount == 0) {
                            amount = 1;
                        }
                        if (amount != 0) {
                        }
                        //Utility.INSTANCE.showToastShort(mContext, holder.tv_position.getText().toString().trim());
                        holder.et_qty.setText(String.valueOf(amount));
                        Log.d("Position-", "" + holder.tv_position.getText().toString());
                        updateCart(holder.tv_position.getText().toString().trim(), holder.et_qty.getText().toString().trim(), "0");
                    }
                } else {
                    Utility.INSTANCE.showToastShort(mContext, "No Internet Connection");
                }
            }
        });

        holder.iv_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cd.isConnected()) {
                    if (holder.iv_add.isPressed()) {
                        if (!holder.et_qty.getText().toString().isEmpty()) {
                            amount = Integer.parseInt(holder.et_qty.getText().toString());
                        } else {
                            amount = 0;
                        }
                        amount += 1;
                        holder.et_qty.setText(String.valueOf(amount));
                        if (amount != 0) {
                        }
                        Log.d("Position+", "" + holder.tv_position.getText().toString());
                        updateCart(holder.tv_position.getText().toString().trim(), holder.et_qty.getText().toString().trim(), "1");
                    }
                } else {
                    Utility.INSTANCE.showToastShort(mContext, "No Internet Connection");
                }
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
        Call<CartDeleteAction> call = redditAPI.GetCartDeleteAction(WMPreference.INSTANCE.get_userId(mContext), cart_id, WMPreference.INSTANCE.get_UniqueId(mContext));
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        ((ProductCart) mContext).LoadCartProduct();
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


    private void updateCart(String cart_id, String quantity, String isCartAdd) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CartDeleteAction> call = redditAPI.UpdateMyCart(WMPreference.INSTANCE.get_userId(mContext), cart_id, WMPreference.INSTANCE.get_UniqueId(mContext), "1", isCartAdd);
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                //Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        Utility.INSTANCE.showToastShort(mContext, cartDeleteAction.getMsg());
                        ((ProductCart) mContext).LoadCartProduct();
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
}
