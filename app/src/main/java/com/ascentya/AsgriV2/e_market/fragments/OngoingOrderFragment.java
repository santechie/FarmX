package com.ascentya.AsgriV2.e_market.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.activities.OrderDetailsActivity;
import com.ascentya.AsgriV2.e_market.adapters.OrderHistoryAdapter;
import com.ascentya.AsgriV2.e_market.data.model.Order;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class OngoingOrderFragment extends BaseFragment implements OrderHistoryAdapter.Action {

    private OrderHistoryAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ongoing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView =  view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new OrderHistoryAdapter(this);

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(int position) {
        getBaseActivity().openActivity(OrderDetailsActivity.class,
                new Pair<>(OrderDetailsActivity.ORDER, getOrders().get(position)));
    }

    @Override
    public List<Order> getOrders() {
        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(new Order());
        orderList.add(new Order());
        orderList.add(new Order());
        orderList.add(new Order());
        return orderList;
    }
}
