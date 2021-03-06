package com.example.lifetracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoRecyclerViewAdapter extends ListAdapter<ToDoItem, ToDoRecyclerViewAdapter.MyViewHolder> {
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
        holder.reminderTextView.setVisibility(toDoItem.getReminder().isEmpty() ? View.INVISIBLE : View.VISIBLE);
        holder.dueDateTextView.setVisibility(toDoItem.getDueDate().isEmpty() ? View.INVISIBLE : View.VISIBLE);
        holder.checkBox.setText(toDoItem.getDescription());
        holder.checkBox.setChecked(toDoItem.isSelected());
        if (getItem(holder.getBindingAdapterPosition()).isSelected()) {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);//strike to do text
            holder.cardView.setCardBackgroundColor(0xADF0E7F3);//make see through
        } else {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);//unstrike text
            holder.cardView.setCardBackgroundColor(0xFFF0E7F3);//make solid colour
        }
        holder.checkBox.setOnClickListener(v -> {
            int adapterPosition = holder.getBindingAdapterPosition();//get the current position of the to do item
            if (adapterPosition == RecyclerView.NO_POSITION) return;
            boolean checked = ((CheckBox) v).isChecked();
            ToDoItem newToDoItem = new ToDoItem(getItem(adapterPosition));
            newToDoItem.setSelected(checked);
            listener.onUpdate(newToDoItem);
        });
        holder.labelTextView.setText(toDoItem.getLabel());
        holder.reminderTextView.setText(toDoItem.getReminder());
        holder.dueDateTextView.setText(toDoItem.getDueDate());
        holder.imageButton.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.popup_to_do_item, popup.getMenu());
            popup.setOnMenuItemClickListener(menuItem -> {
                int adapterPosition = holder.getBindingAdapterPosition();//get the current position of the to do item
                if (adapterPosition == RecyclerView.NO_POSITION) //check if the position is valid
                    return false; //Position is not valid, ignore click
                if (menuItem.getTitle().charAt(0) == 'E') { //Find out which menuItem was pressed (true for Edit, false for Delete)
                    listener.onEditClick(getItem(adapterPosition));
                } else {
                    ToDoItem toDoItem1 = getItem(adapterPosition);
                    //cancel notification
                    Intent alarmIntent = new Intent(view.getContext(), ReminderReceiver.class);
                    AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), toDoItem1.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.cancel(pendingIntent);

                    applicationViewModel.delete(toDoItem1);
                }
                return false;
            });
            popup.show();
        });
    }

    public void setApplicationViewModel(ApplicationViewModel applicationViewModel) {
        this.applicationViewModel = applicationViewModel;
    }

    public void setMenuItemClickListener(MenuItemClickListener listener) {
        this.listener = listener;
    }

    public interface MenuItemClickListener {
        void onEditClick(ToDoItem toDoItem);

        void onUpdate(ToDoItem toDoItem);
    }

    static class ToDoDiff extends DiffUtil.ItemCallback<ToDoItem> {

        @Override
        public boolean areItemsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getDueDate().equals(newItem.getDueDate()) &&
                    oldItem.getLabel().equals(newItem.getLabel()) &&
                    oldItem.getReminder().equals(newItem.getReminder()) &&
                    oldItem.isSelected() == newItem.isSelected();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView labelTextView, reminderTextView, dueDateTextView;
        CheckBox checkBox;
        CardView cardView;
        ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView2);
            labelTextView = itemView.findViewById(R.id.textViewLabel);
            reminderTextView = itemView.findViewById(R.id.reminderTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            checkBox = itemView.findViewById(R.id.checkBox);

            imageButton = itemView.findViewById(R.id.imageButton);
        }

    }
}
