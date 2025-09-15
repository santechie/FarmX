package com.ascentya.AsgriV2.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Main_Dashboard;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Shared_Preference.Userobject;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class Bank_Registration extends AppCompatActivity {
    String[] banks = {"Allahabad Bank", "Bank of India", "Central Bank of India", "Indian Bank", "Punjab National Bank", "Canara Bank", "Syndicate Bank", "State bank of India"};
    String[] bank_branch = {"Arumbakkam Branch", "Vellore Branch", "Avadi Branch", "CMBT branch", "Guindy Branch"};
    Button register;
    Spinner bank_name, branch;
    ViewDialog viewDialog;
    EditText ifsc_code, email_phno, name, designation;
    TextInputEditText password;
    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(this.getClass().getSimpleName(), "onCreate");

        setContentView(R.layout.activity_bank_registration);
        bank_name = (Spinner) findViewById(R.id.bank_name);
        branch = (Spinner) findViewById(R.id.branch);
        ifsc_code = findViewById(R.id.ifsc_code);
        email_phno = findViewById(R.id.email_phno);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        sm = new SessionManager(this);
        designation = findViewById(R.id.designation);
        viewDialog = new ViewDialog(Bank_Registration.this);

        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_row, banks);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter branch_adapter = new ArrayAdapter(this, R.layout.spinner_row, bank_branch);
        branch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bank_name.setAdapter(aa);
        branch.setAdapter(branch_adapter);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSignForm()) {
                    Registers();

                }

            }
        });

    }

    public void Registers() {
        viewDialog.showDialog();
        AndroidNetworking.post(Webservice.register_f)
                .addUrlEncodeFormBodyParameter("username", name.getText().toString())
//                .addUrlEncodeFormBodyParameter("email", email_phno.getText().toString())
                .addUrlEncodeFormBodyParameter("password", password.getText().toString())
                .addUrlEncodeFormBodyParameter("phone", email_phno.getText().toString())
                .addUrlEncodeFormBodyParameter("name", name.getText().toString())
                .addUrlEncodeFormBodyParameter("bank_name", bank_name.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("branch", branch.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("ifsc_code", ifsc_code.getText().toString())
                .addUrlEncodeFormBodyParameter("designation", designation.getText().toString())
                .addUrlEncodeFormBodyParameter("user_type", "bank")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                System.out.println(this.getClass().getSimpleName() + ": " + jsonObject.toString());

                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
//
                        Userobject obj = new Userobject();
                        obj.setId(jsonObject.optString("data"));
                        obj.setFirstname(name.getText().toString());
                        obj.setPhno(email_phno.getText().toString());
//                        obj.setEmail(email_phno.getText().toString());
                        obj.setIspremium("0");
                        sm.setUser(obj);

                        Intent i = new Intent(Bank_Registration.this, Main_Dashboard.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("location", true);
                        startActivity(i);
//                        finishAffinity();
                    } else {
                        Toast.makeText(Bank_Registration.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    Toasty.normal(Bank_Registration.this, "Something went wrong!").show();
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                Toasty.normal(Bank_Registration.this, "Something went wrong! ").show();
                try {
                    System.out.println(GsonUtils.getGson().toJson(anError));
                }catch (Exception e){ e.printStackTrace(); }
            }
        });
    }

    private boolean validateSignForm() {

        ifsc_code.setError(null);
        email_phno.setError(null);
        password.setError(null);
        name.setError(null);
        designation.setError(null);

        ifsc_code.setText(ifsc_code.getText().toString().trim().toUpperCase(Locale.ROOT));
        if (!ValidateInputs.isValidIFSC(ifsc_code.getText().toString().trim())) {
            ifsc_code.findFocus();
            ifsc_code.setError(String.format(getString(R.string.enter_valid_), getString(R.string.ifsc_code)));
            return false;
        } else if (!ValidateInputs.isValidNumber(email_phno.getText().toString().trim())) {
            email_phno.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidPassword(password.getText().toString().trim())) {
            password.setError(getString(R.string.required));
            return false;
        } else if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
            name.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(designation.getText().toString().trim())) {
            designation.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }
}