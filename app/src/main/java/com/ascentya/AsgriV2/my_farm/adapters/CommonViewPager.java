package com.ascentya.AsgriV2.my_farm.adapters;

import com.ascentya.AsgriV2.Adapters.ViewPagerAdapter;
import com.ascentya.AsgriV2.Models.ActivityCycle_Model;

import java.util.List;

import androidx.fragment.app.FragmentManager;

class CommonViewPager extends ViewPagerAdapter {

    public CommonViewPager(FragmentManager fm, Action action, List<ActivityCycle_Model> Data) {
        super(fm, action, Data);
    }
}
