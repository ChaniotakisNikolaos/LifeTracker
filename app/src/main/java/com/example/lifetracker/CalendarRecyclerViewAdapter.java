package com.example.lifetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class CalendarRecyclerViewAdapter extends RecyclerView.Adapter<CalendarRecyclerViewAdapter.CalendarViewHolder> {
    OnDateChangedListener listener;

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CalendarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_calendar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setOnDateChangedListener(OnDateChangedListener listener) {
        this.listener = listener;
    }

    public interface OnDateChangedListener {
        void onDateChange(String date);
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder {

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            CalendarView calendarView = itemView.findViewById(R.id.calendarView);
            //when the date is changed in the calendar
            calendarView.setOnDateChangeListener((calendarView1, year, month, day) -> {
                String date = day + "/" + (month + 1) + "/" + year;//month+1 because otherwise months start from 0
                listener.onDateChange(date);
            });
            //when calendar is created and shows the current date the listener needs to be called(otherwise the todos and budget of that day will not appear)
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            listener.onDateChange(day + "/" + (month + 1) + "/" + year);//month+1 because otherwise months start from 0
        }

    }
}
