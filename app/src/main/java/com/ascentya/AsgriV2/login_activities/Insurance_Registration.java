package com.ascentya.AsgriV2.login_activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.ascentya.AsgriV2.Token_session.Lang_Token;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class Insurance_Registration extends AppCompatActivity {
    Spinner bank_name, branchspinner;
    EditText ifsc_code, email_phno;
    TextInputEditText password;
    Button register;
    String[] banks = {"Allahabad Bank", "Bank of India", "Central Bank of India", "Indian Bank", "Punjab National Bank", "Canara Bank", "Syndicate Bank", "State bank of India"};
    String[] bank_branch = {"arumbakkam branch", "vellore branch", "avadi branch", "CMBT branch", "guindy branch"};

    ViewDialog viewDialog;
    SessionManager sm;
    Lang_Token tk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_registration);
        bank_name = (Spinner) findViewById(R.id.bank_name);
        branchspinner = (Spinner) findViewById(R.id.branchspinner);
        ifsc_code = findViewById(R.id.ifsc_code);
        email_phno = findViewById(R.id.email_phno);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        sm = new SessionManager(this);
        tk = new Lang_Token(this);
        viewDialog = new ViewDialog(Insurance_Registration.this);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_row, banks);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter branch_adapter = new ArrayAdapter(this, R.layout.spinner_row, bank_branch);
        branch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bank_name.setAdapter(aa);
        branchspinner.setAdapter(branch_adapter);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatesignForm()) {
                    Registers();

                }
            }
        });


    }

    public void Registers() {
        viewDialog.showDialog();
        AndroidNetworking.post(Webservice.register_f)

                .addUrlEncodeFormBodyParameter("email", email_phno.getText().toString())
                .addUrlEncodeFormBodyParameter("password", password.getText().toString())
                .addUrlEncodeFormBodyParameter("phone", email_phno.getText().toString())
                .addUrlEncodeFormBodyParameter("bank_name", bank_name.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("branch", branchspinner.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("ifsc_code", ifsc_code.getText().toString())
                .addUrlEncodeFormBodyParameter("user_type", "insurance")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

//
                        tk.setusename("0");
                        Userobject obj = new Userobject();
                        obj.setId(jsonObject.optString("data"));
                        obj.setFirstname(email_phno.getText().toString());
                        obj.setPhno(email_phno.getText().toString());
                        obj.setEmail(email_phno.getText().toString());
                        obj.setIspremium("0");
                        sm.setUser(obj);

                        Intent i = new Intent(Insurance_Registration.this, Main_Dashboard.class);
                        i.putExtra("location", true);
                        startActivity(i);
                        finishAffinity();

                    } else {
                        Toast.makeText(Insurance_Registration.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toasty.normal(Insurance_Registration.this, "Something went wrong! ").show();

                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                Toasty.normal(Insurance_Registration.this, "Something went wrong! " + anError.getErrorCode()).show();
                viewDialog.hideDialog();
            }
        });
    }

    private boolean validatesignForm() {
        if (!ValidateInputs.isValidIFSC(ifsc_code.getText().toString().trim())) {
            ifsc_code.setError(String.format(getString(R.string.enter_valid_), getString(R.string.ifsc_code)));
            return false;
        } else if (!ValidateInputs.isValidNumber(email_phno.getText().toString().trim())) {
            email_phno.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidPassword(password.getText().toString())) {
            password.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }
}