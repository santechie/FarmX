package com.ascentya.AsgriV2.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class Expired_Page extends AppCompatActivity {
    Button contact;
    SessionManager sm;
    TextView refresh;
    String User_Validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired_page);
        contact = findViewById(R.id.contact);
        refresh = findViewById(R.id.refresh);
        sm = new SessionManager(this);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getverify();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Expired_Page.this, "Admin will be contact you soon", Toast.LENGTH_SHORT).show();

                reset();
            }
        });
    }

    public void reset() {

        AndroidNetworking.post(Webservice.getrandomgen)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("data").equalsIgnoreCase("true")) {

                        refresh.setVisibility(View.VISIBLE);

//                        Intent i = new Intent(Expired_Page.this, Random_Verification.class);
//                        startActivity(i);


                    } else {
                        Toasty.success(Expired_Page.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
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


                        if (!jsonObject.optString("verification_code").equalsIgnoreCase("")) {
//                          pinview.setText(jsonObject.optString("verification_code"));


                            Intent i = new Intent(Expired_Page.this, Random_Verification.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(Expired_Page.this, "Kindly wait for some time", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toasty.success(Expired_Page.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
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