package com.ascentya.AsgriV2.my_farm.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.ascentya.AsgriV2.Adapters.GeneralViewPagerAdapter;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.fragments.ActivitiesFragment;
import com.ascentya.AsgriV2.my_farm.fragments.DeviceDataFragment;
import com.ascentya.AsgriV2.my_farm.fragments.FinanceFragment;
import com.ascentya.AsgriV2.my_farm.fragments.LandFragmentNew;
import com.ascentya.AsgriV2.my_farm.fragments.PestDiseaseNewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class LandActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GeneralViewPagerAdapter.Action, ViewPager.OnPageChangeListener {

    private String landId;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private GeneralViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);

        landId = getIntent().getStringExtra("land_id");
        System.out.println("Land Activity: " + landId);

        loadFragments();

        bottomNavigationView = findViewById(R.id.navigationView);
        viewPager = findViewById(R.id.viewPager);

        viewPagerAdapter = new GeneralViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(this);

        //viewPager.setOffscreenPageLimit(4);


        if (!getModuleManager().canView(Components.MyFarm.LAND))
            bottomNavigationView.getMenu().removeItem(R.id.data);
        if (!getModuleManager().canView(Components.MyFarm.ACTIVITY))
            bottomNavigationView.getMenu().removeItem(R.id.activities);
        if (!getModuleManager().canView(Components.MyFarm.INCOME)
        && !getModuleManager().canView(Components.MyFarm.EXPENSE))
            bottomNavigationView.getMenu().removeItem(R.id.finance);
        if (!getModuleManager().canView(Components.MyFarm.PEST_DISEASE))
            bottomNavigationView.getMenu().removeItem(R.id.pest);
        if (!getModuleManager().canView(Components.MyFarm.REAL_TIME_DATA))
            bottomNavigationView.getMenu().removeItem(R.id.device_data);


        /*bottomNavigationView
                .setOnNavigationItemSelectedListener(this::onNavigationItemSelected); */
        bottomNavigationView
                .setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    @SuppressLint("SuspiciousIndentation")
    private void loadFragments() {
        fragments.clear();

        if (getModuleManager().canView(Components.MyFarm.LAND))
        fragments.add(LandFragmentNew.getInstance(landId));
        if (getModuleManager().canView(Components.MyFarm.ACTIVITY))
        fragments.add(ActivitiesFragment.newInstance(true, landId, "", null));
        if (getModuleManager().canView(Components.MyFarm.INCOME)
        || getModuleManager().canView(Components.MyFarm.EXPENSE))
        fragments.add(FinanceFragment.newInstance(true, landId, "", null));
        if (getModuleManager().canView(Components.MyFarm.PEST_DISEASE))
        fragments.add(PestDiseaseNewFragment.newInstance(true, landId, "", ""));
        if (getModuleManager().canView(Components.MyFarm.REAL_TIME_DATA))
        fragments.add(DeviceDataFragment.newInstance(true, landId, /*"", ""*/ null));
//        toast("hello" + getModuleManager().canView(Components.MyFarm.REAL_TIME_DATA));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.data:
                setFragments(getFragmentIndex(LandFragmentNew.class));
                return true;
            case R.id.activities:
                setFragments(getFragmentIndex(ActivitiesFragment.class));
                return true;
            case R.id.finance:
                setFragments(getFragmentIndex(FinanceFragment.class));
                return true;
            case R.id.pest:
                setFragments(getFragmentIndex(PestDiseaseNewFragment.class));
                return true;
            case R.id.device_data:
                setFragments(getFragmentIndex(DeviceDataFragment.class));
                return true;
        }
        return false;
    }

    private int getFragmentIndex(Class cls){
        for (int i = 0; i < getFragments().size(); i++){
            if (cls.isInstance(getFragments().get(i))){
                return i;
            }
        }
        return 0;
    }

    private void setFragments(int position) {
        if (viewPager.getCurrentItem() != position)
            viewPager.setCurrentItem(position);
    }

    @Override
    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(position).getItemId());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }*/
    }
}