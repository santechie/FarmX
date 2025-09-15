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
import es.dmoral.toasty.Toasty;

public class Warehouse_Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    LinearLayout firm_layout;
    Spinner industry_type;
    EditText name, gst_no, unique_no, email_phno, location;
    TextInputEditText password;
    CircleImageView pancard;
    Button register;
    Lang_Token tk;
    SessionManager sm;
    String pan_path;
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_registration);
        industry_type = findViewById(R.id.industry_type);
        firm_layout = findViewById(R.id.firm_layout);
        name = findViewById(R.id.name);
        tk = new Lang_Token(this);
        sm = new SessionManager(this);
        gst_no = findViewById(R.id.gst_no);
        unique_no = findViewById(R.id.unique_no);
        email_phno = findViewById(R.id.email_phno);
        password = findViewById(R.id.password);
        location = findViewById(R.id.location);
        register = findViewById(R.id.register);
        pancard = findViewById(R.id.pancard);
        viewDialog = new ViewDialog(Warehouse_Registration.this);

        pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        pan_path = ImageUtils.
                                setImage(Warehouse_Registration.this, r, pancard, true);
                    }
                }).show(Warehouse_Registration.this);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatesignForm()) {
                    Registers();

                }
            }
        });
        industry_type.setOnItemSelectedListener(this);

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

//        if (item.equalsIgnoreCase("Firm")) {
//            firm_layout.setVisibility(View.VISIBLE);
//        } else {
//            firm_layout.setVisibility(View.GONE);
//        }
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
                .addMultipartParameter("type", industry_type.getSelectedItem().toString())
                .addMultipartParameter("gst_no", gst_no.getText().toString())
                .addMultipartParameter("unique_no", unique_no.getText().toString())
                .addMultipartParameter("location", location.getText().toString())
                .addMultipartFile("pancard", pan_f)
                .addMultipartParameter("user_type", "warehouse")
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

                        Intent i = new Intent(Warehouse_Registration.this, Main_Dashboard.class);
                        i.putExtra("location", true);
                        startActivity(i);
                        finishAffinity();

                    } else {
                        Toast.makeText(Warehouse_Registration.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toasty.normal(Warehouse_Registration.this, "Something went wrong! ").show();

                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                Toasty.normal(Warehouse_Registration.this, "Something went wrong! " + anError.getErrorCode()).show();

                viewDialog.hideDialog();
            }
        });
    }

    private boolean validatesignForm() {


        if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
            name.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidGST(gst_no.getText().toString().trim())) {
            gst_no.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(unique_no.getText().toString().trim())) {
            unique_no.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(email_phno.getText().toString().trim())) {
            email_phno.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidPassword(password.getText().toString())) {
            password.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(location.getText().toString())) {
            location.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }


    }

}