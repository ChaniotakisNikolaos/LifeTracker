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
    private int saved;
    private int total;
    private String dueDate;

    public BudgetItem(@NonNull String label, @NonNull int saved, int total, String dueDate) {
        this.label = label;
        this.saved = saved;
        this.total = total;
        this.dueDate = dueDate;
    }

    public void addSaved(int amount){
        saved+=amount;
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
    public int getSaved() {
        return saved;
    }

    public int getTotal() {
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

    public void setSaved(@NonNull int saved) {
        this.saved = saved;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

}
