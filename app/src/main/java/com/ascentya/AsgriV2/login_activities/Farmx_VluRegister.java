package com.ascentya.AsgriV2.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class Farmx_VluRegister extends AppCompatActivity {
    Button register;
    Spinner farmer_type, gender;
    EditText email_phno, name, age, street_name, city;
    TextInputEditText password;


    CircleImageView aadhar_card;
    ViewDialog viewDialog;

    String[] gender_list = {"Male", "Female", "Others"};
    SessionManager sm;
    String aadhar_path, kissan_path;

    LinearLayout fpo_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmx_vlu_register);
        register = findViewById(R.id.register);
        fpo_layout = findViewById(R.id.fpo_layout);
        email_phno = findViewById(R.id.email_phno);

        password = findViewById(R.id.password);
        fpo_layout = findViewById(R.id.fpo_layout);
        name = findViewById(R.id.name);
        sm = new SessionManager(this);
        farmer_type = findViewById(R.id.farmer_type);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        street_name = findViewById(R.id.street_name);
        city = findViewById(R.id.city);
        aadhar_card = findViewById(R.id.aadhar_card);
        viewDialog = new ViewDialog(Farmx_VluRegister.this);


        ArrayAdapter gender_adapter = new ArrayAdapter(this, R.layout.spinner_row, gender_list);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(gender_adapter);


        aadhar_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        aadhar_path = ImageUtils
                                .setImage(Farmx_VluRegister.this, r, aadhar_card, true);
                    }
                }).show(Farmx_VluRegister.this);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatesignForm()) {

                    Registers();

                }
//                Intent i = new Intent(Farmer_Registration.this, Formx_Login_Activity.class);
//                startActivity(i);
            }
        });


    }


    public void Registers() {

        File aadhar_f = null;

        if (aadhar_path != null && aadhar_path.length() > 0) {

            aadhar_f = new File(aadhar_path);
        }


        viewDialog.showDialog();
        AndroidNetworking.upload(Webservice.register_f)
                .addMultipartParameter("username", name.getText().toString())
                .addMultipartParameter("email", email_phno.getText().toString())
                .addMultipartParameter("password", password.getText().toString())
                .addMultipartParameter("phone", email_phno.getText().toString())

                .addMultipartParameter("user_age", age.getText().toString())
                .addMultipartParameter("streetname", street_name.getText().toString())
                .addMultipartParameter("city_name", city.getText().toString())
                .addMultipartFile("aadharcard", aadhar_f)
                .addMultipartParameter("user_gender", gender.getSelectedItem().toString())
                .addMultipartParameter("user_type", "vlu")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


//
//                        tk.setusename("0");
                        Userobject obj = new Userobject();
                        obj.setId(jsonObject.optString("data"));
                        obj.setFirstname(name.getText().toString());
                        obj.setPhno(email_phno.getText().toString());
                        obj.setEmail(email_phno.getText().toString());
                        obj.setIspremium("0");
                        sm.setUser(obj);

                        Intent i = new Intent(Farmx_VluRegister.this, Main_Dashboard.class);
                        i.putExtra("location", true);
                        startActivity(i);
                        finishAffinity();

                    } else {
                        Toast.makeText(Farmx_VluRegister.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                Toasty.normal(Farmx_VluRegister.this, "Something went wrong! ").show();

                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                Toasty.normal(Farmx_VluRegister.this, "Something went wrong! " + anError.getErrorCode()).show();
                viewDialog.hideDialog();
            }
        });
    }

    private boolean validatesignForm() {
        if (!ValidateInputs.isValidNumber(email_phno.getText().toString().trim())) {
            email_phno.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidPassword(password.getText().toString())) {
            password.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
            name.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(age.getText().toString().trim())) {
            age.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(street_name.getText().toString().trim())) {
            street_name.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(city.getText().toString().trim())) {
            city.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }

}