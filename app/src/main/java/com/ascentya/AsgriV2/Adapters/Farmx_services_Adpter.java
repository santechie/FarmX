package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Activitys.Procurer_Registration;
import com.ascentya.AsgriV2.Interfaces_Class.MyInterface;
import com.ascentya.AsgriV2.Models.Farmx_Services_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.login_activities.Bank_Registration;
import com.ascentya.AsgriV2.login_activities.Equipment_Register;
import com.ascentya.AsgriV2.login_activities.Farmx_VluRegister;
import com.ascentya.AsgriV2.login_activities.Institute_Registration;
import com.ascentya.AsgriV2.login_activities.Insurance_Registration;
import com.ascentya.AsgriV2.login_activities.Warehouse_Registration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Farmx_services_Adpter extends RecyclerView.Adapter<Farmx_services_Adpter.ViewHolder> {

    private List<Farmx_Services_Model> items = new ArrayList<>();
    private Context ctx;
    private MyInterface listener;
    Boolean hide;


    public Farmx_services_Adpter(Context context, List<Farmx_Services_Model> items) {
        ctx = context;
        this.items = items;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public ImageView icons;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            icons = (ImageView) v.findViewById(R.id.icons);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmservice_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Farmx_Services_Model Position_Object = items.get(position);


            view.icons.setImageResource(Position_Object.getIcon());
            view.title.setText(Position_Object.getName());


            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 0) {

                        Intent i = new Intent(ctx, Bank_Registration.class);
                        ctx.startActivity(i);
                    } else if (position == 1) {
                        Intent i = new Intent(ctx, Insurance_Registration.class);
                        ctx.startActivity(i);
                    } else if (position == 2) {
                        Intent i = new Intent(ctx, Procurer_Registration.class);
                        ctx.startActivity(i);
                    } else if (position == 3) {
                        Intent i = new Intent(ctx, Warehouse_Registration.class);
                        ctx.startActivity(i);

                    } else if (position == 7) {
                        Intent i = new Intent(ctx, Equipment_Register.class);
                        ctx.startActivity(i);
                    } else if (position == 6) {
                        Intent i = new Intent(ctx, Farmx_VluRegister.class);
                        ctx.startActivity(i);
                    } else if (position == 8) {
                        Intent i = new Intent(ctx, Institute_Registration.class);
                        ctx.startActivity(i);
                    } else if (position == 4) {
//                        Intent i = new Intent(ctx, Farmx_Myfarm.class);
//                        ctx.startActivity(i);
                        Toast.makeText(ctx, "Updating soon", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ctx, "Updating soon", Toast.LENGTH_SHORT).show();
                    }


//                    Dialog_Master obj = new Dialog_Master();
//                    obj.dialog(ctx,items.get(position).getDisc(),);


                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

}
