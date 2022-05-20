package com.example.lifetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToDoRecyclerViewAdapter extends RecyclerView.Adapter<ToDoRecyclerViewAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<ToDoItem> toDoItemArrayList;

    public ToDoRecyclerViewAdapter(Context context, ArrayList<ToDoItem> toDoItemArrayList) {
        this.context = context;
        this.toDoItemArrayList = toDoItemArrayList;
    }

    @NonNull
    @Override
    public ToDoRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item,parent,false);
        return new ToDoRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoRecyclerViewAdapter.MyViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView labelTextView, reminderTextView, dueDateTextView;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.textViewLabel);
            reminderTextView = itemView.findViewById(R.id.reminderTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
