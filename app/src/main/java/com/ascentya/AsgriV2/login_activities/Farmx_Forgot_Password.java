package com.ascentya.AsgriV2.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ascentya.AsgriV2.R;

import androidx.appcompat.app.AppCompatActivity;

public class Farmx_Forgot_Password extends AppCompatActivity {
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmx_forgot_password);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Farmx_Forgot_Password.this, Farmx_ResetPassword.class);
                startActivity(i);

            }
        });
    }
}