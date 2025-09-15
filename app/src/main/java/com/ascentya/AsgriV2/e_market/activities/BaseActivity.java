package com.ascentya.AsgriV2.e_market.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;

import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.ObjectAccess;
import com.ascentya.AsgriV2.Models.ObjectItem;
import com.ascentya.AsgriV2.Models.Subscription;
import com.ascentya.AsgriV2.Models.UserPrivilege;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.IntentUtils;
import com.ascentya.AsgriV2.Utils.LocaleHelper;
import com.ascentya.AsgriV2.Utils.MyApplication;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.dialog.PayDialog;
import com.ascentya.AsgriV2.dialog.RegisterDialog;
import com.ascentya.AsgriV2.e_market.data.CartManager;
import com.ascentya.AsgriV2.e_market.data.repositeries.DataRepository;
import com.ascentya.AsgriV2.login_activities.Farmer_Registration;
import com.ascentya.AsgriV2.managers.AccessManager;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.managers.SubscriptionManager;
import com.ascentya.AsgriV2.my_farm.activities.AddCropActivity;
import com.ascentya.AsgriV2.my_farm.dialogs.AddCropDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import es.dmoral.toasty.Toasty;

public class BaseActivity extends AppCompatActivity implements PayDialog.Action, AddCropDialog.Action {

    private boolean backEnabled = false,
            setOverrideOnBackPressed = true,
            isBackPressed = false,
            overrideOnPause = false;
    private int menuId = -1;
    protected DataRepository dataRepository;
    protected CartManager cartManager;
    protected EMarketStorage storage;
    public SessionManager sessionManager;
    private ViewDialog viewDialog;
    private HashMap<String, BroadcastReceiver> receivers = new HashMap<>();
    private HashMap<String, ArrayList<ReceiverInterface>> interfaces = new HashMap<>();
    private PayDialog payDialog;
    private AddCropDialog cropDialog = null;

    private ChangeLand currentChangeLand;

