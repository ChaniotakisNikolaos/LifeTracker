package com.example.lifetracker;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private Dao dao;
    private LiveData<List<ToDoItem>> toDoList;
    private LiveData<List<BudgetItem>> budgetList;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public Repository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.dao();
        toDoList = dao.getAllToDoItems();
        budgetList = dao.getAllBudgetItems();
    }

    //TO DO
    public void insert(ToDoItem toDoItem){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertToDoItem(toDoItem);
            }
        });

    }

    public void update(ToDoItem toDoItem){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateToDoItem(toDoItem);
            }
        });
    }

    public void delete(ToDoItem toDoItem){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteToDoItem(toDoItem);
            }
        });
    }

    public LiveData<List<ToDoItem>> getAllToDoItems(){
        return toDoList;
    }

    //BUDGET
    public void insert(BudgetItem budgetItem){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertBudgetItem(budgetItem);
            }
        });

    }

    public void update(BudgetItem budgetItem){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateBudgetItem(budgetItem);
            }
        });
    }

    public void delete(BudgetItem budgetItem){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteBudgetItem(budgetItem);
            }
        });
    }

    public LiveData<List<BudgetItem>> getAllBudgetItems(){
        return budgetList;
    }
}
