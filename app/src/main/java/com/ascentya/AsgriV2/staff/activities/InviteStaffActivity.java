package com.ascentya.AsgriV2.staff.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.IntentUtils;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;

public class InviteStaffActivity extends BaseActivity {

    private LinearLayout typeContainer;
    private TextView typeSpinner;
    private MaterialButton inviteBtn;
    private EditText nameEt, emailEt, mobileNumberEt;

    private List<String> types = Arrays.asList("Manager");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_staff);

        setToolbarTitle("Invite Staff", true);

        typeContainer = findViewById(R.id.typeContainer);
        typeSpinner = findViewById(R.id.typeSpinner);
        typeSpinner.setOnClickListener(v -> showTypes());
        typeSpinner.setText(types.get(0));

        nameEt = findViewById(R.id.name);
        emailEt = findViewById(R.id.email);
        mobileNumberEt = findViewById(R.id.mobileNumber);

        inviteBtn = findViewById(R.id.invite);

        inviteBtn.setOnClickListener(v->invite());
    }

    private void showTypes(){
        new SelectItemDialog("Types", (List<Object>)(List<?>) types, position -> {
        }).show(getSupportFragmentManager(), "crops");
    }

    private void invite(){
        String name = nameEt.getText().toString();
        String email = emailEt.getText().toString();
        String mobileNumber = mobileNumberEt.getText().toString();

        if (!ValidateInputs.isValidName(name)){
            toast("Enter valid name!");
        }else if(!ValidateInputs.isValidNumber(mobileNumber)){
            toast("Enter valid Mobile number!");
        }else if (!ValidateInputs.isValidEmail(email)){
            toast("Enter valid Email ID");
        }else {
            IntentUtils.openWhatsApp(this, "+91" + mobileNumber);
        }
    }
}