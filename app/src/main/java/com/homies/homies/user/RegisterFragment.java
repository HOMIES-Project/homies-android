package com.homies.homies.user;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.homies.homies.R;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.UserRequest;
import com.homies.homies.user.LoginFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    ImageView logo;
    TextView login, sign;
    EditText et_user, et_name, et_lastname, et_email, et_password, et_repassword;
    TextInputLayout ip_user, ip_name, ip_lastname, ip_email, ip_password, ip_repassword;
    Button btn_register;
    Activity activity ;
    boolean condition = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View register = inflater.inflate(R.layout.fragment_register, container, false);

        //Se enlazan los recursos de la interfaz de usuario con las variables en el
        activity = getActivity();
        ip_user = register.findViewById(R.id.ip_user);
        ip_name = register.findViewById(R.id.ip_name);
        ip_lastname = register.findViewById(R.id.ip_lastname);
        ip_email = register.findViewById(R.id.ip_email);
        ip_password = register.findViewById(R.id.ip_password);
        ip_repassword = register.findViewById(R.id.ip_repassword);
        et_user = register.findViewById(R.id.et_user);
        et_name = register.findViewById(R.id.et_name);
        et_lastname = register.findViewById(R.id.et_lastname);
        et_email = register.findViewById(R.id.et_email);
        et_password = register.findViewById(R.id.et_password);
        et_repassword = register.findViewById(R.id.et_repassword);
        btn_register = register.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition = true;
                validatePassword();
                validateEmail();
                if (et_user.getText().toString().trim().length() < 1) {
                    ip_user.setError(getString(R.string.val_username));
                    condition = false;
                } else {
                    ip_user.setErrorEnabled(false);
                }
                if (condition) {
                    saveUser((createRequest()));
                    Login(view);

                }
            }
        });

        return register;
    }

    public UserRequest createRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(et_email.getText().toString());
        userRequest.setPassword(et_password.getText().toString());
        userRequest.setLogin(et_user.getText().toString());
        userRequest.setFirstName(et_name.getText().toString());
        userRequest.setLastName(et_lastname.getText().toString());
        userRequest.setLangKey("en");

        return userRequest;
    }

    public void saveUser(UserRequest userRequest) {
        Call<Void> userResponseCall = ApiClient.getService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful() && condition) {
                    Toast.makeText(activity, "Sing Up correctly", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, "Sing Up failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "Sing Up failed" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    public void Login(View v) {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void validateEmail() {
        String emailInput = et_email.getText().toString().trim();
        if (emailInput.isEmpty()) {
            ip_email.setError("Field can´t be empty");
            condition = false;
        }
        if (emailInput.length() < 8) {
            ip_password.setError(("Password must be at least 8 characters"));
            condition = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            ip_email.setError("Field must be with email format");
            condition = false;
        } else {
            ip_email.setErrorEnabled(false);
        }
    }

    private void validatePassword() {
        String passwordInput = et_password.getText().toString().trim();
        String confirmPasswordInput = et_repassword.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            ip_password.setError("Field can´t be empty");
            condition = false;
        }
        if (passwordInput.length() < 8) {
            ip_password.setError(("Password must be at least 8 characters"));
            condition = false;
        } else {
            ip_password.setErrorEnabled(false);
        }
        if (!passwordInput.equals(confirmPasswordInput)) {
            condition = false;
            ip_repassword.setError("Password would not be matched");
        } else {
            ip_password.setErrorEnabled(false);
            ip_repassword.setErrorEnabled(false);
        }
    }
}