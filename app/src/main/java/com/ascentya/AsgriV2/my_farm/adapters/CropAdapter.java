package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {

    private Action action;

    public CropAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.farm_crop_item, parent, false);
        CropViewHolder cropViewHolder =
                new CropViewHolder(view, action);
        return cropViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        holder.update(action.getCrops().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getCrops().size();
    }

    public class CropViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView cropImage;
        private TextView varietyName, cropName, landName, acreCount;
        private FloatingActionButton activity, finance, pest, disease;

        public CropViewHolder(@NonNull View itemView, Action action) {
            super(itemView);

            cropImage = itemView.findViewById(R.id.crop_icon);

            cropName = itemView.findViewById(R.id.crop_name);
            varietyName = itemView.findViewById(R.id.varietyName);
            landName = itemView.findViewById(R.id.land_name);
            acreCount = itemView.findViewById(R.id.acre);

            activity = itemView.findViewById(R.id.activity);
            finance = itemView.findViewById(R.id.finance);
            pest = itemView.findViewById(R.id.pests);
            disease = itemView.findViewById(R.id.diseases);

            activity.setOnClickListener(view -> action.activityClicked(getAdapterPosition()));
            finance.setOnClickListener(view -> action.expenseClicked(getAdapterPosition()));
            pest.setOnClickListener(view -> action.pestClicked(getAdapterPosition()));
//            disease.setOnClickListener(view -> action.diseaseClicked(getAdapterPosition()));

            itemView.findViewById(R.id.finance).setVisibility(action.canViewFinance() ? View.VISIBLE : View.GONE);
            itemView.findViewById(R.id.pests).setVisibility(action.canViewPest() ? View.VISIBLE : View.GONE);
        }

        public void update(Maincrops_Model model){

            if (action.isMain()){
                Crops_Main cropsMain = Webservice.getCrop(model.getMaincrop());
                cropName.setText(cropsMain.getName());
                Glide.with(itemView.getContext())
                        .load(cropsMain.getIcon()).into(cropImage);
            }else {
                Crops_Main cropsMain = Webservice.getCrop(model.getIntercrop());
                cropName.setText(cropsMain.getName());
                Glide.with(itemView.getContext())
                        .load(cropsMain.getIcon()).into(cropImage);
            }

            VarietyModel varietyModel = Webservice.getVariety(model.getVarietyId());

            if (varietyModel != null){
                varietyName.setVisibility(View.VISIBLE);
                varietyName.setText(varietyModel.getName());
            }else {
                varietyName.setVisibility(View.GONE);
                varietyName.setText("");
            }

            landName.setText(model.getLand_name());
            acreCount.setText(model.getAcre_count() + " " + itemView.getContext().getString(R.string.acres));
        }
    }

    public interface Action{
        ArrayList<Maincrops_Model> getCrops();
        boolean canViewFinance();
        boolean canViewPest();
        boolean isMain();
        void activityClicked(int position);
        void expenseClicked(int position);
        void pestClicked(int position);
        void diseaseClicked(int position);
    }
}
