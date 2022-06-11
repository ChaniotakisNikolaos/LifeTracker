package com.example.lifetracker;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
    boolean toShowAll = true;
    private int labelID = 1;
    private String labelName = null;
    NavigationView navView;
    Menu m;
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
        navView = (requireActivity()).findViewById(R.id.menu_navigation);
        m = navView.getMenu();
        m.add(0, 0,0,"All To Do").setIcon(R.drawable.ic_baseline_label_24);
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

        Log.d("cccccctest", "nexttttttttttttttttttt");
        showAllToDos(toDoRecyclerViewAdapter);
        toDoRecyclerViewAdapter.setApplicationViewModel(applicationViewModel);
        //NavigationView navigationView = requireActivity().findViewById(R.id.menu_navigation);
        navView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            for(int j=0;j<m.size();j++) {
                if(j != id) {
                    Log.d("ffffff", "1111111111111   "+j);
                    m.getItem(j).setIcon(R.drawable.ic_baseline_label_24);
                }
                else {
                    Log.d("ffffff", "0000000000000     "+id);
                    menuItem.setIcon(R.drawable.ic_baseline_current_label_24);
                }
            }
            if (id != 0) {
                labelName = menuItem.getTitle().toString();
                applicationViewModel.getAllToDoItemsWithLabel(labelName).observe(getViewLifecycleOwner(), toDoItems -> {
                    toDoRecyclerViewAdapter.submitList(toDoItems);
                    Log.d("cccccctest", "change");
                });
            } else {
                Log.d("cccccctest", menuItem.getTitle().toString());
                showAllToDos(toDoRecyclerViewAdapter);
                //toDoRecyclerViewAdapter.setApplicationViewModel(applicationViewModel);
            }
            return true;
        });
        return view;
    }
    public void showAllToDos(ToDoRecyclerViewAdapter toDoRecyclerViewAdapter){
        applicationViewModel.getToDoItemList().observe(getViewLifecycleOwner(), toDoItems -> {
            toDoRecyclerViewAdapter.submitList(toDoItems);
            Log.d("cccccctest", "change observed");
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
                    m.add(0, labelID,0,toDoLabel).setIcon(R.drawable.ic_baseline_label_24);
                    labelID++;
                }
            }
        }
        checkIfExistsInLabel(toDoItems);
    }
    public void checkIfExistsInLabel(List<ToDoItem> toDoItems){
        boolean menuFlag = false;
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
                m.removeItem(m.getItem(i).getItemId());
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