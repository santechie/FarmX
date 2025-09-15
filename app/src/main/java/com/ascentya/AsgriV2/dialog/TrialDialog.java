package com.ascentya.AsgriV2.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.managers.NewSubscription;
import com.ascentya.AsgriV2.managers.TrialListAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class TrialDialog extends BottomSheetDialogFragment implements TrialListAdapter.Action {

    private List<NewSubscription> upgradeSubscription = new ArrayList<>();
    private TrialListAdapter adapter;
    private RecyclerView recyclerView;
    private WormDotsIndicator indicator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TrialListAdapter(this);
    }

    public void setTrialList(List<NewSubscription> subscriptionList) {
        this.upgradeSubscription = subscriptionList;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.trail_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.close).setOnClickListener(v -> dismiss());

        recyclerView = view.findViewById(R.id.recyclerViewUpgrade);
        indicator = view.findViewById(R.id.Indicator);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
//        indicator(adapter.getItemCount(), recyclerView);

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                setupFullHeight((BottomSheetDialog) dialogInterface);
                updateSubscription();
            }});
        return dialog;
    }


    private void updateSubscription(){
        adapter.notifyDataSetChanged();
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }



    @Override
    public int getItemCount() {
        return upgradeSubscription.size();
    }

    @Override
    public List<NewSubscription> getSubscriptionList() {
        return upgradeSubscription;
    }
}
