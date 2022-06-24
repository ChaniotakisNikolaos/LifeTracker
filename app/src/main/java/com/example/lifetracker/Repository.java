package com.example.lifetracker;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final Dao dao;
    private final LiveData<List<ToDoItem>> toDoList;
    private final LiveData<List<BudgetItem>> budgetList;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public Repository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.dao();
        toDoList = dao.getAllToDoItems();
        budgetList = dao.getAllBudgetItems();
    }

    //TO DO
    public void insert(ToDoItem toDoItem, Context context) {
        executorService.execute(() -> {
            long toDoId = dao.insertToDoItem(toDoItem);
            if (toDoItem.getReminder().trim().length() != 0) {//if the user has set a reminder
                //Create reminder after insert
                Intent alarmIntent = new Intent(context, ReminderReceiver.class);
                alarmIntent.putExtra("name", toDoItem.getDescription());
                alarmIntent.putExtra("id", toDoId);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) toDoId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(MainActivity.ALARM_SERVICE);
                String myDate = toDoItem.getReminder();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
                try {
                    cal.setTime(Objects.requireNonNull(sdf.parse(myDate)));//change the date that the user gave to type Date
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long timeInMillis = cal.getTimeInMillis();//get time in millis from the user's reminder
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);//RTC_WAKEUP will wake up the device to fire the pending intent at the specified time
            }
        });

    }

    public void update(ToDoItem toDoItem) {
        executorService.execute(() -> dao.updateToDoItem(toDoItem));
    }

    public void delete(ToDoItem toDoItem) {
        executorService.execute(() -> dao.deleteToDoItem(toDoItem));
    }

    public LiveData<List<ToDoItem>> getAllToDoItems() {
        return toDoList;
    }

    public LiveData<List<ToDoItem>> getAllToDoItemsWithLabel(String label) {
        return dao.getAllToDoItemsWithLabel(label);
    }

    public LiveData<List<ToDoItem>> getAllToDoItemsWithDueDate(String dueDate) {
        return dao.getToDoItems(dueDate);
    }

    //BUDGET
    public void insert(BudgetItem budgetItem) {
        executorService.execute(() -> dao.insertBudgetItem(budgetItem));

    }

    public void update(BudgetItem budgetItem) {
        executorService.execute(() -> dao.updateBudgetItem(budgetItem));
    }

    public void delete(BudgetItem budgetItem) {
        executorService.execute(() -> dao.deleteBudgetItem(budgetItem));
    }

    public LiveData<List<BudgetItem>> getAllBudgetItems() {
        return budgetList;
    }

    public LiveData<List<BudgetItem>> getBudgetItemsWithDueDate(String dueDate) {
        return dao.getBudgetItems(dueDate);
    }
}
