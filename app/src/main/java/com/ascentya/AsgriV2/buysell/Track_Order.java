package com.ascentya.AsgriV2.buysell;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;

import androidx.appcompat.app.AppCompatActivity;

public class Track_Order extends AppCompatActivity {
    ProgressBar pb, pb1, pb2;
    TextView back, confirmed, confirmed_icon, packed, packed_icon, shipped, shipped_icon, delivery, delivery_icon;
    ImageView confirmed_img, packed_img, shipped_img, delivery_img;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        status = getIntent().getStringExtra("status");
        pb = (ProgressBar) findViewById(R.id.pb);
        pb1 = (ProgressBar) findViewById(R.id.pb1);
        pb2 = (ProgressBar) findViewById(R.id.pb2);
        confirmed = (TextView) findViewById(R.id.confirmed);
        confirmed_icon = (TextView) findViewById(R.id.confirmed_icon);
        packed = (TextView) findViewById(R.id.packed);
        packed_icon = (TextView) findViewById(R.id.packed_icon);
        shipped = (TextView) findViewById(R.id.shipped);
        shipped_icon = (TextView) findViewById(R.id.shipped_icon);
        delivery = (TextView) findViewById(R.id.delivery);
        delivery_icon = (TextView) findViewById(R.id.delivery_icon);
        confirmed_img = (ImageView) findViewById(R.id.confirm_img);
        packed_img = (ImageView) findViewById(R.id.packed_img);
        shipped_img = (ImageView) findViewById(R.id.shipped_img);
        delivery_img = (ImageView) findViewById(R.id.delivery_img);

//        fm.setBackIcon(back);
        confirmed_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
        confirmed.setTextColor(getResources().getColor(R.color.green_farmx));
        confirmed_img.setColorFilter(getResources().getColor(R.color.green_farmx));

//            new History_Service().execute();


        if (status.equalsIgnoreCase("PROCESSING")) {

            confirmed_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            confirmed.setTextColor(getResources().getColor(R.color.green_farmx));
            confirmed_img.setColorFilter(getResources().getColor(R.color.green_farmx));

        } else if (status.equalsIgnoreCase("ORDER CONFIRMED")) {

            confirmed_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            confirmed.setTextColor(getResources().getColor(R.color.green_farmx));
            confirmed_img.setColorFilter(getResources().getColor(R.color.green_farmx));
            packed_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            packed_icon.setTextColor(getResources().getColor(R.color.green_farmx));
            packed_img.setColorFilter(getResources().getColor(R.color.green_farmx));
            pb.setProgress(100);

        } else if (status.equalsIgnoreCase("DISPATCHED")) {

            confirmed_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            confirmed.setTextColor(getResources().getColor(R.color.green_farmx));
            confirmed_img.setColorFilter(getResources().getColor(R.color.green_farmx));
            packed_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            packed.setTextColor(getResources().getColor(R.color.green_farmx));
            packed_img.setColorFilter(getResources().getColor(R.color.green_farmx));
            shipped_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            shipped.setTextColor(getResources().getColor(R.color.green_farmx));
            shipped_img.setColorFilter(getResources().getColor(R.color.green_farmx));
            pb.setProgress(100);
            pb1.setProgress(100);


        } else if (status.equalsIgnoreCase("DELIVERED")) {

            confirmed_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            confirmed.setTextColor(getResources().getColor(R.color.green_farmx));
            confirmed_img.setColorFilter(getResources().getColor(R.color.green_farmx));
            packed_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            packed.setTextColor(getResources().getColor(R.color.green_farmx));
            packed_img.setColorFilter(getResources().getColor(R.color.green_farmx));
            shipped_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            shipped.setTextColor(getResources().getColor(R.color.green_farmx));
            shipped_img.setColorFilter(getResources().getColor(R.color.green_farmx));


            delivery_icon.setBackground(getResources().getDrawable(R.drawable.edit_icon_bg));
            delivery.setTextColor(getResources().getColor(R.color.green_farmx));
            delivery_img.setColorFilter(getResources().getColor(R.color.green_farmx));
            pb.setProgress(100);
            pb1.setProgress(100);
            pb2.setProgress(100);
        }


    }
}