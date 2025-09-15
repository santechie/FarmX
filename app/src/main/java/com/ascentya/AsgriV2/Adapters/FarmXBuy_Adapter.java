package com.ascentya.AsgriV2.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.FarmXBuy_Model;
import com.ascentya.AsgriV2.Models.SellCat_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.buysell.Detailed_Product;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.skydoves.elasticviews.ElasticFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FarmXBuy_Adapter extends RecyclerView.Adapter<FarmXBuy_Adapter.ViewHolder> {

    private List<FarmXBuy_Model> items = new ArrayList<>();
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

    Listener listener;

    public FarmXBuy_Adapter(Context context, Listener listener, List<FarmXBuy_Model> items, String user_id) {
        this.items = items;
        this.ctx = context;
        this.listener = listener;
        this.user_id = user_id;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView product_title, cart_item_quantity, product_price_new;
        public ImageView icons, cart_item_quantity_minusBtn, cart_item_quantity_plusBtn;
        ElasticFloatingActionButton delete, edit;
        ProgressBar product_cover_loader;
        LinearLayout addquantity;
        AppCompatButton product_card_Btn;


        public ViewHolder(View v, Listener listener) {
            super(v);

            product_title = (TextView) v.findViewById(R.id.product_title);
            icons = (ImageView) v.findViewById(R.id.product_cover);
            product_cover_loader = (ProgressBar) v.findViewById(R.id.product_cover_loader);
            product_price_new = (TextView) v.findViewById(R.id.product_price_new);
            cart_item_quantity_minusBtn = (ImageView) v.findViewById(R.id.cart_item_quantity_minusBtn);
            cart_item_quantity_plusBtn = (ImageView) v.findViewById(R.id.cart_item_quantity_plusBtn);
            cart_item_quantity = (TextView) v.findViewById(R.id.cart_item_quantity);
            addquantity = (LinearLayout) v.findViewById(R.id.addquantity);
            product_card_Btn = v.findViewById(R.id.product_card_Btn);

//            disc = (TextView) v.findViewById(R.id.desc);
//            date = (TextView) v.findViewById(R.id.date);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.farmxbuy_row, parent, false);


        vh = new ViewHolder(v, listener);


        return vh;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder view = (ViewHolder) holder;
            final FarmXBuy_Model Position_Object = items.get(position);
            int quantity = listener.getQuantity(Position_Object.getProduct_id());

            if (listener.canBuy()) {
                if (quantity == 0) {
                    holder.addquantity.setVisibility(View.GONE);
                    holder.product_card_Btn.setVisibility(View.VISIBLE);
                    holder.cart_item_quantity.setText("0");
                } else {
                    holder.addquantity.setVisibility(View.VISIBLE);
                    holder.product_card_Btn.setVisibility(View.GONE);
                    holder.cart_item_quantity.setText(quantity + "");
                }
            }else {
                holder.product_card_Btn.setVisibility(View.INVISIBLE);
            }

            holder.product_price_new.setText(ProductUtils.getPrice(Position_Object.getProduct_price()));

            view.product_card_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vie) {
                  if(ctx != null && ctx instanceof BaseActivity && ((BaseActivity) ctx).checkSubscription(Components.BuyAndSell.BUY , ModuleManager.ACCESS.INSERT))
                      listener.setQuantity(Position_Object.getProduct_id(), 1);
//                    add_tocart(Position_Object, view, FarmXBuy_Adapter.this);

                }
            });

            view.cart_item_quantity_plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vie) {

                    listener.setQuantity(Position_Object.getProduct_id(),
                            Integer.parseInt(view.cart_item_quantity.getText().toString()) + 1);
                /*    cart_quantityupdate(holder.getAdapterPosition(),
                            FarmXBuy_Adapter.this,
                            Position_Object,
                            ,
                            view);*/
//                    view.cart_item_quantity.setText(String.valueOf(Integer.parseInt(view.cart_item_quantity.getText().toString()) + 1));

                }
            });
            view.cart_item_quantity_minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vie) {
                    int quantity = Integer.parseInt(view.cart_item_quantity.getText().toString());
                    if (quantity-1<0){
                        quantity = 0;
                    }else {
                        quantity -= 1;
                    }
                    listener.setQuantity(Position_Object.getProduct_id(), quantity);
                  /*  if () {

                        cart_quantityupdate(holder.getAdapterPosition(),
                                FarmXBuy_Adapter.this,
                                Position_Object,
                                Integer.parseInt(view.cart_item_quantity.getText().toString()) - 1,
                                view);
//                        view.cart_item_quantity.setText(String.valueOf(Integer.parseInt(view.cart_item_quantity.getText().toString()) - 1));

                    }else {
                        if (Integer.parseInt(view.cart_item_quantity.getText().toString()) == 1){
                            delete_cart(Position_Object.getCart_id(), Position_Object, FarmXBuy_Adapter.this);
                        }
                    }*/
                }
            });

            view.product_title.setText(Position_Object.getProduct_name());
