package com.ascentya.AsgriV2.buysell;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class My_Address extends AppCompatActivity {
    Button proceed;
    EditText flat_no, landmark, city, pincode;

    ThemedToggleButtonGroup tags;
    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        proceed = findViewById(R.id.proceed);
        flat_no = findViewById(R.id.flat_no);
        landmark = findViewById(R.id.landmark);
        pincode = findViewById(R.id.pincode);
        city = findViewById(R.id.city);
        tags = findViewById(R.id.tags);
        sm = new SessionManager(this);


        proceed.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

                if (validatesignForm()) {
                    add_tocart();

                }

            }
        });
    }

    public void add_tocart() {


        AndroidNetworking.post(Webservice.add_address)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("add_name", flat_no.getText().toString())
                .addUrlEncodeFormBodyParameter("add_street", landmark.getText().toString())
                .addUrlEncodeFormBodyParameter("add_city", city.getText().toString())
                .addUrlEncodeFormBodyParameter("add_pincode", pincode.getText().toString())
                .addUrlEncodeFormBodyParameter("flat_num", flat_no.getText().toString())
                .addUrlEncodeFormBodyParameter("markus", tags.getSelectedButtons().get(0).getText())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(My_Address.this, jsonObject)){
                    return;
                }

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        onBackPressed();
                    } else {

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

    private boolean validatesignForm() {
        if (!ValidateInputs.isValidInput(flat_no.getText().toString().trim())) {
            flat_no.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(landmark.getText().toString())) {
            landmark.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(city.getText().toString().trim())) {
            city.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(pincode.getText().toString().trim())) {
            pincode.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }

}