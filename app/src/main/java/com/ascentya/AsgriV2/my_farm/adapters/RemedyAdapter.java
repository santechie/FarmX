package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.ZoneRemedy;
import com.ascentya.AsgriV2.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RemedyAdapter extends RecyclerView.Adapter<RemedyAdapter.RemedyViewHolder> {

   private Action action;

   public RemedyAdapter(Action action){
      this.action = action;
   }

   @NonNull
   @Override
   public RemedyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_remedy_item, parent, false);
      RemedyViewHolder viewHolder = new RemedyViewHolder(view, action);
      return viewHolder;
   }

   @Override
   public void onBindViewHolder(@NonNull RemedyViewHolder holder, int position) {
      holder.update(action.getRemedyList().get(position));
   }

   @Override
   public int getItemCount() {
      return action.getRemedyList().size();
   }

   class RemedyViewHolder extends RecyclerView.ViewHolder{

      private Action action;
      private TextView activityTv, fertilizerTv, quantityTv, costTv;

      public RemedyViewHolder(@NonNull View itemView, Action action) {
         super(itemView);
         this.action = action;

         activityTv = itemView.findViewById(R.id.remedyName);
         fertilizerTv = itemView.findViewById(R.id.remedyFertilizer);
         quantityTv = itemView.findViewById(R.id.quantity);
         costTv = itemView.findViewById(R.id.cost);
      }

      public void update(ZoneRemedy remedy){

         activityTv.setText(remedy.getActivityType());
         fertilizerTv.setText(remedy.getFertilizerUsed());
         quantityTv.setText(remedy.getQuantity());
         costTv.setText(remedy.getCost());
      }
   }

   public interface Action{
      public ArrayList<ZoneRemedy> getRemedyList();
   }
}
