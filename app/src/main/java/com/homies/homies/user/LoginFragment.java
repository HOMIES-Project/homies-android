package com.homies.homies.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.homies.homies.MenuActivity;
import com.homies.homies.R;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.UserRequest;
import com.homies.homies.services.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    Button btnLogin;
    EditText inputUser, inputPassword;
    TextView forgotPassword;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View login = inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin = login.findViewById(R.id.loginButton);
        inputUser = login.findViewById(R.id.userInput);
        inputPassword = login.findViewById(R.id.passwordInput);
        forgotPassword = login.findViewById(R.id.forgotPasswordTV);
        activity = getActivity();

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
                    String message = getString(R.string.val_required);
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                } else if ((inputPassword.getText().toString().length() < 8) || inputPassword.getText().toString().length() > 100) {
                    String message = getString(R.string.val_passMin);
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                } else {
                    UserRequest userRequest = new UserRequest();
                    userRequest.setUsername(inputUser.getText().toString());
                    userRequest.setPassword(inputPassword.getText().toString());

                    loginUser(userRequest);
                }
            }
        });
        return login;
    }


    public void loginUser(UserRequest userRequest) {
        Call<UserResponse> loginResponseCall = ApiClient.getService().loginUser(userRequest);
        loginResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    startActivity(new Intent(activity, MenuActivity.class));
                    activity.finish();
                } else {
                    String message = "An error ocurred please try again later ...";
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}