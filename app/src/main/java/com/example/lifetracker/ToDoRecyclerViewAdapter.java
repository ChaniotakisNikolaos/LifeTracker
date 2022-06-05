package com.example.lifetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoRecyclerViewAdapter extends ListAdapter<ToDoItem,ToDoRecyclerViewAdapter.MyViewHolder> {
    private Context context;

    protected ToDoRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<ToDoItem> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ToDoRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoRecyclerViewAdapter.MyViewHolder holder, int position) {
        ToDoItem toDoItem = getItem(position);
        holder.checkBox.setText(toDoItem.getDescription());
        holder.labelTextView.setText(toDoItem.getLabel());
        holder.reminderTextView.setText(toDoItem.getReminder());
        holder.dueDateTextView.setText(toDoItem.getDueDate());
    }

    static class ToDoDiff extends DiffUtil.ItemCallback<ToDoItem> {

        @Override
        public boolean areItemsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getDescription().equals(newItem.getDescription()) && oldItem.getDueDate().equals(newItem.getDueDate()) && oldItem.getLabel().equals(newItem.getLabel()) && oldItem.getReminder().equals(newItem.getReminder());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView labelTextView, reminderTextView, dueDateTextView;
        CheckBox checkBox;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.textViewLabel);
            reminderTextView = itemView.findViewById(R.id.reminderTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        static MyViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view_item, parent, false);
            return new MyViewHolder(view);
        }
    }
}
