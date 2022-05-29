package com.example.lifetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        TextView budgetLabel, savingsLabel, percentageLabel, dueDate;
        ProgressBar progressBar;
        Button button;
        FloatingActionButton addButton, minusButton, editButton, deleteButton;
        boolean showButtons=false;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            budgetLabel = itemView.findViewById(R.id.budgetLabel);
            savingsLabel = itemView.findViewById(R.id.savingsLabel);
            dueDate = itemView.findViewById(R.id.dueDate1);
            progressBar = itemView.findViewById(R.id.progressBar);
            percentageLabel = itemView.findViewById(R.id.percentageLabel);
            addButton = itemView.findViewById(R.id.addButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            button = itemView.findViewById(R.id.buttonClick);
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if(!showButtons) {
                        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_baseline_arrow_drop_up_24);
                        addButton.setVisibility(v.VISIBLE);
                        minusButton.setVisibility(v.VISIBLE);
                        editButton.setVisibility(v.VISIBLE);
                        deleteButton.setVisibility(v.VISIBLE);
                        showButtons=true;
                    }else{
                        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_baseline_arrow_drop_down_24);
                        addButton.setVisibility(v.GONE);
                        minusButton.setVisibility(v.GONE);
                        editButton.setVisibility(v.GONE);
                        deleteButton.setVisibility(v.GONE);
                        showButtons=false;
                    }
                }
            });
        }
    }
}
