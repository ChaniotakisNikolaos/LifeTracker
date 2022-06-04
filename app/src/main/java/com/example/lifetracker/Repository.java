package com.example.lifetracker;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private Dao dao;
    private LiveData<List<ToDoItem>> toDoList;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public Repository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.dao();
        toDoList = dao.getAllToDoItems();
    }

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

}
