package com.example.lifetracker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ApplicationViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<ToDoItem>> toDoItemList;
    private final LiveData<List<BudgetItem>> budgetItemList;

    public ApplicationViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        toDoItemList = repository.getAllToDoItems();
        budgetItemList = repository.getAllBudgetItems();
    }

    //TO DO
    public void insert(ToDoItem toDoItem, Context context) {
        repository.insert(toDoItem, context);
    }

    public void update(ToDoItem toDoItem) {
        repository.update(toDoItem);
    }

    public void delete(ToDoItem toDoItem) {
        repository.delete(toDoItem);
    }

    public LiveData<List<ToDoItem>> getToDoItemList() {
        return toDoItemList;
    }

    public LiveData<List<ToDoItem>> getAllToDoItemsWithLabel(String label) {
        return repository.getAllToDoItemsWithLabel(label);
    }

    public LiveData<List<ToDoItem>> getAllToDoItemsWithDueDate(String dueDate) {
        return repository.getAllToDoItemsWithDueDate(dueDate);
    }

    //BUDGET
    public void insert(BudgetItem budgetItem) {
        repository.insert(budgetItem);
    }

    public void update(BudgetItem budgetItem) {
        repository.update(budgetItem);
    }

    public void delete(BudgetItem budgetItem) {
        repository.delete(budgetItem);
    }

    public LiveData<List<BudgetItem>> getBudgetItemList() {
        return budgetItemList;
    }

    public LiveData<List<BudgetItem>> getAllBudgetItemsWithDueDate(String dueDate) {
        return repository.getBudgetItemsWithDueDate(dueDate);
    }
}
