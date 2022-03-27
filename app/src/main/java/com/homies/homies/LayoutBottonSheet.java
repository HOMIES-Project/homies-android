package com.homies.homies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.homies.homies.services.ApiClient;
import com.homies.homies.services.UserRequest;
import com.homies.homies.services.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LayoutBottonSheet extends AppCompatActivity {

    Button btnRecover;
    EditText passInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_botton_sheet);

        passInput = findViewById(R.id.recoverInput);
        btnRecover = findViewById(R.id.recoverButton);

        btnRecover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (TextUtils.isEmpty(passInput.getText().toString())) {
                            String message = getString(R.string.val_required);
                            Toast.makeText(LayoutBottonSheet.this, message, Toast.LENGTH_SHORT).show();
                        } else if (passInput.getText().toString().length() < 8 || passInput.getText().toString().length() > 100){
                            String message = getString(R.string.val_passMin);
                            Toast.makeText(LayoutBottonSheet.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            UserRequest userRequest = new UserRequest();
                            userRequest.setPassword(passInput.getText().toString());
                            resetPassword(userRequest);
                        }
                    }
                });
    }

    public void resetPassword(UserRequest userRequest) {
        Call<UserResponse> resetPasswordResponseCall = ApiClient.getService().resetPassword(userRequest);
        resetPasswordResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(LayoutBottonSheet.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(LayoutBottonSheet.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}