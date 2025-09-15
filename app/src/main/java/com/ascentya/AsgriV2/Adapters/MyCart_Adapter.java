package com.ascentya.AsgriV2.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ascentya.AsgriV2.Interfaces_Class.Mycart_Interface;
import com.ascentya.AsgriV2.Models.FarmXBuy_Model;
import com.ascentya.AsgriV2.Models.SellCat_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.CartItemUtils;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyCart_Adapter extends RecyclerView.Adapter<MyCart_Adapter.ViewHolder> {

    private Context ctx;
    String lang_id;
    Dialog dialog;
    EditText product_title, product_disc, product_cost;
    NoDefaultSpinner product_category;
    CircleImageView attachment;
    String attachment_path;

    List<String> cat_data = new ArrayList<>();
    List<SellCat_Model> sellcat_data = new ArrayList<>();
    String user_id;
    ViewDialog viewDialog;
    String cat_id;
    private Mycart_Interface cart_interface;

    public MyCart_Adapter(Context context, String user_id, ViewDialog viewDialog, Mycart_Interface cart_interface) {
        this.ctx = context;
        this.user_id = user_id;
        this.cart_interface = cart_interface;
        this.viewDialog = viewDialog;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView product_title, cart_item_quantity, product_price_new, total_amount;
        public ImageView icons, cart_item_quantity_minusBtn, cart_item_quantity_plusBtn, delete;
        ProgressBar product_cover_loader;
        LinearLayout addquantity;


        public ViewHolder(View v) {
            super(v);

            product_title = (TextView) v.findViewById(R.id.product_title);
            icons = (ImageView) v.findViewById(R.id.product_cover);
            product_cover_loader = (ProgressBar) v.findViewById(R.id.product_cover_loader);
            product_price_new = (TextView) v.findViewById(R.id.product_price_new);
            cart_item_quantity_minusBtn = (ImageView) v.findViewById(R.id.cart_item_quantity_minusBtn);
            cart_item_quantity_plusBtn = (ImageView) v.findViewById(R.id.cart_item_quantity_plusBtn);
            cart_item_quantity = (TextView) v.findViewById(R.id.cart_item_quantity);
            addquantity = (LinearLayout) v.findViewById(R.id.addquantity);
            delete = (ImageView) v.findViewById(R.id.delete);
            total_amount = (TextView) v.findViewById(R.id.total_amount);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_row, parent, false);

        vh = new ViewHolder(v);

        return vh;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder view = (ViewHolder) holder;
            final FarmXBuy_Model Position_Object = cart_interface.getList().get(position);

            holder.product_price_new.setText(ProductUtils.getPrice(Position_Object.getProduct_price()));

            if (Integer.parseInt(Position_Object.getProduct_quantity()) == 0) {
                holder.addquantity.setVisibility(View.GONE);
            } else {
                holder.addquantity.setVisibility(View.VISIBLE);
                holder.cart_item_quantity.setText(Position_Object.getProduct_quantity());

            }

            if (!ProductUtils.isZero(Position_Object.getProduct_price())) {
                view.total_amount.setText("Rs." + getmultiply(Double.parseDouble(Position_Object.getProduct_quantity()), Double.parseDouble(Position_Object.getProduct_price())));
            } else {
                view.total_amount.setText("NA");
            }

            view.delete.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View view) {
                    delete_cart(Position_Object);

                }
            });

//            view.product_card_Btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View vie) {
//                    add_tocart(Position_Object, view);
//
//                }
//            });

            view.cart_item_quantity_plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vie) {

                    cart_quantityupdate(Position_Object, Integer.parseInt(view.cart_item_quantity.getText().toString()) + 1, view);

                }
            });
            view.cart_item_quantity_minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vie) {
                    if (Integer.parseInt(view.cart_item_quantity.getText().toString()) > 1) {
                        cart_quantityupdate(Position_Object,
                                Integer.parseInt(view.cart_item_quantity.getText().toString()) - 1, view);
                    }else {
                        delete_cart(Position_Object);
                    }
                }
            });

            view.product_title.setText(Position_Object.getProduct_name());


            Glide.with(ctx).load(Position_Object.getProduct_image()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    view.product_cover_loader.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    view.product_cover_loader.setVisibility(View.GONE);

                    return false;
                }
            }).into(holder.icons);
//            Glide.with(ctx).load(Webservice.buysellimagebase_path + Position_Object.getProduct_image()).error(ctx.getResources().getDrawable(R.drawable.news)).into(view.icons);


        }
    }

    @Override
    public int getItemCount() {
        return cart_interface.getList().size();
    }

    /*public void add_tocart(FarmXBuy_Model model, final ViewHolder holder) {
        AndroidNetworking.post(Webservice.add_cart)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .addUrlEncodeFormBodyParameter("cat_id", model.getCat_id())
                .addUrlEncodeFormBodyParameter("prod_id", model.getProduct_id())
                .addUrlEncodeFormBodyParameter("quantity", "1")
//                .addUrlEncodeFormBodyParameter("amount", model.getProduct_price())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        holder.addquantity.setVisibility(View.VISIBLE);

                    } else {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {


            }
        });

    }*/

    public void delete_cart(FarmXBuy_Model model) {

        CartItemUtils.deleteCartItem(ctx, CartItemUtils.toCartItemEntity(model), object ->
                cart_interface.mycart("12", true)
        );
    }

    public void cart_quantityupdate(final FarmXBuy_Model model, final Integer quantity, final ViewHolder holder) {

        CartItemUtils.updateCartItem(ctx, CartItemUtils.toCartItemEntity(model, quantity), object ->
                cart_interface.mycart("12", true)
        );

     /*   AndroidNetworking.post(Webservice.cart_quantityupdate)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .addUrlEncodeFormBodyParameter("prod_id", model.getProduct_id())
                .addUrlEncodeFormBodyParameter("quantity", String.valueOf(quantity))
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        holder.cart_item_quantity.setText(String.valueOf(quantity));
                        if (!ProductUtils.isZero(model.getProduct_price()))
                            holder.total_amount.setText("Rs." + getmultiply(Double.valueOf(quantity), Double.parseDouble(model.getProduct_price())));

                        cart_interface.mycart("12", false);
                    } else {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {


            }
        });*/

    }

    private String getmultiply(Double Quantity, Double amount) {
        Double total = Quantity * amount;
        return String.valueOf(total);

    }
}
