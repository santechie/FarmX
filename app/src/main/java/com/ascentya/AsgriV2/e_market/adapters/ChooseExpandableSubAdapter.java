package com.ascentya.AsgriV2.e_market.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;

public class ChooseExpandableSubAdapter extends BaseExpandableListAdapter {

    private final String HEAD_VIEW = "1";
    private final String HEAD_SINGLE_VIEW = "3";
    private final String CHILD_VIEW = "2";

    private boolean expandFirst = false;

    private String categoryId;
    private ChooseExpandableAdapter.Action action;

    public ChooseExpandableSubAdapter(String categoryId, ChooseExpandableAdapter.Action action) {
        this.categoryId = categoryId;
        this.action = action;
    }

    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public int getGroupCount() {
        return action.getItems(categoryId).size();
    }

    @Override
    public int getChildrenCount(int i) {
        return action.getItemTypes(categoryId, ((Item) getGroup(i)).getId()).size();
    }

    @Override
    public Object getGroup(int i) {
        return action.getItems(categoryId).get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return action.getItemTypes(categoryId, ((Item) getGroup(i)).getId()).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Item item = (Item) getGroup(i);
        View v = getView(i, -1, b, view, viewGroup);
        TextView textView = v.findViewById(R.id.name);
        CheckBox checkBox = v.findViewById(R.id.checkBox);
        if (textView != null) {
            textView.setText(item.getName());
        }
        if (checkBox != null) {
            checkBox.setText(item.getName());
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(action.isItemSelected(item.getCategoryId(), item.getId()));
            checkBox.setOnCheckedChangeListener(((compoundButton, b1) ->
                    action.selectItem(item.getCategoryId(), item.getId(), b1)
            ));
        }

        if (action.expandAll()){
            ((ExpandableListView) viewGroup).expandGroup(i);
        }

        return v;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View v = getView(i, i1, b, view, viewGroup);
        ItemType itemType = (ItemType) getChild(i, i1);
        ((CheckBox) v.findViewById(R.id.checkBox)).setText(itemType.getName());
        ((CheckBox) v.findViewById(R.id.checkBox)).setOnCheckedChangeListener(null);
        ((CheckBox) v.findViewById(R.id.checkBox)).setChecked(
                action.isItemTypeSelected(
                        itemType.getCategoryId(),
                        itemType.getItemId(),
                        itemType.getId()));
        ((CheckBox) v.findViewById(R.id.checkBox)).setOnCheckedChangeListener(((compoundButton, b1) ->
                action.setItemTypeSelected(itemType.getCategoryId(),
                        itemType.getItemId(), itemType.getId(), b1)));
        return v;
    }

    private View getView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String viewType = getViewType(i, i1);
        if (view != null && view.getTag().equals(viewType)) {
            return view;
        } else {
            View newView = null;
            switch (viewType) {
                case HEAD_VIEW:
                    newView = createView(R.layout.view_emarket_choose_child_head_item, viewGroup, viewType);
                    break;

                case CHILD_VIEW:
                    newView = createView(R.layout.view_emarket_choose_child_item, viewGroup, viewType);
                    break;

                case HEAD_SINGLE_VIEW:
                    newView = createView(R.layout.view_emarket_filter_head_single_item, viewGroup, viewType);
                    break;
            }
            return newView;
        }
    }

    private View createView(int layResId, ViewGroup viewGroup, String viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layResId, viewGroup, false);
        view.setTag(viewType);
        return view;
    }

    private String getViewType(int i, int i1) {
        if (i1 < 0) {
            if (hasItemTypes(i)) {
                return HEAD_VIEW;
            } else {
                return HEAD_SINGLE_VIEW;
            }
        } else {
            return CHILD_VIEW;
        }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
//        action.resize();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
//        action.resize();
    }

    private boolean hasItemTypes(int i) {
        return getChildrenCount(i) != 0;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
