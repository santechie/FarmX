package com.ascentya.AsgriV2.my_farm.activities;

import static com.ascentya.AsgriV2.managers.AccessManager.Modules.VIEW_EXPENSE;
import static com.ascentya.AsgriV2.managers.AccessManager.Modules.VIEW_INCOME;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;

import com.ascentya.AsgriV2.Activitys.Farmx_Addland_New;
import com.ascentya.AsgriV2.Activitys.Farmx_Cattle;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Add_Income;
import com.ascentya.AsgriV2.Utils.Add_ZonesCrop;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.farmx.postharvest_diseas.PestsDisease_Activity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.my_farm.adapters.LandAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public class MyLandsActivity extends BaseActivity
        implements LandAdapter.Action {

    private RecyclerView recyclerView;
    private LandAdapter adapter;

    private final ArrayList<Maincrops_Model> lands = new ArrayList<>();

    private final ReceiverInterface landUpdates = new ReceiverInterface() {
        @Override
        public void onReceive(Intent intent) {
            loadLands();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lands);

        setToolbarTitle("My Lands", true);
        if (getModuleManager().canInsert(Components.MyFarm.LAND))
            setMenu(R.menu.my_land_menu);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LandAdapter(this);
        recyclerView.setAdapter(adapter);
        registerReceiver(Constants.Broadcasts.LAND_UPDATE, landUpdates);
        loadLands();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_land) {
            Intent i = new Intent(getApplicationContext(), Farmx_Addland_New.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);

         /*   openWithSubscription(Farmx_Addland_New.class,
                    Components.MyFarm.LAND,
                    ModuleManager.ACCESS.INSERT);*/
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadLands() {
        showLoading();
        ApiHelper.loadLands(getSessionManager().getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response, ArrayList<Maincrops_Model> lands, boolean error) {

                runOnUiThread(() -> {
                    MyLandsActivity.this.lands.clear();
                    MyLandsActivity.this.lands.addAll(lands);
                    adapter.notifyDataSetChanged();
                    hideLoading();
                });

                hideLoading();
                updateLands();
                if (UserHelper.checkResponse(MyLandsActivity.this, response)) {
                    return;
                }
            }
        });
    }


    private void updateLands() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<Maincrops_Model> getLands() {
        return lands;
    }

    @Override
    public boolean canAddZone() {
        return getModuleManager().canInsert(Components.MyFarm.ZONE);
    }

    @Override
    public boolean canViewZone() {
        return getModuleManager().canView(Components.MyFarm.ZONE);
    }

    @Override
    public boolean canViewRealTimeData() {
        return getModuleManager().canView(Components.MyFarm.REAL_TIME_DATA);
    }

    @Override
    public boolean canViewFinance() {
        return getModuleManager().canView(Components.MyFarm.INCOME) ||
                getModuleManager().canView(Components.MyFarm.EXPENSE);
    }


    @Override
    public boolean canAddIncome() {
        return getModuleManager().canInsert(Components.MyFarm.INCOME);
    }

    @Override
    public boolean canViewPest() {
        return getModuleManager().canView(Components.MyFarm.PEST_DISEASE_DETECTION);
    }

    @Override
    public void activity(int position) {

        Intent ijklmo = new Intent(getApplicationContext(), LandActivity.class);
        ijklmo.putExtra("land_id", lands.get(position).getId());
        ijklmo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(ijklmo);

        /*openWithSubscription(LandActivity.class, Components.MyFarm.LAND, ModuleManager.ACCESS.VIEW,
                Pair.create("land_id", lands.get(position).getId()));*/



    }

    @Override
    public void expense(int position) {

        if (checkCrop(lands.get(position), null, null)) {
           /* boolean hasAccess = checkSubscription(Components.MyFarm.EXPENSE)
                    || checkSubscription(Components.MyFarm.INCOME);
            if (hasAccess)
                openActivity(FinancesActivity.class, Pair.create("land_id", lands.get(position).getId()));*/

            Intent ijklmo = new Intent(getApplicationContext(), FinancesActivity.class);
            ijklmo.putExtra("land_id", lands.get(position).getId());
            ijklmo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(ijklmo);
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void pest(int position) {
        //openActivity(PestDiseaseActivity.class);
       /* if (checkSubscription(Components.MyFarm.PEST_DISEASE_DETECTION, ModuleManager.ACCESS.VIEW))
        openPestDisease(true, lands.get(position));*/

        Intent ijklmo = new Intent(getApplicationContext(), PestDiseaseActivity.class);
        ijklmo.putExtra("land_id", lands.get(position).getId());
        ijklmo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(ijklmo);
        //openPestDisease(true, lands.get(position));
    }

    @Override
    public void disease(int position) {
        //openActivity(PestDiseaseActivity.class);
       /* if (checkSubscription(Components.MyFarm.PEST_DISEASE_DETECTION, ModuleManager.ACCESS.VIEW))
        openPestDisease(true, lands.get(position));*/

        Intent ijklmo = new Intent(getApplicationContext(), PestDiseaseActivity.class);
        ijklmo.putExtra("land_id", lands.get(position).getId());
        ijklmo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(ijklmo);
        //openPestDisease(true, lands.get(position));

    }

    @Override
    public void income(int position) {
        if (checkCrop(lands.get(position), null, null)) {
//            if (checkPrivilege(ADD_INCOME))
           /* if (checkSubscription(Components.MyFarm.INCOME, ModuleManager.ACCESS.INSERT))
            {

            }*/

            Add_Income obj = new Add_Income();
            obj.dialog(this, getString(R.string.add_income), lands.get(position).getId(),
                    null, getSessionManager().getUser().getId(), getViewDialog(),
                    null, lands.get(position), null);
        }
    }

    @Override
    public void addZones(int position) {
        if (checkCrop(lands.get(position), null, null)) {
//            if (checkPrivilege(ADD_ZONES))
            Maincrops_Model land = lands.get(position);

            /*if (checkSubscription(Components.MyFarm.ZONE, ModuleManager.ACCESS.INSERT)) {

            }*/

            Add_ZonesCrop obj = new Add_ZonesCrop();
            obj.dialog(this, getString(R.string.add_zone), land.getLand_name(), land.getId(),
                    land.getMaincrop(), getSessionManager().getUser().getId(),
                    getViewDialog(), "");

        }
    }

    @Override
    public void zones(int position) {
        if (checkCrop(lands.get(position), null, null)) {
//            if (checkPrivilege(VIEW_ZONES))

            Intent ijklmo = new Intent(getApplicationContext(), ZonesActivity.class);
            ijklmo.putExtra("land_id", lands.get(position).getId());
            ijklmo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(ijklmo);

           /* openWithSubscription(ZonesActivity.class,
                    Components.MyFarm.ZONE,
                    ModuleManager.ACCESS.VIEW);
            Pair.create("land_id", lands.get(position).getId());*/
        }
    }

    @Override
    public void deviceData(int position) {
        if (checkCrop(lands.get(position), null, null)) {
//            if (checkPrivilege(VIEW_DEVICE_DATA))
           /* openWithSubscription(DeviceDataActivity.class,
                    Components.MyFarm.REAL_TIME_DATA,
                    ModuleManager.ACCESS.VIEW);
            Pair.create("land_id", lands.get(position).getId());*/

            Intent ijklmo = new Intent(getApplicationContext(), DeviceDataActivity.class);
            ijklmo.putExtra("land_id", lands.get(position).getId());
            ijklmo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(ijklmo);

        }
    }

    private void openPestDisease(boolean main_inter, Maincrops_Model Position_Object) {
        Intent i = new Intent(this, PestsDisease_Activity.class);
        i.putExtra("title", "Pests");
        i.putExtra("type", main_inter);
        i.putExtra("land", GsonUtils.getGson().toJson(Position_Object));
        if (main_inter) {
            i.putExtra("crop_id", Position_Object.getMaincrop());
        } else {
            i.putExtra("crop_id", Position_Object.getIntercrop());
        }
        startActivity(i);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}