package com.homies.homies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.GroupRequest;
import com.homies.homies.services.GroupResponse;
import com.homies.homies.services.UserResponse;
import com.homies.homies.user.LoginFragment;
import com.homies.homies.user.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsUser extends Fragment {


    Button btn_delete, btnConfirm;
    Activity activity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settings = inflater.inflate(R.layout.fragment_settings_user, container, false);

        btn_delete = settings.findViewById(R.id.btn_delete);
        activity = getActivity();

        ((MenuActivity)getActivity()).getSupportActionBar().setTitle("Ajustes de Usuario");


        btn_delete.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.activity_layout_botton_deleteuser,
                            settings.findViewById(R.id.bottonDeleteUser)
                    );

            btnConfirm = bottomSheetView.findViewById(R.id.btnConfirm);
            btnConfirm.setOnClickListener(view1 -> {

                deleteUser(userResponse());


                    bottomSheetDialog.dismiss();

            });


            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        return settings;
    }

    public UserResponse userResponse() {
        UserResponse userResponse = new UserResponse();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        userResponse.setId(userId);


        return userResponse;
    }

    public void deleteUser(UserResponse userResponse) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<UserResponse> deleteRequest = ApiClient.getService().deleteUser("Bearer " + retrivedToken, userResponse().getId());
        deleteRequest.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.deleteSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();

                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


}