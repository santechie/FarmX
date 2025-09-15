package com.ascentya.AsgriV2.e_market.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;

import java.util.ArrayList;
import java.util.List;

public class ChooseExpandableAdapter extends BaseExpandableListAdapter {

    public static final String GROUP_VIEW = "1";
    public static final String ITEM_HEAD_VIEW = "2";
    public static final String ITEM_VIEW = "3";
    public static final String ITEM_TYPE_VIEW = "4";

    private int expandChild = -1;

    private Action action;
    private ArrayList<ChooseExpandableSubAdapter> subAdapters =
            new ArrayList<>();

    public ChooseExpandableAdapter(Action action){
        this.action = action;
    }

    @Override
    public int getGroupCount() {
        return action.getCategories().size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return action.getCategories().get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return action.getItems(((Category) getGroup(i)).getId()).get(i1);
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
        View v = getView(i, -1, b, view, viewGroup);
        ((TextView) v.findViewById(R.id.name)).setText(((Category) getGroup(i)).getName());
        if (action.expandAll()){
            ((ExpandableListView) viewGroup).expandGroup(i);
        }
        return v;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View v = getView(i, i1, b, view, viewGroup);
        setData(i, i1, v);
        return v;
    }

    private View getView(int groupIndex, int childIndex, boolean b, View view, ViewGroup viewGroup){
        String viewType = getViewType(groupIndex, childIndex);
        if (view != null && view.getTag().equals(viewType)){
            return view;
        }else {
            View newView = null;
            switch (viewType){
                case GROUP_VIEW:
                    newView = createView(R.layout.view_emarket_choose_group_item, viewType, viewGroup);
                    break;

                case ITEM_HEAD_VIEW:
                    newView = createView(R.layout.view_emarket_choose_item_head_item, viewType, viewGroup);
                    break;

                case ITEM_VIEW:
                    newView = createView(R.layout.view_emarket_choose_child_item, viewType, viewGroup);
                    break;
            }
            return newView;
        }
    }

    private View createView(int layResId, String viewType, ViewGroup viewGroup){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layResId, viewGroup, false);
        view.setTag(viewType);
        return view;
    }

    private String getViewType(int groupIndex, int childIndex){
        if (childIndex == -1){
            return GROUP_VIEW;
        }else {
            /*Item item = (Item) getChild(groupIndex, childIndex);
            if (action.getItemTypes(item.getCategoryId(), item.getId()).isEmpty()){
                return ITEM_VIEW;
            }else {
                return ITEM_HEAD_VIEW;
            }*/
            return ITEM_HEAD_VIEW;
        }
    }

    private void setData(int i, int i1, View view){
        ChooseExpandableSubAdapter subAdapterNull = getSubAdapter(i);
        if (subAdapterNull == null){
            subAdapterNull = new ChooseExpandableSubAdapter(getCategory(i).getId(), action);
            subAdapters.add(subAdapterNull);
        }
        final ChooseExpandableSubAdapter subAdapter = subAdapterNull;
        System.out.println(i+" Adapter: "+subAdapter.hashCode());
        ExpandableListView expandableListView = view.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(subAdapter);

       /* if (expandChild == i){
            for (int c=0; c<subAdapter.getGroupCount(); c++){
                expandableListView.expandGroup(c);
            }
            expandChild = -1;
        }*/

//        new Handler().postDelayed((Runnable) () -> {subAdapter.notifyDataSetChanged();}, 1);
//        view.invalidate();
//        subAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        action.onCategoryGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private ChooseExpandableSubAdapter getSubAdapter(int i){
        if (hasAdapter(i)){
            for (ChooseExpandableSubAdapter subAdapter: subAdapters){
                if (subAdapter.getCategoryId().equals(getCategory(i).getId())){
                    return subAdapter;
                }
            }
        }
        return null;
    }

    private Category getCategory(int i){
        return (Category) getGroup(i);
    }

    private boolean hasAdapter(int i){
        for (ChooseExpandableSubAdapter subAdapter: subAdapters){
            if (subAdapter.getCategoryId().equals(getCategory(i).getId())){
                return true;
            }
        }
        return false;
    }

    public void notifyChildDataSetChanged(int i) {
        expandChild = i;
        notifyDataSetChanged();
       /* ChooseExpandableSubAdapter subAdapter = getSubAdapter(i);
        System.out.println("Sub Adapter: " + subAdapter);
        if (subAdapter != null)
            subAdapter.notifyDataSetChanged();*/
    }

    public interface Action{
        List<Category> getCategories();
        List<Item> getAllItems();
        List<Item> getItems(String categoryId);
        List<ItemType> getAllItemTypes();
        List<ItemType> getItemTypes(String categoryId, String itemId);
        void onCategoryGroupExpanded(int i);
        void onItemGroupExpanded(int i);
        void selectItem(String categoryId, String itemId, boolean selected);
        void setItemTypeSelected(String categoryId, String itemId, String itemTypeId, boolean selected);
        boolean isItemSelected(String categoryId, String itemId);
        boolean isItemTypeSelected(String categoryId, String itemId, String itemTypeId);
        boolean expandAll();
    }
}
