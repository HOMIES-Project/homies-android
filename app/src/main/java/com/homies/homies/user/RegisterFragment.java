package com.homies.homies.user;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.homies.homies.R;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.UserRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {


    EditText et_user, et_name, et_lastname, et_email, et_password, et_repassword;
    TextInputLayout ip_user, ip_name, ip_lastname, ip_email, ip_password, ip_repassword;
    Button btn_register, signUp, logIn;
    Activity activity;
    boolean condition = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View register = inflater.inflate(R.layout.fragment_register, container, false);

        //The user interface resources are linked with the variables
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

        validateFields();

        btn_register = register.findViewById(R.id.btn_register);
        View snack = register.findViewById(R.id.main_layout);


        logIn = register.findViewById(R.id.logIn);
        signUp = register.findViewById(R.id.signUp);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, loginFragment);
                fragmentTransaction.commit();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition = true;
                validateClickFields();
                if (condition) {
                    saveUser((createRequest()));
                    Login(view);
                }
            }
        });

        return register;
    }

    // Validations when interacting with form fields
    private void validateFields() {
        //region validationUser

        et_user.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_user.getText().toString().trim().length() < 4) {
                    ip_user.setError(getString(R.string.val_username));
                    condition = false;
                } else {
                    ip_user.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
        //region validationEmail
        et_email.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailInput = et_email.getText().toString().trim();
                if (emailInput.isEmpty()) {
                    ip_email.setError(getString(R.string.fieldEmpty));
                    condition = false;
                }
                if (emailInput.length() < 8) {
                    ip_email.setError(getString(R.string.val_email));
                    condition = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    ip_email.setError(getString(R.string.fieldEmail));
                    condition = false;
                } else {
                    ip_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
        //region validatePassword
        et_password.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwordInput = et_password.getText().toString().trim();
                String confirmPasswordInput = et_repassword.getText().toString().trim();
                if (passwordInput.isEmpty()) {
                    ip_password.setError(getString(R.string.fieldEmpty));
                    condition = false;
                }
                if (!passwordInput.equals(confirmPasswordInput)) {
                    condition = false;
                    ip_repassword.setError(getString(R.string.val_pass_matched));
                } else {
                    ip_repassword.setErrorEnabled(false);
                }
                if (passwordInput.length() < 8) {
                    ip_password.setError(getString(R.string.val_passMin));
                    condition = false;
                } else {
                    ip_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //endregion
        //region validateConfirmpassword
        et_repassword.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwordInput = et_password.getText().toString().trim();
                String confirmPasswordInput = et_repassword.getText().toString().trim();

                if (!passwordInput.equals(confirmPasswordInput)) {
                    condition = false;
                    ip_repassword.setError(getString(R.string.val_pass_matched));
                } else {
                    ip_repassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
    }

    //Validations when clicking on the registration button
    private void validateClickFields() {

        if (et_user.getText().toString().trim().length() < 4) {
            ip_user.setError(getString(R.string.val_username));
            condition = false;
        } else {
            ip_user.setErrorEnabled(false);
        }

        String emailInput = et_email.getText().toString().trim();
        if (emailInput.isEmpty()) {
            ip_email.setError(getString(R.string.fieldEmpty));
            condition = false;
        }
        if (emailInput.length() < 8) {
            ip_email.setError(getString(R.string.val_email));
            condition = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            ip_email.setError(getString(R.string.fieldEmail));
            condition = false;
        } else {
            ip_email.setErrorEnabled(false);
        }

        String passwordInput = et_password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            ip_password.setError(getString(R.string.fieldEmpty));
            condition = false;
        }
        if (passwordInput.length() < 8) {
            ip_password.setError(getString(R.string.val_passMin));
            condition = false;
        } else {
            ip_password.setErrorEnabled(false);
        }


        String confirmPasswordInput = et_repassword.getText().toString().trim();

        if (!passwordInput.equals(confirmPasswordInput)) {
            condition = false;
            ip_repassword.setError(getString(R.string.val_pass_matched));
        } else {
            ip_repassword.setErrorEnabled(false);
        }
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
        Call<Void> userResponseCall = NetworkConfig.getService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful() && condition) {
                    Toast.makeText(activity, getString(R.string.signUpCorrect), Toast.LENGTH_LONG).show();
                    showSnackBar();

                } else if (response.code() == 400) {
                    Toast.makeText(activity, getString(R.string.error_user_registered), Toast.LENGTH_LONG).show();

                } else if (response.code() == 500) {
                    Toast.makeText(activity, getString(R.string.error_server), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(activity, getString(R.string.signUpFailed), Toast.LENGTH_LONG).show();
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


    public void showSnackBar() {
        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                getString(R.string.snackbar_confirm_email), Snackbar.LENGTH_INDEFINITE);
        snackBar.getView().setBackgroundColor(Color.parseColor("#64A5C3"));
        snackBar.setAction("Aceptar", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se oculta
            }
        }).show();

    }

}