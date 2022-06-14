package com.example.lifetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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

import com.google.android.material.navigation.NavigationView;

import java.util.List;

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
    private int labelID = 1;
    private int currentId = 0;
    private String labelName = null;
    private NavigationView navView;
    private Menu m;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)requireActivity()).setDrawerUnlocked();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        ToDoRecyclerViewAdapter toDoRecyclerViewAdapter = new ToDoRecyclerViewAdapter(new ToDoRecyclerViewAdapter.ToDoDiff());
        recyclerView.setAdapter(toDoRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        applicationViewModel = new ViewModelProvider(this.requireActivity()).get(ApplicationViewModel.class);

        showAllToDos(toDoRecyclerViewAdapter);
        toDoRecyclerViewAdapter.setApplicationViewModel(applicationViewModel);

        navView = (requireActivity()).findViewById(R.id.menu_navigation);
        m = navView.getMenu();
        //m.add(Menu.NONE, 0,Menu.NONE,"All To Do").setIcon(R.drawable.ic_baseline_current_label_24).setChecked(true);
        navView.setNavigationItemSelectedListener(menuItem -> {
            currentId = menuItem.getItemId();
            if(!menuItem.isChecked()) {
                for(int mId=0;mId<m.size();mId++){
                    if(m.getItem(mId).isChecked()){
                        Log.d("nnnnnISCHECKED", "checked fOUND");
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
                    if(!m.getItem(0).isChecked()) {
                        Log.d("nnnnnn", String.valueOf(menuItem.getItemId()));
                        toDoRecyclerViewAdapter.submitList(toDoItems);
                        Log.d("nnnnnn", "SUBMIIIIIIIIIIT");
                    }
                });
            } else {
                showAllToDos(toDoRecyclerViewAdapter);
            }
            return false;
        });

        ActivityResultLauncher<Intent> editToDoItemActivityResultLauncher = registerForActivityResult(
                  new ActivityResultContracts.StartActivityForResult(),
                  new ActivityResultCallback<ActivityResult>() {
                      @Override
                      public void onActivityResult(ActivityResult result) {
                          if (result.getResultCode() == Activity.RESULT_OK) {
                              // There are no request codes
                              Intent data = result.getData();
                              ToDoItem toDoItem = new ToDoItem(data.getStringExtra(AddToDoItemActivity.EXTRA_DESCRIPTION),data.getStringExtra(AddToDoItemActivity.EXTRA_LABEL),data.getStringExtra(AddToDoItemActivity.EXTRA_DUE_DATE),data.getStringExtra(AddToDoItemActivity.EXTRA_REMINDER), false);
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

              @Override
              public void onUpdate(ToDoItem toDoItem) {
                  applicationViewModel.update(toDoItem);
              }
          });
        return view;
    }
    public void showAllToDos(ToDoRecyclerViewAdapter toDoRecyclerViewAdapter){
        applicationViewModel.getToDoItemList().observe(getViewLifecycleOwner(), toDoItems -> {
            if(m.getItem(0).isChecked()) {
                Log.d("showAllToDos", "change observed");
                toDoRecyclerViewAdapter.submitList(toDoItems);
            }
            checkIfExistsInMenu(toDoItems);
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity)requireActivity()).setDrawerUnlocked();
    }

    public void checkIfExistsInMenu(List<ToDoItem> toDoItems){
        boolean menuFlag = false;
        String toDoLabel;
        for(ToDoItem todo: toDoItems){
            toDoLabel = todo.getLabel();
            if(!toDoLabel.isEmpty()){
                for(int i=0;i<m.size();i++){
                    if(m.getItem(i).getTitle().equals(toDoLabel)){
                        menuFlag = false;
                        break;
                    }else{
                        menuFlag = true;
                    }
                }
                if(menuFlag){
                    m.add(Menu.NONE, labelID,Menu.NONE,toDoLabel).setIcon(R.drawable.ic_baseline_label_24);
                    labelID++;
                }
            }
        }
        checkIfExistsInLabel(toDoItems);
    }
    public void checkIfExistsInLabel(List<ToDoItem> toDoItems){
        boolean menuFlag = false;
        boolean isUserCurrentlyOn;
        String toDoLabel;
        for(int i=1;i<m.size();i++) {
            for(ToDoItem todo: toDoItems) {
                toDoLabel = todo.getLabel();
                if(m.getItem(i).getTitle().equals(toDoLabel)){
                    menuFlag = false;
                    break;
                }else{
                    menuFlag = true;
                }
            }
            if(menuFlag) {
                isUserCurrentlyOn = m.getItem(i).isChecked();
                m.removeItem(m.getItem(i).getItemId());
                if(isUserCurrentlyOn){
                    m.getItem(0).setIcon(R.drawable.ic_baseline_current_label_24).setChecked(true);
                }
            }
        }
    }
    @Override
    public void onPause(){
        super.onPause();
       // Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setHomeButtonEnabled(false);
        ((MainActivity)requireActivity()).setDrawerLocked();
    }
}