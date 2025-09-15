package com.ascentya.AsgriV2.e_market.activities;

import es.dmoral.toasty.Toasty;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.Main_Dashboard;
import com.ascentya.AsgriV2.R;

public class OrderConfirmationActivity extends BaseActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView messageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        setOverrideOnPause();

        imageView = findViewById(R.id.image);
        progressBar = findViewById(R.id.progressBar);
        messageTv = findViewById(R.id.message);


        startAnimationWithDelay();
    }

    private void startAnimationWithDelay(){
        new Handler().postDelayed(() -> {
           doAnimation();
        }, 1000);
    }

    private void doAnimation(){

        long animationTime = 150;

        ValueAnimator imageAnimator = ValueAnimator.ofFloat(0f, 1f);
        imageAnimator.setDuration(animationTime);
        imageAnimator.setInterpolator(new LinearInterpolator());
        imageAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float value = (Float) valueAnimator.getAnimatedValue();
                imageView.setScaleX(value);
                imageView.setScaleY(value);
                imageView.setAlpha(0.5f + (value/2f));

                if (value == 1){
                    finishWithDelay();
                }
            }
        });

        ValueAnimator progressAnimator = ValueAnimator.ofFloat(1.0f, 0f);
        progressAnimator.setDuration(animationTime);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator progressAnimator) {
                Float value = (Float) progressAnimator.getAnimatedValue();
                System.out.println("Progress: " + value);
                progressBar.setScaleX(value);
                progressBar.setScaleY(value);
                progressBar.setAlpha(1 - (value/2f));

                if (value == 0f){
                    messageTv.setText("Order Placed!");
                    imageAnimator.start();
                }
            }
        });

        progressAnimator.start();
    }

    private void finishWithDelay(){
        //finishAffinity();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                openActivity(OrderConfirmationActivity.class);
//                Intent i = new Intent(OrderConfirmationActivity.this, Main_Dashboard.class);
//                startActivity(i);
//                ActivityCompat.finishAffinity(OrderConfirmationActivity.this);
                //finishAffinity();
                //finishAffinity();
                //finishAndRemoveTask();
                Intent intent = new Intent(getApplicationContext(), Main_Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 1000L);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toasty.normal(this, "Please Wait...").show();
    }
}