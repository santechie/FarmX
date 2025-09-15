package com.ascentya.AsgriV2.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Equipment_Register extends AppCompatActivity {
    LinearLayout firm_layout;
    EditText industry_type;
    EditText name, gst_no, unique_no, email_phno, location, tin_no;
    TextInputEditText password;
    CircleImageView pancard;
    Button register;
    ViewDialog viewDialog;
    SessionManager sm;
    Lang_Token tk;
    String pan_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_register);
        industry_type = findViewById(R.id.company_name);
        firm_layout = findViewById(R.id.firm_layout);
        name = findViewById(R.id.name);
        sm = new SessionManager(this);
        tk = new Lang_Token(this);
        gst_no = findViewById(R.id.gst_no);
        unique_no = findViewById(R.id.unique_no);
        email_phno = findViewById(R.id.email_phno);
        password = findViewById(R.id.password);
        location = findViewById(R.id.location);
        pancard = findViewById(R.id.pancard);
        register = findViewById(R.id.register);
        tin_no = findViewById(R.id.tin_no);

        pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        pan_path = ImageUtils
                                .setImage(Equipment_Register.this, r, pancard, true);
                    }
                }).show(Equipment_Register.this);
            }
        });

        viewDialog = new ViewDialog(Equipment_Register.this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validatesignForm()) {
                    Registers();
                }

            }
        });
        // Spinner Drop down elements


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
                .addMultipartParameter("company_name", industry_type.getText().toString())
                .addMultipartParameter("gsd_no", gst_no.getText().toString())
                .addMultipartParameter("unique_no", unique_no.getText().toString())
                .addMultipartParameter("location", location.getText().toString())
                .addMultipartFile("pancard", pan_f)
                .addMultipartParameter("tin_number", tin_no.getText().toString())
                .addMultipartParameter("user_type", "seed")
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

                        Intent i = new Intent(Equipment_Register.this, Main_Dashboard.class);
                        i.putExtra("location", true);
                        startActivity(i);
                        finishAffinity();

                    } else {
                        Toast.makeText(Equipment_Register.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    Toasty.normal(Equipment_Register.this, "Something went wrong! ").show();
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {

                Toasty.normal(Equipment_Register.this, "Something went wrong! " + anError.getErrorCode()).show();

                viewDialog.hideDialog();
            }
        });
    }

    private boolean validatesignForm() {


        if (!ValidateInputs.isValidInput(industry_type.getText().toString().trim())) {
            industry_type.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
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
        } else if (!ValidateInputs.isValidInput(tin_no.getText().toString())) {
            tin_no.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }


    }

}