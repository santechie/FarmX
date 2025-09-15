package com.ascentya.AsgriV2.my_farm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.farmx.postharvest_diseas.PestsDisease_Activity;
import com.ascentya.AsgriV2.my_farm.activities.CropActivity;
import com.ascentya.AsgriV2.my_farm.activities.FinancesActivity;
import com.ascentya.AsgriV2.my_farm.adapters.CropAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CropListFragment extends BaseFragment implements CropAdapter.Action {

    private boolean isMain;
    private CropAdapter adapter;
    private ArrayList<Maincrops_Model> cropList = new ArrayList<>();

    public static CropListFragment getInstance(String name){
        CropListFragment cropListFragment = new CropListFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        cropListFragment.setArguments(args);
        cropListFragment.setName(name);
        return cropListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CropAdapter(CropListFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crop_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((RecyclerView) view.findViewById(R.id.recyclerView))
                .setAdapter(adapter);
        ((RecyclerView) view.findViewById(R.id.recyclerView))
                .setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void updateCrops(ArrayList<Maincrops_Model> crops, boolean isMain){
        this.isMain = isMain;
        cropList.clear();
        cropList.addAll(crops);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<Maincrops_Model> getCrops() {
        return cropList;
    }

    @Override
    public boolean canViewFinance() {
        return getModuleManager().canView(Components.MyFarm.INCOME )
        || getModuleManager().canView(Components.MyFarm.EXPENSE) ;
    }

    @Override
    public boolean canViewPest() {
        return getModuleManager().canView(Components.MyFarm.PEST_DISEASE_DETECTION);
    }


    @Override
    public boolean isMain() {
        return isMain;
    }

    @Override
    public void activityClicked(int position) {
/*        ((BaseActivity) getActivity())
//                .openWithSubscription(CropActivity.class,
//                        Components.findComponent(cropList.get(position).getId(), Components.MyFarm.ZONE),
//                        ModuleManager.ACCESS.VIEW,
                */

                openActivity(CropActivity.class,
                        Pair.create("land_id", cropList.get(position).getId()),
                        Pair.create("crop_id",
                            isMain ? cropList.get(position).getMaincrop()
                            : cropList.get(position).getIntercrop()),
                        Pair.create("variety_id", cropList.get(position).getVarietyId()),
                        Pair.create("land",
                                GsonUtils.toJson(cropList.get(position))));
    }

    @Override
    public void expenseClicked(int position) {
      /*  boolean hasAccess = checkSubscription(Components.MyFarm.EXPENSE, ModuleManager.ACCESS.VIEW)
                || checkSubscription(Components.MyFarm.INCOME, ModuleManager.ACCESS.VIEW);
        if (hasAccess)*/
            openActivity(FinancesActivity.class,
                    Pair.create("land_id", cropList.get(position).getId()),
                    Pair.create("crop_id", isMain ? cropList.get(position).getMaincrop()
                            : cropList.get(position).getIntercrop()),
                    Pair.create("variety_id", cropList.get(position).getVarietyId()));
    }

    @Override
    public void pestClicked(int position) {
        //openActivity(PestDiseaseActivity.class);
        openPestDisease(isMain, getCrops().get(position));
    }

    @Override
    public void diseaseClicked(int position) {
        //openActivity(PestDiseaseActivity.class);
        openPestDisease(isMain, getCrops().get(position));
    }

    private void openPestDisease(boolean main_inter, Maincrops_Model Position_Object){
        Intent i = new Intent(requireContext(), PestsDisease_Activity.class);
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
}
