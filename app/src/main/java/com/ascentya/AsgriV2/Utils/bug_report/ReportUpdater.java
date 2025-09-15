package com.ascentya.AsgriV2.Utils.bug_report;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONObject;

public class ReportUpdater extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");

        try {

            BugReport bugReport = GsonUtils.getGson()
                    .fromJson(intent.getStringExtra("bug_report"), BugReport.class);
            AndroidNetworking.post(Webservice.postBugReport)
                    .addUrlEncodeFormBodyParameter("user_id", bugReport.userId)
                    .addUrlEncodeFormBodyParameter("device", bugReport.device)
                    .addUrlEncodeFormBodyParameter("model", bugReport.model)
                    .addUrlEncodeFormBodyParameter("os", bugReport.os)
                    .addUrlEncodeFormBodyParameter("class_name", bugReport.className)
                    .addUrlEncodeFormBodyParameter("exception", bugReport.exception)
                    .addUrlEncodeFormBodyParameter("line_number", bugReport.lineNumber)
                    //.addApplicationJsonBody(new JSONObject(intent.getStringExtra("bug_report")))
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("BugReport", response.toString());
                    cancelAlarm(context);
                    release(wakeLock);
                }

                @Override
                public void onError(ANError anError) {
                    Log.i("BugReport Error", GsonUtils.getGson().toJson(anError));
                    cancelAlarm(context);
                    release(wakeLock);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            cancelAlarm(context);
            release(wakeLock);
        }
    }

    public void release(PowerManager.WakeLock wakeLock){
        if (wakeLock.isHeld())
            wakeLock.release();
    }

    public static void sendReport(Context context, BugReport bugReport)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, ReportUpdater.class);
        i.putExtra("bug_report", GsonUtils.getGson().toJson(bugReport));
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
    }

    public static void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, ReportUpdater.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public static class BugReport{
        public String userId, device, model, os, exception, className, lineNumber;
    }
}
