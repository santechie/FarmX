package com.ascentya.AsgriV2.my_farm.activities;

import android.os.Bundle;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.fragments.DeviceDataFragment;

public class DeviceDataActivity extends BaseActivity {

    String landId, cropId;
    Crops_Main crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zones);

        landId = getIntent().getStringExtra("land_id");
        cropId = getIntent().getStringExtra("crop_id");
        crop = GsonUtils.fromJson(getIntent().getStringExtra("crop"), Crops_Main.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,
                    DeviceDataFragment.newInstance(false, landId, /*cropId, ""*/ crop)).commit();
        }

        setToolbarTitle(getString(R.string.my_devices), true);
    }
}