package com.ascentya.AsgriV2.my_farm.activities;

import android.os.Bundle;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.fragments.PestDiseaseNewFragment;

public class PestDiseaseActivity extends BaseActivity {

    String landId = null, cropId = null, zoneId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_disease);

        landId = getIntent().getStringExtra("land_id");
        cropId = getIntent().getStringExtra("crop_id");
        zoneId = getIntent().getStringExtra("zone_id");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,
                    PestDiseaseNewFragment.newInstance(landId, cropId, zoneId)).commit();
        }

        setToolbarTitle(getString(R.string.pest_disease), true);
    }
}