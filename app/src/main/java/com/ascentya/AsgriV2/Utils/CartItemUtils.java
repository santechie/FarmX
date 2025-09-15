package com.ascentya.AsgriV2.Utils;

import android.content.Context;

import com.ascentya.AsgriV2.AsyncTasks.MyExecutorService;
import com.ascentya.AsgriV2.Database_Room.entities.CartItemEntity;
import com.ascentya.AsgriV2.Models.FarmXBuy_Model;

public class CartItemUtils {

    public static CartItemEntity toCartItemEntity(FarmXBuy_Model model, int quantity){
        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setProd_id(model.getProduct_id());
        cartItemEntity.setCat_id(model.getCat_id());
        cartItemEntity.setCreated_date(model.getProduct_date());
        cartItemEntity.setProd_desc(model.getProduct_desc());
        cartItemEntity.setProd_image(model.getProduct_image());
        cartItemEntity.setProd_name(model.getProduct_name());
        cartItemEntity.setUser_id(model.getProductuser_id());
        cartItemEntity.setStatus(model.getProduct_status());
        cartItemEntity.setQuantity(quantity);
        cartItemEntity.setProd_price(model.getProduct_price());
        return cartItemEntity;
    }

    public static CartItemEntity toCartItemEntity(FarmXBuy_Model model){
        return toCartItemEntity(model, 0);
    }

    public static FarmXBuy_Model toFarmXBuyModel(CartItemEntity cartItemEntity){
        FarmXBuy_Model model = new FarmXBuy_Model();
        model.setProduct_quantity(String.valueOf(cartItemEntity.getQuantity()));
        model.setCat_id(cartItemEntity.getCat_id());
        model.setProduct_date(cartItemEntity.getCreated_date());
        model.setProduct_image(cartItemEntity.getProd_image());
        model.setProduct_name(cartItemEntity.getProd_name());
        model.setProduct_id(cartItemEntity.getProd_id());
        model.setProduct_desc(cartItemEntity.getProd_desc());
        model.setProduct_price(cartItemEntity.getProd_price());
        model.setProduct_status(cartItemEntity.getStatus());
        model.setProductuser_id(cartItemEntity.getUser_id());
        return model;
    }

    public static void getCartItems(Context context, MyExecutorService.Action action){
        new MyExecutorService(new MyExecutorService.MyRunnable(context, action) {
            @Override
            public Object runForResult() {
                return getCartItemDao().getAll();
            }
        });
    }

    public static void insertCartItem(Context context, CartItemEntity cartItemEntity, MyExecutorService.Action action){
        new MyExecutorService(new MyExecutorService.MyRunnable(context, action) {
            @Override
            public Object runForResult() {
                getCartItemDao().insert(cartItemEntity);
                return null;
            }
        });
    }

    public static void updateCartItem(Context context, CartItemEntity cartItemEntity, MyExecutorService.Action action){
        new MyExecutorService(new MyExecutorService.MyRunnable(context, action) {
            @Override
            public Object runForResult() {
                getCartItemDao().update(cartItemEntity);
                return null;
            }
        });
    }

    public static void deleteCartItem(Context context, CartItemEntity cartItemEntity, MyExecutorService.Action action){
        new MyExecutorService(new MyExecutorService.MyRunnable(context, action) {
            @Override
            public Object runForResult() {
                getCartItemDao().delete(cartItemEntity);
                return null;
            }
        });
    }

    public static void deleteAllCartItem(Context context, MyExecutorService.Action action){
        new MyExecutorService(new MyExecutorService.MyRunnable(context, action) {
            @Override
            public Object runForResult() {
                getCartItemDao().deleteAll();
                return null;
            }
        });
    }
}
