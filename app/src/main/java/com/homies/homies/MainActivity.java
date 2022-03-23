package com.homies.homies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.homies.homies.unused.Adaptador;

public class MainActivity extends AppCompatActivity {
    UserResponse loginResponse;
    Adaptador adaptador;
    Button btnLogin;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.logIn);
        btnRegister = findViewById(R.id.signUp);
        adaptador = new Adaptador(getSupportFragmentManager());

    }
}