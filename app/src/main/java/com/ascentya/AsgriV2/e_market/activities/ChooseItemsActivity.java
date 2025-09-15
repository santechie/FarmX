package com.ascentya.AsgriV2.e_market.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.adapters.ChooseExpandableAdapter;
import com.ascentya.AsgriV2.e_market.adapters.SearchSuggestionAdapter;
import com.ascentya.AsgriV2.e_market.data.DataFilterManager;
import com.ascentya.AsgriV2.e_market.data.SearchManager;
import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;

import java.util.ArrayList;
import java.util.List;

public class ChooseItemsActivity
        extends BaseActivity
        implements ChooseExpandableAdapter.Action,
        SearchManager.Action,
        TextWatcher {

    private DataFilterManager filterManager = DataFilterManager.getInstance();
    private SearchManager searchManager;
//    private SearchView searchView;
    private AutoCompleteTextView autoCompleteTextView;
    private ExpandableListView expandableListView;
    private ImageView closeSearch;
    private ChooseExpandableAdapter adapter;
    private int expandedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_items);

        setToolbarTitle("Choose", true);
//        setMenu(R.menu.emarket_choose_menu);

        searchManager = new SearchManager(dataRepository, this);

        adapter = new ChooseExpandableAdapter(this);

//        searchView = findViewById(R.id.searchView);
        autoCompleteTextView = findViewById(R.id.searchView);
        expandableListView = findViewById(R.id.expandableListView);
        closeSearch = findViewById(R.id.closeSearch);
        closeSearch.setOnClickListener(view -> autoCompleteTextView.getText().clear());

        expandableListView.setAdapter(adapter);

//        setSearchView();

        setAutoCompleteTextView();

      /*  expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                refreshChildLayout();
            }
        });*/
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        setSearchView();
        return super.onPrepareOptionsMenu(menu);
    }*/

   /* private void setSearchView() {
        int[] to = new int[]{android.R.id.text1};
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(this, R.color.black));
        searchView.setOnQueryTextListener(this);
        searchView.setSuggestionsAdapter(
                new SimpleCursorAdapter(this,
                        android.R.layout.simple_list_item_1,
                        null,
                        getSuggestions(), to,
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        );
    }*/

    private void setAutoCompleteTextView(){
        autoCompleteTextView.setAdapter(
                new SearchSuggestionAdapter(this,
                        getSuggestions())
        );
        autoCompleteTextView.addTextChangedListener(this);
    }

    private String[] getSuggestions(){
        ArrayList<String> suggestions = new ArrayList<>();
        for (Category category: dataRepository.getAllCategories()){
            suggestions.add(category.getName());
        }
        for (Item item: dataRepository.getAllItems()){
            suggestions.add(item.getName());
        }
        for (ItemType itemType: dataRepository.getAllItemTypes()){
            suggestions.add(itemType.getName());
        }
        String[] array = new String[suggestions.size()];
        for (int i=0; i<suggestions.size(); i++){
            array[i] = suggestions.get(i);
        }
        return array;
    }

    @Override
    public List<Category> getCategories() {
        if (searchManager.isQueryFound()){
            return searchManager.getSearchedCategories();
        }
        return dataRepository.getAllCategories();
    }

    @Override
    public List<Item> getAllItems() {
        if (searchManager.isQueryFound()){
            return searchManager.getSearchedItems();
        }
        return dataRepository.getAllItems();
    }

    @Override
    public List<Item> getItems(String categoryId) {
        if (searchManager.isQueryFound()){
            return searchManager.getSearchedItems(categoryId);
        }
        return dataRepository.getItems(categoryId);
    }

    @Override
    public List<ItemType> getAllItemTypes() {
        if (searchManager.isQueryFound()){
            return searchManager.getSearchedItemTypes();
        }
        return dataRepository.getAllItemTypes();
    }

    @Override
    public List<ItemType> getItemTypes(String categoryId, String itemId) {
        if (searchManager.isQueryFound())
            return searchManager.getSearchedItemTypes(categoryId, itemId);
        return dataRepository.getItemTypes(categoryId, itemId);
    }

    @Override
    public void onCategoryGroupExpanded(int c) {
        for (int i=0; i<adapter.getGroupCount(); i++){
            if (c != i){
                expandableListView.collapseGroup(i);
            }
        }
    }

    @Override
    public void onItemGroupExpanded(int i) {

    }

    @Override
    public void selectItem(String categoryId, String itemId, boolean selected) {
        filterManager.setItemSelected(categoryId, itemId, selected);
    }

    @Override
    public void setItemTypeSelected(String categoryId, String itemId, String itemTypeId, boolean selected) {
        filterManager.setItemTypeSelected(categoryId, itemId, itemTypeId, selected);
    }

    @Override
    public boolean isItemSelected(String categoryId, String itemId) {
        return filterManager.isItemSelected(categoryId, itemId);
    }

    @Override
    public boolean isItemTypeSelected(String categoryId, String itemId, String itemTypeId) {
        return filterManager.isItemTypeSelected(categoryId, itemId, itemTypeId);
    }

    @Override
    public boolean expandAll() {
        return false;
    }


    /*@Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("Query Submitted: " + query);
        searchManager.setQuery(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("Query Changed: " + newText);
        searchManager.setQuery(newText);
        return false;
    }*/

    @Override
    public void onListRefresh() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        showToast("Before Text", charSequence.toString());
//        searchManager.setQuery(charSequence.toString());
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        showToast("onText", charSequence.toString());
//        searchManager.setQuery(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {
//        showToast("After Text", editable.toString());
        searchManager.setQuery(editable.toString());
        updateCloseSearch(editable.toString());
    }

    private void showToast(String title, String value){
        Toast.makeText(this, title + ": " + value, Toast.LENGTH_SHORT).show();
    }

    private void updateCloseSearch(String text){
        if (text.isEmpty()){
            closeSearch.setVisibility(View.GONE);
        }else {
            closeSearch.setVisibility(View.VISIBLE);
        }
    }
}