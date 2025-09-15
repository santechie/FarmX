package com.ascentya.AsgriV2.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.chaos.view.PinView;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class Random_Verification extends AppCompatActivity {
    Button submit;
    SessionManager sm;
    String user_verifycode;
    PinView pinview;

    @Override
    protected void onResume() {
        super.onResume();
        getverify();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random__verification);
        sm = new SessionManager(this);

        submit = findViewById(R.id.submit);
        pinview = findViewById(R.id.pinview);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();

            }
        });
    }

    public void reset() {


        AndroidNetworking.post(Webservice.getverifiy)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("user_code", user_verifycode)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        Intent i = new Intent(Random_Verification.this, Main_Dashboard.class);
                        startActivity(i);

                    } else {
                        Toasty.success(Random_Verification.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }

    public void getverify() {


        AndroidNetworking.post(Webservice.getverification_code)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if (jsonObject.optString("code_status").equalsIgnoreCase("true")) {


                        pinview.setText(jsonObject.optString("verification_code"));
                        user_verifycode = pinview.getText().toString();

                    } else {
                        Toasty.success(Random_Verification.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }
}