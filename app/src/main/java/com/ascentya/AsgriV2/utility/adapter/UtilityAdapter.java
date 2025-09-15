package com.ascentya.AsgriV2.utility.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.utility.model.Utility;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UtilityAdapter extends ArrayAdapter<Utility> {

    public UtilityAdapter(@NonNull Context context, List<Utility> utilityList) {
        super(context, R.layout.view_utility_item, utilityList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_utility_item, parent, false);
        Utility utility = getItem(position);
        ((ImageView) view.findViewById(R.id.image)).setImageResource(utility.getImage());
        ((TextView) view.findViewById(R.id.text)).setText(utility.getTestType().name);
        return view;
    }

    interface Action{

    }
}
