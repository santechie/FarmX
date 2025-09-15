package com.ascentya.AsgriV2.e_market.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.model.Order;
import com.ascentya.AsgriV2.e_market.data.model.OrderItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryAdapter extends
        RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private Action action;

    public OrderHistoryAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_order_history_item, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.update(action.getOrders().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getOrders().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class OrderHistoryViewHolder extends RecyclerView.ViewHolder{

        private ListView listView;
        private ItemListAdapter adapter;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> action.onClick(getAbsoluteAdapterPosition()));
            listView = itemView.findViewById(R.id.items);
            adapter = new ItemListAdapter(itemView.getContext(), R.layout.view_order_history_item_item);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((adapterView, view, i, l) -> action.onClick(getAbsoluteAdapterPosition()));
        }

        public void update(Order order){
            adapter.clear();
            for (int i=0; i<5; i++){
                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(100f * (i+1));
                orderItem.setId((i+1)+"");
                orderItem.setQuantityType("KG");
                orderItem.setStockId("Apple "+(i+1));
                adapter.add(orderItem);
            }
            adapter.notifyDataSetChanged();
        }
    }

    class ItemListAdapter extends ArrayAdapter<OrderItem> {

        public ItemListAdapter(@NonNull Context context, int resource) {
            super(context, resource, R.id.name);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (view == null)
                view = LayoutInflater.from(getContext())
                        .inflate(R.layout.view_order_history_item_item, null);
            ((TextView) view.findViewById(R.id.name))
                    .setText(getItem(position).getStockId());
            ((TextView) view.findViewById(R.id.quantity))
                    .setText(getItem(position).getQuantity()
                            +" "+getItem(position).getQuantityType());
            return view;
        }
    }

    public interface Action{

        void onClick(int position);
        List<Order> getOrders();
    }
}
