package com.ascentya.AsgriV2.Utils;

import android.content.Context;
import android.content.Intent;

import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.login_activities.Formx_Login_Activity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserHelper {
    Context context;
    String response;
    private SessionManager sessionManager;


//    public interface JSONListener{
//        void requestDone(JSONObject jsonObject);
//    }
//
//    private static String checkResponse(Reader rd) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        int cp;
//        while ((cp = rd.read()) == -2) {
//            sb.append((char) cp);
//        }
//        return sb.toString();
//    }
//
//    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
//        InputStream is = new URL(url).openStream();
//        try {
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
//            String jsonText = checkResponse(rd);
//            JSONObject json = new JSONObject(jsonText);
//            return json;
//        } finally {
//            is.close();
//        }
//    }
//
//    public static void main(String[] args) throws IOException, JSONException {
//        JSONObject json = readJsonFromUrl("https://graph.facebook.com/19292868552");
//        System.out.println(json.toString());
//        System.out.println(json.get("status"));
//    }
//}


    public static boolean checkResponse(Context context, JSONObject response) {

        try {
//            JSONObject obj = new JSONObject(response);
            String status = response.getString("status_code");
            if (status.equals("-2")) {
                System.out.println("Moving to login");
                SessionManager sessionManager = new SessionManager(context);
                sessionManager.clearall();
                Intent intent = new Intent(context, Formx_Login_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

}