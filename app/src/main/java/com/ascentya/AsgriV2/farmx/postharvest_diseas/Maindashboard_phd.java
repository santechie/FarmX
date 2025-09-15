package com.ascentya.AsgriV2.farmx.postharvest_diseas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;

import androidx.appcompat.app.AppCompatActivity;

public class Maindashboard_phd extends AppCompatActivity {
    LinearLayout pestdisease_layout, seedquality_layout;
    TextView maincrop, intercrop, crop_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindashboard_phd);
        pestdisease_layout = findViewById(R.id.pestdisease_layout);
        maincrop = findViewById(R.id.maincrop);
        intercrop = findViewById(R.id.intercrop);
        crop_type = findViewById(R.id.crop_type);


        intercrop.setTextColor(getResources().getColor(R.color.black));
        maincrop.setTextColor(getResources().getColor(R.color.green_farmx));
        crop_type.setText(getString(R.string.main_crops));
        maincrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intercrop.setTextColor(getResources().getColor(R.color.black));
                maincrop.setTextColor(getResources().getColor(R.color.green_farmx));
                crop_type.setText(getString(R.string.main_crops));
            }
        });

        intercrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intercrop.setTextColor(getResources().getColor(R.color.green_farmx));
                maincrop.setTextColor(getResources().getColor(R.color.black));
                crop_type.setText(getString(R.string.inter_crops));
            }
        });
        seedquality_layout = findViewById(R.id.seedquality_layout);

        seedquality_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Maindashboard_phd.this, SeedQuality_activity.class);
                startActivity(i);
            }
        });
        pestdisease_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Maindashboard_phd.this, PestsDisease_Activity.class);
                startActivity(i);
            }
        });

    }
}