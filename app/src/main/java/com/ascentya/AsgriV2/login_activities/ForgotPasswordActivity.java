package com.ascentya.AsgriV2.login_activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity {

    private EditText mobileNumberEt;
    private Button sendOtpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mobileNumberEt = findViewById(R.id.mobileNumber);
        sendOtpBtn = findViewById(R.id.sendOtp);

        sendOtpBtn.setOnClickListener(v -> validate());
    }

    private void validate(){
        String mobileNumber = mobileNumberEt.getText().toString();
        if(TextUtils.isEmpty(mobileNumber)){
            toast("Enter Mobile Number!");
        }else if(!ValidateInputs.isValidPhoneNo(mobileNumber)){
            toast("Enter valid Mobile Number!");
        }else {
            sendOtp(mobileNumber);
        }
    }

    private void sendOtp(String mobileNumber){
        showLoading();
        AndroidNetworking.post(Webservice.sendOtp)
                .addUrlEncodeFormBodyParameter(Constants.MOBILE_NUMBER, mobileNumber)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        if (response.optBoolean("status", false)){
                            goToVerify(mobileNumber);
                        }else {
                            toast(response.optString("message", "Send OTP Failed!"));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        errorToast("Send OTP Error!");
                    }
                });
    }

    private void goToVerify(String mobileNumber){
        openActivity(VerifyOtpActivity.class, Pair.create("mobile_number", mobileNumber));
    }
}