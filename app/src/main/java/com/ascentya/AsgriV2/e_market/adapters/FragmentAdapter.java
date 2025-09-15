package com.ascentya.AsgriV2.e_market.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    private Action action;

    public FragmentAdapter(@NonNull FragmentManager fm, Action action) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.action = action;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return action.getFragment(position);
    }

    @Override
    public int getCount() {
        return action.getFragmentCount();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return action.getPageTitle(position);
    }

    public interface Action{

        int getFragmentCount();
        Fragment getFragment(int position);
        String getPageTitle(int position);
    }
}
