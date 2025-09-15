package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Cultivation_Disc_Model;
import com.ascentya.AsgriV2.Models.PreCultivation_Model;
import com.ascentya.AsgriV2.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class PreCultivation_Adapter extends RecyclerView.Adapter<PreCultivation_Adapter.ItemRowHolder> {

    private List<PreCultivation_Model> dataList;
    private Context mContext;
    private String u_id;


    public PreCultivation_Adapter(Context context, List<PreCultivation_Model> dataList) {
        this.dataList = dataList;
        this.mContext = context;


    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cultivation_row, null);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        ItemRowHolder vh = new ItemRowHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        List<Cultivation_Disc_Model> singleSectionItems = dataList.get(i).getData();

        itemRowHolder.name.setText(dataList.get(i).getTitle());


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {


        // each data item is just a string in this case

        public TextView name;

        public ItemRowHolder(View view) {
            super(view);


            this.name = (TextView) view.findViewById(R.id.title);


        }

    }


}
