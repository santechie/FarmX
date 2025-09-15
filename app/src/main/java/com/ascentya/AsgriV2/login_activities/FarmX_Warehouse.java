package com.ascentya.AsgriV2.login_activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.farmx.log_market_warehouse.Logistics;
import com.ascentya.AsgriV2.farmx.log_market_warehouse.Market_place;
import com.ascentya.AsgriV2.farmx.log_market_warehouse.Warehouse;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class FarmX_Warehouse extends AppCompatActivity {
    private ViewPager view_pager;
    private TabLayout tab_layout;
    LinearLayout back;
    TextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_dummy_activity);
        back = findViewById(R.id.back);
        type = findViewById(R.id.type);
        type.setText("Logistics/warehouse");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        initComponent();

    }

    private void initComponent() {


        view_pager = (ViewPager) findViewById(R.id.view_pager);
        view_pager.setOffscreenPageLimit(1);

        tab_layout = (TabLayout) findViewById(R.id.tabcultivation_layout);
        tab_layout.setupWithViewPager(view_pager);
        setupViewPager(view_pager);

    }

    private void setupViewPager(ViewPager viewPager) {


        SectionsPagerAdapter mSectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Logistics logistics = new Logistics();
        Market_place marketplace = new Market_place();
        Warehouse warehouse = new Warehouse();

        mSectionPagerAdapter.addFragment(logistics, "Logistics");
        mSectionPagerAdapter.addFragment(marketplace, "Market place");
        mSectionPagerAdapter.addFragment(warehouse, "Warehouse");
        viewPager.setAdapter(mSectionPagerAdapter);
        viewPager.setCurrentItem(0);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


    }
}