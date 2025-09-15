package com.ascentya.AsgriV2.my_farm.activities;

import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.ascentya.AsgriV2.Adapters.ActivityImageAdapter;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.my_farm.adapters.RemedyItemAdapter;
import com.ascentya.AsgriV2.my_farm.dialogs.AddRemedyDialog;
import com.ascentya.AsgriV2.my_farm.model.FileImageItem;
import com.ascentya.AsgriV2.my_farm.model.RemedyItem;
import com.ascentya.AsgriV2.my_farm.model.ReportDetail;
import com.ascentya.AsgriV2.my_farm.model.ReportItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PartReportActivity
        extends BaseActivity
        implements
        RemedyItemAdapter.Action {

    private Maincrops_Model land;
    private Crops_Main crop;
    private Zone_Model zone;
    private ReportDetail reportDetail;
    private ReportItem reportItem;

    private TextView landTv, partTv, titleTv, symptomTv, dateTv;
    private Button suggestionBtn;
    private RecyclerView imagesRv, remedyRv;
    private FloatingActionButton addBtn;

    private ActivityImageAdapter imageAdapter;
    private ArrayList<ImageModel> imageModels = new ArrayList<>();
    private ArrayList<FileImageItem> fileModels = new ArrayList<>();

    private ArrayList<RemedyItem> remedyList = new ArrayList<>();
    private RemedyItemAdapter remedyItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_report);


