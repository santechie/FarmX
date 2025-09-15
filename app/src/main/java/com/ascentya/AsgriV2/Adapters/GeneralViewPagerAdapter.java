package com.ascentya.AsgriV2.Adapters;

import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class GeneralViewPagerAdapter extends FragmentStatePagerAdapter {

    private Action action;

    public GeneralViewPagerAdapter(@NonNull FragmentManager fm, Action action) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.action = action;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return action.getFragments().get(position);
    }

    @Override
    public int getCount() {
        return action.getFragments().size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String name = ((BaseFragment) action.getFragments().get(position)).getName();
        System.out.println("Name: " + name);
        return name;
    }

    /*@Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }*/

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    public interface Action{
        ArrayList<Fragment> getFragments();
    }
}
