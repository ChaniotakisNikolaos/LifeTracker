package com.example.lifetracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoRecyclerViewAdapter extends ListAdapter<ToDoItem,ToDoRecyclerViewAdapter.MyViewHolder> {
    ApplicationViewModel applicationViewModel;

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
        if(toDoItem.getReminder().isEmpty()){
            holder.reminderTextView.setVisibility(View.INVISIBLE);
        }
        if(toDoItem.getDueDate().isEmpty()){
            holder.dueDateTextView.setVisibility(View.INVISIBLE);
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
                        if(menuItem.getTitle().toString().equals("Edit")){

                        }else{
                            int adapterPosition=holder.getAdapterPosition();
                            Log.d("adapterPosition",String.valueOf(adapterPosition));
                            if(adapterPosition>RecyclerView.NO_POSITION) {
                                applicationViewModel.delete(getItem(adapterPosition));

                            }else {
                                Log.d("test","NO_POSITION");
                            }
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView labelTextView, reminderTextView, dueDateTextView;
        CheckBox checkBox;
        ImageButton imageButton;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.textViewLabel);
            reminderTextView = itemView.findViewById(R.id.reminderTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
            imageButton = itemView.findViewById(R.id.imageButton);
        }

        static MyViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            return new MyViewHolder(view);
        }
    }

    public void setApplicationViewModel(ApplicationViewModel applicationViewModel) {
        this.applicationViewModel = applicationViewModel;
    }
}
