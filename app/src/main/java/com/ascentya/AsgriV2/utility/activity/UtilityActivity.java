package com.ascentya.AsgriV2.utility.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.AccessManager;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.utility.activity.soil_test.TestRequestsActivity;
import com.ascentya.AsgriV2.utility.adapter.UtilityAdapter;
import com.ascentya.AsgriV2.utility.model.Utility;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.ascentya.AsgriV2.managers.AccessManager.Modules.SOIL_TEST;
import static com.ascentya.AsgriV2.managers.AccessManager.Modules.WATER_TEST;

public class UtilityActivity extends BaseActivity
        implements AdapterView.OnItemClickListener {

    private ListView listView;
    private UtilityAdapter adapter;
    private ArrayList<Utility> utilityList = new ArrayList<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        setToolbarTitle("Utility", true);
        setOverrideOnBackPressed(true);

        listView = findViewById(R.id.listView);
        adapter = new UtilityAdapter(this, utilityList);
        listView.setAdapter(adapter);

        //userTypeAction(this);
        listView.setOnItemClickListener(this);

        loadItems();
        updateList();
    }

    @SuppressLint("SuspiciousIndentation")
    private void loadItems(){

        utilityList.clear();

//        toast("Fc: " + getModuleManager().canView(ModuleManager.Components.Utility.FARMX_FACILITY_CENTER));

        if(getModuleManager().canView(Components.Utility.FERTILIZER_CALCULATOR))
        utilityList.add(new Utility(R.drawable.ic_fertilizer_calc, Constants.TestTypes.fertilizerCalculator,
                null, Components.Utility.FERTILIZER_CALCULATOR));

        if(getModuleManager().canView(Components.Utility.SOIL_TEST))
        utilityList.add(new Utility(R.drawable.ic_soil_test, Constants.TestTypes.SoilTest,
                TestRequestsActivity.class, Components.Utility.SOIL_TEST));

        if(getModuleManager().canView(Components.Utility.WATER_TEST))
        utilityList.add(new Utility(R.drawable.ic_water_test, Constants.TestTypes.WaterTest,
                TestRequestsActivity.class, Components.Utility.WATER_TEST));

        if(getModuleManager().canView(Components.Utility.FARMX_FACILITY_CENTER))
        utilityList.add(new Utility(R.drawable.ic_facility_center, Constants.TestTypes.facilityCenter,
                null, Components.Utility.FARMX_FACILITY_CENTER));
    }

    private void updateList(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        openActivity(i);
    }

    private void openActivity(int position){
        Class activityClass = utilityList.get(position).getActivityClass();

        if(activityClass != null) {
            String testType = utilityList.get(position).getTestType().type;
//            openWithAccess(activityClass, getModule(testType), Pair.create("test_type",
//                    utilityList.get(position).getTestType()));

            openWithSubscription(activityClass, utilityList.get(position).getComponent(),
                    ModuleManager.ACCESS.VIEW,
                    Pair.create("test_type", utilityList.get(position).getTestType()));

        } else Toasty.normal(this, "On Progress").show();
    }

    private AccessManager.Modules.Module getModule(String key){
        if (key.equals(Constants.TestTypes.SoilTest.type)){
            return SOIL_TEST;
        }else if (key.equals(Constants.TestTypes.WaterTest.type)){
            return WATER_TEST;
        }else {
            return null;
        }
    }
}