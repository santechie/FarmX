package com.ascentya.AsgriV2.managers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ProductUtils;

import java.util.List;

public class SubscriptionListAdapter
        extends RecyclerView.Adapter<SubscriptionListAdapter.SubscriptionViewHolder> {

    private Action action;

    public SubscriptionListAdapter(Action action) {
        this.action = action;
    }


    @NonNull
    @Override
    public SubscriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_subscription_item, parent, false);
        return new SubscriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, int position) {
        holder.update(action.getSubscriptionList().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getItemCount();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {


        public CommonViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void update() {

        }
    }

    public class SubscriptionViewHolder extends CommonViewHolder {

        private TextView nameTv, validityTv, priceTv, description;

        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.planName);
            validityTv = itemView.findViewById(R.id.planValidity);
            priceTv = itemView.findViewById(R.id.planPrice);
            description = itemView.findViewById(R.id.descriptionTv);
        }

        public void update(NewSubscription subscription) {

            nameTv.setText(subscription.getPlanName());
            validityTv.setText(subscription.getValidity() + "Months");
            priceTv.setText(ProductUtils.getPrice(subscription.getPrice()));
            description.setText(subscription.getPlanDesc());
        }

    }

    public interface Action {

        int getItemCount();
        List<NewSubscription> getSubscriptionList();
    }
}

