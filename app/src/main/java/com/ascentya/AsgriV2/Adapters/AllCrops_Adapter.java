package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.Dialog_Interface;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.bumptech.glide.Glide;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AllCrops_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Crops_Main> items = new ArrayList<>();
    private Context ctx;
    private Dialog_Interface dialog_interface;
    private SessionManager sessionManager;

    public AllCrops_Adapter(Context context, Dialog_Interface dialog, List<Crops_Main> items) {
        this.items = items;
        ctx = context;
        dialog_interface = dialog;
        sessionManager = new SessionManager(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView c_name;
        ImageView icon;
        RelativeLayout icon_back;
        CardView parent_card;


        public ViewHolder(View v) {
            super(v);
            c_name = (TextView) v.findViewById(R.id.disc);
            icon = (ImageView) v.findViewById(R.id.icon);
            icon_back = (RelativeLayout) v.findViewById(R.id.icon_back);
            parent_card = (CardView) v.findViewById(R.id.parent_card);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cropslist_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;

            view.icon_back.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.scale_in));

            view.c_name.setText(items.get(position).getName());

            Glide.with(ctx).load(items.get(position).getIcon()).into(view.icon);

            view.c_name.setOnClickListener(view1 -> checkUser(position));

            view.itemView.setOnClickListener(view12 -> checkUser(position));
        }
    }

    private void checkUser(int position){

        if(ctx != null && ctx instanceof BaseActivity) {
            //if (((BaseActivity) ctx).checkPrivilege(CROPS, items.get(position).getCrop_id())){
                openCrop(position);
            //}
        }

        /*if(sessionManager.isGuest()) {
            ((BaseActivity) ctx).showRegisterDialog();
        }else if (sessionManager.isRegistered()){
            if (isAllowedCrop(position)) {
                openCrop(position);
            }else {
                Toasty.normal(ctx, "You can view Apple, Banana, Carrot and Ginger").show();
                ((BaseActivity) ctx).showPayDialog();
            }
        }else {
            openCrop(position);
        }*/
    }

    private boolean isAllowedCrop(int position){
        String cropId = items.get(position).getCrop_id();
        return cropId.equals("119") || cropId.equals("185") || cropId.equals("48") || cropId.equals("138");
    }

    private void openCrop(int position){
        Toast.makeText(ctx, items.get(position).getName(), Toast.LENGTH_SHORT).show();
        Webservice.Searchvalue = items.get(position).getName();
        Webservice.Searchicon = items.get(position).getIcon();
        Webservice.Search_id = items.get(position).getCrop_id();

        System.out.println(items.get(position).getName() + " - " + items.get(position).getCrop_id());

        Bus bus = DeleteBus.getInstance();
        bus.post(new DeleteEvent("load_main"));

        dialog_interface.foo(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<Crops_Main> list) {
        items = list;
        notifyDataSetChanged();
    }
}

