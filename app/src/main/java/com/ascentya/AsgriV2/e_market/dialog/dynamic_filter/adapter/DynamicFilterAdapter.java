package com.ascentya.AsgriV2.e_market.dialog.dynamic_filter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.Constants;
import com.ascentya.AsgriV2.e_market.data.model.FilterGroup;
import com.ascentya.AsgriV2.e_market.data.model.FilterValue;
import com.ascentya.AsgriV2.e_market.dialog.filter.DatePickDialog;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class DynamicFilterAdapter extends RecyclerView.Adapter<DynamicFilterAdapter.DynamicFilterViewHolder> {

    private static final int SINGLE = 1;
    private static final int MULTI = 2;
    private static final int NUMBER_RANGE = 3;
    private static final int FLOAT_RANGE = 4;
    private static final int DATE_RANGE = 5;

    private FragmentManager fragmentManager;
    private ArrayList<FilterGroup> filterGroups = new ArrayList<>();

    public DynamicFilterAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public DynamicFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = 0;
        switch (viewType) {
            case SINGLE:
            case MULTI:
                layoutId = R.layout.view_emarket_dynamic_filter_single_item;
                break;
            case NUMBER_RANGE:
                layoutId = R.layout.view_emarket_dynamic_filter_number_range_item;
                break;
            case FLOAT_RANGE:
                layoutId = R.layout.view_emarket_dynamic_filter_float_range_item;
                break;
            case DATE_RANGE:
                layoutId = R.layout.view_emarket_dynamic_filter_date_range_item;
                break;
        }

        View view = createView(layoutId, parent);
        DynamicFilterViewHolder viewHolder = new DynamicFilterViewHolder(view);
        return viewHolder;
    }

    private View createView(int layoutResId, ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicFilterViewHolder holder, int position) {
        holder.update(filterGroups.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        FilterGroup filterGroup = filterGroups.get(position);
        switch (filterGroup.getType()) {
            case Constants.FilterTypes.SINGLE:
                return SINGLE;
            case Constants.FilterTypes.MULTI:
                return MULTI;
            case Constants.FilterTypes.RANGE: {
                String valueType = "";
                if (filterGroup.getFilterValues().isEmpty()) {
                    return 0;
                } else {
                    valueType = filterGroup.getFilterValues().get(0).getValueType();
                    switch (valueType) {
                        case Constants.FilterValueTypes.NUMBER:
                            return NUMBER_RANGE;
                        case Constants.FilterValueTypes.FLOAT:
                            return FLOAT_RANGE;
                        case Constants.FilterValueTypes.DATE:
                            return DATE_RANGE;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return filterGroups.size();
    }

    public void updateFilterGroups(List<FilterGroup> filterGroups) {
        this.filterGroups.clear();
        this.filterGroups.addAll(filterGroups);
        notifyDataSetChanged();
    }

    public void clearFilterGroups(boolean notifyDataSetChanged) {
        this.filterGroups.clear();
        if (notifyDataSetChanged)
            notifyDataSetChanged();
    }

    class DynamicFilterViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv;
        private ChipGroup chipGroup;
        private RangeSlider rangeSlider;
        private Chip fromDate, toDate;
        private Button change;

        DynamicFilterViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name);
            chipGroup = itemView.findViewById(R.id.chipGroup);
            rangeSlider = itemView.findViewById(R.id.rangeSlider);

            fromDate = itemView.findViewById(R.id.fromDate);
            toDate = itemView.findViewById(R.id.toDate);
            change = itemView.findViewById(R.id.changeBtn);
        }

        public void update(FilterGroup filterGroup) {
            nameTv.setText(filterGroup.getName());
            switch (filterGroup.getType()) {
                case Constants.FilterTypes.SINGLE:
                case Constants.FilterTypes.MULTI:
                    prepareSingleItem(filterGroup,
                            filterGroup.getType().equals(Constants.FilterTypes.MULTI));
                    break;
                case Constants.FilterTypes.RANGE: {
                    String valueTypes = filterGroup.getFilterValues().get(0).getValueType();
                    switch (valueTypes) {
                        case Constants.FilterValueTypes.NUMBER:
                            prepareNumberRange(filterGroup);
                            break;
                        case Constants.FilterValueTypes.DATE:
                            prepareDateRange(filterGroup);
                            break;
                    }
                }
            }
        }

        private void prepareSingleItem(FilterGroup filterGroup, boolean isMulti) {
            chipGroup.removeAllViews();
            chipGroup.setSelectionRequired(!isMulti);
            chipGroup.setSingleSelection(!isMulti);
            ArrayList<FilterValue> filterValues = filterGroup.getFilterValues();
            for (int i = 0; i < filterValues.size(); i++) {
                FilterValue filterValue = filterValues.get(i);
                Chip chip = new Chip(itemView.getContext());
                chip.setText(filterValue.getName());
                chip.setCheckable(true);
                if (!isMulti)
                    chip.setChecked(i==0);
                /*chip.setChipBackgroundColorResource(R.color.colorAccent);
                chip.setCloseIconVisible(true);
                chip.setTextColor(getResources().getColor(R.color.white));
                chip.setTextAppearance(R.style.ChipTextAppearance);*/

                chipGroup.addView(chip);
            }
        }

        private void prepareNumberRange(FilterGroup filterGroup) {
            Float valueFrom = Float.parseFloat(filterGroup.getFilterValues().get(0).getName());
            Float valueTo = Float.parseFloat(filterGroup.getFilterValues().get(1).getName());
            System.out.println("Value From: " + valueFrom);
            System.out.println("Value To: " + valueTo);
            rangeSlider.setValueFrom(valueFrom);
            rangeSlider.setValueTo(valueTo);
            rangeSlider.setValues(valueFrom, valueTo);
        }

        private void prepareDateRange(FilterGroup filterGroup) {
            FilterValue fromDateVal = filterGroup.getFilterValues().get(0);
            FilterValue toDateVal = filterGroup.getFilterValues().get(1);

            fromDate.setText(fromDateVal.getName());
            toDate.setText(toDateVal.getName());
            change.setOnClickListener(view ->
                    showDateDialog(fromDateVal, toDateVal)
            );
        }

        private void showDateDialog(FilterValue fromDateVal, FilterValue toDateVal) {
            new DatePickDialog(fragmentManager,
                    "Select Date",
                    DateUtils.getDateFromText(fromDate.getText().toString()),
                    DateUtils.getDateFromText(toDate.getText().toString()),
                    DateUtils.getDateFromText(fromDateVal.getName()),
                    DateUtils.getDateFromText(toDateVal.getName()),
                    (DatePickDialog.Action) (startDate, endDate) -> {
                        fromDate.setText(DateUtils.getDateFromMillis(startDate));
                        toDate.setText(DateUtils.getDateFromMillis(endDate));
                    });
        }

    }
}
