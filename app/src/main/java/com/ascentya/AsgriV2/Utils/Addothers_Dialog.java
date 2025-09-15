package com.ascentya.AsgriV2.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Interfaces_Class.add_others;
import com.ascentya.AsgriV2.Models.others_model;
import com.ascentya.AsgriV2.R;
import com.skydoves.elasticviews.ElasticButton;


public class Addothers_Dialog {

    public void dialog(final Context context, String title, final add_others data) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.activityaddothers_layout);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        TextView title_head = (TextView) dialog.findViewById(R.id.title);
        title_head.setText(title);

        final EditText member = (EditText) dialog.findViewById(R.id.member);
        final EditText payment = (EditText) dialog.findViewById(R.id.hourscost);
        ElasticButton add = (ElasticButton) dialog.findViewById(R.id.add);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (member.getText().length() > 0) {
                    if (payment.getText().length() > 0) {
                        others_model obj = new others_model();


                        obj.setTitle(member.getText().toString());
                        obj.setAmount(payment.getText().toString());

                        data.crop_suggest(obj);

                        dialog.cancel();
                    } else {
                        payment.setError(context.getString(R.string.required_date));
                    }
                } else {
                    member.setError(context.getString(R.string.required_date));
                }

            }
        });


        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


    }
}
