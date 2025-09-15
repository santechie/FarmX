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
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.LandCrop;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Add_ZonesCrop {


    NoDefaultSpinner crop_select;
    EditText rows;
    Context contx;
    Button register;
    Dialog dialog;
    List<String> Data = new ArrayList<>();
    String landName, land_id, user_i;
    ViewDialog viewDialog;
    String crop_name;

    ArrayList<LandCrop> landCrops = new ArrayList<LandCrop>();

    public void dialog(Context context, String title, String landName, String land_id, String crop_id, String user_i, ViewDialog viewDialog, String crop_name) {
        dialog = new Dialog(context, R.style.DialogSlideAnim);
        this.contx = context;
        this.landName = landName;
        this.land_id = land_id;

        this.user_i = user_i;
        this.viewDialog = viewDialog;
        this.crop_name = crop_name;

        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.add_zonelayout, null);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(alertDialogView);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        final TextView title_head = (TextView) dialog.findViewById(R.id.title);
        ImageView add_maincrop = (ImageView) dialog.findViewById(R.id.add_maincrop);
        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        crop_select = dialog.findViewById(R.id.crop_select);
        rows = dialog.findViewById(R.id.rows);
        register = dialog.findViewById(R.id.register);
        register.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

                if (crop_select.getSelectedItemPosition() > -1) {
                    LandCrop landCrop = landCrops.get(crop_select.getSelectedItemPosition());
                    if (rows.getText().toString().length() > 0) {
                        String uniq = landName+":"+crop_select.getSelectedItem().toString()+"-";
                        add_zone(uniq, landCrop.getCropId(), landCrop.getId());
                    } else {
                        rows.setError(contx.getString(R.string.required));
                    }


                } else {
                    Toast.makeText(contx, "Kindly select Crop", Toast.LENGTH_SHORT).show();
                }
            }
        });


        title_head.setText(title);
        get_crops();


    }

    private Integer searchFor(String data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            String unitString = Webservice.Data_crops.get(i).getName().toLowerCase().trim();
            String C_name = Webservice.Data_crops.get(i).getS_name().toLowerCase().trim();


            if (unitString.equals(data.toLowerCase().trim()) || C_name.equals(data.toLowerCase().trim())) {


                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }


    private Integer searchname(Integer data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            Integer unitString = Integer.parseInt(Webservice.Data_crops.get(i).getCrop_id());


            if (unitString.equals(data)) {


                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }

    public void add_zone(String uniq, String cropId, String lcId) {
        viewDialog.showDialog();


        AndroidNetworking.post(Webservice.landzone_get)
                .addUrlEncodeFormBodyParameter("user_id", user_i)
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .addUrlEncodeFormBodyParameter("crop_id", cropId)
                .addUrlEncodeFormBodyParameter("lc_id", lcId)
                .addUrlEncodeFormBodyParameter("zone_count", rows.getText().toString())
                .addUrlEncodeFormBodyParameter("zone_uniqueid", uniq)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        if (contx != null && contx instanceof BaseActivity)
                            ((BaseActivity) contx).sendBroadcast(Constants.Broadcasts.ZONE_UPDATE);

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

    private void showLoading(){
        if (viewDialog != null) viewDialog.showDialog();
    }

    private void hideLoading(){
        if (viewDialog != null) viewDialog.hideDialog();
    }

    public void get_crops() {


        AndroidNetworking.post(Webservice.landzonecrop_get)
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {

                    landCrops.clear();

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            landCrops.add(GsonUtils.getGson().fromJson(jsonArray.getJSONObject(i).toString(), LandCrop.class));

                            String cropId = jsonArray.getJSONObject(i).optString("crop_id");
                            String varietyId = jsonArray.getJSONObject(i).optString("variety_id");

                            Crops_Main crop = Webservice.getCrop(cropId);
                            VarietyModel variety = Webservice.getVariety(varietyId);

                            String name = "";

                            if (crop != null)
                                name = crop.getName();

                            if (variety != null)
                                name += " (" + variety.getName() + ")";

                            Data.add(name);
                        }

                    } else {


                    }

                    ArrayAdapter<String> soiltype_adpter = new ArrayAdapter(contx, R.layout.spinner_item,
                            Data);

                    crop_select.setAdapter(soiltype_adpter);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {

            }
        });


    }

    public String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }
}
