package com.example.lifetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class AddBudgetItemActivity  extends AppCompatActivity {
    public static final String EXTRA_LABEL = "com.example.lifetracker.BUDGET_LABEL";
    public static final String EXTRA_SAVED = "com.example.lifetracker.SAVED";
    public static final String EXTRA_TOTAL = "com.example.lifetracker.TOTAL ";
    public static final String EXTRA_DUE_DATE = "com.example.lifetracker.BUDGET_DUE_DATE";

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
        boolean isEmpty = true;

        EditText budgetNameEditText = findViewById(R.id.budgetNameEditText);
        EditText savingsNowEditText = findViewById(R.id.savingsNowEditText);
        EditText totalSavingsEditText = findViewById(R.id.totalSavingsEditText);
        TextView dueDateTextView1 = findViewById(R.id.dueDateSelectTextView1);

        if(budgetNameEditText.getText().toString().trim().length() == 0){
            Toast.makeText(this,"You must put a Name",Toast.LENGTH_SHORT).show();
            isEmpty = false;
        }
        if(savingsNowEditText.getText().toString().trim().length() == 0){
            savingsNowEditText.setText("0");
        }
        if(totalSavingsEditText.getText().toString().trim().length() == 0){
            Toast.makeText(this,"You must put the total amount",Toast.LENGTH_SHORT).show();
            isEmpty = false;
        }

        if(isEmpty) {
            BudgetItem budgetItem = new BudgetItem(budgetNameEditText.getText().toString(),Integer.parseInt(savingsNowEditText.getText().toString()),Integer.parseInt(totalSavingsEditText.getText().toString()),dueDateTextView1.getText().toString());
            Toast.makeText(this, "Budget item added", Toast.LENGTH_SHORT).show();
            Intent replyIntent = new Intent();
            boolean checks = false;
            if (checks) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                replyIntent.putExtra(EXTRA_LABEL, budgetItem.getLabel());
                replyIntent.putExtra(EXTRA_SAVED, budgetItem.getSaved());
                replyIntent.putExtra(EXTRA_TOTAL, budgetItem.getTotal());
                replyIntent.putExtra(EXTRA_DUE_DATE, budgetItem.getDueDate());
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        }
    }

}
