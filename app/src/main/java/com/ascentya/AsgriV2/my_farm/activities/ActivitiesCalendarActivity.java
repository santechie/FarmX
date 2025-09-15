package com.ascentya.AsgriV2.my_farm.activities;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.Edit_Activity;
import com.ascentya.AsgriV2.dialog.CustomClickableCalender.CalendarCustomView;
import com.ascentya.AsgriV2.dialog.CustomClickableCalender.EventObjects;
import com.ascentya.AsgriV2.Models.Activity_Cat_Model;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.LogUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.my_farm.adapters.ActivitiesAdapter;
import com.events.calendar.views.EventsCalendar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ActivitiesCalendarActivity
        extends BaseActivity
        implements EventsCalendar.Callback, ActivitiesAdapter.Action {

    private View calendarView;
    private CalendarCustomView customCalendar;
    private LinearLayout calendarParent;
    private EventsCalendar calendar;

    private String landId = null, cropId = null, varietyId = null;
    private Crops_Main crop;

    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private ArrayList<Crops_Main> crops = new ArrayList<>();
    private int selectedLandIndex = 0, selectedCropIndex = 0, selectedMenu = 0;

    private RecyclerView activityRecyclerView;
    private ActivitiesAdapter activitiesAdapter;
    private ArrayList<Activity_Cat_Model> activityList = new ArrayList<>();
    private ArrayList<Activity_Cat_Model> selectedActivityList = new ArrayList<>();

    private Calendar selectedDate, selectedMonth;

    private CalendarCustomView.Action calendarAction = new CalendarCustomView.Action() {
        @Override
        public void onDateClicked(Calendar date) {
            selectedDate = date;
            updateActivities();
//            Toasty.normal(ActivitiesCalendarActivity.this, "Date Changed: " + date.getTime()).show();
        }

        @Override
        public void onMonthChanged(Calendar month) {
            selectedMonth = month;
            updateActivities();
//            Toasty.normal(ActivitiesCalendarActivity.this, "Month Changed: " + month.getTime().getMonth()).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_calendar);

        setToolbarTitle(getString(R.string.activities), true);

        landId = getIntent().getStringExtra("land_id");
        cropId = getIntent().getStringExtra("crop_id");
        varietyId = getIntent().getStringExtra("variety_id");
        crop = GsonUtils.fromJson(getIntent().getStringExtra("crop"), Crops_Main.class);

        calendarView = findViewById(R.id.calendarView);
        calendarParent = findViewById(R.id.calendarParent);
        calendar = findViewById(R.id.eventsCalendar);

        Calendar today = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
//        start.add(Calendar.MONTH, -6);
        Calendar end = Calendar.getInstance();
//        end.add(Calendar.MONTH, +6);

        calendar.setSelectionMode(calendar.getMULTIPLE_SELECTION()) //set mode of Calendar
                .setToday(today) //set today's date [today: Calendar]
                .setMonthRange(start, end) //set starting month [start: Calendar] and ending month [end: Calendar]
                .setWeekStartDay(Calendar.SUNDAY, false) //set start day of the week as you wish [startday: Int, doReset: Boolean]
                .setCurrentSelectedDate(today) //set current date and scrolls the calendar to the corresponding month of the selected date [today: Calendar]
//                .setDatesTypeface(typeface) //set font for dates
                .setDateTextFontSize(16f) //set font size for dates
//                .setMonthTitleTypeface(typeface) //set font for title of the calendar
                .setMonthTitleFontSize(16f) //set font size for title of the calendar
//                .setWeekHeaderTypeface(typeface) //set font for week names
                .setWeekHeaderFontSize(16f) //set font size for week names
                .setCallback(this) //set the callback for EventsCalendar
//                .addEvent(c) //set events on the EventsCalendar [c: Calendar]
//                .disableDate(dc) //disable a specific day on the EventsCalendar [c: Calendar]
                .disableDaysInWeek(Calendar.SATURDAY, Calendar.SUNDAY) //disable days in a week on the whole EventsCalendar [varargs days: Int]
                .build();

        findViewById(R.id.landContainer).setOnClickListener(v -> showLands());
        findViewById(R.id.cropContainer).setOnClickListener(v -> showCrops());

        activityRecyclerView = findViewById(R.id.recyclerView);
        activityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activitiesAdapter = new ActivitiesAdapter(this);
        activityRecyclerView.setAdapter(activitiesAdapter);

        setUpCalendar();
        loadLands();
    }

    private void setUpCalendar(){

        ViewGroup parent = (ViewGroup) calendarView.getParent();
        parent.removeView(calendarView);
        calendarParent.removeAllViews();
        calendarParent.setOrientation(LinearLayout.VERTICAL);

        ArrayList<EventObjects> events = new ArrayList<>();

        customCalendar = new CalendarCustomView(this, events);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        customCalendar.setLayoutParams(layoutParams);
        customCalendar.setAction(calendarAction);
        calendarParent.addView(customCalendar);
    }

    @Override
    public void onDayLongPressed(@Nullable Calendar calendar) {

    }

    @Override
    public void onDaySelected(@Nullable Calendar calendar) {
        System.out.println("Selected Day: " + calendar);
//        this.calendar.setCurrentSelectedDate(calendar);
    }

    @Override
    public void onMonthChanged(@Nullable Calendar calendar) {
        System.out.println("Month Changed: " + calendar);
    }

    private void showLands(){
        new SelectItemDialog(getString(R.string.lands), (List<Object>)(List<?>) lands, position -> {
            selectedLandIndex = position;
            updateLand();
        }).show(getSupportFragmentManager(), "lands");
    }

    private void showCrops(){
        new SelectItemDialog(getString(R.string.crops), (List<Object>)(List<?>) crops, position -> {
            selectedCropIndex = position;
            Toasty.normal(ActivitiesCalendarActivity.this,
                    "Lc Id: " + crops.get(selectedCropIndex).getLcId()).show();
            updateCrop();
        }).show(getSupportFragmentManager(), "lands");
    }

    private void loadLands(){
        ApiHelper.loadLands(getSessionManager().getUser().getId(), (response,lands, error) -> {

            if (UserHelper.checkResponse(ActivitiesCalendarActivity.this, response)){
                return;
            }
            ActivitiesCalendarActivity.this.lands.clear();
            ActivitiesCalendarActivity.this.lands.addAll(lands);
            updateLand();
        });
    }

    private boolean isFirstLand = true;
    private boolean isFirstCrop = true;

    private void updateLand(){
        if (lands.isEmpty()) return;
        if (isFirstLand && landId != null){
            isFirstLand = false;
            for (int i=0; i<lands.size(); i++){
                if (lands.get(i).getId().equals(landId)){
                    selectedLandIndex = i;
                }
            }
        }
        ((TextView) findViewById(R.id.landSpinner))
                .setText(lands.get(selectedLandIndex).getLand_name());
        selectedCropIndex = 0;
        loadCrops();
    }

    private void loadCrops(){
        if (lands.isEmpty()) return;
        crops.clear();
        crops.addAll(ApiHelper.getAllCrops(lands.get(selectedLandIndex)));
//        System.out.println("Crops: " + GsonUtils.toJson(crops));
        if (isFirstCrop && cropId != null){
            isFirstCrop = false;
            for (int i=0; i<crops.size(); i++){
                if (crops.get(i).getCrop_id().equals(cropId)){
                    String lcId = crops.get(i).getLcId();
                    if (lcId != null && crop != null){
                        if (crop.getLcId().equals(lcId)){
                            selectedCropIndex = i;
                        }
                    }else {
                        selectedCropIndex = i;
                    }
                }
            }
        }
        updateCrop();
    }

    private void updateCrop(){
        ((TextView) findViewById(R.id.cropSpinner))
                .setText(crops.get(selectedCropIndex).getName());
        onChange();
    }

    private void onChange(){
        loadActivities();
    }

    private void loadActivities(){
        showLoading();
        ApiHelper.loadActivities(sessionManager.getUser().getId(),
                lands.get(selectedLandIndex).getId(),
                crops.get(selectedCropIndex).getCrop_id(),
                "main_crop",
                crops.get(selectedCropIndex).getLcId(),
                "", new ApiHelper.ActivitiesAction() {
                    @Override
                    public void onResult(JSONObject response,ArrayList<Activity_Cat_Model> activityList, boolean error) {
                        hideLoading();

                        if (UserHelper.checkResponse(ActivitiesCalendarActivity.this, response)){
                            return;
                        }
                        if (!error){
                            ActivitiesCalendarActivity.this.activityList.clear();
                            ActivitiesCalendarActivity.this.activityList.addAll(activityList);
                            updateCalendar();
                        }else {
                            Toasty.error(ActivitiesCalendarActivity.this,
                                    "Activities Load Error!").show();
                        }
                    }
                });
    }

    private void updateCalendar(){
        long startDate = 0;
        long endDate = 0;
        for (Activity_Cat_Model activity: activityList){
            long tempStartDate = DateUtils.getLong(activity.getStart_date());
            if (startDate == 0 || startDate > tempStartDate){
                startDate = tempStartDate;
            }
            long tempEndDate = DateUtils.getLong(activity.getEnd_date());
            if (endDate == 0 || endDate < tempEndDate){
                endDate = tempEndDate;
            }
        }
        updateCalendar(startDate, endDate);
        updateEvents();
    }

    private void updateCalendar(long start, long end){
        LogUtils.log(getClass().getSimpleName(),
                "Start Date: " + DateUtils.getDateFromMillis(start));
        LogUtils.log(getClass().getSimpleName(),
                "End Date: " + DateUtils.getDateFromMillis(end));

        if (start != 0 && end != 0){
            Calendar startDate = Calendar.getInstance();
            startDate.setTimeInMillis(start);
            Calendar endDate = Calendar.getInstance();
            endDate.setTimeInMillis(end);
            calendar.setMonthRange(startDate, endDate);
            calendar.setCurrentSelectedDate(startDate);
            calendar.build();

            customCalendar.setRange(startDate, endDate);

        }else {
            calendar.setMonthRange(Calendar.getInstance(), Calendar.getInstance());
            calendar.build();
        }
        updateEvents();
    }

    private void updateEvents(){
        ArrayList<EventObjects> eventObjects = new ArrayList<>();
        calendar.clearEvents();
        for (Activity_Cat_Model activity: activityList){
            Calendar startDate = Calendar.getInstance();
            startDate.setTimeInMillis(DateUtils.getLong(activity.getStart_date()));
            Calendar endDate = Calendar.getInstance();
            endDate.setTimeInMillis(DateUtils.getLong(activity.getEnd_date()));
//            calendar.setSelectionMode(calendar.getMULTIPLE_SELECTION());
            boolean isSame = org.apache.commons.lang3.time.DateUtils.isSameDay(startDate, endDate);
            System.out.println("Event Dates: " + startDate + " - " + endDate);
            if (isSame){
                calendar.addEvent(startDate);
                EventObjects eventObject = new EventObjects(activity.getPrepare_type(), startDate.getTime());
                eventObject.setColor(ContextCompat.getColor(this,
                        activity.getStatus().equals(Constants.ActivityStatus.COMPLETED) ?
                            R.color.green_farmx : R.color.orange));
                eventObjects.add(eventObject);
            }else{
                calendar.addEvent((Calendar[]) Arrays.asList(startDate, endDate).toArray());
                while (!org.apache.commons.lang3.time.DateUtils.isSameDay(startDate.getTime(), endDate.getTime())){
                    EventObjects eventObject = new EventObjects(activity.getPrepare_type(), startDate.getTime());
                    eventObject.setColor(ContextCompat.getColor(this,
                            activity.getStatus().equals(Constants.ActivityStatus.COMPLETED) ?
                                    R.color.green_farmx : R.color.orange));
                    eventObjects.add(eventObject);
                    startDate.set(Calendar.HOUR, 24);
                }
                EventObjects eventObject = new EventObjects(activity.getPrepare_type(), endDate.getTime());
                eventObject.setColor(ContextCompat.getColor(this,
                        activity.getStatus().equals(Constants.ActivityStatus.COMPLETED) ?
                                R.color.green_farmx : R.color.orange));
                eventObjects.add(eventObject);
            }
        }
        calendar.build();
        customCalendar.setRangesOfDate(eventObjects);
        clearDates();
        selectedMonth = customCalendar.getCurrentMonth();
        updateActivities();
    }

    private void clearDates(){
        selectedDate = null;
        selectedMonth = null;
    }

    private void updateActivities(){
        if (selectedMonth != null){
            selectedActivityList.clear();
            for (Activity_Cat_Model activity: activityList){
                Calendar startDate = Calendar.getInstance();
                startDate.setTimeInMillis(DateUtils.getLong(activity.getStart_date()));
                Calendar endDate = Calendar.getInstance();
                endDate.setTimeInMillis(DateUtils.getLong(activity.getEnd_date()));
                if (DateUtils.isSameMonth(selectedMonth.getTime(), startDate.getTime())
                        || DateUtils.isSameMonth(selectedMonth.getTime(), endDate.getTime())){
                    selectedActivityList.add(activity);
                }
            }
            selectedMonth = null;
        }

        if (selectedDate != null){
            selectedActivityList.clear();
            for (Activity_Cat_Model activity: activityList){
                Calendar startDate = Calendar.getInstance();
                startDate.setTimeInMillis(DateUtils.getLong(activity.getStart_date()));
                Calendar endDate = Calendar.getInstance();
                endDate.setTimeInMillis(DateUtils.getLong(activity.getEnd_date()));
                if ((org.apache.commons.lang3.time.DateUtils.isSameDay(selectedDate.getTime(), startDate.getTime())
                        || org.apache.commons.lang3.time.DateUtils.isSameDay(selectedDate.getTime(), endDate.getTime())) ||
                        (startDate.getTime().getTime() <= selectedDate.getTime().getTime()
                                && endDate.getTime().getTime() >= selectedDate.getTime().getTime())){
                    selectedActivityList.add(activity);
                }
            }
            selectedDate = null;
        }

        activitiesAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<Activity_Cat_Model> getActivityList() {
        return selectedActivityList;
    }

    @Override
    public int getActivityIcon(String serviceId) {
        return Constants.Activities.getActivityImages(serviceId);
    }

    @Override
    public String getActivityName(String serviceId) {
        return Constants.Activities.getActivityName(serviceId);
    }

    @Override
    public void onActivityClicked(int position) {
        Intent i = new Intent(this, Edit_Activity.class);
        i.putExtra("act", selectedActivityList.get(position));
        i.putExtra("crop", selectedActivityList.get(position).getCrop_id());
        i.putExtra("land", selectedActivityList.get(position).getLand_id());
        i.putExtra("lc_id", selectedActivityList.get(position).getLc_id());
        i.putExtra("cat", selectedActivityList.get(position).getService_id());
        i.putExtra("crop_type", selectedActivityList.get(position).getCrop_type());
        startActivity(i);
    }

    @Override
    public void onUpdate(int position, boolean checked) {
        updateActivityStatus(selectedActivityList.get(position), checked);
    }

    private void updateActivityStatus(Activity_Cat_Model activity, boolean checked){
        showLoading();
        ApiHelper.updateActivityStatus(activity.getFence_id(),
                activity.getService_id(),
                checked ? Constants.ActivityStatus.COMPLETED : Constants.ActivityStatus.NOT_COMPLETED,
                new ApiHelper.CropUpdateAction(){
            @Override
            public void onResult(JSONObject response,boolean status, boolean error) {
                hideLoading();

                if (UserHelper.checkResponse(ActivitiesCalendarActivity.this, response)){
                    return;
                }
                if(status){
                    Toasty.success(ActivitiesCalendarActivity.this, "Activity Updated!").show();
                    loadActivities();
                }else {
                    Toasty.error(ActivitiesCalendarActivity.this, "Activity Update Failed!").show();
                    updateCalendar();
                }
            }
        });
    }
}