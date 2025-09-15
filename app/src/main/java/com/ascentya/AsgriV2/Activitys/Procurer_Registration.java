package com.ascentya.AsgriV2.Activitys;

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
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Shared_Preference.Userobject;
import com.ascentya.AsgriV2.Token_session.Lang_Token;
import com.ascentya.AsgriV2.Utils.ImageUtils;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.textfield.TextInputEditText;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class Procurer_Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    LinearLayout firm_layout;
    Spinner industry_type;
    EditText name, gst_no, unique_no, email_phno, location;
    TextInputEditText password;

    Button register;
    CircleImageView pancard;
    ViewDialog viewDialog;
    Lang_Token tk;
    SessionManager sm;
    String pan_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurer__registration);
        industry_type = findViewById(R.id.industry_type);
        firm_layout = findViewById(R.id.firm_layout);
        register = findViewById(R.id.register);
        name = findViewById(R.id.name);
        gst_no = findViewById(R.id.gst_no);
        unique_no = findViewById(R.id.unique_no);
        email_phno = findViewById(R.id.email_phno);
        location = findViewById(R.id.location);
        password = findViewById(R.id.password);
        pancard = findViewById(R.id.pancard);
        sm = new SessionManager(this);
        tk = new Lang_Token(this);

        industry_type.setOnItemSelectedListener(this);
        viewDialog = new ViewDialog(Procurer_Registration.this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatesignForm()) {
                    Registers();

                }
            }
        });

        pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        pan_path = ImageUtils.setImage(Procurer_Registration.this, r, pancard, true);
                    }
                }).show(Procurer_Registration.this);
            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Firm");
        categories.add("Invidual");
        categories.add("Government");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        industry_type.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        if (item.equalsIgnoreCase("Firm")) {
            firm_layout.setVisibility(View.VISIBLE);
        } else {
            firm_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void Registers() {

        File pan_f = null;

        if (pan_path != null && pan_path.length() > 0) {

            pan_f = new File(pan_path);
        }


        viewDialog.showDialog();
        AndroidNetworking.upload(Webservice.register_f)


                .addMultipartParameter("email", email_phno.getText().toString())
                .addMultipartParameter("password", password.getText().toString())
                .addMultipartParameter("phone", email_phno.getText().toString())
                .addMultipartParameter("name", name.getText().toString())
                .addMultipartParameter("industry_type", industry_type.getSelectedItem().toString())
                .addMultipartParameter("gsd_no", gst_no.getText().toString())
                .addMultipartParameter("unique_no", unique_no.getText().toString())
                .addMultipartParameter("location", location.getText().toString())
                .addMultipartFile("pancard", pan_f)
                .addMultipartParameter("user_type", "procurer")
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

                        Intent i = new Intent(Procurer_Registration.this, Main_Dashboard.class);
                        i.putExtra("location", true);
                        startActivity(i);
                        finishAffinity();

                    } else {
                        Toast.makeText(Procurer_Registration.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {


                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


                viewDialog.hideDialog();
            }
        });
    }

    private boolean validatesignForm() {

        if (industry_type.getSelectedItem().toString().equalsIgnoreCase("Invidual")) {
            if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
                name.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidNumber(email_phno.getText().toString().trim())) {
                email_phno.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidPassword(password.getText().toString())) {
                password.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidPassword(location.getText().toString())) {
                location.setError(getString(R.string.required_date));
                return false;
            } else {
                return true;
            }
        } else {
            if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
                name.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidGST(gst_no.getText().toString().trim())) {
                gst_no.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidPassword(unique_no.getText().toString())) {
                unique_no.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidNumber(email_phno.getText().toString().trim())) {
                email_phno.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidPassword(password.getText().toString())) {
                password.setError(getString(R.string.required_date));
                return false;
            } else if (!ValidateInputs.isValidPassword(location.getText().toString())) {
                location.setError(getString(R.string.required_date));
                return false;
            } else {
                return true;
            }

        }
    }

}