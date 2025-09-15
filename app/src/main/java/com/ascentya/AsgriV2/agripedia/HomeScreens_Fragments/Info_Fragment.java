package com.ascentya.AsgriV2.agripedia.HomeScreens_Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GPSTracker;
import com.ascentya.AsgriV2.agripedia.Info_Fragments.BasicInfo_fragment;
import com.ascentya.AsgriV2.agripedia.Info_Fragments.Nutrient_Values;
import com.ascentya.AsgriV2.agripedia.Info_Fragments.Texonomy_Fragment;
import com.ascentya.AsgriV2.agripedia.Info_Fragments.Varieties_Fragment;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Info_Fragment extends Fragment {
    View root_view;
    private ViewPager view_pager;
    private TabLayout tab_layout;
    GPSTracker gpsTracker;
    Boolean strtext = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.info_layout, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            strtext = getArguments().getBoolean("edttext", false);
        }
        initComponent();

        return root_view;
    }

    private void initComponent() {

        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.getLongitude() != 0) {
            getstates(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        }


        view_pager = (ViewPager) root_view.findViewById(R.id.view_pager);


        tab_layout = (TabLayout) root_view.findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
        setupViewPager(view_pager);

    }

    private void setupViewPager(ViewPager viewPager) {


        SectionsPagerAdapter mSectionPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        BasicInfo_fragment mywall = new BasicInfo_fragment();
        Texonomy_Fragment myconnections = new Texonomy_Fragment();
        Varieties_Fragment mycommunities = new Varieties_Fragment();
        Nutrient_Values nutrientValues = new Nutrient_Values();
        Bundle bundle = new Bundle();
        bundle.putBoolean("edttext", strtext);

        mywall.setArguments(bundle);

        mSectionPagerAdapter.addFragment(mywall, getString(R.string.basicinfo));
        mSectionPagerAdapter.addFragment(myconnections, getString(R.string.taxonomy));
        mSectionPagerAdapter.addFragment(mycommunities, getString(R.string.varieties));
        mSectionPagerAdapter.addFragment(nutrientValues, getString(R.string.nutrientValues));
        viewPager.setAdapter(mSectionPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
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

    public String getstates(Double lat, Double lang) {

        Geocoder geocoder;
        List<Address> addresses = null;
        String state = "English";
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses.size() > 0) {
                state = addresses.get(0).getAdminArea();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return state;
    }

}
