package com.app.washmania.adapter;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.washmania.R;
import com.app.washmania.model.MyCart;
import com.app.washmania.others.ConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.MyViewHolder> {
    private HashMap<String, List<MyCart.CartDatum>> moviesList;
    Context mContext;
    MyViewHolder holder1;
    ProgressDialog progressDialog;
    ConnectionDetector cd;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    HashMap<String, List<MyCart.CartDatum>> hashMap;
    //List<MyCart.CartDatum> listmycart = new ArrayList<>();
   // List<String> listKeys = new ArrayList<>();
    //List<List<MyCart.CartDatum>> listValues = new ArrayList<>();
    //List<Double> listSum = new ArrayList<>();
    //List<MyCart.CartDatum> listValues1 = new ArrayList<>();

    public CheckoutAdapter(HashMap<String, List<MyCart.CartDatum>> moviesList, Context mContext,List<MyCart.CartDatum> listValues1) {
        this.moviesList = moviesList;
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        cd = new ConnectionDetector(mContext);
        this.hashMap = moviesList;
        for (int i = 0; i < moviesList.size(); i++) {
            expandState.append(i, false);
        }

        /*for (Map.Entry<String, List<MyCart.CartDatum>> entry : hashMap.entrySet()) {
            //System.out.println(entry.getKey()+" : "+entry.getValue());
            Log.e("VAluess", entry.getKey() + " : " + entry.getValue().size());
            listKeys.add(entry.getKey());
            for (int j = 0; j < listKeys.size(); j++) {
                listValues.add(entry.getValue());
            }
        }*/




        /*for (int i = 0; i < listKeys.size(); i++) {
            double sum = 0;
            for (int k = 0; k < listValues.get(i).size(); k++) {
                sum = sum + Double.parseDouble(listValues.get(i).get(k).getUnitPrice());
            }
            listSum.add(sum);
            Log.e("sum", "" + sum);
        }*/

    }

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }


    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, final int i) {
        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout
        if (expandableLayout.getVisibility() == View.VISIBLE) {
            createRotateAnimator(buttonLayout, 180f, 0f).start();
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        } else {
            createRotateAnimator(buttonLayout, 0f, 180f).start();
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout buttonLayout;
        LinearLayout expandableLayout;
        TextView tvCatName, tvPrice;

        public MyViewHolder(View view) {
            super(view);
            tvCatName = view.findViewById(R.id.tvCatName);
            buttonLayout = view.findViewById(R.id.button);
            tvPrice = view.findViewById(R.id.tvPrice);
            expandableLayout = view.findViewById(R.id.expandableLayout);
        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_orderdetails, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder1 = holder;
        holder.setIsRecyclable(false);
        final boolean isExpanded = expandState.get(position);
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout, holder.buttonLayout, position);
            }
        });

        //holder.tvCatName.setText(listKeys.get(position));
        holder.tvCatName.setText(getHashMapKeyFromIndex(hashMap, position));
        holder.expandableLayout.removeAllViews();
        double sum = 0;
        for (int i = 0; i < hashMap.get(getHashMapKeyFromIndex(hashMap, position)).size(); i++) {
            View sublayout = View.inflate(mContext, R.layout.explayout, null);
            TextView tv_productname = sublayout.findViewById(R.id.tv_productname);
            TextView tv_qty = sublayout.findViewById(R.id.tv_qty);
            TextView tv_price = sublayout.findViewById(R.id.tv_price);
            TextView tv_subtotal = sublayout.findViewById(R.id.tv_subtotal);

            tv_productname.setText(hashMap.get(getHashMapKeyFromIndex(hashMap, position)).get(i).getDress_name());
            tv_qty.setText(hashMap.get(getHashMapKeyFromIndex(hashMap, position)).get(i).getQuantity() + " pcs");
            tv_price.setText("\u20B9" + hashMap.get(getHashMapKeyFromIndex(hashMap, position)).get(i).getUnitPrice());
            tv_subtotal.setText("\u20B9" + hashMap.get(getHashMapKeyFromIndex(hashMap, position)).get(i).getSubtotal());
            sum = sum + Double.parseDouble(hashMap.get(getHashMapKeyFromIndex(hashMap, position)).get(i).getSubtotal());
            holder.tvPrice.setText("\u20B9" + " " +sum);
            holder.expandableLayout.addView(sublayout);
        }


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static String getHashMapKeyFromIndex(HashMap hashMap, int index) {

        String key = null;
        HashMap<String, Object> hs = hashMap;
        int pos = 0;
        for (Map.Entry<String, Object> entry : hs.entrySet()) {
            if (index == pos) {
                key = entry.getKey();
            }
            pos++;
        }
        return key;
    }
}
