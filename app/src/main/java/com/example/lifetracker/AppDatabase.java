package com.example.lifetracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ToDoItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "life-tracker-db";
    public abstract Dao dao();
}
