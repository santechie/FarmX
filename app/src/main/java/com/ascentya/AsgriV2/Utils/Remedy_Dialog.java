package com.ascentya.AsgriV2.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Reports_Model;
import com.ascentya.AsgriV2.Models.ZoneReport;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Remedy_Dialog {

    private Action action;
    String user_id, land_id, zone_id;

    Dialog dialog;


    Context con;
    TextView title_page, issueTitleTv;
    Button register;
    ImageView close;
    EditText activity_type, pest_used, quantity, date, costEt;
    Spinner issue;
    RelativeLayout issueSpinner;
    ProgressBar loader;

    List<Reports_Model> Data_report = new ArrayList<>();
    List<String> country = new ArrayList<>();
    String report_id;
    Calendar cal;
    ZoneReport zoneReport;

    public void dialog(final Context context, String title, String land, String zone, String user_id, ZoneReport zoneReport){
        this.zoneReport = zoneReport;
        dialog(context, title, land, zone, user_id);
    }

    public void dialog(final Context context, String title, String land, String zone, String user_id) {
        dialog = new Dialog(context, R.style.DialogSlideAnim);
        this.user_id = user_id;
        this.con = context;
        this.land_id = land;
        this.zone_id = zone;

        cal = Calendar.getInstance();
        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.remedy_land, null);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(alertDialogView);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        title_page = dialog.findViewById(R.id.title);
        activity_type = dialog.findViewById(R.id.activity_type);
        pest_used = dialog.findViewById(R.id.pest_used);
        register = dialog.findViewById(R.id.register);
        quantity = dialog.findViewById(R.id.quantity);
        close = dialog.findViewById(R.id.close);
        date = dialog.findViewById(R.id.date);
        issue = dialog.findViewById(R.id.issue);
        costEt = dialog.findViewById(R.id.cost);
        loader = dialog.findViewById(R.id.progressBar);
        issueTitleTv = dialog.findViewById(R.id.issueTitle);
        issueSpinner = dialog.findViewById(R.id.issueSpinner);

        date.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(con, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                        date.setError(null);
                        date.setText(String.format("%d", dayOfMonth) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", year));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        if (zoneReport != null){
            issueTitleTv.setVisibility(View.GONE);
            issueSpinner.setVisibility(View.GONE);
        }else {
            get_reports();
        }

        close.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                dialog.dismiss();
            }
        });
        title_page.setText(title);

        register.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                if (zoneReport != null || (issue != null && issue.getSelectedItem() != null)) {
                    if (validatesignForm()) {
                        add_remedy();
                    }
                } else {
                    Toast.makeText(con, "Select Issue", Toast.LENGTH_SHORT).show();
                }
            }
        });


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

    private boolean validatesignForm() {
        if (!ValidateInputs.isValidInput(activity_type.getText().toString().trim())) {
            activity_type.setError(con.getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(pest_used.getText().toString())) {
            pest_used.setError(con.getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(quantity.getText().toString().trim())) {
            quantity.setError(con.getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(date.getText().toString().trim())) {
            date.setError(con.getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }

    public void add_remedy() {
//        viewDialog.showDialog();

        issue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                report_id = Data_report.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loader.setVisibility(View.VISIBLE);
        register.setVisibility(View.INVISIBLE);

        AndroidNetworking.post(Webservice.addzone_remedy)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .addUrlEncodeFormBodyParameter("report_id", zoneReport == null ?
                        Data_report.get(issue.getSelectedItemPosition()).getId() : zoneReport.getId())
                .addUrlEncodeFormBodyParameter("zone_name", zone_id)
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .addUrlEncodeFormBodyParameter("activity_type", activity_type.getText().toString())
                .addUrlEncodeFormBodyParameter("fertilizer_used", pest_used.getText().toString())
                .addUrlEncodeFormBodyParameter("quantity", quantity.getText().toString())
                .addUrlEncodeFormBodyParameter("cost", costEt.getText().toString())
                .addUrlEncodeFormBodyParameter("date_action", date.getText().toString())


                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
//                viewDialog.hideDialog();

                loader.setVisibility(View.INVISIBLE);
                register.setVisibility(View.VISIBLE);

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        dialog.dismiss();
                        Toasty.success(con, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        if (action != null) action.onComplete();
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
//                viewDialog.hideDialog();
                loader.setVisibility(View.INVISIBLE);
                register.setVisibility(View.VISIBLE);

            }
        });


    }

    public void get_reports() {
        AndroidNetworking.post(Webservice.reports_get)
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .addUrlEncodeFormBodyParameter("User_id", user_id)
                .addUrlEncodeFormBodyParameter("zone_name", zone_id)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    country = new ArrayList<>();
                    Data_report = new ArrayList<>();
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            Reports_Model obj = new Reports_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("id"));
                            obj.setContent(jsonArray.getJSONObject(i).optString("content"));
                            obj.setAffacted_disease(jsonArray.getJSONObject(i).optString("affected_disease"));
                            obj.setAffected_cause(jsonArray.getJSONObject(i).optString("affected_cause"));
                            obj.setRecovery_process(jsonArray.getJSONObject(i).optString("recovery_process"));
                            country.add(jsonArray.getJSONObject(i).optString("affected_disease"));
                            Data_report.add(obj);
                        }


                    } else {

                    }

                    ArrayAdapter aa = new ArrayAdapter(con, android.R.layout.simple_spinner_item, country);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    issue.setAdapter(aa);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {


            }
        });

    }

    public void sendPestUpdate(){
        if (con != null && con instanceof BaseActivity) {
            ((BaseActivity) con).sendBroadcast(Constants.Broadcasts.PEST_UPDATE);
        }
    }

    public interface Action{
        void onComplete();
    }
}
