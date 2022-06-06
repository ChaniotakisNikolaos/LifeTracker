package com.example.lifetracker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class EditBudgetItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget_item);
        Button cancelEditBudgetButton;
        cancelEditBudgetButton = findViewById(R.id.buttonCancelEditBudget);
        cancelEditBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ddd","exifegt");
                finish();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v.getId());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /*public void editBudgetItem(View v) {
        EditText editBudgetLabelEditText, editSavingsUpToNowEditText, editEndGoalEditText;
        TextView editBudgetLabelTextView, editSavingsUpToNowTextLabel, editEndGoalTextView, editDateTextView, editDueDateSelectTextView;
        Button cancelEditBudgetButton, saveEditBudgetButton;

        editBudgetLabelTextView = findViewById(R.id.textViewEditBudgetLabel);
        editBudgetLabelEditText = findViewById(R.id.editTextEditBudgetLabel);
        editSavingsUpToNowTextLabel = findViewById(R.id.textViewEditSavingsUpToNow);
        editSavingsUpToNowEditText = findViewById(R.id.editTextNumberEditSavingsUpToNow);
        editEndGoalTextView = findViewById(R.id.textViewEditEndGoal);
        editEndGoalEditText = findViewById(R.id.editTextNumberEditEndGoal);
        editDateTextView = findViewById(R.id.textViewEditDueDate);
        editDueDateSelectTextView = findViewById(R.id.textViewEditDueDateSelect);
        cancelEditBudgetButton = findViewById(R.id.buttonCancelEditBudget);
        saveEditBudgetButton = findViewById(R.id.buttonSaveEditBudget);
    }*/
}