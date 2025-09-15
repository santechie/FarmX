package com.ascentya.AsgriV2.my_farm.dialogs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.ActivityImageAdapter;
import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ImageUtils;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.model.CropInfectionType;
import com.ascentya.AsgriV2.my_farm.model.FileImageItem;
import com.ascentya.AsgriV2.my_farm.model.InfectionType;
import com.ascentya.AsgriV2.my_farm.model.NewReportItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public class AddNewReportItemDialog implements ActivityImageAdapter.Action {

    private BaseActivity activity;
    private BottomSheetDialog dialog;

    private Action action;

    private Button addBtn;

    Spinner typeSpinner, partSpinner;
    EditText nameEt, descriptionEt;
    ImageView addImageBtn;

    RecyclerView imagesRv;
    ActivityImageAdapter imageAdapter;
    ArrayList<ImageModel> imageModels = new ArrayList<>();
    ArrayList<FileImageItem> fileModels = new ArrayList<>();
    List<InfectionType> infectionTypeList = new ArrayList<>();
    List<CropInfectionType> cropTypeList = new ArrayList<>();

    private boolean isUpdate;

    private NewReportItem item;

    public AddNewReportItemDialog(BaseActivity context, Action action, NewReportItem item) {
        this.activity = context;
        this.action = action;
        this.item = item;
        dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_report_item, null);
        dialog.setContentView(view);
//        dialog.setCancelable(false);
        onViewCreated(view, null);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        view.findViewById(R.id.close).setOnClickListener(v -> {
            dialog.dismiss();
            //action.onClose();
        });
        //((TextView) view.findViewById(R.id.title)).setText(title);

        typeSpinner = view.findViewById(R.id.typeSpinner);
        partSpinner = view.findViewById(R.id.partSpinner);

        nameEt = view.findViewById(R.id.name);
        descriptionEt = view.findViewById(R.id.description);

        typeSpinner.setAdapter(new ArrayAdapter<Constants.ReportType>(activity,
                R.layout.spinner_row_space,
                Constants.ReportTypes.ALL));

        partSpinner.setAdapter(new ArrayAdapter<Constants.CropPart>(activity,
                R.layout.spinner_row_space,
                Constants.CropParts.ALL));

        addBtn = view.findViewById(R.id.add);

        updateUi();

        addBtn.setOnClickListener(v->{
            dialog.dismiss();
            //action.onAddCrop(land);
        });

        imagesRv = view.findViewById(R.id.imagesRv);

        imageAdapter = new ActivityImageAdapter(this);
        imagesRv.setLayoutManager(new LinearLayoutManager(imagesRv.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        imagesRv.setAdapter(imageAdapter);

        addImageBtn = view.findViewById(R.id.add_image);

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        addBtn.setOnClickListener(v -> add());

        if (item != null){
            nameEt.setText(item.getTitle());
            descriptionEt.setText(item.getSymptom());
            imageModels.addAll(item.getImageModels());
            update();

            for (int i=0; i<typeSpinner.getAdapter().getCount(); i++){
                if (((InfectionType) typeSpinner.getAdapter().getItem(i)).getTypeValue()
                        .equals(item.getReportType().getTypeValue())) {
                    typeSpinner.setSelection(i);
                    break;
                }
            }

            for (int i=0; i<partSpinner.getAdapter().getCount(); i++){
                if (((CropInfectionType) partSpinner.getAdapter().getItem(i)).getTypeValue()
                        .equals(item.getCropPart().getTypeValue())) {
                    partSpinner.setSelection(i);
                    break;
                }
            }
        }
        getInfectionType();
        getCropType();
    }
    private void addImage(){
        PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult result) {
                String imagePath = ImageUtils
                        .setImage(addBtn.getContext(), result, null, true);
                ImageModel imageModel = new ImageModel();
                imageModel.setPath(imagePath);
                imageModels.add(imageModel);
                update();
            }
        }).show(activity);
    }

    public void show(boolean isUpdate){
        this.isUpdate = isUpdate;
        updateUi();
        dialog.show();
    }

    private void updateUi(){
        if (addBtn != null)
            addBtn.setText(dialog.getContext().getString(isUpdate ? R.string.update : R.string.add));
    }

    public boolean isVisible(){
        return dialog != null && dialog.isShowing();
    }

    @Override
    public ArrayList<ImageModel> getImages() {
        return imageModels;
    }

    @Override
    public void update() {
        imageAdapter.notifyDataSetChanged();
        String text = partSpinner.getSelectedItem().toString();
    }


    @Override
    public void onClicked(int position) {
        DIalogwith_Image.showImageViewer(activity, "Images", getImageList());
    }

    private List<String> getImageList(){
        ArrayList<String> images = new ArrayList<>();
        if (imageModels != null) {
            for (ImageModel image : imageModels) {
                images.add(image.getImage());
            }
        }
        return images;
    }


    @Override
    public void onDelete(int position) {
        imageModels.remove(position);
        update();
    }

    @Override
    public boolean showDelete(int position) {
        return true;
    }

    private void add(){
        InfectionType type = ((InfectionType) typeSpinner.getSelectedItem());
        CropInfectionType part = (CropInfectionType) partSpinner.getSelectedItem();
        String title = nameEt.getText().toString().trim();
        String symptom = descriptionEt.getText().toString().trim();
        ArrayList<ImageModel> selectedImages = imageModels;

        if (TextUtils.isEmpty(title)){
            activity.toast("Enter Title!");
        }else if (TextUtils.isEmpty(symptom)){
            activity.toast("Enter Symptom");
        }else if (selectedImages.isEmpty()){
            activity.toast("Update Image!");
        }else {

            if (item == null) item = new NewReportItem();

            item.setTitle(title);
            item.setSymptom(symptom);
            item.getReportType();
            item.setReportType(type);
            item.setCropPart(part);
            item.setImageModels(selectedImages);

            if (action.checkDuplicate(item)){
                activity.toast("Similar Report Found!");
            }else {
                dialog.dismiss();
                action.onAdd(isUpdate, item);
            }
        }
    }

    public interface Action{
        void onAdd(boolean isUpdate, NewReportItem item);
        boolean checkDuplicate(NewReportItem item);
    }

    public void getInfectionType(){
        AndroidNetworking.post(Webservice.getInfectionType)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Infection Response: \n" + response);
                        if (response.optBoolean("status")) {
                            String InfectionJsonArray = response.optString("data");
                            System.out.println(InfectionJsonArray);
                            infectionTypeList = GsonUtils.getGson()
                                    .fromJson(InfectionJsonArray, EMarketStorage.InfectionReportListType);

                            ArrayAdapter<String> infection_adapter =
                                    new ArrayAdapter(activity, R.layout.spinner_item,
                                    infectionTypeList);
                            typeSpinner.setAdapter(infection_adapter);

                            updateInfectionType();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void updateInfectionType(){
        if (infectionTypeList != null){
            ArrayAdapter<String> soilAdapter = new ArrayAdapter(activity, R.layout.spinner_item, infectionTypeList);
            typeSpinner.setAdapter(soilAdapter);

            typeSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            infectionTypeList.get(i).getTypeId();
//                            getSoilType();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

        }
    }

    public void getCropType(){
       // AndroidNetworking.post(Webservice.getCropType)
        AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/crop/get_crop_parts")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Crop Response: \n" + response);
                        if (response.optBoolean("status")) {
                            String CropJsonArray = response.optString("data");
                            System.out.println(CropJsonArray);

                            cropTypeList = GsonUtils.getGson()
                                    .fromJson(CropJsonArray, EMarketStorage.CropReportListType);

                            ArrayAdapter<String> crop_adapter = new ArrayAdapter(activity, R.layout.spinner_item,
                                    cropTypeList);
                            partSpinner.setAdapter(crop_adapter);
                            updateCropType();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    public void updateCropType(){
        if (cropTypeList != null){
            ArrayAdapter<String> cropAdapter = new ArrayAdapter(activity, R.layout.spinner_item, cropTypeList);
            partSpinner.setAdapter(cropAdapter);

            partSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            cropTypeList.get(i).getTypeValue();
//                            getSoilType();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
        }
    }




}
