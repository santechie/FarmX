package com.ascentya.AsgriV2.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddLandDialog extends BottomSheetDialogFragment {

    private Action action;

    public AddLandDialog(Action action) { this.action = action; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_land, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.register).setOnClickListener(v -> {
            dismiss();
            action.onRegisterClicked();
        });
        //((TextView) view.findViewById(R.id.title)).setText(title);
    }

    public interface Action{
        void onRegisterClicked();
    }

}
