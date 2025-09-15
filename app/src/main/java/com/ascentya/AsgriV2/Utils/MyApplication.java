package com.ascentya.AsgriV2.Utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.UserPrivilege;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.bug_report.ReportUpdater;
import com.ascentya.AsgriV2.managers.AccessManager;
import com.ascentya.AsgriV2.stripe.StripeInstaller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;


/**
 * Created by Vengat G on img1/22/2019.
 */

public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {


    private Thread.UncaughtExceptionHandler defaultExceptionHandler;

    private AccessManager accessManager;

    @Override
    public void onCreate() {
        super.onCreate();

        accessManager = AccessManager.getInstance(this);

        checkCrops();
        checkPrivileges();

        StripeInstaller.init(this);
        AndroidNetworking.initialize(getApplicationContext(), getUnsafeOkHttpClient());
        AndroidNetworking.enableLogging();

        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void checkCrops(){
        if (Webservice.Data_crops.isEmpty() || Webservice.crops.isEmpty()){
            SessionManager sessionManager = new SessionManager(this);
            String cropsJsonArray = sessionManager.getCrops();
            if (cropsJsonArray != null){
                try {
                    JSONArray jsonArray = new JSONArray(cropsJsonArray);
                    System.out.println("Crop Load From Memory!");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Crops_Main obj = new Crops_Main();
                        JSONObject jobj = jsonArray.getJSONObject(i);
                        obj.setName(jobj.optString("crop_name").trim());
                        obj.setIcon("https://vrjaitraders.com/ard_farmx/" + jobj.optString("crop_icons_images").trim());
                        obj.setCrop_id(jobj.optString("Basic_info_id").trim());
                        obj.setS_name(jobj.optString("scientific_name").trim());
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkPrivileges(){
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.getUser() != null && getAccessManager().getUserPrivilegeList().isEmpty()){
            ArrayList<UserPrivilege> userPrivileges = sessionManager.getUserPrivileges();
            if (!userPrivileges.isEmpty()) accessManager.setUserPrivilegeList(userPrivileges);
        }
    }

    public AccessManager getAccessManager(){
        return accessManager;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(120, TimeUnit.SECONDS);
            builder.readTimeout(120, TimeUnit.SECONDS);
            builder.writeTimeout(120, TimeUnit.SECONDS);
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {

                    return true;

                }
            });


            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {

        throwable.printStackTrace();
        try {

            ReportUpdater.BugReport bugReport = new ReportUpdater.BugReport();
            SessionManager sessionManager = new SessionManager(this);

            int index = 0;

            StackTraceElement[] stackTrace = Objects.requireNonNull(throwable.getCause()).getStackTrace();
            String fullClassName = stackTrace[index].getClassName();
            String className = fullClassName.substring(fullClassName
                    .lastIndexOf(".") + 1);
            String methodName = stackTrace[index].getMethodName();
            int lineNumber = stackTrace[index].getLineNumber();

            bugReport.userId = sessionManager.getUser().getId();
            bugReport.os = Build.VERSION.SDK;
            bugReport.device = Build.MANUFACTURER;
            bugReport.model = Build.MODEL;
            bugReport.className = className + " / " + methodName;
            bugReport.lineNumber = lineNumber + "";
            bugReport.exception = throwable.toString();
            Log.i("BugReport", GsonUtils.getGson().toJson(bugReport));
            ReportUpdater.sendReport(this, bugReport);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (defaultExceptionHandler != null)
            defaultExceptionHandler.uncaughtException(thread, throwable);
    }
}
