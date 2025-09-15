package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class CustomListAdapter extends ArrayAdapter {

    private List<Crops_Main> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<Crops_Main> dataListAllItems;


    public CustomListAdapter(Context context, int resource, List<Crops_Main> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {

        return dataList.get(position).getName();
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.name);
        TextView sName = (TextView) view.findViewById(R.id.s_name);
        ImageView image = (ImageView) view.findViewById(R.id.crop_icon);

        Glide.with(mContext).load(dataList.get(position).getIcon()).into(image);
        strName.setText(dataList.get(position).getName());
        sName.setText(dataList.get(position).getS_name());


        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<Crops_Main>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                List<Crops_Main> matchValues = new ArrayList<>();

                for (Crops_Main dataItem : dataListAllItems) {
                    if (dataItem.getName().toLowerCase().contains(searchStrLowerCase.toLowerCase()) || dataItem.getS_name().toLowerCase().contains(searchStrLowerCase.toLowerCase())) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<Crops_Main>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }


}