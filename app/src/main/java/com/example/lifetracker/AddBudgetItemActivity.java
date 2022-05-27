package com.example.lifetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

public class AddBudgetItemActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget_item);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v.getId());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void addBudgetItem(View v) {
        EditText PersonNameEditText = findViewById(R.id.PersonNameEditText);
        EditText savingsNowEditText = findViewById(R.id.savingsNowEditText);
        TextView totalSavingsEditText = findViewById(R.id.totalSavingsEditText);
        TextView dueDateTextView = findViewById(R.id.dueDateSelectTextView);
        BudgetItem budgetItem = new BudgetItem(PersonNameEditText.getText().toString(),savingsNowEditText.getText().toString(),totalSavingsEditText.getText().toString(),dueDateTextView.getText().toString());
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, AppDatabase.DB_NAME).build();
    }
}
