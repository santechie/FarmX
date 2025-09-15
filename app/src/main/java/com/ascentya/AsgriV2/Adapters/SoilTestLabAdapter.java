package com.ascentya.AsgriV2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.MyTextUtils;
import com.ascentya.AsgriV2.utility.model.SoilTestLab;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SoilTestLabAdapter extends RecyclerView.Adapter<SoilTestLabAdapter.SoilTestLabViewHolder>{

    private Action action;

    public SoilTestLabAdapter(Action action) {
        this.action = action;
    }

    @NonNull
    @Override
    public SoilTestLabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_soil_test_lab_item, parent, false);
        return new SoilTestLabViewHolder(view, action);
    }

    @Override
    public void onBindViewHolder(@NonNull SoilTestLabViewHolder holder, int position) {
        holder.update(getItems().get(position), action);
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    private List<SoilTestLab> getItems() { return action.getSoilTestLabs(); }

    class SoilTestLabViewHolder extends RecyclerView.ViewHolder{

        private TextView labNameTv, addressTv, distanceTv;

        public SoilTestLabViewHolder(@NonNull View itemView, Action action) {
            super(itemView);
            itemView.setOnClickListener(view -> action.setSelectedItem(getAdapterPosition()));

            labNameTv = itemView.findViewById(R.id.labName);
            addressTv = itemView.findViewById(R.id.address);
            distanceTv = itemView.findViewById(R.id.distance);
        }

        public void update(SoilTestLab soilTestLab, Action action){
            ((RadioButton) itemView.findViewById(R.id.check))
                    .setChecked(action.getSelectedItem() == getAdapterPosition());

            labNameTv.setText(soilTestLab.getName());
            addressTv.setText(MyTextUtils.getFormattedAddress(soilTestLab));
            distanceTv.setText(String.format("%.1f KM", Double.valueOf(soilTestLab.getDistance())));
        }
    }

    public interface Action{
        List<SoilTestLab> getSoilTestLabs();
        int getSelectedItem();
        void setSelectedItem(int position);
    }
}
