package com.homies.homies.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.homies.homies.R;
import com.homies.homies.retrofit.model.UserResponse;

public class MainActivity extends AppCompatActivity {

    Button signUp;
    Button logIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        logIn = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUp);


    }
}