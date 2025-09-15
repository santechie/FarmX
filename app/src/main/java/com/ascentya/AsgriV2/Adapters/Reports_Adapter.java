package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.ZoneReport;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Reports_Adapter extends RecyclerView.Adapter<Reports_Adapter.ViewHolder> {

    private List<ZoneReport> items = new ArrayList<>();
    private Context ctx;


    public Reports_Adapter(Context context, List<ZoneReport> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView title, date, disease, cause;
        RecyclerView remedy_recycler;
        LinearLayout remedyLay;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            disease = view.findViewById(R.id.disease);
            cause = view.findViewById(R.id.cause);
            remedy_recycler = (RecyclerView) view.findViewById(R.id.remedy_recycler);
            remedyLay = view.findViewById(R.id.remedyLay);
        }

        public void update(ZoneReport zoneReport){

                title.setText(zoneReport.getContent());
                date.setText(DateUtils.splitDate(zoneReport.getCreatedAt()));
                disease.setText(zoneReport.getAffectedDisease());
                cause.setText(zoneReport.getAffectedCause());

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx);

                remedy_recycler.setLayoutManager(layoutManager);
                remedy_recycler.hasFixedSize();


                Remedy_Adapter reports_adapter = new Remedy_Adapter(ctx, zoneReport.getRemedyList());

                remedyLay.setVisibility(zoneReport.getRemedyList().isEmpty() ? View.GONE : View.VISIBLE);

                remedy_recycler.setAdapter(reports_adapter);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reports_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.update(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
