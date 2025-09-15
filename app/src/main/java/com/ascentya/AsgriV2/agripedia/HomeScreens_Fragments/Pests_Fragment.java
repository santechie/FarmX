package com.ascentya.AsgriV2.agripedia.HomeScreens_Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.agripedia.Pests_Fragment.Control_Measures_Fragment;
import com.ascentya.AsgriV2.agripedia.Pests_Fragment.Identificatoion_Fargmenr;
import com.ascentya.AsgriV2.agripedia.Pests_Fragment.Symptoms_fragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Pests_Fragment extends Fragment {
    View root_view;
    private ViewPager view_pager;
    private TabLayout tab_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.pests_layout, container, false);
        initComponent();
        return root_view;
    }


    private void initComponent() {


        view_pager = (ViewPager) root_view.findViewById(R.id.view_pager);


        tab_layout = (TabLayout) root_view.findViewById(R.id.tab_pests);
        tab_layout.setupWithViewPager(view_pager);
        setupViewPager(view_pager);

    }

    private void setupViewPager(ViewPager viewPager) {


        SectionsPagerAdapter mSectionPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        Symptoms_fragment mywall = new Symptoms_fragment();
        Identificatoion_Fargmenr myconnections = new Identificatoion_Fargmenr();
        Control_Measures_Fragment mycommunities = new Control_Measures_Fragment();


        mSectionPagerAdapter.addFragment(mywall, getString(R.string.Symptoms));
        mSectionPagerAdapter.addFragment(myconnections, getString(R.string.Identificatoion));
        mSectionPagerAdapter.addFragment(mycommunities, getString(R.string.Control_Measures));

        viewPager.setAdapter(mSectionPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(0);
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
