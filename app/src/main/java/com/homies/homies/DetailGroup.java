package com.homies.homies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.homies.homies.services.UserAdmin;
import com.homies.homies.services.UserRequest;

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

            userInput = userView.findViewById(R.id.addUser);
            btnConfirmUser = userView.findViewById(R.id.btnAddUser);
            btnConfirmUser.setOnClickListener(view1 -> {
                if (userInput.getText().toString().trim().isEmpty()) {
                    String message = getString(R.string.val_required);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                } else {
                    UserAdmin userAdmin = new UserAdmin();
                    userAdmin.setId(Integer.parseInt(userInput.getText().toString().trim()));
                    addUsers(userAdmin);
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setContentView(userView);
            bottomSheetDialog.show();
        });

    }

    public void addUsers(UserAdmin userAdmin) {

        SharedPreferences preferences = this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        Log.e("user","user");
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<Void> userRequest = ApiClient.getService().addUser(userAdmin);
        userRequest.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> userAdmin, Response<Void> response) {
                if (response.isSuccessful()) {
                    String message = "Usuario añadido";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}