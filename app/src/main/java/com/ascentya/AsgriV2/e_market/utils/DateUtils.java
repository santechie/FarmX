package com.ascentya.AsgriV2.e_market.utils;

import com.ascentya.AsgriV2.e_market.dialog.filter.DatePickDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.fragment.app.FragmentManager;

public class DateUtils {

    public static final String TODAY = "today";
    public static final String YESTERDAY = "yesterday";
    public static final String THIS_WEEK = "this_week";
    public static final String LAST_WEEK = "last_week";
    public static final String THIS_MONTH = "this_month";
    public static final String LAST_MONTH = "last_month";
    public static final String CUSTOM = "custom";

    public static final long ONE_MONTH_LONG_MILLS = TimeUnit.DAYS.toMillis(30);
    public static final long TWELE_MONTHS = 12 * ONE_MONTH_LONG_MILLS;

    public static String getDateFromMillis(Long millis){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return formatter.format(calendar.getTime());
    }

    public static Long getDateFromText(String dateText){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
        return date.getTime();
    }

    public static Long getDateLong(String dateText){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
        return date.getTime();
    }

    public static Long getLong(String yyyyMMdd){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(yyyyMMdd);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
        return date.getTime();
    }

    public static String splitDate(String dateText){
        try {
            System.out.println("D# Date: " + dateText);
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = f.parse(dateText);
            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            //DateFormat time = new SimpleDateFormat("hh:mm:ss a");
            String formattedDate = date.format(d);
            System.out.println("D# Split Date 1: " + formattedDate);
            return formattedDate;
//            System.out.println("Time: " + time.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
           /* try {
                DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date d = f.parse(dateText);
                DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                //DateFormat time = new SimpleDateFormat("hh:mm:ss a");
                String formattedDate = date.format(d);
                System.out.println("D# Formatted Date 2: " + formattedDate);
                return formattedDate;
            }catch (Exception e1){
                e.printStackTrace();
                e1.printStackTrace();*/
            /*if(dateText.split(" ").length == 1){
                try {
                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = f.parse(dateText);
                    DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                    //DateFormat time = new SimpleDateFormat("hh:mm:ss a");
                    String formattedDate = date.format(d);
                    System.out.println("D# Split Date 2: " + formattedDate);
                    return formattedDate;
                }catch (Exception e1){
                    e1.printStackTrace();
                    System.out.println("D# Split Date 4: " + dateText.split(" ")[0]);
                    return dateText.split(" ")[0];
                }
            }*/
            System.out.println("D# Split Date 3: " + dateText.split(" ")[0]);
            return dateText.split(" ")[0];
            /*}*/
        }
    }

    public static String getServerDateFormat(String dateString){
        try {
            Date date = new Date(dateString);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return DateUtils.splitDate(dateFormat.format(date.getTime())).replaceAll("/", "-");
        }catch (Exception e){
            e.printStackTrace();
            return dateString;
        }
    }

    public static String displayDate(String dateString) {
        try {
            //Date date = new Date(dateString);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            return dateFormat.format(dateString);
        }catch (Exception e){
            e.printStackTrace();
            return dateString;
        }
    }

