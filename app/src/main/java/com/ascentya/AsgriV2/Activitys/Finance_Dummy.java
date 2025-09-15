package com.ascentya.AsgriV2.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.farmx.myfinance.Expanse;
import com.ascentya.AsgriV2.farmx.myfinance.Revenue;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import es.dmoral.toasty.Toasty;

public class Finance_Dummy extends AppCompatActivity {

    boolean isLand = true;
    String usercrop_id, crop_id, crop_type;

    private ViewPager view_pager;
    private TabLayout tab_layout;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_dummy_activity);

        Toasty.normal(this, getClass().getSimpleName()).show();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent() != null) {
            isLand = getIntent().getBooleanExtra("is_land", true);
            usercrop_id = getIntent().getStringExtra("usercrop"); // land ID
            crop_id = getIntent().getStringExtra("crop_id"); // Crop String
            crop_type = getIntent().getStringExtra("crop_type"); // main_crop or inter_crop
        }

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
        Expanse mywall = new Expanse();
        Revenue myconnections = new Revenue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_land", isLand);
        bundle.putString("usercrop", usercrop_id);
        bundle.putString("crop_id",  isLand ? null : crop_id);
        bundle.putString("crop_type", crop_type);
        mywall.setArguments(bundle);
        myconnections.setArguments(bundle);
        mSectionPagerAdapter.addFragment(mywall, "My Expenses");
        mSectionPagerAdapter.addFragment(myconnections, "My Revenue");
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