package com.ascentya.AsgriV2.e_market.activities;

import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.EditText;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.e_market.adapters.StockAdapter;
import com.ascentya.AsgriV2.e_market.data.CartManager;
import com.ascentya.AsgriV2.e_market.data.DataFilterManager;
import com.ascentya.AsgriV2.e_market.data.DummyDataGenerator;
import com.ascentya.AsgriV2.e_market.data.model.CartItem;
import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import com.ascentya.AsgriV2.e_market.data.model.Stock;
import com.ascentya.AsgriV2.e_market.dialog.book.BookDialog;
import com.ascentya.AsgriV2.e_market.dialog.dynamic_filter.DynamicFilterDialog;
import com.ascentya.AsgriV2.e_market.dialog.filter.FilterDialog;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EMarketActivity
        extends BaseActivity
        implements FilterDialog.Action,
        StockAdapter.Action,
SearchView.OnQueryTextListener{

    private DataFilterManager filterManager = DataFilterManager.getInstance();

    private SearchView searchView;
    private ChipGroup chipGroup;
    private Chip chooseChip, categoryChip, itemChip;
    private MaterialButton filterBtn;
    private RecyclerView recyclerView;

//    private ConstraintLayout countLay;
//    private AppCompatButton cartBtn;
//    private TextView countTv;

    private FilterDialog filterDialog;
    private DynamicFilterDialog dynamicFilterDialog;
    private List<Stock> searchedStock = new ArrayList<>();
    private List<Stock> stocks = new ArrayList<>();

    private String query = "";

    private StockAdapter stockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emarket);

