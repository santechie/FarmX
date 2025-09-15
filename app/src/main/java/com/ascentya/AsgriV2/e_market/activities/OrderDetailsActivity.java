package com.ascentya.AsgriV2.e_market.activities;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.adapters.OrderItemAdapter;
import com.ascentya.AsgriV2.e_market.data.model.Order;
import com.ascentya.AsgriV2.e_market.data.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends BaseActivity implements OrderItemAdapter.Action {

    public static final String ORDER = "order";
    private Order order;

    private RecyclerView recyclerView;

    private OrderItemAdapter orderItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        order = getFromIntent(ORDER, Order.class);

        setToolbarTitle("Order Details", true);

        recyclerView = findViewById(R.id.recyclerView);
        orderItemAdapter = new OrderItemAdapter(this);
        recyclerView.setAdapter(orderItemAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                ((LinearLayoutManager)recyclerView.getLayoutManager()).getOrientation()));
    }

    @Override
    public List<OrderItem> getOrderItems() {
        return getOrderItemList();
    }

    private List<OrderItem> getOrderItemList(){
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem());
        orderItems.add(new OrderItem());
        orderItems.add(new OrderItem());
        orderItems.add(new OrderItem());
        orderItems.add(new OrderItem());
        orderItems.add(new OrderItem());
        orderItems.add(new OrderItem());
        return orderItems;
    }
}