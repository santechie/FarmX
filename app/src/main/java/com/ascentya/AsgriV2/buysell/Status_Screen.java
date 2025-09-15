package com.ascentya.AsgriV2.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ascentya.AsgriV2.Activitys.Main_Dashboard;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.CartItemUtils;

import androidx.appcompat.app.AppCompatActivity;

public class Status_Screen extends AppCompatActivity {
    Button goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_screen);
        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Status_Screen.this, Main_Dashboard.class);
                startActivity(i);
                finishAffinity();
            }
        });
        clearCart();
    }

    private void clearCart(){
        CartItemUtils.deleteAllCartItem(this, object -> {});
    }
}