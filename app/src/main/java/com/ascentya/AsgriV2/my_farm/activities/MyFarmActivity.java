package com.ascentya.AsgriV2.my_farm.activities;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.FarmX_MyStockActivity;
import com.ascentya.AsgriV2.Activitys.Farmx_Addland_New;
import com.ascentya.AsgriV2.Activitys.Farmx_Cattle;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.AddLandDialog;
import com.ascentya.AsgriV2.dialog.AddNoLandDialog;
import com.ascentya.AsgriV2.dialog.SelectControlMeasureDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.AccessManager;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.managers.SubscriptionManager;
import com.ascentya.AsgriV2.my_farm.adapters.MenuAdapter;
import com.ascentya.AsgriV2.my_farm.model.Menu;

import java.util.ArrayList;

import org.json.JSONObject;

public class MyFarmActivity extends BaseActivity implements MenuAdapter.Action{

    private TextView nameTv;
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private ArrayList<Menu> menuList = new ArrayList<>(9);
    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private boolean landLoadError = false, landLoading = false;
    private Class activityToOpen;
    private AccessManager.Modules.Operation operationToExecute;
    private Components.Component componentToExecute;
    public ModuleManager.ACCESS access;

    private ReceiverInterface landUpdate = new ReceiverInterface() {
        @Override
        public void onReceive(Intent intent) {
            checkLands();
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm);
        setToolbarTitle(getString(R.string.my_farm), true);

        loadMenus();

        nameTv = findViewById(R.id.name);
        recyclerView = findViewById(R.id.recyclerView);
        menuAdapter = new MenuAdapter(this, R.layout.view_farm_menu);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(menuAdapter);

        nameTv.setText(getString(R.
                string.hi) + " " + getSessionManager().getUser().getFirstname() + "!");

        registerReceiver(Constants.Broadcasts.LAND_UPDATE, landUpdate);

        checkLands();

      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showControlMeasureDialog();
            }
        }, 5000);*/
    }

    private void showControlMeasureDialog() {
        SelectControlMeasureDialog dialog = new SelectControlMeasureDialog(this,
                Constants.ReportTypes.PEST.getValue(), "1");
    }

    private void loadMenus() {

        menuList.clear();
        if (getModuleManager().canView(Components.MyFarm.LAND)) {
            menuList.add(new Menu(R.drawable.menu_land, getString(R.string.my_lands), "my_land"));
        }

        if (getModuleManager().canView(Components.MyFarm.CROP)) {
            menuList.add(new Menu(R.drawable.menu_crop, getString(R.string.my_crops), "my_crop"));
        }

        if (getModuleManager().canView(Components.MyFarm.ZONE)) {
            menuList.add(new Menu(R.drawable.menu_zones, getString(R.string.my_zones), "my_zone"));
        }

        if (getModuleManager().canView(Components.MyFarm.REAL_TIME_DATA)) {
            menuList.add(new Menu(R.drawable.menu_data, getString(R.string.real_time_data), "real_time_data"));
        }

        if (getModuleManager().canView(Components.MyFarm.PEST_DISEASE)) {
            menuList.add(new Menu(R.drawable.menu_pest, getString(R.string.pest_disease), "pest_and_disease"));
        }

        if (getModuleManager().canView(Components.MyFarm.MY_STOCK)) {
            menuList.add(new Menu(R.drawable.menu_stock, getString(R.string.my_stocks), "my_stocks"));
        }

        if (getModuleManager().canView(Components.MyFarm.MEMBERS)) {
            menuList.add(new Menu(R.drawable.menu_members, getString(R.string.members), "members"));
        }

        if (getModuleManager().canView(Components.MyFarm.ACTIVITY)) {
            menuList.add(new Menu(R.drawable.menu_activity, getString(R.string.activities), "activities"));
        }

        if (getModuleManager().canView(Components.MyFarm.INCOME) ||
                getModuleManager().canView(Components.MyFarm.EXPENSE)) {
            menuList.add(new Menu(R.drawable.stocks, getString(R.string.finance_), "finance"));
        }

    }


    @Override
    public ArrayList<Menu> getMenuList() {
        return menuList;
    }

    @Override
    public void onClicked(int position) {
        switch (menuList.get(position).getKey()) {
            case "my_land":

                Intent i = new Intent(getApplicationContext(), MyLandsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
                // checkSubscription(MyLandsActivity.class, Components.MyFarm.LAND);
                break;
            case "my_crop":
                //checkSubscription(MyCropsActivity.class, Components.MyFarm.CROP);
                Intent ij = new Intent(getApplicationContext(), MyCropsActivity.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(ij);
                break;
            case "my_zone":
                //checkSubscription(ZonesActivity.class, Components.MyFarm.ZONE);
                Intent ijk = new Intent(getApplicationContext(), ZonesActivity.class);
                ijk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(ijk);
                break;
            case "real_time_data":
                //checkSubscription(DeviceDataActivity.class, Components.MyFarm.REAL_TIME_DATA);
                Intent ijkl = new Intent(getApplicationContext(), DeviceDataActivity.class);
                ijkl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(ijkl);
                break;
            case "pest_and_disease":
                //checkSubscription(PestDiseaseActivity.class, Components.MyFarm.PEST_DISEASE);
                Intent ijklm = new Intent(getApplicationContext(), PestDiseaseActivity.class);
                ijklm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(ijklm);
                break;
            case "my_stocks":
                //checkSubscription(FarmX_MyStockActivity.class, Components.MyFarm.MY_STOCK);
                Intent ijklmn = new Intent(getApplicationContext(), FarmX_MyStockActivity.class);
                ijklmn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(ijklmn);
                break;
            case "members":
                //checkSubscription(Farmx_Cattle.class, Components.MyFarm.MEMBERS);
                Intent ijklmo = new Intent(getApplicationContext(), Farmx_Cattle.class);
                ijklmo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(ijklmo);
                break;
            case "activities":
                //checkSubscription(ActivitiesActivity.class, Components.MyFarm.ACTIVITY);
                Intent ijklmp = new Intent(getApplicationContext(), ActivitiesActivity.class);
                ijklmp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(ijklmp);
                break;
            case "finance":
                //checkSubscription(FinancesActivity.class, (Components.MyFarm.EXPENSE));
                Intent ijklmq = new Intent(getApplicationContext(), FinancesActivity.class);
                ijklmq.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(ijklmq);
                break;
        }
    }

    public boolean checkSubscription(Class activityClass, Components.Component component){
        if (landLoading) return false;
        boolean hasAccess = component != null
                && SubscriptionManager.getSubscriptionManager(this)
                .with(getSupportFragmentManager()).checkSubscription(activityToOpen, component);
        if (!hasAccess) return hasAccess;
        if (landLoadError){
            activityToOpen = activityClass;
            componentToExecute = component;
            checkLands();
        }else {
            if (!lands.isEmpty()){
                openActivity(activityClass);
                activityToOpen = null;
                operationToExecute = null;
            }else {
                if (getModuleManager().canInsert(Components.MyFarm.LAND))
                    showAddLandDialog();
                if (!getModuleManager().canInsert(Components.MyFarm.LAND))
                    showAddNoLandDialog();
            }
        }
        return hasAccess;
    }

    private void checkAndOpen(Class activityClass, AccessManager.Modules.Operation operation){
        if (landLoading) return;

        boolean hasAccess = operation == null || checkPrivilege(operation);

        if (!hasAccess) return;

        if (sessionManager.isPaid()){
            if (landLoadError){
                activityToOpen = activityClass;
                operationToExecute = operation;
                checkLands();
            }else {
                if (!lands.isEmpty()){
                    openActivity(activityClass);
                    activityToOpen = null;
                    operationToExecute = null;
                }else {
                    if (getModuleManager().canInsert(Components.MyFarm.LAND))
                        showAddLandDialog();
                     if (!getModuleManager().canInsert(Components.MyFarm.LAND))
                        showAddNoLandDialog();


                }
            }
        }else {
            showPayDialog();
        }
    }


    private void showAddLandDialog(){
        AddLandDialog landDialog = new AddLandDialog(new AddLandDialog.Action() {
            @Override
            public void onRegisterClicked() {
                openActivity(Farmx_Addland_New.class);
            }
        });
        landDialog.show(getSupportFragmentManager(), "add_land");
    }

    private void showAddNoLandDialog(){
        AddNoLandDialog LandDialog = new AddNoLandDialog(new AddNoLandDialog.Action() {
            @Override
            public void onRegisterClicked() {
                openActivity(Farmx_Addland_New.class);
            }
        });
        LandDialog.show(getSupportFragmentManager(), "add_land");
    }

    private void checkLands(){
        showLoading();
        landLoading = true;
        ApiHelper.loadLands(getSessionManager().getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response,ArrayList<Maincrops_Model> lands, boolean error) {
                landLoading = false;
                hideLoading();

              /*  if (UserHelper.checkResponse(MyFarmActivity.this, response)){
                    return;
                }*/
                if (!error) {
                    MyFarmActivity.this.lands.clear();
                    MyFarmActivity.this.lands.addAll(lands);
                   /* if (activityToOpen != null){
                        //checkSubscription(activityToOpen, componentToExecute);
                    }*/
                }else {
                    landLoadError = error;
                    activityToOpen = null;
                    operationToExecute = null;
                    errorToast("Network Error!");
                }
            }
        });
    }
}