package com.ascentya.AsgriV2.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.google.android.material.chip.ChipGroup;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Reports_Dialog {

    String user_id;

    Dialog dialog;
    CircleImageView aadhar_card, recovery_process;
    String affectedImagePath, recoveryImagePath, land_id, lcId, zone_id;
    Context con;
    TextView title_page;
    Button register;
    ImageView close;
    EditText content, affected_disease, affected_cause, recovery, costEt;
    Spinner typeSpinner;
    ProgressBar loader;
    ChipGroup typesGroup;

    private Action action;
    private ArrayList<String> images = new ArrayList<>();

    public void dialog(final Context context, String title, String land, String lcId, String zone, String user_id) {
        dialog = new Dialog(context, R.style.DialogSlideAnim);
        this.user_id = user_id;
        this.con = context;
        this.land_id = land;
        this.lcId = lcId;
        this.zone_id = zone;

        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.reports_land, null);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(alertDialogView);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        aadhar_card = dialog.findViewById(R.id.aadhar_card);
        affected_disease = dialog.findViewById(R.id.affected_disease);
        affected_cause = dialog.findViewById(R.id.affected_cause);
        title_page = dialog.findViewById(R.id.title);
        content = dialog.findViewById(R.id.content);
        register = dialog.findViewById(R.id.register);
        close = dialog.findViewById(R.id.close);
        recovery = dialog.findViewById(R.id.recovery);
        recovery_process = dialog.findViewById(R.id.recovery_process);
        costEt = dialog.findViewById(R.id.cost);
        loader = dialog.findViewById(R.id.progressBar);

        close.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                dialog.dismiss();
            }
        });
        title_page.setText(title);

        typeSpinner = dialog.findViewById(R.id.typeSpinner);
        typeSpinner.setAdapter(new ArrayAdapter<Constants.ReportType>(context,
                R.layout.spinner_row_space,
                Constants.ReportTypes.ALL));

        /*typesGroup = dialog.findViewById(R.id.typeGroup);

        for (Constants.ReportType reportType: Constants.ReportTypes.ALL){
            Chip chip = new Chip(context);
            chip.setText(reportType.getName());
            chip.setCheckable(true);
            chip.setChecked(Constants.ReportTypes.PEST.getValue().equals(reportType.getValue()));
            chip.setTextSize(10f);
            typesGroup.addView(chip);
        }*/

        register.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                if (validatesignForm()) {
                    add_reports();
                }
            }
        });

        aadhar_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        affectedImagePath = ImageUtils
                                .setImage(context, r, aadhar_card, true);
                        Toasty.success(context, affectedImagePath).show();
                    }
                }).show((FragmentActivity) con);

            }
        });
        recovery_process.setOnClickListener(view -> PickImageDialog.build(new PickSetup())
                .setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {
                recoveryImagePath = ImageUtils
                        .setImage(context, r, recovery_process, true);
            }
        }).show((FragmentActivity) con));

//        activity_type = dialog.findViewById(R.id.activity_type);
//        activity_done = dialog.findViewById(R.id.activity_done);
//        activity_start = dialog.findViewById(R.id.activity_start);
//        activity_end = dialog.findViewById(R.id.activity_end);
//        activity_total = dialog.findViewById(R.id.activity_total);
//        machine_type = dialog.findViewById(R.id.machine_type);
//        title_dialog = dialog.findViewById(R.id.title);
//        activity_vendarname = dialog.findViewById(R.id.activity_vendarname);
//        contract_type = dialog.findViewById(R.id.contract_type);
//        member_machine = dialog.findViewById(R.id.member_machine);
//        member_machine.setLayoutManager(new LinearLayoutManager(context));
//        member_machine.setHasFixedSize(true);
//        title_dialog.setText(title);
//
    }

    public void setAction(Action action){
        this.action = action;
    }

    public void add_reports() {
//        viewDialog.showDialog();
        loader.setVisibility(View.VISIBLE);
        register.setVisibility(View.INVISIBLE);

        ANRequest.MultiPartBuilder multiPartBuilder = AndroidNetworking.upload(Webservice.addzone_report);
        multiPartBuilder.addMultipartParameter("user_id", user_id);
        multiPartBuilder.addMultipartParameter("zone_name", zone_id);
        multiPartBuilder.addMultipartParameter("land_id", land_id);
        multiPartBuilder.addMultipartParameter("type", ((Constants.ReportType) typeSpinner.getSelectedItem()).getValue());
        multiPartBuilder.addMultipartParameter("content", content.getText().toString());
        multiPartBuilder.addMultipartParameter("affected_disease", affected_disease.getText().toString());
        multiPartBuilder.addMultipartParameter("affected_cause", affected_cause.getText().toString());
        multiPartBuilder.addMultipartParameter("recovery_process", recovery.getText().toString());
        multiPartBuilder.addMultipartParameter("cost", costEt.getText().toString());

        if (affectedImagePath != null)
            multiPartBuilder.addMultipartFile("images[]", getFileForPath(affectedImagePath));
        if (recoveryImagePath != null)
            multiPartBuilder.addMultipartFile("images[]", getFileForPath(recoveryImagePath));

        multiPartBuilder.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
//                viewDialog.hideDialog();
                loader.setVisibility(View.INVISIBLE);
                register.setVisibility(View.VISIBLE);
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        dialog.dismiss();
                        if (action != null) action.onAdded();
                        Toasty.success(con, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        sendPestUpdate();
                    } else {
                        Toasty.error(con, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                loader.setVisibility(View.INVISIBLE);
                register.setVisibility(View.VISIBLE);
//                viewDialog.hideDialog();
            }
        });
    }

    private boolean validatesignForm() {
        if (!ValidateInputs.isValidInput(content.getText().toString().trim())) {
            content.setError(con.getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(affected_disease.getText().toString())) {
            affected_disease.setError(con.getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(affected_cause.getText().toString().trim())) {
            affected_cause.setError(con.getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(recovery.getText().toString().trim())) {
            recovery.setError(con.getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }

    private File getFileForPath(String path){
        if (path != null && !path.isEmpty()){
            try {
                return new File(path);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private void sendPestUpdate(){
        if (con != null && con instanceof BaseActivity)
            ((BaseActivity) con).sendBroadcast(Constants.Broadcasts.PEST_UPDATE);
    }

    public interface Action{
        void onAdded();
    }
}
