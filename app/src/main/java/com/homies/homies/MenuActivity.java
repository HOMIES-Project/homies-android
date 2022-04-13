package com.homies.homies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.homies.homies.user.LoginFragment;

import java.util.Set;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.opcion1) {
            Toast.makeText(this,"OPCION1", Toast.LENGTH_LONG);
            Settings();

        }else if(id == R.id.opcion2){
            Toast.makeText(this, "OPCION2", Toast.LENGTH_LONG);
        }else if(id == R.id.opcion3) {
            Toast.makeText(this,"OPCION3", Toast.LENGTH_LONG);

        }

        return true;
    }

    public void Settings() {
        SettingsUser settingsUser = new SettingsUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentGroup, settingsUser);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}