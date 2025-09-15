package com.ascentya.AsgriV2.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class Institute_Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button register;
    Spinner bank_name;
    LinearLayout degree_layout, designation_layout, research_layout;
    EditText name, college_name, degree_program, designation, research_topic, email_phno;
    TextInputEditText password;
    ViewDialog viewDialog;
    SessionManager sm;
    Lang_Token tk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_registration);
        register = findViewById(R.id.register);
        bank_name = findViewById(R.id.bank_name);
        sm = new SessionManager(this);
        tk = new Lang_Token(this);
        degree_layout = findViewById(R.id.degree_layout);
        designation_layout = findViewById(R.id.designation_layout);
        research_layout = findViewById(R.id.research_layout);
        name = findViewById(R.id.name);
        college_name = findViewById(R.id.college_name);
        degree_program = findViewById(R.id.degree_program);
        designation = findViewById(R.id.designation);
        research_topic = findViewById(R.id.research_topic);
        email_phno = findViewById(R.id.email_phno);
        password = findViewById(R.id.password);
        List<String> categories = new ArrayList<String>();
        categories.add("Student");
        categories.add("Faculty");
        categories.add("Research");
        viewDialog = new ViewDialog(Institute_Registration.this);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        bank_name.setAdapter(dataAdapter);
        bank_name.setOnItemSelectedListener(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatesignForm()) {
                    Registers();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        if (item.equalsIgnoreCase("Student")) {
            designation_layout.setVisibility(View.GONE);
            research_layout.setVisibility(View.GONE);
            degree_layout.setVisibility(View.VISIBLE);
        } else if (item.equalsIgnoreCase("Faculty")) {
            designation_layout.setVisibility(View.VISIBLE);
            research_layout.setVisibility(View.VISIBLE);
            degree_layout.setVisibility(View.GONE);
        } else {
            designation_layout.setVisibility(View.VISIBLE);
            research_layout.setVisibility(View.GONE);
            degree_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private boolean validatesignForm() {

        if (bank_name.getSelectedItem().toString().equalsIgnoreCase("Student")) {
            if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
                name.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidInput(college_name.getText().toString())) {
                college_name.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidInput(degree_program.getText().toString())) {
                degree_program.setError(getString(R.string.required_date));
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
        } else if (bank_name.getSelectedItem().toString().equalsIgnoreCase("Faculty")) {
            if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
                name.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidInput(college_name.getText().toString())) {
                college_name.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidInput(designation.getText().toString())) {
                designation.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidInput(research_topic.getText().toString())) {
                research_topic.setError(getString(R.string.required_date));
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
        } else {
            if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
                name.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidInput(college_name.getText().toString())) {
                college_name.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidInput(designation.getText().toString())) {
                designation.setError(getString(R.string.required_date));
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

    public void Registers() {
        viewDialog.showDialog();
        AndroidNetworking.post(Webservice.register_f)

                .addUrlEncodeFormBodyParameter("email", email_phno.getText().toString())
                .addUrlEncodeFormBodyParameter("password", password.getText().toString())
                .addUrlEncodeFormBodyParameter("phone", email_phno.getText().toString())
                .addUrlEncodeFormBodyParameter("name", name.getText().toString())
                .addUrlEncodeFormBodyParameter("type", bank_name.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("college_name", college_name.getText().toString())
                .addUrlEncodeFormBodyParameter("degree_name", degree_program.getText().toString())
                .addUrlEncodeFormBodyParameter("designation", designation.getText().toString())
                .addUrlEncodeFormBodyParameter("research_topic", research_topic.getText().toString())
                .addUrlEncodeFormBodyParameter("user_type", "institution")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                        tk.setusename("0");
                        Userobject obj = new Userobject();
                        obj.setId(jsonObject.optString("data"));
                        obj.setFirstname(name.getText().toString());
                        obj.setPhno(email_phno.getText().toString());
                        obj.setEmail(email_phno.getText().toString());
                        obj.setIspremium("0");
                        sm.setUser(obj);

                        Intent i = new Intent(Institute_Registration.this, Main_Dashboard.class);
                        i.putExtra("location", true);
                        startActivity(i);
                        finishAffinity();

                    } else {
                        Toast.makeText(Institute_Registration.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toasty.normal(Institute_Registration.this, "Something went wrong!").show();

                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                Toasty.normal(Institute_Registration.this, "Something went wrong! " + anError.getErrorCode()).show();
                viewDialog.hideDialog();
            }
        });
    }
}