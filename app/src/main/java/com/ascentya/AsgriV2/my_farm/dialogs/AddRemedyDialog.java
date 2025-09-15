package com.ascentya.AsgriV2.my_farm.dialogs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.ActivityImageAdapter;
import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.Utils.ImageUtils;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.model.FileImageItem;
import com.ascentya.AsgriV2.my_farm.model.RemedyItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class AddRemedyDialog implements ActivityImageAdapter.Action {

    private BaseActivity activity;
    private BottomSheetDialog dialog;

    private Action action;

    private Button addBtn;

    Spinner quantityUnitSpinner;
    EditText remedyEt, quantityEt, costEt, descriptionEt;
    ImageView addImageBtn;
    private CheckBox appliedCb, recoveredCb;
    public SessionManager sessionManager;
    private LinearLayoutCompat rateCont;
    private LinearLayout usefulLay, notusefulLay;

    RecyclerView imagesRv;
    ActivityImageAdapter imageAdapter;
    ArrayList<ImageModel> imageModels = new ArrayList<>();
    ArrayList<FileImageItem> fileModels = new ArrayList<>();

    List<String> QUANTITY_UNITS = Arrays.asList("mg", "g", "kg", "ml", "l");

    private boolean isUpdate;

    private RemedyItem item;

    public AddRemedyDialog(BaseActivity context, Action action, RemedyItem item) {
        this.activity = context;
        this.action = action;
        this.item = item;
        sessionManager = new SessionManager(context);
        dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_remedy, null);
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

        remedyEt = view.findViewById(R.id.remedy);
        quantityEt = view.findViewById(R.id.quantity);
        costEt = view.findViewById(R.id.cost);
        descriptionEt = view.findViewById(R.id.remedy_desc);

//        descriptionEt.setEllipsize();
        appliedCb = view.findViewById(R.id.applied);
        recoveredCb = view.findViewById(R.id.recovered);

        quantityUnitSpinner = view.findViewById(R.id.quantityUnit);
        quantityUnitSpinner.setAdapter(new ArrayAdapter<String>(activity,
                R.layout.spinner_row_space,
                QUANTITY_UNITS));

        addBtn = view.findViewById(R.id.add);
        rateCont = view.findViewById(R.id.rateCont);
        usefulLay = view.findViewById(R.id.upvote);
        notusefulLay = view.findViewById(R.id.downvote);

        updateUi();

        addBtn.setOnClickListener(v -> {
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

        usefulLay.setOnClickListener(v -> {
            if (Constants.RemedyRate.isUseful(item.getVote())) {
                updateRate(Constants.RemedyRate.NONE);
            } else {
                updateRate(Constants.RemedyRate.USEFUL);
            }
        });

        notusefulLay.setOnClickListener(v -> {
            if (Constants.RemedyRate.isNotUseful(item.getVote())) {
                updateRate(Constants.RemedyRate.NONE);
            } else {
                updateRate(Constants.RemedyRate.NOT_USEFUL);
            }
        });

        if (item != null) {

            remedyEt.setText(item.getRemedy());
            costEt.setText(item.getCost());
            quantityEt.setText(item.getQuantity());
            descriptionEt.setText(item.getDescription());
            quantityUnitSpinner.setSelection(QUANTITY_UNITS.indexOf(item.getQuantityUnit()));
            imageModels.addAll(item.getImageModels());
            appliedCb.setChecked(item.getIsApplied().equals("1"));
            recoveredCb.setChecked(item.getIsRecovered().equals("1"));
            addBtn.setVisibility(item.isAdmin() ? View.GONE : View.VISIBLE);
            rateCont.setVisibility(item.isAdmin() ? View.VISIBLE : View.GONE);
            if (item.isAdmin()) {
                updateVoteUI();
            }
            update();
            checkAdminUI();

            /*for (int i=0; i<typeSpinner.getAdapter().getCount(); i++){
                if (((Constants.ReportType) typeSpinner.getAdapter().getItem(i)).getValue()
                        .equals(item.getReportType().getValue())) {
                    typeSpinner.setSelection(i);
                    break;
                }
            }

            for (int i=0; i<partSpinner.getAdapter().getCount(); i++){
                if (((Constants.CropPart) partSpinner.getAdapter().getItem(i)).getValue()
                        .equals(item.getCropPart().getValue())) {
                    partSpinner.setSelection(i);
                    break;
                }
            }*/
        }
    }

    private void updateRate(int rate) {
        // OnResponse Success

        Toasty.success(addBtn.getContext(), String.valueOf(rate)).show();

        AndroidNetworking.post(Webservice.updateVoteRemedy)
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .addUrlEncodeFormBodyParameter("infection_remedy_id", item.getIrId())
                .addUrlEncodeFormBodyParameter("vote", String.valueOf(rate))
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Vote Response: \n" + response);
                        if (response.optBoolean("status")) {
                            String VoteJsonArray = response.optString("data");
                            System.out.println(VoteJsonArray);
                            item.setVote(String.valueOf(rate));
                            updateVoteUI();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    private void addImage() {
        PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult result) {
                String imagePath = ImageUtils
                        .setImage(addBtn.getContext(), result, null, true);
                ImageModel imageModel = new ImageModel();
                imageModel.setPath(imagePath);
                imageModel.setNew(true);
                imageModels.add(imageModel);
                update();
            }
        }).show(activity);
    }

    public void show(boolean isUpdate) {
        this.isUpdate = isUpdate;
        updateUi();
        dialog.show();
    }

    private void updateUi() {
        if (addBtn != null)
            addBtn.setText(dialog.getContext().getString(isUpdate ? R.string.update : R.string.add));
    }

    private void checkAdminUI(){
        if (item != null && item.isAdmin()){
            remedyEt.setFocusable(false);
            quantityEt.setFocusable(false);
            quantityUnitSpinner.setEnabled(false);
            costEt.setFocusable(false);
            descriptionEt.setFocusable(false);
            addImageBtn.setEnabled(false);
            appliedCb.setVisibility(View.INVISIBLE);
            recoveredCb.setVisibility(View.INVISIBLE);
        }else {
            remedyEt.setFocusable(true);
            quantityEt.setFocusable(true);
            quantityUnitSpinner.setEnabled(true);
            costEt.setFocusable(true);
            descriptionEt.setFocusable(true);
            addImageBtn.setEnabled(true);
            appliedCb.setVisibility(View.VISIBLE);
            recoveredCb.setVisibility(View.VISIBLE);
        }
    }

    public boolean isVisible() {
        return dialog != null && dialog.isShowing();
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
        DIalogwith_Image.showImageViewer(activity, "Images", getImageList());
    }

    private List<String> getImageList() {
        ArrayList<String> images = new ArrayList<>();
        if (imageModels != null) {
            for (ImageModel image : imageModels) {
                images.add(image.getImage());
            }
        }
        return images;
    }

    private void function(){
    }


    @Override
    public void onDelete(int position) {
        imageModels.remove(position);
        update();
    }

    @Override
    public boolean showDelete(int position) {
        return imageModels.get(position).getNew();
    }


    private void add() {
//        Constants.ReportType type = ((Constants.ReportType) typeSpinner.getSelectedItem());
//        Constants.CropPart part = ((Constants.CropPart) partSpinner.getSelectedItem());
        String remedy = remedyEt.getText().toString().trim();
        String quantity = quantityEt.getText().toString().trim();
        String description = descriptionEt.getText().toString();
        String quantityUnit = (String) quantityUnitSpinner.getSelectedItem();
        String cost = costEt.getText().toString().trim();
        ArrayList<ImageModel> selectedImages = imageModels;

        String applied = appliedCb.isChecked() ? "1" : "0";
        String recovered = recoveredCb.isChecked() ? "1" : "0";

        if (TextUtils.isEmpty(remedy)) {
            activity.toast("Enter Remedy!");
        } else if (TextUtils.isEmpty(quantity)) {
            activity.toast("Enter Quantity");
        } else {

            if (item == null) item = new RemedyItem();

            item.setRemedy(remedy);
            item.setQuantity(quantity);
            item.setDescription(description);
            item.setQuantityUnit(quantityUnit);
            item.setCost(cost);
            item.setIsApplied(applied);
            item.setIsRecovered(recovered);
            item.setImageModels(selectedImages);

            if (action.checkDuplicate(item)) {
                activity.toast("Similar Report Found!");
            } else {
                dialog.dismiss();
                action.onAdd(isUpdate, item);
            }
        }
    }

    public void updateVoteUI() {
        if (item != null && item.isAdmin()) {
            if (Constants.RemedyRate.isUseful(item.getVote())) {
                // useful tint
                // not useful remove tint
                ((ImageView) usefulLay.findViewById(R.id.upvode_icon))
                        .setColorFilter(ContextCompat.getColor(dialog.getContext(), R.color.green_farmx));
                ((TextView) usefulLay.findViewById(R.id.upvote_text))
                        .setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.green_farmx));
                ((ImageView) notusefulLay.findViewById(R.id.downvote_icon))
                        .setColorFilter(ContextCompat.getColor(dialog.getContext(), R.color.grey_10));
                ((TextView) notusefulLay.findViewById(R.id.downvote_text))
                        .setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.grey_10));
            } else if (Constants.RemedyRate.isNotUseful(item.getVote())) {
                ((ImageView) usefulLay.findViewById(R.id.upvode_icon))
                        .setColorFilter(ContextCompat.getColor(dialog.getContext(), R.color.grey_10));
                ((TextView) usefulLay.findViewById(R.id.upvote_text))
                        .setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.grey_10));
                ((ImageView) notusefulLay.findViewById(R.id.downvote_icon))
                        .setColorFilter(ContextCompat.getColor(dialog.getContext(), R.color.red));
                ((TextView) notusefulLay.findViewById(R.id.downvote_text))
                        .setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.red));
            } else {
                // both remove tint
                ((ImageView) usefulLay.findViewById(R.id.upvode_icon))
                        .setColorFilter(ContextCompat.getColor(dialog.getContext(), R.color.grey_10));
                ((TextView) usefulLay.findViewById(R.id.upvote_text))
                        .setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.grey_10));
                ((ImageView) notusefulLay.findViewById(R.id.downvote_icon))
                        .setColorFilter(ContextCompat.getColor(dialog.getContext(), R.color.grey_10));
                ((TextView) notusefulLay.findViewById(R.id.downvote_text))
                        .setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.grey_10));
            }
        }
    }

    public interface Action {
        void onAdd(boolean isUpdate, RemedyItem item);

        boolean checkDuplicate(RemedyItem item);
    }
}
