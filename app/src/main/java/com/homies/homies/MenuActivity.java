package com.homies.homies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
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
        }else if(id == R.id.opcion2){
            Toast.makeText(this, "OPCION2", Toast.LENGTH_LONG);
        }else if(id == R.id.opcion3) {
            Toast.makeText(this,"OPCION3", Toast.LENGTH_LONG);

        }

        return true;
    }
}