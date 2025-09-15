package com.ascentya.AsgriV2.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Main_Dashboard;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.SnackBarUtils;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.intro.IntroActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.managers.SubscriptionManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class Farmx_SplashActivity extends BaseActivity {
    SessionManager sm;
    TextView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmx_splash);

        sm = new SessionManager(this);
        refresh = findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getcrops();
            }
        });

        if (NetworkDetector.isNetworkStatusAvialable(this)) {
            System.out.println("aaaaaaaaaaaa"+"1");
            //checkAccess();
            loadPrivileges();
        } else {
            SnackBarUtils.show(this, "Please check your network connection ",
                    "Retry", new SnackBarUtils.Action() {
                        @Override
                        public void onClicked() {
                            System.out.println("aaaaaaaaaaaa"+"2");
                            //checkAccess();
                            loadPrivileges();
                        }
                    });
        }

    }

    public void checkAccess() {
        System.out.println("aaaaaaaaaaaa"+"3");
        super.checkAccess(isAllowed -> loadPrivileges());
        System.out.println("aaaaaaaaaaaa"+"4");
    }

    public void loadPrivileges() {
        System.out.println("aaaaaaaaaaaa"+"5");
        if (getSubscriptionManager().canLoad()) {
            getSubscriptionManager().load(new SubscriptionManager.Action() {
                @Override
                public void onLoaded(boolean error) {

                    if (error) {
                        System.out.println("sadfdsfasdfdsfds"+error);
                        SnackBarUtils.show(Farmx_SplashActivity.this, "Subscription error",
                                "Retry", new SnackBarUtils.Action() {
                                    @Override
                                    public void onClicked() {
                                        loadPrivileges();
                                    }
                                });
                    } else {
                        loadModules();
                    }
                }
            });
        } else {
            loadModules();
        }
    }

    public void loadModules() {
        if (getModuleManager().canLoad()) {
            getModuleManager().load(new ModuleManager.LoaderAction() {
                @Override
                public void onLoaded(boolean error) {
                    if (error) {
                        SnackBarUtils.show(Farmx_SplashActivity.this, "Please check your network connection ",
                                "Retry", new SnackBarUtils.Action() {
                                    @Override
                                    public void onClicked() {
                                        loadModules();
                                    }
                                });
                    } else {
                        getcrops();
                    }
                }
            });
        } else {
            getcrops();
        }
    }

    public void getcrops() {

        AndroidNetworking.get("http://vrjaitraders.com/ard_farmx/api/Agripedia/ciup")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        System.out.println("sdfsdfsdfdfdsa: \n" + jsonObject);
                        Webservice.Data_crops.clear();
                        Webservice.crops.clear();

                        try {

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            System.out.println("dfgdfgdf"+jsonArray);
                            getSessionManager().setCrops(jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {

                                Crops_Main obj = new Crops_Main();
                                JSONObject jobj = jsonArray.getJSONObject(i);
                                obj.setName(jobj.optString("crop_name").trim());
                                obj.setIcon("https://vrjaitraders.com/ard_farmx/" + jobj.optString("crop_icons_images").trim());
                                obj.setCrop_id(jobj.optString("Basic_info_id").trim());
                                obj.setS_name(jobj.optString("scientifi" + "c_name").trim());
                                obj.setTempreture(jobj.optString("temperature").trim());
                                obj.setPollution("40-50");
                                obj.setHumidity(jobj.optString("humidity").trim());
                                obj.setMoisture(jobj.optString("soil_moisture").trim());
                                obj.setWaterph(jobj.optString("soil_ph").trim());
                                obj.setVarieties(GsonUtils.fromJson(jobj.getJSONArray("varieties").toString(),
                                        EMarketStorage.varietyListType));

                                Webservice.crops.add(jobj.optString("crop_name").trim());
                                Webservice.Data_crops.add(obj);
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        if (sm.getUser() != null || sm.getGuestId() != null) {

                            Intent i = new Intent(Farmx_SplashActivity.this, Main_Dashboard.class);
                            i.putExtra("state", "live");
                            startActivity(i);
                            finish();
                        } else {

                            boolean isFirstRun = sm.showIntro();

                            if (isFirstRun) {
                                Intent i = new Intent(Farmx_SplashActivity.this, IntroActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(Farmx_SplashActivity.this, Formx_Login_Activity.class);
                                startActivity(i);
                                finish();
                            }

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        SnackBarUtils.show(Farmx_SplashActivity.this, "Please check your network connection ",
                                "Retry", new SnackBarUtils.Action() {
                                    @Override
                                    public void onClicked() {
                                        getcrops();
                                    }
                                });

                    }
                });
    }
}
