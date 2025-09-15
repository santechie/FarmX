package com.ascentya.AsgriV2.staff.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.staff.data.Staff;

public class StaffPermissionActivity extends BaseActivity {

    private TextView nameTv, emailTv, mobileTv;
    private Staff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_permission);

        setToolbarTitle("Staff Permission", true);

        staff = getFromIntent("staff", Staff.class);

        nameTv = findViewById(R.id.name);
        emailTv = findViewById(R.id.email);
        mobileTv = findViewById(R.id.mobileNumber);

        nameTv.setText(staff.getName());
        emailTv.setText(staff.getEmail());
        mobileTv.setText(staff.getMobileNumber());
    }
}