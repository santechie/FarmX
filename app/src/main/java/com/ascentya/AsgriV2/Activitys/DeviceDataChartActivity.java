package com.ascentya.AsgriV2.Activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.DeviceData;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class DeviceDataChartActivity extends BaseActivity implements DateUtils.DateOptions.Action {

    private int type = TIME;

    private ViewDialog viewDialog;
    private ImageView calendarIv;
    private TextView dateOptionTv;
    private LineChart temperatureChart, humidityChart, phChart, moistureChart, visibilityChart;

    private DeviceData deviceData;
    private int selectedDateOptionIndex = 0;

    private ArrayList<DeviceData> deviceDataList = new ArrayList<>();
    private ArrayList<DateUtils.DateOptions> dateOptionList = DateUtils.getDateOption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_data_chart);

        setToolbarTitle("Weather Chart", true);

        if (savedInstanceState == null) {
            deviceData = GsonUtils.getGson()
                    .fromJson(getIntent()
                                    .getStringExtra("device_data"),
                    DeviceData.class);
        }else {
            deviceData = GsonUtils.getGson()
                    .fromJson(savedInstanceState
                                    .getString("device_data"),
                    DeviceData.class);
        }

        String type = deviceData.getDeviceType()
                .equals("master") ? "Master" : "Slave";

        ((TextView) findViewById(R.id.deviceName))
                .setText(Html.fromHtml(type + " <b>" + deviceData.getDeviceId() + "</b>"));

        viewDialog = new ViewDialog(this);
        calendarIv = findViewById(R.id.calender);
        dateOptionTv = findViewById(R.id.dateOption);

        temperatureChart = findViewById(R.id.temperatureChart);
        humidityChart = findViewById(R.id.humidityChart);
        phChart = findViewById(R.id.phChart);
        moistureChart = findViewById(R.id.moistureChart);
        visibilityChart = findViewById(R.id.visibilityChart);

        calendarIv.setOnClickListener(view -> showDatePicker());
        dateOptionTv.setOnClickListener(view -> openDateOptionsDialog());

        setupChart(temperatureChart, 15f, 60f, 20f, 25f);
        setupChart(humidityChart, 0f, 100f, 50f, 60f);
        setupChart(phChart, 0f, 10f, 4f, 6f);
        setupChart(moistureChart, 0f, 100f, 10f, 30f);
        setupChart(visibilityChart, 0f, 100f, 5f, 95f);

        updateDateOption();
