package com.homies.homies.groups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.R;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListExpensesFragment extends Fragment {

    TextView expenses,balance,noExpenses;
    ImageButton addExpense;
    Dialog mDialog;
    Activity activity;
    EditText descriptionInput,expenditureInput ;
    Button btnCreateExpense, btnCancelExpense, btnCancelAction;
    Spinner memberInput,whomInput;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View expense = inflater.inflate(R.layout.fragment_list_expenses, container, false);

        expenses = expense.findViewById(R.id.expenses);
        balance = expense.findViewById(R.id.balance);
        noExpenses = expense.findViewById(R.id.noExpenses);
        addExpense = expense.findViewById(R.id.addExpense);
        mDialog = new Dialog(getContext());
        activity = getActivity();


        dialog();


        ((MenuActivity)getActivity()).getSupportActionBar().setTitle("Lista de Gastos");


        addExpense.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.dialog_add_expense,
                            expense.findViewById(R.id.addExpenseContainer)
                    );
            descriptionInput = bottomSheetView.findViewById(R.id.descriptionInput);
            memberInput = bottomSheetView.findViewById(R.id.memberInput);
            expenditureInput = bottomSheetView.findViewById(R.id.expenditureInput);
            whomInput = bottomSheetView.findViewById(R.id.whomInput);
            btnCreateExpense = bottomSheetView.findViewById(R.id.btnCreateExpense);
            btnCancelExpense = bottomSheetView.findViewById(R.id.btnCancelExpense);
            btnCancelAction = bottomSheetView.findViewById(R.id.btnCancelAction);


            spinnerList();
            btnCreateExpense.setOnClickListener(view1 -> {

                if (memberInput.toString().trim().isEmpty() || whomInput.toString().trim().isEmpty()  ) {
                    String message = getString(R.string.val_user);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {

                    String message = getString(R.string.expenseSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();


                    bottomSheetDialog.dismiss();
                }

            });
            btnCancelExpense.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });
            btnCancelAction.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });



            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        return expense;
    }
    public void dialog(){
        mDialog.setContentView(R.layout.popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    public void spinnerList() {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int userId  = preferences.getInt("USER_ID",0);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        Call<GroupResponse> groupResponseCall = NetworkConfig.getService().groupInfo("Bearer " + retrivedToken,idGroup);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> groupResponseCall, Response<GroupResponse> response) {

                if (response.isSuccessful()) {

                    List<UserData> myGroupList = response.body().getUserdata();
                    String[] oneGroup = new String[myGroupList.size()];
                    Integer[] numberGroup = new Integer[myGroupList.size()];

                    for (int i = 0; i < myGroupList.size(); i++) {
                        oneGroup[i] = myGroupList.get(i).getUser().getLogin();
                        numberGroup[i] = myGroupList.get(i).getId();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, oneGroup);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memberInput.setAdapter(adapter);
                    whomInput.setAdapter(adapter);

                }else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> groupResponseCall, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

            }

        });
    }


}
