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

import com.ascentya.AsgriV2.Models.Identification_Stages;
import com.ascentya.AsgriV2.Models.PointerImages_Model;
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

public class Identification_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Identification_Stages> items = new ArrayList<>();
    private Context ctx;


    public Identification_Adapter(Context context, List<Identification_Stages> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image, variety_pic;
        public TextView name;
        public TextView sub_title;
        public FloatingActionButton desc, Gallery;


        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.variety_pic);

            name = (TextView) v.findViewById(R.id.title);
            desc = (FloatingActionButton) v.findViewById(R.id.desc);
            Gallery = (FloatingActionButton) v.findViewById(R.id.Gallery);
            sub_title = (TextView) v.findViewById(R.id.sub_title);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.identification_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Identification_Stages Position_Object = items.get(position);
            view.name.setText(Position_Object.getTitle());

            if (Position_Object.getStage() != null && !Position_Object.getStage().equalsIgnoreCase("")) {
                view.sub_title.setText(Position_Object.getStage());
            } else {
                view.sub_title.setVisibility(View.GONE);
            }


            Glide.with(ctx).load(Position_Object.getDp()).error(R.drawable.leaf).into(view.image);


            view.desc.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {


                    List<PointerImages_Model> Data = new ArrayList<>();


                    PointerImages_Model obj = new PointerImages_Model();

                    if (Position_Object.getDesc().size() > 0) {
                        obj.setDesc(items.get(position).getDesc().get(0).toString());
                    }


                    if (items.get(position).getGallery_Images().size() > 0) {
                        obj.setImages(items.get(position).getGallery_Images().get(0).toString());
                    }

                    Data.add(obj);


                    if (Position_Object.getDesc() != null) {
                        if (Position_Object.getDesc().size() > 0) {
                            DIalogwith_Image obj_point = new DIalogwith_Image();
                            obj_point.dialog(ctx, Data, Position_Object.getTitle());
                        } else {
                            Toast.makeText(ctx, R.string.discnotavailable, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ctx, R.string.discnotavailable, Toast.LENGTH_SHORT).show();

                    }
                }
            });


            if (Position_Object.getCover_image() != null) {
                try {


                    JSONArray jsonArray = null;
                    jsonArray = new JSONArray(Position_Object.getCover_image());
                    if (jsonArray.length() > 0) {
                        if (!jsonArray.get(0).toString().equalsIgnoreCase("")) {

                            view.Gallery.setImageResource(R.drawable.video);
//                            view.Gallery.setText("Video");
                        } else {
                            view.Gallery.setImageResource(R.drawable.novideo);

//                            view.Gallery.setText(" No Video");
                        }


                    } else {
                        view.Gallery.setImageResource(R.drawable.novideo);

//                        view.Gallery.setText(" No Video");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                view.Gallery.setImageResource(R.drawable.novideo);
//                view.Gallery.setText(" No Video");
            }

            view.Gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONArray jsonArray = null;
                    try {


                        if (items.get(position).getCover_image() != null) {
                            jsonArray = new JSONArray(Position_Object.getCover_image());
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

    @Override
    public int getItemCount() {
        return items.size();
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
}
