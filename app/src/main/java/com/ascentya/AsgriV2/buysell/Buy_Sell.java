package com.ascentya.AsgriV2.buysell;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.buysell.fragmens.Buy_Fragment;
import com.ascentya.AsgriV2.buysell.fragmens.MyOrders;
import com.ascentya.AsgriV2.buysell.fragmens.Sell_Fragment;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Buy_Sell extends BaseActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.buy_farmx,
            R.drawable.sell_farmx,
            R.drawable.myorders,

    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);


//        setupTabIcons();
    }

    /*private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }*/

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (getModuleManager().canView(Components.BuyAndSell.BUY)){
            adapter.addFrag(new Buy_Fragment(), "Buy");
            //tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        }

        if (getModuleManager().canView(Components.BuyAndSell.SELL)) {
            adapter.addFrag(new Sell_Fragment(), "Sell");
            //tabLayout.getTabAt(tabLayout.getTabCount()).setIcon(tabIcons[1]);
        }

        if (getModuleManager().canView(Components.BuyAndSell.ORDERS)) {
            adapter.addFrag(new MyOrders(), "Orders");
            //tabLayout.getTabAt(tabLayout.getTabCount()).setIcon(tabIcons[2]);
        }

       viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}