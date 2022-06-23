package com.example.lifetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

//This activity is used to add or edit a BudgetItem
public class AddBudgetItemActivity  extends AppCompatActivity {
    //The names of the extra data to put in the intent and get from the intent(get is used only in edit mode)
    public static final String EXTRA_ID = "com.example.lifetracker.BUDGET_ID";
    public static final String EXTRA_LABEL = "com.example.lifetracker.BUDGET_LABEL";
    public static final String EXTRA_SAVED = "com.example.lifetracker.SAVED";
    public static final String EXTRA_TOTAL = "com.example.lifetracker.TOTAL ";
    public static final String EXTRA_DUE_DATE = "com.example.lifetracker.BUDGET_DUE_DATE";

    private EditText budgetNameEditText; //The field in which the user specifies the budget name/label
    private EditText savingsNowEditText; //The field in which the user specifies the saved amount
    private EditText totalSavingsEditText; //The field in which the user specifies the total amount he needs to save
    private TextView dueDateTextView1; ////The field in which the user specifies the due date
    int budgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget_item);

        budgetNameEditText = findViewById(R.id.budgetNameEditText);
        savingsNowEditText = findViewById(R.id.savingsNowEditText);
        totalSavingsEditText = findViewById(R.id.totalSavingsEditText);
        dueDateTextView1 = findViewById(R.id.dueDateSelectTextView1);
        Button confirmButton = findViewById(R.id.button2);

        Intent intent = getIntent();// Get intent to check if in add or edit mode
        budgetId= intent.getIntExtra(EXTRA_ID,-1); //The id of the BudgetItem to be edited if in edit mode otherwise it defaults to -1(Add mode)
        if (budgetId!=-1){ //Edit BudgetItem mode
            //Field text initialization based on the edited BudgetItem
            budgetNameEditText.setText(intent.getStringExtra(EXTRA_LABEL));
            savingsNowEditText.setText(String.valueOf(intent.getIntExtra(EXTRA_SAVED,0)));
            totalSavingsEditText.setText(String.valueOf(intent.getIntExtra(EXTRA_TOTAL,0)));
            dueDateTextView1.setText(intent.getStringExtra(EXTRA_DUE_DATE));
            confirmButton.setText(R.string.editString);//Change button text from Add to Edit
        }
    }

    //Shows a DatePickerDialog for the user to pick a due date after he clicks on the due date TextView
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v.getId());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Replies with  an intent containing the edited(edit mode) or newly created(add mode) BudgetItem
    public void addBudgetItem(View v) {
        boolean isNotEmpty = true;//Required fields are the total amount and the name, becomes false if either is empty

        Button clearDueDateBudgetButton = findViewById(R.id.clearDueDateBudgetBtn);
        clearDueDateBudgetButton.setOnClickListener(v1 -> dueDateTextView1.setText(""));//Resets due date

        if(budgetNameEditText.getText().toString().trim().length() == 0){//is the name empty
            Toast.makeText(this,"You must put a Name",Toast.LENGTH_SHORT).show();
            isNotEmpty = false;
        }
        if(savingsNowEditText.getText().toString().trim().length() == 0){//is savings are empty they default to 0
            savingsNowEditText.setText("0");
        }
        if(totalSavingsEditText.getText().toString().trim().length() == 0){//is the total amount empty
            Toast.makeText(this,"You must put the total amount",Toast.LENGTH_SHORT).show();
            isNotEmpty = false;
        }

        if(isNotEmpty) {
            //Create BudgetItem based on the users input
            BudgetItem budgetItem = new BudgetItem(budgetNameEditText.getText().toString(),Integer.parseInt(savingsNowEditText.getText().toString()),Integer.parseInt(totalSavingsEditText.getText().toString()),dueDateTextView1.getText().toString());
            Toast.makeText(this, "Budget item "+(budgetId==-1?"added":"edited"), Toast.LENGTH_SHORT).show();//Show proper toast message depended on whether the item is added or edited

            Intent replyIntent = new Intent();
            //Check if savings up to now are equal to the total savings in order to congratulate the user with a toast message
            if(Integer.parseInt(savingsNowEditText.getText().toString())>=Integer.parseInt(totalSavingsEditText.getText().toString())) {
                Toast toast= Toast.makeText(this, "Congratulations!\nYou hit your goal for: "+budgetNameEditText.getText().toString()+"!",Toast.LENGTH_SHORT);
                TextView toastV = toast.getView().findViewById(android.R.id.message);
                toastV.setGravity(Gravity.CENTER); //Center toast's message text
                toast.setGravity(Gravity.TOP, 0, 0); //Put toast at the top of the screen
                toast.show();
            }

            replyIntent.putExtra(EXTRA_ID, budgetId);
            replyIntent.putExtra(EXTRA_LABEL, budgetItem.getLabel());
            replyIntent.putExtra(EXTRA_SAVED, budgetItem.getSaved());
            replyIntent.putExtra(EXTRA_TOTAL, budgetItem.getTotal());
            replyIntent.putExtra(EXTRA_DUE_DATE, budgetItem.getDueDate());
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

}
