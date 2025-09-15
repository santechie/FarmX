package com.ascentya.AsgriV2.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Interfaces_Class.Mycart_Interface;
import com.ascentya.AsgriV2.Models.Address_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.buysell.Choose_Payment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Address_Adapter extends RecyclerView.Adapter<Address_Adapter.ViewHolder> {

    private List<Address_Model> items = new ArrayList<>();
    private Context ctx;

    Dialog dialog;


    String user_id;
    ViewDialog viewDialog;
    String cat_id;
    private Mycart_Interface cart_interface;

    public Address_Adapter(Context context, List<Address_Model> items, String user_id, ViewDialog viewDialog, Mycart_Interface cart_interface) {
        this.items = items;
        this.ctx = context;
        this.user_id = user_id;
        this.cart_interface = cart_interface;
        this.viewDialog = viewDialog;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView nick, shipping_name, shipping_street, shipping_address, pincode;
        public ImageView icons;
        ProgressBar product_cover_loader;
        Button select;
        ImageView delete;


        public ViewHolder(View v) {
            super(v);

            nick = (TextView) v.findViewById(R.id.nick);
            select = (Button) v.findViewById(R.id.select);
            pincode = (TextView) v.findViewById(R.id.pincode);
            icons = (ImageView) v.findViewById(R.id.product_cover);
            product_cover_loader = (ProgressBar) v.findViewById(R.id.product_cover_loader);
            shipping_street = (TextView) v.findViewById(R.id.shipping_street);
            shipping_name = (TextView) v.findViewById(R.id.shipping_name);
            delete = (ImageView) v.findViewById(R.id.delete);
            shipping_address = (TextView) v.findViewById(R.id.shipping_address);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_row, parent, false);

        vh = new ViewHolder(v);

        return vh;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder view = (ViewHolder) holder;
            final Address_Model Position_Object = items.get(position);

            holder.nick.setText(Position_Object.getMarkus());
            holder.shipping_name.setText(Position_Object.getFlatnum());
            holder.shipping_street.setText(Position_Object.getStreet_name());
            holder.shipping_address.setText(Position_Object.getCity());
            holder.pincode.setText(Position_Object.getPincode());


            view.select.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    Intent i = new Intent(ctx, Choose_Payment.class);
                    ctx.startActivity(i);
                    ((Activity) ctx).finish();
                }
            });

            view.itemView.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    Intent i = new Intent(ctx, Choose_Payment.class);
                    ctx.startActivity(i);
                    ((Activity) ctx).finish();
                }
            });


            view.delete.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View view) {
                    deleteAddress(Position_Object.getId());

                }
            });

//            view.product_card_Btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View vie) {
//                    add_tocart(Position_Object, view);
//
//                }
//            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<Address_Model> list) {
        items = list;
        notifyDataSetChanged();
    }


    public void deleteAddress(String id) {
        viewDialog.showDialog();
        AndroidNetworking.post(Webservice.delete_address)
                .addUrlEncodeFormBodyParameter("add_id", id)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        cart_interface.mycart("12", true);

                    } else {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    viewDialog.hideDialog();
                }
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();


            }
        });

    }


}
