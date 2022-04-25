package com.homies.homies.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.MenuActivity;
import com.homies.homies.R;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.UserRequest;
import com.homies.homies.retrofit.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    Button btnLogin, signUp, logIn, btnRecover;
    EditText inputUser, inputPassword, passInput;
    TextView forgotPassword;
    Activity activity;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View login = inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin = login.findViewById(R.id.loginButton);
        inputUser = login.findViewById(R.id.userInput);
        inputPassword = login.findViewById(R.id.passwordInput);
        forgotPassword = login.findViewById(R.id.forgotPasswordTV);

        progressBar = login.findViewById(R.id.progressBar2);

        activity = getActivity();

        logIn = login.findViewById(R.id.logIn);
        signUp = login.findViewById(R.id.signUp);

        signUp.setOnClickListener(view -> {
            RegisterFragment registerFragment = new RegisterFragment();
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment, registerFragment);
            fragmentTransaction.commit();
            progressBar.setVisibility(View.VISIBLE);
        });

        forgotPassword.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.dialog_layout_botton_sheet,
                            (ScrollView)login.findViewById(R.id.bottonSheetContainer)
                    );

            passInput = bottomSheetView.findViewById(R.id.recoverInput);
            btnRecover = bottomSheetView.findViewById(R.id.btnRecover);
            btnRecover.setOnClickListener(view1 -> {
                if (passInput.getText().toString().trim().isEmpty()) {
                    String message = getString(R.string.val_required);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {

                    String email = passInput.getText().toString().trim();
                    resetPassword(email);
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        btnLogin.setOnClickListener(view -> {
            if (TextUtils.isEmpty(inputUser.getText().toString().trim()) || TextUtils.isEmpty(inputPassword.getText().toString().trim())) {
                String message = getString(R.string.val_required);
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            } else if ((inputPassword.getText().toString().trim().length() < 8)) {
                String message = getString(R.string.val_passMin);
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            } else {
                UserRequest userRequest = new UserRequest();
                userRequest.setUsername(inputUser.getText().toString().trim());
                userRequest.setPassword(inputPassword.getText().toString().trim());

                loginUser(userRequest);
            }
        });
        return login;
    }

    public void loginUser(UserRequest userRequest) {
        Call<UserResponse> loginResponseCall = NetworkConfig.getService().loginUser(userRequest);
        loginResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    preferences.edit().putString("TOKEN",token).apply();
                    preferences.edit().putInt("USER_ID",response.body().getId()).apply();
                    startActivity(new Intent(activity, MenuActivity.class));
                    activity.finish();
                } else {
                    String message = getString(R.string.error_login);
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

    public void resetPassword(String email) {
        Call<Void> resetPasswordResponseCall = NetworkConfig.getService().resetPassword(email);
        resetPasswordResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String message = "Revisa tu correo";
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}