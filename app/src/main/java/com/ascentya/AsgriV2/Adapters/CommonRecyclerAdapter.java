package com.ascentya.AsgriV2.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommonRecyclerAdapter
        extends RecyclerView.Adapter<CommonRecyclerAdapter.CommonViewHolder> {

    private Action action;

    public CommonRecyclerAdapter(Action action){
        this.action = action;
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return action.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        action.updateView(holder, position);
    }

    @Override
    public int getItemCount() {
        return action.getItemCount();
    }

    public static class CommonViewHolder extends RecyclerView.ViewHolder {


        public CommonViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void update(){

        }
    }

    public interface Action{

        int getItemCount();
        CommonViewHolder createViewHolder(@NonNull ViewGroup parent, int viewType);
        void updateView(CommonViewHolder holder, int position);
    }
}
