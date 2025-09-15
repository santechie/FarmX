package com.ascentya.AsgriV2.my_farm.activities;

import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.fragments.ActivitiesFragment;

import androidx.annotation.NonNull;

public class ActivitiesActivity extends BaseActivity {

    String landId = null, cropId = null, varietyId = null;
    Crops_Main crop;

    private ActivitiesFragment activitiesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        landId = getIntent().getStringExtra("land_id");
        cropId = getIntent().getStringExtra("crop_id");
        varietyId = getIntent().getStringExtra("variety_id");
        crop = GsonUtils.fromJson(getIntent().getStringExtra("crop"), Crops_Main.class);

        if (savedInstanceState == null) {
            activitiesFragment = ActivitiesFragment.newInstance(landId, cropId, varietyId);
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, activitiesFragment).commit();
        }

        setToolbarTitle(getString(R.string.activities), true);
        setMenu(R.menu.activities_menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.activities){

            if (activitiesFragment != null){
                landId = activitiesFragment.getSelectedLandId();
                cropId = activitiesFragment.getSelectedCropId();
                varietyId = activitiesFragment.getSelectedVarietyId();
                crop = activitiesFragment.getSelectedCrop();
            }

            openActivity(ActivitiesCalendarActivity.class,
                    Pair.create("land_id", landId),
                    Pair.create("crop_id", cropId),
                    Pair.create("variety_id", varietyId),
                    Pair.create("crop", GsonUtils.toJson(crop)));
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}