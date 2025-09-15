package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.my_farm.model.Menu;

import java.util.ArrayList;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


    /*
        public class MyFarmActivity extends BaseActivity implements MenuAdapter.Action

    *   private MenuAdapter menuAdapter;
    *   private ArrayList<Menu> menuList = new ArrayList<>(9);
    *
    *   recyclerView = findViewById(R.id.recyclerView);
        menuAdapter = new MenuAdapter(this, R.layout.view_farm_menu);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(menuAdapter);

        @Override
    public ArrayList<Menu> getMenuList() {
        return menuList;
    }

    @Override
    public void onClicked(int position) {

    }
    *
    * */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Action action;
    private int layoutId = R.layout.my_farm_menu;

    public MenuAdapter(Action action){
        this.action = action;
    }

    public MenuAdapter(Action action, @LayoutRes int layoutId){
        this.layoutId = layoutId;
        this.action = action;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        MenuViewHolder viewHolder = new MenuViewHolder(view, action);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.update(action.getMenuList().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getMenuList().size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{

        public MenuViewHolder(@NonNull View itemView, Action action) {
            super(itemView);
            itemView.setOnClickListener(view -> action.onClicked(getAdapterPosition()));
        }

        public void update(Menu menu){
            ((ImageView) itemView.findViewById(R.id.image)).setImageResource(menu.getImage());
            ((TextView) itemView.findViewById(R.id.title)).setText(menu.getTitle());
        }
    }

    public interface Action{
        ArrayList<Menu> getMenuList();
        void onClicked(int position);
    }
}
