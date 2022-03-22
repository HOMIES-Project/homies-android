package com.homies.homies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText inputUser, inputPassword;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.loginButton);
        inputUser = findViewById(R.id.userInput);
        inputPassword = findViewById(R.id.passwordInput);
        forgotPassword = findViewById(R.id.forgotPasswordTV);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LoginActivity.this));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(inputUser.getText().toString()) || TextUtils.isEmpty(inputPassword.getText().toString())) {
                    String message = "All inputs required ..";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if ((inputPassword.getText().toString().length() < 4) || inputPassword.getText().toString().length() > 100) {
                    String message = "Password must have between 4 and 100 characters ...";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    UserRequest userRequest = new UserRequest();
                    userRequest.setUsername(inputUser.getText().toString());
                    userRequest.setPassword(inputPassword.getText().toString());

                    loginUser(userRequest);
                }
            }
        });
    }

    public void loginUser(UserRequest userRequest) {
        Call<UserResponse> loginResponseCall = ApiClient.getService().loginUser(userRequest);
        loginResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    String message = "An error ocurred please try again later ...";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}