    private ModuleManager moduleManager;
    private SubscriptionManager subscriptionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moduleManager = ModuleManager.getModuleManager(this);
        subscriptionManager = SubscriptionManager.getSubscriptionManager(this);
        dataRepository = (DataRepository) DataRepository.getInstance(this);
        cartManager = CartManager.getInstance(this);
        storage = EMarketStorage.getInstance(this);
        sessionManager = new SessionManager(this);
        viewDialog = new ViewDialog(this);
        payDialog = new PayDialog(this);
        cropDialog = new AddCropDialog(this, this);
    }

    public ModuleManager getModuleManager(){
        return moduleManager;
    }

    public SubscriptionManager getSubscriptionManager(){
        return subscriptionManager;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    public AccessManager getAccessManager() {
        return ((MyApplication) getApplication()).getAccessManager();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    protected void loadUserPrivileges() {
        loadUserPrivileges(false);
    }

    protected void loadUserPrivileges(boolean showLoading) {
        loadUserPrivileges(showLoading, null);
    }

    protected void loadUserPrivileges(boolean showLoading, Result result) {
        if (getAccessManager().canLoadPrivileges()) {
            if (showLoading) showLoading();
            getAccessManager().loadPrivileges(new AccessManager.LoadAction() {
                @Override
                public void onComplete(boolean isError) {
                    if (showLoading) hideLoading();
                    if (isError) {
                        if (result == null) {
                            Toasty.error(BaseActivity.this, "User Privileges Load Error!").show();
                        } else {
                            result.onFinished(true);
                        }
                    } else {
                        if (result != null) result.onFinished(false);
                        System.out.println("User Privileges Loaded!");
                    }
                }
            });
        }
    }

    protected void setOverrideOnBackPressed(boolean override) {
        setOverrideOnBackPressed = override;
    }

    protected void setOverrideOnPause() {
        overrideOnPause = true;
    }

    protected void setActionBar(@IdRes int actionBarId) {
        Toolbar actionBar = findViewById(actionBarId);
        if (actionBar != null)
            setSupportActionBar(actionBar);
    }

    protected void setToolbarTitle(String title, Boolean enableBack) {
        getSupportActionBar().setTitle(title);
        backEnabled = enableBack;
        getSupportActionBar().setDisplayHomeAsUpEnabled(backEnabled);
    }

    protected void setMenu(int menuId) {
        this.menuId = menuId;
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuId != -1) {
            getMenuInflater().inflate(menuId, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (backEnabled)
            onBackPressed();
        return backEnabled;
    }

    public void openActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public void openActivity(Class activityClass, boolean newTask) {
        Intent intent = new Intent(this, activityClass);
        if (newTask)
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(intent);
    }

    public void openActivity(Class activityClass, boolean newTask, Pair<String, Object>... pairs) {
        Intent intent = new Intent(this, activityClass);

        for (Pair pair : pairs) {

            String key = (String) pair.first;
            Object value = pair.second;

            if (value instanceof Integer) {
                intent.putExtra(key, (int) value);
            } else if (value instanceof Long) {
                intent.putExtra(key, (Long) value);
            } else if (value instanceof Float) {
                intent.putExtra(key, (Float) value);
            } else if (value instanceof Double) {
                intent.putExtra(key, (Double) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (Boolean) value);
            } else if (value instanceof String) {
                intent.putExtra(key, (String) value);
            } else {
                intent.putExtra(key, GsonUtils.getGson().toJson(value));
            }

        }

        if (newTask) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        startActivity(intent);

        if (newTask) {
            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void openActivity(Class activityClass, Pair<String, Object>... pairs) {
        Intent intent = new Intent(this, activityClass);

        for (Pair pair : pairs) {

            String key = (String) pair.first;
            Object value = pair.second;

            if (value instanceof Integer) {
                intent.putExtra(key, (int) value);
            } else if (value instanceof Long) {
                intent.putExtra(key, (Long) value);
            } else if (value instanceof Float) {
                intent.putExtra(key, (Float) value);
            } else if (value instanceof Double) {
                intent.putExtra(key, (Double) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (Boolean) value);
            } else if (value instanceof String) {
                intent.putExtra(key, (String) value);
            } else {
                intent.putExtra(key, GsonUtils.getGson().toJson(value));
            }

        }

        startActivity(intent);
    }

    public boolean hasValueInIntent(String key) {
        return getIntent().hasExtra(key);
    }

    public <T> T getFromIntent(String key, Type type) {
        String value = getIntent().getStringExtra(key);
        return GsonUtils.getGson().fromJson(value, type);
    }

    @Override
    public void onBackPressed() {
        isBackPressed = true;
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (setOverrideOnBackPressed && isBackPressed) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            isBackPressed = false;
        }

        if (overrideOnPause) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    protected void openWhatsApp(String number) {
        IntentUtils.openWhatsApp(this, number);
    }

    protected void openCall(String number) {
        IntentUtils.openCall(this, number);
    }

    public void showLoading() {
        viewDialog.showDialog();
    }

    public void hideLoading() {
        viewDialog.hideDialog();
    }

    protected void checkAccess(AccessManager.Action action) {
        AccessManager.Status status = new AccessManager.Status() {
            @Override
            public void onAccept() {
                action.onUpdate(true);
            }

            @Override
            public void onDenied(String image, String message) {
                Toasty.error(BaseActivity.this, message).show();
            }
        };
        AccessManager.getInstance(this).checkAccess(status);
    }

    protected void userTypeAction(UserTypeAction userTypeAction) {
        if (userTypeAction != null) {
            switch (sessionManager.getUserType()) {
                case GUEST:
                    userTypeAction.onGuest();
                    break;
                case REGISTERED:
                    userTypeAction.onRegistered();
                    break;
                case PAID:
                    userTypeAction.onPaid();
                    break;
            }
        }
    }

    public void showRegisterDialog() {
        new RegisterDialog(() -> {
            openActivity(Farmer_Registration.class);
            //finish();
        }).show(getSupportFragmentManager(), null);
    }

    public void showPayDialog() {
        if (payDialog != null && !isPayShowing())
            payDialog.show(getSupportFragmentManager(), "pay_dialog");
    }


    public boolean isPayShowing() {
        return payDialog != null && payDialog.isVisible();
    }

    @Override
    public void onPayNow(Subscription subscription) {

    }

    public void sendBroadcast(String broadcast) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(broadcast));
    }

    public void registerReceiver(String key, ReceiverInterface receiverInterface) {
        if (!receivers.containsKey(key)) {
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ArrayList<ReceiverInterface> interfaceList = interfaces.get(key);
                    for (ReceiverInterface ri : interfaceList) {
                        try {
                            ri.onReceive(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(key));
            receivers.put(key, receiver);
        }

        if (!interfaces.containsKey(key)) {
            ArrayList<ReceiverInterface> interfaceList = new ArrayList<>();
            interfaces.put(key, interfaceList);
        }

        ArrayList<ReceiverInterface> interfaceArrayList = interfaces.get(key);
        interfaceArrayList.add(receiverInterface);
    }

    public void unregisterReceiverInterface(ReceiverInterface receiverInterface) {
        for (String key : interfaces.keySet()) {
            ArrayList<ReceiverInterface> receiverInterfaces = interfaces.get(key);
            receiverInterfaces.remove(receiverInterface);
            /*if (receiverInterfaces.isEmpty()){
                LocalBroadcastManager.getInstance(this).unregisterReceiver(receivers.get(key));
                interfaces.remove(key);
            }*/
        }
    }

    public void unregisterAll() {
        for (String key : receivers.keySet()) {
            BroadcastReceiver broadcastReceiver = receivers.get(key);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
        receivers.clear();
        interfaces.clear();
    }

    public void toast(String message) {
        Toasty.normal(this, message).show();
    }

    public void errorToast(String message) {
        Toasty.error(this, message).show();
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        unregisterAll();
        super.onDestroy();
        System.out.println("On Destroy: " + getSupportFragmentManager().findFragmentByTag("add_crop"));
    }

    @Override
    public void onAddCrop(Maincrops_Model land) {
        openAddCrop(land);
    }

    @Override
    public void onChangeLand() {
        if(currentChangeLand != null) currentChangeLand.onChangeLand();
    }

    @Override
    public void onClose() {
        finish();
    }

    public interface ReceiverInterface {
        void onReceive(Intent intent);
    }

    public interface UserTypeAction {
        void onGuest();
        void onRegistered();
        void onPaid();
    }

    public ViewDialog getViewDialog() {
        return viewDialog;
    }

    public interface Result {
        void onFinished(boolean error);
    }

    public boolean checkPrivilege(AccessManager.Modules.Module module) {
        UserPrivilege userPrivilege = getAccessManager().getPrivilege(module);
        boolean hasAccess = userPrivilege != null && userPrivilege.getStatus().equals("1");
        if (!hasAccess) {
            accessDenied(userPrivilege);
        }
        return hasAccess;
    }

    public boolean checkPrivilege(AccessManager.Modules.Operation operation) {
        UserPrivilege userPrivilege = getAccessManager().getPrivilege(operation);
        boolean hasAccess = userPrivilege != null
                && (userPrivilege.getStatus().equals("1")
                || userPrivilege.getStatus().equals("4"));
        if (!hasAccess) {
            accessDenied(userPrivilege);
        }
        return hasAccess;
    }

    public boolean checkPrivilege(AccessManager.Modules.Object object, String objectId){
        boolean hasAccess = checkPrivilege(object.getOperation());
        if (hasAccess) {
            UserPrivilege userPrivilege = getAccessManager().getPrivilege(object.getOperation());
            if (userPrivilege.getStatus().equals(AccessManager.STATUS.OBJECT_ACCESS)) {
                ObjectAccess objectAccess = getAccessManager().getObjectAccess(object);
                System.out.println("Object Access: " + GsonUtils.toJson(objectAccess));
                if (objectAccess != null && !objectAccess.getAccesses().isEmpty()) {
                    for (ObjectItem objectItem : objectAccess.getAccesses()) {
                        System.out.println(GsonUtils.toJson(objectItem));
                        if (objectItem.getContentId().equals(objectId)) {
                            return hasAccess;
                        }
                    }
                }
                objectAccessDenied(objectAccess);
                return false;
            }else{
                return hasAccess;
            }
        }else {
            return hasAccess;
        }
    }

    private void accessDenied(UserPrivilege userPrivilege) {
        if (userPrivilege == null) {
            toast("Error User Privileges");
            return;
        }

        if ("3".equals(userPrivilege.getStatus())) {
            showPayDialog();
        } else if("4".equals(userPrivilege.getStatus())){

        } else {
            toast(userPrivilege.getMessage());
        }
    }

    private void objectAccessDenied(ObjectAccess objectAccess){
        if (objectAccess == null) {
            toast("Error Object Access");
            return;
        }

        if ("3".equals(objectAccess.getStatus())){
            toast(objectAccess.getMessage());
            showPayDialog();
        }else {
            toast(objectAccess.getMessage());
        }
    }

//    public void checkResponse(){
//
//    }

    public void openWithAccess(Class activityClass,
                               AccessManager.Modules.Module module,
                               Pair<String, Object>... pairs) {
        if (checkPrivilege(module)) {
            openActivity(activityClass, pairs);
        }
    }

    public void openWithAccess(Class activityClass, boolean newTask,
                               AccessManager.Modules.Module module,
                               Pair<String, Object>... pairs) {
        if (checkPrivilege(module)) {
            openActivity(activityClass, newTask, pairs);
        }
    }

    public void openWithAccess(Class activityClass,
                               AccessManager.Modules.Operation operation,
                               Pair<String, Object>... pairs) {
        if (checkPrivilege(operation)) {
            openActivity(activityClass, pairs);
        }
    }

    public void openWithAccess(Class activityClass, boolean newTask,
                               AccessManager.Modules.Operation operation,
                               Pair<String, Object>... pairs) {
        if (checkPrivilege(operation)) {
            openActivity(activityClass, newTask, pairs);
        }
    }

    @SuppressLint("SuspiciousIndentation")
    public boolean checkCrop(Maincrops_Model land, ArrayList<Maincrops_Model> lands, ChangeLand changeLand){
        this.currentChangeLand = changeLand;
        if (land == null) return false;
        if (ApiHelper.getAllCrops(land).isEmpty()){
            if (getModuleManager().canInsert(Components.MyFarm.CROP))
            showAddCropDialog(land, lands);
            return false;
        }
        return true;
    }

    public boolean isCropDialogShowing(){
        System.out.println("Show Thread: " + cropDialog.isVisible());
        return cropDialog != null && cropDialog.isVisible();
    }

    public void showAddCropDialog(Maincrops_Model land, ArrayList<Maincrops_Model> lands){
        if (!isCropDialogShowing()) cropDialog.show(land, lands != null && lands.size() > 1,
                getSupportFragmentManager(), "add_crop");
    }

    public void openAddCrop(Maincrops_Model land){
        if(land != null)
            openActivity(AddCropActivity.class, Pair.create("land_id", land.getId()));
        else openActivity(AddCropActivity.class);
    }

    public interface ChangeLand{
        void onChangeLand();
    }

    public boolean checkSubscription(Modules.Module module){
        return SubscriptionManager.getInstance(this)
                .with(getSupportFragmentManager())
                .checkSubscription(module);
    }

    public boolean checkSubscription(Components.Component component){
        return SubscriptionManager.getInstance(this).with(getSupportFragmentManager()).checkSubscription(component);
    }

    public boolean checkSubscription(Class activityToOpen, Components.Component component){
        return SubscriptionManager.getInstance(this).with(getSupportFragmentManager()).checkSubscription(activityToOpen,component);
    }

    public boolean checkSubscription(Components.Component component, String access){
        return SubscriptionManager.getInstance(this).with(getSupportFragmentManager())
                .checkSubscription(component, access);
    }

    public boolean checkSubscription(Modules.Module module, String access){
        return SubscriptionManager.getInstance(this).with(getSupportFragmentManager()).checkSubscription(module, access);
    }

//    public boolean checkS

    public void openWithSubscription(Class activityClass,
                                     Modules.Module module,
                                     String access,
                                     Pair<String, Object>... pairs){
        boolean hasAccess = SubscriptionManager
                .getSubscriptionManager(this)
                .with(getSupportFragmentManager())
                .checkSubscription(module, access);
        if (hasAccess) openActivity(activityClass, false, pairs);
    }

    public void openWithSubscription(Class activityClass,
                                     Components.Component component,
                                     String access,
                                     Pair<String, Object>... pairs){
        boolean hasAccess = SubscriptionManager
                .getSubscriptionManager(this)
                .with(getSupportFragmentManager())
                .checkSubscription(component, access);
        if (hasAccess) openActivity(activityClass, false, pairs);
    }

    public void openWithSubscription(Class activityClass,
                                     boolean newTask,
                                     Components.Component component,
                                     String access,
                                     Pair<String, Object>... pairs){
        boolean hasAccess = SubscriptionManager
                .getSubscriptionManager(this)
                .with(getSupportFragmentManager())
                .checkSubscription(component, access);
        if (hasAccess) openActivity(activityClass, newTask, pairs);
    }
}