package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.ActivityImageAdapter;
import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.my_farm.model.FileImageItem;
import com.ascentya.AsgriV2.my_farm.model.NewReportItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewReportItemAdapter extends RecyclerView.Adapter<NewReportItemAdapter.NewReportItemViewHolder> {

   private Action action;

   public NewReportItemAdapter(Action action){
      this.action = action;
   }

   @NonNull
   @Override
   public NewReportItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_report_item, parent, false);
      return new NewReportItemViewHolder(action, view);
   }

   @Override
   public void onBindViewHolder(@NonNull NewReportItemViewHolder holder, int position) {
      holder.update(action.getReportItems().get(position));
   }

   @Override
   public int getItemCount() {
      return action.getReportItems().size();
   }

   class NewReportItemViewHolder extends RecyclerView.ViewHolder implements ActivityImageAdapter.Action{

      private ActivityImageAdapter imageAdapter;
      private ArrayList<ImageModel> imageModels = new ArrayList<>();
      private ArrayList<FileImageItem> fileModels = new ArrayList<>();

      public NewReportItemViewHolder(Action action, @NonNull View itemView) {
         super(itemView);

         itemView.setOnClickListener(v -> action.onEdit(getAdapterPosition()));
         itemView.findViewById(R.id.delete).setOnClickListener(v -> action.onDelete(getAdapterPosition()));
         imageAdapter = new ActivityImageAdapter(this);
      }

      public void update(NewReportItem item){

         imageModels.clear();
         imageModels.addAll(item.getImageModels());

         ((TextView) itemView.findViewById(R.id.type))
                 .setText(item.getReportType().getTypeValue() + " - " + item.getCropPart().getCropName());
         ((TextView) itemView.findViewById(R.id.name)).setText(item.getTitle());
         ((TextView) itemView.findViewById(R.id.symptom)).setText(item.getSymptom());
         ((RecyclerView) itemView.findViewById(R.id.imagesRv)).setAdapter(imageAdapter);
         imageAdapter.notifyDataSetChanged();
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
      ArrayList<NewReportItem> getReportItems();
      void onEdit(int position);
      void onDelete(int position);
   }
}
