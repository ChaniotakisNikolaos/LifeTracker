package com.example.lifetracker;

public class ToDoItem {
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
}
