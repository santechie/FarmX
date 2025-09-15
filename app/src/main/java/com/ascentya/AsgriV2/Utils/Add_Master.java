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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Interfaces_Class.UpdateMaster;
import com.ascentya.AsgriV2.Models.Masterfield_Model;
import com.ascentya.AsgriV2.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Add_Master {
    NoDefaultSpinner crop_select;

    Context contx;
    Button register;
    Dialog dialog;
    List<Masterfield_Model> Data = new ArrayList<>();

    String land_id, user_i;
    ViewDialog viewDialog;
    String Master_id;
    UpdateMaster updateMaster;


    public void dialog(Context context, String title, String land_id, String user_i, ViewDialog viewDialog, UpdateMaster updateMaster) {
        dialog = new Dialog(context, R.style.DialogSlideAnim);
        this.contx = context;
        this.land_id = land_id;

        this.user_i = user_i;
        this.viewDialog = viewDialog;
        this.updateMaster = updateMaster;

        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.add_masterrow, null);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(alertDialogView);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        final TextView title_head = (TextView) dialog.findViewById(R.id.title);

        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        crop_select = dialog.findViewById(R.id.crop_select);
        crop_select.setPrompt(contx.getString(R.string.master));

        register = dialog.findViewById(R.id.register);
        register.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

                if (crop_select.getSelectedItemPosition() > -1) {

                    add_master();
//                    add_zone();

//                        add_zone(uniq, crop_select.getSelectedItem().toString());


                } else {
                    Toast.makeText(contx, "Kindly select Master", Toast.LENGTH_SHORT).show();
                }
            }
        });


        title_head.setText(title);
        get_crops();


    }


    public void add_master() {
        viewDialog.showDialog();


        AndroidNetworking.post(Webservice.addenvironment)
                .addUrlEncodeFormBodyParameter("ui", user_i)
                .addUrlEncodeFormBodyParameter("li", land_id)
                .addUrlEncodeFormBodyParameter("dn", Data.get(crop_select.getSelectedItemPosition()).getMaster_name())
                .addUrlEncodeFormBodyParameter("ph", "")
                .addUrlEncodeFormBodyParameter("t", "")
                .addUrlEncodeFormBodyParameter("h", "")
                .addUrlEncodeFormBodyParameter("m", "")
                .addUrlEncodeFormBodyParameter("l", "")
                .addUrlEncodeFormBodyParameter("s", "1")

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        updateMaster.updatemaster();
                        dialog.dismiss();


                        Toasty.success(contx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(contx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();


            }
        });


    }

    public void get_crops() {


        AndroidNetworking.get(Webservice.masterfield)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Masterfield_Model obj = new Masterfield_Model();
                            obj.setMaster_name(jsonArray.getJSONObject(i).optString("master_name"));
                            obj.setId(jsonArray.getJSONObject(i).optString("id"));
//                            obj.setMaster_status(jsonArray.getJSONObject(i).optString("id"));

                            Data.add(obj);
                        }

                        ArrayAdapter<String> soiltype_adpter = new ArrayAdapter(contx, R.layout.spinner_item,
                                Data);

                        crop_select.setAdapter(soiltype_adpter);

                        crop_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Master_id = Data.get(position).getId();
                            }
                        });

                        crop_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object item = parent.getItemAtPosition(position);


                                if (item instanceof Masterfield_Model) {
                                    Masterfield_Model studentInfo = (Masterfield_Model) item;
                                    // do something with the studentInfo object


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {

                    }

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
