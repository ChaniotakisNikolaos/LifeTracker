package com.example.lifetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    ApplicationViewModel applicationViewModel;
    String chosenDate;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.CalendarRecyclerView);
        CalendarRecyclerViewAdapter calendarRecyclerViewAdapter = new CalendarRecyclerViewAdapter();
        ToDoRecyclerViewAdapter toDoRecyclerViewAdapter = new ToDoRecyclerViewAdapter(new ToDoRecyclerViewAdapter.ToDoDiff());
        BudgetRecyclerViewAdapter budgetRecyclerViewAdapter = new BudgetRecyclerViewAdapter(new BudgetRecyclerViewAdapter.BudgetDiff());
        ConcatAdapter concatAdapter = new ConcatAdapter(calendarRecyclerViewAdapter,toDoRecyclerViewAdapter,budgetRecyclerViewAdapter);
        recyclerView.setAdapter(concatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        applicationViewModel = new ViewModelProvider(this.requireActivity()).get(ApplicationViewModel.class);
/*        applicationViewModel.getToDoItemList().observe(getViewLifecycleOwner(),toDoItems -> {
            toDoRecyclerViewAdapter.submitList(toDoItems);
        });
        applicationViewModel.getBudgetItemList().observe(getViewLifecycleOwner(), budgetItems -> {
            budgetRecyclerViewAdapter.submitList(budgetItems);
        });*/


        calendarRecyclerViewAdapter.setOnDateChangedListener(date -> {
            chosenDate = date;
            applicationViewModel.getAllToDoItemsWithDueDate(date).observe(CalendarFragment.this.getViewLifecycleOwner(), toDoItems -> {
                if(chosenDate.equals(date)) {
                    Log.d("Calendar todos", "change observed for date" + date);
                    toDoRecyclerViewAdapter.submitList(toDoItems);
                }
            });
            applicationViewModel.getAllBudgetItemsWithDueDate(date).observe(CalendarFragment.this.getViewLifecycleOwner(), budgetItems -> {
                if(chosenDate.equals(date))  {
                    budgetRecyclerViewAdapter.submitList(budgetItems);
                }
            });
        });

        toDoRecyclerViewAdapter.setApplicationViewModel(applicationViewModel);
        ActivityResultLauncher<Intent> editToDoItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        ToDoItem toDoItem = new ToDoItem(data.getStringExtra(AddToDoItemActivity.EXTRA_DESCRIPTION),data.getStringExtra(AddToDoItemActivity.EXTRA_LABEL),data.getStringExtra(AddToDoItemActivity.EXTRA_DUE_DATE),data.getStringExtra(AddToDoItemActivity.EXTRA_REMINDER), false);
                        toDoItem.setId(data.getIntExtra(AddToDoItemActivity.EXTRA_ID,-1));
                        applicationViewModel.update(toDoItem);
                    }
                });
        toDoRecyclerViewAdapter.setMenuItemClickListener(new ToDoRecyclerViewAdapter.MenuItemClickListener() {
            @Override
            public void onEditClick(ToDoItem toDoItem) {
                Intent intent = new Intent(CalendarFragment.this.getActivity(), AddToDoItemActivity.class);
                intent.putExtra("EDIT_MODE",true);
                intent.putExtra(AddToDoItemActivity.EXTRA_DESCRIPTION,toDoItem.getDescription());
                intent.putExtra(AddToDoItemActivity.EXTRA_LABEL,toDoItem.getLabel());
                intent.putExtra(AddToDoItemActivity.EXTRA_REMINDER,toDoItem.getReminder());
                intent.putExtra(AddToDoItemActivity.EXTRA_DUE_DATE,toDoItem.getDueDate());
                intent.putExtra(AddToDoItemActivity.EXTRA_ID,toDoItem.getId());
                editToDoItemActivityResultLauncher.launch(intent);
            }

            @Override
            public void onUpdate(ToDoItem toDoItem) {
                applicationViewModel.update(toDoItem);
            }
        });

        ActivityResultLauncher<Intent> editBudgetItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        BudgetItem budgetItem = new BudgetItem(data.getStringExtra(AddBudgetItemActivity.EXTRA_LABEL),data.getIntExtra(AddBudgetItemActivity.EXTRA_SAVED,0),data.getIntExtra(AddBudgetItemActivity.EXTRA_TOTAL,0),data.getStringExtra(AddBudgetItemActivity.EXTRA_DUE_DATE));
                        budgetItem.setId(data.getIntExtra(AddBudgetItemActivity.EXTRA_ID,-1));
                        Log.d("budget edit",String.valueOf(budgetItem.getId())+" "+budgetItem.getLabel());
                        applicationViewModel.update(budgetItem);
                    }
                });

        budgetRecyclerViewAdapter.setOnClickListener(new BudgetRecyclerViewAdapter.OnClickListener(){
            @Override
            public void onDeleteClick(BudgetItem budgetItem) {
                applicationViewModel.delete(budgetItem);
            }

            @Override
            public void onAddClick(BudgetItem budgetItem) {
                applicationViewModel.update(budgetItem);
            }

            @Override
            public void onEditClick(BudgetItem item) {
                Intent intent = new Intent(CalendarFragment.this.getActivity(),AddBudgetItemActivity.class);
                intent.putExtra(AddBudgetItemActivity.EXTRA_ID,item.getId());
                intent.putExtra(AddBudgetItemActivity.EXTRA_LABEL,item.getLabel());
                intent.putExtra(AddBudgetItemActivity.EXTRA_SAVED,item.getSaved());
                intent.putExtra(AddBudgetItemActivity.EXTRA_TOTAL,item.getTotal());
                intent.putExtra(AddBudgetItemActivity.EXTRA_DUE_DATE,item.getDueDate());
                editBudgetItemActivityResultLauncher.launch(intent);
            }
        });


        return view;
    }

}