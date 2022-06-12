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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddToDoItemActivity extends AppCompatActivity {
    public static final String EXTRA_DESCRIPTION = "com.example.lifetracker.DESCRIPTION";
    public static final String EXTRA_LABEL = "com.example.lifetracker.LABEL";
    public static final String EXTRA_DUE_DATE = "com.example.lifetracker.DUE_DATE";
    public static final String EXTRA_REMINDER = "com.example.lifetracker.REMINDER";
    public static final String EXTRA_ID = "com.example.lifetracker.ID";
    private EditText toDoEditText;
    private EditText label;
    private TextView reminderTextView;
    private TextView dueDateTextView;
    private Button clearReminderButton, clearDueDateToDoButton;
    private int toDoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_add_to_do_item);

        Button addToDoButton = findViewById(R.id.addToDoButton);
        toDoEditText = findViewById(R.id.toDoEditText);
        label = findViewById(R.id.labelEditText);
        reminderTextView = findViewById(R.id.reminderSelectTextView);
        dueDateTextView = findViewById(R.id.dueDateSelectTextView);

        clearReminderButton = findViewById(R.id.clearReminderBtn);
        clearReminderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reminderTextView.setText(null);
            }
        });
        clearDueDateToDoButton = findViewById(R.id.clearDueDateToDoBtn);
        clearDueDateToDoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dueDateTextView.setText(null);
            }
        });
        Intent intent = getIntent();
        if(intent.getBooleanExtra("EDIT_MODE",false)){
            addToDoButton.setText("Edit");
            toDoEditText.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            label.setText(intent.getStringExtra(EXTRA_LABEL));
            reminderTextView.setText(intent.getStringExtra(EXTRA_REMINDER));
            dueDateTextView.setText(intent.getStringExtra(EXTRA_DUE_DATE));
            toDoId = intent.getIntExtra(EXTRA_ID,-1);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v.getId());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addToDoItem(View v) {
        boolean isEmpty = true;

        //check if reminder is before the current time
        String myDate = reminderTextView.getText().toString();
        long timeInMillis = 0;
        if(myDate.trim().length() != 0) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
            try {
                cal.setTime(Objects.requireNonNull(sdf.parse(myDate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            timeInMillis = cal.getTimeInMillis();
            if (timeInMillis <= System.currentTimeMillis()) {
                Toast.makeText(this, "You cannot put a reminder for a past time", Toast.LENGTH_SHORT).show();
                isEmpty = false;
            }
        }
        if(toDoEditText.getText().toString().trim().length() == 0){
            Toast.makeText(this,"You must put a Description",Toast.LENGTH_SHORT).show();
            isEmpty = false;
        }
        if(isEmpty) {
            ToDoItem toDoItem = new ToDoItem(toDoEditText.getText().toString().trim(), label.getText().toString().trim(), dueDateTextView.getText().toString(), reminderTextView.getText().toString(), false);

            Intent intent = getIntent();
            if(intent.getBooleanExtra("EDIT_MODE",false) && (!toDoEditText.getText().toString().equals(intent.getStringExtra(EXTRA_DESCRIPTION)) || !reminderTextView.getText().toString().equals(intent.getStringExtra(EXTRA_REMINDER)))){
                //delete notification either because the values changed or because it was deleted
                Log.d("aaaaaaaaaaDELETE","delete");
                Intent alarmIntent = new Intent(this, ReminderReceiver.class);
                AlarmManager alarmManager = (AlarmManager)this.getSystemService(MainActivity.ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, toDoItem.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);

                if(!reminderTextView.getText().toString().isEmpty()){
                    Log.d("aaaaaaaaaaNEWWWW","new");
                    Log.d("aaaaaaaaaaNEWWWW",myDate);
                    Log.d("aaaaaaaaaaNEWWWW", String.valueOf(toDoId));

                    //create new notification
                    alarmIntent.putExtra("name", toDoEditText.getText().toString().trim());
                    alarmIntent.putExtra("id", toDoId);
                    PendingIntent pendingIntentNew = PendingIntent.getBroadcast(this, toDoId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    alarmManager.set(AlarmManager.RTC_WAKEUP,timeInMillis, pendingIntentNew);//RTC_WAKEUP will wake up the device to fire the pending intent at the specified time
                }
            }
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
                replyIntent.putExtra(EXTRA_ID, toDoId);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        }
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