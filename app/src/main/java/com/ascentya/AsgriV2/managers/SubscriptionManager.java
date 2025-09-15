package com.ascentya.AsgriV2.managers;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.dialog.ExpiredDialog;
import com.ascentya.AsgriV2.dialog.TrialDialog;
import com.ascentya.AsgriV2.dialog.UpgradeDialog;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionManager {
    private static SubscriptionManager instance;
    private Context context;
    private SessionManager sessionManager;
    private ExpiredDialog expiredDialog;
    private UpgradeDialog upgradeDialog;
    private TrialDialog trialDialog;
    public static final Type accessListType = new TypeToken<List<SubscriptionManager.Access>>() {
    }.getType();
    public static final Type upgradeListType = new TypeToken<List<NewSubscription>>() {
    }.getType();

    private ArrayList<Access> accessList = new ArrayList<>();
    private List<NewSubscription> subscriptionList = new ArrayList<>();
    private SubscriptionDetails subscriptionDetails;
    //    private NewSubscription subscriptionList;
    private FragmentManager fragmentManager;

    private SubscriptionManager(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);
        expiredDialog = new ExpiredDialog();
        upgradeDialog = new UpgradeDialog();
        trialDialog = new TrialDialog();
    }

    public SubscriptionManager with(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        return this;
    }

    private void prepare() {
        if (accessList.isEmpty() || subscriptionDetails == null) {
            subscriptionDetails = sessionManager.getSubscriptionDetails();
            subscriptionList = sessionManager.getSubscriptionList();
            accessList.addAll(sessionManager.getAccess());
            //  upgradeList.addAll(sessionManager.getUpgrade());
        } else if (sessionManager.getAccess().isEmpty() && !accessList.isEmpty()) {
            accessList.clear();
        }
    }

    public boolean canLoad() {
        return sessionManager != null
                && sessionManager.getUser() != null;
    }

    public void load(Action action) {
      //  System.out.println("7575757575"+sessionManager.getUser().getId());
       // AndroidNetworking.post(Webservice.getExpiredDetails)
        AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Subscription/get_subscription_detail")
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("Subscription Response: \n" + response);
                            JSONObject subscriptionDetailsJson = response.getJSONObject("subscription_details");
                            subscriptionDetails = GsonUtils.fromJson(subscriptionDetailsJson.toString(), SubscriptionDetails.class);
                            JSONArray jsonArray1 = response.getJSONArray("subscription_list");
//                            subscriptionList = GsonUtils.fromJson(subscriptionListJson.toString(), SubscriptionList.class);
                            JSONArray jsonArray = response.optJSONArray("subscription_access");
                            accessList.clear();
                            subscriptionList.clear();
                            accessList.addAll(GsonUtils.fromJson(jsonArray.toString(), accessListType));
                            subscriptionList.addAll(GsonUtils.fromJson(jsonArray1.toString(), upgradeListType));
                            sessionManager.setSubscription(subscriptionDetails);
                            sessionManager.setAccess(accessList);
                            sessionManager.setUpgradeSubscription(subscriptionList);
                            if (action != null) action.onLoaded(false);
                        } catch (Exception e) {
                            if (action != null) action.onLoaded(true);
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        if (action != null) action.onLoaded(true);
                    }
                });
    }

    public interface Action {
        void onLoaded(boolean error);
    }

    public boolean canView(Modules.Module module) {
        prepare();
        return check(module, ModuleManager.ACCESS.VIEW);
    }

    public boolean canInsert(Modules.Module module) {
        prepare();
        return check(module, ModuleManager.ACCESS.INSERT);
    }

    public boolean canView(Components.Component component) {
        prepare();
        return check(component, ModuleManager.ACCESS.VIEW);
    }

    public boolean canInsert(Components.Component component) {
        prepare();
        return check(component, ModuleManager.ACCESS.INSERT);
    }

    public boolean canUpdate(Components.Component component) {
        prepare();
        return check(component, ModuleManager.ACCESS.UPDATE);
    }

    public boolean canDelete(Components.Component component) {
        prepare();
        return check(component, ModuleManager.ACCESS.DELETE);
    }

    private boolean check(Modules.Module module, String operation) {
        prepare();
        System.out.println("Module: " + module.value);
        System.out.println("Operation: " + operation);
        for (SubscriptionManager.Access access : accessList) {
            if (access.module_value.equals(module.value)
                    && access.module_status.equals("1")) {
                if (access.component_status.equals("1")) {

                    if (operation != null) {
                        boolean status = false;
                        switch (operation) {
                            case ModuleManager.ACCESS.VIEW:
                                status = access.view_access != null && access.view_access.equals("1");
                                break;
                            case ModuleManager.ACCESS.INSERT:
                                status = access.insert_access != null && access.insert_access.equals("1");
                                break;
                            case ModuleManager.ACCESS.UPDATE:
                                status = access.update_access != null && access.update_access.equals("1");
                                break;
                            case ModuleManager.ACCESS.DELETE:
                                status = access.delete_access != null && access.delete_access.equals("1");
                                break;
                        }
                        if (status) return status;
                    } else if ((access.view_access != null && access.view_access.equals("1"))
                            || (access.insert_access != null && access.insert_access.equals("1"))
                            || (access.update_access != null && access.update_access.equals("1"))
                            || (access.delete_access != null && access.delete_access.equals("1"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean check(Components.Component component, String access) {
        boolean status = false;
        for (SubscriptionManager.Access a : accessList) {
            if (a.module_value.equals(component.module.value) && a.module_status.equals("1")) {
                if (a.component_value.equals(component.value) && a.component_status.equals("1")) {
                    switch (access) {
                        case ModuleManager.ACCESS.VIEW:
                            status = a.view_access != null && a.view_access.equals("1");
                            break;
                        case ModuleManager.ACCESS.INSERT:
                            status = a.insert_access != null && a.insert_access.equals("1");
                            break;
                        case ModuleManager.ACCESS.UPDATE:
                            status = a.update_access != null && a.update_access.equals("1");
                            break;
                        case ModuleManager.ACCESS.DELETE:
                            status = a.delete_access != null && a.delete_access.equals("1");
                            break;
                    }
                    System.out.println("Module: " + a.module_name);
                    System.out.println("Component: " + a.component_name);
                    System.out.println("Access: " + access);
                    System.out.println("Value: " + status);
                    //if (status) return status;
                }
            }
        }
        return status;
    }

//    public interface Action{
//        void onLoaded(String message, boolean error);
//    }

    public static class ACCESS {
        public static final String VIEW = "view";
        public static final String INSERT = "insert";
        public static final String UPDATE = "update";
        public static final String DELETE = "delete";
    }

    public static SubscriptionManager getSubscriptionManager(Context context) {
        if (instance == null) {
            instance = new SubscriptionManager(context);
        }
        return instance;
    }


    public static class Access {

        public String module_id;
        public String module_name;
        public String module_value;
        public String module_status;
        public String component_id;
        public String component_name;
        public String component_value;
        public String component_status;
        public String view_access;
        public String insert_access;
        public String update_access;
        public String delete_access;


    }

    public boolean checkSubscription(Modules.Module module) {
        System.out.println("Check Subscription Called!");
        boolean hasAccess = check(module, ModuleManager.ACCESS.VIEW);
        if (!hasAccess) showDialog();
        return hasAccess;
    }

//    public boolean checkTrial(Modules.Module module){
//        System.out.println("Trial Subscription Called!");
//        boolean hasAccess = check(module, ModuleManager.ACCESS.VIEW);
//        if (!hasAccess) showTrialDialog();
//        return hasAccess;
//    }

    public boolean checkSubscription(Components.Component component) {
        System.out.println("Check Subscription Called!");
        boolean hasAccess = check(component, ModuleManager.ACCESS.VIEW);
        if (!hasAccess) showDialog();
        return hasAccess;
    }

    public boolean checkSubscription(Class activityToOpen, Components.Component component) {
        boolean hasAccess = check(component, ModuleManager.ACCESS.VIEW);
        if (!hasAccess) showDialog();
        return hasAccess;
    }

    public boolean checkSubscription(Modules.Module module, String access) {
        boolean hasAccess = check(module, access);
        if (!hasAccess) showDialog();
        return hasAccess;
    }

    public boolean checkSubscription(Components.Component component, String access) {
        boolean hasAccess = check(component, access);
        if (!hasAccess) showDialog();
        return hasAccess;
    }


    public void showDialog() {
        prepare();
        String status = subscriptionDetails.getStatus();
        System.out.println("75757775"+ subscriptionDetails.getStatus());
        if (status.equals("1")) {
           // showUpgradeDialog();
        } else if (status.equals("2")) {
            showExpiredDialog();
        }
    }

    public void showTrial() {
        prepare();
        String is_default = subscriptionDetails.getTrial();
        if (is_default.equals("1")) {
            showTrialDialog();
        }
    }

    public void showUpgradeDialog() {
//        if (true) {
//            Toast.makeText(context, "Showing Upgrade Dialog", Toast.LENGTH_SHORT).show();
//            upgradeDialog.show(fragmentManager, "Upgrade dialog");

//            if (upgradeDialog != null && !isPayShowing())
//                upgradeDialog.show(fragmentManager, "upgrade_dialog");
//            FragmentManager fragmentManager = upgradeDialog.getFragmentManager();
//            if (fragmentManager != null) {
//                fragmentManager.beginTransaction().remove(upgradeDialog).commitNow();
//                return;
//            }

        if (upgradeDialog != null && (upgradeDialog.isVisible() || upgradeDialog.isRemoving()))
            return;

        if (upgradeDialog != null && (upgradeDialog.isVisible() || upgradeDialog.isAdded())) {
            FragmentManager fragmentManager = upgradeDialog.getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().remove(upgradeDialog).commitNow();
            }
        }

        fragmentManager.executePendingTransactions();

        prepare();
        upgradeDialog.setSubscriptionList(subscriptionList);
        upgradeDialog.showNow(fragmentManager, "Upgrade Dialog");
    }


//            if (upgradeDialog != null && (upgradeDialog.isVisible() || upgradeDialog.isRemoving())
//                    && upgradeDialog.isAdded()) {
//                FragmentManager fragmentManager = upgradeDialog.getFragmentManager();
//                if (fragmentManager != null) {
//                    fragmentManager.beginTransaction().remove(upgradeDialog).commitNow();
//                return;
//                }
//                return;
//            }
//        }

//            if (upgradeDialog != null
//            }
//        }

//        prepare();
//        upgradeDialog.setSubscriptionList(subscriptionList);
//        upgradeDialog.showNow(fragmentManager, "Upgrade Dialog");

        /*if (upgradeDialog != null && !upgradeDialog.isVisible()) {

            upgradeDialog.setSubscriptionList(subscriptionList);
//            upgradeDialog.dismiss();
            upgradeDialog.showNow(fragmentManager, "Upgrade Dialog");
//            upgradeDialog.show(fragmentManager, "Upgrade Dialog");
        }*/
//
//    public boolean isPayShowing() {
//        return upgradeDialog != null && upgradeDialog.isVisible();
//    }


    public void showExpiredDialog() {
        if (expiredDialog != null && (expiredDialog.isVisible() || expiredDialog.isRemoving())) return;

       if (expiredDialog != null && (expiredDialog.isVisible() || expiredDialog.isAdded())){
           FragmentManager fragmentManager = expiredDialog.getFragmentManager();
           if (fragmentManager != null){
               fragmentManager.beginTransaction().remove(expiredDialog).commitNow();
           }
       }

        fragmentManager.executePendingTransactions();

        prepare();
        upgradeDialog.setSubscriptionList(subscriptionList);
        upgradeDialog.showNow(fragmentManager, "Expired Dialog");

        /*if (expiredDialog != null && !expiredDialog.isVisible()) {
            prepare();
            expiredDialog.setSubscriptionDetails(subscriptionDetails);
            expiredDialog.dismiss();
            expiredDialog.showNow(fragmentManager, "Expired Dialog");
            //expiredDialog.show(fragmentManager, "Expired Dialog");
        }*/
    }

    public void showTrialDialog() {
        if (trialDialog != null && !trialDialog.isVisible()) {
            prepare();
            trialDialog.setTrialList(subscriptionList);
            trialDialog.show(fragmentManager, "Trial Dialog");
        }
    }


    public static SubscriptionManager getInstance(Context context){
        if (instance == null)
            instance = new SubscriptionManager(context);
        return instance;
    }



}
