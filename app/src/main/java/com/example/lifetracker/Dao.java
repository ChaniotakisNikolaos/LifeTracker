package com.example.lifetracker;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    //get all the to do items when there is a specific due date
    @Query("SELECT * FROM ToDoItem WHERE dueDate=:dueDate")
    LiveData<List<ToDoItem>> getToDoItems(String dueDate);

    //get all to do items
    @Query("SELECT * FROM ToDoItem")
    LiveData<List<ToDoItem>> getAllToDoItems();

    //get all to do items where there is a specific label
    @Query("SELECT * FROM ToDoItem WHERE label=:label")
    LiveData<List<ToDoItem>> getAllToDoItemsWithLabel(String label);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertToDoItem(ToDoItem toDoItem);

    @Update
    void updateToDoItem(ToDoItem toDoItem);

    @Delete
    void deleteToDoItem(ToDoItem toDoItem);

    //get all budget items where there is a specific due date
    @Query("SELECT * FROM BudgetItem WHERE dueDate=:dueDate")
    LiveData<List<BudgetItem>> getBudgetItems(String dueDate);

    //get all budget items
    @Query("SELECT * FROM BudgetItem")
    LiveData<List<BudgetItem>> getAllBudgetItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBudgetItem(BudgetItem budgetItem);

    @Update
    void updateBudgetItem(BudgetItem budgetItem);

    @Delete
    void deleteBudgetItem(BudgetItem budgetItem);
}
