package com.example.lifetracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.text.MessageFormat;
import java.util.Calendar;

//shows a date picker dialog
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String ARG_VIEW_ID = "id";
    private int viewId;//viewId is used for determining whether it was called by the reminder textview(in order to also show the time picker fragment)

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(int viewId) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_VIEW_ID, viewId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewId = getArguments().getInt(ARG_VIEW_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        TextView textView = requireActivity().findViewById(viewId);
        textView.setText(MessageFormat.format("{0}/{1}/{2}", day, month+1, year));//month+1 because otherwise months start from 0
        //if it was called by reminder text view then show timePickerFragment
        if (viewId == R.id.reminderSelectTextView) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(requireActivity().getSupportFragmentManager(), "timePicker");
        }
    }
}