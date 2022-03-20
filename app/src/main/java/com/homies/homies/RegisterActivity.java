package com.homies.homies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ImageView logo;
    TextView login, sign;
    EditText et_user,et_name,et_lastname,et_email,et_password,et_repassword;
    TextInputLayout ip_user,ip_name,ip_lastname,ip_email,ip_password,ip_repassword;
    Button btn_register;
    boolean condition = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Se enlazan los recursos de la interfaz de usuario con las variables en el
        ip_user = findViewById(R.id.ip_user);
        ip_name = findViewById(R.id.ip_name);
        ip_lastname = findViewById(R.id.ip_lastname);
        ip_email = findViewById(R.id.ip_email);
        ip_password = findViewById(R.id.ip_password);
        ip_repassword = findViewById(R.id.ip_repassword);
        logo = findViewById(R.id.logo);
        login = findViewById(R.id.login);
        sign = findViewById(R.id.sign);
        et_user = findViewById(R.id.et_user);
        et_name = findViewById(R.id.et_name);
        et_lastname = findViewById(R.id.et_lastname);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_repassword = findViewById(R.id.et_repassword);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition = true;
                if (condition == true) {
                    if (et_user.getText().toString().trim().length() < 1) {
                        ip_user.setError(getString(R.string.val_username));
                        condition = false;
                    } else {
                        ip_user.setErrorEnabled(false);
                    }
                    if (et_email.getText().toString().trim().length() < 8) {
                        ip_email.setError(getString(R.string.val_email));
                        condition = false;
                    } else {
                        ip_email.setErrorEnabled(false);
                    }
                    if (et_password.getText().toString().trim().length() < 4) {
                        ip_password.setError(getString(R.string.val_pass));
                        condition = false;
                    } else {
                        ip_password.setErrorEnabled(false);
                    }
                    if (et_repassword.getText().toString().trim().length() < 4) {
                        ip_repassword.setError(getString(R.string.val_repass));
                        condition = false;
                    } else {
                        ip_repassword.setErrorEnabled(false);
                    }
                    if(et_password.getText().toString().trim() != et_repassword.getText().toString().trim()){
                        ip_repassword.setError("No coinciden el password y repetir password");
                        condition = false;
                    }
                    else{
                        ip_repassword.setErrorEnabled(false);
                    }
                    if (condition == true) {
                        saveUser((createRequest()));
                        Login(view);
                    }
                }
            }

        });

    }

    public UserRequest createRequest(){
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(et_email.getText().toString());
        userRequest.setPassword(et_password.getText().toString());

        return userRequest;
    }

    public void saveUser(UserRequest userRequest){
        Call<UserResponse> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if(response.isSuccessful() && condition){
                    Toast.makeText(RegisterActivity.this, "Sing Up correctly", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(RegisterActivity.this, "Sing Up failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Sing Up failed"+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void Login (View view) {
        Intent login = new Intent(this,MainActivity.class);
        startActivity(login);
    }

}