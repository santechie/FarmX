package com.ascentya.AsgriV2.intro;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.login_activities.Formx_Login_Activity;
import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class IntroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));
        ((WormDotsIndicator) findViewById(R.id.indicator)).setViewPager(viewPager);

        ViewPager.OnPageChangeListener pageChangeListener =
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        updateBtn(viewPager);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                };

        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());

        findViewById(R.id.start).setOnClickListener(view -> finishIntro());
        findViewById(R.id.skip).setOnClickListener(view -> finishIntro());
        findViewById(R.id.next).setOnClickListener(view -> showNext(viewPager));
    }

    private void updateBtn(ViewPager viewPager){
        if (viewPager.getCurrentItem() + 1 == viewPager.getAdapter().getCount()){
            findViewById(R.id.indicator).setVisibility(View.INVISIBLE);
            findViewById(R.id.next).setVisibility(View.INVISIBLE);
            findViewById(R.id.skip).setVisibility(View.INVISIBLE);
            findViewById(R.id.start).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.indicator).setVisibility(View.VISIBLE);
            findViewById(R.id.next).setVisibility(View.VISIBLE);
            findViewById(R.id.skip).setVisibility(View.VISIBLE);
            findViewById(R.id.start).setVisibility(View.INVISIBLE);
        }
    }

    private void showNext(ViewPager viewPager){
        if (viewPager.getAdapter().getCount() > viewPager.getCurrentItem() + 1){
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        }
    }

    private void finishIntro(){
        sessionManager.setIntroCompleted();
        openActivity(Formx_Login_Activity.class);
        finish();
    }

    class IntroAdapter extends FragmentStatePagerAdapter{

        private ArrayList<IntroFragment> introFragments = new ArrayList<>();

        public IntroAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            addFragments();
        }

        public void addFragments(){
            introFragments.add(IntroFragment.newInstance(R.drawable.intro_agripedia,
                    "Agripedia", "Contains all Information about all crops"));
            introFragments.add(IntroFragment.newInstance(R.drawable.intro_iot,
                    "IoT", "Integrate IoT devices in your farm to get realtime data"));
            introFragments.add(IntroFragment.newInstance(R.drawable.intro_organic,
                    "Organic", "we help you to do organic farming with set of instructions"));
            introFragments.add(IntroFragment.newInstance(R.drawable.intro_cultivation,
                    "Utility", "we provide lot of services like Soil Test, Water Test, etc.,"));
            introFragments.add(IntroFragment.newInstance(R.drawable.intro_community,
                    "Community", "Get answers for all your question related to farming from out active community"));
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
           return introFragments.get(position);
        }

        @Override
        public int getCount() {
            return introFragments.size();
        }
    }

    public static class IntroFragment extends Fragment{

        private int image;
        private String title, description;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            image = getArguments().getInt("image");
            title = getArguments().getString("title");
            description = getArguments().getString("description");
        }

        public static IntroFragment newInstance(@DrawableRes int image, String title, String description){
            IntroFragment introFragment = new IntroFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("image", image);
            bundle.putString("title", title);
            bundle.putString("description", description);
            introFragment.setArguments(bundle);
            return introFragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ((ImageView) view.findViewById(R.id.image)).setImageResource(image);
            ((TextView) view.findViewById(R.id.title)).setText(title);
            ((TextView) view.findViewById(R.id.description)).setText(description);
        }
    }
}