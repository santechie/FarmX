package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.PointerImages_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.bumptech.glide.Glide;
import com.zagori.mediaviewer.ModalViewer;
import com.zagori.mediaviewer.interfaces.OnImageChangeListener;
import com.zagori.mediaviewer.views.OverlayView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CommenWithImage_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PointerImages_Model> items = new ArrayList<>();
    private Context ctx;
    private OverlayView overlayView;
    private ImageView close;


    public CommenWithImage_Adapter(Context context, List<PointerImages_Model> items) {
        this.items = items;
        this.ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView c_name;
        ImageView icon, gallery;


        public ViewHolder(View v) {
            super(v);
            c_name = (TextView) v.findViewById(R.id.disc);
            icon = (ImageView) v.findViewById(R.id.icon);
            gallery = (ImageView) v.findViewById(R.id.gallery);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagewithpointer_layout, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;

            final PointerImages_Model PositionObject = items.get(position);

            view.c_name.setText(items.get(position).getDesc());


            Glide.with(ctx).load(Webservice.Searchicon).into(view.icon);


//            if (PositionObject.getImages() != null && PositionObject.getImages().length()>0){
            try {

                System.out.println("Symptoms: " + GsonUtils.getGson().toJson(PositionObject.getImages()));

                if (PositionObject.getImages() != null) {
                    JSONArray jsonArray = new JSONArray(PositionObject.getImages());
                    if (jsonArray.length() > 0) {
                        if (jsonArray.get(0).toString().equalsIgnoreCase("")) {

                            ((ViewHolder) holder).gallery.setVisibility(View.GONE);
                        } else {

                            ((ViewHolder) holder).gallery.setVisibility(View.VISIBLE);
                        }

                    } else {
                        ((ViewHolder) holder).gallery.setVisibility(View.GONE);
                    }
                } else {
                    ((ViewHolder) holder).gallery.setVisibility(View.GONE);
                }

//
//
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ((ViewHolder) holder).gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<String> Data = null;
                    JSONArray jsonArray = null;

                    if (PositionObject.getImages() != null) {
                        try {
                            jsonArray = new JSONArray(PositionObject.getImages());
                            Data = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String imageUrl = jsonArray.getString(i).replaceAll("(\\r\\n|\\n|\\r)", "");
                                if (imageUrl.endsWith(".jp"))
                                    imageUrl = imageUrl.replaceAll(".jp", ".jpg");
                                Data.add(imageUrl);
                                System.out.println("Image: "+imageUrl.replace("http", ""));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (jsonArray.length() > 0) {
                            overlayView = new OverlayView(ctx, R.layout.overlay_view);
                            close = overlayView.findViewById(R.id.close_dialog);


                            final ModalViewer modalview = ModalViewer
                                    .load(ctx, Data)
                                    .hideStatusBar(true)
                                    .allowZooming(true)
                                    .allowSwipeToDismiss(true)
                                    .addOverlay(overlayView)
                                    .setImageChangeListener(new OnImageChangeListener() {
                                        @Override
                                        public void onImageChange(final int position) {


                                        }
                                    });

                            modalview.start();


                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modalview.onDismiss();
                                }
                            });


//                            OImageSlider oImageSlider = new OImageSlider();
//                            oImageSlider.setColorActive(Color.WHITE) //bottom indicator active color
//                                    .setColorInactive(Color.GRAY) // bottom indicator inactive color
//
//                                    .setImageUrls(Data) // arraylist of image urls
//                                    .setCurrentImage(0) // the selected image ( starting from 0 )
//                                    .setBackgroundColor(Color.BLACK) // background color of activity
//                                    .start(ctx);

//                            Dialog dialog = new PopopDialogBuilder(ctx)
//                                    .setDialogStyle(R.style.DialogSlidefromright)
//
//                                    .setList(Data)
//
//                                    .setHeaderBackgroundColor(android.R.color.white)
//
//                                    .setDialogBackgroundColor(android.R.color.white)
//
//                                    .setCloseDrawable(R.drawable.ic_close_black_24dp)
//
//                                    .showThumbSlider(true)
//
//                                    // Set image scale type for slider image
//
//                                    // Set indicator drawable
//                                    .setSelectorIndicator(R.drawable.bg_select)
//                                    // Build Km Slider Popup Dialog
//                                    .build();
//                            DisplayMetrics metrics = new DisplayMetrics();
//                            WindowManager windowManager = (WindowManager) ctx
//                                    .getSystemService(Context.WINDOW_SERVICE);
//                            windowManager.getDefaultDisplay().getMetrics(metrics);
//                            int widthLcl = (int) (metrics.widthPixels * 0.9f);
//                            int heightLcl = (int) (metrics.heightPixels * 0.8f);
//                            dialog.getWindow().setLayout(widthLcl, heightLcl);
//                            dialog.setCanceledOnTouchOutside(true);
//
//                            final int sdk = android.os.Build.VERSION.SDK_INT;
//                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                                dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(ctx, R.drawable.dialog_bg) );
//                            } else {
//                                dialog.getWindow().setBackgroundDrawableResource( R.drawable.dialog_bg );
//                            }
//                            dialog.show();
                        }
                    }


                }
            });

//
//
//            } else {
//                ((ViewHolder) holder).gallery.setVisibility(View.GONE);
//            }

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
