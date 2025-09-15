package com.ascentya.AsgriV2.e_market.dialog.filter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import es.dmoral.toasty.Toasty;

class FilterDialogAdapter extends BaseExpandableListAdapter {

    public static final int GROUP_TYPE_TEXT = 1;
    public static final int GROUP_TYPE_DATE = 2;

    public static final String CHILD_TEXT = "1";
    public static final String CHILD_DATE = "2";

    private FragmentManager fragmentManager;
    private Action action;

    FilterDialogAdapter(FragmentManager fragmentManager, Action action) {
        this.fragmentManager = fragmentManager;
        this.action = action;
    }

    @Override
    public int getGroupCount() {
        return action.getFilterGroup().size();
    }

    @Override
    public int getChildrenCount(int i) {
        if (action.getFilterGroup().get(i) instanceof SelectableCategoryGroup) {
            return ((SelectableCategoryGroup) action.getFilterGroup().get(i)).getCategories().size();
        } else if (action.getFilterGroup().get(i) instanceof SelectableItemGroup) {
            return ((SelectableItemGroup) action.getFilterGroup().get(i)).getItems().size();
        } else if (action.getFilterGroup().get(i) instanceof DateSelectorGroup){
            return 1;
        }
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return action.getFilterGroup().get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        if (action.getFilterGroup().get(i) instanceof SelectableCategoryGroup) {
            return ((SelectableCategoryGroup) action.getFilterGroup().get(i)).getCategories().get(i1);
        } else if (action.getFilterGroup().get(i) instanceof SelectableItemGroup) {
            return ((SelectableItemGroup) action.getFilterGroup().get(i)).getItems().get(i1);
        } else if (action.getFilterGroup().get(i) instanceof DateSelectorGroup){
            return action.getFilterGroup().get(i);
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
//        if (getGroup(i) instanceof SelectableCategoryGroup ||
//                getGroup(i) instanceof SelectableItemGroup) {
//            return GROUP_TYPE_TEXT;
//        }
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return Integer.parseInt(i+""+i1);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return setUpGroupView(i, b, view, viewGroup);
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return setUpChildView(i, i1, b, view, viewGroup);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private View setUpGroupView(int i, boolean b, View view, ViewGroup viewGroup){
        Object group = getGroup(i);
        view = createGroupView(i, b, view, viewGroup, group);
        updateGroupView(i, b, view, viewGroup, group);
        return view;
    }

    private View createGroupView(int i, boolean b, View groupView, ViewGroup viewGroup, Object group){
        if (groupView != null)
            return groupView;
        if (isCategoryGroup(group)){
            groupView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_emarket_filter_group_item, viewGroup, false);
        }else if (isItemGroup(group)){
            groupView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_emarket_filter_group_item, viewGroup, false);
        }else if (isDateGroup(group)){
            groupView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_emarket_filter_group_item, viewGroup, false);
        }
        return groupView;
    }

    private void updateGroupView(int i, boolean b, View groupView, ViewGroup viewGroup, Object group){
        if (isCategoryGroup(group)){
            SelectableCategoryGroup categoryGroup = (SelectableCategoryGroup) group;
            TextView nameTv = groupView.findViewById(R.id.name);
            CheckBox selectAll = groupView.findViewById(R.id.checkBox);
            nameTv.setText(categoryGroup.getTitle());
            selectAll.setOnCheckedChangeListener(null);
            selectAll.setChecked(categoryGroup.isSelectAll());
            selectAll.setOnCheckedChangeListener((compoundButton, b12) -> {
                categoryGroup.setSelectAll(b12);
                if (!action.ignoreSelectAll())
                    action.selectAllChanged(categoryGroup);
            });
        }else if (isItemGroup(group)){
            SelectableItemGroup itemGroup = (SelectableItemGroup) group;
            TextView nameTv = groupView.findViewById(R.id.name);
            CheckBox selectAll = groupView.findViewById(R.id.checkBox);
            nameTv.setText(itemGroup.getTitle());
            selectAll.setOnCheckedChangeListener(null);
            selectAll.setChecked(itemGroup.isSelectAll());
            selectAll.setOnCheckedChangeListener((compoundButton, b1) -> {
                itemGroup.setSelectAll(b1);
                action.selectAllChanged(itemGroup);
            });
        }else if (isDateGroup(group)){
            DateSelectorGroup dateGroup = (DateSelectorGroup) group;
            TextView nameTv = groupView.findViewById(R.id.name);
            CheckBox selectAll = groupView.findViewById(R.id.checkBox);
            nameTv.setText("Date Range");
            selectAll.setOnCheckedChangeListener(null);
            selectAll.setChecked(dateGroup.isEnabled());
            selectAll.setOnCheckedChangeListener((compoundButton, b1) -> {
                action.updateDateStatus(b1);
            });
        }
    }

    private View setUpChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup){
        Object child = getChild(i, i1);
        View childView = createChildView(i, i1, view, viewGroup, child);
        updateChildView(i, i1, childView, child);
        return childView;
    }

    private View createChildView(int i, int i1, View childView, ViewGroup viewGroup, Object child){
        if (childView != null && childView.getTag().equals(getChildTagForGroup((FilterGroup) getGroup(i))))
            return childView;

        if (isCategory(child)){
            childView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_emarket_filter_child_item, viewGroup, false);
            childView.setTag(CHILD_TEXT);
        }else if (isItem(child)){
            childView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_emarket_filter_child_item, viewGroup, false);
            childView.setTag(CHILD_TEXT);
        }else if (isDateGroup(child)){
            childView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_emarket_filter_child_date_item, viewGroup, false);
            childView.setTag(CHILD_DATE);
        }

        return childView;
    }

    private void updateChildView(int i, int i1, View childView, Object child){
        if (isCategory(child)){
            Category category = (Category) child;
            CheckBox checkBox = childView.findViewById(R.id.checkBox);
            checkBox.setText(category.getName());
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(action.isCategorySelected(category.getId()));
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                action.categorySelected(category.getId(), b);
            });
        }else if (isItem(child)){
            Item item = (Item) child;
            CheckBox checkBox = childView.findViewById(R.id.checkBox);
            checkBox.setText(item.getName());
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(action.isItemSelected(item.getCategoryId(), item.getId()));
            checkBox.setOnCheckedChangeListener(((compoundButton, b) -> {
                action.itemSelected(item.getCategoryId(), item.getId(), b);
            }));
        }else if (isDateGroup(child)){
            DateSelectorGroup dateSelectorGroup = (DateSelectorGroup) child;
            TextView startDate = childView.findViewById(R.id.startDate);
            TextView endDate = childView.findViewById(R.id.endDate);
            ImageView startDateIcon = childView.findViewById(R.id.startDateIcon);
            ImageView endDateIcon = childView.findViewById(R.id.endDateIcon);
            startDate.setText(DateUtils.getDateFromMillis(dateSelectorGroup.getSelectedStartDate()));
            endDate.setText(DateUtils.getDateFromMillis(dateSelectorGroup.getSelectedEndDate()));

           /* float alpha = 1f;

            if (!dateSelectorGroup.isEnabled())
                alpha = 0.5f;

            startDate.setAlpha(alpha);
            endDate.setAlpha(alpha);
            startDateIcon.setAlpha(alpha);
            endDateIcon.setAlpha(alpha);*/

            startDate.setEnabled(dateSelectorGroup.enabled);
            endDate.setEnabled(dateSelectorGroup.enabled);
            startDateIcon.setEnabled(dateSelectorGroup.enabled);
            endDateIcon.setEnabled(dateSelectorGroup.enabled);

            View.OnClickListener startClickListener = view -> {
                if (!dateSelectorGroup.isEnabled()){
                    Toasty.normal(view.getContext(), "Date Disabled").show();
                    return;
                }
                new DatePickDialog(fragmentManager, "Date Range",
                        dateSelectorGroup.selectedStartDate,
                        dateSelectorGroup.selectedEndDate,
                        dateSelectorGroup.startMillis,
                        dateSelectorGroup.endMillis,
                        (startDate1, endDate1) -> {
                            dateSelectorGroup.selectedStartDate = startDate1;
                            dateSelectorGroup.selectedEndDate = endDate1;
                            action.onDateChanged();
                        });
            };

            startDateIcon.setOnClickListener(startClickListener);
            endDateIcon.setOnClickListener(startClickListener);
            startDate.setOnClickListener(startClickListener);
            endDate.setOnClickListener(startClickListener);
        }
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    private boolean isCategoryGroup(Object object){ return object instanceof SelectableCategoryGroup; }
    private boolean isItemGroup(Object object){ return object instanceof SelectableItemGroup; }
    private boolean isDateGroup(Object object){ return object instanceof DateSelectorGroup; }
    private boolean isCategory(Object object){ return object instanceof Category; }
    private boolean isItem(Object object){ return object instanceof Item; }

    public String getChildTagForGroup(FilterGroup group){
        String tag = "";
        if (isCategoryGroup(group)){
            tag = CHILD_TEXT;
        }else if (isItemGroup(group)){
            tag = CHILD_TEXT;
        }else if (isDateGroup(group)){
            tag = CHILD_DATE;
        }
        return tag;
    }

    public interface Action {
        boolean ignoreSelectAll();
        boolean isCategorySelected(String categoryId);
        boolean isItemSelected(String categoryId, String itemId);
        void categorySelected(String categoryId, boolean isSelected);
        void itemSelected(String categoryId, String itemId, boolean isSelected);
        void selectAllChanged(FilterGroup filterGroup);
        void updateDateStatus(boolean dateEnabled);
        void onDateChanged();
        ArrayList<FilterGroup> getFilterGroup();
    }
}
