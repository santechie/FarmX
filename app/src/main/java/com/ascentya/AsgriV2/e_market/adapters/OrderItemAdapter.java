package com.ascentya.AsgriV2.e_market.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.model.OrderItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Action action;

    public OrderItemAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_order_details_item_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        holder.update(action.getOrderItems().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getOrderItems().size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void update(OrderItem orderItem){}
    }

    public interface Action{
        List<OrderItem> getOrderItems();
    }
}
