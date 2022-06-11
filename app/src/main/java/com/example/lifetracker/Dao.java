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
    @Query("SELECT * FROM ToDoItem")
    List<ToDoItem> getToDoItems();

    @Query("SELECT * FROM ToDoItem WHERE dueDate=:dueDate")
    List<ToDoItem> getToDoItems(String dueDate);

    @Query("SELECT * FROM ToDoItem")
    LiveData<List<ToDoItem>> getAllToDoItems();


    @Query("SELECT * FROM ToDoItem WHERE label=:label")
    LiveData<List<ToDoItem>> getAllToDoItemsWithLabel(String label);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertToDoItem(ToDoItem toDoItem);

    @Update
    void updateToDoItem(ToDoItem toDoItem);

    @Delete
    void deleteToDoItem(ToDoItem toDoItem);

    @Query("SELECT * FROM BudgetItem")
    List<BudgetItem> getBudgetItems();

    @Query("SELECT * FROM BudgetItem WHERE dueDate=:dueDate")
    List<BudgetItem> getBudgetItems(String dueDate);

    @Query("SELECT * FROM BudgetItem")
    LiveData<List<BudgetItem>> getAllBudgetItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBudgetItem(BudgetItem budgetItem);

    @Update
    void updateBudgetItem(BudgetItem budgetItem);

    @Delete
    void deleteBudgetItem(BudgetItem budgetItem);
}
