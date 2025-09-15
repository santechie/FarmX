package com.ascentya.AsgriV2.my_farm.activities;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.my_farm.adapters.ReportItemAdapter;
import com.ascentya.AsgriV2.my_farm.model.ReportDetail;
import com.ascentya.AsgriV2.my_farm.model.ReportItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class ReportDetailActivity
        extends BaseActivity
        implements ReportItemAdapter.Action{

    private Maincrops_Model land;
    private Crops_Main crop;
    private Zone_Model zone;
    private ReportDetail reportDetail;

    private TextView landTv, dateTv /*, cropTv, zoneTv*/;
    private Button suggestionBtn;
    private RecyclerView recyclerView;

    private ReportItemAdapter adapter;

    private ArrayList<ReportItem> items = new ArrayList<>();

    private ReceiverInterface pestUpdates = new ReceiverInterface() {
        @Override
        public void onReceive(Intent intent) {
            loadDetail();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        setToolbarTitle(getString(R.string.report), true);

        land = getFromIntent("land", Maincrops_Model.class);
        crop = getFromIntent("crop", Crops_Main.class);
        zone = getFromIntent("zone", Zone_Model.class);
        reportDetail = getFromIntent("report_detail", ReportDetail.class);

        landTv = findViewById(R.id.land);
        dateTv = findViewById(R.id.date);
        suggestionBtn = findViewById(R.id.suggestions);
        //cropTv = findViewById(R.id.crop);
        //zoneTv = findViewById(R.id.zone);
        recyclerView = findViewById(R.id.recyclerView);

        landTv.setText(land.getLand_name() + " / " + crop.getName() + " / " + zone.getZone_name());
        dateTv.setText(DateUtils.displayDayAndMonth(reportDetail.getCreatedAt()));
//        cropTv.setText(crop.getName());
//        zoneTv.setText(zone.getZone_name());

        adapter = new ReportItemAdapter(this);
        recyclerView.setAdapter(adapter);

        registerReceiver(Constants.Broadcasts.PEST_UPDATE, pestUpdates);

        suggestionBtn.setOnClickListener(v -> showSuggestion());

        loadDetail();
    }

    private void showSuggestion(){
        showLoading();

        //https://vrjaitraders.com/ard_farmx/api/infection/find_suggestion
        AndroidNetworking.post(Webservice.findReportSuggestion)
                .addUrlEncodeFormBodyParameter("zone_infection_id", reportDetail.getZiId())
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
               /* if (UserHelper.checkResponse(ReportDetailActivity.this, response)){
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

    private void loadDetail(){
        showLoading();
        AndroidNetworking.post(Webservice.getPartInfections)
                .addUrlEncodeFormBodyParameter("zone_infection_id", reportDetail.getZiId())
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
               /* if (UserHelper.checkResponse(ReportDetailActivity.this, response)){
                    return;
                }*/
                if (response.optBoolean("status", false)){
                    items.clear();
                    items.addAll(GsonUtils.fromJson(response.optJSONArray("data").toString(),
                            ApiHelper.infectionDetailItemListType));
                    update();
                }else {
                    toast("no Detail!");
                }
            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
                anError.printStackTrace();
                toast("Detail load Error!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void update(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<ReportItem> getReportItems() {
        return items;
    }

    @Override
    public void onClicked(int position) {
        openActivity(PartReportActivity.class,
                Pair.create("land", land),
                Pair.create("crop", crop),
                Pair.create("zone", zone),
                Pair.create("report_detail", reportDetail),
                Pair.create("report_item", items.get(position)));
    }
}