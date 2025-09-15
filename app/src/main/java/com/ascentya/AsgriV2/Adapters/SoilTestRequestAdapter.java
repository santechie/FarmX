package com.ascentya.AsgriV2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.utility.model.SoilTest;
import com.ascentya.AsgriV2.utility.model.WaterTest;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SoilTestRequestAdapter extends RecyclerView.Adapter<SoilTestRequestAdapter.SoilTestReportViewHolder> {

    private Constants.TestType testType;
    private Action action;

    public SoilTestRequestAdapter(Constants.TestType testType, Action action){
        this.testType = testType;
        this.action = action;
    }

    @NonNull
    @Override
    public SoilTestReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_soil_test_request_item, parent, false);
        return new SoilTestReportViewHolder(view, action);
    }

    @Override
    public void onBindViewHolder(@NonNull SoilTestReportViewHolder holder, int position) {
        if (testType.type.equals(Constants.TestTypes.SoilTest.type)) {
            holder.update(action.getSoilRequestList().get(position));
        }else {
            holder.update(action.getWaterRequestList().get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (testType.type.equals(Constants.TestTypes.SoilTest.type)) {
            return action.getSoilRequestList().size();
        }else {
            return action.getWaterRequestList().size();
        }
    }

    class SoilTestReportViewHolder extends RecyclerView.ViewHolder{

        private TextView ticketNumberTv, statusTv,
                dateTv, nameTv,
                mobileTv, landTv,
                labTv, resultTv;

        public SoilTestReportViewHolder(@NonNull View itemView, Action action) {
            super(itemView);

            ticketNumberTv = itemView.findViewById(R.id.key);
            statusTv = itemView.findViewById(R.id.status);
            dateTv = itemView.findViewById(R.id.date);
            nameTv = itemView.findViewById(R.id.name);
            mobileTv = itemView.findViewById(R.id.mobileNumber);
            landTv = itemView.findViewById(R.id.land);
            labTv = itemView.findViewById(R.id.labName);
            resultTv = itemView.findViewById(R.id.result);
            resultTv.setOnClickListener(v -> action.onResultClicked(getAdapterPosition()));
        }

        public void update(SoilTest soilTestRequest){

            dateTv.setText(DateUtils.splitDate(soilTestRequest.getCreatedAt()));
            ticketNumberTv.setText(soilTestRequest.getTicketNumber());
            statusTv.setText(soilTestRequest.getStatus());
            nameTv.setText(soilTestRequest.getName());
            mobileTv.setText(soilTestRequest.getMobileNumber());
            landTv.setText(soilTestRequest.getLandName() + " / " + soilTestRequest.getSoiltype());
            labTv.setText(soilTestRequest.getLabName());

            switch (soilTestRequest.getStatus()){
                case Constants.SoilTestRequestStatus.OPEN:
                case Constants.SoilTestRequestStatus.PICKED:
                    resultTv.setVisibility(View.GONE);
                    break;
                case Constants.SoilTestRequestStatus.CLOSED:
                    resultTv.setVisibility(View.VISIBLE);
                    break;
            }
        }

        public void update(WaterTest waterTest){

            dateTv.setText(DateUtils.splitDate(waterTest.getCreatedAt()));
            ticketNumberTv.setText(waterTest.getTicketNumber());
            statusTv.setText(waterTest.getStatus());
            nameTv.setText(waterTest.getName());
            mobileTv.setText(waterTest.getMobileNumber());
            landTv.setText(waterTest.getLandName() + " / " + waterTest.getSoiltype());
            labTv.setText(waterTest.getLabName());

            switch (waterTest.getStatus()){
                case Constants.SoilTestRequestStatus.OPEN:
                case Constants.SoilTestRequestStatus.PICKED:
                    resultTv.setVisibility(View.GONE);
                    break;
                case Constants.SoilTestRequestStatus.CLOSED:
                    resultTv.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public interface Action{
        List<SoilTest> getSoilRequestList();
        List<WaterTest> getWaterRequestList();
        void onResultClicked(int position);
    }
}
