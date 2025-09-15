package com.ascentya.AsgriV2.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Models.NutrientDef_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.Dialog_Master;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NutrientDeficiency_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NutrientDef_Model> items = new ArrayList<>();
    private Context ctx;


    public NutrientDeficiency_Adapter(Context context, List<NutrientDef_Model> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, symbol;
        public FloatingActionButton symptoms, corrections, Gallery;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            symbol = (TextView) v.findViewById(R.id.symbol);
            symptoms = (FloatingActionButton) v.findViewById(R.id.symptoms);
            corrections = (FloatingActionButton) v.findViewById(R.id.corrections);
            Gallery = (FloatingActionButton) v.findViewById(R.id.Gallery);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrientdeficiency_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final NutrientDef_Model Position_Object = items.get(position);
            view.title.setText(Position_Object.getName());
            view.symbol.setText(Position_Object.getFirstleter());

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(Position_Object.getNd_video());
                if (jsonArray.length() > 0) {
                    if (!jsonArray.get(0).toString().equalsIgnoreCase("")) {

                        view.Gallery.setImageResource(R.drawable.video);
//                        view.Gallery.setText("Video");
                    } else {
                        view.Gallery.setImageResource(R.drawable.novideo);
//                        view.Gallery.setText(" No Video");
                    }

                } else {
                    view.Gallery.setImageResource(R.drawable.novideo);
//                    view.Gallery.setText(" No Video");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            view.Gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        JSONArray jsonArray = new JSONArray(Position_Object.getNd_video());

                        if (jsonArray.length() > 0) {
                            if (!jsonArray.get(0).toString().equalsIgnoreCase("")) {

                                watchYoutubeVideo(jsonArray.get(0).toString().trim());
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


            view.symptoms.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    if (Position_Object.getSymptoms().size() > 0) {
                        DIalogwith_Image obj = new DIalogwith_Image();
                        obj.dialog(ctx, Position_Object.getSymptoms(), Position_Object.getName());
                    } else {
                        Toast.makeText(ctx, R.string.discnotavailable, Toast.LENGTH_SHORT).show();
                    }
                }
            });


            view.corrections.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {

                    if (Position_Object.getCorrective_measure().size() > 0) {
                        Dialog_Master obj = new Dialog_Master();
                        obj.dialog(ctx, Position_Object.getCorrective_measure(), Position_Object.getName());
                    } else {
                        Toast.makeText(ctx, R.string.discnotavailable, Toast.LENGTH_SHORT).show();
                    }
                }
            });


//            view.Gallery.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Dialog dialog = new PopopDialogBuilder(ctx)
//                            // Set list like as option1 or option2 or option3
//                            .setList(items.get(position).getImages())
//                            // Set dialog header color
//                            .setHeaderBackgroundColor(android.R.color.transparent)
//                            // Set dialog background color
//                            .setDialogBackgroundColor(R.color.color_dialog_bg)
//                            // Set close icon drawable
//                            .setCloseDrawable(R.drawable.ic_close_white_24dp)
//                            // Set loading view for pager image and preview image
//                            .setLoadingView(R.layout.loading_view)
//                            // Set dialog style
//                            .setDialogStyle(R.style.DialogStyle)
//                            // Choose selector type, indicator or thumbnail
//                            .showThumbSlider(true)
//                            // Set image scale type for slider image
//                            .setSliderImageScaleType(ImageView.ScaleType.FIT_CENTER)
//                            // Set indicator drawable
//                            .setSelectorIndicator(R.drawable.sample_indicator_selector)
//                            // Build Km Slider Popup Dialog
//                            .build();
//                    dialog.show();
//                }
//            });


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
