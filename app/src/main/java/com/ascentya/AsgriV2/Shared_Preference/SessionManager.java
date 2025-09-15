package com.ascentya.AsgriV2.Shared_Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.ascentya.AsgriV2.Models.SubscriptionModel;
import com.ascentya.AsgriV2.Models.UserPrivilege;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.managers.SubscriptionDetails;
import com.ascentya.AsgriV2.managers.NewSubscription;
import com.ascentya.AsgriV2.managers.SubscriptionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Android on 10/19/2016.
 */
public class SessionManager {
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String KEY = "aa";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("ASGRI", PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLang(String lang){
        editor.putString("land", lang);
    }

    public String getLang(){
        return pref.getString("lang", "en");
    }

    public void setUser(Userobject user) {

        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user", json);
        editor.commit();

    }

    public Userobject getUser() {

        Gson gson = new Gson();
        String json = pref.getString("user", "");

//
        Userobject user = gson.fromJson(json, Userobject.class);

        return user;
    }

    public void setCrops(String cropsJson){
        editor.putString("crops", cropsJson).apply();
    }

    public String getCrops(){
        return pref.getString("crops", null);
    }

    public void setUserPrivileges(ArrayList<UserPrivilege> userPrivileges){
        String userPrivilegesJsonArrayString = GsonUtils.getGson().toJson(userPrivileges);
        pref.edit().putString("user_privileges", userPrivilegesJsonArrayString).apply();
    }

    public ArrayList<UserPrivilege> getUserPrivileges(){
        String userPrivilegesJsonArrayString = pref.getString("user_privileges", null);
        if (userPrivilegesJsonArrayString == null) return new ArrayList<>();
        ArrayList<UserPrivilege> userPrivileges
                = GsonUtils.fromJson(userPrivilegesJsonArrayString, EMarketStorage.userPrivilegeListType);
        return userPrivileges;
    }

    public void setAccesses(ArrayList<ModuleManager.Access> accessList){
        String accessListJsonString = GsonUtils.toJson(accessList);
        pref.edit().putString("accesses", accessListJsonString).apply();
    }

    public void setAccess(ArrayList<SubscriptionManager.Access> accessList){
        String accessListJsonString = GsonUtils.toJson(accessList);
        pref.edit().putString("access", accessListJsonString).apply();
    }

    public ArrayList<ModuleManager.Access> getAccesses(){
        String accessListJsonString = pref.getString("accesses", null);
        if (accessListJsonString == null) return new ArrayList<>();
        return GsonUtils.fromJson(accessListJsonString, ModuleManager.accessListType);
    }

    public ArrayList<SubscriptionManager.Access> getAccess(){
        String accessListJsonString = pref.getString("access", null);
        if (accessListJsonString == null) return new ArrayList<>();
        return GsonUtils.fromJson(accessListJsonString, SubscriptionManager.accessListType);
    }

    public ArrayList<SubscriptionManager.Access> getUpgrade(){
        String accessListJsonString = pref.getString("upgrade", null);
        if (accessListJsonString == null) return new ArrayList<>();
        return GsonUtils.fromJson(accessListJsonString, SubscriptionManager.upgradeListType);
    }


    public void clearall() {
        editor.clear();
        editor.commit();
        setIntroCompleted();
    }

    public void setSubscription(SubscriptionModel subscription){
        String subscriptionJson = GsonUtils.getGson().toJson(subscription);
        System.out.println("Saving Subscription: \n" + subscriptionJson);
        pref.edit().putString("subscription", subscriptionJson).apply();
    }

    public void setSubscription(SubscriptionDetails expired){
        String expiredJson = GsonUtils.getGson().toJson(expired);
        System.out.println("expired Subscription: \n" + expiredJson);
        pref.edit().putString("expired", expiredJson).apply();
    }

    public SubscriptionDetails getSubscriptionDetails(){
        String subscriptionDetailsJson = pref.getString("expired", null);
        return GsonUtils.fromJson(subscriptionDetailsJson, SubscriptionDetails.class);
    }

    public void setUpgradeSubscription(List<NewSubscription> subscriptionList){
        String upgradeJson = GsonUtils.getGson().toJson(subscriptionList);
        System.out.println("update Subscription: \n" + upgradeJson);
        pref.edit().putString("upgrade", upgradeJson).apply();
    }

    public List<NewSubscription> getSubscriptionList(){
        String subscriptionListJson = pref.getString("upgrade", null);
        return GsonUtils.fromJson(subscriptionListJson, SubscriptionManager.upgradeListType);
    }


    public SubscriptionModel getSubscription(){
        String subscriptionJson = pref.getString("subscription", null);
        System.out.println("Retrieve Subscription: \n" + subscriptionJson);
        return GsonUtils.getGson().fromJson(subscriptionJson, SubscriptionModel.class);
    }

    public String getGuestId(){
        return pref.getString("guest_id", null);
    }

    public void setGuestId(String id){
        pref.edit().putString("guest_id", id).apply();
    }

    public Constants.UserType getUserType(){
        if (getUser() != null) {
            if (getSubscription() != null) {
                boolean isValid = System.currentTimeMillis() - DateUtils.getDateLong(getSubscription().getDate())
                        < TimeUnit.DAYS.toMillis(Long.parseLong(getSubscription().getValidity()));
                if (isValid)
                    return Constants.UserType.PAID;
                else return Constants.UserType.REGISTERED;
            } else return Constants.UserType.REGISTERED;
        } else
            return Constants.UserType.GUEST;
    }

    public boolean isPaid(){
        return getUserType() == Constants.UserType.PAID;
    }

    public boolean isGuest(){
        return getUserType() == Constants.UserType.GUEST;
    }

    public boolean showIntro(){
        return !pref.getBoolean("intro_completed", false);
    }

    public void setIntroCompleted(){
        pref.edit().putBoolean("intro_completed", true).apply();
    }

    public boolean isRegistered(){
        return getUserType() == Constants.UserType.REGISTERED;
    }

}
