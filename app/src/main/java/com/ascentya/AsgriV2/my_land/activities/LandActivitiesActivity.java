package com.ascentya.AsgriV2.my_land.activities;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ascentya.AsgriV2.Adapters.GeneralViewPagerAdapter;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_land.fragments.ActivityListFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class LandActivitiesActivity extends BaseActivity implements GeneralViewPagerAdapter.Action {

    private Maincrops_Model landModel;
    private String cropId;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GeneralViewPagerAdapter adapter;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_activities);

        setActionBar(R.id.toolbar);
        setToolbarTitle("Activities", true);

        landModel = getFromIntent("land", Maincrops_Model.class);
        cropId = getIntent().getStringExtra("crop_id");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        adapter = new GeneralViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        loadFragments();
    }

    private void loadFragments(){

        System.out.println("Activity Crop Id: " + cropId);

        fragmentList.clear();

        fragmentList.add(ActivityListFragment.newInstance("Soil Preparation", "soil_preparation", landModel, null));
        fragmentList.add(ActivityListFragment.newInstance("Land Preparation", "land_preparation", landModel, null));
        fragmentList.add(ActivityListFragment.newInstance("Water Analysis", "water_analysis", landModel, null));
        fragmentList.add(ActivityListFragment.newInstance("Cultivation", "cultivation", landModel, null));
        fragmentList.add(ActivityListFragment.newInstance("Post Harvest", "post_harvest", landModel, null));

        adapter.notifyDataSetChanged();

    }

    public static void open(Context context, Maincrops_Model landModel, @Nullable String selectedCrop){
        Intent i = new Intent(context, LandActivitiesActivity.class);
        i.putExtra("land", GsonUtils.getGson().toJson(landModel));
        i.putExtra("crop_id", selectedCrop);
        context.startActivity(i);
    }

    @Override
    public ArrayList<Fragment> getFragments() {
        return fragmentList;
    }

    public interface Action{

    }
}