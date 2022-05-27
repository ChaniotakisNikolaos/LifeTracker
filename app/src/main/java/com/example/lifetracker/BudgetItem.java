package com.example.lifetracker;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BudgetItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String label;
    @NonNull
    private String saved;
    private String total;
    private String dueDate;

    public BudgetItem(@NonNull String label, @NonNull String saved, String total, String dueDate) {
        this.label = label;
        this.saved = saved;
        this.total = total;
        this.dueDate = dueDate;
    }

    //getters
    public int getId() {
        return id;
    }

    @NonNull
    public String getLabel() {
        return label;
    }

    @NonNull
    public String getSaved() {
        return saved;
    }

    public String getTotal() {
        return total;
    }

    public String getDueDate() {
        return dueDate;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setLabel(@NonNull String label) {
        this.label = label;
    }

    public void setSaved(@NonNull String saved) {
        this.saved = saved;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

}
