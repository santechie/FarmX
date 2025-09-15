package com.ascentya.AsgriV2.my_farm.activities;

import android.os.Bundle;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.fragments.FinanceFragment;

public class FinancesActivity extends BaseActivity {

    String landId = null, cropId = null, varietyId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);

        landId = getIntent().getStringExtra("land_id");
        cropId = getIntent().getStringExtra("crop_id");
        varietyId = getIntent().getStringExtra("variety_id");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,
                    FinanceFragment.newInstance(landId, cropId, varietyId)).commit();
        }

        setToolbarTitle(getString(R.string.finance_), true);
    }
}