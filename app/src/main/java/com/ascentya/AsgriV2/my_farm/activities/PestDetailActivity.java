package com.ascentya.AsgriV2.my_farm.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.ActivityImageAdapter;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.ZoneRemedy;
import com.ascentya.AsgriV2.Models.ZoneReport;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.Utils.Remedy_Dialog;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.my_farm.adapters.RemedyAdapter;
import com.ascentya.AsgriV2.my_farm.model.FileImageItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class PestDetailActivity extends BaseActivity
    implements ActivityImageAdapter.Action, RemedyAdapter.Action {

    private Maincrops_Model land;
    private Crops_Main crop;
    private Zone_Model zone;
    private ZoneReport zoneReport;

    private TextView typeTv, headTv, nameTv, diseaseTv, causeTv, remedyHeadTv, remedyName, remedyFertilizer, costTv;
    private ImageView pestIv;
    //private CardView remedyContainer;
    private MaterialButton remedyBtn;

    private RecyclerView imageRv;
    private ActivityImageAdapter imageAdapter;
    private ArrayList<ImageModel> imageModels = new ArrayList<>();
    private ArrayList<FileImageItem> fileModels = new ArrayList<>();

    private RecyclerView remedyRv;
    private RemedyAdapter remedyAdapter;
    private ArrayList<ZoneRemedy> remedyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_detail);

        setToolbarTitle(getString(R.string.pest_disease), true);

        land = getFromIntent("land", Maincrops_Model.class);
        crop = getFromIntent("crop", Crops_Main.class);
        zone = getFromIntent("zone", Zone_Model.class);
        zoneReport = getFromIntent("zone_report", ZoneReport.class);

        typeTv = findViewById(R.id.type);
        headTv = findViewById(R.id.heading);
        nameTv = findViewById(R.id.pestName);
        diseaseTv = findViewById(R.id.affectedDisease);
        causeTv = findViewById(R.id.affectedCause);
        pestIv = findViewById(R.id.pestImage);
        remedyName = findViewById(R.id.remedyName);
        remedyFertilizer = findViewById(R.id.remedyFertilizer);
        costTv = findViewById(R.id.cost);

        imageRv = findViewById(R.id.imageRv);
        imageRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ActivityImageAdapter(this);
        imageRv.setAdapter(imageAdapter);

        remedyHeadTv = findViewById(R.id.remedyTitle);
        //remedyContainer = findViewById(R.id.remedyContainer);

        remedyRv = findViewById(R.id.recyclerView);
        remedyRv.setLayoutManager(new LinearLayoutManager(this));
        remedyAdapter = new RemedyAdapter(this);
        remedyRv.setAdapter(remedyAdapter);

        remedyBtn = findViewById(R.id.remedyBtn);

        remedyBtn.setOnClickListener(v -> remedy());

        if (zoneReport == null){
            finish();
            return;
        }

        //System.out.println("Zone Report: " + GsonUtils.toJson(zoneReport));

        headTv.setText(land.getLand_name() + " / " + crop.getName() + " / " + zone.getZone_name());

        updateRemedy();
        updateImages();
    }

    private void remedy(){
        if (checkSubscription(Components.MyFarm.REMEDY , ModuleManager.ACCESS.INSERT)) {
            Remedy_Dialog obj = new Remedy_Dialog();
            obj.dialog(this, getString(R.string.remedy), land.getId(),
                    zone.getZone_id(),
                    getSessionManager().getUser().getId(),
                    zoneReport);
            obj.setAction(new Remedy_Dialog.Action() {
                @Override
                public void onComplete() {
                    finish();
                }
            });
        }
    }

    private void updateRemedy(){

        Constants.ReportType reportType = Constants.ReportTypes.get(zoneReport.getType());
        if (reportType != null){
            typeTv.setVisibility(View.VISIBLE);
            typeTv.setText(reportType.getName());
        }else{
            typeTv.setVisibility(View.GONE);
            typeTv.setText("");
        }

        nameTv.setText(zoneReport.getContent());
        diseaseTv.setText(zoneReport.getAffectedDisease());
        causeTv.setText(zoneReport.getAffectedCause());
        costTv.setText(zoneReport.getCost());

        if (zoneReport.getRemedyList().isEmpty()){
            remedyHeadTv.setVisibility(View.GONE);
            //remedyContainer.setVisibility(View.GONE);
            //remedyBtn.setVisibility(View.VISIBLE);
        }else {
            remedyHeadTv.setVisibility(View.VISIBLE);
            //remedyContainer.setVisibility(View.VISIBLE);
            //remedyBtn.setVisibility(View.GONE);

            remedyList.clear();
            remedyList.addAll(zoneReport.getRemedyList());

            remedyAdapter.notifyDataSetChanged();

            /*ZoneRemedy zoneRemedy = zoneReport.getRemedyList().get(0);

            remedyName.setText(zoneRemedy.getActivityType());
            remedyFertilizer.setText(zoneRemedy.getFertilizerUsed());*/
        }
    }

    private void updateImages(){
        if (zoneReport.getImages() != null
                && !zoneReport.getImages().isEmpty()){
            imageRv.setVisibility(View.VISIBLE);
            imageModels.clear();
            imageModels.addAll(zoneReport.getImages());
            update();
        }
    }

    @Override
    public ArrayList<ImageModel> getImages() {
        return imageModels;
    }

    @Override
    public void update() {
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {
        DIalogwith_Image.showImageViewer(this, getString(R.string.image), getImageList());
    }

    @Override
    public void onDelete(int position) {

    }

    @Override
    public boolean showDelete(int position) {
        return false;
    }

    private List<String> getImageList(){
        ArrayList<String> images = new ArrayList<>();
        if (zoneReport != null && zoneReport.getImages() != null) {
            for (ImageModel image : zoneReport.getImages()) {
                images.add(image.getImage());
            }
        }
        return images;
    }

    @Override
    public ArrayList<ZoneRemedy> getRemedyList() {
        return remedyList;
    }
}