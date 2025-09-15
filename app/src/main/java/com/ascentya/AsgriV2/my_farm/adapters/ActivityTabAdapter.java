package com.ascentya.AsgriV2.my_farm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.my_farm.model.Menu;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityTabAdapter extends RecyclerView.Adapter<ActivityTabAdapter.TabViewHolder> {

    private Action action;

    public ActivityTabAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public TabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_activity_tab_item, parent, false);
        TabViewHolder viewHolder = new TabViewHolder(action, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TabViewHolder holder, int position) {
        holder.update(action.getMenuList().get(position));
    }

    @Override
    public int getItemCount() {
        return action.getMenuList().size();
    }

    class TabViewHolder extends RecyclerView.ViewHolder{

        private Action action;

        public TabViewHolder(Action action, @NonNull View itemView) {
            super(itemView);
            this.action = action;
            itemView.setOnClickListener(v ->
                    action.onSelected(getAdapterPosition()));
        }

        public void update(Menu menu){
            ((ImageView) itemView.findViewById(R.id.image)).setImageResource(menu.getImage());
            ((TextView) itemView.findViewById(R.id.text)).setText(menu.getTitle());
            itemView.findViewById(R.id.indicator)
                    .setVisibility(action.getSelectedPosition() == getAdapterPosition() ?
                            View.VISIBLE : View.INVISIBLE);
        }
    }

    public interface Action{
        ArrayList<Menu> getMenuList();
        int getSelectedPosition();
        void onSelected(int position);
    }
}