//        System.out.println(EMarketActivity.this.getClass().getSimpleName()+": / Task ID: "+getTaskId());

        setToolbarTitle(getString(R.string.emarket), true);
        setOverrideOnBackPressed(true);
        setMenu(R.menu.emarket_menu);
        setUpFilterView();
        setUpFilterDialog();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        ((EditText) searchView
                .findViewById(androidx.appcompat.R.id.search_src_text))
                .setTextColor(ContextCompat.getColor(this, R.color.black));

        stockAdapter = new StockAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(stockAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            /*case R.id.filter:
                filterDialog.show(getSupportFragmentManager(), "FilterDialog");
                break;*/
            case R.id.orderHistory:
                openActivity(OrderHistoryActivity.class);
                break;
            case R.id.cart:
                openActivity(CartActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpFilterView() {
        chipGroup = findViewById(R.id.chipGroup);
        chooseChip = findViewById(R.id.chooseChip);
        categoryChip = findViewById(R.id.categoryChip);
        itemChip = findViewById(R.id.itemChip);
        filterBtn = findViewById(R.id.filterBtn);
        recyclerView = findViewById(R.id.recyclerView);

//        countLay = findViewById(R.id.countLay);
//        cartBtn = findViewById(R.id.cartBtn);
//        countTv = findViewById(R.id.countTv);

        chooseChip.setOnClickListener(view -> openActivity(ChooseItemsActivity.class));
        filterBtn.setOnClickListener(view -> showDynamicFilterDialog());
    }

    private void loadStocks() {

        if(!checkSubscription(Modules.EMARKET, ModuleManager.ACCESS.VIEW)) return;

        if (filterManager.hasAnyFilters()){
            stocks.clear();
            List<Stock> allStocks = DummyDataGenerator.getStocks();
            for (int i=0; i<allStocks.size(); i++){
                Stock stock = allStocks.get(i);
                String categoryId = stock.getCategoryId();
                String itemId = stock.getItemId();
                String itemTypeId = stock.getItemTypeId();
                if (filterManager.isItemSelected(categoryId, itemId) ||
                filterManager.isItemTypeSelected(categoryId, itemId, itemTypeId)){
                    stocks.add(stock);
                }
            }
        }else {
            stocks.clear();
            stocks.addAll(DummyDataGenerator.getStocks());
        }

        refreshStocks();
    }

    private Stock getStockFor(String categoryId, String itemId){
        for (Stock stock: DummyDataGenerator.getStocks()){
            if (stock.getCategoryId().equals(categoryId) && stock.getItemId().equals(itemId)){
                return stock;
            }
        }
        return null;
    }

    private void setUpFilterDialog() {
        filterDialog = FilterDialog.getInstance(this);
        dynamicFilterDialog = new DynamicFilterDialog(this);
    }

    private void showDynamicFilterDialog(){
        try {
            dynamicFilterDialog.show(getSupportFragmentManager(),
                    DynamicFilterDialog.class.getSimpleName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Stock> getStocks() {
        return stocks;
    }

    @Override
    public void onClicked(int position) {
        openActivity(StockDetailsActivity.class,
                new Pair<>(StockDetailsActivity.STOCK, getAllStocks().get(position)));
    }

    @Override
    public Stock getStock(String stockId) {
        for (Stock stock: DummyDataGenerator.getStocks()){
            if (stock.getId().equals(stockId)){
                return stock;
            }
        }
        return null;
    }

    @Override
    public List<Stock> getAllStocks() {
        if(isSearching())
            return searchedStock;
        return getStocks();
    }

    @Override
    public List<Category> getAllCategories() {
        return dataRepository.getAllCategories();
    }

    @Override
    public List<Item> getAllItems() {
        return dataRepository.getAllItems();
    }

    @Override
    public List<Item> getItem(String categoryId) {
        return dataRepository.getItems(categoryId);
    }

    @Override
    public Item getItem(String categoryId, String itemId) {
        return dataRepository.getItem(categoryId, itemId);
    }

    @Override
    public ItemType getItemType(String categoryId, String itemId, String itemTypeId) {
        return dataRepository.getItemType(categoryId, itemId, itemTypeId);
    }

    @Override
    public void onBookClicked(String stockId) {
        if (!checkSubscription(Modules.EMARKET, ModuleManager.ACCESS.INSERT)) return;

        BookDialog bookDialog = new BookDialog(stockId, this);
        bookDialog.show(getSupportFragmentManager(), BookDialog.class.getSimpleName());
    }

    @Override
    public CartItem getCartItem(String stockId) {
        return CartManager.getInstance(this).getCartItem(stockId);
    }

    @Override
    public void removeCartItem(String stockId) {
        CartManager.getInstance(this).removeFromCart(stockId);
        refreshStocks();
    }

    @Override
    public void refreshStocks() {
        stockAdapter.refreshData();
//        updateCountView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStocks();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.query = query;
        prepareSearchedStock();
        refreshStocks();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.query = newText;
        prepareSearchedStock();
        refreshStocks();
        return true;
    }

    private boolean isSearching(){
        return query != null && !query.trim().isEmpty();
    }

    private void prepareSearchedStock(){
        searchedStock.clear();
        for (Stock stock: stocks){
            Category category = dataRepository
                    .getCategory(stock.getCategoryId());
            Item item = dataRepository
                    .getItem(stock.getCategoryId(), stock.getItemId());
            ItemType itemType = dataRepository
                    .getItemType(stock.getCategoryId(),
                            stock.getItemId(), stock.getItemTypeId());
            if (isContainsOrEqual(category.getName())
                    || isContainsOrEqual(item.getName())
                    || isContainsOrEqual(itemType.getName())) {
                searchedStock.add(stock);
            }
        }
    }

    private boolean isContainsOrEqual(String source){
        return source.toLowerCase().contains(query.toLowerCase())
                || source.toLowerCase().equals(query.toLowerCase());
    }

    /*private void updateCountView(){
        CartManager cartManager = CartManager.getInstance(this);
        int count = cartManager.getCount();
        if (count == 0){
            countLay.setVisibility(View.GONE);
            countTv.setText("0");
        }else {
            countLay.setVisibility(View.VISIBLE);
            countTv.setText(""+count);
        }
    }*/
}