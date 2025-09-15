package com.ascentya.AsgriV2.farmx.postharvest_diseas;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.ascentya.AsgriV2.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import androidx.appcompat.app.AppCompatActivity;

public class SeedQuality_activity extends AppCompatActivity {
    ImageButton pastePin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_quality_activity);
        pastePin = findViewById(R.id.pastePin);
        pastePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {


//
                    }
                }).show(SeedQuality_activity.this);
            }
        });
    }
}