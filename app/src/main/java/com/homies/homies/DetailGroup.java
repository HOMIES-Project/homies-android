package com.homies.homies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.GroupResponse;
import com.homies.homies.services.UserAdminRequest;
import com.homies.homies.services.UserAdminResponse;
import com.homies.homies.services.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailGroup extends AppCompatActivity {

    Button btnAddUser, btnConfirmUser, deleteGroup;
    EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);

        btnAddUser = findViewById(R.id.addUserButton);

        btnAddUser.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    this, R.style.BottonSheetDialogTheme
            );
            View userView = LayoutInflater.from(getApplicationContext())
                    .inflate(
                            R.layout.activity_add_user,
                            (ScrollView)findViewById(R.id.addUserContainer)
                    );

            userInput = userView.findViewById(R.id.ingressUser);
            btnConfirmUser = userView.findViewById(R.id.btnAddUser);
            btnAddUser.setOnClickListener(view1 -> {
                if (userInput.getText().toString().trim().isEmpty()) {
                    String message = getString(R.string.val_required);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                } else {

                    addUser(userAdmin);
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setContentView(userView);
            bottomSheetDialog.show();
        });

    }

    public void addUser() {

        SharedPreferences preferences = this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        Log.e("user","user");
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<List<GroupResponse>> groupResponseCall = ApiClient.getService().getGroup("Bearer " + retrivedToken);
        groupResponseCall.enqueue(new Callback<List<GroupResponse>>() {
            @Override
            public void onResponse(Call<List<GroupResponse>> groupResponseCall, Response<List<GroupResponse>> response) {
                Log.e("error","Hola1");
                if (response.isSuccessful()) {
                    List<GroupResponse> myGroupList = response.body();
                    String[] oneGroup = new String[myGroupList.size()];

                    for (int i = 0; i < myGroupList.size(); i++) {
                        oneGroup[i] = myGroupList.get(i).getGroupName();
                    }

                    recyclerView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, oneGroup));
                    Toast.makeText(getActivity(), "Hola", Toast.LENGTH_LONG).show();
                }else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupResponse>> groupResponseCall, Throwable t) {
                Log.e("error","Hola2");
                Toast.makeText(getActivity(), "An error has occured", Toast.LENGTH_LONG).show();

            }
        });
    }
}