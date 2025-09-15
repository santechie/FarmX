package com.ascentya.AsgriV2.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.ObjectAccess;
import com.ascentya.AsgriV2.Models.SubscriptionModel;
import com.ascentya.AsgriV2.Models.UserPrivilege;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Shared_Preference.Userobject;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessManager {

    private static AccessManager instance = null;

    private Context context;
    private SessionManager sessionManager;

    private ArrayList<UserPrivilege> userPrivilegeList = new ArrayList<>();
    private ArrayList<ObjectAccess> objectAccessList = new ArrayList<>();

    private AccessManager(Context context){
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    public ArrayList<UserPrivilege> getUserPrivilegeList(){
        return userPrivilegeList;
    }

    public void setUserPrivilegeList(ArrayList<UserPrivilege> userPrivilegeList){
        this.userPrivilegeList.clear();
        this.userPrivilegeList.addAll(userPrivilegeList);
    }

    @SuppressLint("HardwareIds")
    public void checkAccess(Status status){

        String userId = null;
        String os = "android";
        String versionStatus = null;
        String versionName = null;
        String version = null;
        String manufacture = null;
        String model = null;
        String ipAddress = null;
        String macAddress = null;
        String imei = null;
        String deviceId = null;
        String network = null;

        try {
            Userobject user = sessionManager.getUser();
            userId = user != null ? user.getId() : null;
            macAddress = getMACAddress("eth0");
            version = Build.VERSION.RELEASE;
            versionStatus = Build.VERSION.CODENAME;
            versionName = Build.VERSION.CODENAME;
            manufacture = Build.MANUFACTURER;
            model = Build.MODEL;
            ipAddress = getIPAddress(true);
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            network = telephonyManager.getNetworkOperatorName();
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            imei = telephonyManager.getDeviceId();
        }catch (Exception e){
            e.printStackTrace();
        }

        final String UserId = userId;
        ANRequest.PostRequestBuilder builder = AndroidNetworking.post(Webservice.checkAccess);
        addIfValid(builder, "user_id", userId);
        addIfValid(builder, "imei", imei);
        addIfValid(builder, "device_id", deviceId);
        addIfValid(builder, "manufacture", manufacture);
        addIfValid(builder, "model", model);
        addIfValid(builder, "os", os);
        addIfValid(builder, "version", version);
        addIfValid(builder, "version_name", versionName);
        addIfValid(builder, "version_status", versionStatus);
        addIfValid(builder, "ip_address", ipAddress);
        addIfValid(builder, "mac_address", macAddress);
        addIfValid(builder, "network", network);

        System.out.println(userId);
        System.out.println(imei);
        System.out.println(deviceId);
        System.out.println(manufacture);
        System.out.println(model);
        System.out.println(os);
        System.out.println(version);
        System.out.println(versionName);
        System.out.println(versionStatus);
        System.out.println(ipAddress);
        System.out.println(macAddress);
        System.out.println(network);



        builder.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                if (UserId != null && UserHelper.checkResponse(context, response)){
                    return;
                }
                System.out.println("Access: \n" + response);

                try {
                    if(response.getBoolean("status")){
                        status.onAccept();
                    }else {
                        status.onDenied(response.optString("image"),
                                response.optString("message"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    status.onDenied("", "Parse Error!");
                }
            }

            @Override
            public void onError(ANError anError) {
                try {
                    anError.printStackTrace();
                    System.out.println("Error: \n" +
                            GsonUtils.getGson().toJson(anError));
                }catch (Exception e) { e.printStackTrace(); }
                status.onDenied("", "Server Error!");
            }
        });
    }


    public boolean canLoadPrivileges(){
        return sessionManager.getUser() != null;
    }

    public void loadPrivileges(LoadAction loadAction){
        Userobject userobject = sessionManager.getUser();
        if (userobject == null){
            loadAction.onComplete(true);
            return;
        }
      //  https://vrjaitraders.com/ard_farmx/api/Access/get_privileges
      //  AndroidNetworking.post(Webservice.getUserPrivileges)
        AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Access/get_privileges")
                .addUrlEncodeFormBodyParameter("user_id", userobject.getId())
                .build().getAsJSONObject(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (UserHelper.checkResponse(context, response)){
                            return;
                        }
                        try {
                            if (response.getBoolean("status")){
                                userPrivilegeList.clear();
                                userPrivilegeList.addAll(GsonUtils
                                        .fromJson(response.getJSONArray("data").toString(),
                                        EMarketStorage.userPrivilegeListType));
                                sessionManager.setUserPrivileges(userPrivilegeList);
                                try {

                                    if (response.has("objects")
                                            && !response.isNull("objects")){
                                        objectAccessList.clear();
                                        objectAccessList.addAll(GsonUtils.getGson()
                                                .fromJson(response.getJSONArray("objects").toString(),
                                                        EMarketStorage.objectAccessListType));
                                    }

                                    if (response.has("subscription")
                                            && !response.isNull("subscription")){
                                        sessionManager.setSubscription(GsonUtils.getGson()
                                                .fromJson(response.getJSONObject("subscription").toString(),
                                                        SubscriptionModel.class));
                                    }

                                }catch (Exception e){ e.printStackTrace(); }
                                System.out.println("User Privileges: "
                                        + GsonUtils.toJson(userPrivilegeList));
                                loadAction.onComplete(false);
                            }else {
                                loadAction.onComplete(true);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            loadAction.onComplete(true);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            System.out.println("User Privileges Error: "
                                    + GsonUtils.toJson(anError));
                        }catch (Exception e) {}
                        anError.printStackTrace();
                        loadAction.onComplete(true);
                    }
                }
        );
    }

    public UserPrivilege getPrivilege(Modules.Module module){
        for (UserPrivilege userPrivilege: userPrivilegeList){
            if (userPrivilege.getModule().equals(module.module)){
                return userPrivilege;
            }
        }
        return null;
    }

    public UserPrivilege getPrivilege(Modules.Operation operation){
        for (UserPrivilege userPrivilege: userPrivilegeList){
            if (userPrivilege.getModule().equals(operation.module.module)
                    && userPrivilege.getOperation() != null
                    && userPrivilege.getOperation().equals(operation.operation)){
                return userPrivilege;
            }
        }
        return null;
    }

    public ObjectAccess getObjectAccess(Modules.Object object){
        System.out.println("Objects: " + objectAccessList.size());
        for (ObjectAccess objectAccess: objectAccessList){
            UserPrivilege userPrivilege = getPrivilege(object.operation);
            System.out.println("Object Access: " + GsonUtils.toJson(objectAccess));
            if (userPrivilege != null
                    && userPrivilege.getOperationId() != null
                    && userPrivilege.getOperationId().equals(userPrivilege.getOperationId())
                    && objectAccess.getValue().equals(object.object)){
                return objectAccess;
            }
        }

        return null;
    }

    private void addIfValid(ANRequest.PostRequestBuilder builder, String name, String value){
        if (value != null) builder.addUrlEncodeFormBodyParameter(name, value);
    }

    public String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    String hex = Integer.toHexString(b & 0xFF);
                    if (hex.length() == 1)
                        hex = "0".concat(hex);
                    res1.append(hex.concat(":"));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:",aMac));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }


    public static synchronized AccessManager getInstance(Context context){
        if(instance == null){
            instance = new AccessManager(context);
        }
        return instance;
    }

    public static class STATUS{
        public static final String ALLOWED = "1";
        public static final String DENIED = "2";
        public static final String SHOW_PAYMENT = "3";
        public static final String OBJECT_ACCESS = "4";
    }

    public static class Modules{

        // MySQL table `asgri_tn.modules`

        private static final String S_AGRIPEDIA = "agripedia";
        private static final String S_MY_CROPS = "my_crops";
        private static final String S_CHATS = "chats";
        private static final String S_SCHEME = "scheme";
        private static final String S_COMMUNITY = "community";
        private static final String S_MARKET_NEWS = "market_news";
        private static final String S_BUY_AND_SELL = "buy_and_sell";
        private static final String S_HISTORY = "history";
        private static final String S_EMARKET = "emarket";
        private static final String S_UTILITY = "utility";
        private static final String S_SOIL_TEST = "soil_test";
        private static final String S_WATER_TEST = "water_test";

        private static final String S_CROPS = "crops";

        public static class Module{
            protected String module, operation;
            protected static Module instance = null;
            public Module(String module){
                this.module = module;
                this.instance = this;
            }
        }

        public static class Operation{
            protected Module module;
            protected String operation;
            private Operation(Module module, String operation){
                this.module = module;
                this.operation = operation;
            }
        }

        public static class Object{
            protected Operation operation;
            protected String object;
            private Object(Operation operation, String object){
                this.operation = operation;
                this.object = object;
            }

            public Operation getOperation(){
                return operation;
            }
        }

        // MySQL table `asgri_tn.operations`

        public static final Module AGRIPEDIA = new Module(S_AGRIPEDIA);
        public static final Module MY_CROPS = new Module(S_MY_CROPS);
        public static final Module CHATS = new Module(S_CHATS);
        public static final Module SCHEME = new Module(S_SCHEME);
        public static final Module COMMUNITY = new Module(S_COMMUNITY);
        public static final Module MARKET_NEWS = new Module(S_MARKET_NEWS);
        public static final Module BUY_AND_SELL = new Module(S_BUY_AND_SELL);
        public static final Module HISTORY = new Module(S_HISTORY);
        public static final Module EMARKET = new Module(S_EMARKET);
        public static final Module UTILITY = new Module(S_UTILITY);
        public static final Module SOIL_TEST = new Module(S_SOIL_TEST);
        public static final Module WATER_TEST = new Module(S_WATER_TEST);

        // Agripedia

        public static final Operation VIEW_CROP = new Operation(AGRIPEDIA, "view_crop");

        // My Farm

        public static final Operation ADD_LAND = new Operation(MY_CROPS, "add_land");
        public static final Operation VIEW_LAND = new Operation(MY_CROPS, "view_land");
        public static final Operation VIEW_CROPS = new Operation(MY_CROPS, "view_crops");
        public static final Operation VIEW_ZONES = new Operation(MY_CROPS, "view_zones");
        public static final Operation VIEW_DEVICE_DATA = new Operation(MY_CROPS, "view_iot_data");
        public static final Operation VIEW_PEST_DISEASES = new Operation(MY_CROPS, "view_pest_diseases");
        public static final Operation VIEW_PEST_HISTORY = new Operation(MY_CROPS, "view_pest_history");
        public static final Operation ADD_PEST_DISEASES = new Operation(MY_CROPS, "add_pest_diseases");
        public static final Operation ADD_REMEDY = new Operation(MY_CROPS, "add_remedy");
        public static final Operation VIEW_LAND_ACTIVITIES = new Operation(MY_CROPS, "view_land_activities");
        public static final Operation ADD_LAND_ACTIVITIES = new Operation(MY_CROPS, "add_land_activities");
        public static final Operation VIEW_EXPENSE = new Operation(MY_CROPS, "view_expense");
        public static final Operation VIEW_INCOME = new Operation(MY_CROPS, "view_income");
        public static final Operation VIEW_STOCKS = new Operation(MY_CROPS, "view_stocks");
        public static final Operation ADD_STOCKS = new Operation(MY_CROPS, "add_stocks");
        public static final Operation EDIT_STOCKS = new Operation(MY_CROPS, "edit_stocks");
        public static final Operation DELETE_STOCKS = new Operation(MY_CROPS, "delete_stocks");
        public static final Operation VIEW_MEMBERS = new Operation(MY_CROPS, "view_members");
        public static final Operation ADD_MEMBERS = new Operation(MY_CROPS, "add_members");
        public static final Operation DELETE_MEMBERS = new Operation(MY_CROPS, "delete_members");
        public static final Operation ADD_INCOME = new Operation(MY_CROPS, "add_income");
        public static final Operation ADD_ZONES = new Operation(MY_CROPS, "add_zones");

        // Schemes

        public static final Operation VIEW_SCHEME = new Operation(SCHEME, "view_scheme");

        // Community

        public static final Operation POST_COMMUNITY = new Operation(COMMUNITY, "post_community");
        public static final Operation VIEW_COMMUNITY = new Operation(COMMUNITY, "view_community");

        // Buy & Sell

        public static final Operation VIEW_PRODUCTS = new Operation(BUY_AND_SELL, "view_products");
        public static final Operation BUY_PRODUCTS = new Operation(BUY_AND_SELL, "buy_products");
        public static final Operation ADD_PRODUCT = new Operation(BUY_AND_SELL, "add_product");
        public static final Operation EDIT_PRODUCT = new Operation(BUY_AND_SELL, "edit_product");
        public static final Operation DELETE_PRODUCT = new Operation(BUY_AND_SELL, "delete_product");

        // eMarket

        public static final Operation EM_VIEW_PRODUCTS = new Operation(EMARKET, "view_products");
        public static final Operation EM_BUY_PRODUCTS = new Operation(EMARKET, "buy_products");

        // Soil Test

        public static final Operation ST_NEW_REQUEST = new Operation(SOIL_TEST, "new_request");
        public static final Operation ST_VIEW_REQUEST = new Operation(SOIL_TEST, "view_request");
        public static final Operation ST_VIEW_RESULT = new Operation(SOIL_TEST, "view_result");

        // Water Test

        public static final Operation WT_NEW_REQUEST = new Operation(WATER_TEST, "new_request");
        public static final Operation WT_VIEW_REQUEST = new Operation(WATER_TEST, "view_request");
        public static final Operation WT_VIEW_RESULT = new Operation(WATER_TEST, "view_result");

        // Object

        public static final Object CROPS = new Object(VIEW_CROP, S_CROPS);

    }

    public interface LoadAction{
        void onComplete(boolean isError);
    }

    public interface Action{
        void onUpdate(boolean isAllowed);
    }

    public interface Status{
        void onAccept();
        void onDenied(String image, String message);
    }
}
