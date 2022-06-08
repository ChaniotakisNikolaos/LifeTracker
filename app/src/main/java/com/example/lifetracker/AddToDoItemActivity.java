package com.example.lifetracker;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddToDoItemActivity extends AppCompatActivity {
    public static final String EXTRA_DESCRIPTION = "com.example.lifetracker.DESCRIPTION";
    public static final String EXTRA_LABEL = "com.example.lifetracker.LABEL";
    public static final String EXTRA_DUE_DATE = "com.example.lifetracker.DUE_DATE";
    public static final String EXTRA_REMINDER = "com.example.lifetracker.REMINDER ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();

        setContentView(R.layout.activity_add_to_do_item);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v.getId());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addToDoItem(View v) {
        boolean isEmpty = true;

        EditText toDoEditText = findViewById(R.id.toDoEditText);
        EditText label = findViewById(R.id.labelEditText);
        TextView reminderTextView = findViewById(R.id.reminderSelectTextView);
        TextView dueDateTextView = findViewById(R.id.dueDateSelectTextView);

        if(toDoEditText.getText().toString().trim().length() == 0){
            Toast.makeText(this,"You must put a Description",Toast.LENGTH_SHORT).show();
            isEmpty = false;
        }
        if(isEmpty) {
            ToDoItem toDoItem = new ToDoItem(toDoEditText.getText().toString(), label.getText().toString(), dueDateTextView.getText().toString(), reminderTextView.getText().toString());
            //AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, AppDatabase.DB_NAME).build();
            Toast.makeText(this, "TODO item added", Toast.LENGTH_SHORT).show();

            Intent replyIntent = new Intent();
            boolean checks = false;
            if (checks) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                replyIntent.putExtra(EXTRA_DESCRIPTION, toDoItem.getDescription());
                replyIntent.putExtra(EXTRA_LABEL, toDoItem.getLabel());
                replyIntent.putExtra(EXTRA_DUE_DATE, toDoItem.getDueDate());
                replyIntent.putExtra(EXTRA_REMINDER, toDoItem.getReminder());
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        }
        //ApplicationViewModel applicationViewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "NiSoReminderChannel";
            String desc = "Channel for NiSo Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyNiso", name, importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}