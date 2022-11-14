package com.example.pbl6app.fragment;
/*
 * Created by tuyen.dang on 11/9/2022
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.example.pbl6app.Listeners.OnDateChoiceListener;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private OnDateChoiceListener onDateChoiceListener;

    public DatePickerFragment(OnDateChoiceListener onDateChoiceListener) {
        this.onDateChoiceListener = onDateChoiceListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the USER
        onDateChoiceListener.onDateChoice(day + "-" + (month+1) + "-" + year);
        this.dismiss();
    }
}