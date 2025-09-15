package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.ZoneReport;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Constants;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PestDiseaseAdapter extends RecyclerView.Adapter<PestDiseaseAdapter.PestDiseaseVH> {

    private Action action;

    public PestDiseaseAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public PestDiseaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pest_disease_item, parent, false);
        return new PestDiseaseVH(action, view);
    }

    @Override
    public void onBindViewHolder(@NonNull PestDiseaseVH holder, int position) {
        holder.update(action.getReports().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getReports().size();
    }

    class PestDiseaseVH extends RecyclerView.ViewHolder{

        private TextView typeTv, pestName, pestDisease, remedyName, remedy;
        private ImageView remedyIcon;

        public PestDiseaseVH(Action action, @NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> action.onClicked(getAdapterPosition()));
            typeTv = itemView.findViewById(R.id.type);
            pestName = itemView.findViewById(R.id.pestName);
            pestDisease = itemView.findViewById(R.id.pestDisease);
            remedyIcon = itemView.findViewById(R.id.remedyIcon);
            remedyName = itemView.findViewById(R.id.remedyTitle);
            remedy = itemView.findViewById(R.id.remedy);
        }

        public void update(ZoneReport zoneReport){

            Constants.ReportType reportType = Constants.ReportTypes.get(zoneReport.getType());

            if (reportType != null){
                typeTv.setVisibility(View.VISIBLE);
                typeTv.setText(reportType.getName());
            }else {
                typeTv.setText("");
                typeTv.setVisibility(View.GONE);
            }

            pestName.setText(zoneReport.getContent());
            pestDisease.setText(zoneReport.getAffectedDisease());

            if (!zoneReport.getRemedyList().isEmpty()){
                remedyIcon.setVisibility(View.VISIBLE);
                remedyName.setVisibility(View.VISIBLE);
                remedy.setVisibility(View.VISIBLE);
                remedy.setText(zoneReport.getRemedyList().get(0).getActivityType());
            }else {
                remedyIcon.setVisibility(View.GONE);
                remedyName.setVisibility(View.GONE);
                remedy.setVisibility(View.GONE);
                remedy.setText("");
            }


        }
    }

    public interface Action {
        ArrayList<ZoneReport> getReports();
        void onClicked(int position);
    }
}