//        if (getModuleManager().canInsert(ModuleManager.Components.MyFarm.REMEDY))
//            addBtn.setVisibility(View.VISIBLE);

        setToolbarTitle(getString(R.string.infection), true);

        land = getFromIntent("land", Maincrops_Model.class);
        crop = getFromIntent("crop", Crops_Main.class);
        zone = getFromIntent("zone", Zone_Model.class);
        reportDetail = getFromIntent("report_detail", ReportDetail.class);
        reportItem = getFromIntent("report_item", ReportItem.class);

        landTv = findViewById(R.id.land);
        partTv = findViewById(R.id.part);
        dateTv = findViewById(R.id.date);
        titleTv = findViewById(R.id.name);
        symptomTv = findViewById(R.id.description);
        imagesRv = findViewById(R.id.imagesRv);
        remedyRv = findViewById(R.id.remedyRv);
        suggestionBtn = findViewById(R.id.suggestions);
        addBtn = findViewById(R.id.add);

        imageModels.addAll(reportItem.getImageModels());

        landTv.setText(new StringBuilder()
                .append(land.getLand_name())
                .append(" / ")
                .append(crop.getName())
                .append(" / ")
                .append(zone.getZone_name()).toString());

        partTv.setText(new StringBuilder()
                .append(Constants.ReportTypes.get(reportItem.getType()))
                .append(" - ")
                .append(Constants.CropParts.get(reportItem.getPart()).getName()).toString());

        titleTv.setText(reportItem.getName());
        dateTv.setText(DateUtils.displayDayAndMonth(reportDetail.getCreatedAt()));
        symptomTv.setText(reportItem.getSymptom());

        imageAdapter = new ActivityImageAdapter(imageAction);
        imagesRv.setLayoutManager(new LinearLayoutManager(imagesRv.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        imagesRv.setAdapter(imageAdapter);

        remedyItemAdapter = new RemedyItemAdapter(this);
        remedyRv.setAdapter(remedyItemAdapter);

        suggestionBtn.setOnClickListener(v -> showSuggestion());

        if (!getModuleManager().canInsert(Components.MyFarm.REMEDY)){
            addBtn.setVisibility(View.INVISIBLE);
        }
        addBtn.setOnClickListener(v -> showRemedyDialog(null));

        loadRemedies();
    }

    private void showSuggestion(){
        showLoading();

        AndroidNetworking.post(Webservice.findReportSuggestion)
                .addUrlEncodeFormBodyParameter("part_infection_id", reportItem.getPiId())
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();

                       /* if (UserHelper.checkResponse(PartReportActivity.this, response)){
                            return;
                        }*/
                        System.out.println("Suggestion Response: " + response);
                        if (response.optBoolean("status", false)){
                            openActivity(ReportSuggestionActivity.class, Pair.create("suggestions",
                                    response.optJSONArray("data").toString()));
                        }else {
                            toast("No Suggestion!");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        anError.printStackTrace();
                        toast("Suggestion Error!");
                    }
                });
    }

    private void showRemedyDialog(@Nullable RemedyItem item){
        new AddRemedyDialog(this, new AddRemedyDialog.Action() {
            @Override
            public void onAdd(boolean isUpdate, RemedyItem item) {
                if (remedyList.contains(item)){
                    updateRemedy(item);
                }else {
                    addRemedy(item);
                }
            }

            @Override
            public boolean checkDuplicate(RemedyItem item) {
                return PartReportActivity.this.checkDuplicate(item);
            }
        }, item).show(item != null);
    }

    private boolean checkDuplicate(RemedyItem item){
        if (!remedyList.contains(item)){

        }
        return false;
    }

    private void addRemedy(RemedyItem item){

        showLoading();

        ANRequest.MultiPartBuilder builder = AndroidNetworking.upload(Webservice.submitInfectionRemedy);

        builder.addMultipartParameter("user_id",getSessionManager().getUser().getId());
        builder.addMultipartParameter("part_infection_id", reportItem.getPiId());
        builder.addMultipartParameter("remedy", item.getRemedy());
        builder.addMultipartParameter("quantity", item.getQuantity());
        builder.addMultipartParameter("quantity_unit", item.getQuantityUnit());
        builder.addMultipartParameter("cost", item.getCost());
        builder.addMultipartParameter("applied", item.getIsApplied());
        builder.addMultipartParameter("recovered", item.getIsRecovered());

        for (ImageModel i: item.getImageModels()) {
            builder.addMultipartFile("images[]", new File(i.getImage()));
        }

        builder.build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                System.out.println(response);
            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
                toast("Submit Error!");
                anError.printStackTrace();
                System.out.println(anError.getErrorBody());
                System.out.println(anError.getResponse().body());
                //System.out.println(GsonUtils.toJson(anError));
            }
        });

        /*builder.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Submit Remedy: " + response);
                hideLoading();
                if (response.optBoolean("status", false)){
                    sendBroadcast(Constants.Broadcasts.PEST_UPDATE);
                    loadRemedies();
                }else {
                    toast("Add Remedy Failed");
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                hideLoading();
                toast("Add Remedy Error!");
            }
        });*/
    }

    private void updateRemedy(RemedyItem item){

        showLoading();

        ANRequest.MultiPartBuilder builder = AndroidNetworking.upload(Webservice.updateInfectionRemedy);

        builder.addMultipartParameter("user_id",getSessionManager().getUser().getId());
        builder.addMultipartParameter("infection_remedy_id", item.getIrId());
        builder.addMultipartParameter("remedy", item.getRemedy());
        builder.addMultipartParameter("quantity", item.getQuantity());
        builder.addMultipartParameter("quantity_unit", item.getQuantityUnit());
        builder.addMultipartParameter("cost", item.getCost());
        builder.addMultipartParameter("applied", item.getIsApplied());
        builder.addMultipartParameter("recovered", item.getIsRecovered());

        for (ImageModel i: item.getImageModels()) {
            if (i.getNew())
                builder.addMultipartFile("images[]", new File(i.getImage()));
        }

        /*builder.build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                System.out.println(response);
            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
                toast("Update Error!");
                anError.printStackTrace();
            }
        });*/

        builder.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                if (response.optBoolean("status", false)){
                    sendBroadcast(Constants.Broadcasts.PEST_UPDATE);
                    loadRemedies();
                }else {
                    toast("update Remedy Failed");
                }
            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
                anError.printStackTrace();
                toast("update Remedy Error!");
            }
        });
    }

    private void loadRemedies(){

        showLoading();
        AndroidNetworking.post(Webservice.getInfectionRemedies)
                .addUrlEncodeFormBodyParameter("part_infection_id", reportItem.getPiId())
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        if (response.optBoolean("status", false)){
                            System.out.println(" Remedy Response" + response);
                            remedyList.clear();
                            remedyList.addAll(
                                    GsonUtils.fromJson(
                                            response.optJSONArray("data").toString(),
                                            ApiHelper.infectionRemedyItemListType));
                            if (getModuleManager().canView(Components.MyFarm.REMEDY))
                                updateRemedy();
                        }else {
                            toast("No Remedies!");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        hideLoading();
                        toast("Remedy Load Error!");
                    }
                });

    }

    private void updateRemedy(){
        remedyItemAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<RemedyItem> getRemedyList() {
        return remedyList;
    }

    @Override
    public void onClicked(int position) {
        if (getModuleManager().canUpdate(Components.MyFarm.REMEDY))
            showRemedyDialog(remedyList.get(position));
    }

    private List<String> getImageList(){
        ArrayList<String> images = new ArrayList<>();
        if (imageModels != null) {
            for (ImageModel image : imageModels) {
                images.add(image.getImage());
            }
        }
        return images;
    }

    private ActivityImageAdapter.Action imageAction = new ActivityImageAdapter.Action() {
        @Override
        public ArrayList<ImageModel> getImages() {
            return imageModels;
        }

        @Override
        public void update() {

        }

        @Override
        public void onClicked(int position) {
            DIalogwith_Image.showImageViewer(PartReportActivity.this, "Images", getImageList());
        }

        @Override
        public void onDelete(int position) {

        }

        @Override
        public boolean showDelete(int position) {
            return false;
        }
    };
}