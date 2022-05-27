package com.example.lifetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BudgetRecyclerViewAdapter extends RecyclerView.Adapter<BudgetRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<BudgetItem> budgetItemList;

    public BudgetRecyclerViewAdapter(Context context, List<BudgetItem> budgetItemList) {
        this.context = context;
        this.budgetItemList = budgetItemList;
    }

    @NonNull
    @Override
    public BudgetRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_budget_item,parent,false);
        return new BudgetRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetRecyclerViewAdapter.MyViewHolder holder, int position) {
        //holder.reminderTextView.setText(BudgetItemList.get(position).getReminder());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView budgetLabel, savingsLabel, progressBar, percentageLabel, dueDate, button, addButton, minusButton, editButton, deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            budgetLabel = itemView.findViewById(R.id.budgetLabel);
            savingsLabel = itemView.findViewById(R.id.savingsLabel);
            dueDate = itemView.findViewById(R.id.dueDate);
            /*progressBar = itemView.findViewById(R.id.progressBar);
            percentageLabel = itemView.findViewById(R.id.percentageLabel);
            dueDate = itemView.findViewById(R.id.dueDate);
            button = itemView.findViewById(R.id.button);
            addButton = itemView.findViewById(R.id.addButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);*/
        }
    }
}
