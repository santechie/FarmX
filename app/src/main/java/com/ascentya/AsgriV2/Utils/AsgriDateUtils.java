package com.ascentya.AsgriV2.Utils;

import java.sql.Timestamp;
import java.util.Date;

public class AsgriDateUtils {

    public static String getDateFromTimestamp(String timestamp){
        Timestamp time = Timestamp.valueOf(timestamp);
        return new Date(time.getTime()).toString().split("GMT")[0].trim();
    }

    public static String millisecondsToTime(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        String minuteStr = Long.toString(minutes);
        String secondsStr = Long.toString(seconds);
        String mins, secs;

        if (minuteStr.length() < 2){
            mins = "0" + minuteStr;
        }else {
            mins = minuteStr;
        }

        if (secondsStr.length() >= 2) {
            secs = secondsStr.substring(0, 2);
        } else {
            secs = "0" + secondsStr;
        }

        return mins + ":" + secs;
    }
}
