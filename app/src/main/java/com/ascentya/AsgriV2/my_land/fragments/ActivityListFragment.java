package com.ascentya.AsgriV2.my_land.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Add_HelperActivity;
import com.ascentya.AsgriV2.Adapters.Activity_Adapter;
import com.ascentya.AsgriV2.Models.Activity_Cat_Model;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityListFragment extends BaseFragment implements Activity_Adapter.Action {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String LAND = "land";
    private static final String CROP = "crop_id";
    private static final String VARIETY_ID = "variety_id";
    private static final String LC_ID = "lc_id";

    private ViewDialog viewDialog;
    private SessionManager sessionManager;
    // TODO: Rename and change types of parameters
    private String type;
    private String cropId, varietyId, lcId;
    private Maincrops_Model land;
    private Crops_Main crop;
    boolean editAccess;

    private ArrayList<Activity_Cat_Model> activityList = new ArrayList<>();

    private Activity_Adapter adapter;

    private BaseActivity.ReceiverInterface receiverInterface = intent -> {
        if (getView() != null)
            loadActivities();
    };

    public ActivityListFragment() {
        // Required empty public constructor
    }

    public static ActivityListFragment newInstance(String name, String type,
                                                   Maincrops_Model land, Crops_Main crop) {
        ActivityListFragment fragment = new ActivityListFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(TYPE, type);
        if (crop != null)
            args.putString(CROP, GsonUtils.toJson(crop));
        args.putString(LAND, GsonUtils.getGson().toJson(land));
        fragment.setArguments(args);
        fragment.set(name, type);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            System.out.println("Args Name: " + getArguments().getString(NAME));
//            System.out.println("Args Type: " + getArguments().getString(TYPE));
            name = getArguments().getString(NAME);
            type = getArguments().getString(TYPE);
            crop = GsonUtils.getGson().fromJson(getArguments().getString(CROP), Crops_Main.class);
            land = GsonUtils.getGson().fromJson(getArguments().getString(LAND), Maincrops_Model.class);
            if (crop != null) cropId = crop.getCrop_id();

//            System.out.println("Type: " + type);
//            System.out.println("Main Crops: " + land.getMainCropsString());
//            System.out.println("Inter Crops: " + land.getInterCropsString());
        }
        sessionManager = new SessionManager(getContext());
        viewDialog = new ViewDialog(getActivity());
       registerReceiver(Constants.Broadcasts.ACTIVITY_UPDATE, receiverInterface);
//        adapter = new Activity_Adapter(this);
        editAccess = getModuleManager().canUpdate(Components.MyFarm.ACTIVITY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_list, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (adapter == null)
            adapter = new Activity_Adapter(getContext(), this, activityList, R.layout.activity_row_new);
        view.findViewById(R.id.add).setOnClickListener(v -> add());
        if (!getModuleManager().canInsert(Components.MyFarm.ACTIVITY)){
            view.findViewById(R.id.add).setVisibility(View.INVISIBLE);}
        ((RecyclerView) view.findViewById(R.id.recyclerView)).setAdapter(adapter);
        ((RecyclerView) view.findViewById(R.id.recyclerView))
                .setLayoutManager(new LinearLayoutManager(getContext()));
        loadActivities();
    }

    private void add() {

        Pair<String, Object> pair = Pair.create("main_crops", land.getMainCropsString());
        Pair<String, Object> pair1 = Pair.create("inter_crops", land.getInterCropsString());
        Pair<String, Object> pair2 = Pair.create("is_land", cropId == null);
        Pair<String, Object> pair3 = Pair.create("crop", crop);
        Pair<String, Object> pair5 = Pair.create("land", land.getId());
        Pair<String, Object> pair6 = Pair.create("cat", getCategoryId());
        Pair<String, Object> pair7 = Pair.create("crop_type", "");

        openActivity(Add_HelperActivity.class,
                pair, pair1, pair2, pair3, pair7, pair5, pair6);
    }

    private String getCategoryId() {
        switch (type) {
            case "soil_preparation":
                return "1";
            case "land_preparation":
                return "2";
            case "water_analysis":
                return "3";
            case "cultivation":
                return "4";
            case "post_harvest":
                return "5";
        }
        return "0";
    }

    private void loadActivities() {

       /* if (!checkSubscription(Components.MyFarm.ACTIVITY, ModuleManager.ACCESS.VIEW)){
            return;
        }*/

        viewDialog.showDialog();

//        System.out.println("User Id: " + sessionManager.getUser().getId());
//        System.out.println("Land Id: " + land.getId());
//        System.out.println("Crop Id: " + cropId);
//        System.out.println("S Type: " + type);

        ANRequest.PostRequestBuilder builder = AndroidNetworking.post(Webservice.activity_list);

        builder.addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId());
        builder.addUrlEncodeFormBodyParameter("land_id", land.getId());
        builder.addUrlEncodeFormBodyParameter("crop_type", "main_crop");
        if (cropId != null)
            builder.addUrlEncodeFormBodyParameter("crop_id", cropId);
        if (crop != null)
            builder.addUrlEncodeFormBodyParameter("lc_id", crop.getLcId());
        //builder.addUrlEncodeFormBodyParameter("lc_id", )
        builder.addUrlEncodeFormBodyParameter("type", type);
        builder.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                if (UserHelper.checkResponse(getContext(), response)){
                    return;
                }
                System.out.println("Activity Fragment:\n" + response);
                viewDialog.hideDialog();
                try {
                    if (response.optBoolean("status")) {
                        activityList.clear();
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            System.out.println("Activity Json Object: " + jsonArray.getJSONObject(i));
                            Activity_Cat_Model activityModel = new Activity_Cat_Model();
                            activityModel.setFence_id(jsonArray.getJSONObject(i).optString("id"));
                            activityModel.setSowing_by(jsonArray.getJSONObject(i).optString("sowing_by"));
                            activityModel.setCrop_id(jsonArray.getJSONObject(i).optString("crop_id"));
                            activityModel.setLand_id(jsonArray.getJSONObject(i).optString("land_id"));
                            activityModel.setLc_id(jsonArray.getJSONObject(i).optString("lc_id"));
                            activityModel.setService_id(jsonArray.getJSONObject(i).optString("service_id").trim());
                            activityModel.setPrepare_type(jsonArray.getJSONObject(i).optString("prepare_type"));
                            activityModel.setCrop_type(jsonArray.getJSONObject(i).optString("crop_type"));
                            activityModel.setFencing_by(jsonArray.getJSONObject(i).optString("fencing_by"));
                            activityModel.setEquipment_name(jsonArray.getJSONObject(i).optString("equipment_name"));
                            activityModel.setEquipment_type(jsonArray.getJSONObject(i).optString("equipment_type"));
                            activityModel.setVendor_name(jsonArray.getJSONObject(i).optString("vendor_name"));
                            activityModel.setContract_type(jsonArray.getJSONObject(i).optString("contract_type"));
                            activityModel.setMaterial_name(jsonArray.getJSONObject(i).optString("material_name"));
                            activityModel.setMember_id(jsonArray.getJSONObject(i).optString("member_id"));
                            activityModel.setStart_date(jsonArray.getJSONObject(i).optString("start_date"));
                            activityModel.setEnd_date(jsonArray.getJSONObject(i).optString("end_date"));
                            activityModel.setDays_count(jsonArray.getJSONObject(i).optString("days_count"));
                            activityModel.setMember_count(jsonArray.getJSONObject(i).optString("member_count"));
                            activityModel.setTotal_amount(jsonArray.getJSONObject(i).optString("total_amount"));
                            activityModel.setCreated_date(jsonArray.getJSONObject(i).optString("created_date"));
                            activityModel.prepareImages(jsonArray.getJSONObject(i).optJSONArray("images"));
                            activityModel.setStatus(jsonArray.getJSONObject(i).optString("status", ""));
                            activityList.add(activityModel);
                        }
                    }
                    updateUI();
//                    System.out.println("Activities: " + GsonUtils.toJson(activityList));
                } catch (Exception e) {
                    Toasty.error(getContext(), "Activities Parse Error!").show();
                }
            }

            @Override
            public void onError(ANError anError) {
                try {
                    System.out.println("Error: \n" + GsonUtils.getGson().toJson(anError));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewDialog.hideDialog();
                //Toasty.error(getContext(), "Activities Load Error!").show();
            }
        });
    }

    public void set(String name, String type) {
        this.name = name;
        this.type = type;
    }

    private void updateUI() {
        updateList();
        updateNoData();
    }

    private void updateList() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private void updateNoData() {
        if (getView() != null)
            getView().findViewById(R.id.noData)
                    .setVisibility(activityList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean canUpdateActivity() {
        return editAccess;
    }

    @Override
    public boolean canEdit() {
        return checkSubscription(Components.MyFarm.ACTIVITY, ModuleManager.ACCESS.UPDATE);
    }
}