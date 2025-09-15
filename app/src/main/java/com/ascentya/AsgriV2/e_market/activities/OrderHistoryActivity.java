package com.ascentya.AsgriV2.e_market.activities;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.adapters.FragmentAdapter;
import com.ascentya.AsgriV2.e_market.fragments.OngoingOrderFragment;
import com.ascentya.AsgriV2.e_market.fragments.PastOrderFragment;
import com.google.android.material.tabs.TabLayout;

public class OrderHistoryActivity extends BaseActivity implements FragmentAdapter.Action {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        setActionBar(R.id.toolbar);
        setToolbarTitle("Order History", true);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public int getFragmentCount() {
        return 2;
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0){
            return new OngoingOrderFragment();
        }else if (position == 1){
            return new PastOrderFragment();
        }
        return null;
    }

    @Override
    public String getPageTitle(int position) {
        if (position == 0){
            return "ONGOING";
        }else if (position == 1){
            return "PAST ORDERS";
        }
        return null;
    }
}