    public static String displayDayAndMonth(String dateString){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(dateString);
            return new SimpleDateFormat("dd MMM").format(date);
        }catch (Exception e){
            e.printStackTrace();
            return dateString;
        }
    }

    public static String splitDay(String dateText){
        try {
            return splitDate(dateText).split("/")[0];
        }catch (Exception e){ e.printStackTrace(); }
        return "00";
    }

    public static String splitRailwayTime(String dateText){
        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = f.parse(dateText);
//            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat time = new SimpleDateFormat("HH:mm:ss");
//            return date.format(d);
            return time.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateText.split(" ")[1];
        }
    }

    public static String splitTime(String dateText){
        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = f.parse(dateText);
//            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat time = new SimpleDateFormat("hh:mm:ss a");
//            return date.format(d);
            return time.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateText.split(" ")[1];
        }
    }

    public static String getDate(String dateType, boolean isStart){
        String date = "";
        switch (dateType){
            case TODAY: date = getDate(new Date(), isStart).toString();
            break;
            case YESTERDAY: date = getDate(getYesterdayDate(), isStart).toString();
            break;
            case THIS_WEEK: if (isStart)
                date = getWeekStartDate().toString();
            else
                date = getWeekEndDate().toString();
            break;
            case LAST_WEEK: if (isStart)
                date = getLastWeekStartDate().toString();
            else
                date = getLastWeekEndDate().toString();
                break;
            case THIS_MONTH: if (isStart)
                date = getDate(getMonthStartDate(), true).toString();
            else
                date = getDate(getMonthEndDate(), false).toString();
            break;
            case LAST_MONTH: if (isStart)
                date = getDate(getLastMonthStartDate(), true).toString();
            else
                date = getDate(getLastMonthEndDate(), false).toString();
                break;
            case CUSTOM: date = null;
                break;
        }
        return date;
    }

    public static Date getYesterdayDate(){
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date getWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                calendar.add(Calendar.DATE, -1);
            }
        }
        return calendar.getTime();
    }

    public static Date getWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                calendar.add(Calendar.DATE, 1);
            }
            calendar.add(Calendar.DATE, -1);
        }else {
            calendar.add(Calendar.DATE, 6);
        }
        return calendar.getTime();
    }

    public static Date getLastWeekStartDate(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getWeekStartDate().getTime());
        c.add(Calendar.DATE, -7);
        return c.getTime();
    }

    public static Date getLastWeekEndDate(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getLastWeekStartDate().getTime());
        c.add(Calendar.DATE, 6);
        return c.getTime();
    }

    public static Date getMonthStartDate(){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static Date getMonthEndDate(){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return c.getTime();
    }

    private static Date getLastMonthStartDate(){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, -1);
        return c.getTime();
    }

    private static Date getLastMonthEndDate(){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
//        c.add(Calendar.MONTH, -1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return c.getTime();
    }

    private static Date getDate(Date date, boolean isStart){
        if (isStart) return atStartOfDay(date);
        else return atEndOfDay(date);
    }

    private static Date atEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    private static Date atStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String getDaysToText(String validity) {
        String text = "";
        try {
            int days = Integer.parseInt(validity);
            if (days >= 360){
                int year = days / 360;
                int remainingDays = days%360;
                text =  year + ((year == 1) ? " year" : " years");
                if (remainingDays > 0)
                    text += " " + getDaysToText(remainingDays + "");
            }else if (days >= 30){
                int months = days/30;
                int remainingDays = days%30;
                text = months + ((months == 1) ? " month" : " months");
                if (remainingDays > 0)
                    text += " " + getDaysToText(remainingDays + "");
            }else if (days >= 1){
                text = days + ((days == 1) ? " day" : " days");
            }
        }catch (Exception e){e.printStackTrace();}
        return text;
    }

    public static class DateOptions{
        private String name, type;
        private String selectedStart, selectedEnd;

        public DateOptions(String name, String type){
            this.name = name;
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStart(){
            if (type.equals(DateUtils.CUSTOM))
                return selectedStart;
            return DateUtils.getDate(type, true);
        }

        public String getEnd(){
            if (type.equals(DateUtils.CUSTOM))
                return selectedEnd;
            return DateUtils.getDate(type, false);
        }

        public String getName(){
            return name;
        }

        public void pickDate(FragmentManager fragmentManager, Action action){
            if (type.equals(DateUtils.CUSTOM)){
                new DatePickDialog(fragmentManager, "Select Date",
                        getStartDate().getTime(),
                        getEndDate().getTime(),
                        System.currentTimeMillis() - TWELE_MONTHS,
                        System.currentTimeMillis(),
                        new DatePickDialog.Action() {
                            @Override
                            public void onDateSelected(Long startDate, Long endDate) {
                                selectedStart = new Date(startDate).toString();
                                selectedEnd = new Date(endDate).toString();
                                action.onDatePicked();
                            }
                        });
            }else {
                action.onDatePicked();
            }
        }

        private Date getStartDate(){
            if (selectedStart == null)
                return new Date();
            return new Date(selectedStart);
        }

        private Date getEndDate(){
            if (selectedEnd == null)
                return new Date();
            return new Date(selectedEnd);
        }

        @Override
        public String toString() {
            return name;
        }

        public interface Action{
            void onDatePicked();
        }
    }

    public static ArrayList<DateOptions> getDateOption(){
        ArrayList<DateOptions> dateOptionList = new ArrayList<>();
        DateOptions todayOption = new DateOptions("Today", DateUtils.TODAY);
        DateOptions yesterdayOption = new DateOptions("Yesterday", DateUtils.YESTERDAY);
        DateOptions thisWeekOption = new DateOptions("This Week", DateUtils.THIS_WEEK);
        DateOptions lastWeekOption = new DateOptions("Last Week", DateUtils.LAST_WEEK);
        DateOptions thisMonth = new DateOptions("This Month", DateUtils.THIS_MONTH);
        DateOptions lastMonth = new DateOptions("Last Month", DateUtils.LAST_MONTH);
        DateOptions customOption = new DateOptions("Custom", DateUtils.CUSTOM);
        dateOptionList.add(todayOption);
        dateOptionList.add(yesterdayOption);
        dateOptionList.add(thisWeekOption);
        dateOptionList.add(lastWeekOption);
        dateOptionList.add(thisMonth);
        dateOptionList.add(lastMonth);
        dateOptionList.add(customOption);
        return dateOptionList;
    }

    public static boolean isSameMonth(Date dateOne, Date dateTwo){
        if (dateOne == null || dateTwo == null){
            return false;
        }

        return dateOne.getMonth() == dateTwo.getMonth()
                && dateOne.getYear() == dateTwo.getYear();
    }

    public static Calendar getCalendar(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getLong(date));
        return calendar;
    }

    public static Date getDate(String date){
        return getCalendar(date).getTime();
    }
}
