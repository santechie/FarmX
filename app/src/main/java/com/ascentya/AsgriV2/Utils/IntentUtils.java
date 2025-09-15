package com.ascentya.AsgriV2.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

public class IntentUtils {

    public static void openWhatsApp(Context context, String number){
        String url = "https://api.whatsapp.com/send?phone=" + number;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Whatsapp app not installed in your phone!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void sendWhatsAppMessage(Context context, String mobileNumber, String message){
        Intent In_Whats = new Intent(Intent.ACTION_VIEW);
        In_Whats.setData(Uri.parse(" https://api.whatsapp.com/send?phone=" + mobileNumber));
        context.startActivity(In_Whats);
    }

    public static void openCall(Context context, String number){
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.fromParts("tel", number, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
