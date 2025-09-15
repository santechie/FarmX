package com.ascentya.AsgriV2.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.managers.NewSubscription;
import com.ascentya.AsgriV2.managers.SubscriptionListAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;


public class UpgradeDialog extends BottomSheetDialogFragment implements SubscriptionListAdapter.Action {

    private List<NewSubscription> upgradeSubscription = new ArrayList<>();
    private SubscriptionListAdapter adapter;
    private RecyclerView recyclerView;
    private WormDotsIndicator indicator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SubscriptionListAdapter(this);
    }

    public void setSubscriptionList(List<NewSubscription> subscriptionList) {
        this.upgradeSubscription = subscriptionList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.upgrade_dialog, container, false);
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
        indicator(adapter.getItemCount(), recyclerView);
    }

    private void indicator(int itemCount, RecyclerView recyclerView){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && itemCount > 0) {

            LinearLayout l1 = getBottomDotsLayout(itemCount);
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int i, int i1, int i2, int i3) {
                    int firstVisible = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    int lastVisible =((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    int total = 0;
                    for(int j = firstVisible; j<= lastVisible; j++){
                        total ++;

                    }

                    transitionDots(l1, lastVisible, total);
                }
            });
        }
    }

    private void transitionDots(LinearLayout l1, int lastVisibleIndex, int totalVisibleItems){
        for (int i =0 ; i<l1.getChildCount(); i++){
            if (l1.getChildAt(i) instanceof TextView){
                l1.getChildAt(i). setBackgroundResource(R.drawable.indicator0);
            }
        }

        for (int j=0; j<totalVisibleItems; j++){
            if (lastVisibleIndex >=0){
                l1.getChildAt(lastVisibleIndex).setBackgroundResource(R.drawable.indicator1);
            }
        }
    }

    private LinearLayout getBottomDotsLayout(int count){
        LinearLayout l1 = new LinearLayout(getContext());
        l1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        l1.setOrientation(LinearLayout.HORIZONTAL);
        l1.setGravity(Gravity.CENTER);

        for (int i =0; i<count; i++){
            TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,4);
            params.setMargins(3,2,3,2);
            tv.setLayoutParams(params);
            tv.setBackgroundResource(R.drawable.indicator0);
            l1.addView(tv);
        }
        return l1;
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

    @SuppressLint("NotifyDataSetChanged")
    private void updateSubscription(){
        adapter.notifyDataSetChanged();
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {

        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        assert bottomSheet != null;
        BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
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
        ((Activity) requireContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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