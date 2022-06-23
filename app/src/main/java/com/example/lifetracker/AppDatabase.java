package com.example.lifetracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ToDoItem.class, BudgetItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase { //singleton pattern

    public static final String DB_NAME = "life-tracker-db";
    private static AppDatabase instance;

    public abstract Dao dao();

    public static synchronized AppDatabase getInstance(Context context) {
        return instance == null ? instance = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build() : instance;
    }
}
