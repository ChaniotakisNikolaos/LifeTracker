package com.example.lifetracker;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.MessageFormat;

public class BudgetRecyclerViewAdapter extends ListAdapter<BudgetItem, BudgetRecyclerViewAdapter.MyViewHolder> {
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
        if (budgetItem.getDueDate().isEmpty()) {
            holder.dueDateBudgetTV.setVisibility(View.INVISIBLE);
        }
        holder.budgetLabel.setText(budgetItem.getLabel());
        //set text for the amount of money the user saved and the amount of total money to be saved, separated by "/"
        holder.savingsLabel.setText(MessageFormat.format("{0}€/{1}€", budgetItem.getSaved(), budgetItem.getTotal()));
        holder.dueDateBudgetTV.setText(budgetItem.getDueDate());

        //percentage contains the saved money/total amount in order to make a progress bar with a percentage label on top
        float percentage = (float) budgetItem.getSaved() / budgetItem.getTotal() * 100;
        //the percentage will not surpass the 100%
        if (percentage > 100) {
            percentage = 100;
        }

        holder.progressBar.setProgress((int) percentage);
        //the percentage label has a value followed by "%"
        holder.percentageLabel.setText(MessageFormat.format("{0}%", String.valueOf((int) percentage)));

        //change the horizontal bias of the percentage label(with its new value)
        ConstraintLayout cl = holder.constraintActivity;
        ConstraintSet cs = new ConstraintSet();
        cs.clone(cl);
        cs.setHorizontalBias(R.id.percentageLabel, percentage / 100);
        cs.applyTo(cl);

        //when user clicks on the add button an alert dialog will pop up
        holder.addButton.setOnClickListener(view -> {
            int adapterPosition = holder.getBindingAdapterPosition();//get the current position of the budget item
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            //alert dialog for adding money
            AlertDialog.Builder dialogBuilder;
            AlertDialog dialog;
            EditText addMoneyEditText;
            Button addMoneyCancelButton, addMoneySaveButton;
            dialogBuilder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addMoneyView = inflater.inflate(R.layout.dialog_add_money, null);
            addMoneyEditText = addMoneyView.findViewById(R.id.editTextAddAmountToBudget);
            addMoneyCancelButton = addMoneyView.findViewById(R.id.buttonCancelAddMoney);
            addMoneySaveButton = addMoneyView.findViewById(R.id.buttonSaveAddMoney);

            dialogBuilder.setView(addMoneyView);
            dialog = dialogBuilder.create();
            dialog.show();

            addMoneyCancelButton.setOnClickListener(v -> {
                dialog.dismiss();   //close dialog when user clicks cancel
            });

            //when user clicks save
            addMoneySaveButton.setOnClickListener(v -> {
                //copy old BudgetItem into new BudgetItem
                BudgetItem oldBudgetItem1 = getItem(adapterPosition);
                BudgetItem newBudgetItem = new BudgetItem(oldBudgetItem1);
                //if there is no amount of money to be added then show toast message to user, else change the value and dismiss the alert dialog
                if (addMoneyEditText.getText().toString().isEmpty()) {
                    Toast.makeText(cl.getContext(), "You have to insert an amount.", Toast.LENGTH_SHORT).show();
                } else {
                    newBudgetItem.addSaved(Integer.parseInt(addMoneyEditText.getText().toString()));
                    listener.onAddClick(newBudgetItem);
                    dialog.dismiss();
                }
            });
        });

