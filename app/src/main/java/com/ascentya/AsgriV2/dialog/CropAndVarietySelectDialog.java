package com.ascentya.AsgriV2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CropAndVarietySelectDialog extends Dialog {

   private EditText searchEt;
   private ImageView closeBtn;
   private RecyclerView recyclerView;
   private AppCompatButton selectBtn;
   private Configuration configuration;
   private Action action;
   private HashMap<String, ArrayList<String>> selectedCrops = new HashMap<>();
   private Adapter adapter;
   private ArrayList<Crops_Main> cropList = new ArrayList<>();
   private ArrayList<Crops_Main> dataList = new ArrayList<>();

  /* public CropAndVarietySelectDialog(@NonNull Context context) {
      super(context);
   }*/

   private String lastQuery = "";

   private TextWatcher textWatcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
         lastQuery = charSequence.toString();
         processData();
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
   };

   private void processData(){
      dataList.clear();
      if (!TextUtils.isEmpty(lastQuery)){
         for(Crops_Main crop: cropList){
            if (crop.getName().toLowerCase().contains(lastQuery)){
               addCropData(crop);
            }else {
               for (VarietyModel variety: crop.getVarieties()){
                  if (variety.getName().toLowerCase().contains(lastQuery)){
                     addCropData(crop);
                  }
               }
            }
         }
      }else {
         dataList.addAll(cropList);
      }
      adapterAction.update();
   }

   private void addCropData(Crops_Main crops_main){
      boolean found = false;
      for(Crops_Main crop: dataList){
         if (crop.getCrop_id().equals(crops_main.getCrop_id())){
            found = true;
         }
      }
      if (!found){
         dataList.add(crops_main);
      }
   }

   private CropAndVarietySelectDialog.Adapter.Action adapterAction =
           new CropAndVarietySelectDialog.Adapter.Action(){
              @Override
              public ArrayList<Crops_Main> getCrops() {
                 return dataList;
              }

              @Override
              public void onCropClicked(String cropId) {
                 adapter.cropClicked(cropId);
              }

              @Override
              public void onCropChecked(String cropId) {
                 if (!configuration.multiSelection
                         && !adapter.selectedVaieties.isEmpty()
                         && !adapter.selectedVaieties.containsKey(cropId)){
                    update();
                    Toast.makeText(getContext(), "Crop already Selected!",
                            Toast.LENGTH_LONG).show();
                    return;
                 }

                 if (configuration.except != null){
                    if(configuration.except.containsKey(cropId)
                            && configuration.except.get(cropId).isEmpty()){
                       Toast.makeText(getContext(), "Crop already Selected!",
                               Toast.LENGTH_LONG).show();
                       update();
                       return;
                    }
                 }

                 adapter.cropSelected(cropId);
              }

              @Override
              public boolean isCropExpanded(String cropId) {
                 return adapter.expandedCrops.contains(cropId);
              }

              @Override
              public void onVarietyClicked(String varietyId) {

                 if (!configuration.multiSelection
                         && !adapter.selectedVaieties.isEmpty()
                         && !adapter.isVarietyChecked(varietyId)){
                    update();
                    Toast.makeText(getContext(), "Crop already Selected!",
                            Toast.LENGTH_LONG).show();
                    return;
                 }

                 if (configuration.except != null){
                    for (String cropId: configuration.except.keySet()){
                       ArrayList<String> selectedVarieties = configuration.except.get(cropId);
                       if (!selectedVarieties.isEmpty()){
                          for (String selectedVarietyId: selectedVarieties){
                             if (selectedVarietyId.equals(varietyId)) {
                                Toast.makeText(getContext(), "Variety already Selected!",
                                        Toast.LENGTH_LONG).show();
                                update();
                                return;
                             }
                          }
                       }
                    }
                 }

                 adapter.varietyClicked(varietyId);
              }

              @Override
              public boolean isVarietyChecked(String varietyId) {
                 return adapter.isVarietyChecked(varietyId);
              }

              @Override
              public boolean isCropSelected(String cropId) {
                 return adapter.selectedVaieties.containsKey(cropId);
              }

              @Override
              public void update() {
                 adapter.updateData();
              }
           };

   public CropAndVarietySelectDialog(@NonNull Context context) {
      super(context, android.R.style.Theme_Material_NoActionBar_Fullscreen);
      adapter = new Adapter(adapterAction);
      this.cropList.clear();
      this.cropList.addAll(Webservice.getCrops());
      //System.out.println("Crops\n" + GsonUtils.toJson(cropList));

      processData();
   }

   public CropAndVarietySelectDialog setConfiguration(Configuration configuration){
      this.configuration = configuration;
      if (adapter != null){
         if (configuration.selected != null){
            for (String cropId: configuration.selected.keySet()){
               adapter.selectedVaieties.put(cropId, configuration.selected.get(cropId));
            }
         }
      }
      return this;
   }

   public CropAndVarietySelectDialog setAction(Action action){
      this.action = action;
      return this;
   }

   /*protected CropAndVarietySelectDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
      super(context, cancelable, cancelListener);
   }*/

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.dialog_crop_variety_select);
     /* getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
              WindowManager.LayoutParams.MATCH_PARENT);*/

      closeBtn = findViewById(R.id.close);
      closeBtn.setOnClickListener(view -> {
         dismiss();
         action.onClose();
      });

      searchEt = findViewById(R.id.searchView);
      searchEt.addTextChangedListener(textWatcher);

      recyclerView = findViewById(R.id.recyclerView);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setAdapter(adapter);

      selectBtn = findViewById(R.id.select);
      selectBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if (adapter.selectedVaieties.isEmpty()){
               Toast.makeText(getContext(), "Select Crop and Variety!", Toast.LENGTH_LONG).show();
               return;
            }
            dismiss();
            action.onSelected(adapter.selectedVaieties);
         }
      });
   }

   public static class Configuration{
      public HashMap<String, ArrayList<String>> selected, except;
      public boolean displayVarieties;
      public boolean multiSelection;
   }

   public interface Action{
      void onSelected(HashMap<String, ArrayList<String>> selectedVarieties);
      void onClose();
   }

   private static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

      private int CROP = 1;
      private int VARIETY = 2;

      private Action action;
      private ArrayList<Item> items = new ArrayList<>();
      private ArrayList<String> expandedCrops = new ArrayList<>();
      private HashMap<String, ArrayList<String>> selectedVaieties = new HashMap<>();

      public Adapter(Action action){
         this.action = action;
      }

      @Override
      public int getItemViewType(int position) {
         if (items.get(position) instanceof VarietyItem){
            return VARIETY;
         }else {
            return CROP;
         }
      }

      @NonNull
      @Override
      public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = null;
         if (viewType == CROP){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.crop_variety_dialog_crop_item, parent, false);
         }else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.crop_variety_dialog_variety_item, parent, false);
         }
         ViewHolder viewHolder = new ViewHolder(view, action);
         return viewHolder;
      }

      @Override
      public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.update(items.get(position));
      }

      @Override
      public int getItemCount() {
         return items.size();
      }

      public void cropClicked(String cropId){
         if (expandedCrops.contains(cropId)){
            expandedCrops.remove(cropId);
         }else {
            expandedCrops.add(cropId);
         }
         updateData();
      }

      public boolean isVarietyChecked(String varietyId){
         for (ArrayList<String> varietyIds: selectedVaieties.values()){
            for (String variety: varietyIds){
               if (variety.equals(varietyId)){
                  return true;
               }
            }
         }
         return false;
      }

      public void varietyClicked(String varietyId){
         VarietyItem varietyItem = getVariety(varietyId);
         if (selectedVaieties.containsKey(varietyItem.cropId)){
            ArrayList<String> varietyIds = selectedVaieties.get(varietyItem.cropId);
            boolean needToAdd = !varietyIds.contains(varietyId);
            if (needToAdd) varietyIds.add(varietyId);
            else varietyIds.remove(varietyId);
            if (varietyIds.isEmpty()){
               selectedVaieties.remove(varietyItem.cropId);
            }
         }else {
            ArrayList<String> varietyIds = new ArrayList<>();
            varietyIds.add(varietyId);
            selectedVaieties.put(varietyItem.cropId, varietyIds);
         }
         System.out.println("Selected\n" + selectedVaieties);
         updateData();
      }

      public VarietyItem getVariety(String varietyId){
         for (Item item: items){
            if (item instanceof VarietyItem){
               if (item.id.equals(varietyId)){
                  return (VarietyItem) item;
               }
            }
         }
         return null;
      }

      public void updateData(){
         items.clear();
         ArrayList<Crops_Main> crops = action.getCrops();
         for (Crops_Main crop: crops) {
            CropItem cropItem = new CropItem(crop.getCrop_id(), crop.getName(), crop.getIcon());
            cropItem.hasVarieties = !crop.getVarieties().isEmpty();
            items.add(cropItem);
            if(expandedCrops.contains(crop.getCrop_id())) {
               for (VarietyModel varietyModel : crop.getVarieties()) {
                  VarietyItem varietyItem = new VarietyItem(
                          varietyModel.getId(), varietyModel.getName(),
                          crop.getCrop_id(), crop.getName());
                  items.add(varietyItem);
               }
            }
         }
         notifyDataSetChanged();
      }

      public void cropSelected(String cropId) {
         System.out.println("Crop Selected: " + cropId);
         if (selectedVaieties.containsKey(cropId)){
            selectedVaieties.remove(cropId);
         }else {
            selectedVaieties.put(cropId, new ArrayList<>());
         }
         notifyDataSetChanged();
      }

      private class ViewHolder extends RecyclerView.ViewHolder{

         private Item item;
         private Action action;
         private TextView nameTv;
         private ImageView imageIv, arrowIv;
         private CheckBox checkBox;
         private boolean manualSet = false;

         public ViewHolder(@NonNull View itemView, Action action) {
            super(itemView);
            this.action = action;

            imageIv = itemView.findViewById(R.id.image);
            arrowIv = itemView.findViewById(R.id.arrow);
            nameTv = itemView.findViewById(R.id.name);
            checkBox = itemView.findViewById(R.id.check);

            itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  if (item instanceof CropItem){
                     action.onCropClicked(item.id);
                  }else {
                     action.onVarietyClicked(item.id);
                  }
               }
            });

            if (arrowIv != null){
               arrowIv.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                     action.onCropClicked(item.id);
                  }
               });
            }

            if (checkBox != null){
               checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                     if (manualSet){
                        manualSet = false;
                        return;
                     }
                     clicked();
                  }
               });
            }

         }

         private void clicked(){
            System.out.println("Clicked: " + GsonUtils.toJson(item));
            if (item instanceof CropItem) {
               System.out.println("Crop Clicked: " + GsonUtils.toJson(item));
               action.onCropChecked(item.id);
            }else if(item instanceof VarietyItem){
               System.out.println("Variety Clicked: " + GsonUtils.toJson(item));
               action.onVarietyClicked(item.id);
            }else {
               System.out.println("item instance invalid!");
            }
         }

         public void update(Item item){
            this.item = item;
            if (nameTv != null && item != null) nameTv.setText(item.name);
            if (arrowIv != null){
               if (item instanceof CropItem){
                  if (action.isCropExpanded(item.id)){
                     arrowIv.setImageResource(R.drawable.ic_arrow_up);
                  }else {
                     arrowIv.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                  }
               }
            }

            if (checkBox != null){
               if (item instanceof CropItem) {
                  boolean selected = action.isCropSelected(item.id);
                  manualSet = checkBox.isChecked() != selected;
                  checkBox.setChecked(selected);
               }else {
                  boolean selected = action.isVarietyChecked(item.id);
                  manualSet = checkBox.isChecked() != selected;
                  checkBox.setChecked(selected);
               }
            }
            if (item instanceof CropItem){
               if (action.isCropSelected(item.id)){
                  nameTv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green_farmx));
               }else {
                  nameTv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.mat_black));
               }
            }
            setImage();
         }

         private void setImage(){
            if (imageIv != null){
               if (item != null && item instanceof CropItem){
                  Glide.with(itemView).load(((CropItem) item).image).into(imageIv);
               }
            }
         }
      }

      interface Action{
         ArrayList<Crops_Main> getCrops();
         void onCropClicked(String cropId);
         void onCropChecked(String cropId);
         boolean isCropExpanded(String cropId);
         void onVarietyClicked(String varietyId);
         boolean isVarietyChecked(String varietyId);
         boolean isCropSelected(String cropId);
         void update();
      }

      class Item{
         String id, name;
         public Item(String id, String name){
            this.id = id;
            this.name = name;
         }
      }

      class CropItem extends Item{
         String image;
         boolean hasVarieties = false;
         public CropItem(String id, String name, String image){
            super(id, name);
            this.image = image;
         }
      }

      class VarietyItem extends Item{
         String cropId, cropName;
         public VarietyItem(String id, String name, String cropId, String cropName){
            super(id, name);
            this.cropId = cropId;
            this.cropName = cropName;
         }
      }
   }
}
