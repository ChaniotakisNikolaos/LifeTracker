package com.example.lifetracker;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class BudgetItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String label;
    private int saved;      //saved amount
    private final int total;//goal amount
    private final String dueDate;

    public BudgetItem(@NonNull String label, int saved, int total, String dueDate) {
        this.label = label;
        this.saved = saved;
        this.total = total;
        this.dueDate = dueDate;
    }

    public void addSaved(int amount) {
        saved += amount;
    }

    @Ignore
    public BudgetItem(BudgetItem budgetItem) {
        this.id = budgetItem.id;
        this.label = budgetItem.label;
        this.saved = budgetItem.saved;
        this.total = budgetItem.total;
        this.dueDate = budgetItem.dueDate;
    }

    //getters
    public int getId() {
        return id;
    }

    @NonNull
    public String getLabel() {
        return label;
    }

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

}
