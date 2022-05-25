package com.homies.homies.groups;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.homies.homies.R;

public class ListExpensesFragment extends Fragment {

    TextView expenses,balance,noExpenses;
    ImageButton addExpense;
    Dialog mDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View expense = inflater.inflate(R.layout.fragment_list_expenses, container, false);

        expenses = expense.findViewById(R.id.expenses);
        balance = expense.findViewById(R.id.balance);
        noExpenses = expense.findViewById(R.id.noExpenses);
        addExpense = expense.findViewById(R.id.addExpense);
        mDialog = new Dialog(getContext());

        dialog();


        ((MenuActivity)getActivity()).getSupportActionBar().setTitle("Lista de Gastos");


        return expense;
    }
    public void dialog(){
        mDialog.setContentView(R.layout.popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }
}
