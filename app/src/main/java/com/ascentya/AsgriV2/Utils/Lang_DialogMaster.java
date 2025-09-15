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

import com.ascentya.AsgriV2.Adapters.Lang_Adapter;
import com.ascentya.AsgriV2.Models.Lang_Model;
import com.ascentya.AsgriV2.R;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Lang_DialogMaster {

    public void dialog(Context context, List<Lang_Model> Data, String title) {
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


        Lang_Adapter rvAdapter = new Lang_Adapter(context, Data, dialog);
        rvTest.setAdapter(rvAdapter);
    }
}
