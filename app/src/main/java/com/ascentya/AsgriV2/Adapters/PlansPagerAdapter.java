package com.ascentya.AsgriV2.Adapters;

import com.ascentya.AsgriV2.Forum.Forum_DynamicFragment;
import com.ascentya.AsgriV2.Models.Cat_Model;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PlansPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    List<Cat_Model> title;


    public PlansPagerAdapter(FragmentManager fm, int NumOfTabs, List<Cat_Model> title) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mNumOfTabs = NumOfTabs;
        this.title = title;

    }

    @Override
    public Fragment getItem(int position) {

        return Forum_DynamicFragment
                .newInstance(title.get(position).getName(),
                        title.get(position).getValue());
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}