package com.homies.homies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ImageView logo;
    TextView login;
    TextView sign;
    EditText et_user;
    EditText et_name;
    EditText et_lastname;
    EditText et_email;
    EditText et_password;
    EditText et_repassword;
    Button btn_register;
    boolean condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Se enlazan los recursos de la interfaz de usuario con las variables en el código
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

                condition = false;
                if(et_user.getText().toString().trim().isEmpty() ||
                        et_password.getText().toString().trim().isEmpty() ||
                        et_repassword.getText().toString().trim().isEmpty() ||
                        et_email.getText().toString().trim().isEmpty())
                { condition = true; }
                saveUser(createRequest());

            }
        });

    }

    public UserRequest createRequest(){
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(et_email.getText().toString());
        userRequest.setEmail(et_password.getText().toString());

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

}