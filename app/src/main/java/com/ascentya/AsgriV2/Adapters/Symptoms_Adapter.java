package com.ascentya.AsgriV2.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Models.Symtoms_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Symptoms_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Symtoms_Model> items = new ArrayList<>();
    private Context ctx;


    public Symptoms_Adapter(Context context, List<Symtoms_Model> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView variety_pic;
        public TextView name;
        FloatingActionButton symtoms, gallery;


        public ViewHolder(View v) {
            super(v);

            variety_pic = (ImageView) v.findViewById(R.id.variety_pic);
            name = (TextView) v.findViewById(R.id.title);
            symtoms = (FloatingActionButton) v.findViewById(R.id.symtoms);
            gallery = (FloatingActionButton) v.findViewById(R.id.Gallery);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.symtoms_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Symtoms_Model Position_Object = items.get(position);
            view.name.setText(Position_Object.getTitle());


            Glide.with(ctx).load(Position_Object.getDp_img()).error(R.drawable.leaf).into(view.variety_pic);


            view.symtoms.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    if (Position_Object.getSymtoms().size() > 0) {
                        DIalogwith_Image obj = new DIalogwith_Image();
                        obj.dialog(ctx, Position_Object.getSymtoms(), Position_Object.getTitle());
                    } else {
                        Toast.makeText(ctx, R.string.symnotavailable, Toast.LENGTH_SHORT).show();
                    }
                }
            });


            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(Position_Object.getGallery_Images());
                if (jsonArray.length() > 0) {
                    if (!jsonArray.get(0).toString().equalsIgnoreCase("")) {

                        view.gallery.setImageResource(R.drawable.video);
//                        view.gallery.setText("Video");
                    } else {
                        view.gallery.setImageResource(R.drawable.novideo);

//                        view.gallery.setText(" No Video");
                    }


                } else {
                    view.gallery.setImageResource(R.drawable.novideo);
//                    view.gallery.setText(" No Video");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            view.gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    JSONArray jsonArray = null;
                    try {


                        if (items.get(position).getGallery_Images() != null) {
                            jsonArray = new JSONArray(Position_Object.getGallery_Images());
                            if (jsonArray.length() > 0) {

                                if (!jsonArray.get(0).toString().equalsIgnoreCase("")) {
                                    watchYoutubeVideo(jsonArray.get(0).toString());

                                } else {
                                    Toast.makeText(ctx, R.string.videocantplay, Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(ctx, R.string.videocantplay, Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(ctx, R.string.videocantplay, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


        }
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + id));
        try {
            ctx.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            ctx.startActivity(webIntent);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
