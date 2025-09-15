package com.ascentya.AsgriV2.Shared_Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.ascentya.AsgriV2.Models.DeviceData;
import com.ascentya.AsgriV2.Models.DistrictModel;
import com.ascentya.AsgriV2.Models.ObjectAccess;
import com.ascentya.AsgriV2.Models.StateModel;
import com.ascentya.AsgriV2.Models.Subscription;
import com.ascentya.AsgriV2.Models.TalukModel;
import com.ascentya.AsgriV2.Models.UserPrivilege;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.Models.ZoneReport;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.e_market.data.model.Address;
import com.ascentya.AsgriV2.my_farm.model.CropHistory;
import com.ascentya.AsgriV2.my_farm.model.CropInfectionType;
import com.ascentya.AsgriV2.my_farm.model.InfectionType;
import com.ascentya.AsgriV2.my_farm.model.SoilType;
import com.ascentya.AsgriV2.utility.model.SoilTest;
import com.ascentya.AsgriV2.utility.model.SoilTestLab;
import com.ascentya.AsgriV2.utility.model.WaterTest;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class EMarketStorage {

    private static String ADDRESS_LIST = "address_list";
    private static String SELECTED_ADDRESS = "selected_address";
    private static String ADDRESS_INDEX = "address_index";

    Type addressListType = new TypeToken<List<Address>>(){}.getType();
    Type addressType = Address.class;

    public static final Type deviceDataToken = new TypeToken<List<DeviceData>>(){}.getType();
    public static final Type SoilTestRequestListType = new TypeToken<List<SoilTest>>(){}.getType();
    public static final Type WaterTestRequestListType = new TypeToken<List<WaterTest>>(){}.getType();
    public static final Type SoilTestLabListType = new TypeToken<List<SoilTestLab>>(){}.getType();
    public static final Type ZoneReportListType = new TypeToken<List<ZoneReport>>(){}.getType();
    public static final Type SoilReportListType = new TypeToken<List<SoilType>>(){}.getType();
    public static final Type InfectionReportListType = new TypeToken<List<InfectionType>>(){}.getType();
    public static final Type CropReportListType = new TypeToken<List<CropInfectionType>>(){}.getType();
    public static final Type stateListType = new TypeToken<List<StateModel>>(){}.getType();
    public static final Type districtListType = new TypeToken<List<DistrictModel>>(){}.getType();
    public static final Type villageListType = new TypeToken<List<TalukModel>>(){}.getType();
    public static final Type subscriptionListType = new TypeToken<List<Subscription>>(){}.getType();
    public static final Type userPrivilegeListType = new TypeToken<List<UserPrivilege>>(){}.getType();
    public static final Type objectAccessListType = new TypeToken<List<ObjectAccess>>(){}.getType();
    public static final Type varietyListType = new TypeToken<List<VarietyModel>>(){}.getType();
    public static final Type cropHistoryListType = new TypeToken<List<CropHistory>>(){}.getType();

    private static EMarketStorage instance;
    private SharedPreferences preferences;

    private EMarketStorage(Context context){
        preferences = context.getSharedPreferences("emarket_storage", 0);
    }

    public void saveAddress(Address address){
        String addressListJson = getString(ADDRESS_LIST);

        if (addressListJson == null){
            addressListJson = new JSONArray().toString();
        }

        try {
            JSONArray addressJsonArray = new JSONArray(addressListJson);
            address.setId(getUpdatedAddressIndex());
            JSONObject addressJson = new JSONObject(GsonUtils.getGson().toJson(address));
            addressJsonArray.put(addressJson);
            saveString(ADDRESS_LIST, addressJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Address getSelectedAddress(){
        int addressId = getSelectedAddressId();
        return getAddress(addressId);
    }

    public Address getAddress(int addressId){
        for (Address address: getAllAddress()){
            if (address.getId() == addressId)
                return address;
        }
        return null;
    }

    public void updateAddress(@NonNull Address address){
        deleteAddress(address.getId());
        saveAddress(address);
    }

    public void deleteAddress(int addressId){
        String addressListJson = getString(ADDRESS_LIST);

        if (addressListJson == null){
            addressListJson = new JSONArray().toString();
        }

        try {
            JSONArray addressJsonArray = new JSONArray(addressListJson);
            for (int i=0; i<addressJsonArray.length(); i++){
                JSONObject addressJson = addressJsonArray.getJSONObject(i);
                if (addressJson.optInt("id") == addressId){
                    addressJsonArray.remove(i);
                    break;
                }
            }
            saveString(ADDRESS_LIST, addressJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Address> getAllAddress(){
        String addressListJson = getString(ADDRESS_LIST);

        if (addressListJson == null){
            return new ArrayList<>();
        }

        return GsonUtils.getGson().fromJson(addressListJson, addressListType);
    }

    public int getSelectedAddressId(){ return getInt(SELECTED_ADDRESS); }

    public void setSelectedAddressId(int addressId){
        saveInt(SELECTED_ADDRESS, addressId);
    }

    private String getString(String key){
        return preferences.getString(key, null);
    }

    private void saveString(String key, String value){
        preferences.edit().putString(key, value).apply();
    }

    private int getInt(String key){
        return preferences.getInt(key, 0);
    }

    private void saveInt(String key, int value){
        preferences.edit().putInt(key, value).apply();
    }

    private int getUpdatedAddressIndex(){
        int addressIndex = getInt(ADDRESS_INDEX) + 1;
        saveInt(ADDRESS_INDEX, addressIndex);
        return addressIndex;
    }

    public static synchronized EMarketStorage getInstance(Context context){
        if (instance == null)
            instance = new EMarketStorage(context);
        return instance;
    }
}
