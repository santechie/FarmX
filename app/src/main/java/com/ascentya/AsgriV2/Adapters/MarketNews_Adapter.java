package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.MarketNews_Model;
import com.ascentya.AsgriV2.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MarketNews_Adapter extends RecyclerView.Adapter<MarketNews_Adapter.ViewHolder> {

    private List<MarketNews_Model> items = new ArrayList<>();
    private Context ctx;
    String lang_id;


    public MarketNews_Adapter(Context context, List<MarketNews_Model> items) {
        this.items = items;
        this.lang_id = lang_id;

        ctx = context;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView title, disc, date;
        public ImageView icons;


        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            disc = (TextView) v.findViewById(R.id.desc);
            date = (TextView) v.findViewById(R.id.date);
            icons = (ImageView) v.findViewById(R.id.picture);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.marketnews_row, parent, false);


        vh = new ViewHolder(v);
        return vh;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final MarketNews_Model Position_Object = items.get(position);


            view.title.setText(Position_Object.getName());
            view.date.setText(Position_Object.getDate());
            view.disc.setText(Html.fromHtml(Position_Object.getDescription()));


            Glide.with(ctx).load(Position_Object.getImage()).error(ctx.getResources().getDrawable(R.drawable.news)).into(view.icons);


            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Intent i = new Intent(ctx, DetailedNews_Activity.class);
//                    i.putExtra("title", Position_Object.getName());
//                    i.putExtra("desc", Position_Object.getDescription());
//                    i.putExtra("date", Position_Object.getDate());
//                    ctx.startActivity(i);

                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<MarketNews_Model> list) {
        items = list;
        notifyDataSetChanged();
    }

}
