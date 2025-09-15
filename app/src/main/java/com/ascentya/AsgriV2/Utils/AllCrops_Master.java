package com.ascentya.AsgriV2.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.HomeScreen_Activity;
import com.ascentya.AsgriV2.Adapters.AllCrops_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Dialog_Interface;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllCrops_Master extends DialogFragment {

    AllCrops_Adapter rvAdapter;

//    Dialog dialog;
    Dialog dialog;
    Context contx;
    List<Crops_Main> Data;
    Boolean forceclose;

    FragmentManager fragmentManager;

    public void dialog(Context context, FragmentManager fragmentManager, List<Crops_Main> data, String title, boolean close){
        this.fragmentManager = fragmentManager;
        dialog(context, data, title, close);
    }

    public void dialog(Context context, List<Crops_Main> Data, String title, final boolean forceclose) {
        dialog = new Dialog(context, R.style.DialogSlideAnim);
        this.contx = context;
        this.Data = Data;
        this.forceclose = forceclose;


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.allcrops_layout);

       dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (forceclose) {
                    ((HomeScreen_Activity) contx).finish();
                }
            }
        });

        setCancelable(true);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        dialog.show();
        dialog.getWindow().setAttributes(lp);
        final TextView title_head = (TextView) dialog.findViewById(R.id.title);
        title_head.setText(title);

        RecyclerView rvTest = (RecyclerView) dialog.findViewById(R.id.dialog_recycler);
        final ImageView close = (ImageView) dialog.findViewById(R.id.close);
        EditText search = (EditText) dialog.findViewById(R.id.search);
        rvTest.setHasFixedSize(true);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


        rvTest.setItemViewCacheSize(20);
        rvTest.setDrawingCacheEnabled(true);
        rvTest.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        rvTest.setLayoutManager(new LinearLayoutManager(context));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        rvAdapter = new AllCrops_Adapter(context, new Dialog_Interface() {
            @Override
            public void foo(String name) {
                dialog.dismiss();
            }
        }, Data);
        rvTest.setAdapter(rvAdapter);

        if (fragmentManager != null){
            show(fragmentManager, AllCrops_Master.class.getSimpleName());
        }else {
            dialog.show();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (forceclose)
            ((HomeScreen_Activity) contx).finish();
        super.onDismiss(dialog);
    }

    void filter(String text) {
        List<Crops_Main> temp = new ArrayList();
        for (Crops_Main d : Data) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        rvAdapter.updateList(temp);
    }

    public void dismiss(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return dialog;
    }
}
