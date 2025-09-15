package com.ascentya.AsgriV2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.LandCropModel;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LandCropAdapter extends RecyclerView.Adapter<LandCropAdapter.LandCropVH> {

    private Action action;

    public LandCropAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public LandCropVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_land_crop_item, parent, false);
        return new LandCropVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandCropVH holder, int position) {
        holder.update(action.getLandCrops().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getLandCrops().size();
    }

    public class LandCropVH extends RecyclerView.ViewHolder{

        public LandCropVH(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> action.onClicked(getAdapterPosition()));
        }

        public void update(LandCropModel model){
            ((TextView) itemView.findViewById(R.id.crop_name))
                    .setText(Webservice.getCrop(model.getCropId()).getName());
            ((TextView) itemView.findViewById(R.id.harvest_date)).setText(model.getHarvestDuring());
            ((TextView) itemView.findViewById(R.id.yield))
                    .setText(model.getYieldDuring() + " " + (model.getYieldUnit() == null ? "" : model.getYieldUnit()));
            Glide.with(itemView.getContext())
                    .load(Webservice.getCrop(model.getCropId()).getIcon())
                    .into((ImageView) itemView.findViewById(R.id.crop_image));
            VarietyModel varietyModel = Webservice.getVariety(model.getVarietyId());
            if (varietyModel != null){
                ((TextView) itemView.findViewById(R.id.varietyName))
                        .setText(varietyModel.getName());
            }else {
                ((TextView) itemView.findViewById(R.id.varietyName))
                        .setText("");
            }
        }
    }

    public interface Action{
        List<LandCropModel> getLandCrops();
        void onClicked(int position);
    }
}
