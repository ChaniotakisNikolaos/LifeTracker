package com.example.lifetracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentAdapter fragmentAdapter;
    private ArrayList<ToDoItem> toDoItemArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fragmentManager,getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    /** Called when the user taps the plus button */
    public void addToDoItem(View view) {
        Intent intent = new Intent(this, AddToDoItemActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v.getId());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    /** Called when the user taps the plus button */
    public void addBudgetItem(View view) {
        Intent intent = new Intent(this, AddBudgetItemActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void addMoneyDialog(View view){
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        EditText addMoneyEditText;
        TextView addMoneyTextView;
        Button addMoneyCancelButton, addMoneySaveButton;
        dialogBuilder = new AlertDialog.Builder(this);
        //LayoutInflater inflater = (LayoutInflater)getLayoutInflater.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addMoneyView = getLayoutInflater().inflate(R.layout.dialog_add_money, null);
        addMoneyEditText = (EditText) addMoneyView.findViewById(R.id.editTextAddAmountToBudget);
        addMoneyTextView = (TextView) addMoneyView.findViewById(R.id.textViewAddMoney);
        addMoneyCancelButton = (Button) addMoneyView.findViewById(R.id.buttonCancelAddMoney);
        addMoneySaveButton = (Button) addMoneyView.findViewById(R.id.buttonSaveAddMoney);

        dialogBuilder.setView(addMoneyView);
        dialog = dialogBuilder.create();
        dialog.show();

        addMoneyCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close dialog
                dialog.dismiss();
            }
        });

        addMoneySaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
    public void subtractMoneyDialog(View view){
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        EditText subtractMoneyEditText;
        TextView subtractMoneyTextView;
        Button subtractMoneyCancelButton, subtractMoneySaveButton;
        dialogBuilder = new AlertDialog.Builder(this);
        //LayoutInflater inflater = (LayoutInflater)getLayoutInflater.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View subtractMoneyView = getLayoutInflater().inflate(R.layout.dialog_subtract_money, null);
        subtractMoneyEditText = (EditText) subtractMoneyView.findViewById(R.id.editTextSubtractAmountToBudget);
        subtractMoneyTextView = (TextView) subtractMoneyView.findViewById(R.id.textViewSubtractMoney);
        subtractMoneyCancelButton = (Button) subtractMoneyView.findViewById(R.id.buttonCancelSubtractMoney);
        subtractMoneySaveButton = (Button) subtractMoneyView.findViewById(R.id.buttonSaveSubtractMoney);

        dialogBuilder.setView(subtractMoneyView);
        dialog = dialogBuilder.create();
        dialog.show();

        subtractMoneyCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close dialog
                dialog.dismiss();
            }
        });

        subtractMoneySaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
    public void editBudgetItemDialog(View view){
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        EditText editBudgetLabelEditText, editSavingsUpToNowEditText, editEndGoalEditText;
        TextView editBudgetLabelTextView, editSavingsUpToNowTextLabel, editEndGoalTextView, editDateTextView, editDueDateSelectTextView;
        Button cancelEditBudgetButton, saveEditBudgetButton;
        dialogBuilder = new AlertDialog.Builder(this);
        final View editMoneyView = getLayoutInflater().inflate(R.layout.dialog_edit_budget_item, null);
        editBudgetLabelTextView = (TextView) editMoneyView.findViewById(R.id.textViewEditBudgetLabel);
        editBudgetLabelEditText = (EditText) editMoneyView.findViewById(R.id.editTextEditBudgetLabel);
        editSavingsUpToNowTextLabel = (TextView) editMoneyView.findViewById(R.id.textViewEditSavingsUpToNow);
        editSavingsUpToNowEditText = (EditText) editMoneyView.findViewById(R.id.editTextNumberEditSavingsUpToNow);
        editEndGoalTextView = (TextView) editMoneyView.findViewById(R.id.textViewEditEndGoal);
        editEndGoalEditText = (EditText) editMoneyView.findViewById(R.id.editTextNumberEditEndGoal);
        editDateTextView = (TextView) editMoneyView.findViewById(R.id.textViewEditDueDate);
        editDueDateSelectTextView = (TextView) editMoneyView.findViewById(R.id.textViewEditDueDateSelect);
        cancelEditBudgetButton = (Button) editMoneyView.findViewById(R.id.buttonCancelEditBudget);
        saveEditBudgetButton = (Button) editMoneyView.findViewById(R.id.buttonSaveEditBudget);

        dialogBuilder.setView(editMoneyView);
        dialog = dialogBuilder.create();
        dialog.show();

        cancelEditBudgetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close dialog
                dialog.dismiss();
            }
        });

        saveEditBudgetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
    public void deleteBudgetItemDialog(View view){
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        TextView deleteBudgetItemTextView;
        Button deleteBudgetItemCancelButton, deleteBudgetItemButton;
        dialogBuilder = new AlertDialog.Builder(this);
        //LayoutInflater inflater = (LayoutInflater)getLayoutInflater.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View deleteBudgetItemView = getLayoutInflater().inflate(R.layout.dialog_delete_budget_item, null);
        deleteBudgetItemTextView = (TextView) deleteBudgetItemView.findViewById(R.id.textViewDeleteBudgetItem);
        deleteBudgetItemCancelButton = (Button) deleteBudgetItemView.findViewById(R.id.buttonCancelDeleteBudgetItem);
        deleteBudgetItemButton = (Button) deleteBudgetItemView.findViewById(R.id.buttonDeleteBudgetItem);

        dialogBuilder.setView(deleteBudgetItemView);
        dialog = dialogBuilder.create();
        dialog.show();

        deleteBudgetItemCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close dialog
                dialog.dismiss();
            }
        });

        deleteBudgetItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
}