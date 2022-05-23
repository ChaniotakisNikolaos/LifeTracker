package com.example.lifetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddToDoItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_item);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v.getId());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addToDoItem(View v) {
        EditText toDoEditText = findViewById(R.id.toDoEditText);
        EditText label = findViewById(R.id.labelEditText);
        TextView reminderTextView = findViewById(R.id.reminderSelectTextView);
        TextView dueDateTextView = findViewById(R.id.dueDateSelectTextView);
        ToDoItem toDoItem = new ToDoItem(toDoEditText.getText().toString(),label.getText().toString(),reminderTextView.getText().toString(),dueDateTextView.getText().toString());
    }
}