//            view.date.setText(Position_Object.getDate());
//            view.disc.setText(Html.fromHtml(Position_Object.getProduct_desc()));
//fm.setPlusIcon(view.cart_item_quantity_plusBtn);
//fm.setMinusIcon(view.cart_item_quantity_minusBtn);

//            Glide.with(ctx)
//                    .load(Position_Object.getProduct_image())
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            holder.cover_loader.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            holder.cover_loader.setVisibility(View.GONE);
//                            return false;
//                        }
//                    })
//                    .into(holder.product_thumbnail);

            view.itemView.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
//                    Intent i = new Intent(ctx, Detailed_Product.class);
//                    Log.i(FarmXBuy_Adapter.class.getSimpleName(), "Model: "+jsonModel);
//                    System.out.println(FarmXBuy_Adapter.class.getSimpleName() + " Model: "+jsonModel);
//                    System.out.println(FarmXBuy_Adapter.class.getSimpleName() + " Description: "+Position_Object.getProduct_desc());
//                    i.putExtra(Constants.BUY_PRODUCT, jsonModel);
//                    ctx.startActivity(i);
                    if(ctx != null && ctx instanceof BaseActivity){
                        String jsonModel = GsonUtils.getGson().toJson(Position_Object);
                        ((BaseActivity) ctx).openActivity(Detailed_Product.class, Pair.create(Constants.BUY_PRODUCT, jsonModel));
                    }
                }
            });

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
        return items.size();
    }

    public void updateList(List<FarmXBuy_Model> list) {
        items = list;
        notifyDataSetChanged();
    }

   /* public void add_tocart(FarmXBuy_Model model, final ViewHolder holder, FarmXBuy_Adapter adapter) {


//
        AndroidNetworking.post(Webservice.add_cart)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .addUrlEncodeFormBodyParameter("cat_id", model.getCat_id())
                .addUrlEncodeFormBodyParameter("prod_id", model.getProduct_id())
                .addUrlEncodeFormBodyParameter("quantity", "1")
//                .addUrlEncodeFormBodyParameter("amount", model.getProduct_price())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e(FarmXBuy_Adapter.class.getSimpleName(),
                        "addToCard LandDeviceData:\n"+jsonObject);

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

//                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        model.setProduct_quantity("1");
                        adapter.notifyDataSetChanged();
                        adapter.listener.update();

                    } else {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e(FarmXBuy_Adapter.class.getSimpleName(), "addToCart Error:\n"+anError.getErrorBody());
            }
        });

    }

    public void cart_quantityupdate(int position, FarmXBuy_Adapter adapter, FarmXBuy_Model model, final Integer quantity, final ViewHolder holder) {

        Log.i(FarmXBuy_Adapter.class.getSimpleName(), "cartQuantityUpdate Start:\n"+quantity);

        AndroidNetworking.post(Webservice.cart_quantityupdate)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .addUrlEncodeFormBodyParameter("prod_id", model.getProduct_id())
                .addUrlEncodeFormBodyParameter("quantity", String.valueOf(quantity))
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        model.setProduct_quantity(String.valueOf(quantity));
                        Log.i(FarmXBuy_Adapter.class.getSimpleName(), "cartQuantityUpdate:\n"+quantity);
                        Log.i(FarmXBuy_Adapter.class.getSimpleName(), "cartQuantityUpdate:\n"+jsonObject);
//                        holder.cart_item_quantity.setText(String.valueOf(quantity));
                        adapter.notifyDataSetChanged();
                        adapter.listener.update();

                    } else {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e(FarmXBuy_Adapter.class.getSimpleName(), "CardUpdateError: \n"+anError.getErrorBody());

            }
        });

    }

    public void delete_cart(String id, FarmXBuy_Model model, FarmXBuy_Adapter adapter) {
        AndroidNetworking.post(Webservice.delete_cart)
                .addUrlEncodeFormBodyParameter("cart_id", id)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.i(FarmXBuy_Adapter.class.getSimpleName(), "deleteCart:\n"+jsonObject);

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        model.setProduct_quantity("0");
                        adapter.notifyDataSetChanged();
                    } else {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e(FarmXBuy_Adapter.class.getSimpleName(), "Delete Cart Error\n"+anError.getErrorBody());


            }
        });

    }*/

    public interface Listener{
        void update();
        int getQuantity(String productId);
        void setQuantity(String productId, int quantity);
        boolean canBuy();
    }
}
