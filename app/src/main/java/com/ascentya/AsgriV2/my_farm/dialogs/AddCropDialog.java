package com.ascentya.AsgriV2.my_farm.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class AddCropDialog {

    private BottomSheetDialog dialog;

    private String title;
    private List<Object> objects;
    private Action action;

    private Button addCropBtn, changeLandBtn;

    private Maincrops_Model land;
    private boolean showLandBtn;

    public AddCropDialog(Context context, Action action) {
        this.action = action;
        dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_crop, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        onViewCreated(view, null);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        view.findViewById(R.id.close).setOnClickListener(v -> {
            dialog.dismiss();
            action.onClose();
        });
        //((TextView) view.findViewById(R.id.title)).setText(title);

        addCropBtn = view.findViewById(R.id.add_crop);
        changeLandBtn = view.findViewById(R.id.change_land);

        updateUi();

        addCropBtn.setOnClickListener(v->{
            dialog.dismiss();
            action.onAddCrop(land);
        });

        changeLandBtn.setOnClickListener(v -> {
            dialog.dismiss();
            action.onChangeLand();
        });
    }

    public void show(Maincrops_Model land, boolean showChangeLand, FragmentManager fragmentManager, String tag){
        this.land = land;
        showLandBtn = showChangeLand;
        if (dialog.isShowing()) return;
        updateUi();
        dialog.show();
    }

    private void updateUi(){
        if (changeLandBtn != null) changeLandBtn.setVisibility(showLandBtn ? View.VISIBLE : View.GONE);
    }

    public boolean isVisible(){
        return dialog != null && dialog.isShowing();
    }

    public interface Action{
        void onAddCrop(Maincrops_Model land);
        void onChangeLand();
        void onClose();
    }

}
