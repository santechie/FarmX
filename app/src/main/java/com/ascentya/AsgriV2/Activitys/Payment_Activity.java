package com.ascentya.AsgriV2.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Shared_Preference.Userobject;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class Payment_Activity extends AppCompatActivity {
    ElasticButton paid;
    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        sm = new SessionManager(this);
        paid = findViewById(R.id.paid);
        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getmembers();


            }
        });
    }

    public void getmembers() {

        AndroidNetworking.get(Webservice.update_payment + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {


                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        Userobject obj = new Userobject();
                        obj.setId(jsonObject.getJSONObject("data").optString("id"));
                        obj.setFirstname(jsonObject.getJSONObject("data").optString("username"));
                        obj.setPhno(jsonObject.getJSONObject("data").optString("phone"));
                        obj.setEmail(jsonObject.getJSONObject("data").optString("email"));
                        obj.setIspremium(jsonObject.getJSONObject("data").optString("is_premium"));
                        obj.setSearch_name("none");
                        sm.setUser(obj);
                        Intent i = new Intent(Payment_Activity.this, Mycrops_Main.class);
                        i.putExtra("page", "crop");
                        startActivity(i);
                        finishAffinity();

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