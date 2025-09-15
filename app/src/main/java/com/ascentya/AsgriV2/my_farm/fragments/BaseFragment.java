package com.ascentya.AsgriV2.my_farm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;

import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.AccessManager;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.managers.SubscriptionManager;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    public static final String NAME = "name";

    protected String name = "name";

    protected HashMap<String, ArrayList<BaseActivity.ReceiverInterface>> receivers = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            name = getArguments().getString(NAME);
        }
    }

    public SessionManager getSessionManager(){
        return ((BaseActivity) getActivity()).getSessionManager();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void openActivity(Class activityClass, Pair<String, Object>... pairs){
        if (getActivity() != null && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).openActivity(activityClass, pairs);
    }

    public void toast(String message){
        ((BaseActivity) requireActivity()).toast(message);
    }

    public void errorToast(String message){
        ((BaseActivity) requireActivity()).errorToast(message);
    }

    public void registerReceiver(String key, BaseActivity.ReceiverInterface receiverInterface){
        if (getActivity() != null && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).registerReceiver(key, receiverInterface);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        System.out.println("Attach: " + getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        removeReceivers();
        super.onDetach();
        System.out.println("Detach: " + getClass().getSimpleName());
    }

    private void removeReceivers(){
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            for (String key : receivers.keySet()) {
                for (BaseActivity.ReceiverInterface receiverInterface : receivers.get(key)) {
                    ((BaseActivity) getActivity())
                            .unregisterReceiverInterface(receiverInterface);
                }
            }
            receivers.clear();
        }
    }

    public ViewDialog getViewDialog(){
        if(getActivity() != null && getActivity() instanceof BaseActivity){
            return ((BaseActivity) getActivity()).getViewDialog();
        }
        return null;
    }

    public void showLoading(){
        if (getViewDialog() != null)
            getViewDialog().showDialog();
    }

    public void hideLoading(){
        if (getViewDialog() != null)
            getViewDialog().hideDialog();
    }

    public boolean checkPrivilege(AccessManager.Modules.Module module){
        if (requireActivity() != null && requireActivity() instanceof BaseActivity)
            return ((BaseActivity) requireActivity()).checkPrivilege(module);
        return false;
    }

    public boolean checkPrivilege(AccessManager.Modules.Operation operation){
        if (requireActivity() != null && requireActivity() instanceof BaseActivity)
            return ((BaseActivity) requireActivity()).checkPrivilege(operation);
        return false;
    }

    public void openWithAccess(Class activityClass,
                               AccessManager.Modules.Module module,
                               Pair<String, Object>... pairs){
        if (requireActivity() != null && requireActivity() instanceof BaseActivity)
            ((BaseActivity) requireActivity()).openWithAccess(activityClass, module, pairs);
    }

    public void openWithAccess(Class activityClass, boolean newTask,
                               AccessManager.Modules.Module module,
                               Pair<String, Object>... pairs){
        if (requireActivity() != null && requireActivity() instanceof BaseActivity)
            ((BaseActivity) requireActivity()).openWithAccess(activityClass, newTask, module, pairs);
    }

    public void openWithAccess(Class activityClass,
                               AccessManager.Modules.Operation operation,
                               Pair<String, Object>... pairs){
        if (requireActivity() != null && requireActivity() instanceof BaseActivity)
            ((BaseActivity) requireActivity()).openWithAccess(activityClass, operation, pairs);
    }

    public void openWithAccess(Class activityClass, boolean newTask,
                               AccessManager.Modules.Operation operation,
                               Pair<String, Object>... pairs){
        if (requireActivity() != null && requireActivity() instanceof BaseActivity)
            ((BaseActivity) requireActivity()).openWithAccess(activityClass, newTask, operation, pairs);
    }

    public boolean checkCrop(Maincrops_Model land, ArrayList<Maincrops_Model> lands, BaseActivity.ChangeLand changeLand){
        if (!isResumed()){
            System.out.println("Skipping Check: " + getClass().getSimpleName());
            return !ApiHelper.getAllCrops(land).isEmpty();
        }
        if (requireActivity() instanceof BaseActivity)
            return ((BaseActivity) requireActivity()).checkCrop(land, lands, changeLand);
        return false;
    }

    public ModuleManager getModuleManager(){
        if (requireActivity() instanceof BaseActivity)
            return ((BaseActivity) requireActivity()).getModuleManager();
        return ModuleManager.getModuleManager(requireContext());
    }

    public SubscriptionManager getSubscriptionManager(){
        if (requireActivity() instanceof BaseActivity)
            return ((BaseActivity) requireActivity()).getSubscriptionManager();
        return SubscriptionManager.getSubscriptionManager(requireContext());
    }

    public boolean checkSubscription(Modules.Module module){
        return SubscriptionManager.getInstance(getContext()).with(getChildFragmentManager()).
                checkSubscription(module);
    }

    public boolean checkSubscription(Components.Component component){
        return SubscriptionManager.getInstance(getContext()).with(getChildFragmentManager()).
                checkSubscription(component);
    }

//    public boolean checkSubscription(Modules.Module module, String access){
//        return SubscriptionManager.getInstance(getContext()).with(getChildFragmentManager()).checkSubscription(module, access);
//    }
//
//    public boolean checkSubscription(Components.Component component, String access){
//        return SubscriptionManager.getInstance(getContext()).with(getChildFragmentManager()).checkSubscription(component, access);
//    }

    public boolean checkSubscription(Modules.Module module, String access){
        if (requireActivity() != null && requireActivity() instanceof BaseActivity)
            return ((BaseActivity) requireActivity()).checkSubscription(module, access);
        return false;
    }

    public boolean checkSubscription(Components.Component component, String access){
        if (requireActivity() != null && requireActivity() instanceof BaseActivity)
            return ((BaseActivity) requireActivity()).checkSubscription(component, access);
        return false;
    }

//    public SubscriptionManager checkSubscription(Modules.Module module){
//        if (requireActivity() != null && requireActivity() instanceof BaseActivity)
//            return ((BaseActivity) requireActivity()).checkSubscription(module);
//        return SubscriptionManager.getSubscriptionManager(requireContext());
//    }

//    public boolean checkSubscription(Components.Component component){
//        return SubscriptionManager.getInstance(getContext()).with(getChildFragmentManager()).
//                checkSubscription(activityToOpen, component, access);
//    }
//

    public void openWithSubscription(Class activityClass,
                                     Components.Component component,
                                     String access,
                                     Pair<String, Object>... pairs){
        boolean hasAccess = SubscriptionManager
                .getSubscriptionManager(getContext())
                .with(getChildFragmentManager())
                .checkSubscription(component, access);
        if (hasAccess) openActivity(activityClass, pairs);
    }


    public void openWithSubscription(Class activityClass,
                                     boolean newTask,
                                     Components.Component component,
                                     String access,
                                     Pair<String, Object>... pairs){
        boolean hasAccess = SubscriptionManager
                .getSubscriptionManager(getContext())
                .with(getChildFragmentManager())
                .checkSubscription(component, access);
        if (hasAccess) openActivity(activityClass,  pairs);
    }
}
