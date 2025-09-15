package com.ascentya.AsgriV2.e_market.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class SearchSuggestionAdapter extends ArrayAdapter<String> {


    public SearchSuggestionAdapter(@NonNull Context context, String[] suggestions) {
        super(context, android.R.layout.simple_list_item_1, suggestions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ((TextView) view.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        return view;
    }
}
