package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.my_farm.model.CropHistory;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CropHistoryAdapter extends RecyclerView.Adapter<CropHistoryAdapter.CropHistoryViewHolder> {

   private Action action;

   public CropHistoryAdapter(Action action){
      this.action = action;
   }

   @NonNull
   @Override
   public CropHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_crop_history_item, parent, false);
      CropHistoryViewHolder viewHolder = new CropHistoryViewHolder(view, action);
      return viewHolder;
   }

   @Override
   public void onBindViewHolder(@NonNull CropHistoryViewHolder holder, int position) {
      holder.update(action.getCropHistoryList().get(position));
   }

   @Override
   public int getItemCount() {
      return action.getCropHistoryList().size();
   }

   class CropHistoryViewHolder extends RecyclerView.ViewHolder{

      private ImageView cropImageIv;
      private TextView cropNameTv, varietyNameTv;

      public CropHistoryViewHolder(@NonNull View itemView, Action action) {
         super(itemView);

         cropImageIv = itemView.findViewById(R.id.cropImage);
         cropNameTv = itemView.findViewById(R.id.cropName);
         varietyNameTv = itemView.findViewById(R.id.varietyName);

      }

      public void update(CropHistory cropHistory){

         Crops_Main crop = action.getCrop(cropHistory.getCropId());
         VarietyModel variety = action.getVariety(cropHistory.getVarietyId());

         Glide.with(itemView.getContext()).load(crop.getIcon()).into(cropImageIv);

         System.out.println("Crop History: " + GsonUtils.toJson(cropHistory));
         System.out.println("Crop: " + crop);
         System.out.println("Variety: " + variety);

         cropNameTv.setText(crop.getName());
         varietyNameTv.setVisibility(variety == null ? View.INVISIBLE : View.VISIBLE);
         varietyNameTv.setText(variety != null ? variety.getName() : "");
      }
   }

   public static interface Action{
      ArrayList<CropHistory> getCropHistoryList();
      Crops_Main getCrop(String cropId);
      VarietyModel getVariety(String varietyId);
   }
}
