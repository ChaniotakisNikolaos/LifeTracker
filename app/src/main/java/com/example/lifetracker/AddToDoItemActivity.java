package com.example.lifetracker;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

//This activity is used to add or edit a ToDoItem
public class AddToDoItemActivity extends AppCompatActivity {
    //The names of the extra data to put in the intent and get from the intent(get is used only in edit mode)
    public static final String EXTRA_DESCRIPTION = "com.example.lifetracker.DESCRIPTION";
    public static final String EXTRA_LABEL = "com.example.lifetracker.LABEL";
    public static final String EXTRA_DUE_DATE = "com.example.lifetracker.DUE_DATE";
    public static final String EXTRA_REMINDER = "com.example.lifetracker.REMINDER";
    public static final String EXTRA_ID = "com.example.lifetracker.ID";
    private EditText toDoEditText; //The field in which the user specifies the description
    private EditText label; //The field in which the user specifies the label
    private TextView reminderTextView; //The field in which the user specifies the time and date of the reminder
    private TextView dueDateTextView; //The field in which the user specifies the due date
    private int toDoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_item);

        createNotificationChannel();

        Button addToDoButton = findViewById(R.id.addToDoButton);
        toDoEditText = findViewById(R.id.toDoEditText);
        label = findViewById(R.id.labelEditText);
        reminderTextView = findViewById(R.id.reminderSelectTextView);
        dueDateTextView = findViewById(R.id.dueDateSelectTextView);

        Button clearReminderButton = findViewById(R.id.clearReminderBtn);
        clearReminderButton.setOnClickListener(v -> reminderTextView.setText(""));//Resets the reminder date and time
        Button clearDueDateToDoButton = findViewById(R.id.clearDueDateToDoBtn);
        clearDueDateToDoButton.setOnClickListener(v -> dueDateTextView.setText(""));//Resets the due date

        Intent intent = getIntent();// Get intent to check if in add or edit mode
        if (intent.getBooleanExtra("EDIT_MODE", false)) {//true:Edit mode false:Add mode
            //Field text initialization based on the edited ToDoItem
            addToDoButton.setText(R.string.editString);//Change button text from Add to Edit
            toDoEditText.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            label.setText(intent.getStringExtra(EXTRA_LABEL));
            reminderTextView.setText(intent.getStringExtra(EXTRA_REMINDER));
            dueDateTextView.setText(intent.getStringExtra(EXTRA_DUE_DATE));
            toDoId = intent.getIntExtra(EXTRA_ID, -1);
        }
    }

    //Shows a DatePickerDialog for the user to pick a due date after he clicks on the dueDateTextView
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v.getId());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addToDoItem(View v) {
        boolean isNotEmpty = true;

        //check if reminder is before the current time
        String myDate = reminderTextView.getText().toString();
        long timeInMillis = 0;
        if (myDate.trim().length() != 0) {
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
                isNotEmpty = false;
            }
        }
        if (toDoEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "You must put a Description", Toast.LENGTH_SHORT).show();
            isNotEmpty = false;
        }
        if (isNotEmpty) {
            ToDoItem toDoItem = new ToDoItem(toDoEditText.getText().toString().trim(), label.getText().toString().trim(), dueDateTextView.getText().toString(), reminderTextView.getText().toString(), false);

            Intent intent = getIntent();
            if (intent.getBooleanExtra("EDIT_MODE", false) && (!toDoEditText.getText().toString().equals(intent.getStringExtra(EXTRA_DESCRIPTION)) || !reminderTextView.getText().toString().equals(intent.getStringExtra(EXTRA_REMINDER)))) {
                //delete notification either because the values changed or because it was deleted
                Intent alarmIntent = new Intent(this, ReminderReceiver.class);
                AlarmManager alarmManager = (AlarmManager) this.getSystemService(MainActivity.ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, toDoItem.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);

                if (!reminderTextView.getText().toString().isEmpty()) {
                    //create new notification
                    alarmIntent.putExtra("name", toDoEditText.getText().toString().trim());
                    alarmIntent.putExtra("id", toDoId);
                    PendingIntent pendingIntentNew = PendingIntent.getBroadcast(this, toDoId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntentNew);//RTC_WAKEUP will wake up the device to fire the pending intent at the specified time
                }
            }
            Toast.makeText(this, "TODO item added", Toast.LENGTH_SHORT).show();

            Intent replyIntent = new Intent();
            replyIntent.putExtra(EXTRA_DESCRIPTION, toDoItem.getDescription());
            replyIntent.putExtra(EXTRA_LABEL, toDoItem.getLabel());
            replyIntent.putExtra(EXTRA_DUE_DATE, toDoItem.getDueDate());
            replyIntent.putExtra(EXTRA_REMINDER, toDoItem.getReminder());
            replyIntent.putExtra(EXTRA_ID, toDoId);
            setResult(RESULT_OK, replyIntent);

            finish();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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