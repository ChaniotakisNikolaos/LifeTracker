package com.example.lifetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetFragment extends Fragment {

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView1);
        BudgetRecyclerViewAdapter budgetRecyclerViewAdapter = new BudgetRecyclerViewAdapter(new BudgetRecyclerViewAdapter.BudgetDiff());
        recyclerView.setAdapter(budgetRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        ApplicationViewModel applicationViewModel = new ViewModelProvider(this.requireActivity()).get(ApplicationViewModel.class);
        applicationViewModel.getBudgetItemList().observe(getViewLifecycleOwner(), budgetRecyclerViewAdapter::submitList); //Observe changes in budgetItems

        //Handles the reply of the AddBudgetItemActivity after edit
        ActivityResultLauncher<Intent> editBudgetItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        BudgetItem budgetItem;
                        if (data != null) {//This should never be false
                            budgetItem = new BudgetItem(data.getStringExtra(AddBudgetItemActivity.EXTRA_LABEL),
                                    data.getIntExtra(AddBudgetItemActivity.EXTRA_SAVED, 0),
                                    data.getIntExtra(AddBudgetItemActivity.EXTRA_TOTAL, 0),
                                    data.getStringExtra(AddBudgetItemActivity.EXTRA_DUE_DATE));
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
                if (budgetItem.getSaved() >= budgetItem.getTotal()) {
                    Toast toast = Toast.makeText(BudgetFragment.this.getContext(), "Congratulations!\nYou hit your goal for: " + budgetItem.getLabel() + "!", Toast.LENGTH_SHORT);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {//api 30+ does not allow toast customization
                        TextView toastV = toast.getView().findViewById(android.R.id.message);
                        toastV.setGravity(Gravity.CENTER);//Center toast's message text
                    }
                    toast.setGravity(Gravity.TOP, 0, 0);//Put toast at the top of the screen
                    toast.show();
                }
            }

            @Override
            public void onEditClick(BudgetItem item) {//Called when the user clicks the edit button for a BudgetItem
                Intent intent = new Intent(BudgetFragment.this.getActivity(), AddBudgetItemActivity.class);
                intent.putExtra(AddBudgetItemActivity.EXTRA_ID, item.getId());
                intent.putExtra(AddBudgetItemActivity.EXTRA_LABEL, item.getLabel());
                intent.putExtra(AddBudgetItemActivity.EXTRA_SAVED, item.getSaved());
                intent.putExtra(AddBudgetItemActivity.EXTRA_TOTAL, item.getTotal());
                intent.putExtra(AddBudgetItemActivity.EXTRA_DUE_DATE, item.getDueDate());
                editBudgetItemActivityResultLauncher.launch(intent); //Launch AddBudgetItemActivity in edit mode
            }
        });
        return view;
    }

}