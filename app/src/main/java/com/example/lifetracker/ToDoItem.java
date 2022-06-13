package com.example.lifetracker;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ToDoItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String description;
    private String label;
    private String dueDate;
    private String reminder;
    private boolean isSelected;

    public ToDoItem(String description, String label, String dueDate, String reminder, boolean isSelected) {
        this.description = description;
        this.label = label;
        this.dueDate = dueDate;
        this.reminder = reminder;
        this.isSelected = isSelected;
    }

    @Ignore
    public ToDoItem(ToDoItem toDoItem) {
        this.id = toDoItem.id;
        this.description = toDoItem.description;
        this.label = toDoItem.label;
        this.dueDate = toDoItem.dueDate;
        this.reminder = toDoItem.reminder;
        this.isSelected = toDoItem.isSelected;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getReminder() {
        return reminder;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public boolean setSelected(boolean selected) {
        this.isSelected = selected;
        return selected;
    }
}
