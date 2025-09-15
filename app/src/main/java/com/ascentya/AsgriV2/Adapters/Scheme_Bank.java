package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.Scheme_Details;
import com.ascentya.AsgriV2.Models.Banks_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Scheme_Bank extends RecyclerView.Adapter<Scheme_Bank.ViewHolder> {

    private List<Banks_Model> items;
    private Context ctx;

    public Scheme_Bank(Context context, List<Banks_Model> items) {
        this.items = items;
        this.ctx = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView bank_name, interest_rate, eligible, scheme_name;
        ImageView icon;

        public ViewHolder(View v) {
            super(v);
            bank_name = (TextView) v.findViewById(R.id.bank_name);
            interest_rate = (TextView) v.findViewById(R.id.interest_rate);
            eligible = (TextView) v.findViewById(R.id.eligible);
            scheme_name = (TextView) v.findViewById(R.id.scheme_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schemebank_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;

            view.bank_name.setText(items.get(position).getBank_name());

            if (!items.get(position).getShort_eligible().equalsIgnoreCase("")) {
                view.eligible.setText("Eligible : " + items.get(position).getShort_eligible());

            } else {
                view.eligible.setText("Eligible : NA ");

            }

            if (!items.get(position).getShort_interest().equalsIgnoreCase("")) {
                view.interest_rate.setText("Int rate : " + items.get(position).getShort_interest());
            } else {
                view.interest_rate.setText("Int rate : NA");
            }


            view.scheme_name.setText(items.get(position).getScheme());
            view.itemView.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
//                    if (((BaseActivity) ctx).sessionManager.isPaid()) {
                        Intent i = new Intent(ctx, Scheme_Details.class);
                        i.putExtra("bankname", items.get(position).getBank_name());
                        i.putExtra("eligible", items.get(position).getEligibility());
                        i.putExtra("amount", items.get(position).getAmount());
                        i.putExtra("interest", items.get(position).getInterest_rate());
                        i.putExtra("others", items.get(position).getOthers());
                        i.putExtra("pos", position);
                        ctx.startActivity(i);
//                    }else {
//                        ((BaseActivity) ctx).showPayDialog();
//                    }
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
