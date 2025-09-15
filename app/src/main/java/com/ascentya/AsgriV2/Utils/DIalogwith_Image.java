package com.ascentya.AsgriV2.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.CommenWithImage_Adapter;
import com.ascentya.AsgriV2.Models.PointerImages_Model;
import com.ascentya.AsgriV2.R;
import com.zagori.mediaviewer.ModalViewer;
import com.zagori.mediaviewer.views.OverlayView;

import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DIalogwith_Image {

    public void dialog(Context context, List<PointerImages_Model> Data, String title) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_layout);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        TextView title_head = (TextView) dialog.findViewById(R.id.title);
        title_head.setText(title);

        RecyclerView rvTest = (RecyclerView) dialog.findViewById(R.id.dialog_recycler);
        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        rvTest.setHasFixedSize(true);
        rvTest.setLayoutManager(new LinearLayoutManager(context));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        CommenWithImage_Adapter rvAdapter = new CommenWithImage_Adapter(context, Data);
        rvTest.setAdapter(rvAdapter);
    }

    public static void showImageViewer(Context context, String title, String imageUrl){
        View overlayView = new OverlayView(context, R.layout.overlay_view);
        View close = overlayView.findViewById(R.id.close_dialog);


        final ModalViewer modelView = ModalViewer
                .load(context, Arrays.asList(imageUrl))
                .hideStatusBar(true)
                .allowZooming(true)
                .allowSwipeToDismiss(true)
                .addOverlay(overlayView);
                /*.setImageChangeListener(new OnImageChangeListener() {
                    @Override
                    public void onImageChange(final int position) {


                    }
                });*/

        modelView.start();


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelView.onDismiss();
            }
        });
    }

    public static void showImageViewer(Context context, String title, List<String> images){
        View overlayView = new OverlayView(context, R.layout.overlay_view);
        View close = overlayView.findViewById(R.id.close_dialog);


        final ModalViewer modelView = ModalViewer
                .load(context, images)
                .hideStatusBar(true)
                .allowZooming(true)
                .allowSwipeToDismiss(true)
                .addOverlay(overlayView);
                /*.setImageChangeListener(new OnImageChangeListener() {
                    @Override
                    public void onImageChange(final int position) {


                    }
                });*/

        modelView.start();


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelView.onDismiss();
            }
        });
    }
}
