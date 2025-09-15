package com.ascentya.AsgriV2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityImageAdapter extends RecyclerView.Adapter<ActivityImageAdapter.ImageViewHolder> {

   private Action action;

   public ActivityImageAdapter(Action action){
      this.action = action;
   }

   @NonNull
   @Override
   public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ImageViewHolder viewHolder = new ImageViewHolder(LayoutInflater.from(parent.getContext())
              .inflate(R.layout.view_activity_image_item, parent, false), action);
      return viewHolder;
   }

   @Override
   public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
      holder.update(action.getImages().get(position));
   }

   @Override
   public int getItemCount() {
      return action.getImages().size();
   }

   class ImageViewHolder
           extends RecyclerView.ViewHolder {

      private ImageView imageView, deleteBtn;

      public ImageViewHolder(@NonNull View itemView, Action action) {
         super(itemView);
         imageView = itemView.findViewById(R.id.image);
         deleteBtn = itemView.findViewById(R.id.delete);

         imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               action.onClicked(getAdapterPosition());
            }
         });


         deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               action.onDelete(getAdapterPosition());
            }
         });

      }

      public void update(ImageModel model){
         deleteBtn.setVisibility(action.showDelete(getAdapterPosition()) ? View.VISIBLE : View.GONE);
         Glide.with(itemView.getContext()).load(model.getImage()).into(imageView);
      }
   }

   public interface Action{
      ArrayList<ImageModel> getImages();
      void update();
      void onClicked(int position);
      void onDelete(int position);
      boolean showDelete(int position);
   }
}
