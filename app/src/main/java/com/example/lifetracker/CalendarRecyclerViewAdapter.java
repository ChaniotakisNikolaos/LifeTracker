package com.example.lifetracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarRecyclerViewAdapter extends RecyclerView.Adapter<CalendarRecyclerViewAdapter.CalendarViewHolder>{
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

    public class CalendarViewHolder extends RecyclerView.ViewHolder{

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            CalendarView calendarView = itemView.findViewById(R.id.calendarView);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView1, int year, int month, int day) {
                    String date= day+"/"+(month+1)+"/"+year;
                    Log.d("seleted date",date);
                    listener.onDateChange(date);
                }
            });

        }

    }

    public interface OnDateChangedListener{
        void onDateChange(String date);
    }

    public void setOnDateChangedListener(OnDateChangedListener listener) {
        this.listener = listener;
    }
}
