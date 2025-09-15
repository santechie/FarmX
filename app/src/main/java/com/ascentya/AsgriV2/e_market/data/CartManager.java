package com.ascentya.AsgriV2.e_market.data;

import android.content.Context;

import com.ascentya.AsgriV2.e_market.data.model.CartItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class CartManager {

    private static CartManager instance;

    private ArrayList<CartItem> cartItems;

    private CartManager(Context context){
        cartItems = new ArrayList<>();
    }

    @Nullable
    public CartItem getCartItem(String stockId){
        for (CartItem cartItem: cartItems){
            if (cartItem.getStockId().equals(stockId)){
                return cartItem;
            }
        }
        return null;
    }

    public List<CartItem> getAllCartItems(){
        return cartItems;
    }

    public void addToCart(CartItem cartItem){
        removeFromCart(cartItem.getStockId());
        cartItems.add(cartItem);
    }

    public void removeFromCart(String stockId){
        for (int i=0; i<cartItems.size(); i++){
            CartItem cartI = cartItems.get(i);
            if (cartI.getStockId().equals(stockId)){
                cartItems.remove(i);
                break;
            }
        }
    }

    public int getCount(){
        return cartItems.size();
    }

    public static CartManager getInstance(Context context){
        if (instance == null)
            instance = new CartManager(context);
        return instance;
    }

}
