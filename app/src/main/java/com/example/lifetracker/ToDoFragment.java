package com.example.lifetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoFragment extends Fragment {
    private ApplicationViewModel applicationViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ToDoFragment() {
        // Required empty public constructor
        Log.d("Understanding","ToDoFragment created");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToDoFragment newInstance(String param1, String param2) {
        ToDoFragment fragment = new ToDoFragment();
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
        //AppDatabase db = Room.databaseBuilder(this.getActivity().getApplicationContext(),AppDatabase.class, "life-tracker-db").build();
        //toDoItemArrayList = db.dao().getToDoItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ToDoRecyclerViewAdapter toDoRecyclerViewAdapter = new ToDoRecyclerViewAdapter(new ToDoRecyclerViewAdapter.ToDoDiff());
        recyclerView.setAdapter(toDoRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        applicationViewModel = new ViewModelProvider(this.requireActivity()).get(ApplicationViewModel.class);
        applicationViewModel.getToDoItemList().observe(getViewLifecycleOwner(), toDoItems -> {
            toDoRecyclerViewAdapter.submitList(toDoItems);
            Log.d("test","change observed");
        });
        toDoRecyclerViewAdapter.setApplicationViewModel(applicationViewModel);
        ActivityResultLauncher<Intent> editToDoItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            ToDoItem toDoItem = new ToDoItem(data.getStringExtra(AddToDoItemActivity.EXTRA_DESCRIPTION),data.getStringExtra(AddToDoItemActivity.EXTRA_LABEL),data.getStringExtra(AddToDoItemActivity.EXTRA_DUE_DATE),data.getStringExtra(AddToDoItemActivity.EXTRA_REMINDER));
                            toDoItem.setId(data.getIntExtra(AddToDoItemActivity.EXTRA_ID,-1));
                            applicationViewModel.update(toDoItem);
                        }
                    }
                });
        toDoRecyclerViewAdapter.setMenuItemClickListener(new ToDoRecyclerViewAdapter.MenuItemClickListener() {
            @Override
            public void onEditClick(ToDoItem toDoItem) {
                Intent intent = new Intent(ToDoFragment.this.getActivity(), AddToDoItemActivity.class);
                intent.putExtra("EDIT_MODE",true);
                intent.putExtra(AddToDoItemActivity.EXTRA_DESCRIPTION,toDoItem.getDescription());
                intent.putExtra(AddToDoItemActivity.EXTRA_LABEL,toDoItem.getLabel());
                intent.putExtra(AddToDoItemActivity.EXTRA_REMINDER,toDoItem.getReminder());
                intent.putExtra(AddToDoItemActivity.EXTRA_DUE_DATE,toDoItem.getDueDate());
                intent.putExtra(AddToDoItemActivity.EXTRA_ID,toDoItem.getId());
                editToDoItemActivityResultLauncher.launch(intent);
            }
        });
        return view;
    }
}