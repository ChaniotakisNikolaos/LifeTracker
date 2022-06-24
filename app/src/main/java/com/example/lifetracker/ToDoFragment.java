package com.example.lifetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ToDoFragment extends Fragment {
    private ApplicationViewModel applicationViewModel;
    private int labelID = 1;
    private int currentId = 0;
    private String labelName = null;
    private Menu m;

    public ToDoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).setDrawerUnlocked();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        //create and set the adapter for the recycler view
        ToDoRecyclerViewAdapter toDoRecyclerViewAdapter = new ToDoRecyclerViewAdapter(new ToDoRecyclerViewAdapter.ToDoDiff());
        recyclerView.setAdapter(toDoRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        applicationViewModel = new ViewModelProvider(this.requireActivity()).get(ApplicationViewModel.class);

        showAllToDos(toDoRecyclerViewAdapter);//display all todos and observe them
        toDoRecyclerViewAdapter.setApplicationViewModel(applicationViewModel);

        NavigationView navView = (requireActivity()).findViewById(R.id.menu_navigation);
        m = navView.getMenu();
        navView.setNavigationItemSelectedListener(menuItem -> {
            currentId = menuItem.getItemId();
            if (!menuItem.isChecked()) {
                for (int mId = 0; mId < m.size(); mId++) {
                    if (m.getItem(mId).isChecked()) {
                        m.getItem(mId).setChecked(false);
                        m.getItem(mId).setIcon(R.drawable.ic_baseline_label_24);
                        break;
                    }
                }
                menuItem.setChecked(true);
                menuItem.setIcon(R.drawable.ic_baseline_current_label_24);
            }
            if (currentId != 0) {
                labelName = menuItem.getTitle().toString();
                applicationViewModel.getAllToDoItemsWithLabel(labelName).observe(getViewLifecycleOwner(), toDoItems -> {
                    if (!m.getItem(0).isChecked()) {
                        toDoRecyclerViewAdapter.submitList(toDoItems);
                    }
                });
            } else {
                showAllToDos(toDoRecyclerViewAdapter);
            }
            return false;
        });

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

        toDoRecyclerViewAdapter.setMenuItemClickListener(new ToDoRecyclerViewAdapter.MenuItemClickListener() {
            @Override
            public void onEditClick(ToDoItem toDoItem) {//called when the user clicks on the edit menu item
                //launch intent with the to do to be edited
                Intent intent = new Intent(ToDoFragment.this.getActivity(), AddToDoItemActivity.class);
                intent.putExtra("EDIT_MODE", true);
                intent.putExtra(AddToDoItemActivity.EXTRA_DESCRIPTION, toDoItem.getDescription());
                intent.putExtra(AddToDoItemActivity.EXTRA_LABEL, toDoItem.getLabel());
                intent.putExtra(AddToDoItemActivity.EXTRA_REMINDER, toDoItem.getReminder());
                intent.putExtra(AddToDoItemActivity.EXTRA_DUE_DATE, toDoItem.getDueDate());
                intent.putExtra(AddToDoItemActivity.EXTRA_ID, toDoItem.getId());
                editToDoItemActivityResultLauncher.launch(intent);
            }

            //called when checking/unchecking a to do item
            @Override
            public void onUpdate(ToDoItem toDoItem) {
                applicationViewModel.update(toDoItem);
            }
        });
        return view;
    }

    public void showAllToDos(ToDoRecyclerViewAdapter toDoRecyclerViewAdapter) {
        applicationViewModel.getToDoItemList().observe(getViewLifecycleOwner(), toDoItems -> {
            if (m.getItem(0).isChecked()) {
                toDoRecyclerViewAdapter.submitList(toDoItems);
            }
            checkIfExistsInMenu(toDoItems);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setDrawerUnlocked();
    }

    public void checkIfExistsInMenu(List<ToDoItem> toDoItems) {
        boolean menuFlag = false;
        String toDoLabel;
        for (ToDoItem todo : toDoItems) {
            toDoLabel = todo.getLabel();
            if (!toDoLabel.isEmpty()) {
                for (int i = 0; i < m.size(); i++) {
                    if (m.getItem(i).getTitle().equals(toDoLabel)) {
                        menuFlag = false;
                        break;
                    } else {
                        menuFlag = true;
                    }
                }
                if (menuFlag) {
                    m.add(Menu.NONE, labelID, Menu.NONE, toDoLabel).setIcon(R.drawable.ic_baseline_label_24);
                    labelID++;
                }
            }
        }
        checkIfExistsInLabel(toDoItems);
    }

    public void checkIfExistsInLabel(List<ToDoItem> toDoItems) {
        boolean menuFlag = false;
        boolean isUserCurrentlyOn;
        String toDoLabel;
        for (int i = 1; i < m.size(); i++) {
            for (ToDoItem todo : toDoItems) {
                toDoLabel = todo.getLabel();
                if (m.getItem(i).getTitle().equals(toDoLabel)) {
                    menuFlag = false;
                    break;
                } else {
                    menuFlag = true;
                }
            }
            if (menuFlag) {
                isUserCurrentlyOn = m.getItem(i).isChecked();
                m.removeItem(m.getItem(i).getItemId());
                if (isUserCurrentlyOn) {
                    m.getItem(0).setIcon(R.drawable.ic_baseline_current_label_24).setChecked(true);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) requireActivity()).setDrawerLocked();
    }
}