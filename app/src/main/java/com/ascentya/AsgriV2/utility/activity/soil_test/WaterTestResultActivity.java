package com.ascentya.AsgriV2.utility.activity.soil_test;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.utility.adapter.SoilTestResultAdapter;
import com.ascentya.AsgriV2.utility.model.WaterTest;

public class WaterTestResultActivity extends BaseActivity {

    private WaterTest waterTest;
    
    private TextView ticketNumberTv, labNameTv;
    private RecyclerView recyclerView;
    private SoilTestResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_test_result);

        waterTest = getFromIntent("water_test", WaterTest.class);

        setToolbarTitle("Water Test Result", true);

        labNameTv = findViewById(R.id.labName);
        ticketNumberTv = findViewById(R.id.ticketNumber);
        recyclerView = findViewById(R.id.recyclerView);

        ticketNumberTv.setText(waterTest.getTicketNumber());
        labNameTv.setText(waterTest.getLabName());


        adapter = new SoilTestResultAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        adapter.update(waterTest);
    }
}