        //when user clicks on the minus button an alert dialog will pop up
        holder.minusButton.setOnClickListener(view -> {
            int adapterPosition = holder.getBindingAdapterPosition();//get the current position of the budget item
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            //alert dialog for adding money
            AlertDialog.Builder dialogBuilder;
            AlertDialog dialog;
            EditText subtractMoneyEditText;
            Button subtractMoneyCancelButton, subtractMoneySaveButton;
            dialogBuilder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View subtractMoneyView = inflater.inflate(R.layout.dialog_subtract_money, null);
            subtractMoneyEditText = subtractMoneyView.findViewById(R.id.editTextSubtractAmountToBudget);
            subtractMoneyCancelButton = subtractMoneyView.findViewById(R.id.buttonCancelSubtractMoney);
            subtractMoneySaveButton = subtractMoneyView.findViewById(R.id.buttonSaveSubtractMoney);

            dialogBuilder.setView(subtractMoneyView);
            dialog = dialogBuilder.create();
            dialog.show();

            subtractMoneyCancelButton.setOnClickListener(v -> {
                dialog.dismiss();   //close dialog when user clicks cancel
            });

            //when user clicks save
            subtractMoneySaveButton.setOnClickListener(v -> {
                //copy old BudgetItem into new BudgetItem
                BudgetItem oldBudgetItem1 = getItem(adapterPosition);
                BudgetItem newBudgetItem = new BudgetItem(oldBudgetItem1);
                //if there is no amount of money to be added then show toast message to user
                //then if the amount of money that the user wishes to subtract is higher than the amount written show a new toast message
                //else change the value and dismiss the alert dialog
                if (subtractMoneyEditText.getText().toString().isEmpty()) {
                    Toast.makeText(cl.getContext(), "You have to insert an amount.", Toast.LENGTH_SHORT).show();
                } else {
                    if ((oldBudgetItem1.getSaved() - Integer.parseInt(subtractMoneyEditText.getText().toString())) < 0) {
                        Toast.makeText(cl.getContext(), "You cannot subtract that amount of money.", Toast.LENGTH_SHORT).show();
                    } else {
                        newBudgetItem.addSaved(-Integer.parseInt(subtractMoneyEditText.getText().toString()));
                        listener.onAddClick(newBudgetItem);
                        dialog.dismiss();
                    }
                }
            });
        });

        //when the user clicks on edit
        holder.editButton.setOnClickListener(view -> {
            int adapterPosition = holder.getBindingAdapterPosition();//get the current position of the budget item
            if (adapterPosition == RecyclerView.NO_POSITION) return;
            listener.onEditClick(getItem(adapterPosition));
        });

        //when user clicks on delete
        holder.deleteButton.setOnClickListener(view -> {
            int adapterPosition = holder.getBindingAdapterPosition();//get the current position of the budget item
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            //alert dialog for adding money
            AlertDialog.Builder dialogBuilder;
            AlertDialog dialog;
            Button deleteBudgetItemCancelButton, deleteBudgetItemButton;
            dialogBuilder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View deleteBudgetItemView = inflater.inflate(R.layout.dialog_delete_budget_item, null);
            deleteBudgetItemCancelButton = deleteBudgetItemView.findViewById(R.id.buttonCancelDeleteBudgetItem);
            deleteBudgetItemButton = deleteBudgetItemView.findViewById(R.id.buttonDeleteBudgetItem);

            dialogBuilder.setView(deleteBudgetItemView);
            dialog = dialogBuilder.create();
            dialog.show();

            deleteBudgetItemCancelButton.setOnClickListener(v -> {
                dialog.dismiss();//close dialog
            });

            //if user is sure he wants to delete the specific budget item,
            //the item will be deleted and the dialog will be dismissed
            deleteBudgetItemButton.setOnClickListener(v -> {
                listener.onDeleteClick(getItem(adapterPosition));
                dialog.dismiss();
            });
        });
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onDeleteClick(BudgetItem budgetItem);

        void onAddClick(BudgetItem budgetItem);

        void onEditClick(BudgetItem item);
    }

    //checks if the items have changed for the updates
    static class BudgetDiff extends DiffUtil.ItemCallback<BudgetItem> {
        @Override
        public boolean areItemsTheSame(@NonNull BudgetItem oldItem, @NonNull BudgetItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull BudgetItem oldItem, @NonNull BudgetItem newItem) {
            return oldItem.getLabel().equals(newItem.getLabel()) && oldItem.getSaved() == newItem.getSaved() && oldItem.getTotal() == newItem.getTotal() && oldItem.getDueDate().equals(newItem.getDueDate());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView budgetLabel, savingsLabel, percentageLabel, dueDateBudgetTV;
        ConstraintLayout constraintActivity;
        ProgressBar progressBar;
        Button button;
        FloatingActionButton addButton, minusButton, editButton, deleteButton;
        boolean showButtons = false;//variable to check if the floating action buttons are visible

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
            //when user clicks on the budget item check if the floating action buttons are visible
            button.setOnClickListener(v -> {
                if (!showButtons) {//if they are not visible, make them visible and change the drawable arrow
                    button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_baseline_arrow_drop_up_24);
                    addButton.setVisibility(View.VISIBLE);
                    minusButton.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    showButtons = true;
                } else {//if they are visible, make them gone and change the drawable arrow
                    button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_baseline_arrow_drop_down_24);
                    addButton.setVisibility(View.GONE);
                    minusButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    showButtons = false;
                }
            });
        }

        public static MyViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_budget_item, parent, false);
            return new BudgetRecyclerViewAdapter.MyViewHolder(view);
        }
    }
}
