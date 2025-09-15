package com.ascentya.AsgriV2.e_market.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.data.model.CartItem;
import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import com.ascentya.AsgriV2.e_market.data.model.Stock;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private final int HEAD = 1;
    private final int SUB_HEAD = 2;
    private final int STOCK = 3;

    private Action action;
    private boolean showCategories = false;
    private boolean showItems = false;

    private ArrayList<String> expandedCategories = new ArrayList<>();
    private HashMap<String, ArrayList<String>> expandedItems = new HashMap<>();

    private ArrayList<Object> preparedStocks = new ArrayList<>();

    public StockAdapter(Action action) {
        this.action = action;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = getView(viewType, parent);
        StockViewHolder stockViewHolder = new StockViewHolder(this, view);
        return stockViewHolder;
    }

    private View getView(int viewType, ViewGroup viewGroup) {
        View view = null;
        switch (viewType) {
            case HEAD:
                view = createView(R.layout.view_emarket_stock_head_item, viewGroup);
                break;
            case SUB_HEAD:
                view = createView(R.layout.view_emarket_stock_sub_head_item, viewGroup);
                break;
            case STOCK:
                view = createView(R.layout.view_emarket_stock_item_1_item, viewGroup);
                break;
        }
        return view;
    }

    private View createView(int layoutId, ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

    public void itemExpand(String categoryId, String itemId, boolean isExpanded) {
        if (isExpanded) {
            if (!isItemExpanded(categoryId, itemId)) {
                ArrayList<String> expandedItemList = expandedItems.get(categoryId);
                if (expandedItemList == null)
                    expandedItemList = new ArrayList<>();
                expandedItemList.add(itemId);
                expandedItems.put(categoryId, expandedItemList);
                refreshData();
            }
        } else {
            ArrayList<String> expandedItemList = expandedItems.get(categoryId);
            if (expandedItemList != null) {
                expandedItemList.remove(itemId);
                refreshData();
            }
        }
    }

    public void categoryExpand(String categoryId, boolean isExpanded) {
        if (isExpanded) {
            if (!isCategoryExpanded(categoryId)) {
                expandedCategories.clear();
                expandedCategories.add(categoryId);
                refreshData();
            }
        } else {
            expandedCategories.remove(categoryId);
            refreshData();
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = preparedStocks.get(position);
        if (object instanceof Category)
            return HEAD;
        else if (object instanceof Item)
            return SUB_HEAD;
        else if (object instanceof Stock)
            return STOCK;
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        holder.update(preparedStocks.get(position));
    }

    @Override
    public int getItemCount() {
        return preparedStocks.size();
    }

    public void refreshData() {
        prepareDataSet();
        notifyDataSetChanged();
    }

    private void prepareDataSet() {
        preparedStocks.clear();
        for (Category category : action.getAllCategories()) {
            if (isStockFound(category.getId())) {
                if (!isSingleCategory() && showCategories) {
                    preparedStocks.add(category);
                }
                if (isCategoryExpanded(category.getId()) || isSingleCategory()) {
                    for (Item item : action.getItem(category.getId())) {
                        if (isStockFound(category.getId(), item.getId())) {
                            if (!isSingleItem() && showItems) {
                                preparedStocks.add(item);
                            }
                            if (isItemExpanded(category.getId(), item.getId()) || isSingleItem()) {
                                preparedStocks.addAll(getStocks(category.getId(), item.getId()));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isSingleCategory(){
        ArrayList<String> stockCatId = new ArrayList<>();
        for (Stock stock: action.getAllStocks()){
            String categoryId = stock.getCategoryId();
            if (!stockCatId.contains(categoryId)){
                stockCatId.add(categoryId);
            }
        }
        System.out.println( "Single Category: " + (stockCatId.size() == 1) );
        return stockCatId.size() == 1;
    }

    private boolean isSingleItem(){
        ArrayList<String> itemIds = new ArrayList<>();
        for (Stock stock: action.getAllStocks()){
            String itemId = stock.getItemId();
            if (!itemIds.contains(itemId)){
                itemIds.add(itemId);
            }
        }
        System.out.println( "Single Item: " + (itemIds.size() == 1) );
        return itemIds.size() == 1;
    }

    private boolean isSingleItemType(){
        return false;
    }

    private boolean isCategoryExpanded(String categoryId) {
        return true;
//        return expandedCategories.contains(categoryId);
    }

    private boolean isItemExpanded(String categoryId, String itemId) {
        return true;
       /* ArrayList<String> expandedCategoryItems = expandedItems.get(categoryId);
        if (expandedCategoryItems != null) {
            for (String expandedItemId : expandedCategoryItems) {
                if (expandedItemId.equals(itemId)) {
                    return true;
                }
            }
        }
        return false;*/
    }

    private List<Stock> getStocks(String categoryId, String itemId) {
        ArrayList<Stock> stocks = new ArrayList<>();
        if (isStockFound(categoryId, itemId)) {
            for (Stock stock : action.getAllStocks()) {
                if (stock.getCategoryId().equals(categoryId) && stock.getItemId().equals(itemId)) {
                    stocks.add(stock);
                }
            }
        }
        return stocks;
    }

    private boolean isStockFound(String categoryId) {
        for (Stock stock : action.getAllStocks()) {
            if (stock.getCategoryId().equals(categoryId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStockFound(String categoryId, String itemId) {
        for (Stock stock : action.getAllStocks()) {
            if (stock.getCategoryId().equals(categoryId)
                    && stock.getItemId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    class StockViewHolder extends RecyclerView.ViewHolder {
        private StockAdapter stockAdapter;

        private TextView name, quantity, date, price, place, itemTv;
        private ImageView arrow;
        private Chip bookChip, bookedChip;

        public StockViewHolder(StockAdapter stockAdapter, @NonNull View itemView) {
            super(itemView);
            this.stockAdapter = stockAdapter;
            name = itemView.findViewById(R.id.name);
            arrow = itemView.findViewById(R.id.arrowIv);

            quantity = itemView.findViewById(R.id.quantity);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
            place = itemView.findViewById(R.id.place);
            itemTv = itemView.findViewById(R.id.itemTv);
            bookChip = itemView.findViewById(R.id.book);
            bookedChip = itemView.findViewById(R.id.booked);

            itemView.setOnClickListener(v -> action.onClicked(getAbsoluteAdapterPosition()));
        }

        public void update(Object object) {
            if (object instanceof Category) {
                Category category = (Category) object;
                name.setText(category.getName());
                boolean isExpanded = stockAdapter.isCategoryExpanded(category.getId());
                name.setOnClickListener(view -> stockAdapter.categoryExpand(category.getId(),
                        !stockAdapter.isCategoryExpanded(category.getId())));
                arrow.setOnClickListener(view -> stockAdapter.categoryExpand(category.getId(),
                        !stockAdapter.isCategoryExpanded(category.getId())));
                if (isExpanded) {
                    arrow.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    arrow.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                }
            } else if (object instanceof Item) {
                Item item = (Item) object;
                name.setText(item.getName());
                boolean isExpanded = stockAdapter.isItemExpanded(item.getCategoryId(), item.getId());
                name.setOnClickListener(view -> stockAdapter.itemExpand(item.getCategoryId(), item.getId(),
                        !stockAdapter.isItemExpanded(item.getCategoryId(), item.getId())));
                arrow.setOnClickListener(view -> stockAdapter.itemExpand(item.getCategoryId(), item.getId(),
                        !stockAdapter.isItemExpanded(item.getCategoryId(), item.getId())));
                if (isExpanded) {
                    arrow.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    arrow.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                }
            } else if (object instanceof Stock) {
                Stock stock = (Stock) object;

                ItemType itemType = action.getItemType(
                        stock.getCategoryId(),
                        stock.getItemId(),
                        stock.getItemTypeId());

                Item item = action.getItem(stock.getCategoryId(), stock.getItemId());

                if (itemType != null){
                    name.setText(itemType.getName());
                    itemTv.setText("in " + item.getName());
                }else {
                    name.setText(item.getName());
                    itemTv.setText("");
                }

                quantity.setText(stock.getQuantity() + " " + stock.getUnit());
                date.setText(stock.getDateStart() + " - " + stock.getDateEnd());
                price.setText("Rs." + stock.getPrice());
                place.setText(stock.getPlaceName());

                CartItem cartItem = action.getCartItem(stock.getId());

                bookChip.setOnClickListener(v -> action.onBookClicked(stock.getId()));
                bookedChip.setOnClickListener(view -> action.onBookClicked(stock.getId()));
                bookedChip.setOnCloseIconClickListener(view -> action.removeCartItem(stock.getId()));

                if (cartItem == null){
                    bookChip.setVisibility(View.VISIBLE);
                    bookedChip.setVisibility(View.GONE);
                    bookedChip.setText("");
                }else {
                    bookChip.setVisibility(View.GONE);
                    bookedChip.setVisibility(View.VISIBLE);
                    bookedChip.setText(cartItem.getQuantity() + " " + cartItem.getQuantityType());
                }
            }
        }
    }

    public interface Action {

        void onClicked(int position);

        Stock getStock(String stockId);

        List<Stock> getAllStocks();

        List<Category> getAllCategories();

        List<Item> getAllItems();

        List<Item> getItem(String categoryId);

        Item getItem(String categoryId, String itemId);

        ItemType getItemType(String categoryId, String itemId, String itemTypeId);

        void onBookClicked(String stockId);

        CartItem getCartItem(String stockId);

        void removeCartItem(String stockId);

        void refreshStocks();
    }
}
