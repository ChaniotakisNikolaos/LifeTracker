package com.example.lifetracker;

import androidx.annotation.NonNull;
import androidx.room.Entity;
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

    public ToDoItem(String description, String label, String dueDate, String reminder) {
        this.description = description;
        this.label = label;
        this.dueDate = dueDate;
        this.reminder = reminder;
    }

    public ToDoItem(int id, String description, String label, String dueDate, String reminder) {
        this.id = id;
        this.description = description;
        this.label = label;
        this.dueDate = dueDate;
        this.reminder = reminder;
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
}
