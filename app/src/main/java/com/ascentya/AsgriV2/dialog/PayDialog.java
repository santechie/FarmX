package com.ascentya.AsgriV2.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.CommonRecyclerAdapter;
import com.ascentya.AsgriV2.Models.Subscription;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class PayDialog extends BottomSheetDialogFragment implements CommonRecyclerAdapter.Action {

    private String title;
    private List<Object> objects;
    private Action action;

    private ViewDialog viewDialog;
    private RecyclerView recyclerView;
    private Button payBtn;

    private ArrayList<Subscription> subscriptions = new ArrayList<>();
    private CommonRecyclerAdapter adapter;
    private int selectedSubscription = 0;

    public PayDialog(Action action) { this.action = action; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDialog = new ViewDialog(getActivity());
        adapter = new CommonRecyclerAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_pay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        //((TextView) view.findViewById(R.id.title)).setText(title);

        recyclerView = view.findViewById(R.id.recyclerView);
        payBtn = view.findViewById(R.id.pay);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        payBtn.setOnClickListener(v->{
            dismiss();
            action.onPayNow(subscriptions.get(selectedSubscription));
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                setupFullHeight((BottomSheetDialog) dialogInterface);
                loadSubscription();
            }});
        return dialog;
    }

    private void loadSubscription(){
        viewDialog.showDialog();
        subscriptions.clear();

        AndroidNetworking.post(Webservice.getSubscriptions)
                .build().getAsJSONObject(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        viewDialog.hideDialog();
                        try {
                            if (response.getBoolean("status")){
                                subscriptions.addAll(GsonUtils.getGson().fromJson(response.getJSONArray("data").toString(),
                                        EMarketStorage.subscriptionListType));
                                updateSubscription();
                            }else {
                                Toasty.normal(getContext(), response.optString("message")).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Toasty.error(getContext(), "Subscriptions Parse Error").show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        viewDialog.hideDialog();
                        Toasty.error(getContext(), "Subscriptions Load Error").show();
                    }
                }
        );

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
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    @Override
    public CommonRecyclerAdapter.CommonViewHolder createViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_subscribe_item, parent, false);
        SubscribeViewHolder viewHolder = new SubscribeViewHolder(view, this::onSubscribeClicked);
        return viewHolder;
    }

    @Override
    public void updateView(CommonRecyclerAdapter.CommonViewHolder holder, int position) {
        ((SubscribeViewHolder) holder).update(subscriptions.get(position),
                selectedSubscription == position);
    }

    public void onSubscribeClicked(int position) {
        selectedSubscription = position;
        adapter.notifyDataSetChanged();
    }

    public interface Action{
        void onPayNow(Subscription subscription);
    }

    static class SubscribeViewHolder extends CommonRecyclerAdapter.CommonViewHolder{

        private TextView nameTv, validityTv, priceTv;

        public SubscribeViewHolder(@NonNull View itemView, Action action) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.name);
            validityTv = itemView.findViewById(R.id.validity);
            priceTv = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(view -> action.onSubscribeClicked(getAdapterPosition()));
        }

        public void update(Subscription subscription, boolean isSelected){

            nameTv.setText(subscription.getName());
            validityTv.setText(DateUtils.getDaysToText(subscription.getValidity()));
            priceTv.setText(ProductUtils.getPrice(subscription.getPrice()));

            int color = ContextCompat.getColor(itemView.getContext(),
                    isSelected ? R.color.green_farmx : R.color.grey_40);

            nameTv.setTextColor(color);
            validityTv.setTextColor(color);
            priceTv.setTextColor(color);
        }

        public interface Action{
            void onSubscribeClicked(int position);
        }
    }

}
