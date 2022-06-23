package com.example.lifetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarFragment extends Fragment {

    ApplicationViewModel applicationViewModel;
    String chosenDate;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.CalendarRecyclerView);

        //recycler view adapter for calendar, to do and budget
        CalendarRecyclerViewAdapter calendarRecyclerViewAdapter = new CalendarRecyclerViewAdapter();
        ToDoRecyclerViewAdapter toDoRecyclerViewAdapter = new ToDoRecyclerViewAdapter(new ToDoRecyclerViewAdapter.ToDoDiff());
        BudgetRecyclerViewAdapter budgetRecyclerViewAdapter = new BudgetRecyclerViewAdapter(new BudgetRecyclerViewAdapter.BudgetDiff());

        //concatenate the 3 adapters
        ConcatAdapter concatAdapter = new ConcatAdapter(calendarRecyclerViewAdapter, toDoRecyclerViewAdapter, budgetRecyclerViewAdapter);
        recyclerView.setAdapter(concatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        applicationViewModel = new ViewModelProvider(this.requireActivity()).get(ApplicationViewModel.class);

        //when user clicks on a date show all the todos and budget with that specific due date
        calendarRecyclerViewAdapter.setOnDateChangedListener(date -> {
            chosenDate = date;
            applicationViewModel.getAllToDoItemsWithDueDate(date).observe(CalendarFragment.this.getViewLifecycleOwner(), toDoItems -> {
                if (chosenDate.equals(date)) {
                    toDoRecyclerViewAdapter.submitList(toDoItems);
                }
            });
            applicationViewModel.getAllBudgetItemsWithDueDate(date).observe(CalendarFragment.this.getViewLifecycleOwner(), budgetItems -> {
                if (chosenDate.equals(date)) {
                    budgetRecyclerViewAdapter.submitList(budgetItems);
                }
            });
        });

        toDoRecyclerViewAdapter.setApplicationViewModel(applicationViewModel);

        //Handles the reply of the AddToDoItemActivity after edit
        ActivityResultLauncher<Intent> editToDoItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        ToDoItem toDoItem;
                        if (data != null) {
                            toDoItem = new ToDoItem(data.getStringExtra(AddToDoItemActivity.EXTRA_DESCRIPTION),
                                    data.getStringExtra(AddToDoItemActivity.EXTRA_LABEL),
                                    data.getStringExtra(AddToDoItemActivity.EXTRA_DUE_DATE),
                                    data.getStringExtra(AddToDoItemActivity.EXTRA_REMINDER), false);
                            toDoItem.setId(data.getIntExtra(AddToDoItemActivity.EXTRA_ID, -1));
                            applicationViewModel.update(toDoItem);
                        }
                    }
                });

        //when user clicks any menu item (edit or delete)
        toDoRecyclerViewAdapter.setMenuItemClickListener(new ToDoRecyclerViewAdapter.MenuItemClickListener() {
            @Override
            public void onEditClick(ToDoItem toDoItem) {
                Intent intent = new Intent(CalendarFragment.this.getActivity(), AddToDoItemActivity.class);
                intent.putExtra("EDIT_MODE", true);
                intent.putExtra(AddToDoItemActivity.EXTRA_DESCRIPTION, toDoItem.getDescription());
                intent.putExtra(AddToDoItemActivity.EXTRA_LABEL, toDoItem.getLabel());
                intent.putExtra(AddToDoItemActivity.EXTRA_REMINDER, toDoItem.getReminder());
                intent.putExtra(AddToDoItemActivity.EXTRA_DUE_DATE, toDoItem.getDueDate());
                intent.putExtra(AddToDoItemActivity.EXTRA_ID, toDoItem.getId());
                editToDoItemActivityResultLauncher.launch(intent);
            }

            @Override
            public void onUpdate(ToDoItem toDoItem) {
                applicationViewModel.update(toDoItem);
            }
        });

        //Handles the reply of the AddBudgetItemActivity after edit
        ActivityResultLauncher<Intent> editBudgetItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        BudgetItem budgetItem;
                        if (data != null) {
                            budgetItem = new BudgetItem(data.getStringExtra(AddBudgetItemActivity.EXTRA_LABEL), data.getIntExtra(AddBudgetItemActivity.EXTRA_SAVED, 0), data.getIntExtra(AddBudgetItemActivity.EXTRA_TOTAL, 0), data.getStringExtra(AddBudgetItemActivity.EXTRA_DUE_DATE));
                            budgetItem.setId(data.getIntExtra(AddBudgetItemActivity.EXTRA_ID, -1));
                            applicationViewModel.update(budgetItem);
                        }
                    }
                });

        budgetRecyclerViewAdapter.setOnClickListener(new BudgetRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onDeleteClick(BudgetItem budgetItem) {//Called when the user clicks the delete button for a BudgetItem
                applicationViewModel.delete(budgetItem);
            }

            @Override
            public void onAddClick(BudgetItem budgetItem) {//Called when the user clicks the Add or Subtract money button for a BudgetItem
                applicationViewModel.update(budgetItem);
            }

            @Override
            public void onEditClick(BudgetItem item) {//Called when the user clicks the edit button for a BudgetItem
                Intent intent = new Intent(CalendarFragment.this.getActivity(), AddBudgetItemActivity.class);
                intent.putExtra(AddBudgetItemActivity.EXTRA_ID, item.getId());
                intent.putExtra(AddBudgetItemActivity.EXTRA_LABEL, item.getLabel());
                intent.putExtra(AddBudgetItemActivity.EXTRA_SAVED, item.getSaved());
                intent.putExtra(AddBudgetItemActivity.EXTRA_TOTAL, item.getTotal());
                intent.putExtra(AddBudgetItemActivity.EXTRA_DUE_DATE, item.getDueDate());
                editBudgetItemActivityResultLauncher.launch(intent);
            }
        });


        return view;
    }

}