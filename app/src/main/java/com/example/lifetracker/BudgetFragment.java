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
 * Use the {@link BudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BudgetFragment() {
        // Required empty public constructor
        Log.d("Understanding","BudgetFragment created");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance(String param1, String param2) {
        BudgetFragment fragment = new BudgetFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView1);
        BudgetRecyclerViewAdapter budgetRecyclerViewAdapter = new BudgetRecyclerViewAdapter(new BudgetRecyclerViewAdapter.BudgetDiff());
        recyclerView.setAdapter(budgetRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ApplicationViewModel applicationViewModel = new ViewModelProvider(this.requireActivity()).get(ApplicationViewModel.class);
        applicationViewModel.getBudgetItemList().observe(getViewLifecycleOwner(), budgetItems -> {
            Log.d("budget items","Change observed");
            budgetRecyclerViewAdapter.submitList(budgetItems);
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
                Intent intent = new Intent(BudgetFragment.this.getActivity(),AddBudgetItemActivity.class);
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