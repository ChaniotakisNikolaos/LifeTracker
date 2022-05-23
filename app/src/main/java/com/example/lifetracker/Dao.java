package com.example.lifetracker;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Query("SELECT * FROM ToDoItem")
    List<ToDoItem> getToDoItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToDoItem(ToDoItem toDoItem);

    @Update
    void updateToDoItem(ToDoItem toDoItem);
}
