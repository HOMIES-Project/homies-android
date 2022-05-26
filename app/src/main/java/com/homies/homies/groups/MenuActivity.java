package com.homies.homies.groups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.homies.homies.R;
import com.homies.homies.SplashActivity;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.UserData;
import com.homies.homies.user.SettingsUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;
    private Bitmap bitmap;
    private Menu menu;
    ImageView img1,img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        toolbar = findViewById(R.id.toolbar);
        img1 = toolbar.findViewById(R.id.img1);
        img2 = toolbar.findViewById(R.id.img2);
        setSupportActionBar(toolbar);
        userInfo();
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings();
            }

        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }

        });
    }

    public void Settings() {
        SettingsUser settingsUser = new SettingsUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentGroup, settingsUser);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void logoutUser() {
        Intent intent = new Intent ( MenuActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void getSupportActionBar(Toolbar toolbar) {
    }

    public UserData userInf() {
        UserData userData = new UserData();
        SharedPreferences preferences = this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        userData.setId(userId);

        return userData;
    }

    public void userInfo() {
        SharedPreferences preferences = this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<UserData> userInfo = NetworkConfig.getService().userInfo("Bearer " + retrivedToken, userInf().getId());
        userInfo.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {

                    UserData adslist= response.body();

                    String user = adslist.getUser().getLogin();
                    String name = adslist.getUser().getFirstName();
                    String lastName = adslist.getUser().getLastName();
                    String email = adslist.getUser().getEmail();
                    String photoString = adslist.getPhoto();

                    if(photoString != null){
                        byte[] decodedString = Base64.decode(photoString, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        img1.setImageBitmap(decodedByte);
                    }



                } else {
                    String message = getString(R.string.error_login);
                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                String message = t.getLocalizedMessage();

            }
        });

    }
}