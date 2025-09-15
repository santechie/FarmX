package com.ascentya.AsgriV2.staff.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.CommonRecyclerAdapter;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.staff.data.Staff;
import com.ascentya.AsgriV2.staff.view_holder.StaffViewHolder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class ManageStaffActivity extends BaseActivity
        implements CommonRecyclerAdapter.Action, StaffViewHolder.Action {

    private RecyclerView recyclerView;
    private ImageView staffIV;
    private TextView staffTv;

    private ArrayList<Staff> staffList = new ArrayList<>();
    private CommonRecyclerAdapter adapter;

    private ExtendedFloatingActionButton addStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_staff);

        setToolbarTitle("Manage Staff", true);

        recyclerView = findViewById(R.id.recyclerView);
        staffIV = findViewById(R.id.image);
        staffTv = findViewById(R.id.hint);

        addStaff = findViewById(R.id.addStaff);
        addStaff.setOnClickListener(v -> addStaff());

        adapter = new CommonRecyclerAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadStaff();
    }

    private void loadStaff(){

        Staff staff = new Staff();
        staff.setName("Ahamed");
        staff.setEmail("yasinahamed1@gmail.com");
        staff.setMobileNumber("6363636363");

        staffList.add(staff);

        updateStaff();
    }

    private void updateStaff(){
        if (staffList.isEmpty()){
            staffIV.setVisibility(View.VISIBLE);
            staffTv.setVisibility(View.VISIBLE);
        }else {
            staffIV.setVisibility(View.INVISIBLE);
            staffTv.setVisibility(View.INVISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void addStaff(){
        openActivity(InviteStaffActivity.class);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    @Override
    public CommonRecyclerAdapter.CommonViewHolder createViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_staff_item, parent, false);
        return new StaffViewHolder(view, this);
    }

    @Override
    public void updateView(CommonRecyclerAdapter.CommonViewHolder holder, int position) {
        ((StaffViewHolder) holder).update(staffList.get(position));
    }

    @Override
    public void onClicked(int position) {
        Staff staff = staffList.get(position);
        openActivity(StaffPermissionActivity.class,
                Pair.create("staff", staff));
    }
}