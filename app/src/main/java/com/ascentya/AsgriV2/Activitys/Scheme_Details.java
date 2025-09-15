package com.ascentya.AsgriV2.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;

import androidx.appcompat.app.AppCompatActivity;

public class Scheme_Details extends AppCompatActivity {
    TextView amount, eligibility, min, max, others, bank_name;
    Integer pos = 0;
    ImageView goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_details);


        amount = findViewById(R.id.amount);
        goback = findViewById(R.id.goback);
        bank_name = findViewById(R.id.bank_name);
        eligibility = findViewById(R.id.eligibility);
        min = findViewById(R.id.min);
        max = findViewById(R.id.max);
        others = findViewById(R.id.others);


        goback.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                onBackPressed();
            }
        });


        bank_name.setText(getIntent().getStringExtra("bankname"));

        pos = getIntent().getIntExtra("pos", 0);

        eligibility.setText(getIntent().getStringExtra("eligible"));
        amount.setText(getIntent().getStringExtra("amount"));
        min.setText(getIntent().getStringExtra("interest"));
        others.setText(getIntent().getStringExtra("others"));

//        if (pos==0){
//            amount.setText("Upto 1,00,000");
//            min.setText("Min : 5.55%");
//            max.setText("Max : 8.55%");
//            eligibility.setText("All farmers");
//            others.setText("Amount Loan upto Rs 10000/- Rate interest Nill loan above 10000, Rate of interest 15%");
//
//        } else if (pos==1){
//            amount.setText("Upto 1,00,000");
//            min.setText("Min : 6.41%");
//            max.setText("Max : 8.55%");
//            eligibility.setText("All farmers");
//            others.setText("Amount Loan upto Rs 10000/- Rate interest Nill loan above 10000, Rate of interest 15%");
//
//        } else if (pos==2){
//            amount.setText("Upto 2,00,000");
//            min.setText("Min : 4.41%");
//            max.setText("Max : 7.55%");
//            eligibility.setText("All farmers");
//            others.setText("Amount Loan upto Rs 10000/- Rate interest Nill loan above 10000, Rate of interest 15%");
//
//        } else {
//            amount.setText("Upto 1,00,000");
//            min.setText("Min : 6.41%");
//            max.setText("Max : 8.55%");
//            eligibility.setText("All farmers");
//            others.setText("Amount Loan upto Rs 10000/- Rate interest Nill loan above 10000, Rate of interest 15%");
//
//        }


    }
}