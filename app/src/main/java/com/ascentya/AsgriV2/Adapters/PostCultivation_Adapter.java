package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Models.PostCultivation_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.Dialog_Master;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PostCultivation_Adapter extends RecyclerView.Adapter<PostCultivation_Adapter.ItemRowHolder> {

    private List<PostCultivation_Model> dataList;
    private Context mContext;
    private String u_id;


    public PostCultivation_Adapter(Context context, List<PostCultivation_Model> dataList) {
        this.dataList = dataList;
        this.mContext = context;


    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cultivation_row, viewGroup, false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {
        final PostCultivation_Model Position_Object = dataList.get(i);

        itemRowHolder.name.setText(dataList.get(i).getTitle());

        if (dataList.size() > 0) {
            itemRowHolder.disc.setText(dataList.get(i).getData().get(0));
        }


        itemRowHolder.disc.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                if (Position_Object.getData().size() > 0) {
                    Dialog_Master obj = new Dialog_Master();
                    obj.dialog(mContext, Position_Object.getData(), Position_Object.getTitle());
                } else {
                    Toast.makeText(mContext, "Description are not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {


        // each data item is just a string in this case

        public TextView name, disc;

        public ItemRowHolder(View view) {
            super(view);


            this.name = (TextView) view.findViewById(R.id.title);
            this.disc = (TextView) view.findViewById(R.id.disc);


        }

    }


}
