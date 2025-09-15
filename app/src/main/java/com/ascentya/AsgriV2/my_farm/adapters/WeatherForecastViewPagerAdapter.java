package com.ascentya.AsgriV2.my_farm.adapters;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.my_farm.fragments.WeatherForecastFragment;
import com.ascentya.AsgriV2.my_farm.model.WeatherForecast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class WeatherForecastViewPagerAdapter extends FragmentStatePagerAdapter {

    public static int BASE_ELEVATION = 2;
    public static int MAX_ELEVATION_FACTOR = 5;

    private Action action;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public WeatherForecastViewPagerAdapter(@NonNull FragmentManager fm, Action action) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.action = action;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void notifyDataSetChanged() {
        fragments.clear();
        for (WeatherForecast weatherForecast: action.getWeatherForecastList()) {
            fragments.add(WeatherForecastFragment.getInstance(weatherForecast));
        }
        super.notifyDataSetChanged();
    }

    public interface Action{
        ArrayList<WeatherForecast> getWeatherForecastList();
    }

    public CardView getCardViewAt(int position){
        return position < getCount() ? (CardView) getItem(position).getView().findViewById(R.id.card) : null;
    }
}
