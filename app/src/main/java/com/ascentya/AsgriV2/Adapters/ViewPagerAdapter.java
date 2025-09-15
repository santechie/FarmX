package com.ascentya.AsgriV2.Adapters;

import android.os.Bundle;

import com.ascentya.AsgriV2.Models.ActivityCycle_Model;
import com.ascentya.AsgriV2.agripedia.cropcycle.CropCycle_Fragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    Fragment fragment = null;
    List<ActivityCycle_Model> Data;
    Action action;

    public ViewPagerAdapter(FragmentManager fm, Action action, List<ActivityCycle_Model> Data) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.Data = Data;
        this.action = action;
    }

    @Override
    public Fragment getItem(int position) {
        String title = action.getTabTitle(position);
        for (ActivityCycle_Model model: Data){
            if (model.getTitle().equals(title)){
                Bundle bundle = new Bundle();
                bundle.putString("Title", model.getTitle());
                bundle.putString("disc", model.getDisc());
                bundle.putString("image", model.getPic());

                fragment = new CropCycle_Fragment();
                fragment.setArguments(bundle);

                return fragment;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return Data.size();
    }

    public interface Action{
        String getTabTitle(int position);
    }

}