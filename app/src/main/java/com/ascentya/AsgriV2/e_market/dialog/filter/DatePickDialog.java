package com.ascentya.AsgriV2.e_market.dialog.filter;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickDialog {

    public DatePickDialog(FragmentManager fragmentManager,
                          String title,
                          Long selectedDateStart, Long selectedDateEnd,
                          Long startDate, Long endDate,
                          Action action) {

        printDate("Start Limit", startDate);
        printDate("End Limit", endDate);

        CalendarConstraints calendarConstraints = new CalendarConstraints.Builder()
                .setStart(startDate).setEnd(endDate).build();

        MaterialDatePicker datePickerDialog = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(title)
                .setCalendarConstraints(calendarConstraints)
            .setSelection(new Pair(selectedDateStart, selectedDateEnd))
            .build();

        datePickerDialog.addOnPositiveButtonClickListener(selection -> {
            if (selection instanceof Pair){
                Long rawStartDate = ((Pair<Long, Long>) selection).first;
                Long rawEndDate = ((Pair<Long, Long>) selection).second;

               if (rawStartDate < startDate)
                   rawStartDate = startDate;

               if (rawEndDate > endDate)
                   rawEndDate = endDate;

                action.onDateSelected(rawStartDate, rawEndDate);
                printDate("Selected Start", ((Pair<Long, Long>) selection).first);
                printDate("Selected End", ((Pair<Long, Long>) selection).second);
            }
        });

        datePickerDialog.show(fragmentManager, title);

//        printDate("Start Date", startDate);
//        printDate("End Date", endDate);
//        printDate("Selected Start Date", selectedDateStart);
//        printDate("Selected End Date", selectedDateEnd);

    }

    private void printDate(String title, Long millis){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("\n" + title + "\nDate Millis" + millis);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        System.out.println(formatter.format(calendar.getTime()) + "\n");
    }

    public interface Action{
        void onDateSelected(Long startDate, Long endDate);
    }
}