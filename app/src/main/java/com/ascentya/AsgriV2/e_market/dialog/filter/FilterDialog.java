package com.ascentya.AsgriV2.e_market.dialog.filter;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.DummyDataGenerator;
import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.Stock;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FilterDialog extends DialogFragment implements FilterDialogAdapter.Action {

    private Action action;
    private FilterDialogAdapter filterDialogAdapter;
    private List<Category> allCategories;
    private List<Item> allItems;
    private ArrayList<FilterGroup> filterGroups = new ArrayList<>();
    private ArrayList<String> selectedCategories = new ArrayList<>();
    private HashMap<String, ArrayList<String>> selectedItems = new HashMap<>();
    private boolean ignoreSelectAll = false;

    public FilterDialog(Action action) {
        this.action = action;
        prepareFilterGroups();
        setSelectAllData();
        prepareDateGroup();
    }

    public static FilterDialog getInstance(Action action) {
        return new FilterDialog(action);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterDialogAdapter = new FilterDialogAdapter(getChildFragmentManager(), this);
        view.findViewById(R.id.close).setOnClickListener(closeIv -> dismiss());
        ((ExpandableListView) view.findViewById(R.id.expandableListView))
                .setAdapter(filterDialogAdapter);
    }

    private void prepareFilterGroups() {

        SelectableCategoryGroup categoryGroup = new SelectableCategoryGroup();
        categoryGroup.setTitle("Category");

        allCategories = DummyDataGenerator.getCategories();
        categoryGroup.setCategories(allCategories);

        filterGroups.add(categoryGroup);

        SelectableItemGroup itemGroup = new SelectableItemGroup();
        itemGroup.setTitle("Items");

        // Getting All Items
        allItems = DummyDataGenerator.getAllItems();
        itemGroup.setItems(allItems);



        filterGroups.add(itemGroup);
    }

    private void setSelectAllData() {

        for (Category category : getSelectableCategoryGroup().getCategories()) {
            selectedCategories.add(category.getId());
            if (!selectedItems.containsKey(category.getId()))
                selectedItems.put(category.getId(), new ArrayList<>());
            ArrayList<String> selectedItemIds = selectedItems.get(category.getId());
            selectedItemIds.clear();
            for (Item item : getItems(category.getId())) {
                selectedItemIds.add(item.getId());
            }
        }
    }

    private void prepareDateGroup(){

        Pair<String, Long> startDatePair = getStartDateFromFilteredStock();
        Pair<String, Long> endDatePair = getEndDateFromFilteredStock();

        DateSelectorGroup dateSelectorGroup = getDateSelectorGroup();
        boolean add = dateSelectorGroup == null;

        if (add){
            dateSelectorGroup = new DateSelectorGroup(true,
                    startDatePair.first,
                    endDatePair.first);
        }else {
            dateSelectorGroup.setDateStart(startDatePair.first);
            dateSelectorGroup.setDateEnd(endDatePair.first);
        }

        dateSelectorGroup.startMillis = startDatePair.second;
        dateSelectorGroup.endMillis = endDatePair.second;

        dateSelectorGroup.selectedStartDate = startDatePair.second;
        dateSelectorGroup.selectedEndDate = endDatePair.second;

        if (add)
            filterGroups.add(dateSelectorGroup);

    }

    @Override
    public boolean ignoreSelectAll() {
        if (ignoreSelectAll) {
            ignoreSelectAll = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean isCategorySelected(String categoryId) {
        return selectedCategories.contains(categoryId);
    }

    @Override
    public boolean isItemSelected(String categoryId, String itemId) {
        return selectedItems.containsKey(categoryId) && selectedItems.get(categoryId).contains(itemId);
    }

    @Override
    public void categorySelected(String categoryId, boolean isSelected) {
        if (isSelected) {
            selectedCategories.add(categoryId);
            updateSelectAllCategory();
            refreshUI();
        } else {
            selectedCategories.remove(categoryId);
//            ignoreSelectAll = true;
            getSelectableCategoryGroup().setSelectAll(false);
            updateItemList();
            refreshUI();
        }
    }

    @Override
    public void itemSelected(String categoryId, String itemId, boolean isSelected) {
        if (isSelected) {
            selectedItems.get(categoryId).add(itemId);
        } else {
            selectedItems.get(categoryId).remove(itemId);
        }
        updateSelectAllItems();
        prepareDateGroup();
        refreshUI();
    }

    @Override
    public void selectAllChanged(FilterGroup filterGroup) {
        if (filterGroup instanceof SelectableCategoryGroup) {
            selectedCategories.clear();
            for (Category category : ((SelectableCategoryGroup) filterGroup).getCategories()) {
                if (((SelectableCategoryGroup) filterGroup).isSelectAll()) {
                    selectedCategories.add(category.getId());
                }
            }
            updateItemList();
        } else if (filterGroup instanceof SelectableItemGroup) {
            if (((SelectableItemGroup) filterGroup).isSelectAll()) {
                for (Item item : getSelectableItemGroup().getItems()) {
                    selectedItems.get(item.getCategoryId()).add(item.getId());
                }
            } else {
                for (ArrayList<String> selectedItem : selectedItems.values()) {
                    selectedItem.clear();
                }
            }
            prepareDateGroup();
        }
        refreshUI();
    }

    @Override
    public void updateDateStatus(boolean dateEnabled) {
        getDateSelectorGroup().enabled = dateEnabled;
        if (!dateEnabled){
            getDateSelectorGroup().selectedStartDate = getDateSelectorGroup().startMillis;
            getDateSelectorGroup().selectedEndDate = getDateSelectorGroup().endMillis;
        }
        refreshUI();
    }

    @Override
    public void onDateChanged() {
        refreshUI();
    }

    private void updateItemList() {
        ArrayList<Item> itemsToShow = new ArrayList<>();
        for (Category category : allCategories) {
            if (isCategorySelected(category.getId())) {
                itemsToShow.addAll(getItems(category.getId()));
            } else {
                // Remove not showed items in selected item list
                selectedItems.get(category.getId()).clear();
            }
        }
        getSelectableItemGroup().setItems(itemsToShow);
        updateSelectAllItems();
        prepareDateGroup();
    }

    private void updateSelectAllCategory() {
        boolean isAllSelected = isAnyCategorySelected();
        for (Category category : getAllCategories()) {
            isAllSelected = isAllSelected && selectedCategories.contains(category.getId());
        }
        updateItemList();
        getSelectableCategoryGroup().setSelectAll(isAllSelected);
    }

    private void updateSelectAllItems() {
        boolean isAllSelected = isAnyItemSelected();
        for (Item item : getSelectableItemGroup().getItems()) {
            isAllSelected = isAllSelected && selectedItems.get(item.getCategoryId()).contains(item.getId());
        }
        getSelectableItemGroup().setSelectAll(isAllSelected);
    }

    private boolean isAnyCategorySelected(){
        return !selectedCategories.isEmpty();
    }

    private boolean isAnyItemSelected(){
        for (Item item : getSelectableItemGroup().getItems()) {
            return selectedItems.get(item.getCategoryId()).contains(item.getId());
        }
        return false;
    }

    private void refreshUI() {
        filterDialogAdapter.refresh();
    }

    @Override
    public ArrayList<FilterGroup> getFilterGroup() {
        return filterGroups;
    }

    private Category getCategory(String categoryId) {
        List<Category> categories = allCategories;
        for (Category category : categories) {
            if (category.getId().equals(categoryId))
                return category;
        }
        return null;
    }

    private List<Category> getAllCategories() {
        return allCategories;
    }

    private List<Item> getAllItems() {
        return allItems;
    }

    private List<Item> getItems(String categoryId) {
        ArrayList<Item> categoryItems = new ArrayList<>();
        for (Item item : getAllItems()) {
            if (item.getCategoryId().equals(categoryId))
                categoryItems.add(item);
        }
        return categoryItems;
    }

    private Item getItem(String categoryId, String itemId) {
        List<Item> items = allItems;
        for (Item item : items) {
            if (item.getId().equals(itemId) && item.getCategoryId().equals(categoryId)) {
                return item;
            }
        }
        return null;
    }

    private SelectableCategoryGroup getSelectableCategoryGroup() {
        for (FilterGroup filterGroup : filterGroups) {
            if (filterGroup instanceof SelectableCategoryGroup) {
                return (SelectableCategoryGroup) filterGroup;
            }
        }
        return null;
    }

    private SelectableItemGroup getSelectableItemGroup() {
        for (FilterGroup filterGroup : filterGroups) {
            if (filterGroup instanceof SelectableItemGroup) {
                return (SelectableItemGroup) filterGroup;
            }
        }
        return null;
    }

    private DateSelectorGroup getDateSelectorGroup(){
        for (FilterGroup filterGroup: filterGroups){
            if (filterGroup instanceof DateSelectorGroup) {
                return (DateSelectorGroup) filterGroup;
            }
        }
        return null;
    }

    public interface Action{
        public List<Stock> getStocks();
    }

    public Pair<String, Long> getStartDateFromFilteredStock(){
        List<String> datesFromStock = getAllDatesFromFilteredStocks();
        Pair<String, Long> datePair = null;
        for (String dateString: datesFromStock){
            long dateLong = DateUtils.getDateFromText(dateString);

            if (datePair == null){
                datePair = new Pair<>(dateString, dateLong);
            }

            if (datePair.second > dateLong){
                datePair = new Pair<>(dateString, dateLong);
            }
        }
        if (datePair == null){
            long todayMillis = System.currentTimeMillis();
            String todayDateString = DateUtils.getDateFromMillis(todayMillis);
            datePair = new Pair<>(todayDateString, todayMillis);
        }
        return datePair;
    }

    public Pair<String, Long> getEndDateFromFilteredStock(){ List<String> datesFromStock = getAllDatesFromFilteredStocks();
        Pair<String, Long> datePair = null;
        for (String dateString: datesFromStock){
            long dateLong = DateUtils.getDateFromText(dateString);

            if (datePair == null){
                datePair = new Pair<>(dateString, dateLong);
            }

            if (datePair.second < dateLong){
                datePair = new Pair<>(dateString, dateLong);
            }
        }
        if (datePair == null){
            long todayMillis = System.currentTimeMillis();
            String todayDateString = DateUtils.getDateFromMillis(todayMillis);
            datePair = new Pair<>(todayDateString, todayMillis);
        }
        return datePair;
    }

    private List<String> getAllDatesFromFilteredStocks(){
        ArrayList<String> dates = new ArrayList<>();
        for (Stock stock: action.getStocks()){
            if (isStockFiltered(stock)){
               addDate(stock.getDateStart(), dates);
               addDate(stock.getDateEnd(), dates);
            }
        }
        return dates;
    }

    private boolean isStockFiltered(Stock stock){
        return isCategorySelected(stock.getCategoryId()) && isItemSelected(stock.getCategoryId(), stock.getItemId());
    }

    private void addDate(String date, ArrayList<String> dates){
        if (!dates.contains(date)) dates.add(date);
    }
}
