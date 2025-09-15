package com.ascentya.AsgriV2.beta;

import es.dmoral.toasty.Toasty;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.dialog.MulitSelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.activities.LandMapActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ascentya.AsgriV2.managers.AccessManager.Modules.VIEW_LAND;

public class NewFeaturesActivity extends BaseActivity {

    private ArrayList<Maincrops_Model> lands =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_features);

        setToolbarTitle("Beta", true);

        findViewById(R.id.landMap)
                .setOnClickListener(v -> openWithAccess(LandMapActivity.class, VIEW_LAND));

        findViewById(R.id.iotRequest)
                .setOnClickListener(view -> iotRequest());

        loadLands();
    }

    private void loadLands(){
        ApiHelper.loadLands(sessionManager.getUser().getId(),
                new ApiHelper.LandAction() {
                    @Override
                    public void onLoadComplete(JSONObject response,ArrayList<Maincrops_Model> lands, boolean error) {

                        if (UserHelper.checkResponse(NewFeaturesActivity.this, response)){
                            return;
                        }

                        if (!error) {
                            NewFeaturesActivity.this.lands.clear();
                            NewFeaturesActivity.this.lands.addAll(lands);
                        }else {
                            Toasty.error(NewFeaturesActivity.this, "Load Land Error!").show();
                        }
                    }
                });
    }

    private void iotRequest(){
        new MulitSelectItemDialog("Lands", Arrays.asList(lands.toArray()), selectedObjects -> {
            try {
                requestIoT(Arrays.asList(selectedObjects.toArray(new Maincrops_Model[selectedObjects.size()])));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).show(getSupportFragmentManager(), "lands");
    }

    private void requestIoT(List<Maincrops_Model> lands) throws JSONException {
        showLoading();

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("user_id", sessionManager.getUser().getId());

            JSONArray landsArray = new JSONArray();

            for (Maincrops_Model land: lands){
                landsArray.put(land.getId());
            }

            jsonObject.put("lands[]", landsArray);

            System.out.println(jsonObject);

            ANRequest.PostRequestBuilder requestBuilder = AndroidNetworking.post(Webservice.iotRequest);
            requestBuilder.addJSONObjectBody(jsonObject);
            //requestBuilder.addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId());
            //requestBuilder.addUrlEncodeFormBodyParameter("lands", landsArray);

           /* ANRequest.MultiPartBuilder builder = AndroidNetworking.upload(Webservice.iotRequest);
            builder.addMultipartParameter("user_id", sessionManager.getUser().getId());

            for (Maincrops_Model land : lands) {
                builder.addMultipartParameter("lands[]", land.getId());
            }*/

            requestBuilder.build().getAsJSONObject(
                    new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideLoading();
                            if (response.optBoolean("status")) {
                                System.out.println("Response: " + response);
                                Toasty.normal(NewFeaturesActivity.this,
                                        "IoT Request Sent!").show();
                            }else {
                                Toasty.error(NewFeaturesActivity.this,
                                        response.optString("message")).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                            try {
                                System.out.println("Error: " + GsonUtils.getGson().toJson(anError));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            hideLoading();
                            Toasty.error(NewFeaturesActivity.this, "IoT Request Error!").show();
                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
            hideLoading();
            Toasty.error(NewFeaturesActivity.this, "IoT Request Error!").show();
        }
    }
}