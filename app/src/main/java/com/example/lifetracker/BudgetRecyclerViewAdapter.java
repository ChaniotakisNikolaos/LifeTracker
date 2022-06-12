package com.example.lifetracker;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.MessageFormat;

public class BudgetRecyclerViewAdapter extends ListAdapter<BudgetItem,BudgetRecyclerViewAdapter.MyViewHolder> {
    OnClickListener listener;

    public BudgetRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<BudgetItem> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public BudgetRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetRecyclerViewAdapter.MyViewHolder holder, int position) {
        BudgetItem budgetItem = getItem(position);
        if(budgetItem.getDueDate().isEmpty()){
            holder.dueDateBudgetTV.setVisibility(View.INVISIBLE);
        }
        holder.budgetLabel.setText(budgetItem.getLabel());
        holder.savingsLabel.setText(MessageFormat.format("{0}€/{1}€", budgetItem.getSaved(), budgetItem.getTotal()));
        holder.dueDateBudgetTV.setText(budgetItem.getDueDate());
        float percentage = (Float.parseFloat(budgetItem.getSaved())/Float.parseFloat(budgetItem.getTotal()))*100;
        holder.progressBar.setProgress((int) percentage);
        holder.percentageLabel.setText(MessageFormat.format("{0}%", String.valueOf((int) Math.floor(percentage))));

        ConstraintLayout cl = holder.constraintActivity;
        ConstraintSet cs = new ConstraintSet();
        cs.clone(cl);
        cs.setHorizontalBias(R.id.percentageLabel, percentage/100);
        cs.applyTo(cl);

        holder.deleteButton.setOnClickListener(view -> {
            int adapterPosition=holder.getAdapterPosition();//get the current position of the budget item
            if(adapterPosition == RecyclerView.NO_POSITION) return;
            AlertDialog.Builder dialogBuilder;
            AlertDialog dialog;
            TextView deleteBudgetItemTextView;
            Button deleteBudgetItemCancelButton, deleteBudgetItemButton;
            dialogBuilder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View deleteBudgetItemView = inflater.inflate(R.layout.dialog_delete_budget_item, null);
            deleteBudgetItemTextView = (TextView) deleteBudgetItemView.findViewById(R.id.textViewDeleteBudgetItem);
            deleteBudgetItemCancelButton = (Button) deleteBudgetItemView.findViewById(R.id.buttonCancelDeleteBudgetItem);
            deleteBudgetItemButton = (Button) deleteBudgetItemView.findViewById(R.id.buttonDeleteBudgetItem);

            dialogBuilder.setView(deleteBudgetItemView);
            dialog = dialogBuilder.create();
            dialog.show();

            deleteBudgetItemCancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //close dialog
                    dialog.dismiss();
                }
            });

            deleteBudgetItemButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listener.onDeleteClick(getItem(adapterPosition));
                    dialog.dismiss();
                }
            });
        });
    }

    static class BudgetDiff extends DiffUtil.ItemCallback<BudgetItem> {
        @Override
        public boolean areItemsTheSame(@NonNull BudgetItem oldItem, @NonNull BudgetItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull BudgetItem oldItem, @NonNull BudgetItem newItem) {
            return oldItem.getLabel().equals(newItem.getLabel()) && oldItem.getSaved().equals(newItem.getSaved()) && oldItem.getTotal().equals(newItem.getTotal()) && oldItem.getDueDate().equals(newItem.getDueDate());
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView budgetLabel, savingsLabel, percentageLabel, dueDateBudgetTV;
        ConstraintLayout constraintActivity;
        ProgressBar progressBar;
        Button button;
        FloatingActionButton addButton, minusButton, editButton, deleteButton;
        boolean showButtons=false;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintActivity = itemView.findViewById(R.id.activity_constraint);
            budgetLabel = itemView.findViewById(R.id.budgetLabel);
            savingsLabel = itemView.findViewById(R.id.savingsLabel);
            dueDateBudgetTV = itemView.findViewById(R.id.dueDate1);
            progressBar = itemView.findViewById(R.id.progressBar);
            percentageLabel = itemView.findViewById(R.id.percentageLabel);
            addButton = itemView.findViewById(R.id.addButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            button = itemView.findViewById(R.id.buttonClick);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(!showButtons) {
                        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_baseline_arrow_drop_up_24);
                        addButton.setVisibility(View.VISIBLE);
                        minusButton.setVisibility(View.VISIBLE);
                        editButton.setVisibility(View.VISIBLE);
                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), EditBudgetItemActivity.class);
                                v.getContext().startActivity(intent);
                            }
                        });
                        deleteButton.setVisibility(View.VISIBLE);
                        showButtons=true;
                    }else{
                        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_baseline_arrow_drop_down_24);
                        addButton.setVisibility(View.GONE);
                        minusButton.setVisibility(View.GONE);
                        editButton.setVisibility(View.GONE);
                        deleteButton.setVisibility(View.GONE);
                        showButtons=false;
                    }
                }
            });
        }

        public static MyViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_budget_item, parent, false);
            return new BudgetRecyclerViewAdapter.MyViewHolder(view);
        }
    }

    public interface OnClickListener{
        void onDeleteClick(BudgetItem budgetItem);
        void onAddClick(BudgetItem budgetItem);
    }

    public void setOnClickListener(OnClickListener listener){
        this.listener=listener;
    }
}
