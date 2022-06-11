package com.example.lifetracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ToDoRecyclerViewAdapter extends ListAdapter<ToDoItem,ToDoRecyclerViewAdapter.MyViewHolder> {
    ApplicationViewModel applicationViewModel;
    MenuItemClickListener listener;

    protected ToDoRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<ToDoItem> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ToDoRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoRecyclerViewAdapter.MyViewHolder holder, int position) {
        ToDoItem toDoItem = getItem(position);
        if(toDoItem.getReminder().isEmpty()){
            holder.reminderTextView.setVisibility(View.INVISIBLE);
        }else{
            holder.reminderTextView.setVisibility(View.VISIBLE);
        }
        if(toDoItem.getDueDate().isEmpty()){
            holder.dueDateTextView.setVisibility(View.INVISIBLE);
        }else{
            holder.dueDateTextView.setVisibility(View.VISIBLE);
        }
        holder.checkBox.setText(toDoItem.getDescription());
        holder.labelTextView.setText(toDoItem.getLabel());
        holder.reminderTextView.setText(toDoItem.getReminder());
        holder.dueDateTextView.setText(toDoItem.getDueDate());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_to_do_item, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int adapterPosition=holder.getAdapterPosition();//get the current position of the to do item
                        if (adapterPosition==RecyclerView.NO_POSITION) //check if the position is valid
                            return false; //Position is not valid, ignore click
                        if(menuItem.getTitle().charAt(0)=='E'){ //Find out which menuItem was pressed (true for Edit, false for Delete)
                            listener.onEditClick(getItem(adapterPosition));
                        }else{
                            ToDoItem toDoItem = getItem(adapterPosition);
                            //cancel notification
                            Intent alarmIntent = new Intent(view.getContext(), ReminderReceiver.class);
                            //alarmIntent.putExtra("name", toDoItem.getDescription());
                            //alarmIntent.putExtra("id", toDoItem.getId());
                            AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), toDoItem.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.cancel(pendingIntent);

                            applicationViewModel.delete(toDoItem);
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    static class ToDoDiff extends DiffUtil.ItemCallback<ToDoItem> {

        @Override
        public boolean areItemsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getDescription().equals(newItem.getDescription()) && oldItem.getDueDate().equals(newItem.getDueDate()) && oldItem.getLabel().equals(newItem.getLabel()) && oldItem.getReminder().equals(newItem.getReminder());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView labelTextView, reminderTextView, dueDateTextView;
        CheckBox checkBox;
        ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.textViewLabel);
            reminderTextView = itemView.findViewById(R.id.reminderTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
            imageButton = itemView.findViewById(R.id.imageButton);
        }

         MyViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            return new MyViewHolder(view);
        }

    }

    public void setApplicationViewModel(ApplicationViewModel applicationViewModel) {
        this.applicationViewModel = applicationViewModel;
    }

    public interface MenuItemClickListener{
        void onEditClick(ToDoItem toDoItem);
    }

    public void setMenuItemClickListener(MenuItemClickListener listener){
        this.listener=listener;
    }
}
