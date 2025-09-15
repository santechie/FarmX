package com.ascentya.AsgriV2.Activitys;

import android.content.Context;
import android.os.Bundle;

import com.ascentya.AsgriV2.Mycrops_Mainfragments.Cropsub_fragments.AddNewCrop_Fragments;
import com.ascentya.AsgriV2.Mycrops_Mainfragments.Cropsub_fragments.Members_fragments;
import com.ascentya.AsgriV2.Mycrops_Mainfragments.Rearing_Fragment;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Farmx_Cattle extends BaseActivity {

    private ViewPager view_pager;
    private TabLayout tab_layout;

    boolean deleteAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmx_cattle);
        initComponent();

        deleteAccess = getModuleManager().canDelete(Components.MyFarm.MEMBERS);
    }

    private void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
        setupViewPager(view_pager);
    }


    private void setupViewPager(ViewPager viewPager) {

        SectionsPagerAdapter mSectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        AddNewCrop_Fragments addnewcrop = new AddNewCrop_Fragments();
        Members_fragments members = new Members_fragments();

        if (getModuleManager().canInsert(Components.MyFarm.MEMBERS)) {
        mSectionPagerAdapter.addFragment(addnewcrop, getString(R.string.addmember));}

        mSectionPagerAdapter.addFragment(members, getString(R.string.members));


        viewPager.setAdapter(mSectionPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public SectionsPagerAdapter(FragmentManager fragadapter, Context context) {
            super(fragadapter, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public SectionsPagerAdapter(Context context, FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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

        public static Rearing_Fragment.PlaceholderFragment newInstance(int sectionNumber) {
            Rearing_Fragment.PlaceholderFragment fragment = new Rearing_Fragment.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

    }

}