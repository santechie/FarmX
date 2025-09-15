package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.my_farm.model.FileImageItem;
import com.ascentya.AsgriV2.my_farm.model.RemedyItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RemedyItemAdapter extends RecyclerView.Adapter<RemedyItemAdapter.RemedyItemViewHolder> {

    private Action action;

    public RemedyItemAdapter(Action action){
        this.action = action;
    }


    @NonNull
    @Override
    public RemedyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_remedy_item_new, parent, false);
        return new RemedyItemViewHolder(action, view);
    }


    @Override
    public void onBindViewHolder(@NonNull RemedyItemViewHolder holder, int position) {
        holder.update(action.getRemedyList().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getRemedyList().size();
    }

    class RemedyItemViewHolder extends RecyclerView.ViewHolder
            implements ImageItemAdapter.Action {

        private TextView remedyTv, dateTv, quantityTv, descriptionTv, costTv, appliedTv, recoveredTv;
        //      private Button img, file;
        private ImageView adminIcon;

        private RecyclerView imageRv;
//        private ArrayList<ImageModel> imageModels = new ArrayList<>();
        private ArrayList<FileImageItem> fileModels = new ArrayList<>();
//        private ActivityImageAdapter imageAdapter;
        private ImageItemAdapter imageAdapter;

        public RemedyItemViewHolder(Action action, @NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> action.onClicked(getAdapterPosition()));

            remedyTv = itemView.findViewById(R.id.remedy);
            dateTv = itemView.findViewById(R.id.date);
            quantityTv = itemView.findViewById(R.id.quantity);
            descriptionTv = itemView.findViewById(R.id.description);
            costTv = itemView.findViewById(R.id.cost);
            appliedTv = itemView.findViewById(R.id.applied);
            recoveredTv = itemView.findViewById(R.id.recovered);
            imageRv = itemView.findViewById(R.id.RemedyImagesRv);
//         img = itemView.findViewById(R.id.imgDownload);
//         file = itemView.findViewById(R.id.fileDownload);
            adminIcon = itemView.findViewById(R.id.adminIcon);

            imageAdapter = new ImageItemAdapter(this);
        }

        public void update(RemedyItem item) {

//            imageModels.clear();
//            imageModels.addAll(item.getImageModels());
            fileModels.clear();
            fileModels.addAll(item.getFileModels());
            imageRv.setAdapter(imageAdapter);
            imageAdapter.notifyDataSetChanged();

            adminIcon.setVisibility(item.isAdmin() ? View.VISIBLE : View.GONE);

            remedyTv.setText(item.getRemedy());
            dateTv.setText(DateUtils.displayDayAndMonth(item.getCreatedAt()));
            descriptionTv.setText(item.getDescription());
            quantityTv.setText(item.getQuantity() + item.getQuantityUnit());
            costTv.setText(item.getCost());
            appliedTv.setText(item.getIsApplied().equals("0") ? "No" : "Yes");
            recoveredTv.setText(item.getIsRecovered().equals("0") ? "No" : "Yes");

//         file.setOnClickListener(new View.OnClickListener(){
//            public void onClick (View v){
//               Intent intent = new Intent("android.intent.action.GET_CONTENT");
//               ((Intent)intent).addCategory("android.intent.category.OPENABLE");
//               ((Intent)intent).setType("pdf/*");
////               Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////               intent.addCategory(Intent.CATEGORY_OPENABLE);
////               intent.setType("image/*");
//               v.getContext().startActivity(intent);
//            }
//         });
        }
//
//        @Override
//        public ArrayList<FileImageItem> getImages() {
//            return fileModels;
//        }
//
//        @Override
//        public void update() {
//
//        }
//
//        @Override
//        public void onClicked(int position) {
//
//        }
//
//        @Override
//        public void onDelete(int position) {
//
//        }
//
//        @Override
//        public boolean showDelete(int position) {
//            return false;
//        }

        @Override
        public ArrayList<FileImageItem> getFileReportItems() {
            return fileModels;
        }
    }

        public interface Action {
            ArrayList<RemedyItem> getRemedyList();

            void onClicked(int position);
        }
    }

