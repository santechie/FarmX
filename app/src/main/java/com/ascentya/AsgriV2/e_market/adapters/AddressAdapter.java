package com.ascentya.AsgriV2.e_market.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.model.Address;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private Action action;

    public AddressAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_emarket_address_list_item, parent, false);
        return new AddressViewHolder(action, view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.update(getItem(position));
    }

    @Override
    public int getItemCount() {
        return action.getAllAddress().size();
    }

    public Address getItem(int position){
        return action.getAllAddress().get(position);
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder{

        private Action action;
        private TextView line1Tv, line2Tv, line3Tv, line4Tv;
        private MaterialButton selectBtn;
        private ImageView editBtn, deleteBtn;

        public AddressViewHolder(Action action, @NonNull View itemView) {
            super(itemView);
            this.action = action;

            line1Tv = itemView.findViewById(R.id.line1);
            line2Tv = itemView.findViewById(R.id.line2);
            line3Tv = itemView.findViewById(R.id.line3);
            line4Tv = itemView.findViewById(R.id.line4);

            editBtn = itemView.findViewById(R.id.edit);
            deleteBtn = itemView.findViewById(R.id.delete);

            selectBtn = itemView.findViewById(R.id.select);

            editBtn.setOnClickListener(v -> action.onEdit(getAbsoluteAdapterPosition()));
            deleteBtn.setOnClickListener(v -> action.onDelete(getAbsoluteAdapterPosition()));
            selectBtn.setOnClickListener(v -> action.onSelect(getAbsoluteAdapterPosition()));
        }

        public void update(Address address){

            line1Tv.setText(String.format("%s, %s", address.getDoorNumber(), address.getLandmark()));
            line2Tv.setText(String.format("%s, %s", address.getStreet(), address.getArea()));
            line3Tv.setText(String.format("%s - %s", address.getCity(), address.getPincode()));
            line4Tv.setText(String.format("%s, %s", address.getDistrict(), address.getState()));
        }
    }

    public interface Action{
        List<Address> getAllAddress();
        void onEdit(int position);
        void onDelete(int position);
        void onSelect(int position);
        void onRefresh();
    }
}
