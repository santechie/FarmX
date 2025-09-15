package com.ascentya.AsgriV2.farmx.my_lands;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Remedy_Adapter;
import com.ascentya.AsgriV2.Adapters.Reports_Adapter;
import com.ascentya.AsgriV2.Models.Remedy_Model;
import com.ascentya.AsgriV2.Models.ZoneReport;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class ReportHistory_Activity extends AppCompatActivity {

    ViewDialog viewDialog;
    RecyclerView reports_recycler;
    SessionManager sm;
    List<ZoneReport> Data_report;
    List<Remedy_Model> Data_remedy;
    Reports_Adapter reports_adapter;
    Remedy_Adapter remedy_adapter;
    String land_id, zone_id, cropname, zonename;
    ImageView goback;
    TextView cropZoneTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);
        sm = new SessionManager(this);
        Data_remedy = new ArrayList<>();
        Data_report = new ArrayList<>();
        zone_id = getIntent().getStringExtra("zone");
        land_id = getIntent().getStringExtra("land");
        cropname = getIntent().getStringExtra("crop_name");
        zonename = getIntent().getStringExtra("zone_name");

        viewDialog = new ViewDialog(this);

        reports_recycler = findViewById(R.id.reports_recycler);
        cropZoneTv = findViewById(R.id.cropZone);

        String text = cropname + " / <b> " + zonename + "</b>";

        cropZoneTv.setText(Html.fromHtml(text));

        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        reports_recycler.setLayoutManager(new LinearLayoutManager(this));
        reports_recycler.hasFixedSize();
        get_reports();
//        get_remedy();

    }

    public void get_reports() {
        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.reports_get)
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .addUrlEncodeFormBodyParameter("User_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("zone_name", zone_id)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
viewDialog.hideDialog();
                System.out.println("Report Response \n" + jsonObject);

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        List<ZoneReport> zoneReportList = GsonUtils.getGson().fromJson(jsonArray.toString(),
                                EMarketStorage.ZoneReportListType);

                        Data_report.addAll(zoneReportList);

                        /*for (int i = 0; i < jsonArray.length(); i++) {
                            Reports_Model obj = new Reports_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("id"));
                            obj.setContent(jsonArray.getJSONObject(i).optString("content"));
                            obj.setAffacted_disease(jsonArray.getJSONObject(i).optString("affected_disease"));
                            obj.setAffected_cause(jsonArray.getJSONObject(i).optString("affected_cause"));
                            obj.setRecovery_process(jsonArray.getJSONObject(i).optString("recovery_process"));

                            JSONArray remedy_array = jsonArray.getJSONObject(i).getJSONArray("remedy_list");
                            Data_remedy = new ArrayList<>();
                            for (int j = 0; j < remedy_array.length(); j++) {

                                Remedy_Model obj_one = new Remedy_Model();
                                obj_one.setActivity_type(remedy_array.getJSONObject(j).optString("activity_type"));
                                obj_one.setQuantity(remedy_array.getJSONObject(j).optString("quantity"));
                                obj_one.setPest_used(remedy_array.getJSONObject(j).optString("fertilizer_used"));
                                obj_one.setDate(remedy_array.getJSONObject(j).optString("date_action"));

                                Data_remedy.add(obj_one);
                            }


                            obj.setRemedy_data(Data_remedy);


                            Data_report.add(obj);
                        }*/

                    } else {

                    }

                    reports_adapter = new Reports_Adapter(ReportHistory_Activity.this, Data_report);
                    reports_recycler.setAdapter(reports_adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                Toasty.error(ReportHistory_Activity.this, "History Load Error!").show();

            }
        });

    }

    public void get_remedy() {


        AndroidNetworking.post(Webservice.remedy_get)
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("zone_name", zone_id)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {


                            Remedy_Model obj = new Remedy_Model();
                            obj.setActivity_type(jsonArray.getJSONObject(i).optString("activity_type"));
                            obj.setQuantity(jsonArray.getJSONObject(i).optString("quantity"));
                            obj.setPest_used(jsonArray.getJSONObject(i).optString("fertilizer_used"));
                            obj.setDate(jsonArray.getJSONObject(i).optString("date_action"));

//                            Data_remedy.add(obj);

                        }

                    } else {

                    }

//                    remedy_adapter = new Remedy_Adapter(ReportHistory_Activity.this, Data_remedy);
//                    remedy_recycler.setAdapter(remedy_adapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });

    }
}