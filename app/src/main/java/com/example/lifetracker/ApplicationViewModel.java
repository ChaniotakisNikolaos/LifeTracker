package com.example.lifetracker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ApplicationViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<ToDoItem>> toDoItemList;
    private LiveData<List<ToDoItem>> toDoItemLabelList;
    private LiveData<List<ToDoItem>> toDoDListDueDate;
    private final LiveData<List<BudgetItem>> budgetItemList;
    private LiveData<List<BudgetItem>> budgetListDueDate;

    public ApplicationViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        toDoItemList = repository.getAllToDoItems();
        budgetItemList = repository.getAllBudgetItems();
    }

    //TO DO
    public void insert(ToDoItem toDoItem, Context context){
        repository.insert(toDoItem, context);
    }

    public void update(ToDoItem toDoItem){
        repository.update(toDoItem);
    }

    public void delete(ToDoItem toDoItem){
        repository.delete(toDoItem);
    }

    public LiveData<List<ToDoItem>> getToDoItemList() {
        return toDoItemList;
    }

    public LiveData<List<ToDoItem>> getAllToDoItemsWithLabel(String label) {
        toDoItemLabelList = repository.getAllToDoItemsWithLabel(label);
        return toDoItemLabelList;
    }

    public LiveData<List<ToDoItem>> getAllToDoItemsWithDueDate(String dueDate){
        toDoDListDueDate = repository.getAllToDoItemsWithDueDate(dueDate);
        return toDoDListDueDate;
    }

    //BUDGET
    public void insert(BudgetItem budgetItem){
        repository.insert(budgetItem);
    }

    public void update(BudgetItem budgetItem){
        repository.update(budgetItem);
    }

    public void delete(BudgetItem budgetItem){
        repository.delete(budgetItem);
    }

    public LiveData<List<BudgetItem>> getBudgetItemList() {
        return budgetItemList;
    }

    public LiveData<List<BudgetItem>> getAllBudgetItemsWithDueDate(String dueDate){
        budgetListDueDate = repository.getBudgetItemsWithDueDate(dueDate);
        return budgetListDueDate;
    }
}
