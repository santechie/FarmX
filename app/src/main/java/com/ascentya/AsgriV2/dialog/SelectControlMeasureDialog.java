package com.ascentya.AsgriV2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Constants;

import androidx.annotation.NonNull;

public class SelectControlMeasureDialog extends Dialog {

    private Constants.ReportType reportType;
    private String type, cropId;

    private TextView titleTv;
    private ImageView closeIv;

    public SelectControlMeasureDialog(@NonNull Context context, String type, String cropId){
        super(context, android.R.style.Theme_Material_NoActionBar_Fullscreen);
        this.type = type;
        this.cropId = cropId;
        reportType = Constants.ReportTypes.get(type);
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_control_measure);

        titleTv = findViewById(R.id.title);
        closeIv = findViewById(R.id.close);

        if (reportType != null){
            titleTv.setText(reportType.getName());
        }

        closeIv.setOnClickListener(view -> { dismiss(); });
    }
}
