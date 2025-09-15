package com.ascentya.AsgriV2.login_activities;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.AsgriDateUtils;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyOtpActivity extends BaseActivity {

    private String mobileNumber;
    private TextView mobileNumberTv, changeTv, resendOtpTv;
    private EditText otpEt;
    private Button verifyBtn;

    private CountDownTimer timer;
    private boolean isTimerFinished = false;

    private long OTP_TIME = 5 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        
        mobileNumber = savedInstanceState == null ?
                getIntent().getStringExtra(Constants.MOBILE_NUMBER) :
                savedInstanceState.getString(Constants.MOBILE_NUMBER);

        mobileNumberTv = findViewById(R.id.mobileNumberTv);
        changeTv = findViewById(R.id.change);
        resendOtpTv = findViewById(R.id.resendOtp);

        otpEt = findViewById(R.id.otp);

        changeTv.setOnClickListener(v -> finish());

        resendOtpTv.setOnClickListener(v -> resendOtp());

        verifyBtn = findViewById(R.id.verifyBtn);
        verifyBtn.setOnClickListener(v -> validate());

        updateMobileNumber();
        startTimer();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.MOBILE_NUMBER, mobileNumber);

        updateMobileNumber();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mobileNumber = savedInstanceState.getString(Constants.MOBILE_NUMBER);

        updateMobileNumber();
    }

    private void startTimer(){
        timer = new CountDownTimer(OTP_TIME, 1000) {
            @Override
            public void onTick(long l) {
                resendOtpTv.setText(AsgriDateUtils.millisecondsToTime(l + 1000));
            }

            @Override
            public void onFinish() {
                isTimerFinished = true;
                resendOtpTv.setText("Resend OTP");
            }
        };

        timer.start();
    }

    private void updateMobileNumber(){
        mobileNumberTv.setText(mobileNumber);
    }

    private void resendOtp(){
        if (isTimerFinished){
            showLoading();
            AndroidNetworking.post(Webservice.sendOtp)
                    .addUrlEncodeFormBodyParameter(Constants.MOBILE_NUMBER, mobileNumber)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideLoading();
                            if (response.optBoolean("status", false)){
                                isTimerFinished = false;
                                startTimer();
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
    }

    private void validate(){

        String otp = otpEt.getText().toString();

        if (TextUtils.isEmpty(otp)){
            toast("Enter OTP!");
        }else if (!ValidateInputs.isValidOtp(otp)){
            toast("Enter valid OTP!");
        }else {
            validateOtp(otp);
        }
    }

    private void validateOtp(String otp){
        showLoading();
        AndroidNetworking.post(Webservice.validateOtp)
                .addUrlEncodeFormBodyParameter("mobile_number", mobileNumber)
                .addUrlEncodeFormBodyParameter("otp", otp)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                if (response.optBoolean("status", false)){
                    try {
                        String uoId = response.getString("uo_id");
                        goToReset(uoId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    toast(response.optString("message", "Validate OTP Failed!"));
                }
            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
                errorToast("Validate OTP Error!");
            }
        });
    }

    private void goToReset(String uoId){
        openActivity(ResetPasswordActivity.class, Pair.create("uo_id", uoId));
    }
}