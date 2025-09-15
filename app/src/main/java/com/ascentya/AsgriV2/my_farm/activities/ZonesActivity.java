package com.ascentya.AsgriV2.my_farm.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.fragments.ZonesFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ZonesActivity extends BaseActivity {

    String landId, cropId, varietyId;
    Crops_Main crop;
    private ZonesFragment zonesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zones);

        landId = getIntent().getStringExtra("land_id");
        cropId = getIntent().getStringExtra("crop_id");
        varietyId = getIntent().getStringExtra("variety_id");
        crop = GsonUtils.fromJson(getIntent().getStringExtra("crop"), Crops_Main.class);

        if (savedInstanceState == null) {
            zonesFragment = ZonesFragment.newInstance(landId, /*cropId, varietyId*/ crop);
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, zonesFragment).commit();
        }else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            if (fragment instanceof ZonesFragment){
                zonesFragment = (ZonesFragment) fragment;
            }
        }

        setToolbarTitle(getString(R.string.my_zones), true);
        if(getModuleManager().canInsert(Components.MyFarm.ZONE))
            setMenu(R.menu.my_land_menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_land){
//            openWithSubscription(ZonesActivity.class,
//                    Components.MyFarm.ZONE,
//                    ModuleManager.ACCESS.INSERT);
            addZones();
    }
       /* if (checkSubscription(Components.MyFarm.ZONE, ModuleManager.ACCESS.INSERT)) {
            addZones();
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void addZones(){
        if (zonesFragment != null)
            zonesFragment.addZones();

    }
}