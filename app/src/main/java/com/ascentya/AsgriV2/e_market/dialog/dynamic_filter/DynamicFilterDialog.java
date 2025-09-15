package com.ascentya.AsgriV2.e_market.dialog.dynamic_filter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.DummyDataGenerator;
import com.ascentya.AsgriV2.e_market.dialog.dynamic_filter.adapter.DynamicFilterAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DynamicFilterDialog extends BottomSheetDialogFragment {

    private ImageView closeBtn;
    private RecyclerView recyclerView;
    private DynamicFilterAdapter adapter;

    public DynamicFilterDialog(Context context){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_dynamic_filter, container, false);

        getDialog().setOnShowListener(dialogInterface -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
            FrameLayout frameLayout = dialog.findViewById(R.id.design_bottom_sheet);
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) frameLayout.getParent();
            BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(frameLayout);
            sheetBehavior.setPeekHeight(frameLayout.getHeight());
            coordinatorLayout.getParent().requestLayout();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (adapter == null)
            adapter = new DynamicFilterAdapter(getChildFragmentManager());

        closeBtn = view.findViewById(R.id.closeBtn);
        recyclerView = view.findViewById(R.id.recyclerView);

        closeBtn.setOnClickListener(v -> dismiss());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadData();
    }

    private void loadData(){
        adapter.clearFilterGroups(false);
        adapter.updateFilterGroups(DummyDataGenerator.getFilterGroups());
    }


}
