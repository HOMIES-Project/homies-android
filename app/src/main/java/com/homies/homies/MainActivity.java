package com.homies.homies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.homies.homies.services.UserResponse;
import com.homies.homies.services.Adaptador;
import com.homies.homies.user.LoginFragment;
import com.homies.homies.user.RegisterFragment;

public class MainActivity extends AppCompatActivity {
    UserResponse loginResponse;
    Adaptador adaptador;
    Button signUp;
    Button logIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logIn = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUp);
        adaptador = new Adaptador(getSupportFragmentManager());


    }
}