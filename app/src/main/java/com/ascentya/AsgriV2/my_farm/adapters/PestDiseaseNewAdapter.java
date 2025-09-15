package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.my_farm.model.ReportDetail;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PestDiseaseNewAdapter extends RecyclerView.Adapter<PestDiseaseNewAdapter.PestDiseaseVH> {

    private Action action;

    public PestDiseaseNewAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public PestDiseaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pest_disease_new_item, parent, false);
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

        private TextView nameTv, dateTv, pestName, pestDisease, remedyName, remedy;
        private ImageView remedyIcon;
        private TextView appliedTv, recoveredTv;

        public PestDiseaseVH(Action action, @NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> action.onClicked(getAdapterPosition()));
            nameTv = itemView.findViewById(R.id.name);
            dateTv = itemView.findViewById(R.id.date);
            pestName = itemView.findViewById(R.id.pestName);
            pestDisease = itemView.findViewById(R.id.pestDisease);
            remedyIcon = itemView.findViewById(R.id.remedyIcon);
            remedyName = itemView.findViewById(R.id.remedyTitle);
            remedy = itemView.findViewById(R.id.remedy);

            appliedTv = itemView.findViewById(R.id.applied);
            recoveredTv = itemView.findViewById(R.id.recovered);
        }

        public void update(ReportDetail reportDetail){

            nameTv.setText(reportDetail.getName());
            System.out.println("infectioncount"+reportDetail.getName());
            dateTv.setText(DateUtils.displayDayAndMonth(reportDetail.getCreatedAt()));
            System.out.println("infectioncount"+reportDetail.getPartInfectionCount());
            pestName.setText(reportDetail.getPartInfectionCount().equals("0") ?
                    "No" : reportDetail.getPartInfectionCount());
            remedy.setText(reportDetail.getRemedyCount().equals("0") ?
                    "No" : reportDetail.getRemedyCount());

            String applied = reportDetail.getApplied();
            String recovered = reportDetail.getRecovered();

            appliedTv.setText(((applied != null && !applied.equals("0")) ? applied : "Not") + " Applied");
            if (applied != null) recoveredTv.setText(((recovered != null && !recovered.equals("0")) ? recovered : "Not") + " Recovered");
            else recoveredTv.setText("");

           /* Constants.ReportType reportType = Constants.ReportTypes.get(reportDetail.getType());

            if (reportType != null){
                typeTv.setVisibility(View.VISIBLE);
                typeTv.setText(reportType.getName());
            }else {
                typeTv.setText("");
                typeTv.setVisibility(View.GONE);
            }

            pestName.setText(reportDetail.getContent());
            pestDisease.setText(reportDetail.getAffectedDisease());

            if (!reportDetail.getRemedyList().isEmpty()){
                remedyIcon.setVisibility(View.VISIBLE);
                remedyName.setVisibility(View.VISIBLE);
                remedy.setVisibility(View.VISIBLE);
                remedy.setText(reportDetail.getRemedyList().get(0).getActivityType());
            }else {
                remedyIcon.setVisibility(View.GONE);
                remedyName.setVisibility(View.GONE);
                remedy.setVisibility(View.GONE);
                remedy.setText("");
            }*/


        }
    }

    public interface Action {
        ArrayList<ReportDetail> getReports();
        void onClicked(int position);
    }
}
