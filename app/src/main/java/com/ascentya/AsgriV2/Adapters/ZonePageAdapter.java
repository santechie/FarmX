package com.ascentya.AsgriV2.Adapters;

import com.ascentya.AsgriV2.Models.Cat_Model;
import com.ascentya.AsgriV2.farmx.my_lands.My_Zones;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ZonePageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    List<Cat_Model> title;
    String land_id;

    public ZonePageAdapter(FragmentManager fm, int NumOfTabs, List<Cat_Model> title, String landid) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mNumOfTabs = NumOfTabs;
        this.title = title;
        this.land_id = landid;
    }

    @Override
    public Fragment getItem(int position) {

        return My_Zones.newInstance(title.get(position).getId(), land_id);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}