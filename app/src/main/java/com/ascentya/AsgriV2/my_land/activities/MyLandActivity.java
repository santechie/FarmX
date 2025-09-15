package com.ascentya.AsgriV2.my_land.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

public class MyLandActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_land);
        setToolbarTitle("Land", true);

        ((RecyclerView) findViewById(R.id.mainCropsRv)).setAdapter(new CropAdapter(2));
        ((RecyclerView) findViewById(R.id.interCropsRv)).setAdapter(new CropAdapter(2));
    }

    public static void open(Context context, Maincrops_Model model){
        Intent intent = new Intent(context, MyLandActivity.class);
        intent.putExtra("land", GsonUtils.getGson().toJson(model));
        context.startActivity(intent);
    }

    class CropAdapter extends RecyclerView.Adapter<CropVH>{

        private int count;

        public CropAdapter(int count){
            this.count = count;
        }

        @NonNull
        @Override
        public CropVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_land_crop_icon, parent, false);
            return new CropVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CropVH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return count;
        }
    }

    class CropVH extends RecyclerView.ViewHolder{

        public CropVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    interface Action{
        int getCount();
    }
}