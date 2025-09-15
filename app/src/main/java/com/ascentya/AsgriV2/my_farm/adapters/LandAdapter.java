package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LandAdapter extends RecyclerView.Adapter<LandAdapter.LandViewHolder> {

    private final Action action;
    private List<Maincrops_Model> landList;

    public LandAdapter(Action action){
        this.action = action;
    }

    public LandAdapter(List<Maincrops_Model> landList, Action action) {
        this.landList = landList;
        this.action = action;
    }

    @NonNull
    @Override
    public LandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_land_item, parent, false);
        return new LandViewHolder(action, view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandViewHolder holder, int position) {
        holder.update(action.getLands().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getLands().size();
    }

    // ✅ Method to update the dataset and refresh RecyclerView
    public void updateList(List<Maincrops_Model> newList) {
        this.landList = newList;
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    class LandViewHolder extends RecyclerView.ViewHolder{

        private TextView landNameTv;

        public LandViewHolder(Action action, @NonNull View itemView) {
            super(itemView);

            //getBindingAdapterPosition
            //getAdapterPosition
            landNameTv = itemView.findViewById(R.id.landName);
            itemView.findViewById(R.id.activity).setOnClickListener(v -> action.activity(getBindingAdapterPosition()));
            itemView.findViewById(R.id.finance_).setOnClickListener(v -> action.expense(getBindingAdapterPosition()));
            itemView.findViewById(R.id.pests).setOnClickListener(v -> action.pest(getBindingAdapterPosition()));
//            itemView.findViewById(R.id.diseases).setOnClickListener(v -> action.disease(getAdapterPosition()));
            itemView.findViewById(R.id.income).setOnClickListener(v -> action.income(getBindingAdapterPosition()));
            itemView.findViewById(R.id.addZones).setOnClickListener(v -> action.addZones(getBindingAdapterPosition()));
            itemView.findViewById(R.id.zones).setOnClickListener(v -> action.zones(getBindingAdapterPosition()));
            itemView.findViewById(R.id.deviceData).setOnClickListener(v -> action.deviceData(getBindingAdapterPosition()));

            itemView.findViewById(R.id.addZones).setVisibility(action.canAddZone() ? View.VISIBLE : View.GONE);
            itemView.findViewById(R.id.zones).setVisibility(action.canViewZone() ? View.VISIBLE : View.GONE);
            itemView.findViewById(R.id.deviceData).setVisibility(action.canViewRealTimeData() ? View.VISIBLE : View.GONE);
            itemView.findViewById(R.id.finance_).setVisibility(action.canViewFinance() ? View.VISIBLE : View.GONE);
            itemView.findViewById(R.id.income).setVisibility(action.canAddIncome() ? View.VISIBLE : View.GONE);
            itemView.findViewById(R.id.pests).setVisibility(action.canViewPest() ? View.VISIBLE : View.GONE);
        }

        public void update(Maincrops_Model land){
            landNameTv.setText(land.getLand_name());
        }
    }


    public interface Action{
        ArrayList<Maincrops_Model> getLands();
        boolean canAddZone();
        boolean canViewZone();
        boolean canViewRealTimeData();
        boolean canViewFinance();
        boolean canAddIncome();
        boolean canViewPest();
        void activity(int position);
        void expense(int position);
        void pest(int position);
        void disease(int position);
        void income(int position);
        void addZones(int position);
        void zones(int position);
        void deviceData(int position);
    }
}
