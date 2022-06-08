package com.example.lifetracker;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
    public void insert(ToDoItem toDoItem, Context context){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long toDoId = dao.insertToDoItem(toDoItem);
                if(toDoItem.getReminder().trim().length()!= 0){
                    Log.d("ttt", String.valueOf(toDoId));
                    Log.d("test","ToDo inserted");
                    Log.d("testID", String.valueOf(toDoId));
                    Intent alarmIntent = new Intent(context, ReminderReceiver.class);
                    alarmIntent.putExtra("name", toDoItem.getDescription());
                    alarmIntent.putExtra("id", toDoId);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) toDoId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(MainActivity.ALARM_SERVICE);
                    String myDate = toDoItem.getReminder();
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
                    try {
                        cal.setTime(Objects.requireNonNull(sdf.parse(myDate)));// all done
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long timeInMillis =cal.getTimeInMillis();
                    alarmManager.set(AlarmManager.RTC_WAKEUP,timeInMillis, pendingIntent);//RTC_WAKEUP will wake up the device to fire the pending intent at the specified time
                }
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
