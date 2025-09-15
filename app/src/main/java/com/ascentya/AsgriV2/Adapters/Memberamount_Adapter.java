package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Interfaces_Class.ContractUpdate;
import com.ascentya.AsgriV2.Models.AddMemberamount_Model;
import com.ascentya.AsgriV2.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Memberamount_Adapter extends RecyclerView.Adapter<Memberamount_Adapter.ViewHolder> {

    private List<AddMemberamount_Model> items = new ArrayList<>();
    private Context ctx;
    private Boolean delete;
    private ContractUpdate contractUpdate;

    public Memberamount_Adapter(Context context, List<AddMemberamount_Model> items, Boolean delete) {
        this.items = items;
        this.ctx = context;
        this.delete = delete;
    }

    public void setContractUpdate(ContractUpdate contractUpdate){
        this.contractUpdate = contractUpdate;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView c_name, costperday, hours, total_amount;
        ImageView delete_row;


        public ViewHolder(View v) {
            super(v);
            c_name = (TextView) v.findViewById(R.id.name);
            costperday = (TextView) v.findViewById(R.id.costperday);
            hours = (TextView) v.findViewById(R.id.hours);
            total_amount = (TextView) v.findViewById(R.id.total_amount);
            delete_row = (ImageView) v.findViewById(R.id.delete_row);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memberupdated_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;

            view.c_name.setText(items.get(position).getName());
            view.costperday.setText(items.get(position).getBillingtype());
            view.hours.setText(items.get(position).getHours());
            view.total_amount.setText(items.get(position).getAmount());
            if (delete) {
                view.delete_row.setVisibility(View.GONE);
            } else {
                view.delete_row.setVisibility(View.VISIBLE);
            }

            view.delete_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    removeItem(holder.getAdapterPosition());
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
        if (contractUpdate != null) contractUpdate.onContractUpdate();
    }
}