//        loadHistory();
    }

    private void loadHistory() {
        deviceDataList.clear();
        viewDialog.showDialog();

        String fromDate = DateUtils.getServerDateFormat(dateOptionList.get(selectedDateOptionIndex).getStart());
        String toDate = DateUtils.getServerDateFormat(dateOptionList.get(selectedDateOptionIndex).getEnd());

        System.out.println("D# Device Id: " + deviceData.getDeviceId());
        System.out.println("D# Start Date: " + fromDate);
        System.out.println("D# End Date: " + toDate);

        AndroidNetworking
                .post(Webservice.getDeviceData)
                .addUrlEncodeFormBodyParameter("device_id", deviceData.getDeviceId() + "")
                .addUrlEncodeFormBodyParameter("from_date", fromDate)
                .addUrlEncodeFormBodyParameter("to_date", toDate)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("getDeviceData:\n" + response);
                viewDialog.hideDialog();
                deviceDataList.clear();
                try {
                    if (response.getBoolean("status")) {
                        JSONArray jsonArray = response.optJSONArray("data");
                        deviceDataList.addAll(GsonUtils.getGson()
                                .fromJson(jsonArray.toString(), EMarketStorage.deviceDataToken));
                        //Toast.makeText(DeviceDataChartActivity.this, "Data: " + deviceDataList.size(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toasty.normal(DeviceDataChartActivity.this, response.getString("message")).show();
                    }
                    updateHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                viewDialog.hideDialog();
                Toasty.error(DeviceDataChartActivity.this,
                        "Device Data Load Error!").show();
            }
        });
    }

    private void updateHistory() {

//        setData(temperatureChart, 50, 100);
//        setData(humidityChart, 50, 100);
//        setData(phChart, 50, 100);
//        setData(moistureChart, 50, 100);
//        setData(visibilityChart, 50, 100);

        setData(DeviceDataActivity.TEMPERATURE);
        setData(DeviceDataActivity.HUMIDITY);
        setData(DeviceDataActivity.PH);
        setData(DeviceDataActivity.MOISTURE);
        setData(DeviceDataActivity.VISIBILITY);
    }

    public static void open(Context context, DeviceData deviceData) {
        Intent intent = new Intent(context, DeviceDataChartActivity.class);
        intent.putExtra("device_data", GsonUtils.getGson().toJson(deviceData));
        context.startActivity(intent);
    }

    private void openDateOptionsDialog() {
        new SelectItemDialog("History", (List<Object>) (List<?>) dateOptionList, position -> {
            selectedDateOptionIndex = position;
            updateDateOption();
        }).show(getSupportFragmentManager(), "SelectItemDialog");
    }

    private void updateDateOption() {
        final DateUtils.DateOptions dateOptions = dateOptionList.get(selectedDateOptionIndex);
        dateOptionTv.setText(dateOptions.getName());
        dateOptions.pickDate(getSupportFragmentManager(), this);
        updateCalenderBtn();
    }

    @Override
    public void onDatePicked() {
        switch (dateOptionList.get(selectedDateOptionIndex).getType()){
            case DateUtils.TODAY: type = TIME;
            case DateUtils.YESTERDAY: type = TIME;
                break;
            case DateUtils.THIS_WEEK: type = DAY;
            case DateUtils.LAST_WEEK: type = DAY;
                break;
            case DateUtils.THIS_MONTH: type = WEEK;
            case DateUtils.LAST_MONTH: type = WEEK;
                break;
            default: type = YEAR;
                break;
        }
        //Toasty.normal(this, "Type: " + type).show();
//        DateUtils.DateOptions dateOptions = dateOptionList.get(selectedDateOptionIndex);
       /* Toasty.normal(DeviceDataChartActivity.this,
                dateOptions.getStart() + " - " + dateOptions.getEnd()).show();*/
        loadHistory();
    }

    private void updateCalenderBtn() {
        DateUtils.DateOptions dateOptions = dateOptionList.get(selectedDateOptionIndex);
        calendarIv.setVisibility(
                dateOptions.getType().equals(DateUtils.CUSTOM) ?
                        View.VISIBLE : View.INVISIBLE);
    }

    private void showDatePicker() {
        DateUtils.DateOptions dateOptions = dateOptionList.get(selectedDateOptionIndex);
        dateOptions.pickDate(getSupportFragmentManager(), this);
    }

    private void setupChart(LineChart lineChart, float maximum, float minimum, float rangeMinimum, float rangeMaximum) {
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter(new MyValueFormatter(new Action() {
            @Override
            public int getType() {
                return type;
            }
        }));
        lineChart.getAxisRight().setEnabled(false);

        /*YAxis yAxis;
        {
            yAxis = lineChart.getAxisLeft();
            lineChart.getAxisRight().setEnabled(false);
            yAxis.enableGridDashedLine(10f, 10f, 0f);
            yAxis.setAxisMinimum(minimum);
            yAxis.setAxisMaximum(maximum);
        }*/

       /* {
            LimitLine llXAxis = new LimitLine(rangeMinimum, "Minimum");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            llXAxis.setTextSize(10f);

            LimitLine ll2XAxis = new LimitLine(rangeMinimum, "Maximum");
            ll2XAxis.setLineWidth(4f);
            ll2XAxis.enableDashedLine(10f, 10f, 0);
            ll2XAxis.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            ll2XAxis.setTextSize(10f);

            yAxis.addLimitLine(llXAxis);
            yAxis.addLimitLine(ll2XAxis);
        }*/

        lineChart.animateX(1500);

//        Legend legend = lineChart.getLegend();
//
//        legend.setForm(Legend.LegendForm.LINE);
    }

    private void setData(String type) {

        switch (type) {
            case DeviceDataActivity.TEMPERATURE:
                resetData(temperatureChart, getEntries(type));
                break;
            case DeviceDataActivity.HUMIDITY:
                resetData(humidityChart, getEntries(type));
                break;
            case DeviceDataActivity.PH:
                resetData(phChart, getEntries(type));
                break;
            case DeviceDataActivity.MOISTURE:
                resetData(moistureChart, getEntries(type));
                break;
            case DeviceDataActivity.VISIBILITY:
                resetData(visibilityChart, getEntries(type));
                break;
        }
    }

    private void resetData(LineChart lineChart, LineData lineData) {
        try {
            if (lineChart.getData() != null)
                lineChart.clearValues();
            lineChart.setData(lineData);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private LineData getEntries(String type) {
        ArrayList<Entry> entries = new ArrayList<>();
        int i = 0;
        boolean isSameDay = isSameDate();
        for (DeviceData deviceData : deviceDataList) {
            switch (type) {
                case DeviceDataActivity.TEMPERATURE:
                    entries.add(new Entry(getEntryKey(deviceData.getCreatedAt(), isSameDay),
                            deviceData.getTemperature()));
                    break;
                case DeviceDataActivity.HUMIDITY:
                    entries.add(new Entry(getEntryKey(deviceData.getCreatedAt(), isSameDay),
                            deviceData.getHumidity()));
                    break;
                case DeviceDataActivity.PH:
                    entries.add(new Entry(getEntryKey(deviceData.getCreatedAt(), isSameDay),
                            deviceData.getPh()));
                    break;
                case DeviceDataActivity.MOISTURE:
                    entries.add(new Entry(getEntryKey(deviceData.getCreatedAt(), isSameDay),
                            deviceData.getSoilMoisture()));
                    break;
                case DeviceDataActivity.VISIBILITY:
                    entries.add(new Entry(getEntryKey(deviceData.getCreatedAt(), isSameDay),
                            deviceData.getLight()));
                    break;
            }
        }
        LineDataSet set1 = new LineDataSet(entries, "");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        // create a data object with the data sets
        LineData data = new LineData(dataSets);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setDrawValues(false);
        data.setHighlightEnabled(false);
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(false);
        set1.setDrawIcons(false);
        set1.setDrawValues(false);
        set1.setColor(Color.BLACK, 20);
        set1.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.chart);
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(ContextCompat.getColor(this, R.color.green_farmx));
        }
        System.out.println("Chart Data: " + GsonUtils.getGson().toJson(entries));
        return data;
    }

    private float getEntryKey(String date, boolean isSameDate) {
        long dateLong = DateUtils.getDateLong(date);
        //System.out.println(date + ": " + dateLong);
        return (float) (dateLong / 1000f);
        /*if (isSameDate) {
            return Float.parseFloat(DateUtils.splitRailwayTime(date).split(":")[0]);
        } else {
            return Float.parseFloat(DateUtils.splitDay(date));
        }*/
    }

    private boolean isSameDate() {
        boolean sameDate = true;
        ArrayList<String> splitDateList = new ArrayList<>();
        for (DeviceData deviceData : deviceDataList) {
            splitDateList.add(DateUtils.splitDate(deviceData.getCreatedAt()));
        }
        String lastDate = splitDateList.isEmpty() ? "" : splitDateList.get(0);
        for (String date : splitDateList) {
            sameDate = sameDate && lastDate.equals(date);
            lastDate = date;
        }
        System.out.println("Same Date: " + sameDate);
        return sameDate;
    }

    private void setData(LineChart lineChart, int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
//            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();

        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawValues(false);
            set1.setDrawIcons(false);

//            set1.setMode(LineDataSet.Mode.LINEAR);
//            set1.setDrawIcons(false);

            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
//            set1.setColor(Color.BLACK);
//            set1.setCircleColor(Color.BLACK);
//            set1.setMode(LineDataSet.Mode.LINEAR);

            // line thickness and point size
//            set1.setLineWidth(1f);
//            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);
            set1.setDrawCircles(false);
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setColor(Color.BLACK, 20);

            // customize legend entry
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);

            // text size of values
