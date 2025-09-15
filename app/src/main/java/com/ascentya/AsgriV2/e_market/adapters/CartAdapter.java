package com.ascentya.AsgriV2.e_market.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.e_market.data.model.CartItem;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import com.ascentya.AsgriV2.e_market.data.model.Stock;
import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemViewHolder> {

    private Action action;

    public CartAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_emarket_cart_item, parent, false);
        CartItemViewHolder holder = new CartItemViewHolder(action, view, this::getItem);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.update(getItem(position));
    }

    @Override
    public int getItemCount() {
        return action.getCartItems().size();
    }

    public CartItem getItem(int position){
        return action.getCartItems().get(position);
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder{

        TextView itemTypeTv, itemTv, dateRangeTv, placeTv, priceTv;
        ImageView deleteBtn;
        Chip quantityChip;
        Action action;

        public CartItemViewHolder(Action action, @NonNull View itemView, Local local) {
            super(itemView);
            this.action = action;

            itemTypeTv = itemView.findViewById(R.id.itemType);
            itemTv = itemView.findViewById(R.id.item);
            dateRangeTv = itemView.findViewById(R.id.dateRange);
            placeTv = itemView.findViewById(R.id.place);
            priceTv = itemView.findViewById(R.id.price);
            deleteBtn = itemView.findViewById(R.id.delete);
            quantityChip = itemView.findViewById(R.id.chip);

            deleteBtn.setOnClickListener(view ->
                    action.deleteCartItem(local.getItem(getAbsoluteAdapterPosition()).getStockId()));

            quantityChip.setOnClickListener(view ->
                    action.editCartItem(local.getItem(getAbsoluteAdapterPosition()).getStockId()));

        }

        public void update(CartItem cartItem){

            itemTypeTv.setText(
                    action.getItemType(
                            cartItem.getCategoryId(),
                            cartItem.getItemId(),
                            cartItem.getItemTypeId()).getName());

            itemTv.setText(
                    String.format("in %s",
                            action.getItem(
                                    cartItem.getCategoryId(),
                                    cartItem.getItemId()).getName()));

            Stock stock = action.getStock(cartItem.getStockId());

            dateRangeTv.setText(
                    String.format("%s - %s",
                            stock.getDateStart(),
                            stock.getDateEnd())
            );

            placeTv.setText(
                    stock.getPlaceName()
            );

            priceTv.setText(
                    ProductUtils.getPrice(
                            String.valueOf(cartItem.getPrice())
                    )
            );

            quantityChip.setText(
                    String.format(
                            "%.2f %s",
                            cartItem.getQuantity(),
                            cartItem.getQuantityType()

                    )
            );

        }
    }

    public interface Action{
        List<CartItem> getCartItems();
        void deleteCartItem(String stockId);
        void editCartItem(String stockId);
        void refreshStocks();
        Stock getStock(String stockId);
        Item getItem(String categoryId, String itemId);
        ItemType getItemType(String categoryId, String itemId, String itemTypeId);
    }

    interface Local{
        CartItem getItem(int position);
    }
}
