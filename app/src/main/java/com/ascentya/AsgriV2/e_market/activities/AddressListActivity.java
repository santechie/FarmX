package com.ascentya.AsgriV2.e_market.activities;

import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;
import android.util.Pair;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.adapters.AddressAdapter;
import com.ascentya.AsgriV2.e_market.data.model.Address;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddressListActivity extends BaseActivity implements AddressAdapter.Action {

    public static final String MODE = "mode";
    public static final int NORMAL = 0;

    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;

    private FloatingActionButton addressFab;
    private int selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        setToolbarTitle("Select Address", true);

        recyclerView = findViewById(R.id.recyclerView);
        addressAdapter = new AddressAdapter(this);

        recyclerView.setAdapter(addressAdapter);

        addressFab = findViewById(R.id.addAddressFab);
        addressFab.setOnClickListener(v -> openActivity(AddressActivity.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public List<Address> getAllAddress() {
        return storage.getAllAddress();
    }

    @Override
    public void onEdit(int position) {
        openActivity(AddressActivity.class,
                new Pair<>(AddressActivity.ADDRESS, getAllAddress().get(position)));
    }

    @Override
    public void onDelete(int position) {
        storage.deleteAddress(getAllAddress().get(position).getId());
        Toasty.normal(this, "Address Deleted!").show();
        onRefresh();
    }

    @Override
    public void onSelect(int position) {
        storage.setSelectedAddressId(getAllAddress().get(position).getId());
        Toasty.normal(this, "Address Selected!").show();
        finish();
    }

    @Override
    public void onRefresh() {
        addressAdapter.notifyDataSetChanged();
    }
}