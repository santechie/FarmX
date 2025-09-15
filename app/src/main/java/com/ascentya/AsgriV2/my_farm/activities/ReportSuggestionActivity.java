package com.ascentya.AsgriV2.my_farm.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.adapters.ReportSuggestionAdapter;
import com.ascentya.AsgriV2.my_farm.model.ReportSuggestion;

import java.util.ArrayList;
import java.util.List;

public class ReportSuggestionActivity
        extends BaseActivity
        implements ReportSuggestionAdapter.Action {

    private RecyclerView recyclerView;

    private ReportSuggestionAdapter adapter;
    private ArrayList<ReportSuggestion> suggestionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_suggestion);

        setToolbarTitle("Suggestions", true);

        recyclerView = findViewById(R.id.recyclerView);

        adapter = new ReportSuggestionAdapter(this);

        suggestionList.addAll(getFromIntent("suggestions", ApiHelper.reportSuggestionListType));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public ArrayList<ReportSuggestion> getReportSuggestionItems() {
        return suggestionList;
    }

    @Override
    public boolean canUpdateRemedy() {
        return false;
    }

    @Override
    public void onClicked(int position) {
        DIalogwith_Image.showImageViewer(this, "Images", getImageList(suggestionList.get(position)));
    }

    private List<String> getImageList(ReportSuggestion suggestion){
        ArrayList<String> images = new ArrayList<>();
        if (suggestion != null) {
            for (ImageModel image : suggestion.getImageModels()) {
                images.add(image.getImage());
            }
        }
        return images;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}