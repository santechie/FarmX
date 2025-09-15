package com.ascentya.AsgriV2.login_activities;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import org.json.JSONObject;

public class ResetPasswordActivity extends BaseActivity {

    private String uoId;

    private EditText newPasswordEt, confirmPasswordEt;
    private Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        uoId = savedInstanceState == null ?
                getIntent().getStringExtra("uo_id") :
                savedInstanceState.getString("uo_id");

        newPasswordEt = findViewById(R.id.newPassword);
        confirmPasswordEt = findViewById(R.id.confirmPassword);

        resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> validate());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("uo_id", uoId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        uoId = savedInstanceState.getString("uo_id");
    }

    private void validate(){

        String newPassword = newPasswordEt.getText().toString();
        String confirmPassword = confirmPasswordEt.getText().toString();

        if (TextUtils.isEmpty(newPassword)){
            toast("Enter New Password!");
        }else if (TextUtils.isEmpty(confirmPassword)){
            toast("Enter Confirm Password!");
        }else if(!ValidateInputs.isValidPassword(newPassword)){
            toast("Enter Valid New Password!");
        }else if (!newPassword.equals(confirmPassword)){
            toast("New and Confirm Password are not same!");
        }else {
            resetPassword(newPassword, confirmPassword);
        }
    }

    private void resetPassword(String newPassword, String confirmPassword){
        showLoading();
        AndroidNetworking.post(Webservice.resetPassword)
                .addUrlEncodeFormBodyParameter("uo_id", uoId)
                .addUrlEncodeFormBodyParameter("new_password", newPassword)
                .addUrlEncodeFormBodyParameter("confirm_password", confirmPassword)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                if (response.optBoolean("status", false)){
                    goToLogin();
                }else {
                    toast(response.optString("message", "Reset Password Failed!"));
                }
            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
                errorToast("Reset Password Error!");
            }
        });
    }

    private void goToLogin(){
        openActivity(Formx_Login_Activity.class);
        finishAffinity();
    }

}