//            set1.setValueTextSize(9f);

            // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(
                    true
            );

            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return lineChart.getAxisLeft().getAxisMinimum();
                }
            });

            set1.setValueFormatter(new DefaultValueFormatter(0));

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.green_drawable);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(ContextCompat.getColor(this, R.color.green_farmx_trans));
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);
            data.setValueFormatter(new DefaultValueFormatter(0));
            data.setDrawValues(false);
            data.setHighlightEnabled(false);

            // set data
            lineChart.setData(data);
        }
    }

    class MyValueFormatter extends IndexAxisValueFormatter {

        private Action action;

        public MyValueFormatter(Action action){
            this.action = action;
        }

        @Override
        public String getFormattedValue(float value) {

            // Convert float value to date string
            // Convert from seconds back to milliseconds to format time  to show to the user
            long emissionsMilliSince1970Time = ((long) value) * 1000;

            String dateFormat = "HH:mm";

            switch (action.getType()){
                case YEAR:
                    dateFormat = "MMM-yy";
                    break;
                case MONTH:
                    dateFormat = "dd-MMM";
                    break;
                case WEEK:
                    dateFormat = "EEE dd";
                    break;
                case DAY:
                    dateFormat = "EEE dd";
                    break;
                case TIME:
                    dateFormat = "HH:mm";
                    break;
            }

            // Show time in local version
            Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
            DateFormat dateTimeFormat = new SimpleDateFormat(dateFormat);

            return dateTimeFormat.format(timeMilliseconds);
        }
    }

    private static final int YEAR = 1;
    private static final int MONTH = 2;
    private static final int WEEK = 3;
    private static final int DAY = 4;
    private static final int TIME = 5;

    interface Action{
        int getType();
    }
}