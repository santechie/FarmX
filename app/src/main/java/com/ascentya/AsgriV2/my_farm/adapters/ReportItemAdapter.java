package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.ActivityImageAdapter;
import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.my_farm.model.FileImageItem;
import com.ascentya.AsgriV2.my_farm.model.ReportItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.ReportItemViewHolder> {

   private Action action;

   public ReportItemAdapter(Action action){
      this.action = action;
   }

   @NonNull
   @Override
   public ReportItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_report_item, parent, false);
      return new ReportItemViewHolder(action, view);
   }

   @Override
   public void onBindViewHolder(@NonNull ReportItemViewHolder holder, int position) {
      holder.update(action.getReportItems().get(position));
   }

   @Override
   public int getItemCount() {
      return action.getReportItems().size();
   }

   class ReportItemViewHolder extends RecyclerView.ViewHolder implements ActivityImageAdapter.Action{

      private ActivityImageAdapter imageAdapter;
      private ArrayList<ImageModel> imageModels = new ArrayList<>();
      private ArrayList<FileImageItem> fileModels = new ArrayList<>();

      public ReportItemViewHolder(Action action, @NonNull View itemView) {
         super(itemView);
         itemView.setOnClickListener(v -> action.onClicked(getAdapterPosition()));
         //itemView.findViewById(R.id.delete).setOnClickListener(v -> action.onDelete(getAdapterPosition()));
         imageAdapter = new ActivityImageAdapter(this);
      }

      public void update(ReportItem item){

         imageModels.clear();
         imageModels.addAll(item.getImageModels());

         ((TextView) itemView.findViewById(R.id.type))
                 .setText(Constants.ReportTypes.get(item.getType())
                         + " - " +
                         Constants.CropParts.get(item.getPart()));
         ((TextView) itemView.findViewById(R.id.name)).setText(item.getName());
         ((TextView) itemView.findViewById(R.id.symptom)).setText(item.getSymptom());
         ((RecyclerView) itemView.findViewById(R.id.imagesRv)).setAdapter(imageAdapter);
         imageAdapter.notifyDataSetChanged();

         String applied = item.getApplied();
         String recovered = item.getRecovered();

         ((TextView) itemView.findViewById(R.id.remedy))
                 .setText(item.getRemedyCount().equals("0") ?
                         "No" : item.getRemedyCount());

         ((TextView) itemView.findViewById(R.id.applied)).setText(((applied != null && !applied.equals("0")) ? applied : "Not") + " Applied");
         if (applied != null) ((TextView) itemView.findViewById(R.id.recovered)).setText(((recovered != null && !recovered.equals("0")) ? "" : "Not ") + "Recovered");
         else ((TextView) itemView.findViewById(R.id.recovered)).setText("");
      }

      @Override
      public ArrayList<ImageModel> getImages() {
         return imageModels;
      }

      @Override
      public void update() {
         imageAdapter.notifyDataSetChanged();
      }

      @Override
      public void onClicked(int position) {

      }

      @Override
      public void onDelete(int position) {

      }

      @Override
      public boolean showDelete(int position) {
         return false;
      }
   }

   public interface Action{
      ArrayList<ReportItem> getReportItems();
      void onClicked(int position);
   }
}
