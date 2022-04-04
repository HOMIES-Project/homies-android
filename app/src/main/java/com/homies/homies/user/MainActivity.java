package com.homies.homies.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.homies.homies.R;
import com.homies.homies.services.UserResponse;
import com.homies.homies.services.Adaptador;

public class MainActivity extends AppCompatActivity {
    UserResponse loginResponse;
    Adaptador adaptador;
    Button signUp;
    Button logIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        logIn = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUp);
        adaptador = new Adaptador(getSupportFragmentManager());


    }
}